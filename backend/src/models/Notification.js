const mongoose = require('mongoose');

const NotificationSchema = new mongoose.Schema({
  userId: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true, index: true },
  titleUrdu: { type: String, required: true },
  titleEnglish: { type: String, required: true },
  descriptionUrdu: { type: String },
  descriptionEnglish: { type: String },
  category: { 
    type: String, 
    enum: ['FACTORY', 'FINANCE', 'SECURITY', 'COACH'],
    default: 'FINANCE'
  },
  isRead: { type: Boolean, default: false },
  destination: { type: String },
  spokenText: { type: String },
  createdAt: { type: Date, default: Date.now }
});

NotificationSchema.index({ userId: 1, createdAt: -1 });

module.exports = mongoose.model('Notification', NotificationSchema);
