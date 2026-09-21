import React, { useState } from 'react';
import { KeyRound, Phone, ArrowRight } from 'lucide-react';
import { useAuth } from '../context/AuthContext';

export const Login: React.FC = () => {
  const { login } = useAuth();
  const [identifier, setIdentifier] = useState<string>('');
  const [password, setPassword] = useState<string>('');
  const [error, setError] = useState<string>('');
  const [loading, setLoading] = useState<boolean>(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      await login(identifier, password);
    } catch (err: any) {
      setError(err.message || 'Invalid administrative credentials');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-white flex flex-col justify-center items-center p-4 selection:bg-neutral-900 selection:text-white">
      <div className="w-full max-w-md space-y-6">
        {/* Branding */}
        <div className="text-center space-y-2">
          <div className="w-12 h-12 rounded-xl bg-neutral-900 text-white flex items-center justify-center font-bold text-2xl mx-auto shadow-sm">
            🌱
          </div>
          <div>
            <h1 className="text-2xl font-bold tracking-tight text-neutral-900">
              Khushhaal Admin Console
            </h1>
            <p className="text-sm text-neutral-500 mt-1">
              صنعتی ورکرز کی مالیاتی فلاح و بہبود کا ایڈمن کنٹرول
            </p>
          </div>
        </div>

        {/* Card Form */}
        <div className="bg-white border border-neutral-200 rounded-2xl p-7 shadow-sm space-y-5">
          <div className="border-b border-neutral-100 pb-3">
            <h2 className="text-sm font-bold text-neutral-900 uppercase tracking-wider">
              Admin Authentication
            </h2>
            <p className="text-xs text-neutral-500 mt-0.5">
              Enter your registered admin mobile number or CNIC
            </p>
          </div>

          {error && (
            <div className="p-3 rounded-lg bg-rose-50 border border-rose-200 text-rose-800 text-xs">
              {error}
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-4 text-xs">
            <div>
              <label className="block font-medium text-neutral-700 mb-1">
                Admin Mobile (+92XXXXXXXXXX) or CNIC
              </label>
              <div className="relative">
                <Phone className="w-4 h-4 text-neutral-400 absolute left-3 top-1/2 -translate-y-1/2" />
                <input
                  type="text"
                  required
                  placeholder="+923XXXXXXXXX"
                  value={identifier}
                  onChange={(e) => setIdentifier(e.target.value)}
                  className="w-full bg-neutral-50 border border-neutral-200 rounded-lg pl-9 pr-3 py-2.5 text-xs text-neutral-900 outline-none focus:border-neutral-900 focus:bg-white font-mono transition-colors"
                />
              </div>
            </div>

            <div>
              <label className="block font-medium text-neutral-700 mb-1">
                Password
              </label>
              <div className="relative">
                <KeyRound className="w-4 h-4 text-neutral-400 absolute left-3 top-1/2 -translate-y-1/2" />
                <input
                  type="password"
                  required
                  placeholder="••••••••••••"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  className="w-full bg-neutral-50 border border-neutral-200 rounded-lg pl-9 pr-3 py-2.5 text-xs text-neutral-900 outline-none focus:border-neutral-900 focus:bg-white font-mono transition-colors"
                />
              </div>
            </div>

            <button
              type="submit"
              disabled={loading || !identifier || !password}
              className="w-full py-2.5 px-4 bg-neutral-900 hover:bg-neutral-800 disabled:bg-neutral-400 text-white rounded-lg text-xs font-semibold shadow-sm transition-colors flex items-center justify-center gap-2"
            >
              <span>{loading ? 'Authenticating...' : 'Sign In'}</span>
              <ArrowRight className="w-4 h-4" />
            </button>
          </form>
        </div>

        <p className="text-center text-[11px] text-neutral-400">
          Khushhaal Worker Financial Wellbeing Platform
        </p>
      </div>
    </div>
  );
};
