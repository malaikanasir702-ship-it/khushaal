import React, { useEffect, useState } from 'react';
import {
  Users,
  CreditCard,
  ShieldAlert,
  HandCoins,
  Coins,
  Receipt,
  ArrowUpRight,
  ArrowDownRight,
  Factory,
  TrendingUp,
  RefreshCw,
  Megaphone,
  UserPlus
} from 'lucide-react';
import { StatCard } from '../components/StatCard';
import { Badge } from '../components/Badge';
import { DashboardStats } from '../types';
import { api } from '../services/api';
import { NavTab } from '../components/Sidebar';

interface DashboardProps {
  onNavigate: (tab: NavTab) => void;
  onOpenAddUser?: () => void;
}

export const Dashboard: React.FC<DashboardProps> = ({ onNavigate, onOpenAddUser }) => {
  const [stats, setStats] = useState<DashboardStats | null>(null);
  const [loading, setLoading] = useState<boolean>(true);

  const loadData = async () => {
    setLoading(true);
    try {
      const data = await api.getDashboardStats();
      setStats(data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const formatPKR = (amount: number) => {
    return `Rs. ${Number(amount || 0).toLocaleString('en-PK')}`;
  };

  if (loading && !stats) {
    return (
      <div className="flex items-center justify-center min-h-[400px]">
        <div className="flex flex-col items-center gap-2">
          <RefreshCw className="w-6 h-6 animate-spin text-neutral-400" />
          <p className="text-xs text-neutral-500 font-medium">Loading platform analytics...</p>
        </div>
      </div>
    );
  }

  const s = stats!;

  return (
    <div className="space-y-6">
      {/* Top Banner / Welcome */}
      <div className="bg-white border border-neutral-200 rounded-xl p-6 flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div>
          <div className="flex items-center gap-2">
            <h2 className="text-xl font-bold tracking-tight text-neutral-900">
              Khushhaal Workforce Operations Control
            </h2>
            <Badge variant="neutral">Superadmin Portal</Badge>
          </div>
          <p className="text-xs text-neutral-500 mt-1 max-w-2xl">
            Real-time administrative supervision across registered Pakistani factory workers, community savings pools, utility debt relief, and AI financial coaching.
          </p>
        </div>
        <div className="flex items-center gap-2">
          <button
            onClick={loadData}
            className="flex items-center gap-1.5 px-3 py-1.5 border border-neutral-200 bg-white hover:bg-neutral-50 rounded-md text-xs font-medium text-neutral-700 transition-colors"
          >
            <RefreshCw className={`w-3.5 h-3.5 ${loading ? 'animate-spin' : ''}`} />
            <span>Refresh Stats</span>
          </button>
          {onOpenAddUser && (
            <button
              onClick={onOpenAddUser}
              className="flex items-center gap-1.5 px-3.5 py-1.5 bg-neutral-900 hover:bg-neutral-800 text-white rounded-md text-xs font-medium shadow-xs transition-colors"
            >
              <UserPlus className="w-3.5 h-3.5" />
              <span>Onboard Worker</span>
            </button>
          )}
        </div>
      </div>

      {/* 6 Metric Stat Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
        <StatCard
          title="Registered Workforce"
          urduTitle="کل ورکرز"
          value={s.workers.total.toLocaleString()}
          subtitle={`${s.workers.active} Active Workers`}
          icon={Users}
          trend={{ value: `${s.workers.inactive} Suspended`, isPositive: false }}
          onClick={() => onNavigate('users')}
        />

        <StatCard
          title="Platform Ledger Volume"
          urduTitle="کل لین دین"
          value={formatPKR(s.transactions.volume)}
          subtitle={`${s.transactions.count} Total Records`}
          icon={CreditCard}
          trend={{ value: `Net Savings: ${formatPKR(s.transactions.netSavings)}`, isPositive: true }}
          onClick={() => onNavigate('transactions')}
        />

        <StatCard
          title="Emergency Locker Reserve"
          urduTitle="ہنگامی لاکر تحفظ"
          value={formatPKR(s.emergencyLocker.totalReserve)}
          subtitle={`Avg ${formatPKR(s.emergencyLocker.avgWorkerReserve)} / worker`}
          icon={ShieldAlert}
          trend={{ value: 'Financial Safety Cushion', isPositive: true }}
          onClick={() => onNavigate('locker')}
        />

        <StatCard
          title="Outstanding Worker Debts"
          urduTitle="قرض و ادھار واجبات"
          value={formatPKR(s.debts.totalRemaining)}
          subtitle={`${s.debts.count} Active Credit Lines`}
          icon={HandCoins}
          trend={{ value: `Monthly: ${formatPKR(s.debts.monthlyBurden)}`, isPositive: false }}
          onClick={() => onNavigate('debts')}
        />

        <StatCard
          title="Active Kameti Pools"
          urduTitle="کمیٹی بچت پول"
          value={formatPKR(s.kametis.poolValue)}
          subtitle={`${s.kametis.activeCommittees} Committees Running`}
          icon={Coins}
          trend={{ value: `Monthly Inflow: ${formatPKR(s.kametis.monthlyContributionSum)}`, isPositive: true }}
          onClick={() => onNavigate('kametis')}
        />

        <StatCard
          title="Pending Utility Bills"
          urduTitle="غیر ادا شدہ بلز"
          value={formatPKR(s.bills.unpaidAmount)}
          subtitle={`${s.bills.unpaidCount} Overdue Invoices`}
          icon={Receipt}
          trend={{ value: `${s.bills.paidCount} Paid on time`, isPositive: true }}
          onClick={() => onNavigate('bills')}
        />
      </div>

      {/* Grid: Financial Inflow/Outflow Breakdown & Prosperity Distribution */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Financial Flow Overview */}
        <div className="bg-white border border-neutral-200 rounded-xl p-6 lg:col-span-2 space-y-6">
          <div className="flex items-center justify-between pb-4 border-b border-neutral-100">
            <div>
              <h3 className="text-sm font-bold tracking-tight text-neutral-900 uppercase">
                Workforce Financial Flow (PKR)
              </h3>
              <p className="text-xs text-neutral-500 mt-0.5">
                Total aggregate monthly worker earnings, living expenditures, and retained savings
              </p>
            </div>
            <button
              onClick={() => onNavigate('transactions')}
              className="text-xs text-neutral-900 font-medium hover:underline flex items-center gap-1"
            >
              <span>View Ledger</span>
              <ArrowUpRight className="w-3.5 h-3.5" />
            </button>
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
            <div className="p-4 rounded-lg bg-neutral-50 border border-neutral-100">
              <div className="flex items-center gap-1.5 text-xs text-neutral-500">
                <ArrowDownRight className="w-4 h-4 text-emerald-600" />
                <span>Total Recorded Income</span>
              </div>
              <p className="text-xl font-bold text-neutral-900 mt-1">
                {formatPKR(s.transactions.income)}
              </p>
              <p className="text-[11px] text-neutral-400 font-urdu mt-0.5">تنخواہ و اوور ٹائم</p>
            </div>

            <div className="p-4 rounded-lg bg-neutral-50 border border-neutral-100">
              <div className="flex items-center gap-1.5 text-xs text-neutral-500">
                <ArrowUpRight className="w-4 h-4 text-rose-600" />
                <span>Household Outflows</span>
              </div>
              <p className="text-xl font-bold text-neutral-900 mt-1">
                {formatPKR(s.transactions.expenses)}
              </p>
              <p className="text-[11px] text-neutral-400 font-urdu mt-0.5">راشن، کرایہ و بلز</p>
            </div>

            <div className="p-4 rounded-lg bg-neutral-50 border border-neutral-100">
              <div className="flex items-center gap-1.5 text-xs text-neutral-500">
                <TrendingUp className="w-4 h-4 text-neutral-900" />
                <span>Retained Buffer</span>
              </div>
              <p className="text-xl font-bold text-neutral-900 mt-1">
                {formatPKR(s.transactions.netSavings)}
              </p>
              <p className="text-[11px] text-neutral-400 font-urdu mt-0.5">بچت و کمیٹی ذخائر</p>
            </div>
          </div>

          {/* Visual Ratio Bar */}
          <div className="space-y-2 pt-2">
            <div className="flex justify-between text-xs text-neutral-600">
              <span>Expenses vs Savings Allocation</span>
              <span className="font-mono">
                {Math.round((s.transactions.expenses / (s.transactions.income || 1)) * 100)}% Spent / {Math.round((s.transactions.netSavings / (s.transactions.income || 1)) * 100)}% Saved
              </span>
            </div>
            <div className="h-3.5 w-full bg-neutral-100 rounded-full overflow-hidden flex border border-neutral-200">
              <div 
                className="bg-neutral-900 transition-all duration-500" 
                style={{ width: `${Math.min(100, Math.round((s.transactions.expenses / (s.transactions.income || 1)) * 100))}%` }} 
                title="Expenses"
              />
              <div 
                className="bg-emerald-500 transition-all duration-500" 
                style={{ width: `${Math.max(0, Math.min(100, Math.round((s.transactions.netSavings / (s.transactions.income || 1)) * 100)))}%` }} 
                title="Retained Savings"
              />
            </div>
            <div className="flex items-center gap-4 text-[11px] text-neutral-500 pt-1">
              <span className="flex items-center gap-1.5">
                <span className="w-2.5 h-2.5 rounded-xs bg-neutral-900 inline-block" />
                Living Costs
              </span>
              <span className="flex items-center gap-1.5">
                <span className="w-2.5 h-2.5 rounded-xs bg-emerald-500 inline-block" />
                Emergency & Goal Reserves
              </span>
            </div>
          </div>
        </div>

        {/* Prosperity Index Health Distribution */}
        <div className="bg-white border border-neutral-200 rounded-xl p-6 space-y-5">
          <div className="flex items-center justify-between pb-3 border-b border-neutral-100">
            <div>
              <h3 className="text-sm font-bold tracking-tight text-neutral-900 uppercase">
                Prosperity Index
              </h3>
              <p className="text-xs text-neutral-500 font-urdu mt-0.5">
                مزدوروں کا مالیاتی خوشحالی تناسب
              </p>
            </div>
            <div className="text-right">
              <span className="text-2xl font-bold text-neutral-900">
                {s.prosperity.averageScore}
              </span>
              <span className="text-xs text-neutral-400 font-mono">/100</span>
            </div>
          </div>

          <div className="space-y-3.5">
            <div>
              <div className="flex justify-between text-xs mb-1">
                <span className="font-medium text-emerald-800 flex items-center gap-1.5">
                  <span className="w-2 h-2 rounded-full bg-emerald-500" />
                  High Prosperity (&gt; 70)
                </span>
                <span className="font-mono text-neutral-600">{s.prosperity.highTier} Workers</span>
              </div>
              <div className="h-2 w-full bg-neutral-100 rounded-full overflow-hidden">
                <div 
                  className="h-full bg-emerald-500" 
                  style={{ width: `${(s.prosperity.highTier / (s.workers.total || 1)) * 100}%` }} 
                />
              </div>
            </div>

            <div>
              <div className="flex justify-between text-xs mb-1">
                <span className="font-medium text-amber-800 flex items-center gap-1.5">
                  <span className="w-2 h-2 rounded-full bg-amber-500" />
                  Moderate Stability (30-70)
                </span>
                <span className="font-mono text-neutral-600">{s.prosperity.midTier} Workers</span>
              </div>
              <div className="h-2 w-full bg-neutral-100 rounded-full overflow-hidden">
                <div 
                  className="h-full bg-amber-500" 
                  style={{ width: `${(s.prosperity.midTier / (s.workers.total || 1)) * 100}%` }} 
                />
              </div>
            </div>

            <div>
              <div className="flex justify-between text-xs mb-1">
                <span className="font-medium text-rose-800 flex items-center gap-1.5">
                  <span className="w-2 h-2 rounded-full bg-rose-500" />
                  High Financial Vulnerability (&lt; 30)
                </span>
                <span className="font-mono text-neutral-600">{s.prosperity.lowTier} Workers</span>
              </div>
              <div className="h-2 w-full bg-neutral-100 rounded-full overflow-hidden">
                <div 
                  className="h-full bg-rose-500" 
                  style={{ width: `${(s.prosperity.lowTier / (s.workers.total || 1)) * 100}%` }} 
                />
              </div>
            </div>
          </div>

          <button
            onClick={() => onNavigate('prosperity')}
            className="w-full mt-2 py-2 px-3 bg-neutral-50 hover:bg-neutral-100 text-neutral-800 border border-neutral-200 rounded-lg text-xs font-medium transition-colors text-center block"
          >
            Inspect 6 Pillars Breakdown
          </button>
        </div>
      </div>

      {/* Factory Distribution & Recent Activity */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Factory Distribution */}
        <div className="bg-white border border-neutral-200 rounded-xl p-6 space-y-4">
          <div className="flex items-center justify-between pb-3 border-b border-neutral-100">
            <div className="flex items-center gap-2">
              <Factory className="w-4 h-4 text-neutral-700" />
              <h3 className="text-sm font-bold tracking-tight text-neutral-900 uppercase">
                Workers per Industrial Plant
              </h3>
            </div>
            <button
              onClick={() => onNavigate('users')}
              className="text-xs text-neutral-600 hover:text-neutral-900 font-medium"
            >
              Filter Workers
            </button>
          </div>

          <div className="space-y-3">
            {s.factoryDistribution.map((item, index) => (
              <div key={index} className="flex items-center justify-between py-1.5 border-b border-neutral-50 last:border-0">
                <div className="truncate pr-4">
                  <p className="text-xs font-medium text-neutral-900 truncate">
                    {item.factory}
                  </p>
                  <p className="text-[10px] text-neutral-400">
                    Industrial Unit Affiliation
                  </p>
                </div>
                <div className="flex items-center gap-3 shrink-0">
                  <div className="w-24 bg-neutral-100 h-2 rounded-full overflow-hidden hidden sm:block">
                    <div 
                      className="bg-neutral-800 h-full rounded-full" 
                      style={{ width: `${Math.min(100, (item.workersCount / (s.workers.total || 1)) * 100)}%` }} 
                    />
                  </div>
                  <Badge variant="neutral">{item.workersCount} Workers</Badge>
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* Recent Ledger Records */}
        <div className="bg-white border border-neutral-200 rounded-xl p-6 space-y-4">
          <div className="flex items-center justify-between pb-3 border-b border-neutral-100">
            <div className="flex items-center gap-2">
              <CreditCard className="w-4 h-4 text-neutral-700" />
              <h3 className="text-sm font-bold tracking-tight text-neutral-900 uppercase">
                Recent Platform Transactions
              </h3>
            </div>
            <button
              onClick={() => onNavigate('transactions')}
              className="text-xs text-neutral-600 hover:text-neutral-900 font-medium"
            >
              All Records
            </button>
          </div>

          <div className="divide-y divide-neutral-100">
            {s.recentTransactions.map((tx) => (
              <div key={tx.id} className="py-2.5 flex items-center justify-between">
                <div className="truncate pr-4">
                  <p className="text-xs font-semibold text-neutral-900 truncate">
                    {tx.title}
                  </p>
                  <div className="flex items-center gap-2 text-[11px] text-neutral-500 mt-0.5">
                    <span>{tx.workerName}</span>
                    <span>•</span>
                    <span className="font-mono">{new Date(tx.date).toLocaleDateString()}</span>
                  </div>
                </div>
                <div className="text-right shrink-0">
                  <p className={`text-xs font-bold font-mono ${tx.isExpense ? 'text-rose-700' : 'text-emerald-700'}`}>
                    {tx.isExpense ? '-' : '+'} {formatPKR(tx.amount)}
                  </p>
                  <Badge variant="neutral" size="sm" className="mt-0.5">
                    {tx.category}
                  </Badge>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};
