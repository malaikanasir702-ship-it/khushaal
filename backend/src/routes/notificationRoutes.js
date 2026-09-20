const express = require('express');
const authMiddleware = require('../middleware/auth');
const Notification = require('../models/Notification');

const router = express.Router();

// GET /api/notifications
router.get('/', authMiddleware, async (req, res) => {
  try {
    const notifications = await Notification.find({ userId: req.userId }).sort({ createdAt: -1 });
    const formatted = notifications.map(n => ({
      id: n._id.toString(),
      titleUrdu: n.titleUrdu,
      titleEnglish: n.titleEnglish,
      descriptionUrdu: n.descriptionUrdu || '',
      descriptionEnglish: n.descriptionEnglish || '',
      category: n.category,
      isRead: n.isRead,
      timestamp: n.createdAt.toISOString(),
      destination: n.destination || null,
      spokenText: n.spokenText || ''
    }));
    return res.json(formatted);
  } catch (error) {
    return res.status(500).json({ error: 'Error fetching notifications' });
  }
});

// PUT /api/notifications/:id/read
router.put('/:id/read', authMiddleware, async (req, res) => {
  try {
    const notification = await Notification.findOne({ _id: req.params.id, userId: req.userId });
    if (!notification) {
      return res.status(404).json({ error: 'Notification not found' });
    }

    notification.isRead = true;
    await notification.save();

    return res.json({ message: 'Marked as read', id: notification._id.toString() });
  } catch (error) {
    return res.status(500).json({ error: 'Error updating notification' });
  }
});

// DELETE /api/notifications
router.delete('/', authMiddleware, async (req, res) => {
  try {
    await Notification.deleteMany({ userId: req.userId });
    return res.json({ message: 'All notifications cleared' });
  } catch (error) {
    return res.status(500).json({ error: 'Error clearing notifications' });
  }
});

module.exports = router;
