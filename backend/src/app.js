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

const app = express();

// Security and middleware
app.use(helmet({
  contentSecurityPolicy: false, // Allow admin panel assets
}));
app.use(cors());
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
