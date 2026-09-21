const express = require('express');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const mongoose = require('mongoose');

const env = require('../config/env');
const adminAuth = require('../middleware/adminAuth');
const { seedUserDefaults } = require('../services/seedService');
const { calculateProsperityScore } = require('../services/prosperityCalculator');

// Models
const User = require('../models/User');
const Transaction = require('../models/Transaction');
const Bill = require('../models/Bill');
const Debt = require('../models/Debt');
const Kameti = require('../models/Kameti');
const Envelope = require('../models/Envelope');
const Locker = require('../models/Locker');
const Goal = require('../models/Goal');
const Order = require('../models/Order');
const Prosperity = require('../models/Prosperity');
const Notification = require('../models/Notification');
const CoachMessage = require('../models/CoachMessage');
const CashFlow = require('../models/CashFlow');
const Payslip = require('../models/Payslip');

const rateLimit = require('express-rate-limit');

// Rate limit for admin login: 5 attempts per 15 min per IP
const adminLoginLimiter = rateLimit({
  windowMs: 15 * 60 * 1000,
  max: 5,
  message: { error: 'Too many login attempts. Please try again after 15 minutes.' },
  standardHeaders: true,
  legacyHeaders: false,
  skip: () => process.env.NODE_ENV === 'test'
});

const router = express.Router();

// ==========================================
// 1. AUTHENTICATION
// ==========================================

// POST /api/admin/auth/login
router.post('/auth/login', adminLoginLimiter, async (req, res) => {
  try {
    const { identifier, password } = req.body;
    if (!identifier || !password) {
      return res.status(400).json({ error: 'Phone/CNIC and password are required' });
    }

    // Find user by phone or CNIC
    const user = await User.findOne({
      $or: [{ phone: identifier }, { cnic: identifier }]
    });

    if (!user) {
      return res.status(401).json({ error: 'غلط تفصیلات / Invalid credentials' });
    }

    if (user.role !== 'admin') {
      return res.status(403).json({ error: 'اس پورٹل تک صرف ایڈمن رسائی حاصل کر سکتے ہیں / Admin access required' });
    }

    if (!user.isActive) {
      return res.status(403).json({ error: 'اکاؤنٹ معطل ہے / Account is inactive' });
    }

    const isMatch = await bcrypt.compare(password, user.passwordHash);
    if (!isMatch) {
      return res.status(401).json({ error: 'غلط پاسورڈ / Invalid password' });
    }

    const accessToken = jwt.sign({ userId: user._id, role: 'admin' }, env.JWT_SECRET, { expiresIn: '12h' });
    const refreshToken = jwt.sign({ userId: user._id, tv: user.tokenVersion || 0 }, env.REFRESH_SECRET, { expiresIn: '30d' });

    user.lastLoginAt = new Date();
    await user.save();

    return res.json({
      accessToken,
      refreshToken,
      user: {
        id: user._id.toString(),
        name: user.name,
        urduName: user.urduName || user.name,
        phone: user.phone,
        cnic: user.cnic,
        factory: user.factory,
        role: user.role,
        isActive: user.isActive,
        lastLoginAt: user.lastLoginAt
      }
    });
  } catch (error) {
    console.error('Admin login error:', error);
    return res.status(500).json({ error: 'Internal server error during login' });
  }
});

// All subsequent routes require adminAuth
router.use(adminAuth);

// ==========================================
// 2. DASHBOARD & ANALYTICS STATS
// ==========================================

// GET /api/admin/dashboard/stats
router.get('/dashboard/stats', async (req, res) => {
  try {
    const [
      totalUsers,
      activeUsers,
      totalTransactions,
      transactionSummary,
      debtSummary,
      kametiSummary,
      lockerSummary,
      pendingBillsSummary,
      goalsSummary,
      ordersSummary,
      prosperitySummary,
      factoryAggregates,
      recentTransactions,
      recentUsers
    ] = await Promise.all([
      User.countDocuments({ role: 'user' }),
      User.countDocuments({ role: 'user', isActive: true }),
      Transaction.countDocuments(),
      Transaction.aggregate([
        {
          $group: {
            _id: null,
            totalVolume: { $sum: '$amount' },
            totalExpenses: { $sum: { $cond: [{ $eq: ['$isExpense', true] }, '$amount', 0] } },
            totalIncome: { $sum: { $cond: [{ $eq: ['$isExpense', false] }, '$amount', 0] } }
          }
        }
      ]),
      Debt.aggregate([
        {
          $group: {
            _id: null,
            totalDebtAmount: { $sum: '$totalAmount' },
            totalRemainingDebt: { $sum: '$remainingAmount' },
            totalMonthlyCommitment: { $sum: '$monthlyCommitment' },
            count: { $sum: 1 }
          }
        }
      ]),
      Kameti.aggregate([
        {
          $group: {
            _id: null,
            totalCommittees: { $sum: 1 },
            totalPoolValue: { $sum: { $multiply: ['$monthlyAmount', '$totalMembers'] } },
            monthlyContributionSum: { $sum: '$monthlyAmount' }
          }
        }
      ]),
      Locker.aggregate([
        {
          $group: {
            _id: null,
            totalReserve: { $sum: '$balance' },
            avgBalance: { $avg: '$balance' }
          }
        }
      ]),
      Bill.aggregate([
        {
          $group: {
            _id: '$isPaid',
            totalAmount: { $sum: '$amount' },
            count: { $sum: 1 }
          }
        }
      ]),
      Goal.aggregate([
        {
          $group: {
            _id: '$isCompleted',
            count: { $sum: 1 },
            targetTotal: { $sum: '$targetAmount' },
            savedTotal: { $sum: '$currentAmount' }
          }
        }
      ]),
      Order.aggregate([
        {
          $group: {
            _id: null,
            totalOrders: { $sum: 1 },
            totalRevenue: { $sum: '$totalAmount' },
            totalAdvance: { $sum: '$advancePaid' },
            deliveredCount: { $sum: { $cond: [{ $eq: ['$isDelivered', true] }, 1, 0] } },
            fullyPaidCount: { $sum: { $cond: [{ $eq: ['$isFullyPaid', true] }, 1, 0] } }
          }
        }
      ]),
      Prosperity.aggregate([
        {
          $group: {
            _id: null,
            avgScore: { $avg: '$score' },
            highTier: { $sum: { $cond: [{ $gte: ['$score', 70] }, 1, 0] } },
            midTier: { $sum: { $cond: [{ $and: [{ $gte: ['$score', 30] }, { $lt: ['$score', 70] }] }, 1, 0] } },
            lowTier: { $sum: { $cond: [{ $lt: ['$score', 30] }, 1, 0] } }
          }
        }
      ]),
      User.aggregate([
        { $match: { role: 'user' } },
        { $group: { _id: '$factory', count: { $sum: 1 } } },
        { $sort: { count: -1 } },
        { $limit: 10 }
      ]),
      Transaction.find().sort({ date: -1 }).limit(5).populate('userId', 'name phone factory'),
      User.find({ role: 'user' }).sort({ createdAt: -1 }).limit(5)
    ]);

    const txStats = transactionSummary[0] || { totalVolume: 0, totalExpenses: 0, totalIncome: 0 };
    const debtStats = debtSummary[0] || { totalDebtAmount: 0, totalRemainingDebt: 0, totalMonthlyCommitment: 0, count: 0 };
    const kametiStats = kametiSummary[0] || { totalCommittees: 0, totalPoolValue: 0, monthlyContributionSum: 0 };
    const lockerStats = lockerSummary[0] || { totalReserve: 0, avgBalance: 0 };
    const orderStats = ordersSummary[0] || { totalOrders: 0, totalRevenue: 0, totalAdvance: 0, deliveredCount: 0, fullyPaidCount: 0 };
    const prosperityStats = prosperitySummary[0] || { avgScore: 0, highTier: 0, midTier: 0, lowTier: 0 };

    let unpaidBillsCount = 0;
    let unpaidBillsAmount = 0;
    let paidBillsCount = 0;
    let paidBillsAmount = 0;
    pendingBillsSummary.forEach(b => {
      if (b._id === false) {
        unpaidBillsCount = b.count;
        unpaidBillsAmount = b.totalAmount;
      } else {
        paidBillsCount = b.count;
        paidBillsAmount = b.totalAmount;
      }
    });

    return res.json({
      workers: {
        total: totalUsers,
        active: activeUsers,
        inactive: totalUsers - activeUsers
      },
      transactions: {
        count: totalTransactions,
        volume: txStats.totalVolume,
        expenses: txStats.totalExpenses,
        income: txStats.totalIncome,
        netSavings: txStats.totalIncome - txStats.totalExpenses
      },
      debts: {
        count: debtStats.count,
        totalRemaining: debtStats.totalRemainingDebt,
        monthlyBurden: debtStats.totalMonthlyCommitment
      },
      kametis: {
        activeCommittees: kametiStats.totalCommittees,
        poolValue: kametiStats.totalPoolValue,
        monthlyContributionSum: kametiStats.monthlyContributionSum
      },
      emergencyLocker: {
        totalReserve: lockerStats.totalReserve,
        avgWorkerReserve: Math.round(lockerStats.avgBalance || 0)
      },
      bills: {
        unpaidCount: unpaidBillsCount,
        unpaidAmount: unpaidBillsAmount,
        paidCount: paidBillsCount,
        paidAmount: paidBillsAmount
      },
      orders: {
        count: orderStats.totalOrders,
        revenue: orderStats.totalRevenue,
        delivered: orderStats.deliveredCount
      },
      prosperity: {
        averageScore: Math.round(prosperityStats.avgScore || 0),
        highTier: prosperityStats.highTier,
        midTier: prosperityStats.midTier,
        lowTier: prosperityStats.lowTier
      },
      factoryDistribution: factoryAggregates.map(f => ({
        factory: f._id || 'Unspecified',
        workersCount: f.count
      })),
      recentTransactions: recentTransactions.map(t => ({
        id: t._id.toString(),
        title: t.title,
        amount: t.amount,
        isExpense: t.isExpense,
        category: t.category,
        date: t.date,
        workerName: t.userId ? t.userId.name : 'Unknown',
        factory: t.userId ? t.userId.factory : ''
      })),
      recentWorkers: recentUsers.map(u => ({
        id: u._id.toString(),
        name: u.name,
        phone: u.phone,
        factory: u.factory,
        createdAt: u.createdAt,
        isActive: u.isActive
      }))
    });
  } catch (error) {
    console.error('Stats error:', error);
    return res.status(500).json({ error: 'Error fetching dashboard stats' });
  }
});

// ==========================================
// 3. USER MANAGEMENT (CRUD & 360-VIEW)
// ==========================================

// GET /api/admin/users
router.get('/users', async (req, res) => {
  try {
    const { search, factory, status, page = 1, limit = 20 } = req.query;
    const pageNum = Math.max(1, parseInt(page, 10) || 1);
    const limitNum = Math.max(1, parseInt(limit, 10) || 20);

    const query = {};
    if (status === 'active') query.isActive = true;
    if (status === 'suspended') query.isActive = false;
    if (factory && factory !== 'ALL') query.factory = factory;

    if (search && search.trim()) {
      const s = search.trim();
      query.$or = [
        { name: { $regex: s, $options: 'i' } },
        { urduName: { $regex: s, $options: 'i' } },
        { phone: { $regex: s, $options: 'i' } },
        { cnic: { $regex: s, $options: 'i' } },
        { factoryId: { $regex: s, $options: 'i' } },
        { factory: { $regex: s, $options: 'i' } }
      ];
    }

    const total = await User.countDocuments(query);
    const users = await User.find(query)
      .sort({ createdAt: -1 })
      .skip((pageNum - 1) * limitNum)
      .limit(limitNum);

    // Attach summary stats for each user
    const userIds = users.map(u => u._id);
    const [lockers, prosperities, billsCounts, debtsCounts, kametisCounts] = await Promise.all([
      Locker.find({ userId: { $in: userIds } }),
      Prosperity.find({ userId: { $in: userIds } }),
      Bill.aggregate([
        { $match: { userId: { $in: userIds } } },
        { $group: { _id: '$userId', count: { $sum: 1 }, unpaid: { $sum: { $cond: [{ $eq: ['$isPaid', false] }, 1, 0] } } } }
      ]),
      Debt.aggregate([
        { $match: { userId: { $in: userIds } } },
        { $group: { _id: '$userId', count: { $sum: 1 }, totalRemaining: { $sum: '$remainingAmount' } } }
      ]),
      Kameti.aggregate([
        { $match: { userId: { $in: userIds } } },
        { $group: { _id: '$userId', count: { $sum: 1 } } }
      ])
    ]);

    const lockerMap = new Map(lockers.map(l => [l.userId.toString(), l.balance]));
    const prosperityMap = new Map(prosperities.map(p => [p.userId.toString(), p.score]));
    const billsMap = new Map(billsCounts.map(b => [b._id.toString(), b]));
    const debtsMap = new Map(debtsCounts.map(d => [d._id.toString(), d]));
    const kametisMap = new Map(kametisCounts.map(k => [k._id.toString(), k.count]));

    const formatted = users.map(u => {
      const uid = u._id.toString();
      const bInfo = billsMap.get(uid) || { count: 0, unpaid: 0 };
      const dInfo = debtsMap.get(uid) || { count: 0, totalRemaining: 0 };
      return {
        id: uid,
        name: u.name,
        urduName: u.urduName || u.name,
        phone: u.phone,
        cnic: u.cnic,
        factory: u.factory,
        factoryId: u.factoryId || '',
        jazzCashNumber: u.jazzCashNumber || '',
        role: u.role || 'user',
        isActive: u.isActive,
        preferredLanguage: u.preferredLanguage,
        createdAt: u.createdAt,
        lastLoginAt: u.lastLoginAt,
        lockerBalance: lockerMap.get(uid) || 0,
        prosperityScore: prosperityMap.get(uid) || 0,
        unpaidBills: bInfo.unpaid,
        totalDebts: dInfo.totalRemaining,
        activeKametis: kametisMap.get(uid) || 0
      };
    });

    return res.json({
      items: formatted,
      total,
      page: pageNum,
      pages: Math.ceil(total / limitNum) || 1
    });
  } catch (error) {
    console.error('Fetch users error:', error);
    return res.status(500).json({ error: 'Error fetching users list' });
  }
});

// POST /api/admin/users (Onboard worker)
router.post('/users', async (req, res) => {
  try {
    const { name, urduName, phone, cnic, factory, factoryId, jazzCashNumber, password, role = 'user' } = req.body;

    if (!name || !phone || !cnic || !factory || !password) {
      return res.status(400).json({ error: 'Name, Phone, CNIC, Factory, and Password are required' });
    }

    const cleanPhone = phone.trim();
    const cleanCnic = cnic.trim();

    if (!/^\+92\d{10}$/.test(cleanPhone)) {
      return res.status(400).json({ error: 'Invalid Pakistani phone format (+92XXXXXXXXXX)' });
    }

    if (!/^\d{5}-\d{7}-\d$/.test(cleanCnic)) {
      return res.status(400).json({ error: 'Invalid CNIC format (DDDDD-DDDDDDD-D)' });
    }

    const existingPhone = await User.findOne({ phone: cleanPhone });
    if (existingPhone) {
      return res.status(409).json({ error: 'Phone number already registered' });
    }

    const existingCnic = await User.findOne({ cnic: cleanCnic });
    if (existingCnic) {
      return res.status(409).json({ error: 'CNIC already registered' });
    }

    const passwordHash = await bcrypt.hash(password, 12);
    const newUser = new User({
      name: name.trim(),
      urduName: urduName ? urduName.trim() : name.trim(),
      phone: cleanPhone,
      cnic: cleanCnic,
      passwordHash,
      factory: factory.trim(),
      factoryId: factoryId ? factoryId.trim() : '',
      jazzCashNumber: jazzCashNumber ? jazzCashNumber.trim() : '',
      role: role === 'admin' ? 'admin' : 'user',
      isActive: true
    });

    await newUser.save();
    await seedUserDefaults(newUser._id);

    return res.status(201).json({
      id: newUser._id.toString(),
      name: newUser.name,
      urduName: newUser.urduName,
      phone: newUser.phone,
      cnic: newUser.cnic,
      factory: newUser.factory,
      role: newUser.role,
      isActive: newUser.isActive
    });
  } catch (error) {
    console.error('Create user error:', error);
    return res.status(500).json({ error: 'Error creating user', message: error.message });
  }
});

// GET /api/admin/users/:id (360-degree worker dossier)
router.get('/users/:id', async (req, res) => {
  try {
    const user = await User.findById(req.params.id);
    if (!user) {
      return res.status(404).json({ error: 'User not found' });
    }

    const uid = user._id;
    const [
      envelopes,
      locker,
      prosperity,
      cashFlows,
      transactions,
      bills,
      debts,
      kametis,
      goals,
      orders,
      coachMessages
    ] = await Promise.all([
      Envelope.find({ userId: uid }),
      Locker.findOne({ userId: uid }),
      Prosperity.findOne({ userId: uid }),
      CashFlow.find({ userId: uid }).sort({ month: -1 }).limit(6),
      Transaction.find({ userId: uid }).sort({ date: -1 }).limit(20),
      Bill.find({ userId: uid }).sort({ createdAt: -1 }),
      Debt.find({ userId: uid }).sort({ createdAt: -1 }),
      Kameti.find({ userId: uid }).sort({ createdAt: -1 }),
      Goal.find({ userId: uid }).sort({ createdAt: -1 }),
      Order.find({ userId: uid }).sort({ createdAt: -1 }),
      CoachMessage.find({ userId: uid }).sort({ createdAt: -1 }).limit(10)
    ]);

    return res.json({
      user: {
        id: user._id.toString(),
        name: user.name,
        urduName: user.urduName || user.name,
        phone: user.phone,
        cnic: user.cnic,
        factory: user.factory,
        factoryId: user.factoryId,
        jazzCashNumber: user.jazzCashNumber,
        role: user.role,
        isActive: user.isActive,
        preferredLanguage: user.preferredLanguage,
        createdAt: user.createdAt,
        lastLoginAt: user.lastLoginAt
      },
      envelopes,
      locker: locker || { balance: 0 },
      prosperity: prosperity || { score: 0, pillars: [] },
      cashFlows,
      transactions,
      bills,
      debts,
      kametis,
      goals,
      orders,
      coachMessages
    });
  } catch (error) {
    console.error('Fetch user dossier error:', error);
    return res.status(500).json({ error: 'Error fetching user profile dossier' });
  }
});

// PUT /api/admin/users/:id (Update worker profile)
router.put('/users/:id', async (req, res) => {
  try {
    const { name, urduName, factory, factoryId, phone, cnic, jazzCashNumber, role, isActive, preferredLanguage } = req.body;
    const user = await User.findById(req.params.id);
    if (!user) {
      return res.status(404).json({ error: 'User not found' });
    }

    if (phone && phone !== user.phone) {
      const duplicatePhone = await User.findOne({ phone, _id: { $ne: user._id } });
      if (duplicatePhone) return res.status(409).json({ error: 'Phone number already used by another account' });
      user.phone = phone;
    }

    if (cnic && cnic !== user.cnic) {
      const duplicateCnic = await User.findOne({ cnic, _id: { $ne: user._id } });
      if (duplicateCnic) return res.status(409).json({ error: 'CNIC already used by another account' });
      user.cnic = cnic;
    }

    if (name !== undefined) user.name = name.trim();
    if (urduName !== undefined) user.urduName = urduName.trim();
    if (factory !== undefined) user.factory = factory.trim();
    if (factoryId !== undefined) user.factoryId = factoryId.trim();
    if (jazzCashNumber !== undefined) user.jazzCashNumber = jazzCashNumber.trim();
    if (role !== undefined && ['user', 'admin'].includes(role)) user.role = role;
    if (isActive !== undefined) user.isActive = Boolean(isActive);
    if (preferredLanguage !== undefined) user.preferredLanguage = preferredLanguage;

    await user.save();
    return res.json({ message: 'User updated successfully', user: user.toProfileDto() });
  } catch (error) {
    console.error('Update user error:', error);
    return res.status(500).json({ error: 'Error updating user profile' });
  }
});

// PUT /api/admin/users/:id/status (Toggle activate / suspend)
router.put('/users/:id/status', async (req, res) => {
  try {
    const { isActive } = req.body;
    const user = await User.findByIdAndUpdate(
      req.params.id,
      { $set: { isActive: Boolean(isActive) } },
      { new: true }
    );
    if (!user) return res.status(404).json({ error: 'User not found' });
    return res.json({ message: `User status changed to ${user.isActive ? 'Active' : 'Suspended'}`, isActive: user.isActive });
  } catch (error) {
    return res.status(500).json({ error: 'Error updating user status' });
  }
});

// PUT /api/admin/users/:id/reset-password
router.put('/users/:id/reset-password', async (req, res) => {
  try {
    const { newPassword } = req.body;
    if (!newPassword || newPassword.length < 8) {
      return res.status(400).json({ error: 'Password must be at least 8 characters long' });
    }
    const passwordHash = await bcrypt.hash(newPassword, 12);
    const user = await User.findByIdAndUpdate(
      req.params.id,
      { $set: { passwordHash, tokenVersion: 0, refreshTokenHash: null } },
      { new: true }
    );
    if (!user) return res.status(404).json({ error: 'User not found' });
    return res.json({ message: 'Password reset successfully' });
  } catch (error) {
    return res.status(500).json({ error: 'Error resetting password' });
  }
});

// DELETE /api/admin/users/:id
router.delete('/users/:id', async (req, res) => {
  try {
    const uid = req.params.id;
    const user = await User.findById(uid);
    if (!user) return res.status(404).json({ error: 'User not found' });

    // Cascade delete worker records
    await Promise.all([
      User.findByIdAndDelete(uid),
      Transaction.deleteMany({ userId: uid }),
      Bill.deleteMany({ userId: uid }),
      Debt.deleteMany({ userId: uid }),
      Kameti.deleteMany({ userId: uid }),
      Envelope.deleteMany({ userId: uid }),
      Locker.deleteMany({ userId: uid }),
      Goal.deleteMany({ userId: uid }),
      Order.deleteMany({ userId: uid }),
      Prosperity.deleteMany({ userId: uid }),
      Notification.deleteMany({ userId: uid }),
      CoachMessage.deleteMany({ userId: uid }),
      CashFlow.deleteMany({ userId: uid })
    ]);

    return res.json({ message: 'Worker and all associated records deleted successfully' });
  } catch (error) {
    return res.status(500).json({ error: 'Error deleting user' });
  }
});

// ==========================================
// 4. TRANSACTIONS LEDGER (CRUD & EXPORT)
// ==========================================

// GET /api/admin/transactions
router.get('/transactions', async (req, res) => {
  try {
    const { userId, category, isExpense, search, page = 1, limit = 25 } = req.query;
    const pageNum = Math.max(1, parseInt(page, 10) || 1);
    const limitNum = Math.max(1, parseInt(limit, 10) || 25);

    const query = {};
    if (userId) query.userId = userId;
    if (category && category !== 'ALL') query.category = category;
    if (isExpense !== undefined && isExpense !== 'ALL') query.isExpense = isExpense === 'true';

    if (search && search.trim()) {
      query.$or = [
        { title: { $regex: search.trim(), $options: 'i' } },
        { urduTitle: { $regex: search.trim(), $options: 'i' } }
      ];
    }

    const total = await Transaction.countDocuments(query);
    const items = await Transaction.find(query)
      .sort({ date: -1 })
      .skip((pageNum - 1) * limitNum)
      .limit(limitNum)
      .populate('userId', 'name urduName phone factory');

    return res.json({
      items: items.map(t => ({
        id: t._id.toString(),
        userId: t.userId ? t.userId._id.toString() : null,
        workerName: t.userId ? t.userId.name : 'Deleted Worker',
        workerPhone: t.userId ? t.userId.phone : '',
        factory: t.userId ? t.userId.factory : '',
        title: t.title,
        urduTitle: t.urduTitle,
        amount: t.amount,
        isExpense: t.isExpense,
        category: t.category,
        envelopeId: t.envelopeId,
        paymentMethod: t.paymentMethod,
        date: t.date
      })),
      total,
      page: pageNum,
      pages: Math.ceil(total / limitNum) || 1
    });
  } catch (error) {
    return res.status(500).json({ error: 'Error fetching transactions' });
  }
});

// POST /api/admin/transactions
router.post('/transactions', async (req, res) => {
  try {
    const { userId, title, urduTitle, amount, isExpense, category, envelopeId, paymentMethod, date } = req.body;
    if (!userId || !title || !amount) {
      return res.status(400).json({ error: 'User ID, title, and amount are required' });
    }

    const transaction = new Transaction({
      userId,
      title: title.trim(),
      urduTitle: urduTitle ? urduTitle.trim() : title.trim(),
      amount: Number(amount),
      isExpense: Boolean(isExpense),
      category: category || 'General',
      envelopeId: envelopeId || 'needs',
      paymentMethod: paymentMethod || 'Cash',
      date: date ? new Date(date) : new Date()
    });

    await transaction.save();
    await calculateProsperityScore(userId);

    return res.status(201).json(transaction);
  } catch (error) {
    return res.status(500).json({ error: 'Error creating transaction' });
  }
});

// PUT /api/admin/transactions/:id
router.put('/transactions/:id', async (req, res) => {
  try {
    const { title, urduTitle, amount, isExpense, category, envelopeId, paymentMethod, date } = req.body;
    const transaction = await Transaction.findById(req.params.id);
    if (!transaction) return res.status(404).json({ error: 'Transaction not found' });

    if (title !== undefined) transaction.title = title.trim();
    if (urduTitle !== undefined) transaction.urduTitle = urduTitle.trim();
    if (amount !== undefined) transaction.amount = Number(amount);
    if (isExpense !== undefined) transaction.isExpense = Boolean(isExpense);
    if (category !== undefined) transaction.category = category;
    if (envelopeId !== undefined) transaction.envelopeId = envelopeId;
    if (paymentMethod !== undefined) transaction.paymentMethod = paymentMethod;
    if (date !== undefined) transaction.date = new Date(date);

    await transaction.save();
    return res.json(transaction);
  } catch (error) {
    return res.status(500).json({ error: 'Error updating transaction' });
  }
});

// DELETE /api/admin/transactions/:id
router.delete('/transactions/:id', async (req, res) => {
  try {
    const t = await Transaction.findByIdAndDelete(req.params.id);
    if (!t) return res.status(404).json({ error: 'Transaction not found' });
    return res.json({ message: 'Transaction deleted' });
  } catch (error) {
    return res.status(500).json({ error: 'Error deleting transaction' });
  }
});

// ==========================================
// 5. BILLS & UTILITY INVOICES
// ==========================================

// GET /api/admin/bills
router.get('/bills', async (req, res) => {
  try {
    const { userId, isPaid, billType, page = 1, limit = 25 } = req.query;
    const pageNum = Math.max(1, parseInt(page, 10) || 1);
    const limitNum = Math.max(1, parseInt(limit, 10) || 25);

    const query = {};
    if (userId) query.userId = userId;
    if (isPaid !== undefined && isPaid !== 'ALL') query.isPaid = isPaid === 'true';
    if (billType && billType !== 'ALL') query.billType = billType;

    const total = await Bill.countDocuments(query);
    const bills = await Bill.find(query)
      .sort({ createdAt: -1 })
      .skip((pageNum - 1) * limitNum)
      .limit(limitNum)
      .populate('userId', 'name phone factory');

    return res.json({
      items: bills.map(b => ({
        id: b._id.toString(),
        userId: b.userId ? b.userId._id.toString() : null,
        workerName: b.userId ? b.userId.name : 'Unknown',
        workerPhone: b.userId ? b.userId.phone : '',
        factory: b.userId ? b.userId.factory : '',
        companyName: b.companyName,
        companyUrdu: b.companyUrdu,
        consumerNumber: b.consumerNumber,
        billType: b.billType,
        month: b.month,
        dueDate: b.dueDate,
        amount: b.amount,
        unitsConsumed: b.unitsConsumed,
        isPaid: b.isPaid,
        paidDate: b.paidDate,
        alertTip: b.alertTip
      })),
      total,
      page: pageNum,
      pages: Math.ceil(total / limitNum) || 1
    });
  } catch (error) {
    return res.status(500).json({ error: 'Error fetching bills' });
  }
});

// POST /api/admin/bills
router.post('/bills', async (req, res) => {
  try {
    const { userId, companyName, companyUrdu, consumerNumber, billType, month, dueDate, amount, unitsConsumed, alertTip } = req.body;
    if (!userId || !companyName || !amount) {
      return res.status(400).json({ error: 'User ID, company name, and amount are required' });
    }

    const bill = new Bill({
      userId,
      companyName: companyName.trim(),
      companyUrdu: companyUrdu ? companyUrdu.trim() : companyName.trim(),
      consumerNumber: consumerNumber ? consumerNumber.trim() : '',
      billType: billType || 'Electricity',
      month: month || '',
      dueDate: dueDate || '',
      amount: Number(amount),
      unitsConsumed: Number(unitsConsumed) || 0,
      alertTip: alertTip || ''
    });

    await bill.save();
    return res.status(201).json(bill);
  } catch (error) {
    return res.status(500).json({ error: 'Error creating bill' });
  }
});

// PUT /api/admin/bills/:id
router.put('/bills/:id', async (req, res) => {
  try {
    const { companyName, companyUrdu, consumerNumber, billType, month, dueDate, amount, unitsConsumed, isPaid, alertTip } = req.body;
    const bill = await Bill.findById(req.params.id);
    if (!bill) return res.status(404).json({ error: 'Bill not found' });

    if (companyName !== undefined) bill.companyName = companyName.trim();
    if (companyUrdu !== undefined) bill.companyUrdu = companyUrdu.trim();
    if (consumerNumber !== undefined) bill.consumerNumber = consumerNumber.trim();
    if (billType !== undefined) bill.billType = billType;
    if (month !== undefined) bill.month = month;
    if (dueDate !== undefined) bill.dueDate = dueDate;
    if (amount !== undefined) bill.amount = Number(amount);
    if (unitsConsumed !== undefined) bill.unitsConsumed = Number(unitsConsumed);
    if (alertTip !== undefined) bill.alertTip = alertTip;
    if (isPaid !== undefined) {
      bill.isPaid = Boolean(isPaid);
      if (bill.isPaid && !bill.paidDate) {
        bill.paidDate = new Date().toISOString();
      } else if (!bill.isPaid) {
        bill.paidDate = null;
      }
    }

    await bill.save();
    return res.json(bill);
  } catch (error) {
    return res.status(500).json({ error: 'Error updating bill' });
  }
});

// DELETE /api/admin/bills/:id
router.delete('/bills/:id', async (req, res) => {
  try {
    await Bill.findByIdAndDelete(req.params.id);
    return res.json({ message: 'Bill deleted' });
  } catch (error) {
    return res.status(500).json({ error: 'Error deleting bill' });
  }
});

// ==========================================
// 6. DEBTS & UDHAAR REGISTER
// ==========================================

// GET /api/admin/debts
router.get('/debts', async (req, res) => {
  try {
    const { userId, urgencyLevel, isShariahFriendly, page = 1, limit = 25 } = req.query;
    const pageNum = Math.max(1, parseInt(page, 10) || 1);
    const limitNum = Math.max(1, parseInt(limit, 10) || 25);

    const query = {};
    if (userId) query.userId = userId;
    if (urgencyLevel && urgencyLevel !== 'ALL') query.urgencyLevel = urgencyLevel;
    if (isShariahFriendly !== undefined && isShariahFriendly !== 'ALL') query.isShariahFriendly = isShariahFriendly === 'true';

    const total = await Debt.countDocuments(query);
    const debts = await Debt.find(query)
      .sort({ createdAt: -1 })
      .skip((pageNum - 1) * limitNum)
      .limit(limitNum)
      .populate('userId', 'name phone factory');

    return res.json({
      items: debts.map(d => ({
        id: d._id.toString(),
        userId: d.userId ? d.userId._id.toString() : null,
        workerName: d.userId ? d.userId.name : 'Unknown',
        workerPhone: d.userId ? d.userId.phone : '',
        factory: d.userId ? d.userId.factory : '',
        creditorName: d.creditorName,
        creditorUrdu: d.creditorUrdu,
        relationOrType: d.relationOrType,
        totalAmount: d.totalAmount,
        remainingAmount: d.remainingAmount,
        monthlyCommitment: d.monthlyCommitment,
        urgencyLevel: d.urgencyLevel,
        isShariahFriendly: d.isShariahFriendly,
        repaymentStrategyTip: d.repaymentStrategyTip
      })),
      total,
      page: pageNum,
      pages: Math.ceil(total / limitNum) || 1
    });
  } catch (error) {
    return res.status(500).json({ error: 'Error fetching debts' });
  }
});

// POST /api/admin/debts
router.post('/debts', async (req, res) => {
  try {
    const { userId, creditorName, creditorUrdu, relationOrType, totalAmount, remainingAmount, monthlyCommitment, urgencyLevel, isShariahFriendly, repaymentStrategyTip } = req.body;
    if (!userId || !creditorName || !totalAmount) {
      return res.status(400).json({ error: 'User ID, creditor name, and total amount are required' });
    }

    const debt = new Debt({
      userId,
      creditorName: creditorName.trim(),
      creditorUrdu: creditorUrdu ? creditorUrdu.trim() : creditorName.trim(),
      relationOrType: relationOrType || '',
      totalAmount: Number(totalAmount),
      remainingAmount: remainingAmount !== undefined ? Number(remainingAmount) : Number(totalAmount),
      monthlyCommitment: Number(monthlyCommitment) || 0,
      urgencyLevel: urgencyLevel || 'MEDIUM',
      isShariahFriendly: isShariahFriendly !== undefined ? Boolean(isShariahFriendly) : true,
      repaymentStrategyTip: repaymentStrategyTip || ''
    });

    await debt.save();
    await calculateProsperityScore(userId);
    return res.status(201).json(debt);
  } catch (error) {
    return res.status(500).json({ error: 'Error creating debt' });
  }
});

// PUT /api/admin/debts/:id
router.put('/debts/:id', async (req, res) => {
  try {
    const { creditorName, creditorUrdu, relationOrType, totalAmount, remainingAmount, monthlyCommitment, urgencyLevel, isShariahFriendly, repaymentStrategyTip } = req.body;
    const debt = await Debt.findById(req.params.id);
    if (!debt) return res.status(404).json({ error: 'Debt not found' });

    if (creditorName !== undefined) debt.creditorName = creditorName.trim();
    if (creditorUrdu !== undefined) debt.creditorUrdu = creditorUrdu.trim();
    if (relationOrType !== undefined) debt.relationOrType = relationOrType;
    if (totalAmount !== undefined) debt.totalAmount = Number(totalAmount);
    if (remainingAmount !== undefined) debt.remainingAmount = Math.max(0, Number(remainingAmount));
    if (monthlyCommitment !== undefined) debt.monthlyCommitment = Number(monthlyCommitment);
    if (urgencyLevel !== undefined) debt.urgencyLevel = urgencyLevel;
    if (isShariahFriendly !== undefined) debt.isShariahFriendly = Boolean(isShariahFriendly);
    if (repaymentStrategyTip !== undefined) debt.repaymentStrategyTip = repaymentStrategyTip;

    await debt.save();
    await calculateProsperityScore(debt.userId);
    return res.json(debt);
  } catch (error) {
    return res.status(500).json({ error: 'Error updating debt' });
  }
});

// DELETE /api/admin/debts/:id
router.delete('/debts/:id', async (req, res) => {
  try {
    const debt = await Debt.findByIdAndDelete(req.params.id);
    if (debt) await calculateProsperityScore(debt.userId);
    return res.json({ message: 'Debt deleted' });
  } catch (error) {
    return res.status(500).json({ error: 'Error deleting debt' });
  }
});

// ==========================================
// 7. KAMETIS (COMMITTEES)
// ==========================================

// GET /api/admin/kametis
router.get('/kametis', async (req, res) => {
  try {
    const { userId, page = 1, limit = 25 } = req.query;
    const pageNum = Math.max(1, parseInt(page, 10) || 1);
    const limitNum = Math.max(1, parseInt(limit, 10) || 25);

    const query = {};
    if (userId) query.userId = userId;

    const total = await Kameti.countDocuments(query);
    const kametis = await Kameti.find(query)
      .sort({ createdAt: -1 })
      .skip((pageNum - 1) * limitNum)
      .limit(limitNum)
      .populate('userId', 'name phone factory');

    return res.json({
      items: kametis.map(k => ({
        id: k._id.toString(),
        userId: k.userId ? k.userId._id.toString() : null,
        workerName: k.userId ? k.userId.name : 'Unknown',
        workerPhone: k.userId ? k.userId.phone : '',
        factory: k.userId ? k.userId.factory : '',
        name: k.name,
        urduName: k.urduName,
        monthlyAmount: k.monthlyAmount,
        totalMembers: k.totalMembers,
        myTurnMonth: k.myTurnMonth,
        currentMonth: k.currentMonth,
        payoutAmount: k.payoutAmount || (k.monthlyAmount * k.totalMembers),
        organizer: k.organizer,
        isPaidThisMonth: k.isPaidThisMonth
      })),
      total,
      page: pageNum,
      pages: Math.ceil(total / limitNum) || 1
    });
  } catch (error) {
    return res.status(500).json({ error: 'Error fetching kametis' });
  }
});

// POST /api/admin/kametis
router.post('/kametis', async (req, res) => {
  try {
    const { userId, name, urduName, monthlyAmount, totalMembers, myTurnMonth, currentMonth, organizer } = req.body;
    if (!userId || !name || !monthlyAmount || !totalMembers) {
      return res.status(400).json({ error: 'User ID, name, monthly amount, and total members are required' });
    }

    const kameti = new Kameti({
      userId,
      name: name.trim(),
      urduName: urduName ? urduName.trim() : name.trim(),
      monthlyAmount: Number(monthlyAmount),
      totalMembers: Number(totalMembers),
      myTurnMonth: Number(myTurnMonth) || 1,
      currentMonth: Number(currentMonth) || 1,
      payoutAmount: Number(monthlyAmount) * Number(totalMembers),
      organizer: organizer ? organizer.trim() : ''
    });

    await kameti.save();
    await calculateProsperityScore(userId);
    return res.status(201).json(kameti);
  } catch (error) {
    return res.status(500).json({ error: 'Error creating kameti' });
  }
});

// PUT /api/admin/kametis/:id
router.put('/kametis/:id', async (req, res) => {
  try {
    const { name, urduName, monthlyAmount, totalMembers, myTurnMonth, currentMonth, organizer, isPaidThisMonth } = req.body;
    const kameti = await Kameti.findById(req.params.id);
    if (!kameti) return res.status(404).json({ error: 'Kameti not found' });

    if (name !== undefined) kameti.name = name.trim();
    if (urduName !== undefined) kameti.urduName = urduName.trim();
    if (monthlyAmount !== undefined) kameti.monthlyAmount = Number(monthlyAmount);
    if (totalMembers !== undefined) kameti.totalMembers = Number(totalMembers);
    if (myTurnMonth !== undefined) kameti.myTurnMonth = Number(myTurnMonth);
    if (currentMonth !== undefined) kameti.currentMonth = Number(currentMonth);
    if (organizer !== undefined) kameti.organizer = organizer;
    if (isPaidThisMonth !== undefined) kameti.isPaidThisMonth = Boolean(isPaidThisMonth);
    kameti.payoutAmount = kameti.monthlyAmount * kameti.totalMembers;

    await kameti.save();
    await calculateProsperityScore(kameti.userId);
    return res.json(kameti);
  } catch (error) {
    return res.status(500).json({ error: 'Error updating kameti' });
  }
});

// DELETE /api/admin/kametis/:id
router.delete('/kametis/:id', async (req, res) => {
  try {
    const k = await Kameti.findByIdAndDelete(req.params.id);
    if (k) await calculateProsperityScore(k.userId);
    return res.json({ message: 'Kameti deleted' });
  } catch (error) {
    return res.status(500).json({ error: 'Error deleting kameti' });
  }
});

// ==========================================
// 8. EMERGENCY LOCKER ADJUSTMENT
// ==========================================

// GET /api/admin/lockers
router.get('/lockers', async (req, res) => {
  try {
    const lockers = await Locker.find().populate('userId', 'name phone factory cnic');
    return res.json(lockers.map(l => ({
      id: l._id.toString(),
      userId: l.userId ? l.userId._id.toString() : null,
      workerName: l.userId ? l.userId.name : 'Unknown',
      workerPhone: l.userId ? l.userId.phone : '',
      factory: l.userId ? l.userId.factory : '',
      balance: l.balance,
      updatedAt: l.updatedAt
    })));
  } catch (error) {
    return res.status(500).json({ error: 'Error fetching lockers' });
  }
});

// PUT /api/admin/lockers/:userId
router.put('/lockers/:userId', async (req, res) => {
  try {
    const { balance, adjustmentAmount, note } = req.body;
    let locker = await Locker.findOne({ userId: req.params.userId });
    if (!locker) {
      locker = new Locker({ userId: req.params.userId, balance: 0 });
    }

    if (balance !== undefined) {
      locker.balance = Math.max(0, Number(balance));
    } else if (adjustmentAmount !== undefined) {
      locker.balance = Math.max(0, locker.balance + Number(adjustmentAmount));
    }
    locker.updatedAt = new Date();
    await locker.save();
    await calculateProsperityScore(req.params.userId);

    return res.json({
      message: 'Emergency locker balance updated',
      balance: locker.balance,
      updatedAt: locker.updatedAt
    });
  } catch (error) {
    return res.status(500).json({ error: 'Error updating emergency locker' });
  }
});

// ==========================================
// 9. GOALS & SAVINGS
// ==========================================

// GET /api/admin/goals
router.get('/goals', async (req, res) => {
  try {
    const goals = await Goal.find().sort({ createdAt: -1 }).populate('userId', 'name phone factory');
    return res.json(goals.map(g => ({
      id: g._id.toString(),
      userId: g.userId ? g.userId._id.toString() : null,
      workerName: g.userId ? g.userId.name : 'Unknown',
      workerPhone: g.userId ? g.userId.phone : '',
      factory: g.userId ? g.userId.factory : '',
      title: g.title,
      urduTitle: g.urduTitle,
      targetAmount: g.targetAmount,
      currentAmount: g.currentAmount,
      targetDate: g.targetDate,
      emoji: g.emoji,
      isCompleted: g.isCompleted
    })));
  } catch (error) {
    return res.status(500).json({ error: 'Error fetching goals' });
  }
});

// POST /api/admin/goals
router.post('/goals', async (req, res) => {
  try {
    const { userId, title, urduTitle, targetAmount, currentAmount, targetDate, emoji } = req.body;
    if (!userId || !title || !targetAmount) {
      return res.status(400).json({ error: 'User ID, title, and target amount are required' });
    }
    const current = Number(currentAmount) || 0;
    const target = Number(targetAmount);
    const goal = new Goal({
      userId,
      title: title.trim(),
      urduTitle: urduTitle ? urduTitle.trim() : title.trim(),
      targetAmount: target,
      currentAmount: current,
      targetDate: targetDate || '',
      emoji: emoji || '🎯',
      isCompleted: current >= target
    });
    await goal.save();
    await calculateProsperityScore(userId);
    return res.status(201).json(goal);
  } catch (error) {
    return res.status(500).json({ error: 'Error creating goal' });
  }
});

// PUT /api/admin/goals/:id
router.put('/goals/:id', async (req, res) => {
  try {
    const { title, urduTitle, targetAmount, currentAmount, targetDate, emoji, isCompleted } = req.body;
    const goal = await Goal.findById(req.params.id);
    if (!goal) return res.status(404).json({ error: 'Goal not found' });

    if (title !== undefined) goal.title = title.trim();
    if (urduTitle !== undefined) goal.urduTitle = urduTitle.trim();
    if (targetAmount !== undefined) goal.targetAmount = Number(targetAmount);
    if (currentAmount !== undefined) goal.currentAmount = Number(currentAmount);
    if (targetDate !== undefined) goal.targetDate = targetDate;
    if (emoji !== undefined) goal.emoji = emoji;
    if (isCompleted !== undefined) goal.isCompleted = Boolean(isCompleted);

    await goal.save();
    await calculateProsperityScore(goal.userId);
    return res.json(goal);
  } catch (error) {
    return res.status(500).json({ error: 'Error updating goal' });
  }
});

// DELETE /api/admin/goals/:id
router.delete('/goals/:id', async (req, res) => {
  try {
    const g = await Goal.findByIdAndDelete(req.params.id);
    if (g) await calculateProsperityScore(g.userId);
    return res.json({ message: 'Goal deleted' });
  } catch (error) {
    return res.status(500).json({ error: 'Error deleting goal' });
  }
});

// ==========================================
// 10. MICROENTERPRISE (SIDE HUSTLE) ORDERS
// ==========================================

// GET /api/admin/orders
router.get('/orders', async (req, res) => {
  try {
    const orders = await Order.find().sort({ createdAt: -1 }).populate('userId', 'name phone factory');
    return res.json(orders.map(o => ({
      id: o._id.toString(),
      userId: o.userId ? o.userId._id.toString() : null,
      workerName: o.userId ? o.userId.name : 'Unknown',
      workerPhone: o.userId ? o.userId.phone : '',
      factory: o.userId ? o.userId.factory : '',
      customerName: o.customerName,
      phone: o.phone,
      serviceTitle: o.serviceTitle,
      totalAmount: o.totalAmount,
      advancePaid: o.advancePaid,
      dueDate: o.dueDate,
      isDelivered: o.isDelivered,
      isFullyPaid: o.isFullyPaid
    })));
  } catch (error) {
    return res.status(500).json({ error: 'Error fetching orders' });
  }
});

// POST /api/admin/orders
router.post('/orders', async (req, res) => {
  try {
    const { userId, customerName, phone, serviceTitle, totalAmount, advancePaid, dueDate } = req.body;
    if (!userId || !customerName || !serviceTitle || !totalAmount) {
      return res.status(400).json({ error: 'User ID, customer name, service title, and total amount are required' });
    }
    const total = Number(totalAmount);
    const advance = Number(advancePaid) || 0;
    const order = new Order({
      userId,
      customerName: customerName.trim(),
      phone: phone || '',
      serviceTitle: serviceTitle.trim(),
      totalAmount: total,
      advancePaid: advance,
      dueDate: dueDate || '',
      isDelivered: false,
      isFullyPaid: advance >= total
    });
    await order.save();
    return res.status(201).json(order);
  } catch (error) {
    return res.status(500).json({ error: 'Error creating order' });
  }
});

// PUT /api/admin/orders/:id
router.put('/orders/:id', async (req, res) => {
  try {
    const { customerName, phone, serviceTitle, totalAmount, advancePaid, dueDate, isDelivered, isFullyPaid } = req.body;
    const order = await Order.findById(req.params.id);
    if (!order) return res.status(404).json({ error: 'Order not found' });

    if (customerName !== undefined) order.customerName = customerName.trim();
    if (phone !== undefined) order.phone = phone.trim();
    if (serviceTitle !== undefined) order.serviceTitle = serviceTitle.trim();
    if (totalAmount !== undefined) order.totalAmount = Number(totalAmount);
    if (advancePaid !== undefined) order.advancePaid = Number(advancePaid);
    if (dueDate !== undefined) order.dueDate = dueDate;
    if (isDelivered !== undefined) order.isDelivered = Boolean(isDelivered);
    if (isFullyPaid !== undefined) {
      order.isFullyPaid = Boolean(isFullyPaid);
    } else if (order.advancePaid >= order.totalAmount) {
      order.isFullyPaid = true;
    }

    await order.save();
    return res.json(order);
  } catch (error) {
    return res.status(500).json({ error: 'Error updating order' });
  }
});

// DELETE /api/admin/orders/:id
router.delete('/orders/:id', async (req, res) => {
  try {
    await Order.findByIdAndDelete(req.params.id);
    return res.json({ message: 'Order deleted' });
  } catch (error) {
    return res.status(500).json({ error: 'Error deleting order' });
  }
});

// ==========================================
// 11. PROSPERITY INDEX MANAGEMENT
// ==========================================

// GET /api/admin/prosperity
router.get('/prosperity', async (req, res) => {
  try {
    const scores = await Prosperity.find().populate('userId', 'name phone factory');
    return res.json(scores.map(p => ({
      id: p._id.toString(),
      userId: p.userId ? p.userId._id.toString() : null,
      workerName: p.userId ? p.userId.name : 'Unknown',
      workerPhone: p.userId ? p.userId.phone : '',
      factory: p.userId ? p.userId.factory : '',
      score: p.score,
      maxScore: p.maxScore,
      savingsPct: p.savingsPct,
      debtControlPct: p.debtControlPct,
      safetyShieldPct: p.safetyShieldPct,
      daysRunway: p.daysRunway,
      pillars: p.pillars,
      lastCalculatedAt: p.lastCalculatedAt
    })));
  } catch (error) {
    return res.status(500).json({ error: 'Error fetching prosperity data' });
  }
});

// POST /api/admin/prosperity/recalculate/:userId
router.post('/prosperity/recalculate/:userId', async (req, res) => {
  try {
    const updated = await calculateProsperityScore(req.params.userId);
    return res.json({ message: 'Prosperity score recalculated successfully', prosperity: updated });
  } catch (error) {
    return res.status(500).json({ error: 'Error recalculating prosperity score' });
  }
});

// ==========================================
// 12. BROADCAST NOTIFICATIONS
// ==========================================

// GET /api/admin/notifications
router.get('/notifications', async (req, res) => {
  try {
    const notifications = await Notification.find().sort({ createdAt: -1 }).limit(50).populate('userId', 'name phone factory');
    return res.json(notifications.map(n => ({
      id: n._id.toString(),
      workerName: n.userId ? n.userId.name : 'All / Broadcast',
      titleEnglish: n.titleEnglish,
      titleUrdu: n.titleUrdu,
      descriptionEnglish: n.descriptionEnglish,
      descriptionUrdu: n.descriptionUrdu,
      category: n.category,
      isRead: n.isRead,
      createdAt: n.createdAt
    })));
  } catch (error) {
    return res.status(500).json({ error: 'Error fetching notifications' });
  }
});

// POST /api/admin/notifications/broadcast
router.post('/notifications/broadcast', async (req, res) => {
  try {
    const { titleUrdu, titleEnglish, descriptionUrdu, descriptionEnglish, category, targetFactory, targetUserId } = req.body;

    if (!titleEnglish && !titleUrdu) {
      return res.status(400).json({ error: 'Notification title is required' });
    }

    let targetUsers = [];
    if (targetUserId) {
      targetUsers = await User.find({ _id: targetUserId });
    } else if (targetFactory && targetFactory !== 'ALL') {
      targetUsers = await User.find({ factory: targetFactory, role: 'user' });
    } else {
      targetUsers = await User.find({ role: 'user' });
    }

    const docs = targetUsers.map(u => ({
      userId: u._id,
      titleEnglish: titleEnglish || titleUrdu,
      titleUrdu: titleUrdu || titleEnglish,
      descriptionEnglish: descriptionEnglish || '',
      descriptionUrdu: descriptionUrdu || '',
      category: category || 'SYSTEM',
      isRead: false,
      createdAt: new Date()
    }));

    if (docs.length > 0) {
      await Notification.insertMany(docs);
    }

    return res.status(201).json({
      message: `Broadcast successfully sent to ${docs.length} worker(s)`,
      recipientsCount: docs.length
    });
  } catch (error) {
    console.error('Broadcast error:', error);
    return res.status(500).json({ error: 'Error sending broadcast notifications' });
  }
});

// ==========================================
// 13. AI COACH AUDIT LOGS
// ==========================================

// GET /api/admin/coach/logs
router.get('/coach/logs', async (req, res) => {
  try {
    const messages = await CoachMessage.find()
      .sort({ createdAt: -1 })
      .limit(60)
      .populate('userId', 'name phone factory');

    return res.json(messages.map(m => ({
      id: m._id.toString(),
      workerName: m.userId ? m.userId.name : 'Unknown',
      workerPhone: m.userId ? m.userId.phone : '',
      factory: m.userId ? m.userId.factory : '',
      textUrdu: m.textUrdu,
      textRoman: m.textRoman,
      isFromCoach: m.isFromCoach,
      createdAt: m.createdAt
    })));
  } catch (error) {
    return res.status(500).json({ error: 'Error fetching coach logs' });
  }
});

// ==========================================
// 14. SYSTEM HEALTH & MONITORING
// ==========================================

// GET /api/admin/system/health
router.get('/system/health', async (req, res) => {
  try {
    const isDbConnected = mongoose.connection.readyState === 1;
    const collections = await Promise.all([
      User.countDocuments(),
      Transaction.countDocuments(),
      Bill.countDocuments(),
      Debt.countDocuments(),
      Kameti.countDocuments(),
      Envelope.countDocuments(),
      Locker.countDocuments(),
      Goal.countDocuments(),
      Order.countDocuments(),
      Prosperity.countDocuments()
    ]);

    const memoryUsage = process.memoryUsage();

    return res.json({
      status: isDbConnected ? 'healthy' : 'degraded',
      serverTime: new Date().toISOString(),
      uptimeSeconds: Math.floor(process.uptime()),
      nodeVersion: process.version,
      database: {
        connected: isDbConnected,
        name: mongoose.connection.name || 'khushhaal_db',
        host: mongoose.connection.host || 'localhost'
      },
      memory: {
        rssMb: Math.round(memoryUsage.rss / 1024 / 1024),
        heapUsedMb: Math.round(memoryUsage.heapUsed / 1024 / 1024),
        heapTotalMb: Math.round(memoryUsage.heapTotal / 1024 / 1024)
      },
      collectionCounts: {
        users: collections[0],
        transactions: collections[1],
        bills: collections[2],
        debts: collections[3],
        kametis: collections[4],
        envelopes: collections[5],
        lockers: collections[6],
        goals: collections[7],
        orders: collections[8],
        prosperities: collections[9]
      }
    });
  } catch (error) {
    return res.status(500).json({ error: 'Error getting system health metrics' });
  }
});

// ==========================================
// 15. PAYSLIPS (FACTORY SALARY SLIPS)
// ==========================================

// GET /api/admin/payslips — List all payslips with optional filters
router.get('/payslips', async (req, res) => {
  try {
    const { userId, month, page = 1, limit = 25 } = req.query;
    const pageNum = Math.max(1, parseInt(page, 10) || 1);
    const limitNum = Math.max(1, Math.min(100, parseInt(limit, 10) || 25));

    const query = {};
    if (userId) query.userId = userId;
    if (month) query.month = { $regex: month, $options: 'i' };

    const total = await Payslip.countDocuments(query);
    const payslips = await Payslip.find(query)
      .sort({ createdAt: -1 })
      .skip((pageNum - 1) * limitNum)
      .limit(limitNum)
      .populate('userId', 'name phone factory factoryId');

    return res.json({
      items: payslips.map(p => ({
        id: p._id.toString(),
        userId: p.userId ? p.userId._id.toString() : null,
        workerName: p.userId ? p.userId.name : 'Unknown',
        workerPhone: p.userId ? p.userId.phone : '',
        factory: p.userId ? p.userId.factory : '',
        month: p.month,
        employeeName: p.employeeName,
        employeeId: p.employeeId,
        department: p.department,
        daysPresent: p.daysPresent,
        daysAbsent: p.daysAbsent,
        overtimeHours: p.overtimeHours,
        baseWage: p.baseWage,
        overtimePay: p.overtimePay,
        attendanceBonus: p.attendanceBonus,
        productionBonus: p.productionBonus,
        totalGrossWage: p.totalGrossWage,
        eobiDeduction: p.eobiDeduction,
        messAdvanceDeduction: p.messAdvanceDeduction,
        unionFundDeduction: p.unionFundDeduction,
        totalDeductions: p.totalDeductions,
        netTakeHome: p.netTakeHome,
        paymentStatus: p.paymentStatus,
        creditedDate: p.creditedDate,
        disbursementAccount: p.disbursementAccount,
        createdAt: p.createdAt
      })),
      total,
      page: pageNum,
      pages: Math.ceil(total / limitNum) || 1
    });
  } catch (error) {
    return res.status(500).json({ error: 'Error fetching payslips' });
  }
});

// POST /api/admin/payslips — Create payslip for a worker
router.post('/payslips', async (req, res) => {
  try {
    const {
      userId, month, employeeName, employeeId, department,
      daysPresent, daysAbsent, overtimeHours,
      baseWage, overtimePay, attendanceBonus, productionBonus,
      eobiDeduction, messAdvanceDeduction, unionFundDeduction,
      paymentStatus, creditedDate, disbursementAccount
    } = req.body;

    if (!userId || !month) {
      return res.status(400).json({ error: 'userId and month are required' });
    }

    const gross = (Number(baseWage) || 0) + (Number(overtimePay) || 0) +
      (Number(attendanceBonus) || 0) + (Number(productionBonus) || 0);
    const deductions = (Number(eobiDeduction) || 0) + (Number(messAdvanceDeduction) || 0) +
      (Number(unionFundDeduction) || 0);

    const payslip = new Payslip({
      userId,
      month: month.trim(),
      employeeName: employeeName || '',
      employeeId: employeeId || '',
      department: department || '',
      daysPresent: Number(daysPresent) || 0,
      daysAbsent: Number(daysAbsent) || 0,
      overtimeHours: Number(overtimeHours) || 0,
      baseWage: Number(baseWage) || 0,
      overtimePay: Number(overtimePay) || 0,
      attendanceBonus: Number(attendanceBonus) || 0,
      productionBonus: Number(productionBonus) || 0,
      totalGrossWage: gross,
      eobiDeduction: Number(eobiDeduction) || 0,
      messAdvanceDeduction: Number(messAdvanceDeduction) || 0,
      unionFundDeduction: Number(unionFundDeduction) || 0,
      totalDeductions: deductions,
      netTakeHome: Math.max(0, gross - deductions),
      paymentStatus: paymentStatus || 'زیر عمل (Pending)',
      creditedDate: creditedDate || '',
      disbursementAccount: disbursementAccount || ''
    });

    await payslip.save();
    return res.status(201).json(payslip);
  } catch (error) {
    if (error.code === 11000) {
      return res.status(409).json({ error: 'Payslip for this worker and month already exists' });
    }
    return res.status(500).json({ error: 'Error creating payslip', message: error.message });
  }
});

// PUT /api/admin/payslips/:id — Update a payslip
router.put('/payslips/:id', async (req, res) => {
  try {
    const payslip = await Payslip.findById(req.params.id);
    if (!payslip) return res.status(404).json({ error: 'Payslip not found' });

    const fields = [
      'month', 'employeeName', 'employeeId', 'department',
      'daysPresent', 'daysAbsent', 'overtimeHours',
      'baseWage', 'overtimePay', 'attendanceBonus', 'productionBonus',
      'eobiDeduction', 'messAdvanceDeduction', 'unionFundDeduction',
      'paymentStatus', 'creditedDate', 'disbursementAccount'
    ];

    fields.forEach(field => {
      if (req.body[field] !== undefined) {
        payslip[field] = ['daysPresent','daysAbsent','overtimeHours','baseWage','overtimePay',
          'attendanceBonus','productionBonus','eobiDeduction','messAdvanceDeduction','unionFundDeduction']
          .includes(field) ? Number(req.body[field]) : req.body[field];
      }
    });

    // Recalculate derived fields
    payslip.totalGrossWage = payslip.baseWage + payslip.overtimePay +
      payslip.attendanceBonus + payslip.productionBonus;
    payslip.totalDeductions = payslip.eobiDeduction + payslip.messAdvanceDeduction +
      payslip.unionFundDeduction;
    payslip.netTakeHome = Math.max(0, payslip.totalGrossWage - payslip.totalDeductions);
    payslip.updatedAt = new Date();

    await payslip.save();
    return res.json(payslip);
  } catch (error) {
    return res.status(500).json({ error: 'Error updating payslip' });
  }
});

// DELETE /api/admin/payslips/:id
router.delete('/payslips/:id', async (req, res) => {
  try {
    await Payslip.findByIdAndDelete(req.params.id);
    return res.json({ message: 'Payslip deleted' });
  } catch (error) {
    return res.status(500).json({ error: 'Error deleting payslip' });
  }
});

module.exports = router;
