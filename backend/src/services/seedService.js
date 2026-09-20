const Envelope = require('../models/Envelope');
const Locker = require('../models/Locker');
const Prosperity = require('../models/Prosperity');
const CoachMessage = require('../models/CoachMessage');

const seedUserDefaults = async (userId) => {
  // 1. Create 4 default envelopes
  const defaultEnvelopes = [
    {
      userId,
      envelopeKey: 'needs',
      percentage: 70,
      amount: 0,
      titleEnglish: 'Household Needs',
      titleUrdu: 'ضروری اخراجات',
      tag: 'خوراک، کرایہ، بلز',
      items: [
        { label: 'راشن و گروسری', amount: 0 },
        { label: 'مکان کا کرایہ', amount: 0 },
        { label: 'بجلی و گیس کے بل', amount: 0 }
      ]
    },
    {
      userId,
      envelopeKey: 'commitments',
      percentage: 6,
      amount: 0,
      titleEnglish: 'Commitments',
      titleUrdu: 'کمیٹی و واجبات',
      tag: 'کمیٹی، ادھار واپسی',
      items: [
        { label: 'ماہانہ کمیٹی', amount: 0 },
        { label: 'دکاندار کا ادھار', amount: 0 }
      ]
    },
    {
      userId,
      envelopeKey: 'emergency',
      percentage: 6,
      amount: 0,
      titleEnglish: 'Emergency Fund',
      titleUrdu: 'ہنگامی تحفظ',
      tag: 'طبی، غیر متوقع اخراجات',
      items: [
        { label: 'ہنگامی لاکر ڈپازٹ', amount: 0 }
      ]
    },
    {
      userId,
      envelopeKey: 'savings',
      percentage: 18,
      amount: 0,
      titleEnglish: 'Savings & Buffer',
      titleUrdu: 'دستیاب بچت',
      tag: 'مستقبل، بچوں کی تعلیم',
      items: [
        { label: 'گول سیونگز', amount: 0 },
        { label: 'آزاد بچت', amount: 0 }
      ]
    }
  ];

  await Envelope.deleteMany({ userId });
  await Envelope.insertMany(defaultEnvelopes);

  // 2. Create empty emergency locker
  await Locker.findOneAndUpdate(
    { userId },
    { userId, balance: 0, updatedAt: new Date() },
    { upsert: true, new: true }
  );

  // 3. Create default prosperity score
  const defaultPillars = [
    { id: 1, titleEnglish: 'Budgeting & Expense Discipline', titleUrdu: 'بجٹ اور اخراجات پر قابو', weightPercent: 20, currentScore: 8, maxScore: 20, statusText: 'شروعات', statusColorType: 'WARNING' },
    { id: 2, titleEnglish: 'Regular Savings Habit', titleUrdu: 'مسلسل بچت کی عادت', weightPercent: 20, currentScore: 0, maxScore: 20, statusText: 'کمیٹی یا بچت شروع کریں', statusColorType: 'URGENT' },
    { id: 3, titleEnglish: 'Emergency Safety Buffer', titleUrdu: 'ہنگامی تحفظ کا بفر', weightPercent: 20, currentScore: 0, maxScore: 20, statusText: 'لاکر میں رقم رکھیں', statusColorType: 'URGENT' },
    { id: 4, titleEnglish: 'Debt & Borrowing Control', titleUrdu: 'قرضوں پر کنٹرول', weightPercent: 15, currentScore: 12, maxScore: 15, statusText: 'قرض قابو میں ہے', statusColorType: 'SUCCESS' },
    { id: 5, titleEnglish: 'Digital Safety & Fraud Defense', titleUrdu: 'ڈیجیٹل تحفظ اور آگاہی', weightPercent: 10, currentScore: 9, maxScore: 10, statusText: 'محفوظ', statusColorType: 'SUCCESS' },
    { id: 6, titleEnglish: 'Income Resilience & Goals', titleUrdu: 'آمدنی کا استحکام اور اہداف', weightPercent: 15, currentScore: 0, maxScore: 15, statusText: 'کوئی ہدف مقرر کریں', statusColorType: 'WARNING' }
  ];

  await Prosperity.findOneAndUpdate(
    { userId },
    {
      userId,
      score: 29,
      maxScore: 100,
      savingsPct: 0,
      debtControlPct: 0.8,
      safetyShieldPct: 0.9,
      daysRunway: 0,
      pillars: defaultPillars,
      lastCalculatedAt: new Date()
    },
    { upsert: true, new: true }
  );

  // 4. Create welcome coach message
  await CoachMessage.create({
    userId,
    textUrdu: 'السلام علیکم! میں آپ کی مالیاتی کوچ فاطمہ ہوں۔ شروع کرنے کے لیے اپنی ماہانہ تنخواہ درج کریں۔',
    textRoman: 'Assalam-o-Alaikum! Main aap ki maliyati coach Fatima hoon. Shuru karne ke liye apni maahana tankhaah darj karein.',
    isFromCoach: true,
    spokenText: 'Assalam-o-Alaikum! I am your financial coach Fatima.'
  });
};

module.exports = { seedUserDefaults };
