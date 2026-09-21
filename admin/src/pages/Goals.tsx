import React, { useState, useEffect } from 'react';
import {
  Target,
  Plus,
  Trash2,
  CheckCircle2,
  RefreshCw,
  Coins
} from 'lucide-react';
import { Goal } from '../types';
import { api } from '../services/api';
import { Badge } from '../components/Badge';
import { Modal } from '../components/Modal';

export const Goals: React.FC = () => {
  const [goals, setGoals] = useState<Goal[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [isAddOpen, setIsAddOpen] = useState<boolean>(false);
  const [isContributeOpen, setIsContributeOpen] = useState<boolean>(false);
  const [selectedGoal, setSelectedGoal] = useState<Goal | null>(null);
  const [contribution, setContribution] = useState<string>('');
  const [actionLoading, setActionLoading] = useState<boolean>(false);

  const [formData, setFormData] = useState({
    userId: 'usr-101',
    title: '',
    urduTitle: '',
    targetAmount: '',
    currentAmount: '',
    targetDate: 'May 2025',
    emoji: '🎯'
  });

  const loadGoals = async () => {
    setLoading(true);
    try {
      const data = await api.getGoals();
      setGoals(data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadGoals();
  }, []);

  const handleToggleComplete = async (goal: Goal) => {
    const nextState = !goal.isCompleted;
    await api.updateGoal(goal.id, { isCompleted: nextState });
    loadGoals();
  };

  const handleDelete = async (id: string) => {
    if (confirm('Delete this savings goal?')) {
      await api.deleteGoal(id);
      loadGoals();
    }
  };

  const handleContribute = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!selectedGoal || !contribution) return;
    setActionLoading(true);
    try {
      const amt = Number(contribution);
      const newCurrent = selectedGoal.currentAmount + amt;
      await api.updateGoal(selectedGoal.id, {
        currentAmount: newCurrent,
        isCompleted: newCurrent >= selectedGoal.targetAmount
      });
      setIsContributeOpen(false);
      setContribution('');
      loadGoals();
    } catch (err: any) {
      alert(err.message || 'Error recording contribution');
    } finally {
      setActionLoading(false);
    }
  };

  const handleCreate = async (e: React.FormEvent) => {
    e.preventDefault();
    setActionLoading(true);
    try {
      await api.createGoal(formData);
      setIsAddOpen(false);
      setFormData({
        userId: 'usr-101',
        title: '',
        urduTitle: '',
        targetAmount: '',
        currentAmount: '',
        targetDate: 'May 2025',
        emoji: '🎯'
      });
      loadGoals();
    } catch (err: any) {
      alert(err.message || 'Error creating goal');
    } finally {
      setActionLoading(false);
    }
  };

  const totalTarget = goals.reduce((sum, g) => sum + g.targetAmount, 0);
  const totalSaved = goals.reduce((sum, g) => sum + g.currentAmount, 0);
  const completedCount = goals.filter(g => g.isCompleted).length;

  return (
    <div className="space-y-6">
      {/* Top Banner Stats */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
        <div className="bg-white border border-neutral-200 rounded-xl p-5">
          <div className="flex items-center gap-1.5 text-xs text-neutral-500">
            <Target className="w-4 h-4 text-neutral-900" />
            <span>Total Targeted Goals</span>
          </div>
          <h3 className="text-2xl font-bold font-mono text-neutral-900 mt-1">
            Rs. {totalTarget.toLocaleString()}
          </h3>
          <p className="text-xs text-neutral-500 mt-0.5">{goals.length} Registered Worker Goals</p>
        </div>

        <div className="bg-white border border-neutral-200 rounded-xl p-5">
          <div className="flex items-center gap-1.5 text-xs text-neutral-500">
            <Coins className="w-4 h-4 text-emerald-600" />
            <span>Accumulated Worker Savings</span>
          </div>
          <h3 className="text-2xl font-bold font-mono text-neutral-900 mt-1">
            Rs. {totalSaved.toLocaleString()}
          </h3>
          <p className="text-xs text-neutral-500 mt-0.5">
            {Math.round((totalSaved / (totalTarget || 1)) * 100)}% of Targets Achieved
          </p>
        </div>

        <div className="bg-white border border-neutral-200 rounded-xl p-5">
          <div className="flex items-center gap-1.5 text-xs text-neutral-500">
            <CheckCircle2 className="w-4 h-4 text-emerald-600" />
            <span>Completed Goals</span>
          </div>
          <h3 className="text-2xl font-bold font-mono text-emerald-700 mt-1">
            {completedCount} of {goals.length}
          </h3>
          <p className="text-xs text-neutral-500 mt-0.5">Purchases & Milestones Reached</p>
        </div>
      </div>

      {/* Header and Action */}
      <div className="bg-white border border-neutral-200 rounded-xl p-4 flex items-center justify-between">
        <div>
          <h3 className="text-sm font-bold text-neutral-900 uppercase">Targeted Savings Goals</h3>
          <p className="text-xs text-neutral-500">Track worker aspirations for education, machinery, Eid, and assets</p>
        </div>
        <button
          onClick={() => setIsAddOpen(true)}
          className="flex items-center gap-1.5 px-3.5 py-2 bg-neutral-900 hover:bg-neutral-800 text-white rounded-lg text-xs font-medium shadow-xs transition-colors"
        >
          <Plus className="w-3.5 h-3.5" />
          <span>Add Goal</span>
        </button>
      </div>

      {/* Goals Table */}
      <div className="bg-white border border-neutral-200 rounded-xl overflow-hidden shadow-2xs">
        <div className="overflow-x-auto">
          <table className="w-full text-left text-xs border-collapse">
            <thead>
              <tr className="bg-neutral-50/80 border-b border-neutral-200 text-[11px] font-semibold text-neutral-500 uppercase tracking-wider">
                <th className="py-3 px-4">Goal & Emoji</th>
                <th className="py-3 px-4">Worker Profile</th>
                <th className="py-3 px-4">Target Amount</th>
                <th className="py-3 px-4">Current Saved</th>
                <th className="py-3 px-4">Progress</th>
                <th className="py-3 px-4">Target Date</th>
                <th className="py-3 px-4">Status</th>
                <th className="py-3 px-4 text-right">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-neutral-100">
              {loading ? (
                <tr>
                  <td colSpan={8} className="py-12 text-center text-neutral-400">
                    <RefreshCw className="w-5 h-5 animate-spin mx-auto mb-2" />
                    Loading worker goals...
                  </td>
                </tr>
              ) : goals.length === 0 ? (
                <tr>
                  <td colSpan={8} className="py-12 text-center text-neutral-500">
                    No savings goals found.
                  </td>
                </tr>
              ) : (
                goals.map((goal) => {
                  const pct = Math.min(100, Math.round((goal.currentAmount / (goal.targetAmount || 1)) * 100));
                  return (
                    <tr key={goal.id} className="hover:bg-neutral-50/60 transition-colors">
                      <td className="py-3.5 px-4">
                        <div className="flex items-center gap-2.5">
                          <span className="text-xl">{goal.emoji || '🎯'}</span>
                          <div>
                            <p className="font-semibold text-neutral-900">{goal.title}</p>
                            <p className="font-urdu text-[11px] text-neutral-500">{goal.urduTitle}</p>
                          </div>
                        </div>
                      </td>

                      <td className="py-3.5 px-4">
                        <p className="font-medium text-neutral-900">{goal.workerName}</p>
                        <p className="text-[10px] text-neutral-400">{goal.factory || 'Worker'}</p>
                      </td>

                      <td className="py-3.5 px-4 font-mono font-bold text-neutral-900">
                        Rs. {goal.targetAmount.toLocaleString()}
                      </td>

                      <td className="py-3.5 px-4 font-mono font-bold text-emerald-700">
                        Rs. {goal.currentAmount.toLocaleString()}
                      </td>

                      <td className="py-3.5 px-4">
                        <div className="space-y-1">
                          <div className="flex justify-between font-mono text-[10px] text-neutral-500">
                            <span>{pct}%</span>
                          </div>
                          <div className="w-24 bg-neutral-100 h-1.5 rounded-full overflow-hidden">
                            <div
                              className={`h-full rounded-full ${pct >= 100 ? 'bg-emerald-500' : 'bg-neutral-900'}`}
                              style={{ width: `${pct}%` }}
                            />
                          </div>
                        </div>
                      </td>

                      <td className="py-3.5 px-4 font-mono text-neutral-600">
                        {goal.targetDate || 'Flexible'}
                      </td>

                      <td className="py-3.5 px-4">
                        <button
                          onClick={() => handleToggleComplete(goal)}
                          className="cursor-pointer"
                        >
                          <Badge variant={goal.isCompleted ? 'success' : 'neutral'}>
                            {goal.isCompleted ? 'Completed' : 'In Progress'}
                          </Badge>
                        </button>
                      </td>

                      <td className="py-3.5 px-4 text-right">
                        <div className="flex items-center justify-end gap-1.5">
                          <button
                            onClick={() => {
                              setSelectedGoal(goal);
                              setContribution('');
                              setIsContributeOpen(true);
                            }}
                            className="px-2 py-1 bg-neutral-900 text-white rounded text-[11px] font-medium hover:bg-neutral-800"
                          >
                            Add Funds
                          </button>
                          <button
                            onClick={() => handleDelete(goal.id)}
                            className="p-1 text-neutral-400 hover:text-rose-600 rounded hover:bg-rose-50"
                          >
                            <Trash2 className="w-4 h-4" />
                          </button>
                        </div>
                      </td>
                    </tr>
                  );
                })
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Contribute Modal */}
      {selectedGoal && (
        <Modal
          isOpen={isContributeOpen}
          onClose={() => setIsContributeOpen(false)}
          title={`Add Funds: ${selectedGoal.title}`}
          urduTitle="ہدف میں رقم شامل کریں"
          maxWidth="sm"
        >
          <form onSubmit={handleContribute} className="space-y-4 text-xs">
            <div className="p-3 bg-neutral-50 border border-neutral-200 rounded-lg space-y-1">
              <p className="text-neutral-600">Target: <strong className="text-neutral-900 font-mono">Rs. {selectedGoal.targetAmount.toLocaleString()}</strong></p>
              <p className="text-neutral-600">Currently Saved: <strong className="text-emerald-700 font-mono">Rs. {selectedGoal.currentAmount.toLocaleString()}</strong></p>
            </div>

            <div>
              <label className="block font-medium text-neutral-700 mb-1">Contribution Amount (PKR) *</label>
              <input
                type="number"
                required
                min={1}
                placeholder="2000"
                value={contribution}
                onChange={(e) => setContribution(e.target.value)}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900 font-mono"
              />
            </div>

            <div className="flex justify-end gap-2 pt-2 border-t border-neutral-100">
              <button
                type="button"
                onClick={() => setIsContributeOpen(false)}
                className="px-3 py-1.5 border border-neutral-200 rounded-lg text-xs font-medium text-neutral-700 hover:bg-neutral-50"
              >
                Cancel
              </button>
              <button
                type="submit"
                disabled={actionLoading}
                className="px-3.5 py-1.5 bg-neutral-900 text-white rounded-lg text-xs font-medium hover:bg-neutral-800 transition-colors"
              >
                {actionLoading ? 'Saving...' : 'Add Contribution'}
              </button>
            </div>
          </form>
        </Modal>
      )}

      {/* Add Goal Modal */}
      <Modal
        isOpen={isAddOpen}
        onClose={() => setIsAddOpen(false)}
        title="Create Savings Goal"
        urduTitle="نیا ہدف شامل کریں"
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
              <label className="block font-medium text-neutral-700 mb-1">Goal Title</label>
              <input
                type="text"
                required
                placeholder="Children School Admission"
                value={formData.title}
                onChange={(e) => setFormData({ ...formData, title: e.target.value })}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900"
              />
            </div>
            <div>
              <label className="block font-medium text-neutral-700 mb-1">Urdu Title</label>
              <input
                type="text"
                placeholder="بچوں کے اسکول داخلہ"
                value={formData.urduTitle}
                onChange={(e) => setFormData({ ...formData, urduTitle: e.target.value })}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900 font-urdu"
              />
            </div>
          </div>

          <div className="grid grid-cols-3 gap-3">
            <div>
              <label className="block font-medium text-neutral-700 mb-1">Target Amount (PKR)</label>
              <input
                type="number"
                required
                min={1}
                placeholder="30000"
                value={formData.targetAmount}
                onChange={(e) => setFormData({ ...formData, targetAmount: e.target.value })}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900 font-mono"
              />
            </div>
            <div>
              <label className="block font-medium text-neutral-700 mb-1">Initial Saved</label>
              <input
                type="number"
                min={0}
                placeholder="0"
                value={formData.currentAmount}
                onChange={(e) => setFormData({ ...formData, currentAmount: e.target.value })}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900 font-mono"
              />
            </div>
            <div>
              <label className="block font-medium text-neutral-700 mb-1">Target Date</label>
              <input
                type="text"
                placeholder="May 2025"
                value={formData.targetDate}
                onChange={(e) => setFormData({ ...formData, targetDate: e.target.value })}
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
              {actionLoading ? 'Saving...' : 'Save Goal'}
            </button>
          </div>
        </form>
      </Modal>
    </div>
  );
};
