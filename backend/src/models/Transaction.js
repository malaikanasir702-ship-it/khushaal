const mongoose = require('mongoose');

const TransactionSchema = new mongoose.Schema({
  userId: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true, index: true },
  title: { type: String, required: true, trim: true },
  urduTitle: { type: String, trim: true },
  amount: { type: Number, required: true, min: 1 },
  isExpense: { type: Boolean, required: true },
  category: { type: String, required: true },
  envelopeId: { type: String },
  paymentMethod: { type: String, default: 'Cash' },
  date: { type: Date, default: Date.now }
});

TransactionSchema.index({ userId: 1, date: -1 });

module.exports = mongoose.model('Transaction', TransactionSchema);
