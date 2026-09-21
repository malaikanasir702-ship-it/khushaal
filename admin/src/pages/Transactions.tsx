import React, { useState, useEffect } from 'react';
import {
  Search,
  Plus,
  Download,
  Filter,
  ArrowUpRight,
  ArrowDownRight,
  Trash2,
  RefreshCw,
  CreditCard
} from 'lucide-react';
import { Transaction } from '../types';
import { api } from '../services/api';
import { Badge } from '../components/Badge';
import { Modal } from '../components/Modal';

export const Transactions: React.FC = () => {
  const [transactions, setTransactions] = useState<Transaction[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [search, setSearch] = useState<string>('');
  const [categoryFilter, setCategoryFilter] = useState<string>('ALL');
  const [typeFilter, setTypeFilter] = useState<string>('ALL');

  const [isAddOpen, setIsAddOpen] = useState<boolean>(false);
  const [submitting, setSubmitting] = useState<boolean>(false);

  const [formData, setFormData] = useState({
    userId: 'usr-101',
    title: '',
    urduTitle: '',
    amount: '',
    isExpense: true,
    category: 'Groceries',
    envelopeId: 'needs',
    paymentMethod: 'Cash',
    date: new Date().toISOString().split('T')[0]
  });

  const loadTransactions = async () => {
    setLoading(true);
    try {
      const res = await api.getTransactions({
        search,
        category: categoryFilter,
        isExpense: typeFilter
      });
      setTransactions(res.items);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadTransactions();
  }, [categoryFilter, typeFilter]);

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    loadTransactions();
  };

  const handleDelete = async (id: string) => {
    if (confirm('Are you sure you want to delete this transaction record?')) {
      await api.deleteTransaction(id);
      loadTransactions();
    }
  };

  const handleCreate = async (e: React.FormEvent) => {
    e.preventDefault();
    setSubmitting(true);
    try {
      await api.createTransaction(formData);
      setIsAddOpen(false);
      setFormData({
        userId: 'usr-101',
        title: '',
        urduTitle: '',
        amount: '',
        isExpense: true,
        category: 'Groceries',
        envelopeId: 'needs',
        paymentMethod: 'Cash',
        date: new Date().toISOString().split('T')[0]
      });
      loadTransactions();
    } catch (err: any) {
      alert(err.message || 'Error saving transaction');
    } finally {
      setSubmitting(false);
    }
  };

  const exportCSV = () => {
    const headers = ['ID', 'Date', 'Worker Name', 'Factory', 'Title', 'Urdu Title', 'Type', 'Category', 'Payment Method', 'Amount (PKR)'];
    const rows = transactions.map(t => [
      t.id,
      new Date(t.date).toISOString().split('T')[0],
      `"${t.workerName}"`,
      `"${t.factory || ''}"`,
      `"${t.title}"`,
      `"${t.urduTitle}"`,
      t.isExpense ? 'Expense' : 'Income',
      t.category,
      t.paymentMethod,
      t.amount
    ]);
    const csvContent = 'data:text/csv;charset=utf-8,' + [headers.join(','), ...rows.map(r => r.join(','))].join('\n');
    const encodedUri = encodeURI(csvContent);
    const link = document.createElement('a');
    link.setAttribute('href', encodedUri);
    link.setAttribute('download', `khushhaal_ledger_${new Date().toISOString().split('T')[0]}.csv`);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  };

  // Aggregates
  const totalIncome = transactions.filter(t => !t.isExpense).reduce((sum, t) => sum + t.amount, 0);
  const totalExpense = transactions.filter(t => t.isExpense).reduce((sum, t) => sum + t.amount, 0);

  return (
    <div className="space-y-6">
      {/* Top Ledger Summary Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
        <div className="bg-white border border-neutral-200 rounded-xl p-4">
          <div className="flex items-center gap-1.5 text-xs text-neutral-500">
            <ArrowDownRight className="w-4 h-4 text-emerald-600" />
            <span>Recorded Inflow / Wages</span>
          </div>
          <h3 className="text-xl font-bold font-mono text-neutral-900 mt-1">
            Rs. {totalIncome.toLocaleString()}
          </h3>
          <p className="text-[11px] text-neutral-400 font-urdu mt-0.5">کل آمدن و فیکٹری اجرت</p>
        </div>

        <div className="bg-white border border-neutral-200 rounded-xl p-4">
          <div className="flex items-center gap-1.5 text-xs text-neutral-500">
            <ArrowUpRight className="w-4 h-4 text-rose-600" />
            <span>Recorded Outflow / Living</span>
          </div>
          <h3 className="text-xl font-bold font-mono text-neutral-900 mt-1">
            Rs. {totalExpense.toLocaleString()}
          </h3>
          <p className="text-[11px] text-neutral-400 font-urdu mt-0.5">کل اخراجات و راشن</p>
        </div>

        <div className="bg-white border border-neutral-200 rounded-xl p-4">
          <div className="flex items-center gap-1.5 text-xs text-neutral-500">
            <CreditCard className="w-4 h-4 text-neutral-900" />
            <span>Net Retained Margin</span>
          </div>
          <h3 className="text-xl font-bold font-mono text-neutral-900 mt-1">
            Rs. {(totalIncome - totalExpense).toLocaleString()}
          </h3>
          <p className="text-[11px] text-neutral-400 font-urdu mt-0.5">خالص بچت تناسب</p>
        </div>
      </div>

      {/* Action and Filter Controls */}
      <div className="bg-white border border-neutral-200 rounded-xl p-4 flex flex-col md:flex-row items-center justify-between gap-4">
        <form onSubmit={handleSearch} className="flex items-center gap-2 w-full md:w-80">
          <div className="relative flex-1">
            <Search className="w-4 h-4 text-neutral-400 absolute left-3 top-1/2 -translate-y-1/2" />
            <input
              type="text"
              placeholder="Search by title, worker..."
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              className="w-full bg-neutral-50 border border-neutral-200 rounded-lg pl-9 pr-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900"
            />
          </div>
          <button
            type="submit"
            className="px-3 py-2 bg-neutral-900 text-white rounded-lg text-xs font-medium hover:bg-neutral-800"
          >
            Filter
          </button>
        </form>

        <div className="flex flex-wrap items-center gap-3 w-full md:w-auto justify-end">
          {/* Category Filter */}
          <select
            value={categoryFilter}
            onChange={(e) => setCategoryFilter(e.target.value)}
            className="bg-neutral-50 border border-neutral-200 rounded-lg px-2.5 py-1.5 text-xs text-neutral-900 outline-none focus:border-neutral-900"
          >
            <option value="ALL">All Categories</option>
            <option value="Salary">Salary (تنخواہ)</option>
            <option value="Groceries">Groceries (راشن)</option>
            <option value="Utilities">Utilities (بلز)</option>
            <option value="Kameti">Kameti (کمیٹی)</option>
            <option value="Healthcare">Healthcare (علاج)</option>
            <option value="Side Hustle">Side Hustle (اضافی آمدن)</option>
          </select>

          {/* Type Filter */}
          <select
            value={typeFilter}
            onChange={(e) => setTypeFilter(e.target.value)}
            className="bg-neutral-50 border border-neutral-200 rounded-lg px-2.5 py-1.5 text-xs text-neutral-900 outline-none focus:border-neutral-900"
          >
            <option value="ALL">All Types</option>
            <option value="false">Income Only (+)</option>
            <option value="true">Expenses Only (-)</option>
          </select>

          {/* Export CSV */}
          <button
            onClick={exportCSV}
            className="flex items-center gap-1.5 px-3 py-2 border border-neutral-200 bg-white hover:bg-neutral-50 rounded-lg text-xs font-medium text-neutral-700 transition-colors"
          >
            <Download className="w-3.5 h-3.5" />
            <span>Export CSV</span>
          </button>

          {/* Add Transaction */}
          <button
            onClick={() => setIsAddOpen(true)}
            className="flex items-center gap-1.5 px-3.5 py-2 bg-neutral-900 hover:bg-neutral-800 text-white rounded-lg text-xs font-medium shadow-xs transition-colors"
          >
            <Plus className="w-3.5 h-3.5" />
            <span>Record Transaction</span>
          </button>
        </div>
      </div>

      {/* Transactions Table */}
      <div className="bg-white border border-neutral-200 rounded-xl overflow-hidden shadow-2xs">
        <div className="overflow-x-auto">
          <table className="w-full text-left text-xs border-collapse">
            <thead>
              <tr className="bg-neutral-50/80 border-b border-neutral-200 text-[11px] font-semibold text-neutral-500 uppercase tracking-wider">
                <th className="py-3 px-4">Date</th>
                <th className="py-3 px-4">Worker</th>
                <th className="py-3 px-4">Description / Urdu</th>
                <th className="py-3 px-4">Category</th>
                <th className="py-3 px-4">Method</th>
                <th className="py-3 px-4 text-right">Amount (PKR)</th>
                <th className="py-3 px-4 text-right">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-neutral-100">
              {loading ? (
                <tr>
                  <td colSpan={7} className="py-12 text-center text-neutral-400">
                    <RefreshCw className="w-5 h-5 animate-spin mx-auto mb-2" />
                    Loading platform ledger...
                  </td>
                </tr>
              ) : transactions.length === 0 ? (
                <tr>
                  <td colSpan={7} className="py-12 text-center text-neutral-500">
                    No transactions match your search.
                  </td>
                </tr>
              ) : (
                transactions.map((tx) => (
                  <tr key={tx.id} className="hover:bg-neutral-50/60 transition-colors">
                    <td className="py-3.5 px-4 font-mono text-neutral-500 text-[11px]">
                      {new Date(tx.date).toISOString().split('T')[0]}
                    </td>

                    <td className="py-3.5 px-4">
                      <p className="font-semibold text-neutral-900">{tx.workerName}</p>
                      <p className="text-[10px] text-neutral-400 truncate max-w-[150px]">{tx.factory || 'Worker'}</p>
                    </td>

                    <td className="py-3.5 px-4">
                      <p className="font-medium text-neutral-900">{tx.title}</p>
                      <p className="font-urdu text-[11px] text-neutral-500">{tx.urduTitle}</p>
                    </td>

                    <td className="py-3.5 px-4">
                      <Badge variant="neutral">{tx.category}</Badge>
                    </td>

                    <td className="py-3.5 px-4 text-neutral-600 font-mono text-[11px]">
                      {tx.paymentMethod}
                    </td>

                    <td className="py-3.5 px-4 text-right">
                      <span className={`font-mono font-bold text-xs ${tx.isExpense ? 'text-rose-700' : 'text-emerald-700'}`}>
                        {tx.isExpense ? '-' : '+'} Rs. {tx.amount.toLocaleString()}
                      </span>
                    </td>

                    <td className="py-3.5 px-4 text-right">
                      <button
                        onClick={() => handleDelete(tx.id)}
                        className="p-1 text-neutral-400 hover:text-rose-600 rounded hover:bg-rose-50"
                        title="Delete Record"
                      >
                        <Trash2 className="w-4 h-4" />
                      </button>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Add Transaction Modal */}
      <Modal
        isOpen={isAddOpen}
        onClose={() => setIsAddOpen(false)}
        title="Record Platform Transaction"
        urduTitle="نئی ٹرانزیکشن درج کریں"
        maxWidth="md"
      >
        <form onSubmit={handleCreate} className="space-y-4 text-xs">
          <div>
            <label className="block font-medium text-neutral-700 mb-1">Worker User ID</label>
            <input
              type="text"
              required
              placeholder="usr-101"
              value={formData.userId}
              onChange={(e) => setFormData({ ...formData, userId: e.target.value })}
              className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900 font-mono"
            />
          </div>

          <div className="grid grid-cols-2 gap-3">
            <div>
              <label className="block font-medium text-neutral-700 mb-1">Transaction Title</label>
              <input
                type="text"
                required
                placeholder="Ration Purchase"
                value={formData.title}
                onChange={(e) => setFormData({ ...formData, title: e.target.value })}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900"
              />
            </div>
            <div>
              <label className="block font-medium text-neutral-700 mb-1">Urdu Title</label>
              <input
                type="text"
                placeholder="راشن خریداری"
                value={formData.urduTitle}
                onChange={(e) => setFormData({ ...formData, urduTitle: e.target.value })}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900 font-urdu"
              />
            </div>
          </div>

          <div className="grid grid-cols-2 gap-3">
            <div>
              <label className="block font-medium text-neutral-700 mb-1">Amount (PKR) *</label>
              <input
                type="number"
                required
                min={1}
                placeholder="5000"
                value={formData.amount}
                onChange={(e) => setFormData({ ...formData, amount: e.target.value })}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900 font-mono"
              />
            </div>
            <div>
              <label className="block font-medium text-neutral-700 mb-1">Type</label>
              <select
                value={formData.isExpense ? 'expense' : 'income'}
                onChange={(e) => setFormData({ ...formData, isExpense: e.target.value === 'expense' })}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900"
              >
                <option value="expense">Expense (-)</option>
                <option value="income">Income (+)</option>
              </select>
            </div>
          </div>

          <div className="grid grid-cols-2 gap-3">
            <div>
              <label className="block font-medium text-neutral-700 mb-1">Category</label>
              <select
                value={formData.category}
                onChange={(e) => setFormData({ ...formData, category: e.target.value })}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900"
              >
                <option value="Salary">Salary</option>
                <option value="Groceries">Groceries</option>
                <option value="Utilities">Utilities</option>
                <option value="Kameti">Kameti</option>
                <option value="Healthcare">Healthcare</option>
                <option value="Side Hustle">Side Hustle</option>
                <option value="General">General</option>
              </select>
            </div>
            <div>
              <label className="block font-medium text-neutral-700 mb-1">Payment Method</label>
              <select
                value={formData.paymentMethod}
                onChange={(e) => setFormData({ ...formData, paymentMethod: e.target.value })}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900"
              >
                <option value="Cash">Cash (نقدی)</option>
                <option value="JazzCash">JazzCash</option>
                <option value="EasyPaisa">EasyPaisa</option>
                <option value="Bank Transfer">Bank Transfer</option>
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
              disabled={submitting}
              className="px-4 py-2 bg-neutral-900 text-white rounded-lg text-xs font-medium hover:bg-neutral-800 transition-colors"
            >
              {submitting ? 'Recording...' : 'Record Transaction'}
            </button>
          </div>
        </form>
      </Modal>
    </div>
  );
};
