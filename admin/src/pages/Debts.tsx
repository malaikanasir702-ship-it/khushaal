import React, { useState, useEffect } from 'react';
import {
  HandCoins,
  Search,
  Plus,
  AlertCircle,
  CheckCircle2,
  Trash2,
  Coins,
  RefreshCw,
  Scale
} from 'lucide-react';
import { Debt } from '../types';
import { api } from '../services/api';
import { Badge } from '../components/Badge';
import { Modal } from '../components/Modal';

export const Debts: React.FC = () => {
  const [debts, setDebts] = useState<Debt[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [urgencyFilter, setUrgencyFilter] = useState<string>('ALL');
  const [shariahFilter, setShariahFilter] = useState<string>('ALL');

  const [isAddOpen, setIsAddOpen] = useState<boolean>(false);
  const [isRepayOpen, setIsRepayOpen] = useState<boolean>(false);
  const [selectedDebt, setSelectedDebt] = useState<Debt | null>(null);
  const [repaymentAmount, setRepaymentAmount] = useState<string>('');
  const [actionLoading, setActionLoading] = useState<boolean>(false);

  const [formData, setFormData] = useState({
    userId: 'usr-101',
    creditorName: '',
    creditorUrdu: '',
    relationOrType: 'Neighborhood Kiryana Store',
    totalAmount: '',
    remainingAmount: '',
    monthlyCommitment: '',
    urgencyLevel: 'MEDIUM' as 'HIGH' | 'MEDIUM' | 'LOW',
    isShariahFriendly: true,
    repaymentStrategyTip: ''
  });

  const loadDebts = async () => {
    setLoading(true);
    try {
      const res = await api.getDebts({
        urgencyLevel: urgencyFilter,
        isShariahFriendly: shariahFilter
      });
      setDebts(res.items);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadDebts();
  }, [urgencyFilter, shariahFilter]);

  const handleDelete = async (id: string) => {
    if (confirm('Delete this debt record?')) {
      await api.deleteDebt(id);
      loadDebts();
    }
  };

  const handleRepayment = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!selectedDebt || !repaymentAmount) return;
    setActionLoading(true);
    try {
      const amt = Number(repaymentAmount);
      const newRemaining = Math.max(0, selectedDebt.remainingAmount - amt);
      await api.updateDebt(selectedDebt.id, { remainingAmount: newRemaining });
      setIsRepayOpen(false);
      setRepaymentAmount('');
      loadDebts();
    } catch (err: any) {
      alert(err.message || 'Error recording repayment');
    } finally {
      setActionLoading(false);
    }
  };

  const handleCreate = async (e: React.FormEvent) => {
    e.preventDefault();
    setActionLoading(true);
    try {
      await api.createDebt(formData);
      setIsAddOpen(false);
      setFormData({
        userId: 'usr-101',
        creditorName: '',
        creditorUrdu: '',
        relationOrType: 'Neighborhood Kiryana Store',
        totalAmount: '',
        remainingAmount: '',
        monthlyCommitment: '',
        urgencyLevel: 'MEDIUM',
        isShariahFriendly: true,
        repaymentStrategyTip: ''
      });
      loadDebts();
    } catch (err: any) {
      alert(err.message || 'Error creating debt record');
    } finally {
      setActionLoading(false);
    }
  };

  const totalRemaining = debts.reduce((sum, d) => sum + d.remainingAmount, 0);
  const totalMonthlyCommitment = debts.reduce((sum, d) => sum + d.monthlyCommitment, 0);
  const highUrgencyCount = debts.filter(d => d.urgencyLevel === 'HIGH').length;

  return (
    <div className="space-y-6">
      {/* Metrics Banner */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
        <div className="bg-white border border-neutral-200 rounded-xl p-5">
          <div className="flex items-center gap-1.5 text-xs text-neutral-500">
            <HandCoins className="w-4 h-4 text-rose-600" />
            <span>Total Outstanding Debt</span>
          </div>
          <h3 className="text-2xl font-bold font-mono text-neutral-900 mt-1">
            Rs. {totalRemaining.toLocaleString()}
          </h3>
          <p className="text-xs text-neutral-500 mt-0.5">{debts.length} Registered Borrowings</p>
        </div>

        <div className="bg-white border border-neutral-200 rounded-xl p-5">
          <div className="flex items-center gap-1.5 text-xs text-neutral-500">
            <Coins className="w-4 h-4 text-neutral-900" />
            <span>Monthly Repayment Burden</span>
          </div>
          <h3 className="text-2xl font-bold font-mono text-neutral-900 mt-1">
            Rs. {totalMonthlyCommitment.toLocaleString()}
          </h3>
          <p className="text-xs text-neutral-500 mt-0.5">Aggregated Monthly Installments</p>
        </div>

        <div className="bg-white border border-neutral-200 rounded-xl p-5">
          <div className="flex items-center gap-1.5 text-xs text-neutral-500">
            <AlertCircle className="w-4 h-4 text-rose-600" />
            <span>High Urgency Loans</span>
          </div>
          <h3 className="text-2xl font-bold font-mono text-rose-700 mt-1">
            {highUrgencyCount}
          </h3>
          <p className="text-xs text-neutral-500 mt-0.5">Require Immediate Snowball Strategy</p>
        </div>
      </div>

      {/* Filter and Action Bar */}
      <div className="bg-white border border-neutral-200 rounded-xl p-4 flex flex-col md:flex-row items-center justify-between gap-4">
        <div className="flex flex-wrap items-center gap-3 w-full md:w-auto">
          {/* Urgency Filter */}
          <select
            value={urgencyFilter}
            onChange={(e) => setUrgencyFilter(e.target.value)}
            className="bg-neutral-50 border border-neutral-200 rounded-lg px-2.5 py-1.5 text-xs text-neutral-900 outline-none focus:border-neutral-900"
          >
            <option value="ALL">All Urgency Levels</option>
            <option value="HIGH">High Urgency Only</option>
            <option value="MEDIUM">Medium Urgency</option>
            <option value="LOW">Low Urgency</option>
          </select>

          {/* Shariah Filter */}
          <select
            value={shariahFilter}
            onChange={(e) => setShariahFilter(e.target.value)}
            className="bg-neutral-50 border border-neutral-200 rounded-lg px-2.5 py-1.5 text-xs text-neutral-900 outline-none focus:border-neutral-900"
          >
            <option value="ALL">All Loan Structures</option>
            <option value="true">Shariah Friendly (Qarz-e-Hasna / 0% Interest)</option>
            <option value="false">Commercial / Moneylender Interest</option>
          </select>
        </div>

        <button
          onClick={() => setIsAddOpen(true)}
          className="flex items-center gap-1.5 px-3.5 py-2 bg-neutral-900 hover:bg-neutral-800 text-white rounded-lg text-xs font-medium shadow-xs transition-colors"
        >
          <Plus className="w-3.5 h-3.5" />
          <span>Record New Debt</span>
        </button>
      </div>

      {/* Debts Table */}
      <div className="bg-white border border-neutral-200 rounded-xl overflow-hidden shadow-2xs">
        <div className="overflow-x-auto">
          <table className="w-full text-left text-xs border-collapse">
            <thead>
              <tr className="bg-neutral-50/80 border-b border-neutral-200 text-[11px] font-semibold text-neutral-500 uppercase tracking-wider">
                <th className="py-3 px-4">Creditor / Type</th>
                <th className="py-3 px-4">Worker Profile</th>
                <th className="py-3 px-4">Total Borrowed</th>
                <th className="py-3 px-4">Remaining Balance</th>
                <th className="py-3 px-4">Monthly Commitment</th>
                <th className="py-3 px-4">Urgency</th>
                <th className="py-3 px-4">Shariah Status</th>
                <th className="py-3 px-4 text-right">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-neutral-100">
              {loading ? (
                <tr>
                  <td colSpan={8} className="py-12 text-center text-neutral-400">
                    <RefreshCw className="w-5 h-5 animate-spin mx-auto mb-2" />
                    Loading debt records...
                  </td>
                </tr>
              ) : debts.length === 0 ? (
                <tr>
                  <td colSpan={8} className="py-12 text-center text-neutral-500">
                    No debts match your filter.
                  </td>
                </tr>
              ) : (
                debts.map((debt) => (
                  <tr key={debt.id} className="hover:bg-neutral-50/60 transition-colors">
                    <td className="py-3.5 px-4">
                      <p className="font-semibold text-neutral-900">{debt.creditorName}</p>
                      <p className="font-urdu text-[11px] text-neutral-500">{debt.creditorUrdu}</p>
                      <p className="text-[10px] text-neutral-400">{debt.relationOrType}</p>
                    </td>

                    <td className="py-3.5 px-4">
                      <p className="font-medium text-neutral-900">{debt.workerName}</p>
                      <p className="text-[11px] text-neutral-400 truncate max-w-[140px]">{debt.factory || 'Worker'}</p>
                    </td>

                    <td className="py-3.5 px-4 font-mono font-medium text-neutral-500">
                      Rs. {debt.totalAmount.toLocaleString()}
                    </td>

                    <td className="py-3.5 px-4 font-mono font-bold text-rose-700">
                      Rs. {debt.remainingAmount.toLocaleString()}
                    </td>

                    <td className="py-3.5 px-4 font-mono text-neutral-700">
                      Rs. {debt.monthlyCommitment.toLocaleString()} / mo
                    </td>

                    <td className="py-3.5 px-4">
                      <Badge
                        variant={
                          debt.urgencyLevel === 'HIGH'
                            ? 'danger'
                            : debt.urgencyLevel === 'MEDIUM'
                            ? 'warning'
                            : 'neutral'
                        }
                      >
                        {debt.urgencyLevel}
                      </Badge>
                    </td>

                    <td className="py-3.5 px-4">
                      <Badge variant={debt.isShariahFriendly ? 'success' : 'danger'}>
                        {debt.isShariahFriendly ? 'Shariah Friendly' : 'Commercial Interest'}
                      </Badge>
                    </td>

                    <td className="py-3.5 px-4 text-right">
                      <div className="flex items-center justify-end gap-1.5">
                        <button
                          onClick={() => {
                            setSelectedDebt(debt);
                            setRepaymentAmount('');
                            setIsRepayOpen(true);
                          }}
                          className="px-2.5 py-1 bg-neutral-900 text-white rounded text-[11px] font-medium hover:bg-neutral-800"
                        >
                          Repay
                        </button>
                        <button
                          onClick={() => handleDelete(debt.id)}
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

      {/* Record Repayment Modal */}
      {selectedDebt && (
        <Modal
          isOpen={isRepayOpen}
          onClose={() => setIsRepayOpen(false)}
          title={`Record Repayment for ${selectedDebt.creditorName}`}
          urduTitle="ادھار کی ادائیگی کا اندراج"
          maxWidth="sm"
        >
          <form onSubmit={handleRepayment} className="space-y-4 text-xs">
            <div className="p-3 bg-neutral-50 border border-neutral-200 rounded-lg space-y-1">
              <p className="text-neutral-600">Worker: <strong className="text-neutral-900">{selectedDebt.workerName}</strong></p>
              <p className="text-neutral-600">Current Balance: <strong className="text-rose-700 font-mono font-bold">Rs. {selectedDebt.remainingAmount.toLocaleString()}</strong></p>
            </div>

            <div>
              <label className="block font-medium text-neutral-700 mb-1">Repayment Amount (PKR) *</label>
              <input
                type="number"
                required
                min={1}
                max={selectedDebt.remainingAmount}
                placeholder="Enter amount paid"
                value={repaymentAmount}
                onChange={(e) => setRepaymentAmount(e.target.value)}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900 font-mono"
              />
            </div>

            <div className="flex justify-end gap-2 pt-2 border-t border-neutral-100">
              <button
                type="button"
                onClick={() => setIsRepayOpen(false)}
                className="px-3 py-1.5 border border-neutral-200 rounded-lg text-xs font-medium text-neutral-700 hover:bg-neutral-50"
              >
                Cancel
              </button>
              <button
                type="submit"
                disabled={actionLoading}
                className="px-3.5 py-1.5 bg-neutral-900 text-white rounded-lg text-xs font-medium hover:bg-neutral-800 transition-colors"
              >
                {actionLoading ? 'Processing...' : 'Confirm Repayment'}
              </button>
            </div>
          </form>
        </Modal>
      )}

      {/* Add Debt Modal */}
      <Modal
        isOpen={isAddOpen}
        onClose={() => setIsAddOpen(false)}
        title="Record New Worker Debt"
        urduTitle="نیا قرض یا ادھار درج کریں"
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
              <label className="block font-medium text-neutral-700 mb-1">Creditor Name</label>
              <input
                type="text"
                required
                placeholder="Chaudhry Kiryana Store"
                value={formData.creditorName}
                onChange={(e) => setFormData({ ...formData, creditorName: e.target.value })}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900"
              />
            </div>
            <div>
              <label className="block font-medium text-neutral-700 mb-1">Creditor Urdu</label>
              <input
                type="text"
                placeholder="چوہدری کریانہ اسٹور"
                value={formData.creditorUrdu}
                onChange={(e) => setFormData({ ...formData, creditorUrdu: e.target.value })}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900 font-urdu"
              />
            </div>
          </div>

          <div className="grid grid-cols-3 gap-3">
            <div>
              <label className="block font-medium text-neutral-700 mb-1">Total Loan Amount</label>
              <input
                type="number"
                required
                min={1}
                placeholder="25000"
                value={formData.totalAmount}
                onChange={(e) => setFormData({ ...formData, totalAmount: e.target.value, remainingAmount: e.target.value })}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900 font-mono"
              />
            </div>
            <div>
              <label className="block font-medium text-neutral-700 mb-1">Remaining Balance</label>
              <input
                type="number"
                required
                min={0}
                placeholder="25000"
                value={formData.remainingAmount}
                onChange={(e) => setFormData({ ...formData, remainingAmount: e.target.value })}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900 font-mono"
              />
            </div>
            <div>
              <label className="block font-medium text-neutral-700 mb-1">Monthly Commitment</label>
              <input
                type="number"
                placeholder="3000"
                value={formData.monthlyCommitment}
                onChange={(e) => setFormData({ ...formData, monthlyCommitment: e.target.value })}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900 font-mono"
              />
            </div>
          </div>

          <div className="grid grid-cols-2 gap-3">
            <div>
              <label className="block font-medium text-neutral-700 mb-1">Urgency Level</label>
              <select
                value={formData.urgencyLevel}
                onChange={(e) => setFormData({ ...formData, urgencyLevel: e.target.value as any })}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900"
              >
                <option value="HIGH">High (Immediate Action)</option>
                <option value="MEDIUM">Medium</option>
                <option value="LOW">Low</option>
              </select>
            </div>
            <div>
              <label className="block font-medium text-neutral-700 mb-1">Shariah Compliance</label>
              <select
                value={formData.isShariahFriendly ? 'true' : 'false'}
                onChange={(e) => setFormData({ ...formData, isShariahFriendly: e.target.value === 'true' })}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900"
              >
                <option value="true">Shariah Friendly (Qarz-e-Hasna)</option>
                <option value="false">Commercial Interest Involved</option>
              </select>
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
              {actionLoading ? 'Saving...' : 'Save Debt Record'}
            </button>
          </div>
        </form>
      </Modal>
    </div>
  );
};
