import {
  WorkerUser,
  Transaction,
  Bill,
  Debt,
  Kameti,
  Goal,
  Order,
  EmergencyLocker,
  ProsperityScore,
  NotificationItem,
  CoachLog,
  DashboardStats,
  SystemHealth
} from '../types';

const BASE_URL = '/api/admin';

const TOKEN_KEY = 'khushhaal_admin_token';
const USER_KEY = 'khushhaal_admin_user';

export const getStoredToken = () => localStorage.getItem(TOKEN_KEY);
export const setStoredToken = (token: string) => localStorage.setItem(TOKEN_KEY, token);
export const removeStoredToken = () => {
  localStorage.removeItem(TOKEN_KEY);
  localStorage.removeItem(USER_KEY);
};

export const getStoredAdmin = () => {
  const u = localStorage.getItem(USER_KEY);
  return u ? JSON.parse(u) : null;
};
export const setStoredAdmin = (admin: any) => localStorage.setItem(USER_KEY, JSON.stringify(admin));

// Generic fetch wrapper
async function request<T>(endpoint: string, options: RequestInit = {}): Promise<T> {
  const token = getStoredToken();
  const headers: Record<string, string> = {
    'Content-Type': 'application/json',
    ...(options.headers as Record<string, string> || {}),
  };

  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }

  const res = await fetch(`${BASE_URL}${endpoint}`, {
    ...options,
    headers
  });

  if (res.status === 401) {
    removeStoredToken();
    window.location.reload();
    throw new Error('Session expired. Please log in again.');
  }

  if (!res.ok) {
    const err = await res.json().catch(() => ({ error: res.statusText }));
    throw new Error(err.error || err.message || `Request failed with status ${res.status}`);
  }

  return await res.json();
}

export const api = {
  // Auth
  async login(identifier: string, password: string): Promise<{ accessToken: string; user: any }> {
    const data = await request<{ accessToken: string; user: any }>('/auth/login', {
      method: 'POST',
      body: JSON.stringify({ identifier, password })
    });
    setStoredToken(data.accessToken);
    setStoredAdmin(data.user);
    return data;
  },

  logout() {
    removeStoredToken();
  },

  // Dashboard
  async getDashboardStats(): Promise<DashboardStats> {
    return await request<DashboardStats>('/dashboard/stats');
  },

  // Users
  async getUsers(params: { search?: string; factory?: string; status?: string; page?: number; limit?: number } = {}) {
    const q = new URLSearchParams();
    if (params.search) q.append('search', params.search);
    if (params.factory) q.append('factory', params.factory);
    if (params.status) q.append('status', params.status);
    if (params.page) q.append('page', params.page.toString());
    if (params.limit) q.append('limit', params.limit.toString());
    return await request<{ items: WorkerUser[]; total: number; page: number; pages: number }>(`/users?${q.toString()}`);
  },

  async createUser(data: Partial<WorkerUser> & { password: string }) {
    return await request<WorkerUser>('/users', {
      method: 'POST',
      body: JSON.stringify(data)
    });
  },

  async getUserDossier(id: string) {
    return await request<any>(`/users/${id}`);
  },

  async updateUser(id: string, data: Partial<WorkerUser>) {
    return await request<{ message: string; user: any }>(`/users/${id}`, {
      method: 'PUT',
      body: JSON.stringify(data)
    });
  },

  async toggleUserStatus(id: string, isActive: boolean) {
    return await request<{ message: string; isActive: boolean }>(`/users/${id}/status`, {
      method: 'PUT',
      body: JSON.stringify({ isActive })
    });
  },

  async resetUserPassword(id: string, newPassword: string) {
    return await request<{ message: string }>(`/users/${id}/reset-password`, {
      method: 'PUT',
      body: JSON.stringify({ newPassword })
    });
  },

  async deleteUser(id: string) {
    return await request<{ message: string }>(`/users/${id}`, { method: 'DELETE' });
  },

  // Transactions
  async getTransactions(params: { userId?: string; category?: string; isExpense?: string; search?: string; page?: number } = {}) {
    const q = new URLSearchParams();
    if (params.userId) q.append('userId', params.userId);
    if (params.category) q.append('category', params.category);
    if (params.isExpense !== undefined) q.append('isExpense', params.isExpense);
    if (params.search) q.append('search', params.search);
    if (params.page) q.append('page', params.page.toString());
    return await request<{ items: Transaction[]; total: number; page: number; pages: number }>(`/transactions?${q.toString()}`);
  },

  async createTransaction(data: any) {
    return await request<Transaction>('/transactions', { method: 'POST', body: JSON.stringify(data) });
  },

  async deleteTransaction(id: string) {
    return await request<any>(`/transactions/${id}`, { method: 'DELETE' });
  },

  // Bills
  async getBills(params: { userId?: string; isPaid?: string; billType?: string } = {}) {
    const q = new URLSearchParams();
    if (params.userId) q.append('userId', params.userId);
    if (params.isPaid !== undefined) q.append('isPaid', params.isPaid);
    if (params.billType) q.append('billType', params.billType);
    return await request<{ items: Bill[]; total: number }>(`/bills?${q.toString()}`);
  },

  async createBill(data: any) {
    return await request<Bill>('/bills', { method: 'POST', body: JSON.stringify(data) });
  },

  async updateBill(id: string, data: any) {
    return await request<Bill>(`/bills/${id}`, { method: 'PUT', body: JSON.stringify(data) });
  },

  async deleteBill(id: string) {
    return await request<any>(`/bills/${id}`, { method: 'DELETE' });
  },

  // Debts
  async getDebts(params: { urgencyLevel?: string; isShariahFriendly?: string } = {}) {
    const q = new URLSearchParams();
    if (params.urgencyLevel) q.append('urgencyLevel', params.urgencyLevel);
    if (params.isShariahFriendly !== undefined) q.append('isShariahFriendly', params.isShariahFriendly);
    return await request<{ items: Debt[]; total: number }>(`/debts?${q.toString()}`);
  },

  async createDebt(data: any) {
    return await request<Debt>('/debts', { method: 'POST', body: JSON.stringify(data) });
  },

  async updateDebt(id: string, data: any) {
    return await request<Debt>(`/debts/${id}`, { method: 'PUT', body: JSON.stringify(data) });
  },

  async deleteDebt(id: string) {
    return await request<any>(`/debts/${id}`, { method: 'DELETE' });
  },

  // Kametis
  async getKametis() {
    return await request<{ items: Kameti[]; total: number }>('/kametis');
  },

  async createKameti(data: any) {
    return await request<Kameti>('/kametis', { method: 'POST', body: JSON.stringify(data) });
  },

  async updateKameti(id: string, data: any) {
    return await request<Kameti>(`/kametis/${id}`, { method: 'PUT', body: JSON.stringify(data) });
  },

  async deleteKameti(id: string) {
    return await request<any>(`/kametis/${id}`, { method: 'DELETE' });
  },

  // Lockers
  async getLockers(): Promise<EmergencyLocker[]> {
    return await request<EmergencyLocker[]>('/lockers');
  },

  async updateLocker(userId: string, data: { balance?: number; adjustmentAmount?: number }) {
    return await request<any>(`/lockers/${userId}`, { method: 'PUT', body: JSON.stringify(data) });
  },

  // Goals
  async getGoals(): Promise<Goal[]> {
    return await request<Goal[]>('/goals');
  },

  async createGoal(data: any) {
    return await request<Goal>('/goals', { method: 'POST', body: JSON.stringify(data) });
  },

  async updateGoal(id: string, data: any) {
    return await request<Goal>(`/goals/${id}`, { method: 'PUT', body: JSON.stringify(data) });
  },

  async deleteGoal(id: string) {
    return await request<any>(`/goals/${id}`, { method: 'DELETE' });
  },

  // Orders
  async getOrders(): Promise<Order[]> {
    return await request<Order[]>('/orders');
  },

  async createOrder(data: any) {
    return await request<Order>('/orders', { method: 'POST', body: JSON.stringify(data) });
  },

  async updateOrder(id: string, data: any) {
    return await request<Order>(`/orders/${id}`, { method: 'PUT', body: JSON.stringify(data) });
  },

  async deleteOrder(id: string) {
    return await request<any>(`/orders/${id}`, { method: 'DELETE' });
  },

  // Prosperity
  async getProsperity(): Promise<ProsperityScore[]> {
    return await request<ProsperityScore[]>('/prosperity');
  },

  async recalculateProsperity(userId: string) {
    return await request<any>(`/prosperity/recalculate/${userId}`, { method: 'POST' });
  },

  // Notifications
  async getNotifications(): Promise<NotificationItem[]> {
    return await request<NotificationItem[]>('/notifications');
  },

  async broadcastNotification(data: {
    titleEnglish: string;
    titleUrdu: string;
    descriptionEnglish: string;
    descriptionUrdu: string;
    category: string;
    targetFactory?: string;
  }) {
    return await request<any>('/notifications/broadcast', { method: 'POST', body: JSON.stringify(data) });
  },

  // Coach Logs
  async getCoachLogs(): Promise<CoachLog[]> {
    return await request<CoachLog[]>('/coach/logs');
  },

  // System Health
  async getSystemHealth(): Promise<SystemHealth> {
    return await request<SystemHealth>('/system/health');
  }
};
