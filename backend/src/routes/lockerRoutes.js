const express = require('express');
const authMiddleware = require('../middleware/auth');
const Locker = require('../models/Locker');
const { calculateProsperityScore } = require('../services/prosperityCalculator');

const router = express.Router();

// GET /api/emergency-locker
router.get('/', authMiddleware, async (req, res) => {
  try {
    let locker = await Locker.findOne({ userId: req.userId });
    if (!locker) {
      locker = await Locker.create({ userId: req.userId, balance: 0 });
    }
    return res.json({
      balance: locker.balance,
      updatedAt: locker.updatedAt
    });
  } catch (error) {
    return res.status(500).json({ error: 'Error fetching locker balance' });
  }
});

// POST /api/emergency-locker/deposit
router.post('/deposit', authMiddleware, async (req, res) => {
  try {
    const { amount } = req.body;
    const depositAmount = Number(amount);

    if (isNaN(depositAmount) || depositAmount <= 0) {
      return res.status(400).json({ error: 'Deposit amount must be greater than 0' });
    }

    const locker = await Locker.findOneAndUpdate(
      { userId: req.userId },
      { $inc: { balance: depositAmount }, $set: { updatedAt: new Date() } },
      { upsert: true, new: true }
    );

    await calculateProsperityScore(req.userId);

    return res.json({
      balance: locker.balance,
      updatedAt: locker.updatedAt
    });
  } catch (error) {
    return res.status(500).json({ error: 'Error depositing to locker' });
  }
});

// POST /api/emergency-locker/withdraw
router.post('/withdraw', authMiddleware, async (req, res) => {
  try {
    const { amount } = req.body;
    const withdrawAmount = Number(amount);

    if (isNaN(withdrawAmount) || withdrawAmount <= 0) {
      return res.status(400).json({ error: 'Withdrawal amount must be greater than 0' });
    }

    let locker = await Locker.findOne({ userId: req.userId });
    if (!locker) {
      locker = await Locker.create({ userId: req.userId, balance: 0 });
    }

    if (withdrawAmount > locker.balance) {
      return res.status(400).json({ 
        error: 'Insufficient balance / رقم ناکافی ہے',
        balance: locker.balance 
      });
    }

    locker.balance = Math.max(0, locker.balance - withdrawAmount);
    locker.updatedAt = new Date();
    await locker.save();

    await calculateProsperityScore(req.userId);

    return res.json({
      balance: locker.balance,
      updatedAt: locker.updatedAt
    });
  } catch (error) {
    return res.status(500).json({ error: 'Error withdrawing from locker' });
  }
});

module.exports = router;
