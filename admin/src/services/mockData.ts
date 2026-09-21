import {
  WorkerUser,
  Transaction,
  Bill,
  Debt,
  Kameti,
  Goal,
  Order,
  EmergencyLocker,
  ProsperityScore,
  NotificationItem,
  CoachLog,
  DashboardStats,
  SystemHealth
} from '../types';

export const mockWorkers: WorkerUser[] = [
  {
    id: 'usr-101',
    name: 'Muhammad Tariq',
    urduName: 'محمد طارق',
    phone: '+923014589211',
    cnic: '35201-1284950-3',
    factory: 'Nishat Mills (Weaving Unit 4)',
    factoryId: 'NM-W4-049',
    jazzCashNumber: '03014589211',
    role: 'user',
    isActive: true,
    preferredLanguage: 'BILINGUAL',
    createdAt: '2025-01-12T08:30:00.000Z',
    lastLoginAt: '2025-03-10T14:20:00.000Z',
    lockerBalance: 12500,
    prosperityScore: 68,
    unpaidBills: 1,
    totalDebts: 18000,
    activeKametis: 2
  },
  {
    id: 'usr-102',
    name: 'Bashir Ahmed',
    urduName: 'بشیر احمد',
    phone: '+923129845120',
    cnic: '35202-8392014-7',
    factory: 'Crescent Bahuman Denim Plant',
    factoryId: 'CB-D2-114',
    jazzCashNumber: '03129845120',
    role: 'user',
    isActive: true,
    preferredLanguage: 'URDU',
    createdAt: '2025-01-15T10:15:00.000Z',
    lastLoginAt: '2025-03-09T18:45:00.000Z',
    lockerBalance: 4500,
    prosperityScore: 42,
    unpaidBills: 2,
    totalDebts: 45000,
    activeKametis: 1
  },
  {
    id: 'usr-103',
    name: 'Parveen Akhtar',
    urduName: 'پروین اختر',
    phone: '+923218765432',
    cnic: '35201-9482710-2',
    factory: 'Artistic Milliners Garment Div',
    factoryId: 'AM-G3-205',
    jazzCashNumber: '03218765432',
    role: 'user',
    isActive: true,
    preferredLanguage: 'BILINGUAL',
    createdAt: '2025-02-01T09:00:00.000Z',
    lastLoginAt: '2025-03-10T11:10:00.000Z',
    lockerBalance: 24000,
    prosperityScore: 84,
    unpaidBills: 0,
    totalDebts: 0,
    activeKametis: 2
  },
  {
    id: 'usr-104',
    name: 'Ghulam Rasool',
    urduName: 'غلام رسول',
    phone: '+923335678901',
    cnic: '38403-1928374-5',
    factory: 'Lucky Textile Mills (Karachi)',
    factoryId: 'LTM-K1-088',
    jazzCashNumber: '03335678901',
    role: 'user',
    isActive: false,
    preferredLanguage: 'URDU',
    createdAt: '2025-01-20T12:00:00.000Z',
    lastLoginAt: '2025-02-18T16:30:00.000Z',
    lockerBalance: 1000,
    prosperityScore: 24,
    unpaidBills: 3,
    totalDebts: 72000,
    activeKametis: 0
  },
  {
    id: 'usr-105',
    name: 'Zahid Mehmood',
    urduName: 'زاہد محمود',
    phone: '+923451122334',
    cnic: '33100-5544332-1',
    factory: 'Interloop Hosiery Plant 1 (Faisalabad)',
    factoryId: 'IL-H1-312',
    jazzCashNumber: '03451122334',
    role: 'user',
    isActive: true,
    preferredLanguage: 'BILINGUAL',
    createdAt: '2025-02-10T11:00:00.000Z',
    lastLoginAt: '2025-03-10T09:05:00.000Z',
    lockerBalance: 32000,
    prosperityScore: 78,
    unpaidBills: 0,
    totalDebts: 12000,
    activeKametis: 1
  },
  {
    id: 'usr-106',
    name: 'Khadija Bibi',
    urduName: 'خدیجہ بی بی',
    phone: '+923087788990',
    cnic: '35201-6677889-4',
    factory: 'Sapphire Textile Finishing Unit',
    factoryId: 'ST-FU-019',
    jazzCashNumber: '03087788990',
    role: 'user',
    isActive: true,
    preferredLanguage: 'URDU',
    createdAt: '2025-02-14T14:20:00.000Z',
    lastLoginAt: '2025-03-08T13:40:00.000Z',
    lockerBalance: 15500,
    prosperityScore: 61,
    unpaidBills: 1,
    totalDebts: 25000,
    activeKametis: 2
  }
];

export const mockTransactions: Transaction[] = [
  {
    id: 'tx-001',
    userId: 'usr-101',
    workerName: 'Muhammad Tariq',
    workerPhone: '+923014589211',
    factory: 'Nishat Mills (Weaving Unit 4)',
    title: 'Monthly Factory Salary & Production Bonus',
    urduTitle: 'ماہانہ فیکٹری تنخواہ اور پروڈکشن بونس',
    amount: 52000,
    isExpense: false,
    category: 'Salary',
    envelopeId: 'income',
    paymentMethod: 'Bank Transfer / JazzCash',
    date: '2025-03-01T10:00:00.000Z'
  },
  {
    id: 'tx-002',
    userId: 'usr-101',
    workerName: 'Muhammad Tariq',
    workerPhone: '+923014589211',
    factory: 'Nishat Mills (Weaving Unit 4)',
    title: 'Monthly Atta, Ghee & Grocery Ration',
    urduTitle: 'ماہانہ راشن، آٹا و گھی',
    amount: 19500,
    isExpense: true,
    category: 'Groceries',
    envelopeId: 'needs',
    paymentMethod: 'Cash',
    date: '2025-03-03T16:30:00.000Z'
  },
  {
    id: 'tx-003',
    userId: 'usr-103',
    workerName: 'Parveen Akhtar',
    workerPhone: '+923218765432',
    factory: 'Artistic Milliners Garment Div',
    title: 'Garment Alteration & Stitched Kurtis Sale',
    urduTitle: 'کپڑوں کی سلائی اور کرتیاں فروخت',
    amount: 8500,
    isExpense: false,
    category: 'Side Hustle',
    envelopeId: 'savings',
    paymentMethod: 'JazzCash',
    date: '2025-03-04T12:15:00.000Z'
  },
  {
    id: 'tx-004',
    userId: 'usr-102',
    workerName: 'Bashir Ahmed',
    workerPhone: '+923129845120',
    factory: 'Crescent Bahuman Denim Plant',
    title: 'Medical Clinic Treatment & Medicines',
    urduTitle: 'بچوں کے علاج اور ادویات کا خرچہ',
    amount: 6200,
    isExpense: true,
    category: 'Healthcare',
    envelopeId: 'emergency',
    paymentMethod: 'Cash',
    date: '2025-03-05T19:00:00.000Z'
  },
  {
    id: 'tx-005',
    userId: 'usr-105',
    workerName: 'Zahid Mehmood',
    workerPhone: '+923451122334',
    factory: 'Interloop Hosiery Plant 1 (Faisalabad)',
    title: 'Community Kameti Installment Paid',
    urduTitle: 'ماہانہ کمیٹی قسط ادا کی گئی',
    amount: 5000,
    isExpense: true,
    category: 'Kameti',
    envelopeId: 'commitments',
    paymentMethod: 'Cash',
    date: '2025-03-06T11:00:00.000Z'
  },
  {
    id: 'tx-006',
    userId: 'usr-106',
    workerName: 'Khadija Bibi',
    workerPhone: '+923087788990',
    factory: 'Sapphire Textile Finishing Unit',
    title: 'Electricity Bill Payment via EasyPaisa',
    urduTitle: 'لیسکو بجلی کا بل ادا کیا',
    amount: 4800,
    isExpense: true,
    category: 'Utilities',
    envelopeId: 'needs',
    paymentMethod: 'EasyPaisa',
    date: '2025-03-07T14:45:00.000Z'
  }
];

export const mockBills: Bill[] = [
  {
    id: 'bil-01',
    userId: 'usr-101',
    workerName: 'Muhammad Tariq',
    workerPhone: '+923014589211',
    factory: 'Nishat Mills (Weaving Unit 4)',
    companyName: 'LESCO Electricity',
    companyUrdu: 'لیسکو بجلی کا بل',
    consumerNumber: '08112345678901U',
    billType: 'Electricity',
    month: 'March 2025',
    dueDate: '2025-03-18',
    amount: 5450,
    unitsConsumed: 185,
    isPaid: false,
    alertTip: 'Peak hours (6 PM - 10 PM) mein استری اور موٹر نہ چلائیں۔'
  },
  {
    id: 'bil-02',
    userId: 'usr-102',
    workerName: 'Bashir Ahmed',
    workerPhone: '+923129845120',
    factory: 'Crescent Bahuman Denim Plant',
    companyName: 'SNGPL Sui Gas',
    companyUrdu: 'سوئی گیس بل',
    consumerNumber: '98765432100',
    billType: 'Gas',
    month: 'March 2025',
    dueDate: '2025-03-15',
    amount: 1980,
    unitsConsumed: 42,
    isPaid: false,
    alertTip: 'بغیر ضرورت گیس ہیٹر بند رکھیں۔'
  },
  {
    id: 'bil-03',
    userId: 'usr-103',
    workerName: 'Parveen Akhtar',
    workerPhone: '+923218765432',
    factory: 'Artistic Milliners Garment Div',
    companyName: 'K-Electric Bill',
    companyUrdu: 'کے الیکٹرک بل',
    consumerNumber: '040001293849',
    billType: 'Electricity',
    month: 'March 2025',
    dueDate: '2025-03-12',
    amount: 4100,
    unitsConsumed: 140,
    isPaid: true,
    paidDate: '2025-03-08T10:30:00.000Z',
    alertTip: 'وقت پر ادا کیا گیا! لیٹ فیس سے بچت ہوئی۔'
  },
  {
    id: 'bil-04',
    userId: 'usr-104',
    workerName: 'Ghulam Rasool',
    workerPhone: '+923335678901',
    factory: 'Lucky Textile Mills (Karachi)',
    companyName: 'WASA Water & Sanitation',
    companyUrdu: 'واسا پانی کا بل',
    consumerNumber: '1122334455',
    billType: 'Water',
    month: 'February 2025',
    dueDate: '2025-02-25',
    amount: 1200,
    unitsConsumed: 0,
    isPaid: false,
    alertTip: 'بل اوور ڈیو ہو چکا ہے! فائن سے بچنے کے لیے فوراً ادا کریں۔'
  }
];

export const mockDebts: Debt[] = [
  {
    id: 'dbt-01',
    userId: 'usr-102',
    workerName: 'Bashir Ahmed',
    workerPhone: '+923129845120',
    factory: 'Crescent Bahuman Denim Plant',
    creditorName: 'Chaudhry Kiryana Store',
    creditorUrdu: 'چوہدری کریانہ اسٹور',
    relationOrType: 'Neighborhood Shopkeeper',
    totalAmount: 30000,
    remainingAmount: 22000,
    monthlyCommitment: 4000,
    urgencyLevel: 'HIGH',
    isShariahFriendly: true,
    repaymentStrategyTip: 'اگلی تنخواہ ملتے ہی کم از کم 4,000 روپے ادا کریں۔'
  },
  {
    id: 'dbt-02',
    userId: 'usr-101',
    workerName: 'Muhammad Tariq',
    workerPhone: '+923014589211',
    factory: 'Nishat Mills (Weaving Unit 4)',
    creditorName: 'Mama Rafiq (Uncle)',
    creditorUrdu: 'ماموں رفیق (قرضِ حسنہ)',
    relationOrType: 'Family Relative',
    totalAmount: 25000,
    remainingAmount: 18000,
    monthlyCommitment: 3000,
    urgencyLevel: 'MEDIUM',
    isShariahFriendly: true,
    repaymentStrategyTip: 'قرض حسنہ ہے۔ ماہانہ 3,000 ادا کرتے رہیں۔'
  },
  {
    id: 'dbt-03',
    userId: 'usr-104',
    workerName: 'Ghulam Rasool',
    workerPhone: '+923335678901',
    factory: 'Lucky Textile Mills (Karachi)',
    creditorName: 'Local Finance Agent',
    creditorUrdu: 'مقامی کمیٹی ساہوکار',
    relationOrType: 'Informal Money Lender',
    totalAmount: 85000,
    remainingAmount: 72000,
    monthlyCommitment: 8000,
    urgencyLevel: 'HIGH',
    isShariahFriendly: false,
    repaymentStrategyTip: 'سود سے بچنے کے لیے اس قرض کو سب سے پہلے ڈیٹ اسنو بال سے ختم کریں۔'
  }
];

export const mockKametis: Kameti[] = [
  {
    id: 'kam-01',
    userId: 'usr-101',
    workerName: 'Muhammad Tariq',
    workerPhone: '+923014589211',
    factory: 'Nishat Mills (Weaving Unit 4)',
    name: 'Shift-A Weaving Workers Committee',
    urduName: 'شفٹ اے ویونگ کمیٹی',
    monthlyAmount: 5000,
    totalMembers: 10,
    myTurnMonth: 7,
    currentMonth: 4,
    payoutAmount: 50000,
    organizer: 'Ustad Aslam (Jobber)',
    isPaidThisMonth: true
  },
  {
    id: 'kam-02',
    userId: 'usr-103',
    workerName: 'Parveen Akhtar',
    workerPhone: '+923218765432',
    factory: 'Artistic Milliners Garment Div',
    name: 'Women Stitching Unit Committee',
    urduName: 'سلائی یونٹ خواتین کمیٹی',
    monthlyAmount: 3000,
    totalMembers: 12,
    myTurnMonth: 3,
    currentMonth: 3,
    payoutAmount: 36000,
    organizer: 'Baji Shahnaz',
    isPaidThisMonth: true
  },
  {
    id: 'kam-03',
    userId: 'usr-105',
    workerName: 'Zahid Mehmood',
    workerPhone: '+923451122334',
    factory: 'Interloop Hosiery Plant 1 (Faisalabad)',
    name: 'Hosiery Plant Welfare Kameti',
    urduName: 'ہوزری ویلفیئر کمیٹی',
    monthlyAmount: 6000,
    totalMembers: 8,
    myTurnMonth: 6,
    currentMonth: 2,
    payoutAmount: 48000,
    organizer: 'Supervisor Naeem',
    isPaidThisMonth: false
  }
];

export const mockGoals: Goal[] = [
  {
    id: 'gol-01',
    userId: 'usr-101',
    workerName: 'Muhammad Tariq',
    workerPhone: '+923014589211',
    factory: 'Nishat Mills (Weaving Unit 4)',
    title: 'Children Annual School Admission & Books',
    urduTitle: 'بچوں کے اسکول کے داخلے اور کتب',
    targetAmount: 30000,
    currentAmount: 18500,
    targetDate: 'May 2025',
    emoji: '📚',
    isCompleted: false
  },
  {
    id: 'gol-02',
    userId: 'usr-103',
    workerName: 'Parveen Akhtar',
    workerPhone: '+923218765432',
    factory: 'Artistic Milliners Garment Div',
    title: 'Industrial Sewing Machine for Microenterprise',
    urduTitle: 'سلائی کاروبار کے لیے انڈسٹریل مشین',
    targetAmount: 45000,
    currentAmount: 45000,
    targetDate: 'Feb 2025',
    emoji: '🧵',
    isCompleted: true
  },
  {
    id: 'gol-03',
    userId: 'usr-105',
    workerName: 'Zahid Mehmood',
    workerPhone: '+923451122334',
    factory: 'Interloop Hosiery Plant 1 (Faisalabad)',
    title: 'Motorcycle Overhauling & New Tires',
    urduTitle: 'موٹرسائیکل کی مرمت اور نئے ٹائر',
    targetAmount: 20000,
    currentAmount: 11000,
    targetDate: 'April 2025',
    emoji: '🏍️',
    isCompleted: false
  }
];

export const mockOrders: Order[] = [
  {
    id: 'ord-01',
    userId: 'usr-103',
    workerName: 'Parveen Akhtar',
    workerPhone: '+923218765432',
    factory: 'Artistic Milliners Garment Div',
    customerName: 'Farzana Kausar',
    phone: '03001239841',
    serviceTitle: '3 Ladies Fancy Suits Stitching with Embroidery',
    totalAmount: 7500,
    advancePaid: 3500,
    dueDate: '2025-03-20',
    isDelivered: false,
    isFullyPaid: false
  },
  {
    id: 'ord-02',
    userId: 'usr-101',
    workerName: 'Muhammad Tariq',
    workerPhone: '+923014589211',
    factory: 'Nishat Mills (Weaving Unit 4)',
    customerName: 'Malik Sohail',
    phone: '03138844112',
    serviceTitle: 'Washing Machine Motor Winding & Servicing',
    totalAmount: 3800,
    advancePaid: 3800,
    dueDate: '2025-03-08',
    isDelivered: true,
    isFullyPaid: true
  }
];

export const mockLockers: EmergencyLocker[] = [
  {
    id: 'lok-01',
    userId: 'usr-101',
    workerName: 'Muhammad Tariq',
    workerPhone: '+923014589211',
    factory: 'Nishat Mills (Weaving Unit 4)',
    balance: 12500,
    updatedAt: '2025-03-05T12:00:00.000Z'
  },
  {
    id: 'lok-02',
    userId: 'usr-102',
    workerName: 'Bashir Ahmed',
    workerPhone: '+923129845120',
    factory: 'Crescent Bahuman Denim Plant',
    balance: 4500,
    updatedAt: '2025-03-02T10:00:00.000Z'
  },
  {
    id: 'lok-03',
    userId: 'usr-103',
    workerName: 'Parveen Akhtar',
    workerPhone: '+923218765432',
    factory: 'Artistic Milliners Garment Div',
    balance: 24000,
    updatedAt: '2025-03-08T18:00:00.000Z'
  },
  {
    id: 'lok-04',
    userId: 'usr-105',
    workerName: 'Zahid Mehmood',
    workerPhone: '+923451122334',
    factory: 'Interloop Hosiery Plant 1 (Faisalabad)',
    balance: 32000,
    updatedAt: '2025-03-07T09:30:00.000Z'
  }
];

export const mockProsperityScores: ProsperityScore[] = [
  {
    id: 'pr-01',
    userId: 'usr-101',
    workerName: 'Muhammad Tariq',
    workerPhone: '+923014589211',
    factory: 'Nishat Mills (Weaving Unit 4)',
    score: 68,
    maxScore: 100,
    savingsPct: 0.15,
    debtControlPct: 0.75,
    safetyShieldPct: 0.9,
    daysRunway: 18,
    lastCalculatedAt: '2025-03-10T12:00:00.000Z',
    pillars: [
      { id: 1, titleEnglish: 'Budgeting & Expense Discipline', titleUrdu: 'بجٹ اور اخراجات پر قابو', weightPercent: 20, currentScore: 16, maxScore: 20, statusText: 'مستحکم', statusColorType: 'SUCCESS' },
      { id: 2, titleEnglish: 'Regular Savings Habit', titleUrdu: 'مسلسل بچت کی عادت', weightPercent: 20, currentScore: 14, maxScore: 20, statusText: 'بہترین', statusColorType: 'SUCCESS' },
      { id: 3, titleEnglish: 'Emergency Safety Buffer', titleUrdu: 'ہنگامی تحفظ کا بفر', weightPercent: 20, currentScore: 12, maxScore: 20, statusText: '18 دن کا بفر', statusColorType: 'WARNING' },
      { id: 4, titleEnglish: 'Debt & Borrowing Control', titleUrdu: 'قرضوں پر کنٹرول', weightPercent: 15, currentScore: 10, maxScore: 15, statusText: 'قرض قابو میں ہے', statusColorType: 'SUCCESS' },
      { id: 5, titleEnglish: 'Digital Safety & Fraud Defense', titleUrdu: 'ڈیجیٹل تحفظ اور آگاہی', weightPercent: 10, currentScore: 9, maxScore: 10, statusText: 'محفوظ', statusColorType: 'SUCCESS' },
      { id: 6, titleEnglish: 'Income Resilience & Goals', titleUrdu: 'آمدنی کا استحکام اور اہداف', weightPercent: 15, currentScore: 7, maxScore: 15, statusText: '1 فعال ہدف', statusColorType: 'WARNING' }
    ]
  },
  {
    id: 'pr-02',
    userId: 'usr-103',
    workerName: 'Parveen Akhtar',
    workerPhone: '+923218765432',
    factory: 'Artistic Milliners Garment Div',
    score: 84,
    maxScore: 100,
    savingsPct: 0.25,
    debtControlPct: 1.0,
    safetyShieldPct: 1.0,
    daysRunway: 32,
    lastCalculatedAt: '2025-03-10T11:00:00.000Z',
    pillars: [
      { id: 1, titleEnglish: 'Budgeting & Expense Discipline', titleUrdu: 'بجٹ اور اخراجات پر قابو', weightPercent: 20, currentScore: 18, maxScore: 20, statusText: 'بہترین', statusColorType: 'SUCCESS' },
      { id: 2, titleEnglish: 'Regular Savings Habit', titleUrdu: 'مسلسل بچت کی عادت', weightPercent: 20, currentScore: 18, maxScore: 20, statusText: 'شاندار', statusColorType: 'SUCCESS' },
      { id: 3, titleEnglish: 'Emergency Safety Buffer', titleUrdu: 'ہنگامی تحفظ کا بفر', weightPercent: 20, currentScore: 18, maxScore: 20, statusText: '32 دن کا بفر', statusColorType: 'SUCCESS' },
      { id: 4, titleEnglish: 'Debt & Borrowing Control', titleUrdu: 'قرضوں پر کنٹرول', weightPercent: 15, currentScore: 15, maxScore: 15, statusText: 'کوئی قرض نہیں', statusColorType: 'SUCCESS' },
      { id: 5, titleEnglish: 'Digital Safety & Fraud Defense', titleUrdu: 'ڈیجیٹل تحفظ اور آگاہی', weightPercent: 10, currentScore: 9, maxScore: 10, statusText: 'محفوظ', statusColorType: 'SUCCESS' },
      { id: 6, titleEnglish: 'Income Resilience & Goals', titleUrdu: 'آمدنی کا استحکام اور اہداف', weightPercent: 15, currentScore: 6, maxScore: 15, statusText: 'اضافی کاروبار جاری', statusColorType: 'SUCCESS' }
    ]
  }
];

export const mockNotifications: NotificationItem[] = [
  {
    id: 'notif-01',
    workerName: 'All / Broadcast',
    titleEnglish: 'Factory Advance Salary Disbursement Notice',
    titleUrdu: 'فیکٹری ایڈوانس تنخواہ کا شیڈول',
    descriptionEnglish: 'Advance payments for upcoming Eid will be processed by payroll on 24th March.',
    descriptionUrdu: 'آئندہ عید کے لیے ایڈوانس تنخواہیں 24 مارچ کو فیکٹری اکاؤنٹ میں ٹرانسفر ہوں گی۔',
    category: 'SYSTEM',
    isRead: true,
    createdAt: '2025-03-09T08:00:00.000Z'
  },
  {
    id: 'notif-02',
    workerName: 'Nishat Mills (Weaving Unit 4)',
    titleEnglish: 'Electricity Peak Hours Rate Alert',
    titleUrdu: 'لیسکو پیک آورز بجلی کے نرخوں میں اضافہ',
    descriptionEnglish: 'Avoid running heavy appliances during 6 PM - 10 PM to protect your budget.',
    descriptionUrdu: 'شام 6 سے رات 10 بجے کے دوران بجلی کے نرخ زیادہ ہیں۔ غیر ضروری لائٹس بند رکھیں۔',
    category: 'BILL',
    isRead: false,
    createdAt: '2025-03-08T15:30:00.000Z'
  }
];

export const mockCoachLogs: CoachLog[] = [
  {
    id: 'cl-01',
    workerName: 'Muhammad Tariq',
    workerPhone: '+923014589211',
    factory: 'Nishat Mills (Weaving Unit 4)',
    textUrdu: 'بچوں کے اسکول کی فیس کے لیے پیسے کیسے بچاؤں؟',
    textRoman: 'Bachon ke school ki fees ke liye paise kaise bachaoon?',
    isFromCoach: false,
    createdAt: '2025-03-10T10:15:00.000Z'
  },
  {
    id: 'cl-02',
    workerName: 'Muhammad Tariq',
    workerPhone: '+923014589211',
    factory: 'Nishat Mills (Weaving Unit 4)',
    textUrdu: 'بہترین! آپ کی ماہانہ بچت کا ہدف 3,000 روپے مقرر کریں۔ اپنی سیونگز اینولپ میں ہر ہفتے 750 روپے الگ رکھیں۔',
    textRoman: 'Behtareen! Aap ki maahana bachat ka hadaf 3,000 rupaye muqarrar karein. Har hafte 750 rupaye alag rakhein.',
    isFromCoach: true,
    createdAt: '2025-03-10T10:15:05.000Z'
  },
  {
    id: 'cl-03',
    workerName: 'Bashir Ahmed',
    workerPhone: '+923129845120',
    factory: 'Crescent Bahuman Denim Plant',
    textUrdu: 'دکاندار کے 22 ہزار کے ادھار سے پریشان ہوں، کیا کروں؟',
    textRoman: 'Dukandar ke 22 hazar ke udhaar se pareshan hoon, kya karoon?',
    isFromCoach: false,
    createdAt: '2025-03-09T18:10:00.000Z'
  },
  {
    id: 'cl-04',
    workerName: 'Bashir Ahmed',
    workerPhone: '+923129845120',
    factory: 'Crescent Bahuman Denim Plant',
    textUrdu: 'پریشان نہ ہوں! دکاندار سے بات کر کے ماہانہ 4,000 کی قسط طے کریں۔ پہلے ادھار ادا کریں تاکہ نیا بوجھ نہ بنے۔',
    textRoman: 'Pareshan na hon! Dukandar se baat kar ke maahana 4,000 ki qist tay karein. Pehle udhaar ada karein.',
    isFromCoach: true,
    createdAt: '2025-03-09T18:10:06.000Z'
  }
];

export const mockDashboardStats: DashboardStats = {
  workers: {
    total: 248,
    active: 236,
    inactive: 12
  },
  transactions: {
    count: 1420,
    volume: 6840000,
    expenses: 4920000,
    income: 7600000,
    netSavings: 2680000
  },
  debts: {
    count: 86,
    totalRemaining: 1840000,
    monthlyBurden: 310000
  },
  kametis: {
    activeCommittees: 34,
    poolValue: 1720000,
    monthlyContributionSum: 215000
  },
  emergencyLocker: {
    totalReserve: 2840000,
    avgWorkerReserve: 11450
  },
  bills: {
    unpaidCount: 42,
    unpaidAmount: 198500,
    paidCount: 178,
    paidAmount: 840000
  },
  orders: {
    count: 29,
    revenue: 142000,
    delivered: 21
  },
  prosperity: {
    averageScore: 64,
    highTier: 82,
    midTier: 134,
    lowTier: 32
  },
  factoryDistribution: [
    { factory: 'Nishat Mills Ltd', workersCount: 78 },
    { factory: 'Crescent Bahuman Denim', workersCount: 54 },
    { factory: 'Artistic Milliners', workersCount: 42 },
    { factory: 'Interloop Hosiery Plant', workersCount: 38 },
    { factory: 'Lucky Textile Mills', workersCount: 24 },
    { factory: 'Sapphire Finishing', workersCount: 12 }
  ],
  recentTransactions: [
    {
      id: 'tx-001',
      title: 'Monthly Factory Salary & Production Bonus',
      amount: 52000,
      isExpense: false,
      category: 'Salary',
      date: '2025-03-10T09:00:00.000Z',
      workerName: 'Muhammad Tariq',
      factory: 'Nishat Mills'
    },
    {
      id: 'tx-002',
      title: 'Household Ration & Grocery Envelope',
      amount: 19500,
      isExpense: true,
      category: 'Groceries',
      date: '2025-03-09T17:30:00.000Z',
      workerName: 'Muhammad Tariq',
      factory: 'Nishat Mills'
    },
    {
      id: 'tx-003',
      title: 'Stitching Microenterprise Service',
      amount: 8500,
      isExpense: false,
      category: 'Side Hustle',
      date: '2025-03-09T14:15:00.000Z',
      workerName: 'Parveen Akhtar',
      factory: 'Artistic Milliners'
    },
    {
      id: 'tx-004',
      title: 'Emergency Medical Hospital Fee',
      amount: 6200,
      isExpense: true,
      category: 'Healthcare',
      date: '2025-03-08T19:00:00.000Z',
      workerName: 'Bashir Ahmed',
      factory: 'Crescent Bahuman'
    }
  ],
  recentWorkers: [
    {
      id: 'usr-106',
      name: 'Khadija Bibi',
      phone: '+923087788990',
      factory: 'Sapphire Textile Finishing Unit',
      createdAt: '2025-03-09T14:20:00.000Z',
      isActive: true
    },
    {
      id: 'usr-105',
      name: 'Zahid Mehmood',
      phone: '+923451122334',
      factory: 'Interloop Hosiery Plant 1 (Faisalabad)',
      createdAt: '2025-03-07T11:00:00.000Z',
      isActive: true
    }
  ]
};

export const mockSystemHealth: SystemHealth = {
  status: 'healthy',
  serverTime: new Date().toISOString(),
  uptimeSeconds: 84320,
  nodeVersion: 'v24.14.0',
  database: {
    connected: true,
    name: 'khushhaal_db',
    host: 'cluster0.mongodb.net'
  },
  memory: {
    rssMb: 64,
    heapUsedMb: 38,
    heapTotalMb: 52
  },
  collectionCounts: {
    users: 248,
    transactions: 1420,
    bills: 220,
    debts: 86,
    kametis: 34,
    envelopes: 992,
    lockers: 248,
    goals: 118,
    orders: 29,
    prosperities: 248
  }
};
