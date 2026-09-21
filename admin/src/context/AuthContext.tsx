import React, { createContext, useContext, useState, useEffect } from 'react';
import { api, getStoredAdmin, getStoredToken, removeStoredToken } from '../services/api';

interface AdminUser {
  id: string;
  name: string;
  urduName: string;
  phone: string;
  cnic: string;
  factory: string;
  role: string;
  isActive: boolean;
}

interface AuthContextType {
  admin: AdminUser | null;
  token: string | null;
  isLoading: boolean;
  login: (identifier: string, password: string) => Promise<void>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [admin, setAdmin] = useState<AdminUser | null>(null);
  const [token, setToken] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState<boolean>(true);

  useEffect(() => {
    // Only restore session if we have a real stored token
    const storedToken = getStoredToken();
    const storedAdmin = getStoredAdmin();

    if (storedToken && storedAdmin && storedToken !== 'demo_admin_jwt_token_sample_abc123') {
      setToken(storedToken);
      setAdmin(storedAdmin);
    } else {
      // Clear any leftover demo tokens
      removeStoredToken();
    }
    setIsLoading(false);
  }, []);

  const login = async (identifier: string, password: string) => {
    const res = await api.login(identifier, password);
    setToken(res.accessToken);
    setAdmin(res.user);
  };

  const logout = () => {
    api.logout();
    setToken(null);
    setAdmin(null);
  };

  return (
    <AuthContext.Provider value={{ admin, token, isLoading, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};
