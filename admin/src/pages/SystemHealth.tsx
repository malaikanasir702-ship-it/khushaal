import React, { useState, useEffect } from 'react';
import {
  Activity,
  Database,
  Server,
  Cpu,
  RefreshCw,
  ShieldCheck,
  HardDrive,
  Key,
  Layers
} from 'lucide-react';
import { SystemHealth } from '../types';
import { api } from '../services/api';
import { Badge } from '../components/Badge';
import { useAuth } from '../context/AuthContext';

export const SystemHealthPage: React.FC = () => {
  const [health, setHealth] = useState<SystemHealth | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const { admin, demoMode, toggleDemoMode } = useAuth();

  const loadHealth = async () => {
    setLoading(true);
    try {
      const data = await api.getSystemHealth();
      setHealth(data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadHealth();
  }, []);

  const formatUptime = (seconds: number) => {
    const d = Math.floor(seconds / (3600 * 24));
    const h = Math.floor((seconds % (3600 * 24)) / 3600);
    const m = Math.floor((seconds % 3600) / 60);
    return `${d}d ${h}h ${m}m`;
  };

  return (
    <div className="space-y-6">
      {/* Top Banner */}
      <div className="bg-white border border-neutral-200 rounded-xl p-6 flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div>
          <div className="flex items-center gap-2">
            <h3 className="text-base font-bold text-neutral-900 tracking-tight flex items-center gap-2">
              <Activity className="w-5 h-5 text-neutral-900" />
              <span>Platform Infrastructure & System Health</span>
            </h3>
            <Badge variant="success">Operational</Badge>
          </div>
          <p className="text-xs text-neutral-500 mt-1">
            Real-time status of Node.js Express server, MongoDB Atlas connectivity, memory overhead, and collection volumes.
          </p>
        </div>
        <button
          onClick={loadHealth}
          className="flex items-center gap-1.5 px-3 py-1.5 border border-neutral-200 bg-white hover:bg-neutral-50 rounded-lg text-xs font-medium text-neutral-700 transition-colors"
        >
          <RefreshCw className="w-3.5 h-3.5" />
          <span>Refresh System Metrics</span>
        </button>
      </div>

      {health && (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
          <div className="bg-white border border-neutral-200 rounded-xl p-4 space-y-1">
            <div className="flex items-center gap-1.5 text-xs text-neutral-500">
              <Database className="w-4 h-4 text-emerald-600" />
              <span>Database Connection</span>
            </div>
            <p className="text-lg font-bold text-neutral-900">
              {health.database.connected ? 'Connected' : 'Degraded'}
            </p>
            <p className="text-[11px] font-mono text-neutral-400 truncate">
              {health.database.name} ({health.database.host})
            </p>
          </div>

          <div className="bg-white border border-neutral-200 rounded-xl p-4 space-y-1">
            <div className="flex items-center gap-1.5 text-xs text-neutral-500">
              <Server className="w-4 h-4 text-neutral-800" />
              <span>Server Uptime</span>
            </div>
            <p className="text-lg font-bold font-mono text-neutral-900">
              {formatUptime(health.uptimeSeconds)}
            </p>
            <p className="text-[11px] font-mono text-neutral-400">
              Node {health.nodeVersion}
            </p>
          </div>

          <div className="bg-white border border-neutral-200 rounded-xl p-4 space-y-1">
            <div className="flex items-center gap-1.5 text-xs text-neutral-500">
              <Cpu className="w-4 h-4 text-neutral-800" />
              <span>Memory Heap Usage</span>
            </div>
            <p className="text-lg font-bold font-mono text-neutral-900">
              {health.memory.heapUsedMb} MB / {health.memory.heapTotalMb} MB
            </p>
            <p className="text-[11px] font-mono text-neutral-400">
              RSS: {health.memory.rssMb} MB
            </p>
          </div>

          <div className="bg-white border border-neutral-200 rounded-xl p-4 space-y-1">
            <div className="flex items-center gap-1.5 text-xs text-neutral-500">
              <HardDrive className="w-4 h-4 text-neutral-800" />
              <span>Total Collections Monitored</span>
            </div>
            <p className="text-lg font-bold font-mono text-neutral-900">
              {Object.keys(health.collectionCounts || {}).length} Collections
            </p>
            <p className="text-[11px] text-neutral-400">
              Mongoose Schemas
            </p>
          </div>
        </div>
      )}

      {/* Database Collections Inventory */}
      {health && (
        <div className="bg-white border border-neutral-200 rounded-xl p-6 space-y-4">
          <div className="border-b border-neutral-100 pb-3">
            <h3 className="text-sm font-bold text-neutral-900 uppercase flex items-center gap-2">
              <Layers className="w-4 h-4" />
              <span>MongoDB Collections Document Inventory</span>
            </h3>
            <p className="text-xs text-neutral-500">Live counts of all records across the Khushhaal data architecture</p>
          </div>

          <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-5 gap-3 text-xs">
            {Object.entries(health.collectionCounts || {}).map(([key, count]) => (
              <div key={key} className="p-3 bg-neutral-50 rounded-lg border border-neutral-200">
                <span className="text-neutral-500 uppercase font-semibold text-[10px] block">
                  {key}
                </span>
                <span className="text-base font-bold font-mono text-neutral-900 mt-0.5 block">
                  {count.toLocaleString()}
                </span>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* Superadmin Profile & Environment Details */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div className="bg-white border border-neutral-200 rounded-xl p-6 space-y-4">
          <div className="border-b border-neutral-100 pb-3">
            <h3 className="text-sm font-bold text-neutral-900 uppercase flex items-center gap-2">
              <Key className="w-4 h-4" />
              <span>Superadmin Authentication Dossier</span>
            </h3>
            <p className="text-xs text-neutral-500">Current authenticated administrative identity</p>
          </div>

          <div className="space-y-2.5 text-xs">
            <div className="flex justify-between py-1 border-b border-neutral-100">
              <span className="text-neutral-500">Admin Name</span>
              <span className="font-semibold text-neutral-900">{admin?.name || 'Khushhaal Super Admin'}</span>
            </div>
            <div className="flex justify-between py-1 border-b border-neutral-100">
              <span className="text-neutral-500">Authorized Phone</span>
              <span className="font-mono text-neutral-900">{admin?.phone || '+923000000000'}</span>
            </div>
            <div className="flex justify-between py-1 border-b border-neutral-100">
              <span className="text-neutral-500">Administrative CNIC</span>
              <span className="font-mono text-neutral-900">{admin?.cnic || '00000-0000000-0'}</span>
            </div>
            <div className="flex justify-between py-1 border-b border-neutral-100">
              <span className="text-neutral-500">Role Privilege Level</span>
              <Badge variant="default">FULL_ACCESS_SUPERADMIN</Badge>
            </div>
          </div>
        </div>

        <div className="bg-white border border-neutral-200 rounded-xl p-6 space-y-4">
          <div className="border-b border-neutral-100 pb-3">
            <h3 className="text-sm font-bold text-neutral-900 uppercase flex items-center gap-2">
              <ShieldCheck className="w-4 h-4" />
              <span>Data Source & Failover Settings</span>
            </h3>
            <p className="text-xs text-neutral-500">Switch between local simulated dataset and production MongoDB</p>
          </div>

          <div className="space-y-3 text-xs">
            <p className="text-neutral-600 leading-relaxed">
              When Demo Mode is active, all CRUD modifications (worker onboarding, transaction entries, utility bill payments) persist in-memory during your session without requiring an active local MongoDB daemon.
            </p>

            <button
              onClick={() => toggleDemoMode(!demoMode)}
              className="w-full py-2.5 px-4 bg-neutral-900 hover:bg-neutral-800 text-white rounded-lg text-xs font-semibold transition-colors flex items-center justify-center gap-2"
            >
              <span>{demoMode ? 'Switch to Live MongoDB API' : 'Switch to Demo / Offline Mode'}</span>
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};
