const express = require('express');
const authMiddleware = require('../middleware/auth');
const Order = require('../models/Order');

const router = express.Router();

// GET /api/orders
router.get('/', authMiddleware, async (req, res) => {
  try {
    const orders = await Order.find({ userId: req.userId }).sort({ createdAt: -1 });
    const formatted = orders.map(o => ({
      id: o._id.toString(),
      customerName: o.customerName,
      phone: o.phone || '',
      serviceTitle: o.serviceTitle,
      totalAmount: o.totalAmount,
      advancePaid: o.advancePaid,
      dueDate: o.dueDate || '',
      isDelivered: o.isDelivered,
      isFullyPaid: o.isFullyPaid
    }));
    return res.json(formatted);
  } catch (error) {
    return res.status(500).json({ error: 'Error fetching orders' });
  }
});

// POST /api/orders
router.post('/', authMiddleware, async (req, res) => {
  try {
    const { customerName, phone, serviceTitle, totalAmount, advancePaid, dueDate } = req.body;

    if (!customerName || !customerName.trim()) {
      return res.status(400).json({ error: 'Customer name is required' });
    }
    if (!serviceTitle || !serviceTitle.trim()) {
      return res.status(400).json({ error: 'Service title is required' });
    }

    const total = Number(totalAmount);
    if (isNaN(total) || total < 1) {
      return res.status(400).json({ error: 'Total amount must be at least 1' });
    }

    const advance = Math.max(0, Number(advancePaid) || 0);

    const order = new Order({
      userId: req.userId,
      customerName: customerName.trim(),
      phone: phone ? phone.trim() : '',
      serviceTitle: serviceTitle.trim(),
      totalAmount: total,
      advancePaid: advance,
      dueDate: dueDate ? dueDate.trim() : '',
      isDelivered: false,
      isFullyPaid: advance >= total
    });

    await order.save();

    return res.status(201).json({
      id: order._id.toString(),
      customerName: order.customerName,
      phone: order.phone,
      serviceTitle: order.serviceTitle,
      totalAmount: order.totalAmount,
      advancePaid: order.advancePaid,
      dueDate: order.dueDate,
      isDelivered: order.isDelivered,
      isFullyPaid: order.isFullyPaid
    });
  } catch (error) {
    return res.status(500).json({ error: 'Error creating order' });
  }
});

// PUT /api/orders/:id
router.put('/:id', authMiddleware, async (req, res) => {
  try {
    const { isDelivered, isFullyPaid, advancePaid, dueDate } = req.body;
    const order = await Order.findOne({ _id: req.params.id, userId: req.userId });

    if (!order) {
      return res.status(404).json({ error: 'Order not found' });
    }

    if (isDelivered !== undefined) order.isDelivered = Boolean(isDelivered);
    if (isFullyPaid !== undefined) order.isFullyPaid = Boolean(isFullyPaid);
    if (advancePaid !== undefined) {
      order.advancePaid = Math.max(0, Number(advancePaid) || 0);
      if (order.advancePaid >= order.totalAmount) {
        order.isFullyPaid = true;
      }
    }
    if (dueDate !== undefined) order.dueDate = dueDate;

    await order.save();

    return res.json({
      id: order._id.toString(),
      customerName: order.customerName,
      phone: order.phone,
      serviceTitle: order.serviceTitle,
      totalAmount: order.totalAmount,
      advancePaid: order.advancePaid,
      dueDate: order.dueDate,
      isDelivered: order.isDelivered,
      isFullyPaid: order.isFullyPaid
    });
  } catch (error) {
    return res.status(500).json({ error: 'Error updating order' });
  }
});

module.exports = router;
