const mongoose = require('mongoose');

const rationItemSchema = new mongoose.Schema({
  id: String,
  nameUrdu: String,
  nameEnglish: String,
  category: String,
  defaultQty: String,
  unitPriceEstimate: Number,
  marketPriceRange: String,
  isEssential: { type: Boolean, default: true },
  savingsTip: String
}, { _id: false });

const scamSimulationSchema = new mongoose.Schema({
  id: String,
  titleUrdu: String,
  titleEnglish: String,
  scamText: String,
  optionSafe: String,
  optionTrap: String,
  audioExplanation: String
}, { _id: false });

const appConfigSchema = new mongoose.Schema({
  key: { type: String, unique: true, required: true },  // e.g. 'global'
  factoryName: { type: String, default: 'Naveena Mills Ltd.' },
  factoryUrdu: { type: String, default: 'نویینا ملز لمیٹڈ' },
  welfareHelpline: { type: String, default: '0800-64557' },
  welfareHelplineLabel: { type: String, default: 'Naveena Welfare' },
  rationItems: { type: [rationItemSchema], default: [] },
  scamSimulations: { type: [scamSimulationSchema], default: [] },
  updatedAt: { type: Date, default: Date.now }
});

module.exports = mongoose.model('AppConfig', appConfigSchema);
