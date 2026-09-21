const mongoose = require('mongoose');

/**
 * FactoryPayslip model
 * Admin enters payslip data per worker per month.
 * Worker reads via GET /api/payslip (their own data).
 */
const payslipSchema = new mongoose.Schema({
  userId: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true, index: true },
  month: { type: String, required: true }, // e.g. "June 2025 / جون 2025"
  employeeName: { type: String, default: '' },
  employeeId: { type: String, default: '' },
  department: { type: String, default: '' },
  daysPresent: { type: Number, default: 0 },
  daysAbsent: { type: Number, default: 0 },
  overtimeHours: { type: Number, default: 0 },
  baseWage: { type: Number, default: 0 },
  overtimePay: { type: Number, default: 0 },
  attendanceBonus: { type: Number, default: 0 },
  productionBonus: { type: Number, default: 0 },
  totalGrossWage: { type: Number, default: 0 },
  eobiDeduction: { type: Number, default: 0 },
  messAdvanceDeduction: { type: Number, default: 0 },
  unionFundDeduction: { type: Number, default: 0 },
  totalDeductions: { type: Number, default: 0 },
  netTakeHome: { type: Number, default: 0 },
  paymentStatus: { type: String, default: 'زیر عمل (Pending)' },
  creditedDate: { type: String, default: '' },
  disbursementAccount: { type: String, default: '' },
  createdAt: { type: Date, default: Date.now },
  updatedAt: { type: Date, default: Date.now }
});

// Compound unique index: one payslip per worker per month
payslipSchema.index({ userId: 1, month: 1 }, { unique: true });

module.exports = mongoose.model('Payslip', payslipSchema);
