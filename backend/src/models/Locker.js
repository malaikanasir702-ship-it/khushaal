const mongoose = require('mongoose');

const LockerSchema = new mongoose.Schema({
  userId: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true, unique: true },
  balance: { 
    type: Number, 
    default: 0, 
    min: [0, 'Emergency locker balance cannot be negative'] 
  },
  updatedAt: { type: Date, default: Date.now }
});

module.exports = mongoose.model('Locker', LockerSchema);
