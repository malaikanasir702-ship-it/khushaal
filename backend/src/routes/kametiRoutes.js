const express = require('express');
const authMiddleware = require('../middleware/auth');
const Kameti = require('../models/Kameti');
const { calculateProsperityScore } = require('../services/prosperityCalculator');

const router = express.Router();

// GET /api/kametis
router.get('/', authMiddleware, async (req, res) => {
  try {
    const kametis = await Kameti.find({ userId: req.userId }).sort({ createdAt: -1 });
    const formatted = kametis.map(k => ({
      id: k._id.toString(),
      name: k.name,
      urduName: k.urduName || k.name,
      monthlyAmount: k.monthlyAmount,
      totalMembers: k.totalMembers,
      myTurnMonth: k.myTurnMonth,
      currentMonth: k.currentMonth,
      payoutAmount: k.payoutAmount || (k.monthlyAmount * k.totalMembers),
      organizer: k.organizer || '',
      isPaidThisMonth: k.isPaidThisMonth
    }));
    return res.json(formatted);
  } catch (error) {
    return res.status(500).json({ error: 'Error fetching kametis' });
  }
});

// POST /api/kametis
router.post('/', authMiddleware, async (req, res) => {
  try {
    const { name, urduName, monthlyAmount, totalMembers, myTurnMonth, currentMonth, organizer } = req.body;

    if (!name || !name.trim()) {
      return res.status(400).json({ error: 'Kameti name is required' });
    }

    const amt = Number(monthlyAmount);
    if (isNaN(amt) || amt < 1) {
      return res.status(400).json({ error: 'Monthly amount must be at least 1' });
    }

    const members = Number(totalMembers);
    if (isNaN(members) || members < 2) {
      return res.status(400).json({ error: 'Total members must be at least 2' });
    }

    const kameti = new Kameti({
      userId: req.userId,
      name: name.trim(),
      urduName: urduName ? urduName.trim() : name.trim(),
      monthlyAmount: amt,
      totalMembers: members,
      myTurnMonth: Number(myTurnMonth) || 1,
      currentMonth: Number(currentMonth) || 1,
      payoutAmount: amt * members,
      organizer: organizer ? organizer.trim() : '',
      isPaidThisMonth: false
    });

    await kameti.save();
    await calculateProsperityScore(req.userId);

    return res.status(201).json({
      id: kameti._id.toString(),
      name: kameti.name,
      urduName: kameti.urduName,
      monthlyAmount: kameti.monthlyAmount,
      totalMembers: kameti.totalMembers,
      myTurnMonth: kameti.myTurnMonth,
      currentMonth: kameti.currentMonth,
      payoutAmount: kameti.payoutAmount,
      organizer: kameti.organizer,
      isPaidThisMonth: kameti.isPaidThisMonth
    });
  } catch (error) {
    return res.status(500).json({ error: 'Error creating kameti' });
  }
});

// PUT /api/kametis/:id/pay
router.put('/:id/pay', authMiddleware, async (req, res) => {
  try {
    const kameti = await Kameti.findOne({ _id: req.params.id, userId: req.userId });
    if (!kameti) {
      return res.status(404).json({ error: 'Kameti not found' });
    }

    kameti.isPaidThisMonth = true;
    await kameti.save();
    await calculateProsperityScore(req.userId);

    return res.json({
      id: kameti._id.toString(),
      name: kameti.name,
      urduName: kameti.urduName,
      monthlyAmount: kameti.monthlyAmount,
      totalMembers: kameti.totalMembers,
      myTurnMonth: kameti.myTurnMonth,
      currentMonth: kameti.currentMonth,
      payoutAmount: kameti.payoutAmount,
      organizer: kameti.organizer,
      isPaidThisMonth: kameti.isPaidThisMonth
    });
  } catch (error) {
    return res.status(500).json({ error: 'Error marking kameti paid' });
  }
});

module.exports = router;
