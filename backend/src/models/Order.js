const mongoose = require('mongoose');

const OrderSchema = new mongoose.Schema({
  userId: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true, index: true },
  customerName: { type: String, required: true, trim: true },
  phone: { type: String, trim: true },
  serviceTitle: { type: String, required: true, trim: true },
  totalAmount: { type: Number, required: true, min: 1 },
  advancePaid: { type: Number, default: 0, min: 0 },
  dueDate: { type: String, trim: true },
  isDelivered: { type: Boolean, default: false },
  isFullyPaid: { type: Boolean, default: false },
  createdAt: { type: Date, default: Date.now }
});

module.exports = mongoose.model('Order', OrderSchema);
