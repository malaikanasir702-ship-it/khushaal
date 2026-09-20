const express = require('express');
const authMiddleware = require('../middleware/auth');
const CoachMessage = require('../models/CoachMessage');
const CashFlow = require('../models/CashFlow');
const Locker = require('../models/Locker');

const router = express.Router();

// GET /api/coach/messages
router.get('/messages', authMiddleware, async (req, res) => {
  try {
    const messages = await CoachMessage.find({ userId: req.userId }).sort({ createdAt: 1 });
    const formatted = messages.map(m => ({
      id: m._id.toString(),
      textUrdu: m.textUrdu,
      textRoman: m.textRoman || '',
      isFromCoach: m.isFromCoach,
      timestamp: m.createdAt.toISOString(),
      spokenText: m.spokenText || ''
    }));
    return res.json(formatted);
  } catch (error) {
    return res.status(500).json({ error: 'Error fetching coach messages' });
  }
});

// POST /api/coach/messages
router.post('/messages', authMiddleware, async (req, res) => {
  try {
    const { textUrdu, textRoman } = req.body;

    // Empty or whitespace-only message validation (auth-independent check requirement)
    const text = textUrdu || textRoman || '';
    if (!text || !text.trim()) {
      return res.status(400).json({ error: 'Message cannot be empty / پیغام خالی نہیں ہو سکتا' });
    }

    // 1. Save user message
    const userMsg = new CoachMessage({
      userId: req.userId,
      textUrdu: textUrdu ? textUrdu.trim() : textRoman.trim(),
      textRoman: textRoman ? textRoman.trim() : textUrdu.trim(),
      isFromCoach: false,
      spokenText: textUrdu ? textUrdu.trim() : textRoman.trim()
    });
    await userMsg.save();

    // 2. Fetch context for personalized coach guidance
    const [cashFlow, locker] = await Promise.all([
      CashFlow.findOne({ userId: req.userId }).sort({ month: -1 }),
      Locker.findOne({ userId: req.userId })
    ]);

    const lockerBal = locker ? locker.balance : 0;
    const salary = cashFlow ? cashFlow.income : 0;

    let coachReplyUrdu = 'شاباش! آپ کا سوال موصول ہوا۔ اپنی مالی حفاظت کے لیے ہنگامی لاکر میں کم از کم ایک ماہ کا خرچہ محفوظ رکھیں۔';
    let coachReplyRoman = 'Shabash! Aap ka sawal mosool hua. Apni mali hifazat ke liye emergency locker mein kam az kam aik mah ka kharcha mehfooz rakhein.';

    if (text.includes('سیونگ') || text.includes('بچت') || text.toLowerCase().includes('saving')) {
      coachReplyUrdu = `بہترین! اگر آپ کی آمدن ${salary} روپے ہے، تو کم از کم 18 فیصد بچت اور کمیٹی میں لگائیں۔ اس وقت آپ کے لاکر میں ${lockerBal} روپے موجود ہیں۔`;
      coachReplyRoman = `Behtareen! Agar aap ki aamdan ${salary} rupaye hai, to kam az kam 18% bachat aur kameti mein lagayein. Is waqt aap ke locker mein ${lockerBal} rupaye hain.`;
    } else if (text.includes('قرض') || text.toLowerCase().includes('debt') || text.toLowerCase().includes('loan')) {
      coachReplyUrdu = 'قرض ادا کرنے کے لیے ڈیٹ اسنو بال طریقہ استعمال کریں۔ سب سے چھوٹے قرض کو پہلے ختم کریں تاکہ ذہنی سکون ملے۔';
      coachReplyRoman = 'Qarz ada karne ke liye debt snowball tareeqa istemal karein. Sab se chote qarz ko pehle khatam karein.';
    } else if (text.includes('لاکر') || text.toLowerCase().includes('locker')) {
      coachReplyUrdu = `آپ کے ہنگامی لاکر کا موجودہ بیلنس ${lockerBal} روپے ہے۔ جب بھی فیکٹری سے اوور ٹائم بونس ملے، کچھ رقم یہاں ڈالیں۔`;
      coachReplyRoman = `Aap ke emergency locker ka mojooda balance ${lockerBal} rupaye hai. Jab bhi factory se overtime bonus mile, kuch raqam yahan dalein.`;
    }

    // 3. Save coach response
    const coachMsg = new CoachMessage({
      userId: req.userId,
      textUrdu: coachReplyUrdu,
      textRoman: coachReplyRoman,
      isFromCoach: true,
      spokenText: coachReplyRoman
    });
    await coachMsg.save();

    return res.status(201).json({
      id: coachMsg._id.toString(),
      textUrdu: coachMsg.textUrdu,
      textRoman: coachMsg.textRoman,
      isFromCoach: coachMsg.isFromCoach,
      timestamp: coachMsg.createdAt.toISOString(),
      spokenText: coachMsg.spokenText
    });
  } catch (error) {
    return res.status(500).json({ error: 'Error processing coach message' });
  }
});

module.exports = router;
