const express = require('express');
const auth = require('../middleware/auth');
const Payslip = require('../models/Payslip');

const router = express.Router();

// GET /api/payslip — Get worker's own payslips (latest first)
router.get('/', auth, async (req, res) => {
  try {
    const payslips = await Payslip.find({ userId: req.userId })
      .sort({ createdAt: -1 })
      .limit(12); // last 12 months

    if (payslips.length === 0) {
      return res.json(null); // app handles null = no payslip yet
    }
    return res.json(payslips[0]); // return most recent
  } catch (error) {
    return res.status(500).json({ error: 'Error fetching payslip' });
  }
});

// GET /api/payslip/all — Get all payslips for the worker
router.get('/all', auth, async (req, res) => {
  try {
    const payslips = await Payslip.find({ userId: req.userId })
      .sort({ createdAt: -1 });
    return res.json(payslips);
  } catch (error) {
    return res.status(500).json({ error: 'Error fetching payslips' });
  }
});

module.exports = router;
