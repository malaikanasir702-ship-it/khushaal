import React from 'react';
import {
  LayoutDashboard,
  Users,
  CreditCard,
  Receipt,
  HandCoins,
  Coins,
  ShieldAlert,
  Target,
  ShoppingBag,
  TrendingUp,
  Megaphone,
  Bot,
  Activity,
  LogOut
} from 'lucide-react';
import { useAuth } from '../context/AuthContext';

export type NavTab = 
  | 'dashboard'
  | 'users'
  | 'transactions'
  | 'bills'
  | 'debts'
  | 'kametis'
  | 'locker'
  | 'goals'
  | 'orders'
  | 'prosperity'
  | 'broadcast'
  | 'coach'
  | 'health';

interface SidebarProps {
  currentTab: NavTab;
  onTabChange: (tab: NavTab) => void;
  isOpen: boolean;
  onClose: () => void;
}

export const Sidebar: React.FC<SidebarProps> = ({
  currentTab,
  onTabChange,
  isOpen,
  onClose
}) => {
  const { admin, logout } = useAuth();

  const navGroups = [
    {
      title: 'CORE PLATFORM',
      items: [
        { id: 'dashboard' as NavTab, label: 'Dashboard', urdu: 'ڈیش بورڈ', icon: LayoutDashboard },
        { id: 'users' as NavTab, label: 'Workers Directory', urdu: 'ورکرز ڈائریکٹری', icon: Users, badge: '248' },
        { id: 'transactions' as NavTab, label: 'Transactions Ledger', urdu: 'آمدن و اخراجات', icon: CreditCard },
        { id: 'locker' as NavTab, label: 'Emergency Locker', urdu: 'ہنگامی لاکر', icon: ShieldAlert },
      ]
    },
    {
      title: 'COMMITMENTS & SAVINGS',
      items: [
        { id: 'bills' as NavTab, label: 'Utility Bills', urdu: 'یوٹیلیٹی بلز', icon: Receipt },
        { id: 'debts' as NavTab, label: 'Debts & Udhaar', urdu: 'ادھار و واجبات', icon: HandCoins },
        { id: 'kametis' as NavTab, label: 'Kameti Committees', urdu: 'کمیٹیاں', icon: Coins },
        { id: 'goals' as NavTab, label: 'Savings Goals', urdu: 'بچت کے اہداف', icon: Target },
      ]
    },
    {
      title: 'ENTERPRISE & INTELLIGENCE',
      items: [
        { id: 'orders' as NavTab, label: 'Microenterprise Orders', urdu: 'کاروباری آرڈرز', icon: ShoppingBag },
        { id: 'prosperity' as NavTab, label: 'Prosperity Index (6 Pillars)', urdu: 'خوشحالی انڈیکس', icon: TrendingUp },
        { id: 'broadcast' as NavTab, label: 'Broadcast Center', urdu: 'پیغامات کی ترسیل', icon: Megaphone },
        { id: 'coach' as NavTab, label: 'AI Coach Fatima Logs', urdu: 'اے آئی کوچ فاطمہ', icon: Bot },
      ]
    },
    {
      title: 'OPERATIONS',
      items: [
        { id: 'health' as NavTab, label: 'System Health & DB', urdu: 'سسٹم صحت و سیٹنگز', icon: Activity },
      ]
    }
  ];

  return (
    <>
      {/* Mobile backdrop */}
      {isOpen && (
        <div
          className="fixed inset-0 bg-black/30 backdrop-blur-xs z-30 lg:hidden"
          onClick={onClose}
        />
      )}

      <aside
        className={`fixed top-0 bottom-0 left-0 z-40 w-64 bg-white border-r border-neutral-200 flex flex-col transition-transform duration-200 ease-in-out lg:translate-x-0 ${
          isOpen ? 'translate-x-0' : '-translate-x-full'
        }`}
      >
        {/* Header Branding */}
        <div className="p-5 border-b border-neutral-100 flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="w-9 h-9 rounded-lg bg-neutral-900 text-white flex items-center justify-center font-bold text-lg shadow-xs">
              🌱
            </div>
            <div>
              <div className="flex items-center gap-1.5">
                <span className="font-bold tracking-tight text-neutral-900 text-base">Khushhaal</span>
                <span className="text-[10px] font-semibold bg-neutral-100 text-neutral-800 px-1.5 py-0.5 rounded uppercase tracking-wider">
                  ADMIN
                </span>
              </div>
              <p className="text-[11px] text-neutral-400 font-urdu leading-none mt-0.5">
                مالیاتی فلاح و بہبود برائے ورکرز
              </p>
            </div>
          </div>
        </div>

        {/* Navigation List */}
        <nav className="flex-1 overflow-y-auto px-3 py-4 space-y-6">
          {navGroups.map((group, idx) => (
            <div key={idx} className="space-y-1">
              <p className="px-3 text-[10px] font-semibold tracking-wider text-neutral-400 uppercase">
                {group.title}
              </p>
              <div className="space-y-0.5 mt-1">
                {group.items.map((item) => {
                  const Icon = item.icon;
                  const isActive = currentTab === item.id;
                  return (
                    <button
                      key={item.id}
                      onClick={() => {
                        onTabChange(item.id);
                        onClose();
                      }}
                      className={`w-full flex items-center justify-between px-3 py-2 rounded-md text-xs font-medium transition-all ${
                        isActive
                          ? 'bg-neutral-900 text-white shadow-xs'
                          : 'text-neutral-600 hover:text-neutral-900 hover:bg-neutral-100'
                      }`}
                    >
                      <div className="flex items-center gap-2.5 truncate">
                        <Icon className={`w-4 h-4 shrink-0 ${isActive ? 'text-white' : 'text-neutral-500'}`} strokeWidth={1.8} />
                        <span className="truncate">{item.label}</span>
                      </div>
                      {item.badge && (
                        <span className={`text-[10px] px-1.5 py-0.2 rounded-full font-semibold ${
                          isActive ? 'bg-neutral-800 text-neutral-200' : 'bg-neutral-100 text-neutral-600'
                        }`}>
                          {item.badge}
                        </span>
                      )}
                    </button>
                  );
                })}
              </div>
            </div>
          ))}
        </nav>

        {/* Footer Admin Info & Controls */}
        <div className="p-3 border-t border-neutral-200 bg-neutral-50/50 space-y-2">
        {/* Admin Profile & Logout */}
          <div className="flex items-center justify-between px-2 pt-1">
            <div className="truncate pr-2">
              <p className="text-xs font-semibold text-neutral-900 truncate">
                {admin?.name || 'Super Admin'}
              </p>
              <p className="text-[11px] text-neutral-500 font-mono truncate">
                {admin?.phone || ''}
              </p>
            </div>
            <button
              onClick={logout}
              title="Sign Out"
              className="p-1.5 text-neutral-400 hover:text-rose-600 hover:bg-rose-50 rounded-md transition-colors"
            >
              <LogOut className="w-4 h-4" />
            </button>
          </div>
        </div>
      </aside>
    </>
  );
};
