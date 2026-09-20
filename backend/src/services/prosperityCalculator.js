const CashFlow = require('../models/CashFlow');
const Locker = require('../models/Locker');
const Debt = require('../models/Debt');
const Transaction = require('../models/Transaction');
const Kameti = require('../models/Kameti');
const Goal = require('../models/Goal');
const Prosperity = require('../models/Prosperity');

const calculateProsperityScore = async (userId) => {
  const [cashFlow, locker, debts, kametis, goals] = await Promise.all([
    CashFlow.findOne({ userId }).sort({ month: -1 }),
    Locker.findOne({ userId }),
    Debt.find({ userId }),
    Kameti.find({ userId }),
    Goal.find({ userId })
  ]);

  const now = new Date();
  const currentMonthStart = new Date(now.getFullYear(), now.getMonth(), 1);
  const currentMonthEnd = new Date(now.getFullYear(), now.getMonth() + 1, 0, 23, 59, 59);
  const transactionCount = await Transaction.countDocuments({
    userId,
    date: { $gte: currentMonthStart, $lte: currentMonthEnd }
  });

  const income = cashFlow ? Number(cashFlow.income) : 0;
  const expenses = cashFlow ? Number(cashFlow.expenses) : 0;
  const lockerBalance = locker ? Number(locker.balance) : 0;

  // Requirement 15.3 & Task 7.1: If income == 0 or no record, total score = 0
  if (!cashFlow || income <= 0) {
    const zeroPillars = [
      { id: 1, titleEnglish: 'Budgeting & Expense Discipline', titleUrdu: 'بجٹ اور اخراجات پر قابو', weightPercent: 20, currentScore: 0, maxScore: 20, statusText: 'آمدنی درج کریں', statusColorType: 'URGENT' },
      { id: 2, titleEnglish: 'Regular Savings Habit', titleUrdu: 'مسلسل بچت کی عادت', weightPercent: 20, currentScore: 0, maxScore: 20, statusText: 'آمدنی درج کریں', statusColorType: 'URGENT' },
      { id: 3, titleEnglish: 'Emergency Safety Buffer', titleUrdu: 'ہنگامی تحفظ کا بفر', weightPercent: 20, currentScore: 0, maxScore: 20, statusText: 'آمدنی درج کریں', statusColorType: 'URGENT' },
      { id: 4, titleEnglish: 'Debt & Borrowing Control', titleUrdu: 'قرضوں پر کنٹرول', weightPercent: 15, currentScore: 0, maxScore: 15, statusText: 'آمدنی درج کریں', statusColorType: 'URGENT' },
      { id: 5, titleEnglish: 'Digital Safety & Fraud Defense', titleUrdu: 'ڈیجیٹل تحفظ اور آگاہی', weightPercent: 10, currentScore: 0, maxScore: 10, statusText: 'آمدنی درج کریں', statusColorType: 'URGENT' },
      { id: 6, titleEnglish: 'Income Resilience & Goals', titleUrdu: 'آمدنی کا استحکام اور اہداف', weightPercent: 15, currentScore: 0, maxScore: 15, statusText: 'آمدنی درج کریں', statusColorType: 'URGENT' }
    ];

    return await Prosperity.findOneAndUpdate(
      { userId },
      {
        userId,
        score: 0,
        maxScore: 100,
        savingsPct: 0,
        debtControlPct: 0,
        safetyShieldPct: 0,
        daysRunway: 0,
        pillars: zeroPillars,
        lastCalculatedAt: new Date()
      },
      { upsert: true, new: true }
    );
  }

  // Pillar 1: Budgeting (Weight: 20)
  const pillar1Score = transactionCount > 3 ? 16 : 8;

  // Pillar 2: Savings Habit (Weight: 20)
  const totalKametiSavings = kametis.reduce((sum, k) => sum + (Number(k.monthlyAmount) || 0), 0);
  const savingsRate = income > 0 ? (totalKametiSavings / income) : 0;
  const pillar2Score = Math.min(20, Math.max(0, Math.floor(savingsRate * 100)));

  // Pillar 3: Emergency Buffer (Weight: 20)
  let daysRunway = 0;
  let pillar3Score = 0;
  if (expenses > 0) {
    const dailyExpenses = expenses / 30;
    daysRunway = Math.floor(lockerBalance / dailyExpenses);
    pillar3Score = Math.min(20, Math.max(0, Math.floor((daysRunway / 30) * 20)));
  } else {
    // expenses = 0 -> max emergency buffer score (20)
    daysRunway = 30;
    pillar3Score = 20;
  }

  // Pillar 4: Debt Control (Weight: 15)
  const totalDebt = debts.reduce((sum, d) => sum + (Number(d.remainingAmount) || 0), 0);
  const debtToIncomeRatio = income > 0 ? (totalDebt / income) : 0;
  let pillar4Score = 3;
  if (debtToIncomeRatio < 1) {
    pillar4Score = 12;
  } else if (debtToIncomeRatio < 3) {
    pillar4Score = 8;
  } else {
    pillar4Score = 3;
  }

  // Pillar 5: Digital Safety (Weight: 10)
  const pillar5Score = 9;

  // Pillar 6: Income Resilience (Weight: 15)
  const activeGoals = goals.filter(g => !g.isCompleted).length;
  const pillar6Score = Math.min(15, Math.max(0, activeGoals * 3));

  let totalScore = pillar1Score + pillar2Score + pillar3Score + pillar4Score + pillar5Score + pillar6Score;
  totalScore = Math.min(100, Math.max(0, totalScore));

  const pillars = [
    {
      id: 1,
      titleEnglish: 'Budgeting & Expense Discipline',
      titleUrdu: 'بجٹ اور اخراجات پر قابو',
      weightPercent: 20,
      currentScore: pillar1Score,
      maxScore: 20,
      statusText: pillar1Score >= 16 ? 'مستحکم' : 'بہتری کی گنجائش',
      statusColorType: pillar1Score >= 16 ? 'SUCCESS' : 'WARNING'
    },
    {
      id: 2,
      titleEnglish: 'Regular Savings Habit',
      titleUrdu: 'مسلسل بچت کی عادت',
      weightPercent: 20,
      currentScore: pillar2Score,
      maxScore: 20,
      statusText: pillar2Score >= 14 ? 'بہترین' : 'کمیٹی بچت بڑھائیں',
      statusColorType: pillar2Score >= 14 ? 'SUCCESS' : (pillar2Score >= 8 ? 'WARNING' : 'URGENT')
    },
    {
      id: 3,
      titleEnglish: 'Emergency Safety Buffer',
      titleUrdu: 'ہنگامی تحفظ کا بفر',
      weightPercent: 20,
      currentScore: pillar3Score,
      maxScore: 20,
      statusText: `${daysRunway} دن کا بفر`,
      statusColorType: daysRunway >= 20 ? 'SUCCESS' : (daysRunway >= 10 ? 'WARNING' : 'URGENT')
    },
    {
      id: 4,
      titleEnglish: 'Debt & Borrowing Control',
      titleUrdu: 'قرضوں پر کنٹرول',
      weightPercent: 15,
      currentScore: pillar4Score,
      maxScore: 15,
      statusText: pillar4Score >= 12 ? 'قرض کنٹرول میں ہے' : 'قرض کم کریں',
      statusColorType: pillar4Score >= 12 ? 'SUCCESS' : (pillar4Score >= 8 ? 'WARNING' : 'URGENT')
    },
    {
      id: 5,
      titleEnglish: 'Digital Safety & Fraud Defense',
      titleUrdu: 'ڈیجیٹل تحفظ اور آگاہی',
      weightPercent: 10,
      currentScore: pillar5Score,
      maxScore: 10,
      statusText: 'محفوظ',
      statusColorType: 'SUCCESS'
    },
    {
      id: 6,
      titleEnglish: 'Income Resilience & Goals',
      titleUrdu: 'آمدنی کا استحکام اور اہداف',
      weightPercent: 15,
      currentScore: pillar6Score,
      maxScore: 15,
      statusText: `${activeGoals} فعال اہداف`,
      statusColorType: activeGoals >= 2 ? 'SUCCESS' : 'WARNING'
    }
  ];

  const updatedProsperity = await Prosperity.findOneAndUpdate(
    { userId },
    {
      userId,
      score: totalScore,
      maxScore: 100,
      savingsPct: income > 0 ? (totalKametiSavings / income) : 0,
      debtControlPct: Math.max(0, Math.min(1, 1 - (debtToIncomeRatio / 3))),
      safetyShieldPct: pillar5Score / 10,
      daysRunway: daysRunway,
      pillars,
      lastCalculatedAt: new Date()
    },
    { upsert: true, new: true }
  );

  return updatedProsperity;
};

module.exports = { calculateProsperityScore };
