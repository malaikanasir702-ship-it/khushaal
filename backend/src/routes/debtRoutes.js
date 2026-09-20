const express = require('express');
const authMiddleware = require('../middleware/auth');
const Debt = require('../models/Debt');
const { calculateProsperityScore } = require('../services/prosperityCalculator');

const router = express.Router();

// GET /api/debts
router.get('/', authMiddleware, async (req, res) => {
  try {
    const debts = await Debt.find({ userId: req.userId }).sort({ createdAt: -1 });
    const formatted = debts.map(d => ({
      id: d._id.toString(),
      creditorName: d.creditorName,
      creditorUrdu: d.creditorUrdu || d.creditorName,
      relationOrType: d.relationOrType || '',
      totalAmount: d.totalAmount,
      remainingAmount: d.remainingAmount,
      monthlyCommitment: d.monthlyCommitment,
      urgencyLevel: d.urgencyLevel || 'MEDIUM',
      isShariahFriendly: d.isShariahFriendly,
      repaymentStrategyTip: d.repaymentStrategyTip || ''
    }));
    return res.json(formatted);
  } catch (error) {
    return res.status(500).json({ error: 'Error fetching debts' });
  }
});

// POST /api/debts
router.post('/', authMiddleware, async (req, res) => {
  try {
    const { 
      creditorName, 
      creditorUrdu, 
      relationOrType, 
      totalAmount, 
      remainingAmount, 
      monthlyCommitment, 
      urgencyLevel, 
      isShariahFriendly, 
      repaymentStrategyTip 
    } = req.body;

    if (!creditorName || !creditorName.trim()) {
      return res.status(400).json({ error: 'Creditor name is required' });
    }

    const total = Number(totalAmount);
    if (isNaN(total) || total < 1) {
      return res.status(400).json({ error: 'Total amount must be at least 1' });
    }

    const remaining = remainingAmount !== undefined ? Number(remainingAmount) : total;
    if (isNaN(remaining) || remaining < 0) {
      return res.status(400).json({ error: 'Remaining amount cannot be negative' });
    }

    const debt = new Debt({
      userId: req.userId,
      creditorName: creditorName.trim(),
      creditorUrdu: creditorUrdu ? creditorUrdu.trim() : creditorName.trim(),
      relationOrType: relationOrType ? relationOrType.trim() : '',
      totalAmount: total,
      remainingAmount: remaining,
      monthlyCommitment: Number(monthlyCommitment) || 0,
      urgencyLevel: urgencyLevel || 'MEDIUM',
      isShariahFriendly: isShariahFriendly !== undefined ? Boolean(isShariahFriendly) : true,
      repaymentStrategyTip: repaymentStrategyTip ? repaymentStrategyTip.trim() : ''
    });

    await debt.save();
    await calculateProsperityScore(req.userId);

    return res.status(201).json({
      id: debt._id.toString(),
      creditorName: debt.creditorName,
      creditorUrdu: debt.creditorUrdu,
      relationOrType: debt.relationOrType,
      totalAmount: debt.totalAmount,
      remainingAmount: debt.remainingAmount,
      monthlyCommitment: debt.monthlyCommitment,
      urgencyLevel: debt.urgencyLevel,
      isShariahFriendly: debt.isShariahFriendly,
      repaymentStrategyTip: debt.repaymentStrategyTip
    });
  } catch (error) {
    return res.status(500).json({ error: 'Error creating debt' });
  }
});

// PUT /api/debts/:id/repay
router.put('/:id/repay', authMiddleware, async (req, res) => {
  try {
    const { amount } = req.body;
    const repayment = Number(amount);

    if (isNaN(repayment) || repayment <= 0) {
      return res.status(400).json({ error: 'Repayment amount must be greater than 0' });
    }

    const debt = await Debt.findOne({ _id: req.params.id, userId: req.userId });
    if (!debt) {
      return res.status(404).json({ error: 'Debt not found' });
    }

    if (repayment > debt.remainingAmount) {
      return res.status(400).json({ 
        error: 'Repayment exceeds remaining balance / ادھار کی رقم سے زیادہ ادائیگی ممکن نہیں' 
      });
    }

    debt.remainingAmount = Math.max(0, debt.remainingAmount - repayment);
    await debt.save();

    await calculateProsperityScore(req.userId);

    return res.json({
      id: debt._id.toString(),
      creditorName: debt.creditorName,
      creditorUrdu: debt.creditorUrdu,
      relationOrType: debt.relationOrType,
      totalAmount: debt.totalAmount,
      remainingAmount: debt.remainingAmount,
      monthlyCommitment: debt.monthlyCommitment,
      urgencyLevel: debt.urgencyLevel,
      isShariahFriendly: debt.isShariahFriendly,
      repaymentStrategyTip: debt.repaymentStrategyTip
    });
  } catch (error) {
    return res.status(500).json({ error: 'Error repaying debt' });
  }
});

module.exports = router;
