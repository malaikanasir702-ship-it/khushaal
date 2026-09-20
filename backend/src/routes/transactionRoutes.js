const express = require('express');
const authMiddleware = require('../middleware/auth');
const Transaction = require('../models/Transaction');

const router = express.Router();

// GET /api/transactions
router.get('/', authMiddleware, async (req, res) => {
  try {
    const { month, page = 1, limit = 20 } = req.query;
    const pageNum = Math.max(1, parseInt(page, 10) || 1);
    const limitNum = Math.max(1, parseInt(limit, 10) || 20);

    const query = { userId: req.userId };
    if (month) {
      // month is YYYY-MM
      const [year, m] = month.split('-').map(Number);
      if (!isNaN(year) && !isNaN(m)) {
        const start = new Date(year, m - 1, 1);
        const end = new Date(year, m, 0, 23, 59, 59, 999);
        query.date = { $gte: start, $lte: end };
      }
    }

    const total = await Transaction.countDocuments(query);
    const items = await Transaction.find(query)
      .sort({ date: -1 })
      .skip((pageNum - 1) * limitNum)
      .limit(limitNum);

    const formattedItems = items.map(item => ({
      id: item._id.toString(),
      title: item.title,
      urduTitle: item.urduTitle || item.title,
      amount: item.amount,
      isExpense: item.isExpense,
      category: item.category,
      envelopeId: item.envelopeId || '',
      paymentMethod: item.paymentMethod || 'Cash',
      date: item.date.toISOString()
    }));

    return res.json({
      items: formattedItems,
      total,
      page: pageNum,
      pages: Math.ceil(total / limitNum) || 1
    });
  } catch (error) {
    return res.status(500).json({ error: 'Error fetching transactions' });
  }
});

// POST /api/transactions
router.post('/', authMiddleware, async (req, res) => {
  try {
    const { title, urduTitle, amount, isExpense, category, envelopeId, paymentMethod, date } = req.body;

    if (!title || typeof title !== 'string' || !title.trim()) {
      return res.status(400).json({ error: 'Title is required / عنوان ضروری ہے' });
    }

    const numericAmount = Number(amount);
    if (isNaN(numericAmount) || numericAmount < 1) {
      return res.status(400).json({ error: 'Amount must be at least 1 / رقم کم از کم 1 ہونی چاہیے' });
    }

    const transaction = new Transaction({
      userId: req.userId,
      title: title.trim(),
      urduTitle: urduTitle ? urduTitle.trim() : title.trim(),
      amount: numericAmount,
      isExpense: isExpense !== undefined ? Boolean(isExpense) : true,
      category: category || 'General',
      envelopeId: envelopeId || 'needs',
      paymentMethod: paymentMethod || 'Cash',
      date: date ? new Date(date) : new Date()
    });

    await transaction.save();

    return res.status(201).json({
      id: transaction._id.toString(),
      title: transaction.title,
      urduTitle: transaction.urduTitle,
      amount: transaction.amount,
      isExpense: transaction.isExpense,
      category: transaction.category,
      envelopeId: transaction.envelopeId,
      paymentMethod: transaction.paymentMethod,
      date: transaction.date.toISOString()
    });
  } catch (error) {
    return res.status(500).json({ error: 'Error saving transaction' });
  }
});

module.exports = router;
