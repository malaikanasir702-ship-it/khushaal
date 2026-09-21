import React, { useState, useEffect } from 'react';
import {
  ShoppingBag,
  Plus,
  Trash2,
  CheckCircle2,
  Clock,
  RefreshCw,
  TrendingUp,
  Package
} from 'lucide-react';
import { Order } from '../types';
import { api } from '../services/api';
import { Badge } from '../components/Badge';
import { Modal } from '../components/Modal';

export const Orders: React.FC = () => {
  const [orders, setOrders] = useState<Order[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [isAddOpen, setIsAddOpen] = useState<boolean>(false);
  const [actionLoading, setActionLoading] = useState<boolean>(false);

  const [formData, setFormData] = useState({
    userId: 'usr-103',
    customerName: '',
    phone: '',
    serviceTitle: '3 Ladies Fancy Suits Stitching',
    totalAmount: '',
    advancePaid: '',
    dueDate: '2025-03-25'
  });

  const loadOrders = async () => {
    setLoading(true);
    try {
      const data = await api.getOrders();
      setOrders(data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadOrders();
  }, []);

  const handleToggleDelivered = async (order: Order) => {
    const nextState = !order.isDelivered;
    await api.updateOrder(order.id, { isDelivered: nextState });
    loadOrders();
  };

  const handleTogglePaid = async (order: Order) => {
    const nextState = !order.isFullyPaid;
    await api.updateOrder(order.id, {
      isFullyPaid: nextState,
      advancePaid: nextState ? order.totalAmount : order.advancePaid
    });
    loadOrders();
  };

  const handleDelete = async (id: string) => {
    if (confirm('Delete this microenterprise order?')) {
      await api.deleteOrder(id);
      loadOrders();
    }
  };

  const handleCreate = async (e: React.FormEvent) => {
    e.preventDefault();
    setActionLoading(true);
    try {
      await api.createOrder(formData);
      setIsAddOpen(false);
      setFormData({
        userId: 'usr-103',
        customerName: '',
        phone: '',
        serviceTitle: '3 Ladies Fancy Suits Stitching',
        totalAmount: '',
        advancePaid: '',
        dueDate: '2025-03-25'
      });
      loadOrders();
    } catch (err: any) {
      alert(err.message || 'Error creating order');
    } finally {
      setActionLoading(false);
    }
  };

  const totalRevenue = orders.reduce((sum, o) => sum + o.totalAmount, 0);
  const totalAdvance = orders.reduce((sum, o) => sum + o.advancePaid, 0);
  const deliveredCount = orders.filter(o => o.isDelivered).length;

  return (
    <div className="space-y-6">
      {/* Metrics Banner */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
        <div className="bg-white border border-neutral-200 rounded-xl p-5">
          <div className="flex items-center gap-1.5 text-xs text-neutral-500">
            <ShoppingBag className="w-4 h-4 text-neutral-900" />
            <span>Total Enterprise Revenue</span>
          </div>
          <h3 className="text-2xl font-bold font-mono text-neutral-900 mt-1">
            Rs. {totalRevenue.toLocaleString()}
          </h3>
          <p className="text-xs text-neutral-500 mt-0.5">{orders.length} Side-Hustle Customer Orders</p>
        </div>

        <div className="bg-white border border-neutral-200 rounded-xl p-5">
          <div className="flex items-center gap-1.5 text-xs text-neutral-500">
            <TrendingUp className="w-4 h-4 text-emerald-600" />
            <span>Advance Cash Collected</span>
          </div>
          <h3 className="text-2xl font-bold font-mono text-neutral-900 mt-1">
            Rs. {totalAdvance.toLocaleString()}
          </h3>
          <p className="text-xs text-neutral-500 mt-0.5">Liquid Working Capital in Hand</p>
        </div>

        <div className="bg-white border border-neutral-200 rounded-xl p-5">
          <div className="flex items-center gap-1.5 text-xs text-neutral-500">
            <Package className="w-4 h-4 text-neutral-900" />
            <span>Orders Completed & Delivered</span>
          </div>
          <h3 className="text-2xl font-bold font-mono text-neutral-900 mt-1">
            {deliveredCount} of {orders.length}
          </h3>
          <p className="text-xs text-neutral-500 mt-0.5">Reliable Microenterprise Delivery</p>
        </div>
      </div>

      {/* Header and Action */}
      <div className="bg-white border border-neutral-200 rounded-xl p-4 flex items-center justify-between">
        <div>
          <h3 className="text-sm font-bold text-neutral-900 uppercase">Worker Microenterprise Orders</h3>
          <p className="text-xs text-neutral-500">Supervise extra income generation from stitching, repairs, and crafting</p>
        </div>
        <button
          onClick={() => setIsAddOpen(true)}
          className="flex items-center gap-1.5 px-3.5 py-2 bg-neutral-900 hover:bg-neutral-800 text-white rounded-lg text-xs font-medium shadow-xs transition-colors"
        >
          <Plus className="w-3.5 h-3.5" />
          <span>New Customer Order</span>
        </button>
      </div>

      {/* Orders Table */}
      <div className="bg-white border border-neutral-200 rounded-xl overflow-hidden shadow-2xs">
        <div className="overflow-x-auto">
          <table className="w-full text-left text-xs border-collapse">
            <thead>
              <tr className="bg-neutral-50/80 border-b border-neutral-200 text-[11px] font-semibold text-neutral-500 uppercase tracking-wider">
                <th className="py-3 px-4">Service & Product</th>
                <th className="py-3 px-4">Worker Entrepreneur</th>
                <th className="py-3 px-4">Customer Contact</th>
                <th className="py-3 px-4">Due Date</th>
                <th className="py-3 px-4">Order Value</th>
                <th className="py-3 px-4">Advance Paid</th>
                <th className="py-3 px-4">Delivery</th>
                <th className="py-3 px-4">Payment</th>
                <th className="py-3 px-4 text-right">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-neutral-100">
              {loading ? (
                <tr>
                  <td colSpan={9} className="py-12 text-center text-neutral-400">
                    <RefreshCw className="w-5 h-5 animate-spin mx-auto mb-2" />
                    Loading microenterprise orders...
                  </td>
                </tr>
              ) : orders.length === 0 ? (
                <tr>
                  <td colSpan={9} className="py-12 text-center text-neutral-500">
                    No orders registered yet.
                  </td>
                </tr>
              ) : (
                orders.map((order) => (
                  <tr key={order.id} className="hover:bg-neutral-50/60 transition-colors">
                    <td className="py-3.5 px-4 font-semibold text-neutral-900">
                      {order.serviceTitle}
                    </td>

                    <td className="py-3.5 px-4">
                      <p className="font-medium text-neutral-900">{order.workerName}</p>
                      <p className="text-[10px] text-neutral-400">{order.factory || 'Worker'}</p>
                    </td>

                    <td className="py-3.5 px-4">
                      <p className="font-medium text-neutral-900">{order.customerName}</p>
                      <p className="text-[11px] font-mono text-neutral-500">{order.phone || 'N/A'}</p>
                    </td>

                    <td className="py-3.5 px-4 font-mono text-neutral-600">
                      {order.dueDate || 'Flexible'}
                    </td>

                    <td className="py-3.5 px-4 font-mono font-bold text-neutral-900">
                      Rs. {order.totalAmount.toLocaleString()}
                    </td>

                    <td className="py-3.5 px-4 font-mono font-bold text-emerald-700">
                      Rs. {order.advancePaid.toLocaleString()}
                    </td>

                    <td className="py-3.5 px-4">
                      <button
                        onClick={() => handleToggleDelivered(order)}
                        className="cursor-pointer"
                      >
                        <Badge variant={order.isDelivered ? 'success' : 'warning'}>
                          {order.isDelivered ? 'Delivered' : 'Pending'}
                        </Badge>
                      </button>
                    </td>

                    <td className="py-3.5 px-4">
                      <button
                        onClick={() => handleTogglePaid(order)}
                        className="cursor-pointer"
                      >
                        <Badge variant={order.isFullyPaid ? 'success' : 'danger'}>
                          {order.isFullyPaid ? 'Fully Paid' : 'Balance Due'}
                        </Badge>
                      </button>
                    </td>

                    <td className="py-3.5 px-4 text-right">
                      <button
                        onClick={() => handleDelete(order.id)}
                        className="p-1 text-neutral-400 hover:text-rose-600 rounded hover:bg-rose-50"
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

      {/* Add Order Modal */}
      <Modal
        isOpen={isAddOpen}
        onClose={() => setIsAddOpen(false)}
        title="Register Microenterprise Order"
        urduTitle="نیا کاروباری آرڈر درج کریں"
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
              <label className="block font-medium text-neutral-700 mb-1">Customer Name</label>
              <input
                type="text"
                required
                placeholder="Farzana Kausar"
                value={formData.customerName}
                onChange={(e) => setFormData({ ...formData, customerName: e.target.value })}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900"
              />
            </div>
            <div>
              <label className="block font-medium text-neutral-700 mb-1">Customer Phone</label>
              <input
                type="text"
                placeholder="03001239841"
                value={formData.phone}
                onChange={(e) => setFormData({ ...formData, phone: e.target.value })}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900 font-mono"
              />
            </div>
          </div>

          <div>
            <label className="block font-medium text-neutral-700 mb-1">Service / Item Description</label>
            <input
              type="text"
              required
              placeholder="3 Ladies Fancy Suits Stitching"
              value={formData.serviceTitle}
              onChange={(e) => setFormData({ ...formData, serviceTitle: e.target.value })}
              className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900"
            />
          </div>

          <div className="grid grid-cols-3 gap-3">
            <div>
              <label className="block font-medium text-neutral-700 mb-1">Total Price (PKR)</label>
              <input
                type="number"
                required
                min={1}
                placeholder="7500"
                value={formData.totalAmount}
                onChange={(e) => setFormData({ ...formData, totalAmount: e.target.value })}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900 font-mono"
              />
            </div>
            <div>
              <label className="block font-medium text-neutral-700 mb-1">Advance Received</label>
              <input
                type="number"
                min={0}
                placeholder="3500"
                value={formData.advancePaid}
                onChange={(e) => setFormData({ ...formData, advancePaid: e.target.value })}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900 font-mono"
              />
            </div>
            <div>
              <label className="block font-medium text-neutral-700 mb-1">Delivery Due Date</label>
              <input
                type="date"
                value={formData.dueDate}
                onChange={(e) => setFormData({ ...formData, dueDate: e.target.value })}
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
              {actionLoading ? 'Saving...' : 'Register Order'}
            </button>
          </div>
        </form>
      </Modal>
    </div>
  );
};
