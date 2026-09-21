import React, { useState, useEffect } from 'react';
import {
  Megaphone,
  Send,
  Bell,
  CheckCircle2,
  RefreshCw,
  ShieldAlert
} from 'lucide-react';
import { NotificationItem } from '../types';
import { api } from '../services/api';
import { Badge } from '../components/Badge';

export const Broadcast: React.FC = () => {
  const [notifications, setNotifications] = useState<NotificationItem[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [sending, setSending] = useState<boolean>(false);

  const [formData, setFormData] = useState({
    titleEnglish: '',
    titleUrdu: '',
    descriptionEnglish: '',
    descriptionUrdu: '',
    category: 'SYSTEM',
    targetFactory: 'ALL'
  });

  const loadNotifications = async () => {
    setLoading(true);
    try {
      const data = await api.getNotifications();
      setNotifications(data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadNotifications();
  }, []);

  const handleBroadcast = async (e: React.FormEvent) => {
    e.preventDefault();
    setSending(true);
    try {
      await api.broadcastNotification(formData);
      alert('Broadcast alert successfully dispatched to workers!');
      setFormData({
        titleEnglish: '',
        titleUrdu: '',
        descriptionEnglish: '',
        descriptionUrdu: '',
        category: 'SYSTEM',
        targetFactory: 'ALL'
      });
      loadNotifications();
    } catch (err: any) {
      alert(err.message || 'Error broadcasting message');
    } finally {
      setSending(false);
    }
  };

  return (
    <div className="space-y-6">
      {/* Broadcast Composer Form */}
      <div className="bg-white border border-neutral-200 rounded-xl p-6 space-y-4">
        <div className="flex items-center justify-between pb-3 border-b border-neutral-100">
          <div>
            <h3 className="text-base font-bold text-neutral-900 tracking-tight flex items-center gap-2">
              <Megaphone className="w-5 h-5 text-neutral-900" />
              <span>Compose Broadcast Alert / Push Notification</span>
            </h3>
            <p className="text-xs text-neutral-500 font-urdu mt-0.5">
              ورکرز کے موبائل پر فوری الرٹ اور یاد دہانی روانہ کریں
            </p>
          </div>
          <Badge variant="neutral">Platform Broadcast Center</Badge>
        </div>

        <form onSubmit={handleBroadcast} className="space-y-4 text-xs">
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <div>
              <label className="block font-medium text-neutral-700 mb-1">Alert Title (English) *</label>
              <input
                type="text"
                required
                placeholder="e.g. Factory Advance Salary Schedule"
                value={formData.titleEnglish}
                onChange={(e) => setFormData({ ...formData, titleEnglish: e.target.value })}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900"
              />
            </div>
            <div>
              <label className="block font-medium text-neutral-700 mb-1">Alert Title (Urdu / اردو عنوان) *</label>
              <input
                type="text"
                required
                placeholder="فیکٹری ایڈوانس تنخواہ کا شیڈول"
                value={formData.titleUrdu}
                onChange={(e) => setFormData({ ...formData, titleUrdu: e.target.value })}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900 font-urdu"
              />
            </div>
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <div>
              <label className="block font-medium text-neutral-700 mb-1">Message Description (English)</label>
              <textarea
                rows={3}
                placeholder="Advance salary for Eid celebrations will be disbursed on 24th March."
                value={formData.descriptionEnglish}
                onChange={(e) => setFormData({ ...formData, descriptionEnglish: e.target.value })}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900"
              />
            </div>
            <div>
              <label className="block font-medium text-neutral-700 mb-1">Message Description (Urdu / اردو تفصیل)</label>
              <textarea
                rows={3}
                placeholder="عید کی تعطیلات کے لیے ایڈوانس تنخواہیں 24 مارچ کو فیکٹری اکاؤنٹ میں ٹرانسفر ہو جائیں گی۔"
                value={formData.descriptionUrdu}
                onChange={(e) => setFormData({ ...formData, descriptionUrdu: e.target.value })}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900 font-urdu"
              />
            </div>
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 pt-1">
            <div>
              <label className="block font-medium text-neutral-700 mb-1">Category & Priority</label>
              <select
                value={formData.category}
                onChange={(e) => setFormData({ ...formData, category: e.target.value })}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900"
              >
                <option value="SYSTEM">General System Notice (عام اعلان)</option>
                <option value="BILL">Utility Bill Payment Deadline (بل الرٹ)</option>
                <option value="KAMETI">Kameti Turn / Payout Notice (کمیٹی الرٹ)</option>
                <option value="FRAUD">Fraud & Digital Safety Warning (فراڈ سے تحفظ)</option>
                <option value="GOAL">Savings & Prosperity Incentive (بچت ترغیب)</option>
              </select>
            </div>
            <div>
              <label className="block font-medium text-neutral-700 mb-1">Target Recipients Audience</label>
              <select
                value={formData.targetFactory}
                onChange={(e) => setFormData({ ...formData, targetFactory: e.target.value })}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900"
              >
                <option value="ALL">Broadcast to All Enrolled Workers (تمام ورکرز)</option>
                <option value="Nishat Mills (Weaving Unit 4)">Nishat Mills (Weaving Unit 4)</option>
                <option value="Crescent Bahuman Denim Plant">Crescent Bahuman Denim Plant</option>
                <option value="Artistic Milliners Garment Div">Artistic Milliners Garment Div</option>
                <option value="Lucky Textile Mills (Karachi)">Lucky Textile Mills (Karachi)</option>
                <option value="Interloop Hosiery Plant 1 (Faisalabad)">Interloop Hosiery Plant 1</option>
              </select>
            </div>
          </div>

          <div className="pt-2 flex justify-end">
            <button
              type="submit"
              disabled={sending}
              className="flex items-center gap-2 px-5 py-2.5 bg-neutral-900 hover:bg-neutral-800 text-white rounded-lg text-xs font-semibold shadow-xs transition-colors"
            >
              <Send className="w-3.5 h-3.5" />
              <span>{sending ? 'Dispatching Broadcast...' : 'Dispatch Broadcast Now'}</span>
            </button>
          </div>
        </form>
      </div>

      {/* Dispatched History */}
      <div className="bg-white border border-neutral-200 rounded-xl overflow-hidden shadow-2xs">
        <div className="p-4 border-b border-neutral-200 flex justify-between items-center">
          <div>
            <h3 className="text-sm font-bold text-neutral-900 uppercase">Recent Dispatched Announcements</h3>
            <p className="text-xs text-neutral-500">Log of SMS and mobile app notifications</p>
          </div>
          <button
            onClick={loadNotifications}
            className="flex items-center gap-1 text-xs text-neutral-600 hover:text-neutral-900 border border-neutral-200 px-2.5 py-1.5 rounded-lg"
          >
            <RefreshCw className="w-3.5 h-3.5" />
            <span>Refresh</span>
          </button>
        </div>

        <div className="divide-y divide-neutral-100">
          {loading ? (
            <div className="py-12 text-center text-neutral-400 text-xs">
              <RefreshCw className="w-5 h-5 animate-spin mx-auto mb-2" />
              Loading notification logs...
            </div>
          ) : notifications.length === 0 ? (
            <div className="py-12 text-center text-neutral-500 text-xs">
              No notifications dispatched yet.
            </div>
          ) : (
            notifications.map((item) => (
              <div key={item.id} className="p-4 hover:bg-neutral-50/60 transition-colors flex flex-col md:flex-row md:items-center justify-between gap-3">
                <div className="space-y-1">
                  <div className="flex items-center gap-2">
                    <Badge variant="neutral">{item.category}</Badge>
                    <span className="text-xs font-bold text-neutral-900">{item.titleEnglish}</span>
                    <span className="text-xs font-urdu text-neutral-600">({item.titleUrdu})</span>
                  </div>
                  <p className="text-xs text-neutral-600 max-w-3xl">
                    {item.descriptionEnglish || item.descriptionUrdu}
                  </p>
                </div>
                <div className="text-right shrink-0 text-xs text-neutral-500 font-mono">
                  <p className="font-semibold text-neutral-700">{item.workerName}</p>
                  <p className="text-[11px] text-neutral-400">{new Date(item.createdAt).toLocaleString()}</p>
                </div>
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  );
};
