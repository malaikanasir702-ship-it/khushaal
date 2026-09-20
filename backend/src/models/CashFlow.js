const mongoose = require('mongoose');

const CashFlowSchema = new mongoose.Schema({
  userId: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true, index: true },
  month: { type: String, required: true },  // "2025-03" (YYYY-MM)
  income: { type: Number, required: true, min: 0 },
  incomeLabel: { type: String, default: 'Factory Salary' },
  expenses: { type: Number, default: 0, min: 0 },
  expensesLabel: { type: String, default: 'Household Expenses' },
  savings: { type: Number, default: 0 },
  available: { type: Number, default: 0 },
  updatedAt: { type: Date, default: Date.now }
});

CashFlowSchema.index({ userId: 1, month: -1 });

module.exports = mongoose.model('CashFlow', CashFlowSchema);
