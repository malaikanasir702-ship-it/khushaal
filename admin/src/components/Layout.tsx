import React, { useState } from 'react';
import { Sidebar, NavTab } from './Sidebar';
import { Header } from './Header';

interface LayoutProps {
  currentTab: NavTab;
  onTabChange: (tab: NavTab) => void;
  children: React.ReactNode;
  onQuickAction?: () => void;
}

export const Layout: React.FC<LayoutProps> = ({
  currentTab,
  onTabChange,
  children,
  onQuickAction
}) => {
  const [sidebarOpen, setSidebarOpen] = useState(false);

  return (
    <div className="min-h-screen bg-white flex flex-col text-neutral-900 selection:bg-neutral-900 selection:text-white">
      {/* Fixed Sidebar */}
      <Sidebar
        currentTab={currentTab}
        onTabChange={onTabChange}
        isOpen={sidebarOpen}
        onClose={() => setSidebarOpen(false)}
      />

      {/* Main Content Area */}
      <div className="lg:pl-64 flex flex-col flex-1 min-w-0">
        <Header
          currentTab={currentTab}
          onOpenSidebar={() => setSidebarOpen(true)}
          onQuickAction={onQuickAction}
        />

        <main className="flex-1 p-4 lg:p-8 bg-neutral-50/40">
          <div className="max-w-7xl mx-auto space-y-6">
            {children}
          </div>
        </main>
      </div>
    </div>
  );
};
