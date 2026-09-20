const express = require('express');
const authMiddleware = require('../middleware/auth');
const Bill = require('../models/Bill');

const router = express.Router();

// GET /api/bills
router.get('/', authMiddleware, async (req, res) => {
  try {
    const bills = await Bill.find({ userId: req.userId }).sort({ createdAt: -1 });
    const formatted = bills.map(b => ({
      id: b._id.toString(),
      companyName: b.companyName,
      companyUrdu: b.companyUrdu || b.companyName,
      consumerNumber: b.consumerNumber || '',
      billType: b.billType || 'Electricity',
      month: b.month || '',
      dueDate: b.dueDate || '',
      amount: b.amount,
      unitsConsumed: b.unitsConsumed,
      isPaid: b.isPaid,
      paidDate: b.paidDate || null,
      alertTip: b.alertTip || ''
    }));
    return res.json(formatted);
  } catch (error) {
    return res.status(500).json({ error: 'Error fetching bills' });
  }
});

// POST /api/bills
router.post('/', authMiddleware, async (req, res) => {
  try {
    const { 
      companyName, 
      companyUrdu, 
      consumerNumber, 
      billType, 
      month, 
      dueDate, 
      amount, 
      unitsConsumed, 
      alertTip 
    } = req.body;

    if (!companyName || !companyName.trim()) {
      return res.status(400).json({ error: 'Company name is required' });
    }

    const amt = Number(amount);
    if (isNaN(amt) || amt < 1) {
      return res.status(400).json({ error: 'Amount must be at least 1' });
    }

    const bill = new Bill({
      userId: req.userId,
      companyName: companyName.trim(),
      companyUrdu: companyUrdu ? companyUrdu.trim() : companyName.trim(),
      consumerNumber: consumerNumber ? consumerNumber.trim() : '',
      billType: billType || 'Electricity',
      month: month ? month.trim() : '',
      dueDate: dueDate ? dueDate.trim() : '',
      amount: amt,
      unitsConsumed: Number(unitsConsumed) || 0,
      isPaid: false,
      alertTip: alertTip ? alertTip.trim() : ''
    });

    await bill.save();

    return res.status(201).json({
      id: bill._id.toString(),
      companyName: bill.companyName,
      companyUrdu: bill.companyUrdu,
      consumerNumber: bill.consumerNumber,
      billType: bill.billType,
      month: bill.month,
      dueDate: bill.dueDate,
      amount: bill.amount,
      unitsConsumed: bill.unitsConsumed,
      isPaid: bill.isPaid,
      paidDate: bill.paidDate,
      alertTip: bill.alertTip
    });
  } catch (error) {
    return res.status(500).json({ error: 'Error creating bill' });
  }
});

// PUT /api/bills/:id/pay
router.put('/:id/pay', authMiddleware, async (req, res) => {
  try {
    const bill = await Bill.findOne({ _id: req.params.id, userId: req.userId });
    if (!bill) {
      return res.status(404).json({ error: 'Bill not found' });
    }

    bill.isPaid = true;
    bill.paidDate = new Date().toISOString();
    await bill.save();

    return res.json({
      id: bill._id.toString(),
      companyName: bill.companyName,
      companyUrdu: bill.companyUrdu,
      consumerNumber: bill.consumerNumber,
      billType: bill.billType,
      month: bill.month,
      dueDate: bill.dueDate,
      amount: bill.amount,
      unitsConsumed: bill.unitsConsumed,
      isPaid: bill.isPaid,
      paidDate: bill.paidDate,
      alertTip: bill.alertTip
    });
  } catch (error) {
    return res.status(500).json({ error: 'Error marking bill paid' });
  }
});

module.exports = router;
