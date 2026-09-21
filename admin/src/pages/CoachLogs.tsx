import React, { useState, useEffect } from 'react';
import {
  Bot,
  User,
  Search,
  RefreshCw,
  MessageSquareQuote,
  Sparkles
} from 'lucide-react';
import { CoachLog } from '../types';
import { api } from '../services/api';
import { Badge } from '../components/Badge';

export const CoachLogs: React.FC = () => {
  const [logs, setLogs] = useState<CoachLog[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [search, setSearch] = useState<string>('');

  const loadLogs = async () => {
    setLoading(true);
    try {
      const data = await api.getCoachLogs();
      setLogs(data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadLogs();
  }, []);

  const filteredLogs = logs.filter(l => 
    l.workerName.toLowerCase().includes(search.toLowerCase()) ||
    l.textUrdu.includes(search) ||
    l.textRoman.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <div className="space-y-6">
      {/* Top Banner */}
      <div className="bg-white border border-neutral-200 rounded-xl p-6 flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div>
          <div className="flex items-center gap-2">
            <h3 className="text-base font-bold text-neutral-900 tracking-tight flex items-center gap-2">
              <Bot className="w-5 h-5 text-neutral-900" />
              <span>AI Financial Coach Fatima — Worker Dialogue Audit</span>
            </h3>
            <Badge variant="neutral">AI Conversational Logs</Badge>
          </div>
          <p className="text-xs text-neutral-500 mt-1 max-w-2xl">
            Audit natural language interactions between factory workers and AI Coach Fatima to evaluate financial literacy, common emergency concerns, and debt relief guidance.
          </p>
        </div>
        <button
          onClick={loadLogs}
          className="flex items-center gap-1.5 px-3 py-1.5 border border-neutral-200 bg-white hover:bg-neutral-50 rounded-lg text-xs font-medium text-neutral-700 transition-colors"
        >
          <RefreshCw className="w-3.5 h-3.5" />
          <span>Refresh Dialogue Logs</span>
        </button>
      </div>

      {/* Search Bar */}
      <div className="bg-white border border-neutral-200 rounded-xl p-4">
        <div className="relative max-w-md">
          <Search className="w-4 h-4 text-neutral-400 absolute left-3 top-1/2 -translate-y-1/2" />
          <input
            type="text"
            placeholder="Search questions or advice (e.g. قرض، بچت، کمیٹی)..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            className="w-full bg-neutral-50 border border-neutral-200 rounded-lg pl-9 pr-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900"
          />
        </div>
      </div>

      {/* Conversation Thread Feed */}
      <div className="bg-white border border-neutral-200 rounded-xl overflow-hidden shadow-2xs">
        <div className="divide-y divide-neutral-100">
          {loading ? (
            <div className="py-12 text-center text-neutral-400 text-xs">
              <RefreshCw className="w-5 h-5 animate-spin mx-auto mb-2" />
              Loading AI coach dialogue records...
            </div>
          ) : filteredLogs.length === 0 ? (
            <div className="py-12 text-center text-neutral-500 text-xs">
              No coach conversations match your search.
            </div>
          ) : (
            filteredLogs.map((log) => (
              <div key={log.id} className="p-4 hover:bg-neutral-50/50 transition-colors">
                <div className="flex items-start gap-3">
                  <div
                    className={`w-8 h-8 rounded-full flex items-center justify-center shrink-0 border ${
                      log.isFromCoach
                        ? 'bg-neutral-900 text-white border-neutral-900'
                        : 'bg-neutral-100 text-neutral-800 border-neutral-200'
                    }`}
                  >
                    {log.isFromCoach ? <Bot className="w-4 h-4" /> : <User className="w-4 h-4" />}
                  </div>

                  <div className="flex-1 min-w-0">
                    <div className="flex items-center justify-between gap-2">
                      <div className="flex items-center gap-2">
                        <span className="text-xs font-bold text-neutral-900">
                          {log.isFromCoach ? 'Coach Fatima (کوچ فاطمہ)' : log.workerName}
                        </span>
                        {!log.isFromCoach && (
                          <span className="text-[10px] text-neutral-400 font-mono">
                            {log.workerPhone} • {log.factory}
                          </span>
                        )}
                      </div>
                      <span className="text-[11px] text-neutral-400 font-mono">
                        {new Date(log.createdAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })} • {new Date(log.createdAt).toLocaleDateString()}
                      </span>
                    </div>

                    <div className="mt-1.5 p-3 rounded-lg bg-neutral-50/80 border border-neutral-100 space-y-1">
                      <p className="text-xs text-neutral-900 font-urdu leading-relaxed text-right" dir="rtl">
                        {log.textUrdu}
                      </p>
                      {log.textRoman && (
                        <p className="text-[11px] text-neutral-600 font-sans border-t border-neutral-100 pt-1">
                          {log.textRoman}
                        </p>
                      )}
                    </div>
                  </div>
                </div>
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  );
};
