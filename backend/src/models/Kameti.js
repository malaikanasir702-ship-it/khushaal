const mongoose = require('mongoose');

const KametiSchema = new mongoose.Schema({
  userId: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true, index: true },
  name: { type: String, required: true, trim: true },
  urduName: { type: String, trim: true },
  monthlyAmount: { type: Number, required: true, min: 1 },
  totalMembers: { type: Number, required: true, min: 2 },
  myTurnMonth: { type: Number, required: true, min: 1 },
  currentMonth: { type: Number, required: true, default: 1 },
  payoutAmount: { type: Number },
  organizer: { type: String, trim: true },
  isPaidThisMonth: { type: Boolean, default: false },
  createdAt: { type: Date, default: Date.now }
});

module.exports = mongoose.model('Kameti', KametiSchema);
