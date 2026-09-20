const mongoose = require('mongoose');

const CoachMessageSchema = new mongoose.Schema({
  userId: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true, index: true },
  textUrdu: { type: String, required: true, trim: true },
  textRoman: { type: String, trim: true },
  isFromCoach: { type: Boolean, required: true },
  spokenText: { type: String, trim: true },
  createdAt: { type: Date, default: Date.now }
});

CoachMessageSchema.index({ userId: 1, createdAt: 1 });

module.exports = mongoose.model('CoachMessage', CoachMessageSchema);
