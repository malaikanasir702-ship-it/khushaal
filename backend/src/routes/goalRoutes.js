const express = require('express');
const authMiddleware = require('../middleware/auth');
const Goal = require('../models/Goal');
const { calculateProsperityScore } = require('../services/prosperityCalculator');

const router = express.Router();

// GET /api/goals
router.get('/', authMiddleware, async (req, res) => {
  try {
    const goals = await Goal.find({ userId: req.userId }).sort({ createdAt: -1 });
    const formatted = goals.map(g => ({
      id: g._id.toString(),
      title: g.title,
      urduTitle: g.urduTitle || g.title,
      targetAmount: g.targetAmount,
      currentAmount: g.currentAmount,
      targetDate: g.targetDate || '',
      emoji: g.emoji || '🎯',
      isCompleted: g.isCompleted
    }));
    return res.json(formatted);
  } catch (error) {
    return res.status(500).json({ error: 'Error fetching goals' });
  }
});

// POST /api/goals
router.post('/', authMiddleware, async (req, res) => {
  try {
    const { title, urduTitle, targetAmount, currentAmount, targetDate, emoji } = req.body;

    if (!title || !title.trim()) {
      return res.status(400).json({ error: 'Goal title is required' });
    }

    const target = Number(targetAmount);
    if (isNaN(target) || target < 1) {
      return res.status(400).json({ error: 'Target amount must be at least 1' });
    }

    const current = Math.max(0, Number(currentAmount) || 0);

    const goal = new Goal({
      userId: req.userId,
      title: title.trim(),
      urduTitle: urduTitle ? urduTitle.trim() : title.trim(),
      targetAmount: target,
      currentAmount: current,
      targetDate: targetDate ? targetDate.trim() : '',
      emoji: emoji || '🎯',
      isCompleted: current >= target
    });

    await goal.save();
    await calculateProsperityScore(req.userId);

    return res.status(201).json({
      id: goal._id.toString(),
      title: goal.title,
      urduTitle: goal.urduTitle,
      targetAmount: goal.targetAmount,
      currentAmount: goal.currentAmount,
      targetDate: goal.targetDate,
      emoji: goal.emoji,
      isCompleted: goal.isCompleted
    });
  } catch (error) {
    return res.status(500).json({ error: 'Error creating goal' });
  }
});

// PUT /api/goals/:id/contribute
router.put('/:id/contribute', authMiddleware, async (req, res) => {
  try {
    const { amount } = req.body;
    const contribution = Number(amount);

    if (isNaN(contribution) || contribution <= 0) {
      return res.status(400).json({ error: 'Contribution amount must be greater than 0' });
    }

    const goal = await Goal.findOne({ _id: req.params.id, userId: req.userId });
    if (!goal) {
      return res.status(404).json({ error: 'Goal not found' });
    }

    goal.currentAmount += contribution;
    goal.isCompleted = goal.currentAmount >= goal.targetAmount;
    await goal.save();

    await calculateProsperityScore(req.userId);

    return res.json({
      id: goal._id.toString(),
      title: goal.title,
      urduTitle: goal.urduTitle,
      targetAmount: goal.targetAmount,
      currentAmount: goal.currentAmount,
      targetDate: goal.targetDate,
      emoji: goal.emoji,
      isCompleted: goal.isCompleted
    });
  } catch (error) {
    return res.status(500).json({ error: 'Error contributing to goal' });
  }
});

module.exports = router;
