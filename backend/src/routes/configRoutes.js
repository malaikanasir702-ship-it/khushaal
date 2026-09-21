const express = require('express');
const AppConfig = require('../models/AppConfig');
const adminAuth = require('../middleware/adminAuth');

const router = express.Router();

// Default config fallback
const DEFAULT_CONFIG = {
  factoryName: 'Naveena Mills Ltd.',
  factoryUrdu: 'نویینا ملز لمیٹڈ',
  welfareHelpline: '0800-64557',
  welfareHelplineLabel: 'Naveena Welfare',
  rationItems: [
    { id: 'r-1', nameUrdu: 'گندم کا آٹا (چکی والا)', nameEnglish: 'Chakki Wheat Flour (20kg)', category: 'بنیادی اناج', defaultQty: '20 کلو', unitPriceEstimate: 2700, marketPriceRange: 'Rs. 2,600 - 2,850', isEssential: true, savingsTip: 'یوٹیلیٹی اسٹور سے خریدنے پر 300 روپے بچت ہوتی ہے۔' },
    { id: 'r-2', nameUrdu: 'کوکنگ آئل / بناسپتی گھی', nameEnglish: 'Cooking Oil (5 Litre)', category: 'روغن و چکنائی', defaultQty: '5 لیٹر', unitPriceEstimate: 2450, marketPriceRange: 'Rs. 2,350 - 2,600', isEssential: true, savingsTip: 'معیاری پیک لیں، کم مقدار میں استعمال صحت کے لیے بہتر ہے۔' },
    { id: 'r-3', nameUrdu: 'چاول (باسمتی ٹوٹا)', nameEnglish: 'Basmati Broken Rice (5kg)', category: 'بنیادی اناج', defaultQty: '5 کلو', unitPriceEstimate: 1400, marketPriceRange: 'Rs. 1,300 - 1,550', isEssential: true, savingsTip: 'تھوک مارکیٹ سے 10 کلو لینے پر فی کلو 30 روپے بچتے ہیں۔' },
    { id: 'r-4', nameUrdu: 'دال چنا و مونگ', nameEnglish: 'Chana & Moong Pulses (2kg)', category: 'دالیں', defaultQty: '2 کلو', unitPriceEstimate: 720, marketPriceRange: 'Rs. 680 - 780', isEssential: true, savingsTip: 'ہفتے میں 3 دن دال کا استعمال گوشت پر 4,000 روپے ماہانہ بچاتا ہے۔' },
    { id: 'r-5', nameUrdu: 'چینی', nameEnglish: 'White Sugar (3kg)', category: 'مٹھاس', defaultQty: '3 کلو', unitPriceEstimate: 480, marketPriceRange: 'Rs. 450 - 520', isEssential: true, savingsTip: 'گڑ کا متبادل استعمال صحت مند اور سستا ہے۔' },
    { id: 'r-6', nameUrdu: 'چائے کی پتی', nameEnglish: 'Danedar Tea (400g)', category: 'مٹھاس', defaultQty: '400 گرام', unitPriceEstimate: 650, marketPriceRange: 'Rs. 620 - 700', isEssential: true, savingsTip: 'بڑے فیملی پیک میں بچت زیادہ ہوتی ہے۔' },
    { id: 'r-7', nameUrdu: 'دودھ', nameEnglish: 'Fresh/Powder Milk', category: 'ڈیری', defaultQty: 'ماہانہ تخمینہ', unitPriceEstimate: 4800, marketPriceRange: 'Rs. 4,500 - 5,200', isEssential: true, savingsTip: 'بچوں کی غذائیت کے لیے لازمی ہے، اس میں کٹوتی نہ کریں۔' },
    { id: 'r-8', nameUrdu: 'صابن و صفائی', nameEnglish: 'Washing Powder & Soaps', category: 'گھریلو صفائی', defaultQty: '1 ماہ', unitPriceEstimate: 1650, marketPriceRange: 'Rs. 1,500 - 1,800', isEssential: false, savingsTip: 'بڑا پیک لینے پر 25% کم خرچ آتا ہے۔' }
  ],
  scamSimulations: [
    { id: 'scam-1', titleUrdu: 'بینک منیجر کی جعلی کال', titleEnglish: 'Fake Bank Manager OTP Call', scamText: "کال کرنے والا: 'میں اسٹیٹ بینک سے ہوں، آپ کا ATM بلاک ہو گیا ہے، ابھی آئے ہوئے 4 ہندسوں کا کوڈ بتائیں!'", optionSafe: 'فوراً کال کاٹیں! بینک کبھی فون پر OTP نہیں مانگتا ✓', optionTrap: 'جلدی میں کارڈ چالو کروانے کے لیے کوڈ بتا دیں', audioExplanation: 'Khabardaar! Kisi ko bhi call par OTP ya PIN mat dein.' },
    { id: 'scam-2', titleUrdu: 'انعامی میسج فراڈ', titleEnglish: 'Fake Cash Prize SMS', scamText: "میسج: 'مبارک ہو! آپ کا 25,000 وظیفہ منظور ہوا۔ حاصل کرنے کے لیے 1,000 کا ایزی لوڈ بھیجیں۔'", optionSafe: 'فراڈ میسج ڈیلیٹ کریں اور پیسے نہ بھیجیں ✓', optionTrap: 'پچیس ہزار کے لالچ میں ایک ہزار کا لوڈ کروا دیں', audioExplanation: 'Yeh jaali SMS hota ha. Kisi sarkari imdad ke liye pehlay paisay nahi maangay jatay.' },
    { id: 'scam-3', titleUrdu: 'غلطی سے رقم کا فراڈ', titleEnglish: 'Fake Accidental Transfer', scamText: "ایک اجنبی: 'بھائی غلطی سے آپ کے اکاؤنٹ میں 5,000 آ گئے، واپس بھیج دیں۔' جبکہ پیسے نہیں آئے۔", optionSafe: 'پہلے اپنا اصلی بینک بیلنس چیک کریں ✓', optionTrap: 'بغیر بیلنس دیکھے اپنے پیسے واپس بھیج دیں', audioExplanation: 'Fasadi log farzi SMS bhej kar asli paisay mangwatay hain. Hamesha apna balance pehlay check karein.' },
    { id: 'scam-4', titleUrdu: 'سود خور ایپس', titleEnglish: 'Predatory Loan App', scamText: "فیس بک اشتہار: 'صرف شناختی کارڈ پر 20 ہزار فوری قرض۔' پھر ہفتے بعد 40 ہزار مانگتے ہیں۔", optionSafe: 'ایسی غیر قانونی ایپس ہرگز ڈاؤنلوڈ نہ کریں ✓', optionTrap: 'جلدی میں ذاتی کانٹیکٹس دے کر قرض لے لیں', audioExplanation: 'Soodi qarz apps aap ke mobile ka data chura kar blackmail karti hain.' }
  ]
};

// GET /api/config — Public endpoint, no auth required
// Android app fetches this on startup to get company-configured content
router.get('/', async (req, res) => {
  try {
    let config = await AppConfig.findOne({ key: 'global' });
    if (!config) {
      // Return defaults if not configured yet
      return res.json(DEFAULT_CONFIG);
    }
    return res.json({
      factoryName: config.factoryName,
      factoryUrdu: config.factoryUrdu,
      welfareHelpline: config.welfareHelpline,
      welfareHelplineLabel: config.welfareHelplineLabel,
      rationItems: config.rationItems.length > 0 ? config.rationItems : DEFAULT_CONFIG.rationItems,
      scamSimulations: config.scamSimulations.length > 0 ? config.scamSimulations : DEFAULT_CONFIG.scamSimulations
    });
  } catch (error) {
    console.error('Config fetch error:', error);
    return res.json(DEFAULT_CONFIG); // Always return something
  }
});

// PUT /api/config — Admin-only: update app config
router.put('/', adminAuth, async (req, res) => {
  try {
    const { factoryName, factoryUrdu, welfareHelpline, welfareHelplineLabel, rationItems, scamSimulations, skillOpportunities } = req.body;

    let config = await AppConfig.findOne({ key: 'global' });
    if (!config) {
      config = new AppConfig({ key: 'global' });
    }

    if (factoryName !== undefined) config.factoryName = factoryName.trim();
    if (factoryUrdu !== undefined) config.factoryUrdu = factoryUrdu.trim();
    if (welfareHelpline !== undefined) config.welfareHelpline = welfareHelpline.trim();
    if (welfareHelplineLabel !== undefined) config.welfareHelplineLabel = welfareHelplineLabel.trim();
    if (rationItems !== undefined && Array.isArray(rationItems)) config.rationItems = rationItems;
    if (scamSimulations !== undefined && Array.isArray(scamSimulations)) config.scamSimulations = scamSimulations;
    if (skillOpportunities !== undefined && Array.isArray(skillOpportunities)) config.skillOpportunities = skillOpportunities;
    config.updatedAt = new Date();

    await config.save();
    return res.json({ message: 'App config updated successfully', config });
  } catch (error) {
    console.error('Config update error:', error);
    return res.status(500).json({ error: 'Error updating app config' });
  }
});

module.exports = router;
