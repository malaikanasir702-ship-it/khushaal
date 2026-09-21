import React, { useState, useEffect } from 'react';
import {
  TrendingUp,
  RefreshCw,
  ShieldCheck,
  Award,
  AlertTriangle,
  CheckCircle,
  HelpCircle
} from 'lucide-react';
import { ProsperityScore } from '../types';
import { api } from '../services/api';
import { Badge } from '../components/Badge';

export const ProsperityIndexPage: React.FC = () => {
  const [scores, setScores] = useState<ProsperityScore[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [recalculatingId, setRecalculatingId] = useState<string | null>(null);

  const loadScores = async () => {
    setLoading(true);
    try {
      const data = await api.getProsperity();
      setScores(data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadScores();
  }, []);

  const handleRecalculate = async (userId: string) => {
    setRecalculatingId(userId);
    try {
      await api.recalculateProsperity(userId);
      alert('Prosperity index successfully recalculated using latest transactions, locker buffer, and debt balances!');
      loadScores();
    } catch (err: any) {
      alert(err.message || 'Error recalculating prosperity');
    } finally {
      setRecalculatingId(null);
    }
  };

  const avgScore = scores.length > 0 ? Math.round(scores.reduce((sum, s) => sum + s.score, 0) / scores.length) : 0;
  const highTier = scores.filter(s => s.score >= 70).length;
  const midTier = scores.filter(s => s.score >= 30 && s.score < 70).length;
  const lowTier = scores.filter(s => s.score < 30).length;

  const pillarsReference = [
    { id: 1, name: 'Budgeting & Expense Discipline', urdu: 'بجٹ اور اخراجات پر قابو', weight: '20%', desc: 'Tracks monthly transaction frequency and envelope allocation discipline' },
    { id: 2, name: 'Regular Savings Habit', urdu: 'مسلسل بچت کی عادت', weight: '20%', desc: 'Measures active kameti participation and automated savings vs income' },
    { id: 3, name: 'Emergency Safety Buffer', urdu: 'ہنگامی تحفظ کا بفر', weight: '20%', desc: 'Days of living runway provided by liquid emergency locker reserves' },
    { id: 4, name: 'Debt & Borrowing Control', urdu: 'قرضوں پر کنٹرول', weight: '15%', desc: 'Debt-to-income ratio and adherence to debt snowball repayment' },
    { id: 5, name: 'Digital Safety & Fraud Defense', urdu: 'ڈیجیٹل تحفظ اور آگاہی', weight: '10%', desc: 'Worker awareness against OTP fraud and secure JazzCash usage' },
    { id: 6, name: 'Income Resilience & Goals', urdu: 'آمدنی کا استحکام اور اہداف', weight: '15%', desc: 'Active savings goals and microenterprise side-hustle order income' }
  ];

  return (
    <div className="space-y-6">
      {/* Top Banner Stats */}
      <div className="grid grid-cols-1 sm:grid-cols-4 gap-4">
        <div className="bg-white border border-neutral-200 rounded-xl p-5">
          <div className="flex items-center gap-1.5 text-xs text-neutral-500">
            <Award className="w-4 h-4 text-neutral-900" />
            <span>Workforce Average Index</span>
          </div>
          <h3 className="text-3xl font-bold font-mono text-neutral-900 mt-1">
            {avgScore} <span className="text-sm font-normal text-neutral-400">/ 100</span>
          </h3>
          <p className="text-xs text-neutral-500 mt-0.5">Platform Holistic Financial Health</p>
        </div>

        <div className="bg-white border border-neutral-200 rounded-xl p-5">
          <div className="flex items-center gap-1.5 text-xs text-emerald-700">
            <CheckCircle className="w-4 h-4 text-emerald-600" />
            <span>High Prosperity (&gt;= 70)</span>
          </div>
          <h3 className="text-3xl font-bold font-mono text-emerald-700 mt-1">
            {highTier}
          </h3>
          <p className="text-xs text-neutral-500 mt-0.5">Self-Reliant & Buffer Protected</p>
        </div>

        <div className="bg-white border border-neutral-200 rounded-xl p-5">
          <div className="flex items-center gap-1.5 text-xs text-amber-700">
            <AlertTriangle className="w-4 h-4 text-amber-600" />
            <span>Moderate Stability (30-69)</span>
          </div>
          <h3 className="text-3xl font-bold font-mono text-amber-700 mt-1">
            {midTier}
          </h3>
          <p className="text-xs text-neutral-500 mt-0.5">Building Habit & Managing Debt</p>
        </div>

        <div className="bg-white border border-neutral-200 rounded-xl p-5">
          <div className="flex items-center gap-1.5 text-xs text-rose-700">
            <ShieldCheck className="w-4 h-4 text-rose-600" />
            <span>High Vulnerability (&lt; 30)</span>
          </div>
          <h3 className="text-3xl font-bold font-mono text-rose-700 mt-1">
            {lowTier}
          </h3>
          <p className="text-xs text-neutral-500 mt-0.5">Requires Direct Coaching Intervention</p>
        </div>
      </div>

      {/* 6 Pillars Methodology Reference */}
      <div className="bg-white border border-neutral-200 rounded-xl p-6 space-y-4">
        <div className="border-b border-neutral-100 pb-3">
          <h3 className="text-sm font-bold text-neutral-900 uppercase">
            The 6 Pillars of Industrial Worker Prosperity
          </h3>
          <p className="text-xs text-neutral-500 font-urdu mt-0.5">
            خوشحالی کے چھ بنیادی ستون جن پر مزدوروں کی مالی خودمختاری کی جانچ کی جاتی ہے
          </p>
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4 text-xs">
          {pillarsReference.map((pillar) => (
            <div key={pillar.id} className="p-4 bg-neutral-50 rounded-lg border border-neutral-200 space-y-1.5">
              <div className="flex justify-between items-start">
                <span className="font-bold text-neutral-900">Pillar {pillar.id}</span>
                <Badge variant="neutral">{pillar.weight} Weight</Badge>
              </div>
              <p className="font-semibold text-neutral-800">{pillar.name}</p>
              <p className="font-urdu text-[11px] text-neutral-500">{pillar.urdu}</p>
              <p className="text-[11px] text-neutral-500 pt-1 leading-relaxed">{pillar.desc}</p>
            </div>
          ))}
        </div>
      </div>

      {/* Workers Prosperity Table */}
      <div className="bg-white border border-neutral-200 rounded-xl overflow-hidden shadow-2xs">
        <div className="p-4 border-b border-neutral-200 flex justify-between items-center">
          <div>
            <h3 className="text-sm font-bold text-neutral-900 uppercase">Worker Prosperity Index Scores</h3>
            <p className="text-xs text-neutral-500">Live ratings and calculation triggers</p>
          </div>
          <button
            onClick={loadScores}
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
                <th className="py-3 px-4">Worker Profile</th>
                <th className="py-3 px-4">Factory</th>
                <th className="py-3 px-4">Prosperity Index</th>
                <th className="py-3 px-4">Living Runway</th>
                <th className="py-3 px-4">Savings Rate</th>
                <th className="py-3 px-4">Last Evaluated</th>
                <th className="py-3 px-4 text-right">Engine Action</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-neutral-100">
              {loading ? (
                <tr>
                  <td colSpan={7} className="py-12 text-center text-neutral-400">
                    <RefreshCw className="w-5 h-5 animate-spin mx-auto mb-2" />
                    Calculating scores...
                  </td>
                </tr>
              ) : scores.length === 0 ? (
                <tr>
                  <td colSpan={7} className="py-12 text-center text-neutral-500">
                    No prosperity score records found.
                  </td>
                </tr>
              ) : (
                scores.map((item) => (
                  <tr key={item.id} className="hover:bg-neutral-50/60 transition-colors">
                    <td className="py-3.5 px-4 font-semibold text-neutral-900">
                      {item.workerName}
                    </td>

                    <td className="py-3.5 px-4 text-neutral-700">
                      {item.factory || 'Worker'}
                    </td>

                    <td className="py-3.5 px-4">
                      <div className="flex items-center gap-2">
                        <span className="font-mono font-bold text-sm text-neutral-900">
                          {item.score}/100
                        </span>
                        <Badge
                          variant={
                            item.score >= 70
                              ? 'success'
                              : item.score >= 30
                              ? 'warning'
                              : 'danger'
                          }
                        >
                          {item.score >= 70 ? 'Prosperous' : item.score >= 30 ? 'Moderate' : 'At Risk'}
                        </Badge>
                      </div>
                    </td>

                    <td className="py-3.5 px-4 font-mono text-neutral-700">
                      {item.daysRunway} Days
                    </td>

                    <td className="py-3.5 px-4 font-mono text-neutral-700">
                      {Math.round((item.savingsPct || 0) * 100)}% of Income
                    </td>

                    <td className="py-3.5 px-4 font-mono text-neutral-500 text-[11px]">
                      {new Date(item.lastCalculatedAt).toLocaleDateString()}
                    </td>

                    <td className="py-3.5 px-4 text-right">
                      <button
                        onClick={() => handleRecalculate(item.userId!)}
                        disabled={recalculatingId === item.userId}
                        className="px-2.5 py-1 bg-neutral-900 text-white rounded text-[11px] font-medium hover:bg-neutral-800 disabled:opacity-50 transition-colors inline-flex items-center gap-1"
                      >
                        <RefreshCw className={`w-3 h-3 ${recalculatingId === item.userId ? 'animate-spin' : ''}`} />
                        <span>Recalculate</span>
                      </button>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};
