const express = require('express');
const authMiddleware = require('../middleware/auth');
const Prosperity = require('../models/Prosperity');
const { calculateProsperityScore } = require('../services/prosperityCalculator');

const router = express.Router();

// GET /api/prosperity
router.get('/', authMiddleware, async (req, res) => {
  try {
    let prosperity = await Prosperity.findOne({ userId: req.userId });
    if (!prosperity) {
      prosperity = await calculateProsperityScore(req.userId);
    }

    return res.json({
      score: prosperity.score,
      maxScore: prosperity.maxScore,
      savingsPct: prosperity.savingsPct,
      debtControlPct: prosperity.debtControlPct,
      safetyShieldPct: prosperity.safetyShieldPct,
      daysRunway: prosperity.daysRunway,
      pillars: prosperity.pillars,
      lastCalculatedAt: prosperity.lastCalculatedAt
    });
  } catch (error) {
    return res.status(500).json({ error: 'Error fetching prosperity score' });
  }
});

module.exports = router;
