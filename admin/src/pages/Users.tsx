import React, { useState, useEffect } from 'react';
import {
  Search,
  Filter,
  UserPlus,
  MoreVertical,
  Shield,
  Phone,
  CreditCard,
  Building,
  CheckCircle,
  XCircle,
  KeyRound,
  Trash2,
  ExternalLink,
  RefreshCw,
  Eye,
  Sliders,
  Wallet,
  AlertCircle
} from 'lucide-react';
import { WorkerUser } from '../types';
import { api } from '../services/api';
import { Badge } from '../components/Badge';
import { Modal } from '../components/Modal';

export const Users: React.FC = () => {
  const [users, setUsers] = useState<WorkerUser[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [search, setSearch] = useState<string>('');
  const [factoryFilter, setFactoryFilter] = useState<string>('ALL');
  const [statusFilter, setStatusFilter] = useState<string>('ALL');

  // Modals & Drawer state
  const [selectedUser, setSelectedUser] = useState<WorkerUser | null>(null);
  const [dossierData, setDossierData] = useState<any>(null);
  const [loadingDossier, setLoadingDossier] = useState<boolean>(false);
  const [dossierTab, setDossierTab] = useState<'overview' | 'envelopes' | 'debts' | 'bills' | 'kametis' | 'prosperity'>('overview');

  const [isAddOpen, setIsAddOpen] = useState<boolean>(false);
  const [isEditOpen, setIsEditOpen] = useState<boolean>(false);
  const [isResetPassOpen, setIsResetPassOpen] = useState<boolean>(false);
  const [newPassword, setNewPassword] = useState<string>('');
  const [actionLoading, setActionLoading] = useState<boolean>(false);

  // Form states
  const [formData, setFormData] = useState({
    name: '',
    urduName: '',
    phone: '+923',
    cnic: '',
    factory: 'Nishat Mills (Weaving Unit 4)',
    factoryId: '',
    jazzCashNumber: '',
    password: '',
    role: 'user' as 'user' | 'admin',
    preferredLanguage: 'BILINGUAL' as 'BILINGUAL' | 'URDU' | 'ENGLISH'
  });

  const loadUsers = async () => {
    setLoading(true);
    try {
      const res = await api.getUsers({
        search,
        factory: factoryFilter,
        status: statusFilter
      });
      setUsers(res.items);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadUsers();
  }, [factoryFilter, statusFilter]);

  const handleSearchSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    loadUsers();
  };

  // Open 360 Dossier
  const openDossier = async (user: WorkerUser) => {
    setSelectedUser(user);
    setLoadingDossier(true);
    setDossierTab('overview');
    try {
      const dossier = await api.getUserDossier(user.id);
      setDossierData(dossier);
    } catch (err) {
      console.error(err);
    } finally {
      setLoadingDossier(false);
    }
  };

  // Toggle active/suspended status
  const handleToggleStatus = async (user: WorkerUser) => {
    const nextStatus = !user.isActive;
    if (confirm(`Are you sure you want to ${nextStatus ? 'activate' : 'suspend'} ${user.name}?`)) {
      await api.toggleUserStatus(user.id, nextStatus);
      loadUsers();
      if (selectedUser?.id === user.id) {
        setSelectedUser({ ...selectedUser, isActive: nextStatus });
      }
    }
  };

  // Reset Password
  const handleResetPassword = async () => {
    if (!selectedUser || !newPassword) return;
    setActionLoading(true);
    try {
      await api.resetUserPassword(selectedUser.id, newPassword);
      alert(`Password for ${selectedUser.name} successfully updated!`);
      setIsResetPassOpen(false);
      setNewPassword('');
    } catch (err: any) {
      alert(err.message || 'Error updating password');
    } finally {
      setActionLoading(false);
    }
  };

  // Delete worker
  const handleDeleteUser = async (user: WorkerUser) => {
    if (confirm(`CAUTION: Deleting worker ${user.name} will permanently purge all their transactions, bills, debts, kametis, and locker records. Continue?`)) {
      await api.deleteUser(user.id);
      loadUsers();
      if (selectedUser?.id === user.id) {
        setSelectedUser(null);
      }
    }
  };

  // Onboard Worker Form Submit
  const handleCreateUser = async (e: React.FormEvent) => {
    e.preventDefault();
    setActionLoading(true);
    try {
      await api.createUser(formData);
      setIsAddOpen(false);
      setFormData({
        name: '',
        urduName: '',
        phone: '+923',
        cnic: '',
        factory: 'Nishat Mills (Weaving Unit 4)',
        factoryId: '',
        jazzCashNumber: '',
        password: '',
        role: 'user',
        preferredLanguage: 'BILINGUAL'
      });
      loadUsers();
    } catch (err: any) {
      alert(err.message || 'Failed to create worker');
    } finally {
      setActionLoading(false);
    }
  };

  // Edit Worker Form Submit
  const handleUpdateUser = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!selectedUser) return;
    setActionLoading(true);
    try {
      await api.updateUser(selectedUser.id, formData);
      setIsEditOpen(false);
      loadUsers();
    } catch (err: any) {
      alert(err.message || 'Failed to update worker');
    } finally {
      setActionLoading(false);
    }
  };

  return (
    <div className="space-y-6">
      {/* Search and Filters Bar */}
      <div className="bg-white border border-neutral-200 rounded-xl p-4 flex flex-col md:flex-row items-center justify-between gap-4">
        <form onSubmit={handleSearchSubmit} className="flex items-center gap-2 w-full md:w-96">
          <div className="relative flex-1">
            <Search className="w-4 h-4 text-neutral-400 absolute left-3 top-1/2 -translate-y-1/2" />
            <input
              type="text"
              placeholder="Search by name, CNIC, phone, factory ID..."
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              className="w-full bg-neutral-50 border border-neutral-200 text-neutral-900 text-xs rounded-lg pl-9 pr-3 py-2 outline-none focus:border-neutral-900 focus:bg-white transition-colors"
            />
          </div>
          <button
            type="submit"
            className="px-3 py-2 bg-neutral-900 text-white rounded-lg text-xs font-medium hover:bg-neutral-800 transition-colors"
          >
            Search
          </button>
        </form>

        <div className="flex flex-wrap items-center gap-3 w-full md:w-auto justify-end">
          {/* Factory Filter */}
          <div className="flex items-center gap-1.5 text-xs text-neutral-600">
            <Filter className="w-3.5 h-3.5 text-neutral-400" />
            <select
              value={factoryFilter}
              onChange={(e) => setFactoryFilter(e.target.value)}
              className="bg-neutral-50 border border-neutral-200 rounded-lg px-2.5 py-1.5 text-xs text-neutral-900 outline-none focus:border-neutral-900"
            >
              <option value="ALL">All Factories</option>
              <option value="Nishat Mills (Weaving Unit 4)">Nishat Mills (Weaving Unit 4)</option>
              <option value="Crescent Bahuman Denim Plant">Crescent Bahuman Denim Plant</option>
              <option value="Artistic Milliners Garment Div">Artistic Milliners Garment Div</option>
              <option value="Lucky Textile Mills (Karachi)">Lucky Textile Mills (Karachi)</option>
              <option value="Interloop Hosiery Plant 1 (Faisalabad)">Interloop Hosiery Plant 1</option>
              <option value="Sapphire Textile Finishing Unit">Sapphire Textile Finishing Unit</option>
            </select>
          </div>

          {/* Status Filter */}
          <select
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value)}
            className="bg-neutral-50 border border-neutral-200 rounded-lg px-2.5 py-1.5 text-xs text-neutral-900 outline-none focus:border-neutral-900"
          >
            <option value="ALL">All Statuses</option>
            <option value="active">Active Workers</option>
            <option value="suspended">Suspended Workers</option>
          </select>

          {/* Onboard Button */}
          <button
            onClick={() => {
              setFormData({
                name: '',
                urduName: '',
                phone: '+923',
                cnic: '',
                factory: 'Nishat Mills (Weaving Unit 4)',
                factoryId: '',
                jazzCashNumber: '',
                password: '',
                role: 'user',
                preferredLanguage: 'BILINGUAL'
              });
              setIsAddOpen(true);
            }}
            className="flex items-center gap-1.5 px-3.5 py-2 bg-neutral-900 hover:bg-neutral-800 text-white rounded-lg text-xs font-medium shadow-xs transition-colors"
          >
            <UserPlus className="w-3.5 h-3.5" />
            <span>Onboard Worker</span>
          </button>
        </div>
      </div>

      {/* Workers Table */}
      <div className="bg-white border border-neutral-200 rounded-xl overflow-hidden shadow-2xs">
        <div className="overflow-x-auto">
          <table className="w-full text-left text-xs border-collapse">
            <thead>
              <tr className="bg-neutral-50/80 border-b border-neutral-200 text-[11px] font-semibold text-neutral-500 uppercase tracking-wider">
                <th className="py-3 px-4">Worker Profile</th>
                <th className="py-3 px-4">Phone / CNIC</th>
                <th className="py-3 px-4">Factory & ID</th>
                <th className="py-3 px-4">Locker Balance</th>
                <th className="py-3 px-4">Prosperity Index</th>
                <th className="py-3 px-4">Status</th>
                <th className="py-3 px-4 text-right">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-neutral-100">
              {loading ? (
                <tr>
                  <td colSpan={7} className="py-12 text-center text-neutral-400">
                    <RefreshCw className="w-5 h-5 animate-spin mx-auto mb-2" />
                    Loading worker records...
                  </td>
                </tr>
              ) : users.length === 0 ? (
                <tr>
                  <td colSpan={7} className="py-12 text-center text-neutral-500">
                    No workers match your filter criteria.
                  </td>
                </tr>
              ) : (
                users.map((worker) => (
                  <tr key={worker.id} className="hover:bg-neutral-50/60 transition-colors">
                    <td className="py-3.5 px-4">
                      <div className="flex items-center gap-3">
                        <div className="w-8 h-8 rounded-full bg-neutral-100 border border-neutral-200 flex items-center justify-center font-bold text-neutral-700 text-xs">
                          {worker.name.charAt(0)}
                        </div>
                        <div>
                          <p className="font-semibold text-neutral-900">{worker.name}</p>
                          <p className="font-urdu text-[11px] text-neutral-500">{worker.urduName}</p>
                        </div>
                      </div>
                    </td>

                    <td className="py-3.5 px-4 font-mono text-neutral-700">
                      <div>{worker.phone}</div>
                      <div className="text-[11px] text-neutral-400">{worker.cnic}</div>
                    </td>

                    <td className="py-3.5 px-4">
                      <p className="font-medium text-neutral-900 truncate max-w-[200px]">{worker.factory}</p>
                      <p className="text-[11px] text-neutral-400 font-mono">ID: {worker.factoryId || 'N/A'}</p>
                    </td>

                    <td className="py-3.5 px-4 font-mono font-medium text-neutral-900">
                      Rs. {(worker.lockerBalance || 0).toLocaleString()}
                    </td>

                    <td className="py-3.5 px-4">
                      <div className="flex items-center gap-2">
                        <Badge
                          variant={
                            (worker.prosperityScore || 0) >= 70
                              ? 'success'
                              : (worker.prosperityScore || 0) >= 30
                              ? 'warning'
                              : 'danger'
                          }
                        >
                          Score: {worker.prosperityScore || 0}/100
                        </Badge>
                      </div>
                    </td>

                    <td className="py-3.5 px-4">
                      <button
                        onClick={() => handleToggleStatus(worker)}
                        className="cursor-pointer"
                        title="Click to toggle status"
                      >
                        <Badge variant={worker.isActive ? 'success' : 'neutral'}>
                          {worker.isActive ? 'Active' : 'Suspended'}
                        </Badge>
                      </button>
                    </td>

                    <td className="py-3.5 px-4 text-right">
                      <div className="flex items-center justify-end gap-1.5">
                        <button
                          onClick={() => openDossier(worker)}
                          className="flex items-center gap-1 px-2.5 py-1 bg-neutral-900 text-white rounded-md text-xs font-medium hover:bg-neutral-800 transition-colors shadow-2xs"
                        >
                          <Eye className="w-3.5 h-3.5" />
                          <span>360° Dossier</span>
                        </button>

                        <button
                          onClick={() => {
                            setSelectedUser(worker);
                            setFormData({
                              name: worker.name,
                              urduName: worker.urduName,
                              phone: worker.phone,
                              cnic: worker.cnic,
                              factory: worker.factory,
                              factoryId: worker.factoryId,
                              jazzCashNumber: worker.jazzCashNumber,
                              password: '',
                              role: worker.role,
                              preferredLanguage: worker.preferredLanguage
                            });
                            setIsEditOpen(true);
                          }}
                          className="p-1 text-neutral-500 hover:text-neutral-900 rounded hover:bg-neutral-100"
                          title="Edit Profile"
                        >
                          <Sliders className="w-4 h-4" />
                        </button>

                        <button
                          onClick={() => {
                            setSelectedUser(worker);
                            setNewPassword('');
                            setIsResetPassOpen(true);
                          }}
                          className="p-1 text-neutral-500 hover:text-neutral-900 rounded hover:bg-neutral-100"
                          title="Reset Password"
                        >
                          <KeyRound className="w-4 h-4" />
                        </button>

                        <button
                          onClick={() => handleDeleteUser(worker)}
                          className="p-1 text-neutral-400 hover:text-rose-600 rounded hover:bg-rose-50"
                          title="Delete Worker"
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

      {/* ========================================================================= */}
      {/* WORKER 360-DEGREE DOSSIER DRAWER / MODAL */}
      {/* ========================================================================= */}
      {selectedUser && dossierData && (
        <Modal
          isOpen={!!selectedUser && !!dossierData}
          onClose={() => {
            setSelectedUser(null);
            setDossierData(null);
          }}
          title={`Worker Dossier: ${selectedUser.name}`}
          urduTitle={selectedUser.urduName}
          maxWidth="4xl"
        >
          <div className="space-y-6">
            {/* Top Dossier Header Info */}
            <div className="grid grid-cols-2 sm:grid-cols-4 gap-3 p-3.5 rounded-lg bg-neutral-50 border border-neutral-200 text-xs">
              <div>
                <span className="text-neutral-400 block text-[10px] uppercase font-semibold">Factory / Unit</span>
                <span className="font-semibold text-neutral-900">{selectedUser.factory}</span>
              </div>
              <div>
                <span className="text-neutral-400 block text-[10px] uppercase font-semibold">Pakistani CNIC</span>
                <span className="font-mono font-medium text-neutral-800">{selectedUser.cnic}</span>
              </div>
              <div>
                <span className="text-neutral-400 block text-[10px] uppercase font-semibold">Mobile Account</span>
                <span className="font-mono font-medium text-neutral-800">{selectedUser.phone}</span>
              </div>
              <div>
                <span className="text-neutral-400 block text-[10px] uppercase font-semibold">Locker Balance</span>
                <span className="font-bold text-emerald-800">Rs. {(selectedUser.lockerBalance || 0).toLocaleString()}</span>
              </div>
            </div>

            {/* Dossier Tabs */}
            <div className="flex border-b border-neutral-200 gap-2 overflow-x-auto text-xs">
              {[
                { id: 'overview', label: 'Overview' },
                { id: 'envelopes', label: '4 Budget Envelopes' },
                { id: 'debts', label: `Debts (${dossierData.debts?.length || 0})` },
                { id: 'bills', label: `Bills (${dossierData.bills?.length || 0})` },
                { id: 'kametis', label: `Kametis (${dossierData.kametis?.length || 0})` },
                { id: 'prosperity', label: '6 Prosperity Pillars' }
              ].map((tab) => (
                <button
                  key={tab.id}
                  onClick={() => setDossierTab(tab.id as any)}
                  className={`pb-2.5 px-3 font-medium transition-colors border-b-2 -mb-px whitespace-nowrap ${
                    dossierTab === tab.id
                      ? 'border-neutral-900 text-neutral-900'
                      : 'border-transparent text-neutral-500 hover:text-neutral-900'
                  }`}
                >
                  {tab.label}
                </button>
              ))}
            </div>

            {/* Tab Contents */}
            {dossierTab === 'overview' && (
              <div className="space-y-4 text-xs">
                <div className="grid grid-cols-2 gap-4">
                  <div className="p-4 rounded-lg border border-neutral-200 space-y-2">
                    <h4 className="font-bold text-neutral-900 uppercase tracking-wider text-[11px]">Personal Profile</h4>
                    <p className="text-neutral-600"><strong className="text-neutral-900">Name:</strong> {selectedUser.name}</p>
                    <p className="text-neutral-600"><strong className="text-neutral-900">Urdu Name:</strong> {selectedUser.urduName}</p>
                    <p className="text-neutral-600"><strong className="text-neutral-900">CNIC:</strong> {selectedUser.cnic}</p>
                    <p className="text-neutral-600"><strong className="text-neutral-900">Phone:</strong> {selectedUser.phone}</p>
                    <p className="text-neutral-600"><strong className="text-neutral-900">JazzCash:</strong> {selectedUser.jazzCashNumber || 'None'}</p>
                    <p className="text-neutral-600"><strong className="text-neutral-900">Language:</strong> {selectedUser.preferredLanguage}</p>
                  </div>
                  <div className="p-4 rounded-lg border border-neutral-200 space-y-2">
                    <h4 className="font-bold text-neutral-900 uppercase tracking-wider text-[11px]">Factory Affiliation</h4>
                    <p className="text-neutral-600"><strong className="text-neutral-900">Organization:</strong> {selectedUser.factory}</p>
                    <p className="text-neutral-600"><strong className="text-neutral-900">Factory Employee ID:</strong> {selectedUser.factoryId || 'N/A'}</p>
                    <p className="text-neutral-600"><strong className="text-neutral-900">Status:</strong> {selectedUser.isActive ? 'Active' : 'Suspended'}</p>
                    <p className="text-neutral-600"><strong className="text-neutral-900">Account Role:</strong> {selectedUser.role}</p>
                    <p className="text-neutral-600"><strong className="text-neutral-900">Enrolled On:</strong> {new Date(selectedUser.createdAt).toLocaleDateString()}</p>
                  </div>
                </div>

                <div className="p-4 rounded-lg border border-neutral-200">
                  <h4 className="font-bold text-neutral-900 uppercase tracking-wider text-[11px] mb-2">Recent Cashflow</h4>
                  <div className="divide-y divide-neutral-100">
                    {dossierData.cashFlows?.map((cf: any, i: number) => (
                      <div key={i} className="py-2 flex justify-between items-center">
                        <span className="font-mono font-medium">{cf.month}</span>
                        <div className="space-x-4">
                          <span className="text-emerald-700">Income: Rs. {cf.income.toLocaleString()}</span>
                          <span className="text-rose-700">Expenses: Rs. {cf.expenses.toLocaleString()}</span>
                          <span className="text-neutral-800 font-bold">Savings: Rs. {cf.savings.toLocaleString()}</span>
                        </div>
                      </div>
                    ))}
                  </div>
                </div>
              </div>
            )}

            {dossierTab === 'envelopes' && (
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 text-xs">
                {dossierData.envelopes?.map((env: any, idx: number) => (
                  <div key={idx} className="p-4 rounded-lg border border-neutral-200 bg-white space-y-2">
                    <div className="flex justify-between items-start">
                      <div>
                        <h4 className="font-bold text-neutral-900">{env.titleEnglish}</h4>
                        <p className="font-urdu text-neutral-500">{env.titleUrdu}</p>
                      </div>
                      <Badge variant="neutral">{env.percentage}% Split</Badge>
                    </div>
                    <p className="text-sm font-bold font-mono text-neutral-900">Rs. {env.amount?.toLocaleString() || 0}</p>
                    <p className="text-[11px] text-neutral-400 font-urdu">{env.tag}</p>
                  </div>
                ))}
              </div>
            )}

            {dossierTab === 'debts' && (
              <div className="space-y-3 text-xs">
                {dossierData.debts?.length === 0 ? (
                  <p className="text-neutral-500 py-6 text-center">No outstanding debts recorded for this worker.</p>
                ) : (
                  dossierData.debts?.map((debt: any) => (
                    <div key={debt.id || debt._id} className="p-3.5 rounded-lg border border-neutral-200 flex justify-between items-center">
                      <div>
                        <p className="font-bold text-neutral-900">{debt.creditorName}</p>
                        <p className="text-[11px] text-neutral-500">{debt.relationOrType} • Monthly Commitment: Rs. {debt.monthlyCommitment?.toLocaleString()}</p>
                      </div>
                      <div className="text-right">
                        <p className="font-mono font-bold text-rose-700">Remaining: Rs. {debt.remainingAmount?.toLocaleString()}</p>
                        <Badge variant={debt.isShariahFriendly ? 'success' : 'danger'} size="sm">
                          {debt.isShariahFriendly ? 'Shariah Friendly' : 'Commercial Interest'}
                        </Badge>
                      </div>
                    </div>
                  ))
                )}
              </div>
            )}

            {dossierTab === 'bills' && (
              <div className="space-y-3 text-xs">
                {dossierData.bills?.length === 0 ? (
                  <p className="text-neutral-500 py-6 text-center">No utility bills recorded for this worker.</p>
                ) : (
                  dossierData.bills?.map((bill: any) => (
                    <div key={bill.id || bill._id} className="p-3.5 rounded-lg border border-neutral-200 flex justify-between items-center">
                      <div>
                        <p className="font-bold text-neutral-900">{bill.companyName}</p>
                        <p className="text-[11px] text-neutral-500">Consumer: {bill.consumerNumber} • Due: {bill.dueDate}</p>
                      </div>
                      <div className="text-right">
                        <p className="font-mono font-bold text-neutral-900">Rs. {bill.amount?.toLocaleString()}</p>
                        <Badge variant={bill.isPaid ? 'success' : 'danger'} size="sm">
                          {bill.isPaid ? 'Paid' : 'Unpaid'}
                        </Badge>
                      </div>
                    </div>
                  ))
                )}
              </div>
            )}

            {dossierTab === 'kametis' && (
              <div className="space-y-3 text-xs">
                {dossierData.kametis?.length === 0 ? (
                  <p className="text-neutral-500 py-6 text-center">No active kametis for this worker.</p>
                ) : (
                  dossierData.kametis?.map((kameti: any) => (
                    <div key={kameti.id || kameti._id} className="p-3.5 rounded-lg border border-neutral-200 flex justify-between items-center">
                      <div>
                        <p className="font-bold text-neutral-900">{kameti.name}</p>
                        <p className="text-[11px] text-neutral-500">Organizer: {kameti.organizer} • Month {kameti.currentMonth} of {kameti.totalMembers}</p>
                      </div>
                      <div className="text-right">
                        <p className="font-mono font-bold text-neutral-900">Rs. {kameti.monthlyAmount?.toLocaleString()} / mo</p>
                        <p className="text-[11px] text-neutral-500">Payout: Rs. {kameti.payoutAmount?.toLocaleString()}</p>
                      </div>
                    </div>
                  ))
                )}
              </div>
            )}

            {dossierTab === 'prosperity' && (
              <div className="space-y-4 text-xs">
                <div className="flex items-center justify-between p-3.5 bg-neutral-50 rounded-lg border border-neutral-200">
                  <div>
                    <h4 className="font-bold text-neutral-900">Overall Prosperity Index</h4>
                    <p className="text-[11px] text-neutral-500">Calculated based on 6 core pillars of worker financial stability</p>
                  </div>
                  <div className="text-2xl font-bold text-neutral-900">
                    {dossierData.prosperity?.score || 0} / 100
                  </div>
                </div>

                <div className="space-y-2.5">
                  {dossierData.prosperity?.pillars?.map((pillar: any, index: number) => (
                    <div key={index} className="p-3 rounded-lg border border-neutral-200 flex justify-between items-center">
                      <div>
                        <p className="font-semibold text-neutral-900">{pillar.titleEnglish}</p>
                        <p className="text-[11px] text-neutral-500 font-urdu">{pillar.titleUrdu}</p>
                      </div>
                      <div className="text-right">
                        <span className="font-mono font-bold text-neutral-900">{pillar.currentScore} / {pillar.maxScore}</span>
                        <div className="mt-0.5">
                          <Badge 
                            variant={pillar.statusColorType === 'SUCCESS' ? 'success' : pillar.statusColorType === 'WARNING' ? 'warning' : 'danger'}
                            size="sm"
                          >
                            {pillar.statusText}
                          </Badge>
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            )}
          </div>
        </Modal>
      )}

      {/* ========================================================================= */}
      {/* ONBOARD WORKER MODAL */}
      {/* ========================================================================= */}
      <Modal
        isOpen={isAddOpen}
        onClose={() => setIsAddOpen(false)}
        title="Onboard New Factory Worker"
        urduTitle="نیا ورکر شامل کریں"
        maxWidth="lg"
      >
        <form onSubmit={handleCreateUser} className="space-y-4 text-xs">
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
            <div>
              <label className="block font-medium text-neutral-700 mb-1">Full Name (English) *</label>
              <input
                type="text"
                required
                placeholder="Muhammad Tariq"
                value={formData.name}
                onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900"
              />
            </div>
            <div>
              <label className="block font-medium text-neutral-700 mb-1">Urdu Name (اردو نام)</label>
              <input
                type="text"
                placeholder="محمد طارق"
                value={formData.urduName}
                onChange={(e) => setFormData({ ...formData, urduName: e.target.value })}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900 font-urdu"
              />
            </div>
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
            <div>
              <label className="block font-medium text-neutral-700 mb-1">Pakistani Mobile (+92XXXXXXXXXX) *</label>
              <input
                type="text"
                required
                placeholder="+923014589211"
                value={formData.phone}
                onChange={(e) => setFormData({ ...formData, phone: e.target.value })}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900 font-mono"
              />
            </div>
            <div>
              <label className="block font-medium text-neutral-700 mb-1">CNIC (DDDDD-DDDDDDD-D) *</label>
              <input
                type="text"
                required
                placeholder="35201-1284950-3"
                value={formData.cnic}
                onChange={(e) => setFormData({ ...formData, cnic: e.target.value })}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900 font-mono"
              />
            </div>
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
            <div>
              <label className="block font-medium text-neutral-700 mb-1">Factory / Organization *</label>
              <select
                value={formData.factory}
                onChange={(e) => setFormData({ ...formData, factory: e.target.value })}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900"
              >
                <option value="Nishat Mills (Weaving Unit 4)">Nishat Mills (Weaving Unit 4)</option>
                <option value="Crescent Bahuman Denim Plant">Crescent Bahuman Denim Plant</option>
                <option value="Artistic Milliners Garment Div">Artistic Milliners Garment Div</option>
                <option value="Lucky Textile Mills (Karachi)">Lucky Textile Mills (Karachi)</option>
                <option value="Interloop Hosiery Plant 1 (Faisalabad)">Interloop Hosiery Plant 1</option>
                <option value="Sapphire Textile Finishing Unit">Sapphire Textile Finishing Unit</option>
              </select>
            </div>
            <div>
              <label className="block font-medium text-neutral-700 mb-1">Factory Worker ID</label>
              <input
                type="text"
                placeholder="NM-W4-049"
                value={formData.factoryId}
                onChange={(e) => setFormData({ ...formData, factoryId: e.target.value })}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900"
              />
            </div>
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
            <div>
              <label className="block font-medium text-neutral-700 mb-1">Initial Password *</label>
              <input
                type="password"
                required
                minLength={8}
                placeholder="Min 8 characters"
                value={formData.password}
                onChange={(e) => setFormData({ ...formData, password: e.target.value })}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900 font-mono"
              />
            </div>
            <div>
              <label className="block font-medium text-neutral-700 mb-1">JazzCash Account</label>
              <input
                type="text"
                placeholder="03014589211"
                value={formData.jazzCashNumber}
                onChange={(e) => setFormData({ ...formData, jazzCashNumber: e.target.value })}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900 font-mono"
              />
            </div>
          </div>

          <div className="pt-4 border-t border-neutral-100 flex justify-end gap-2">
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
              className="px-4 py-2 bg-neutral-900 text-white rounded-lg text-xs font-medium hover:bg-neutral-800 transition-colors shadow-2xs"
            >
              {actionLoading ? 'Onboarding...' : 'Complete Onboarding'}
            </button>
          </div>
        </form>
      </Modal>

      {/* ========================================================================= */}
      {/* EDIT WORKER MODAL */}
      {/* ========================================================================= */}
      {selectedUser && (
        <Modal
          isOpen={isEditOpen}
          onClose={() => setIsEditOpen(false)}
          title={`Edit Worker Profile: ${selectedUser.name}`}
          maxWidth="lg"
        >
          <form onSubmit={handleUpdateUser} className="space-y-4 text-xs">
            <div className="grid grid-cols-2 gap-3">
              <div>
                <label className="block font-medium text-neutral-700 mb-1">Full Name</label>
                <input
                  type="text"
                  required
                  value={formData.name}
                  onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                  className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900"
                />
              </div>
              <div>
                <label className="block font-medium text-neutral-700 mb-1">Urdu Name</label>
                <input
                  type="text"
                  value={formData.urduName}
                  onChange={(e) => setFormData({ ...formData, urduName: e.target.value })}
                  className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900 font-urdu"
                />
              </div>
            </div>

            <div className="grid grid-cols-2 gap-3">
              <div>
                <label className="block font-medium text-neutral-700 mb-1">Mobile Phone</label>
                <input
                  type="text"
                  required
                  value={formData.phone}
                  onChange={(e) => setFormData({ ...formData, phone: e.target.value })}
                  className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900 font-mono"
                />
              </div>
              <div>
                <label className="block font-medium text-neutral-700 mb-1">CNIC</label>
                <input
                  type="text"
                  required
                  value={formData.cnic}
                  onChange={(e) => setFormData({ ...formData, cnic: e.target.value })}
                  className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900 font-mono"
                />
              </div>
            </div>

            <div className="grid grid-cols-2 gap-3">
              <div>
                <label className="block font-medium text-neutral-700 mb-1">Factory</label>
                <input
                  type="text"
                  required
                  value={formData.factory}
                  onChange={(e) => setFormData({ ...formData, factory: e.target.value })}
                  className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900"
                />
              </div>
              <div>
                <label className="block font-medium text-neutral-700 mb-1">Factory ID</label>
                <input
                  type="text"
                  value={formData.factoryId}
                  onChange={(e) => setFormData({ ...formData, factoryId: e.target.value })}
                  className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900"
                />
              </div>
            </div>

            <div className="pt-4 border-t border-neutral-100 flex justify-end gap-2">
              <button
                type="button"
                onClick={() => setIsEditOpen(false)}
                className="px-4 py-2 border border-neutral-200 rounded-lg text-xs font-medium text-neutral-700 hover:bg-neutral-50"
              >
                Cancel
              </button>
              <button
                type="submit"
                disabled={actionLoading}
                className="px-4 py-2 bg-neutral-900 text-white rounded-lg text-xs font-medium hover:bg-neutral-800 transition-colors"
              >
                {actionLoading ? 'Saving...' : 'Save Changes'}
              </button>
            </div>
          </form>
        </Modal>
      )}

      {/* ========================================================================= */}
      {/* RESET PASSWORD MODAL */}
      {/* ========================================================================= */}
      {selectedUser && (
        <Modal
          isOpen={isResetPassOpen}
          onClose={() => setIsResetPassOpen(false)}
          title={`Reset Password for ${selectedUser.name}`}
          maxWidth="sm"
        >
          <div className="space-y-4 text-xs">
            <p className="text-neutral-500">
              Provide a new temporary password for worker <strong className="text-neutral-900">{selectedUser.phone}</strong>.
            </p>
            <div>
              <label className="block font-medium text-neutral-700 mb-1">New Password</label>
              <input
                type="password"
                placeholder="Min 6 characters"
                value={newPassword}
                onChange={(e) => setNewPassword(e.target.value)}
                className="w-full bg-neutral-50 border border-neutral-200 rounded-lg px-3 py-2 text-xs text-neutral-900 outline-none focus:border-neutral-900 font-mono"
              />
            </div>
            <div className="flex justify-end gap-2 pt-2 border-t border-neutral-100">
              <button
                onClick={() => setIsResetPassOpen(false)}
                className="px-3 py-1.5 border border-neutral-200 rounded-lg text-xs font-medium text-neutral-700 hover:bg-neutral-50"
              >
                Cancel
              </button>
              <button
                onClick={handleResetPassword}
                disabled={actionLoading || newPassword.length < 6}
                className="px-3.5 py-1.5 bg-neutral-900 text-white rounded-lg text-xs font-medium hover:bg-neutral-800 transition-colors disabled:opacity-50"
              >
                {actionLoading ? 'Updating...' : 'Set New Password'}
              </button>
            </div>
          </div>
        </Modal>
      )}
    </div>
  );
};
