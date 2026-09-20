const mongoose = require('mongoose');

const PillarSchema = new mongoose.Schema({
  id: { type: Number, required: true },
  titleEnglish: { type: String, required: true },
  titleUrdu: { type: String, required: true },
  weightPercent: { type: Number, required: true },
  currentScore: { type: Number, required: true, min: 0 },
  maxScore: { type: Number, required: true },
  statusText: { type: String, default: '' },
  statusColorType: { 
    type: String, 
    enum: ['SUCCESS', 'WARNING', 'URGENT'],
    default: 'SUCCESS'
  }
}, { _id: false });

const ProsperitySchema = new mongoose.Schema({
  userId: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true, unique: true },
  score: { type: Number, default: 0, min: 0, max: 100 },
  maxScore: { type: Number, default: 100 },
  savingsPct: { type: Number, default: 0 },
  debtControlPct: { type: Number, default: 0 },
  safetyShieldPct: { type: Number, default: 0 },
  daysRunway: { type: Number, default: 0 },
  pillars: [PillarSchema],
  lastCalculatedAt: { type: Date, default: Date.now }
});

module.exports = mongoose.model('Prosperity', ProsperitySchema);
