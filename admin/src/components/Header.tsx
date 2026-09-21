import React from 'react';
import { Menu, Search, Plus, Bell, ShieldCheck } from 'lucide-react';
import { NavTab } from './Sidebar';
import { useAuth } from '../context/AuthContext';

interface HeaderProps {
  currentTab: NavTab;
  onOpenSidebar: () => void;
  onQuickAction?: () => void;
}

const tabTitles: Record<NavTab, { title: string; urdu: string; desc: string }> = {
  dashboard: {
    title: 'Platform Overview & Analytics',
    urdu: 'پلیٹ فارم جائزہ اور اعداد و شمار',
    desc: 'Real-time overview of workforce financial wellness, savings pools, and debt burdens'
  },
  users: {
    title: 'Workers Directory & Dossiers',
    urdu: 'رجسٹرڈ مزدوروں کی مکمل تفصیلات',
    desc: 'Manage all enrolled factory workers, CNIC records, and 360-degree financial profiles'
  },
  transactions: {
    title: 'Financial Transactions Ledger',
    urdu: 'آمدن و اخراجات کا مکمل کھاتہ',
    desc: 'All recorded factory wages, daily rations, utility disbursements, and cashflow movements'
  },
  bills: {
    title: 'Utility Invoices & Bills Oversight',
    urdu: 'بجلی، گیس اور پانی کے بلز کا انتظام',
    desc: 'Track consumer numbers, due dates, units consumed, and payment statuses'
  },
  debts: {
    title: 'Worker Debts & Udhaar Register',
    urdu: 'قرض حسنہ، کریانہ ادھار اور واجبات',
    desc: 'Supervise worker borrowings, shopkeeper debts, and Shariah-compliant payoff strategies'
  },
  kametis: {
    title: 'Rotating Committees (BC / Kameti)',
    urdu: 'ماہانہ کمیٹیاں اور بچت گروپس',
    desc: 'Traditional informal savings pools, monthly turns, and collective payout disbursements'
  },
  locker: {
    title: 'Emergency Locker Buffers',
    urdu: 'ہنگامی لاکر اور محفوظ بچت',
    desc: 'Worker safety cushions, reserve health, and administrative emergency grants'
  },
  goals: {
    title: 'Targeted Savings Goals',
    urdu: 'مستقبل کے اہداف اور پیش رفت',
    desc: 'Track worker savings towards children education, transport, and family emergencies'
  },
  orders: {
    title: 'Microenterprise & Side Hustles',
    urdu: 'چھوٹے کاروبار، سلائی اور مرمت کے آرڈرز',
    desc: 'Worker supplementary income builder, client orders, advances, and delivery status'
  },
  prosperity: {
    title: 'Prosperity Index (6 Pillars)',
    urdu: '6 ستونوں پر مبنی مالیاتی خوشحالی اسکور',
    desc: 'Financial discipline, debt control, safety buffer, and digital literacy score engine'
  },
  broadcast: {
    title: 'Broadcast Alerts & Notifications',
    urdu: 'ورکرز کو اہم پیغامات اور اعلانات',
    desc: 'Dispatch urgent push notifications and reminders by factory or platform-wide'
  },
  coach: {
    title: 'AI Financial Coach Fatima Conversations',
    urdu: 'اے آئی مالیاتی کوچ فاطمہ کے مکالمات',
    desc: 'Audit conversations and financial guidance given to factory workers'
  },
  health: {
    title: 'System Health & Administrative Settings',
    urdu: 'سرور کی صحت اور ایڈمن ترتیبات',
    desc: 'MongoDB connections, API uptime, system collections, and administrative credentials'
  }
};

export const Header: React.FC<HeaderProps> = ({
  currentTab,
  onOpenSidebar,
  onQuickAction
}) => {
  const { admin } = useAuth();
  const info = tabTitles[currentTab] || tabTitles.dashboard;

  return (
    <header className="sticky top-0 z-20 bg-white/95 backdrop-blur-md border-b border-neutral-200 px-4 lg:px-8 py-3.5">
      <div className="flex items-center justify-between gap-4">
        {/* Left: Mobile Toggle & Page Title */}
        <div className="flex items-center gap-3">
          <button
            onClick={onOpenSidebar}
            className="p-1.5 -ml-1.5 text-neutral-600 hover:text-neutral-900 rounded-md hover:bg-neutral-100 lg:hidden"
          >
            <Menu className="w-5 h-5" />
          </button>
          <div>
            <div className="flex items-center gap-2">
              <h1 className="text-base lg:text-lg font-bold tracking-tight text-neutral-900">
                {info.title}
              </h1>
              <span className="hidden sm:inline-block font-urdu text-neutral-400 text-xs font-normal">
                ({info.urdu})
              </span>
            </div>
            <p className="text-xs text-neutral-500 hidden md:block">
              {info.desc}
            </p>
          </div>
        </div>

        {/* Right: Quick actions & status */}
        <div className="flex items-center gap-2.5">
          {/* Status Indicator */}
          <div className="hidden sm:flex items-center gap-1.5 px-2.5 py-1 rounded-full border border-neutral-200 bg-neutral-50 text-[11px] font-medium text-neutral-700">
            <span className="w-2 h-2 rounded-full bg-emerald-500 animate-pulse" />
            <span>Live Connected</span>
          </div>

          {/* Quick Action Button */}
          {onQuickAction && (
            <button
              onClick={onQuickAction}
              className="flex items-center gap-1.5 bg-neutral-900 hover:bg-neutral-800 text-white px-3 py-1.5 rounded-md text-xs font-medium shadow-xs transition-colors"
            >
              <Plus className="w-3.5 h-3.5" />
              <span className="hidden sm:inline">Add Record</span>
              <span className="sm:hidden">Add</span>
            </button>
          )}

          {/* Admin Avatar */}
          <div className="w-8 h-8 rounded-full bg-neutral-900 text-white flex items-center justify-center font-bold text-xs border border-neutral-200">
            {admin?.name ? admin.name.charAt(0).toUpperCase() : 'A'}
          </div>
        </div>
      </div>
    </header>
  );
};
