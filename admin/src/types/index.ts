export interface WorkerUser {
  id: string;
  name: string;
  urduName: string;
  phone: string;
  cnic: string;
  factory: string;
  factoryId: string;
  jazzCashNumber: string;
  role: 'user' | 'admin';
  isActive: boolean;
  preferredLanguage: 'BILINGUAL' | 'URDU' | 'ENGLISH';
  createdAt: string;
  lastLoginAt?: string;
  lockerBalance?: number;
  prosperityScore?: number;
  unpaidBills?: number;
  totalDebts?: number;
  activeKametis?: number;
}

export interface EnvelopeSubItem {
  label: string;
  amount: number;
}

export interface Envelope {
  id: string;
  userId?: string;
  envelopeKey: 'needs' | 'commitments' | 'emergency' | 'savings';
  titleEnglish: string;
  titleUrdu: string;
  percentage: number;
  amount: number;
  tag: string;
  items: EnvelopeSubItem[];
}

export interface Transaction {
  id: string;
  userId: string | null;
  workerName: string;
  workerPhone?: string;
  factory?: string;
  title: string;
  urduTitle: string;
  amount: number;
  isExpense: boolean;
  category: string;
  envelopeId?: string;
  paymentMethod: string;
  date: string;
}

export interface Bill {
  id: string;
  userId: string | null;
  workerName: string;
  workerPhone?: string;
  factory?: string;
  companyName: string;
  companyUrdu: string;
  consumerNumber: string;
  billType: string;
  month: string;
  dueDate: string;
  amount: number;
  unitsConsumed: number;
  isPaid: boolean;
  paidDate?: string | null;
  alertTip?: string;
}

export interface Debt {
  id: string;
  userId: string | null;
  workerName: string;
  workerPhone?: string;
  factory?: string;
  creditorName: string;
  creditorUrdu: string;
  relationOrType: string;
  totalAmount: number;
  remainingAmount: number;
  monthlyCommitment: number;
  urgencyLevel: 'HIGH' | 'MEDIUM' | 'LOW';
  isShariahFriendly: boolean;
  repaymentStrategyTip: string;
}

export interface Kameti {
  id: string;
  userId: string | null;
  workerName: string;
  workerPhone?: string;
  factory?: string;
  name: string;
  urduName: string;
  monthlyAmount: number;
  totalMembers: number;
  myTurnMonth: number;
  currentMonth: number;
  payoutAmount: number;
  organizer: string;
  isPaidThisMonth: boolean;
}

export interface Goal {
  id: string;
  userId: string | null;
  workerName: string;
  workerPhone?: string;
  factory?: string;
  title: string;
  urduTitle: string;
  targetAmount: number;
  currentAmount: number;
  targetDate: string;
  emoji: string;
  isCompleted: boolean;
}

export interface Order {
  id: string;
  userId: string | null;
  workerName: string;
  workerPhone?: string;
  factory?: string;
  customerName: string;
  phone: string;
  serviceTitle: string;
  totalAmount: number;
  advancePaid: number;
  dueDate: string;
  isDelivered: boolean;
  isFullyPaid: boolean;
}

export interface EmergencyLocker {
  id: string;
  userId: string | null;
  workerName: string;
  workerPhone?: string;
  factory?: string;
  balance: number;
  updatedAt: string;
}

export interface Pillar {
  id: number;
  titleEnglish: string;
  titleUrdu: string;
  weightPercent: number;
  currentScore: number;
  maxScore: number;
  statusText: string;
  statusColorType: 'SUCCESS' | 'WARNING' | 'URGENT';
}

export interface ProsperityScore {
  id: string;
  userId: string | null;
  workerName: string;
  workerPhone?: string;
  factory?: string;
  score: number;
  maxScore: number;
  savingsPct: number;
  debtControlPct: number;
  safetyShieldPct: number;
  daysRunway: number;
  pillars: Pillar[];
  lastCalculatedAt: string;
}

export interface NotificationItem {
  id: string;
  workerName: string;
  titleEnglish: string;
  titleUrdu: string;
  descriptionEnglish: string;
  descriptionUrdu: string;
  category: string;
  isRead: boolean;
  createdAt: string;
}

export interface CoachLog {
  id: string;
  workerName: string;
  workerPhone: string;
  factory: string;
  textUrdu: string;
  textRoman: string;
  isFromCoach: boolean;
  createdAt: string;
}

export interface DashboardStats {
  workers: {
    total: number;
    active: number;
    inactive: number;
  };
  transactions: {
    count: number;
    volume: number;
    expenses: number;
    income: number;
    netSavings: number;
  };
  debts: {
    count: number;
    totalRemaining: number;
    monthlyBurden: number;
  };
  kametis: {
    activeCommittees: number;
    poolValue: number;
    monthlyContributionSum: number;
  };
  emergencyLocker: {
    totalReserve: number;
    avgWorkerReserve: number;
  };
  bills: {
    unpaidCount: number;
    unpaidAmount: number;
    paidCount: number;
    paidAmount: number;
  };
  orders: {
    count: number;
    revenue: number;
    delivered: number;
  };
  prosperity: {
    averageScore: number;
    highTier: number;
    midTier: number;
    lowTier: number;
  };
  factoryDistribution: Array<{
    factory: string;
    workersCount: number;
  }>;
  recentTransactions: Array<{
    id: string;
    title: string;
    amount: number;
    isExpense: boolean;
    category: string;
    date: string;
    workerName: string;
    factory: string;
  }>;
  recentWorkers: Array<{
    id: string;
    name: string;
    phone: string;
    factory: string;
    createdAt: string;
    isActive: boolean;
  }>;
}

export interface SystemHealth {
  status: string;
  serverTime: string;
  uptimeSeconds: number;
  nodeVersion: string;
  database: {
    connected: boolean;
    name: string;
    host: string;
  };
  memory: {
    rssMb: number;
    heapUsedMb: number;
    heapTotalMb: number;
  };
  collectionCounts: Record<string, number>;
}
