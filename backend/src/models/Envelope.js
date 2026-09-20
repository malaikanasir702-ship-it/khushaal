const mongoose = require('mongoose');

const EnvelopeSubItemSchema = new mongoose.Schema({
  label: { type: String, required: true },
  amount: { type: Number, default: 0 }
}, { _id: false });

const EnvelopeSchema = new mongoose.Schema({
  userId: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true, index: true },
  envelopeKey: { 
    type: String, 
    required: true,
    enum: ['needs', 'commitments', 'emergency', 'savings']
  },
  titleEnglish: { type: String, required: true },
  titleUrdu: { type: String },
  percentage: { type: Number, min: 0, max: 100, default: 0 },
  amount: { type: Number, default: 0 },
  tag: { type: String },
  items: [EnvelopeSubItemSchema],
  updatedAt: { type: Date, default: Date.now }
});

EnvelopeSchema.index({ userId: 1, envelopeKey: 1 }, { unique: true });

module.exports = mongoose.model('Envelope', EnvelopeSchema);
