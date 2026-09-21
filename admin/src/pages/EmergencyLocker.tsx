import React, { useState, useEffect } from 'react';
import {
  ShieldAlert,
  Search,
  PlusCircle,
  ArrowDownCircle,
  ArrowUpCircle,
  RefreshCw,
  Wallet
} from 'lucide-react';
import { EmergencyLocker } from '../types';
import { api } from '../services/api';
import { Badge } from '../components/Badge';
import { Modal } from '../components/Modal';

export const EmergencyLockerPage: React.FC = () => {
  const [lockers, setLockers] = useState<EmergencyLocker[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [selectedLocker, setSelectedLocker] = useState<EmergencyLocker | null>(null);
  const [isAdjustOpen, setIsAdjustOpen] = useState<boolean>(false);
  const [adjustAmount, setAdjustAmount] = useState<string>('');
  const [adjustType, setAdjustType] = useState<'deposit' | 'grant' | 'set'>('grant');
  const [actionLoading, setActionLoading] = useState<boolean>(false);

  const loadLockers = async () => {
    setLoading(true);
    try {
      const data = await api.getLockers();
      setLockers(data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadLockers();
  }, []);

  const handleAdjust = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!selectedLocker || !adjustAmount) return;
    setActionLoading(true);
    try {
      const amt = Number(adjustAmount);
      if (adjustType === 'set') {
        await api.updateLocker(selectedLocker.userId!, { balance: amt });
      } else {
        await api.updateLocker(selectedLocker.userId!, { adjustmentAmount: amt });
      }
      setIsAdjustOpen(false);
      setAdjustAmount('');
      loadLockers();
    } catch (err: any) {
      alert(err.message || 'Error updating emergency locker');
    } finally {
      setActionLoading(false);
    }
  };

  const totalReserve = lockers.reduce((sum, l) => sum + l.balance, 0);
  const avgReserve = lockers.length > 0 ? Math.round(totalReserve / lockers.length) : 0;
  const criticalCount = lockers.filter(l => l.balance < 5000).length;

  return (
    <div className="space-y-6">
      {/* Top Banner */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
        <div className="bg-white border border-neutral-200 rounded-xl p-5">
          <div className="flex items-center gap-1.5 text-xs text-neutral-500">
            <ShieldAlert className="w-4 h-4 text-emerald-600" />
            <span>Total Emergency Buffer Held</span>
          </div>
          <h3 className="text-2xl font-bold font-mono text-neutral-900 mt-1">
            Rs. {totalReserve.toLocaleString()}
          </h3>
          <p className="text-xs text-neutral-500 mt-0.5">Liquid Protection for Medical / Crises</p>
        </div>

        <div className="bg-white border border-neutral-200 rounded-xl p-5">
          <div className="flex items-center gap-1.5 text-xs text-neutral-500">
            <Wallet className="w-4 h-4 text-neutral-900" />
            <span>Average Worker Buffer</span>
          </div>
          <h3 className="text-2xl font-bold font-mono text-neutral-900 mt-1">
            Rs. {avgReserve.toLocaleString()}
          </h3>
          <p className="text-xs text-neutral-500 mt-0.5">Approx 12-18 Days of Living Runway</p>
        </div>

        <div className="bg-white border border-neutral-200 rounded-xl p-5">
          <div className="flex items-center gap-1.5 text-xs text-neutral-500">
            <ShieldAlert className="w-4 h-4 text-rose-600" />
            <span>Vulnerable Workers (&lt; Rs. 5,000)</span>
          </div>
          <h3 className="text-2xl font-bold font-mono text-rose-700 mt-1">
            {criticalCount} Workers
          </h3>
          <p className="text-xs text-neutral-500 mt-0.5">Require Emergency Subsidy or Incentive</p>
        </div>
      </div>

      {/* Lockers Table */}
      <div className="bg-white border border-neutral-200 rounded-xl overflow-hidden shadow-2xs">
        <div className="p-4 border-b border-neutral-200 flex justify-between items-center">
          <div>
            <h3 className="text-sm font-bold text-neutral-900 uppercase">Emergency Locker Accounts</h3>
            <p className="text-xs text-neutral-500">Individual worker emergency savings reserves</p>
          </div>
          <button
            onClick={loadLockers}
            className="flex items-center gap-1 text-xs text-neutral-600 hover:text-neutral-900 border border-neutral-200 px-2.5 py-1.5 rounded-lg"
          >
            <RefreshCw className="w-3.5 h-3.5" />
            <span>Refresh</span>
          </button>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-left text-xs border-collapse">
            <thead>
              <tr className="bg-neutral-50/80 border-b border-neutral-200 text-[11px] font-semibold text-neutral-500 uppercase tracking-wider">
                <th className="py-3 px-4">Worker Name</th>
                <th className="py-3 px-4">Factory / Organization</th>
                <th className="py-3 px-4">Locker Balance (PKR)</th>
                <th className="py-3 px-4">Runway Status</th>
                <th className="py-3 px-4">Last Updated</th>
                <th className="py-3 px-4 text-right">Administrative Action</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-neutral-100">
              {loading ? (
                <tr>
                  <td colSpan={6} className="py-12 text-center text-neutral-400">
                    <RefreshCw className="w-5 h-5 animate-spin mx-auto mb-2" />
                    Loading emergency locker balances...
                  </td>
                </tr>
              ) : lockers.length === 0 ? (
                <tr>
                  <td colSpan={6} className="py-12 text-center text-neutral-500">
                    No emergency lockers found.
                  </td>
                </tr>
              ) : (
                lockers.map((locker) => (
                  <tr key={locker.id} className="hover:bg-neutral-50/60 transition-colors">
                    <td className="py-3.5 px-4">
                      <p className="font-semibold text-neutral-900">{locker.workerName}</p>
                      <p className="text-[10px] text-neutral-400 font-mono">{locker.workerPhone || 'N/A'}</p>
                    </td>

                    <td className="py-3.5 px-4 text-neutral-700">
                      {locker.factory || 'Independent Factory'}
                    </td>

                    <td className="py-3.5 px-4 font-mono font-bold text-sm text-neutral-900">
                      Rs. {locker.balance.toLocaleString()}
                    </td>

                    <td className="py-3.5 px-4">
                      <Badge
                        variant={
                          locker.balance >= 15000
                            ? 'success'
                            : locker.balance >= 5000
                            ? 'warning'
                            : 'danger'
                        }
                      >
                        {locker.balance >= 15000
                          ? 'Healthy Buffer (>20 Days)'
                          : locker.balance >= 5000
                          ? 'Moderate Buffer (10-15 Days)'
                          : 'Critical Risk (<7 Days)'}
                      </Badge>
                    </td>

                    <td className="py-3.5 px-4 font-mono text-neutral-500 text-[11px]">
                      {new Date(locker.updatedAt).toLocaleDateString()}
                    </td>

                    <td className="py-3.5 px-4 text-right">
                      <button
                        onClick={() => {
                          setSelectedLocker(locker);
                          setAdjustAmount('');
                          setAdjustType('grant');
                          setIsAdjustOpen(true);
                        }}
                        className="px-3 py-1 bg-neutral-900 hover:bg-neutral-800 text-white rounded text-[11px] font-medium transition-colors"
                      >
                        Adjust / Grant
                      </button>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Adjust Locker Modal */}
      {selectedLocker && (
        <Modal
          isOpen={isAdjustOpen}
          onClose={() => setIsAdjustOpen(false)}
          title={`Emergency Locker: ${selectedLocker.workerName}`}
          urduTitle="ہنگامی لاکر فنڈ کا اندراج"
          maxWidth="sm"
        >
          <form onSubmit={handleAdjust} className="space-y-4 text-xs">
            <div className="p-3 bg-neutral-50 border border-neutral-200 rounded-lg">
              <span className="text-neutral-500 text-[11px] block">Current Reserve</span>
              <span className="text-xl font-bold font-mono text-neutral-900">
                Rs. {selectedLocker.balance.toLocaleString()}
              </span>
            </div>

            <div>
              <label className="block font-medium text-neutral-700 mb-1">Action Type</label>
              <select
                value={adjustType}
                onChange={(e) => setAdjustType(e.target.value as any)}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900"
              >
                <option value="grant">Grant / Subsidy (+)</option>
                <option value="deposit">Manual Deposit (+)</option>
                <option value="set">Set Exact Balance</option>
              </select>
            </div>

            <div>
              <label className="block font-medium text-neutral-700 mb-1">
                {adjustType === 'set' ? 'New Exact Balance (PKR)' : 'Adjustment Amount (PKR)'}
              </label>
              <input
                type="number"
                required
                min={0}
                placeholder="e.g. 5000"
                value={adjustAmount}
                onChange={(e) => setAdjustAmount(e.target.value)}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900 font-mono"
              />
            </div>

            <div className="flex justify-end gap-2 pt-2 border-t border-neutral-100">
              <button
                type="button"
                onClick={() => setIsAdjustOpen(false)}
                className="px-3 py-1.5 border border-neutral-200 rounded-lg text-xs font-medium text-neutral-700 hover:bg-neutral-50"
              >
                Cancel
              </button>
              <button
                type="submit"
                disabled={actionLoading}
                className="px-3.5 py-1.5 bg-neutral-900 text-white rounded-lg text-xs font-medium hover:bg-neutral-800 transition-colors"
              >
                {actionLoading ? 'Saving...' : 'Apply Locker Adjustment'}
              </button>
            </div>
          </form>
        </Modal>
      )}
    </div>
  );
};
