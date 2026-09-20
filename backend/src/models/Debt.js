const mongoose = require('mongoose');

const DebtSchema = new mongoose.Schema({
  userId: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true, index: true },
  creditorName: { type: String, required: true, trim: true },
  creditorUrdu: { type: String, trim: true },
  relationOrType: { type: String, trim: true },
  totalAmount: { type: Number, required: true, min: 1 },
  remainingAmount: { type: Number, required: true, min: 0 },
  monthlyCommitment: { type: Number, default: 0, min: 0 },
  urgencyLevel: { type: String, trim: true },
  isShariahFriendly: { type: Boolean, default: true },
  repaymentStrategyTip: { type: String, trim: true },
  createdAt: { type: Date, default: Date.now }
});

module.exports = mongoose.model('Debt', DebtSchema);
