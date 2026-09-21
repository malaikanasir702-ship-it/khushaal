import React, { useState, useEffect } from 'react';
import {
  Receipt,
  Search,
  Plus,
  CheckCircle2,
  AlertTriangle,
  Zap,
  Flame,
  Droplets,
  Trash2,
  Edit2,
  RefreshCw
} from 'lucide-react';
import { Bill } from '../types';
import { api } from '../services/api';
import { Badge } from '../components/Badge';
import { Modal } from '../components/Modal';

export const Bills: React.FC = () => {
  const [bills, setBills] = useState<Bill[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [billTypeFilter, setBillTypeFilter] = useState<string>('ALL');
  const [statusFilter, setStatusFilter] = useState<string>('ALL');

  const [isAddOpen, setIsAddOpen] = useState<boolean>(false);
  const [editingBill, setEditingBill] = useState<Bill | null>(null);
  const [actionLoading, setActionLoading] = useState<boolean>(false);

  const [formData, setFormData] = useState({
    userId: 'usr-101',
    companyName: 'LESCO Electricity',
    companyUrdu: 'لیسکو بجلی بل',
    consumerNumber: '',
    billType: 'Electricity',
    month: 'March 2025',
    dueDate: '',
    amount: '',
    unitsConsumed: '',
    alertTip: ''
  });

  const loadBills = async () => {
    setLoading(true);
    try {
      const res = await api.getBills({
        isPaid: statusFilter,
        billType: billTypeFilter
      });
      setBills(res.items);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadBills();
  }, [billTypeFilter, statusFilter]);

  const handleTogglePaid = async (bill: Bill) => {
    const nextState = !bill.isPaid;
    await api.updateBill(bill.id, { isPaid: nextState });
    loadBills();
  };

  const handleDelete = async (id: string) => {
    if (confirm('Delete this utility invoice?')) {
      await api.deleteBill(id);
      loadBills();
    }
  };

  const handleCreate = async (e: React.FormEvent) => {
    e.preventDefault();
    setActionLoading(true);
    try {
      await api.createBill(formData);
      setIsAddOpen(false);
      setFormData({
        userId: 'usr-101',
        companyName: 'LESCO Electricity',
        companyUrdu: 'لیسکو بجلی بل',
        consumerNumber: '',
        billType: 'Electricity',
        month: 'March 2025',
        dueDate: '',
        amount: '',
        unitsConsumed: '',
        alertTip: ''
      });
      loadBills();
    } catch (err: any) {
      alert(err.message || 'Error creating bill');
    } finally {
      setActionLoading(false);
    }
  };

  const unpaidCount = bills.filter(b => !b.isPaid).length;
  const unpaidTotal = bills.filter(b => !b.isPaid).reduce((sum, b) => sum + b.amount, 0);
  const paidCount = bills.filter(b => b.isPaid).length;
  const paidTotal = bills.filter(b => b.isPaid).reduce((sum, b) => sum + b.amount, 0);

  const getBillIcon = (type: string) => {
    switch (type.toLowerCase()) {
      case 'electricity': return <Zap className="w-4 h-4 text-amber-600" />;
      case 'gas': return <Flame className="w-4 h-4 text-rose-600" />;
      case 'water': return <Droplets className="w-4 h-4 text-blue-600" />;
      default: return <Receipt className="w-4 h-4 text-neutral-600" />;
    }
  };

  return (
    <div className="space-y-6">
      {/* Top Metric Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
        <div className="bg-white border border-neutral-200 rounded-xl p-5 flex items-center justify-between">
          <div>
            <div className="flex items-center gap-1.5 text-xs text-neutral-500">
              <AlertTriangle className="w-4 h-4 text-rose-600" />
              <span>Pending Workforce Bills</span>
            </div>
            <h3 className="text-2xl font-bold font-mono text-neutral-900 mt-1">
              Rs. {unpaidTotal.toLocaleString()}
            </h3>
            <p className="text-xs text-neutral-500 mt-0.5">{unpaidCount} Bills Overdue or Upcoming</p>
          </div>
          <div className="p-3 bg-rose-50 border border-rose-100 rounded-lg text-rose-800 font-bold text-lg font-mono">
            {unpaidCount}
          </div>
        </div>

        <div className="bg-white border border-neutral-200 rounded-xl p-5 flex items-center justify-between">
          <div>
            <div className="flex items-center gap-1.5 text-xs text-neutral-500">
              <CheckCircle2 className="w-4 h-4 text-emerald-600" />
              <span>Settled Utility Bills</span>
            </div>
            <h3 className="text-2xl font-bold font-mono text-neutral-900 mt-1">
              Rs. {paidTotal.toLocaleString()}
            </h3>
            <p className="text-xs text-neutral-500 mt-0.5">{paidCount} Paid Invoices</p>
          </div>
          <div className="p-3 bg-emerald-50 border border-emerald-100 rounded-lg text-emerald-800 font-bold text-lg font-mono">
            {paidCount}
          </div>
        </div>
      </div>

      {/* Filter and Action Bar */}
      <div className="bg-white border border-neutral-200 rounded-xl p-4 flex flex-col md:flex-row items-center justify-between gap-4">
        <div className="flex flex-wrap items-center gap-3 w-full md:w-auto">
          {/* Bill Type Filter */}
          <select
            value={billTypeFilter}
            onChange={(e) => setBillTypeFilter(e.target.value)}
            className="bg-neutral-50 border border-neutral-200 rounded-lg px-2.5 py-1.5 text-xs text-neutral-900 outline-none focus:border-neutral-900"
          >
            <option value="ALL">All Utilities</option>
            <option value="Electricity">Electricity (LESCO / KE)</option>
            <option value="Gas">Sui Gas (SNGPL)</option>
            <option value="Water">Water (WASA)</option>
          </select>

          {/* Status Filter */}
          <select
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value)}
            className="bg-neutral-50 border border-neutral-200 rounded-lg px-2.5 py-1.5 text-xs text-neutral-900 outline-none focus:border-neutral-900"
          >
            <option value="ALL">All Payment Statuses</option>
            <option value="false">Unpaid Invoices Only</option>
            <option value="true">Paid Invoices Only</option>
          </select>
        </div>

        <button
          onClick={() => setIsAddOpen(true)}
          className="flex items-center gap-1.5 px-3.5 py-2 bg-neutral-900 hover:bg-neutral-800 text-white rounded-lg text-xs font-medium shadow-xs transition-colors"
        >
          <Plus className="w-3.5 h-3.5" />
          <span>Add Utility Invoice</span>
        </button>
      </div>

      {/* Bills Grid/Table */}
      <div className="bg-white border border-neutral-200 rounded-xl overflow-hidden shadow-2xs">
        <div className="overflow-x-auto">
          <table className="w-full text-left text-xs border-collapse">
            <thead>
              <tr className="bg-neutral-50/80 border-b border-neutral-200 text-[11px] font-semibold text-neutral-500 uppercase tracking-wider">
                <th className="py-3 px-4">Utility & Provider</th>
                <th className="py-3 px-4">Worker</th>
                <th className="py-3 px-4">Consumer #</th>
                <th className="py-3 px-4">Billing Month & Due</th>
                <th className="py-3 px-4">Units</th>
                <th className="py-3 px-4">Amount (PKR)</th>
                <th className="py-3 px-4">Status</th>
                <th className="py-3 px-4 text-right">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-neutral-100">
              {loading ? (
                <tr>
                  <td colSpan={8} className="py-12 text-center text-neutral-400">
                    <RefreshCw className="w-5 h-5 animate-spin mx-auto mb-2" />
                    Loading utility invoices...
                  </td>
                </tr>
              ) : bills.length === 0 ? (
                <tr>
                  <td colSpan={8} className="py-12 text-center text-neutral-500">
                    No utility bills match your filter.
                  </td>
                </tr>
              ) : (
                bills.map((bill) => (
                  <tr key={bill.id} className="hover:bg-neutral-50/60 transition-colors">
                    <td className="py-3.5 px-4">
                      <div className="flex items-center gap-2.5">
                        <div className="p-1.5 bg-neutral-100 rounded-md">
                          {getBillIcon(bill.billType)}
                        </div>
                        <div>
                          <p className="font-semibold text-neutral-900">{bill.companyName}</p>
                          <p className="font-urdu text-[11px] text-neutral-500">{bill.companyUrdu}</p>
                        </div>
                      </div>
                    </td>

                    <td className="py-3.5 px-4">
                      <p className="font-medium text-neutral-900">{bill.workerName}</p>
                      <p className="text-[11px] text-neutral-400 truncate max-w-[140px]">{bill.factory || 'Worker'}</p>
                    </td>

                    <td className="py-3.5 px-4 font-mono text-neutral-700 text-[11px]">
                      {bill.consumerNumber || 'N/A'}
                    </td>

                    <td className="py-3.5 px-4">
                      <p className="font-medium text-neutral-900">{bill.month}</p>
                      <p className="text-[11px] font-mono text-neutral-500">Due: {bill.dueDate || 'N/A'}</p>
                    </td>

                    <td className="py-3.5 px-4 font-mono text-neutral-600">
                      {bill.unitsConsumed > 0 ? `${bill.unitsConsumed} units` : '-'}
                    </td>

                    <td className="py-3.5 px-4 font-mono font-bold text-neutral-900">
                      Rs. {bill.amount.toLocaleString()}
                    </td>

                    <td className="py-3.5 px-4">
                      <button
                        onClick={() => handleTogglePaid(bill)}
                        className="cursor-pointer"
                        title="Click to toggle paid status"
                      >
                        <Badge variant={bill.isPaid ? 'success' : 'danger'}>
                          {bill.isPaid ? 'Paid' : 'Unpaid'}
                        </Badge>
                      </button>
                    </td>

                    <td className="py-3.5 px-4 text-right">
                      <div className="flex items-center justify-end gap-1.5">
                        <button
                          onClick={() => handleTogglePaid(bill)}
                          className="px-2 py-1 border border-neutral-200 rounded text-[11px] font-medium hover:bg-neutral-100"
                        >
                          {bill.isPaid ? 'Mark Unpaid' : 'Mark Paid'}
                        </button>
                        <button
                          onClick={() => handleDelete(bill.id)}
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

      {/* Add Bill Modal */}
      <Modal
        isOpen={isAddOpen}
        onClose={() => setIsAddOpen(false)}
        title="Add Utility Bill Invoice"
        urduTitle="نیا یوٹیلیٹی بل شامل کریں"
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
              <label className="block font-medium text-neutral-700 mb-1">Company / Utility</label>
              <input
                type="text"
                required
                placeholder="LESCO Electricity"
                value={formData.companyName}
                onChange={(e) => setFormData({ ...formData, companyName: e.target.value })}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900"
              />
            </div>
            <div>
              <label className="block font-medium text-neutral-700 mb-1">Urdu Title</label>
              <input
                type="text"
                placeholder="لیسکو بجلی بل"
                value={formData.companyUrdu}
                onChange={(e) => setFormData({ ...formData, companyUrdu: e.target.value })}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900 font-urdu"
              />
            </div>
          </div>

          <div className="grid grid-cols-2 gap-3">
            <div>
              <label className="block font-medium text-neutral-700 mb-1">Consumer Number</label>
              <input
                type="text"
                placeholder="08112345678901U"
                value={formData.consumerNumber}
                onChange={(e) => setFormData({ ...formData, consumerNumber: e.target.value })}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900 font-mono"
              />
            </div>
            <div>
              <label className="block font-medium text-neutral-700 mb-1">Bill Type</label>
              <select
                value={formData.billType}
                onChange={(e) => setFormData({ ...formData, billType: e.target.value })}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900"
              >
                <option value="Electricity">Electricity</option>
                <option value="Gas">Gas</option>
                <option value="Water">Water</option>
              </select>
            </div>
          </div>

          <div className="grid grid-cols-3 gap-3">
            <div>
              <label className="block font-medium text-neutral-700 mb-1">Amount (PKR) *</label>
              <input
                type="number"
                required
                min={1}
                placeholder="4500"
                value={formData.amount}
                onChange={(e) => setFormData({ ...formData, amount: e.target.value })}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900 font-mono"
              />
            </div>
            <div>
              <label className="block font-medium text-neutral-700 mb-1">Units Consumed</label>
              <input
                type="number"
                placeholder="150"
                value={formData.unitsConsumed}
                onChange={(e) => setFormData({ ...formData, unitsConsumed: e.target.value })}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900 font-mono"
              />
            </div>
            <div>
              <label className="block font-medium text-neutral-700 mb-1">Due Date</label>
              <input
                type="date"
                value={formData.dueDate}
                onChange={(e) => setFormData({ ...formData, dueDate: e.target.value })}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900"
              />
            </div>
          </div>

          <div>
            <label className="block font-medium text-neutral-700 mb-1">Alert / Savings Tip</label>
            <input
              type="text"
              placeholder="پیک آورز میں استری اور موٹر بند رکھیں۔"
              value={formData.alertTip}
              onChange={(e) => setFormData({ ...formData, alertTip: e.target.value })}
              className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900 font-urdu"
            />
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
              {actionLoading ? 'Saving...' : 'Save Invoice'}
            </button>
          </div>
        </form>
      </Modal>
    </div>
  );
};
