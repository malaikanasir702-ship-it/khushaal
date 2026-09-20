const express = require('express');
const authMiddleware = require('../middleware/auth');
const Envelope = require('../models/Envelope');

const router = express.Router();

// GET /api/envelopes
router.get('/', authMiddleware, async (req, res) => {
  try {
    const envelopes = await Envelope.find({ userId: req.userId });
    const formatted = envelopes.map(e => ({
      id: e._id.toString(),
      envelopeKey: e.envelopeKey,
      titleEnglish: e.titleEnglish,
      titleUrdu: e.titleUrdu || '',
      percentage: e.percentage,
      amount: e.amount,
      tag: e.tag || '',
      items: e.items || []
    }));
    return res.json(formatted);
  } catch (error) {
    return res.status(500).json({ error: 'Error fetching envelopes' });
  }
});

// PUT /api/envelopes/:id
router.put('/:id', authMiddleware, async (req, res) => {
  try {
    const { percentage, amount, items, tag, titleEnglish, titleUrdu } = req.body;
    const envelope = await Envelope.findOne({ _id: req.params.id, userId: req.userId });

    if (!envelope) {
      return res.status(404).json({ error: 'Envelope not found' });
    }

    if (percentage !== undefined) {
      const pct = Number(percentage);
      if (isNaN(pct) || pct < 0 || pct > 100) {
        return res.status(400).json({ error: 'Percentage must be between 0 and 100' });
      }
      envelope.percentage = pct;
    }

    if (amount !== undefined) {
      const amt = Number(amount);
      if (isNaN(amt) || amt < 0) {
        return res.status(400).json({ error: 'Amount cannot be negative' });
      }
      envelope.amount = amt;
    }

    if (items !== undefined && Array.isArray(items)) {
      envelope.items = items;
    }

    if (tag !== undefined) envelope.tag = tag;
    if (titleEnglish !== undefined) envelope.titleEnglish = titleEnglish;
    if (titleUrdu !== undefined) envelope.titleUrdu = titleUrdu;
    envelope.updatedAt = new Date();

    await envelope.save();

    return res.json({
      id: envelope._id.toString(),
      envelopeKey: envelope.envelopeKey,
      titleEnglish: envelope.titleEnglish,
      titleUrdu: envelope.titleUrdu || '',
      percentage: envelope.percentage,
      amount: envelope.amount,
      tag: envelope.tag || '',
      items: envelope.items || []
    });
  } catch (error) {
    return res.status(500).json({ error: 'Error updating envelope' });
  }
});

module.exports = router;
