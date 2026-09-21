const express = require('express');
const cors = require('cors');
const helmet = require('helmet');
const mongoose = require('mongoose');
const path = require('path');

// Routes
const authRoutes = require('./routes/authRoutes');
const userRoutes = require('./routes/userRoutes');
const cashflowRoutes = require('./routes/cashflowRoutes');
const transactionRoutes = require('./routes/transactionRoutes');
const envelopeRoutes = require('./routes/envelopeRoutes');
const prosperityRoutes = require('./routes/prosperityRoutes');
const kametiRoutes = require('./routes/kametiRoutes');
const goalRoutes = require('./routes/goalRoutes');
const debtRoutes = require('./routes/debtRoutes');
const billRoutes = require('./routes/billRoutes');
const lockerRoutes = require('./routes/lockerRoutes');
const orderRoutes = require('./routes/orderRoutes');
const notificationRoutes = require('./routes/notificationRoutes');
const coachRoutes = require('./routes/coachRoutes');
const adminRoutes = require('./routes/adminRoutes');
const configRoutes = require('./routes/configRoutes');
const payslipRoutes = require('./routes/payslipRoutes');

const app = express();

// Security and middleware
app.use(helmet({
  contentSecurityPolicy: {
    directives: {
      defaultSrc: ["'self'"],
      scriptSrc: ["'self'", "'unsafe-inline'", "'unsafe-eval'"], // needed for React SPA
      styleSrc: ["'self'", "'unsafe-inline'"],
      imgSrc: ["'self'", "data:", "https:"],
      connectSrc: ["'self'"],
      fontSrc: ["'self'", "https:", "data:"],
      objectSrc: ["'none'"],
      mediaSrc: ["'self'"],
      frameSrc: ["'none'"],
    },
  },
  crossOriginEmbedderPolicy: false,
}));

// CORS — restrict to admin panel domain in production
const corsOptions = {
  origin: process.env.NODE_ENV === 'production'
    ? (process.env.ALLOWED_ORIGINS ? process.env.ALLOWED_ORIGINS.split(',').map(o => o.trim()) : [])
    : true, // allow all in dev
  credentials: true,
};
app.use(cors(corsOptions));
app.use(express.json());

// Serve admin panel static files at /admin
const adminDistPath = path.join(__dirname, '../public/admin');
app.use('/admin', express.static(adminDistPath));
// SPA fallback — any /admin/* route serves index.html
app.get('/admin/*', (req, res) => {
  res.sendFile(path.join(adminDistPath, 'index.html'));
});

// Health check endpoint (Task 1.2)
app.get('/api/health', (req, res) => {
  const isConnected = mongoose.connection.readyState === 1;
  if (isConnected) {
    return res.status(200).json({ status: 'ok', db: 'connected' });
  } else {
    return res.status(503).json({ status: 'degraded', db: 'disconnected' });
  }
});

// One-time admin seed endpoint — env-gated, never runs in production by default
// Set ENABLE_SEED_ENDPOINT=true in .env to enable (development/staging only)
if (process.env.ENABLE_SEED_ENDPOINT === 'true' && process.env.NODE_ENV !== 'production') {
  app.post('/api/internal/seed-admin', async (req, res) => {
    const { secret } = req.body;
    const expectedSecret = process.env.SEED_SECRET;
    if (!expectedSecret || secret !== expectedSecret) {
      return res.status(403).json({ error: 'Forbidden' });
    }
    try {
      const bcrypt = require('bcryptjs');
      const User = require('./models/User');
      const adminPhone = process.env.SEED_ADMIN_PHONE;
      const adminPassword = process.env.SEED_ADMIN_PASSWORD;
      if (!adminPhone || !adminPassword) {
        return res.status(500).json({ error: 'SEED_ADMIN_PHONE and SEED_ADMIN_PASSWORD must be set in .env' });
      }
      const existing = await User.findOne({ phone: adminPhone });
      if (existing) {
        return res.json({ message: 'Admin already exists', phone: adminPhone });
      }
      const passwordHash = await bcrypt.hash(adminPassword, 12);
      const admin = new User({
        name: process.env.SEED_ADMIN_NAME || 'Khushhaal Admin',
        urduName: 'خوشحال ایڈمن',
        phone: adminPhone,
        cnic: process.env.SEED_ADMIN_CNIC || '35201-0000001-1',
        passwordHash,
        factory: 'Khushhaal Head Office',
        factoryId: 'KHQ-ADMIN-001',
        role: 'admin',
        isActive: true
      });
      await admin.save();
      // NEVER return password in response
      return res.status(201).json({ message: 'Admin created successfully', phone: adminPhone });
    } catch (error) {
      return res.status(500).json({ error: error.message });
    }
  });
}

// Mount routes
app.use('/api/auth', authRoutes);
app.use('/api/users', userRoutes);
app.use('/api/cashflow', cashflowRoutes);
app.use('/api/transactions', transactionRoutes);
app.use('/api/envelopes', envelopeRoutes);
app.use('/api/prosperity', prosperityRoutes);
app.use('/api/kametis', kametiRoutes);
app.use('/api/goals', goalRoutes);
app.use('/api/debts', debtRoutes);
app.use('/api/bills', billRoutes);
app.use('/api/emergency-locker', lockerRoutes);
app.use('/api/orders', orderRoutes);
app.use('/api/notifications', notificationRoutes);
app.use('/api/coach', coachRoutes);
app.use('/api/admin', adminRoutes);
app.use('/api/config', configRoutes);
app.use('/api/payslip', payslipRoutes);

// 404 handler
app.use((req, res) => {
  res.status(404).json({ error: 'Endpoint not found / راستہ نہیں ملا' });
});

// Global error handler
app.use((err, req, res, next) => {
  console.error('Unhandled error:', err);
  res.status(err.status || 500).json({
    error: err.message || 'Internal Server Error',
    message: 'سرور میں غیر متوقع خرابی پیش آئی ہے'
  });
});

module.exports = app;
