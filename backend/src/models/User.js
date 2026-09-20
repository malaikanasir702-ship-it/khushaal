const mongoose = require('mongoose');

const UserSchema = new mongoose.Schema({
  name: { type: String, required: true, trim: true },
  urduName: { type: String, trim: true },
  phone: { 
    type: String, 
    required: true, 
    unique: true,
    match: [/^\+92\d{10}$/, 'Invalid Pakistani phone number format (+92XXXXXXXXXX)']
  },
  cnic: { 
    type: String, 
    required: true, 
    unique: true,
    match: [/^\d{5}-\d{7}-\d$/, 'Invalid CNIC format (DDDDD-DDDDDDD-D)']
  },
  passwordHash: { type: String, required: true },
  factory: { type: String, required: true, trim: true },
  factoryId: { type: String, trim: true },
  jazzCashNumber: { type: String, trim: true },
  avatarUrl: { 
    type: String, 
    default: 'https://www.gstatic.com/labs-code/stitch/stitch-placeholder-300x300.svg' 
  },
  preferredLanguage: { 
    type: String, 
    enum: ['BILINGUAL', 'URDU', 'ENGLISH'], 
    default: 'BILINGUAL' 
  },
  refreshTokenHash: { type: String },
  tokenVersion: { type: Number, default: 0 },
  isActive: { type: Boolean, default: true },
  createdAt: { type: Date, default: Date.now },
  lastLoginAt: { type: Date }
});

UserSchema.methods.toProfileDto = function() {
  const maskedCnic = this.cnic ? this.cnic.replace(/^(\d{5}-)(\d{7})(-\d)$/, '$1•••••••$3') : '';
  const maskedPhone = this.phone && this.phone.length >= 10 
    ? `${this.phone.substring(0, 6)} ••••${this.phone.substring(this.phone.length - 3)}`
    : this.phone;

  return {
    id: this._id.toString(),
    name: this.name,
    urduName: this.urduName || this.name,
    greetingEnglish: 'Assalam-o-Alaikum!',
    greetingUrdu: 'سلام علیکم',
    organization: this.factory,
    verifiedText: 'تصدیق شدہ',
    factoryId: this.factoryId || '',
    phoneMasked: maskedPhone,
    cnicMasked: maskedCnic,
    paymentAccount: this.jazzCashNumber ? `JazzCash (${this.jazzCashNumber})` : 'JazzCash',
    avatarUrl: this.avatarUrl,
    preferredLanguage: this.preferredLanguage
  };
};

module.exports = mongoose.model('User', UserSchema);
