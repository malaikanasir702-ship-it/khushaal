const express = require('express');
const authMiddleware = require('../middleware/auth');
const User = require('../models/User');

const router = express.Router();

// GET /api/users/me
router.get('/me', authMiddleware, async (req, res) => {
  try {
    const user = await User.findById(req.userId);
    if (!user) {
      return res.status(404).json({ error: 'User not found' });
    }
    return res.json(user.toProfileDto());
  } catch (error) {
    return res.status(500).json({ error: 'Error fetching profile' });
  }
});

// PUT /api/users/me
router.put('/me', authMiddleware, async (req, res) => {
  try {
    const { name, urduName, factory, jazzCashNumber, bankName, bankAccountNumber, preferredLanguage, avatarUrl } = req.body;

    if (preferredLanguage && !['BILINGUAL', 'URDU', 'ENGLISH'].includes(preferredLanguage)) {
      return res.status(400).json({ error: 'Invalid preferred language. Must be BILINGUAL, URDU, or ENGLISH.' });
    }

    const updates = {};
    if (name !== undefined) updates.name = name;
    if (urduName !== undefined) updates.urduName = urduName;
    if (factory !== undefined) updates.factory = factory;
    if (jazzCashNumber !== undefined) updates.jazzCashNumber = jazzCashNumber;
    if (bankName !== undefined) updates.bankName = bankName;
    if (bankAccountNumber !== undefined) updates.bankAccountNumber = bankAccountNumber;
    if (preferredLanguage !== undefined) updates.preferredLanguage = preferredLanguage;
    if (avatarUrl !== undefined) updates.avatarUrl = avatarUrl;

    const user = await User.findByIdAndUpdate(req.userId, { $set: updates }, { new: true });
    if (!user) {
      return res.status(404).json({ error: 'User not found' });
    }

    return res.json(user.toProfileDto());
  } catch (error) {
    return res.status(500).json({ error: 'Error updating profile' });
  }
});

module.exports = router;
