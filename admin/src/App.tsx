import React, { useState } from 'react';
import { useAuth } from './context/AuthContext';
import { Login } from './pages/Login';
import { Layout } from './components/Layout';
import { NavTab } from './components/Sidebar';

import { Dashboard } from './pages/Dashboard';
import { Users } from './pages/Users';
import { Transactions } from './pages/Transactions';
import { Bills } from './pages/Bills';
import { Debts } from './pages/Debts';
import { Kametis } from './pages/Kametis';
import { EmergencyLockerPage } from './pages/EmergencyLocker';
import { Goals } from './pages/Goals';
import { Orders } from './pages/Orders';
import { ProsperityIndexPage } from './pages/ProsperityIndex';
import { Broadcast } from './pages/Broadcast';
import { CoachLogs } from './pages/CoachLogs';
import { SystemHealthPage } from './pages/SystemHealth';

export const AppContent: React.FC = () => {
  const { token, isLoading } = useAuth();
  const [currentTab, setCurrentTab] = useState<NavTab>('dashboard');

  if (isLoading) {
    return (
      <div className="min-h-screen bg-white flex items-center justify-center">
        <div className="flex flex-col items-center gap-2">
          <div className="w-8 h-8 rounded-full border-2 border-neutral-900 border-t-transparent animate-spin" />
          <p className="text-xs text-neutral-500 font-medium">Initializing Khushhaal Admin Console...</p>
        </div>
      </div>
    );
  }

  if (!token) {
    return <Login />;
  }

  const renderContent = () => {
    switch (currentTab) {
      case 'dashboard':
        return <Dashboard onNavigate={setCurrentTab} />;
      case 'users':
        return <Users />;
      case 'transactions':
        return <Transactions />;
      case 'bills':
        return <Bills />;
      case 'debts':
        return <Debts />;
      case 'kametis':
        return <Kametis />;
      case 'locker':
        return <EmergencyLockerPage />;
      case 'goals':
        return <Goals />;
      case 'orders':
        return <Orders />;
      case 'prosperity':
        return <ProsperityIndexPage />;
      case 'broadcast':
        return <Broadcast />;
      case 'coach':
        return <CoachLogs />;
      case 'health':
        return <SystemHealthPage />;
      default:
        return <Dashboard onNavigate={setCurrentTab} />;
    }
  };

  return (
    <Layout currentTab={currentTab} onTabChange={setCurrentTab}>
      {renderContent()}
    </Layout>
  );
};

export default function App() {
  return <AppContent />;
}
