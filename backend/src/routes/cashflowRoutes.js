const express = require('express');
const authMiddleware = require('../middleware/auth');
const CashFlow = require('../models/CashFlow');
const { calculateProsperityScore } = require('../services/prosperityCalculator');

const router = express.Router();

const getCurrentMonthString = () => {
  const d = new Date();
  const year = d.getFullYear();
  const month = String(d.getMonth() + 1).padStart(2, '0');
  return `${year}-${month}`;
};

// GET /api/cashflow/current
router.get('/current', authMiddleware, async (req, res) => {
  try {
    const currentMonth = getCurrentMonthString();
    let record = await CashFlow.findOne({ userId: req.userId, month: currentMonth });

    if (!record) {
      // Find latest record or return zero-value default
      record = await CashFlow.findOne({ userId: req.userId }).sort({ month: -1 });
      if (!record) {
        return res.json({
          month: currentMonth,
          income: 0,
          incomeLabel: 'Salary',
          expenses: 0,
          expensesLabel: 'Expenses',
          savings: 0,
          available: 0
        });
      }
    }

    return res.json({
      id: record._id,
      month: record.month,
      income: record.income,
      incomeLabel: record.incomeLabel,
      expenses: record.expenses,
      expensesLabel: record.expensesLabel,
      savings: record.savings,
      available: record.available
    });
  } catch (error) {
    return res.status(500).json({ error: 'Error fetching cash flow' });
  }
});

// PUT /api/cashflow/current
router.put('/current', authMiddleware, async (req, res) => {
  try {
    const { income, incomeLabel, expenses, expensesLabel, savings, available, month } = req.body;
    const currentMonth = month || getCurrentMonthString();

    const record = await CashFlow.findOneAndUpdate(
      { userId: req.userId, month: currentMonth },
      {
        userId: req.userId,
        month: currentMonth,
        income: Number(income) || 0,
        incomeLabel: incomeLabel || 'Salary',
        expenses: Number(expenses) || 0,
        expensesLabel: expensesLabel || 'Expenses',
        savings: Number(savings) || 0,
        available: Number(available) || 0,
        updatedAt: new Date()
      },
      { upsert: true, new: true }
    );

    // Trigger prosperity score recalculation
    await calculateProsperityScore(req.userId);

    return res.json({
      id: record._id,
      month: record.month,
      income: record.income,
      incomeLabel: record.incomeLabel,
      expenses: record.expenses,
      expensesLabel: record.expensesLabel,
      savings: record.savings,
      available: record.available
    });
  } catch (error) {
    return res.status(500).json({ error: 'Error updating cash flow', message: error.message });
  }
});

module.exports = router;
