const mongoose = require('mongoose');

const BillSchema = new mongoose.Schema({
  userId: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true, index: true },
  companyName: { type: String, required: true, trim: true },
  companyUrdu: { type: String, trim: true },
  consumerNumber: { type: String, trim: true },
  billType: { type: String, trim: true },
  month: { type: String, trim: true },
  dueDate: { type: String, trim: true },
  amount: { type: Number, required: true, min: 1 },
  unitsConsumed: { type: Number, default: 0 },
  isPaid: { type: Boolean, default: false },
  paidDate: { type: String, trim: true },
  alertTip: { type: String, trim: true },
  createdAt: { type: Date, default: Date.now }
});

module.exports = mongoose.model('Bill', BillSchema);
