import React, { useState, useEffect } from 'react';
import {
  Coins,
  Plus,
  Trash2,
  CheckCircle2,
  RefreshCw,
  Users,
  Calendar
} from 'lucide-react';
import { Kameti } from '../types';
import { api } from '../services/api';
import { Badge } from '../components/Badge';
import { Modal } from '../components/Modal';

export const Kametis: React.FC = () => {
  const [kametis, setKametis] = useState<Kameti[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [isAddOpen, setIsAddOpen] = useState<boolean>(false);
  const [actionLoading, setActionLoading] = useState<boolean>(false);

  const [formData, setFormData] = useState({
    userId: 'usr-101',
    name: 'Weaving Shift Committee',
    urduName: 'ویونگ شفٹ کمیٹی',
    monthlyAmount: '5000',
    totalMembers: '10',
    myTurnMonth: '5',
    currentMonth: '3',
    organizer: 'Supervisor Naeem'
  });

  const loadKametis = async () => {
    setLoading(true);
    try {
      const res = await api.getKametis();
      setKametis(res.items);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadKametis();
  }, []);

  const handleTogglePaid = async (k: Kameti) => {
    const nextState = !k.isPaidThisMonth;
    await api.updateKameti(k.id, { isPaidThisMonth: nextState });
    loadKametis();
  };

  const handleDelete = async (id: string) => {
    if (confirm('Delete this kameti committee record?')) {
      await api.deleteKameti(id);
      loadKametis();
    }
  };

  const handleCreate = async (e: React.FormEvent) => {
    e.preventDefault();
    setActionLoading(true);
    try {
      await api.createKameti(formData);
      setIsAddOpen(false);
      setFormData({
        userId: 'usr-101',
        name: '',
        urduName: '',
        monthlyAmount: '5000',
        totalMembers: '10',
        myTurnMonth: '1',
        currentMonth: '1',
        organizer: ''
      });
      loadKametis();
    } catch (err: any) {
      alert(err.message || 'Error creating committee');
    } finally {
      setActionLoading(false);
    }
  };

  const totalPool = kametis.reduce((sum, k) => sum + (k.payoutAmount || k.monthlyAmount * k.totalMembers), 0);
  const totalMonthly = kametis.reduce((sum, k) => sum + k.monthlyAmount, 0);

  return (
    <div className="space-y-6">
      {/* Top Banner Stats */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
        <div className="bg-white border border-neutral-200 rounded-xl p-5">
          <div className="flex items-center gap-1.5 text-xs text-neutral-500">
            <Coins className="w-4 h-4 text-neutral-900" />
            <span>Total Committee Payout Pool</span>
          </div>
          <h3 className="text-2xl font-bold font-mono text-neutral-900 mt-1">
            Rs. {totalPool.toLocaleString()}
          </h3>
          <p className="text-xs text-neutral-500 mt-0.5">{kametis.length} Active Committees</p>
        </div>

        <div className="bg-white border border-neutral-200 rounded-xl p-5">
          <div className="flex items-center gap-1.5 text-xs text-neutral-500">
            <Coins className="w-4 h-4 text-emerald-600" />
            <span>Monthly Contribution Pool</span>
          </div>
          <h3 className="text-2xl font-bold font-mono text-neutral-900 mt-1">
            Rs. {totalMonthly.toLocaleString()}
          </h3>
          <p className="text-xs text-neutral-500 mt-0.5">Recurring Monthly Collective Savings</p>
        </div>

        <div className="bg-white border border-neutral-200 rounded-xl p-5">
          <div className="flex items-center gap-1.5 text-xs text-neutral-500">
            <Users className="w-4 h-4 text-neutral-900" />
            <span>Workers Participating</span>
          </div>
          <h3 className="text-2xl font-bold font-mono text-neutral-900 mt-1">
            {kametis.length} Members
          </h3>
          <p className="text-xs text-neutral-500 mt-0.5">Disciplined Informal Banking</p>
        </div>
      </div>

      {/* Action Header */}
      <div className="bg-white border border-neutral-200 rounded-xl p-4 flex items-center justify-between">
        <div>
          <h3 className="text-sm font-bold text-neutral-900 uppercase">Workforce Committees Directory</h3>
          <p className="text-xs text-neutral-500">Supervise worker BC turns and monthly installment deposits</p>
        </div>
        <button
          onClick={() => setIsAddOpen(true)}
          className="flex items-center gap-1.5 px-3.5 py-2 bg-neutral-900 hover:bg-neutral-800 text-white rounded-lg text-xs font-medium shadow-xs transition-colors"
        >
          <Plus className="w-3.5 h-3.5" />
          <span>Add Kameti Committee</span>
        </button>
      </div>

      {/* Kametis Table */}
      <div className="bg-white border border-neutral-200 rounded-xl overflow-hidden shadow-2xs">
        <div className="overflow-x-auto">
          <table className="w-full text-left text-xs border-collapse">
            <thead>
              <tr className="bg-neutral-50/80 border-b border-neutral-200 text-[11px] font-semibold text-neutral-500 uppercase tracking-wider">
                <th className="py-3 px-4">Committee Name</th>
                <th className="py-3 px-4">Worker Profile</th>
                <th className="py-3 px-4">Monthly Share</th>
                <th className="py-3 px-4">Members</th>
                <th className="py-3 px-4">Worker Turn</th>
                <th className="py-3 px-4">Payout Pool</th>
                <th className="py-3 px-4">Organizer</th>
                <th className="py-3 px-4">This Month Status</th>
                <th className="py-3 px-4 text-right">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-neutral-100">
              {loading ? (
                <tr>
                  <td colSpan={9} className="py-12 text-center text-neutral-400">
                    <RefreshCw className="w-5 h-5 animate-spin mx-auto mb-2" />
                    Loading committee records...
                  </td>
                </tr>
              ) : kametis.length === 0 ? (
                <tr>
                  <td colSpan={9} className="py-12 text-center text-neutral-500">
                    No active kameti committees found.
                  </td>
                </tr>
              ) : (
                kametis.map((k) => (
                  <tr key={k.id} className="hover:bg-neutral-50/60 transition-colors">
                    <td className="py-3.5 px-4">
                      <p className="font-semibold text-neutral-900">{k.name}</p>
                      <p className="font-urdu text-[11px] text-neutral-500">{k.urduName}</p>
                    </td>

                    <td className="py-3.5 px-4">
                      <p className="font-medium text-neutral-900">{k.workerName}</p>
                      <p className="text-[10px] text-neutral-400">{k.factory || 'Worker'}</p>
                    </td>

                    <td className="py-3.5 px-4 font-mono font-bold text-neutral-900">
                      Rs. {k.monthlyAmount.toLocaleString()}
                    </td>

                    <td className="py-3.5 px-4 font-mono text-neutral-600">
                      {k.totalMembers} Members
                    </td>

                    <td className="py-3.5 px-4">
                      <div className="space-y-1">
                        <span className="font-mono font-bold text-neutral-900">
                          Month {k.myTurnMonth} of {k.totalMembers}
                        </span>
                        <div className="w-20 bg-neutral-100 h-1.5 rounded-full overflow-hidden">
                          <div 
                            className="bg-neutral-900 h-full rounded-full" 
                            style={{ width: `${(k.currentMonth / k.totalMembers) * 100}%` }} 
                          />
                        </div>
                        <span className="text-[10px] text-neutral-400 block font-mono">Current: Month {k.currentMonth}</span>
                      </div>
                    </td>

                    <td className="py-3.5 px-4 font-mono font-bold text-emerald-700">
                      Rs. {(k.payoutAmount || k.monthlyAmount * k.totalMembers).toLocaleString()}
                    </td>

                    <td className="py-3.5 px-4 text-neutral-700">
                      {k.organizer || 'Self Managed'}
                    </td>

                    <td className="py-3.5 px-4">
                      <button
                        onClick={() => handleTogglePaid(k)}
                        className="cursor-pointer"
                        title="Click to toggle paid status"
                      >
                        <Badge variant={k.isPaidThisMonth ? 'success' : 'danger'}>
                          {k.isPaidThisMonth ? 'Paid' : 'Unpaid'}
                        </Badge>
                      </button>
                    </td>

                    <td className="py-3.5 px-4 text-right">
                      <div className="flex items-center justify-end gap-1.5">
                        <button
                          onClick={() => handleTogglePaid(k)}
                          className="px-2 py-1 border border-neutral-200 rounded text-[11px] font-medium hover:bg-neutral-100"
                        >
                          {k.isPaidThisMonth ? 'Mark Unpaid' : 'Mark Paid'}
                        </button>
                        <button
                          onClick={() => handleDelete(k.id)}
                          className="p-1 text-neutral-400 hover:text-rose-600 rounded hover:bg-rose-50"
                        >
                          <Trash2 className="w-4 h-4" />
                        </button>
                      </div>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Add Kameti Modal */}
      <Modal
        isOpen={isAddOpen}
        onClose={() => setIsAddOpen(false)}
        title="Add Kameti Committee"
        urduTitle="نئی کمیٹی کا اندراج کریں"
        maxWidth="md"
      >
        <form onSubmit={handleCreate} className="space-y-4 text-xs">
          <div>
            <label className="block font-medium text-neutral-700 mb-1">Worker User ID</label>
            <input
              type="text"
              required
              value={formData.userId}
              onChange={(e) => setFormData({ ...formData, userId: e.target.value })}
              className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900 font-mono"
            />
          </div>

          <div className="grid grid-cols-2 gap-3">
            <div>
              <label className="block font-medium text-neutral-700 mb-1">Committee Name</label>
              <input
                type="text"
                required
                placeholder="Weaving Shift Committee"
                value={formData.name}
                onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900"
              />
            </div>
            <div>
              <label className="block font-medium text-neutral-700 mb-1">Urdu Name</label>
              <input
                type="text"
                placeholder="ویونگ شفٹ کمیٹی"
                value={formData.urduName}
                onChange={(e) => setFormData({ ...formData, urduName: e.target.value })}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900 font-urdu"
              />
            </div>
          </div>

          <div className="grid grid-cols-2 gap-3">
            <div>
              <label className="block font-medium text-neutral-700 mb-1">Monthly Contribution (PKR)</label>
              <input
                type="number"
                required
                min={1}
                value={formData.monthlyAmount}
                onChange={(e) => setFormData({ ...formData, monthlyAmount: e.target.value })}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900 font-mono"
              />
            </div>
            <div>
              <label className="block font-medium text-neutral-700 mb-1">Total Members</label>
              <input
                type="number"
                required
                min={2}
                value={formData.totalMembers}
                onChange={(e) => setFormData({ ...formData, totalMembers: e.target.value })}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900 font-mono"
              />
            </div>
          </div>

          <div className="grid grid-cols-3 gap-3">
            <div>
              <label className="block font-medium text-neutral-700 mb-1">Worker Turn Month</label>
              <input
                type="number"
                required
                min={1}
                value={formData.myTurnMonth}
                onChange={(e) => setFormData({ ...formData, myTurnMonth: e.target.value })}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900 font-mono"
              />
            </div>
            <div>
              <label className="block font-medium text-neutral-700 mb-1">Current Month</label>
              <input
                type="number"
                required
                min={1}
                value={formData.currentMonth}
                onChange={(e) => setFormData({ ...formData, currentMonth: e.target.value })}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900 font-mono"
              />
            </div>
            <div>
              <label className="block font-medium text-neutral-700 mb-1">Organizer</label>
              <input
                type="text"
                placeholder="Supervisor Naeem"
                value={formData.organizer}
                onChange={(e) => setFormData({ ...formData, organizer: e.target.value })}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900"
              />
            </div>
          </div>

          <div className="pt-3 border-t border-neutral-100 flex justify-end gap-2">
            <button
              type="button"
              onClick={() => setIsAddOpen(false)}
              className="px-4 py-2 border border-neutral-200 rounded-lg text-xs font-medium text-neutral-700 hover:bg-neutral-50"
            >
              Cancel
            </button>
            <button
              type="submit"
              disabled={actionLoading}
              className="px-4 py-2 bg-neutral-900 text-white rounded-lg text-xs font-medium hover:bg-neutral-800 transition-colors"
            >
              {actionLoading ? 'Saving...' : 'Save Committee'}
            </button>
          </div>
        </form>
      </Modal>
    </div>
  );
};
