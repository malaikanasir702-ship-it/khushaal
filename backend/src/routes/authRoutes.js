const express = require('express');
const { body, validationResult } = require('express-validator');
const rateLimit = require('express-rate-limit');
const authController = require('../controllers/authController');
const authMiddleware = require('../middleware/auth');

const router = express.Router();

// Rate limiting for login: 5 attempts per 15 min per IP (Skip in test environment)
const loginLimiter = rateLimit({
  windowMs: 15 * 60 * 1000,
  max: 5,
  message: { error: 'Too many login attempts. Please try again after 15 minutes.' },
  standardHeaders: true,
  legacyHeaders: false,
  skip: () => process.env.NODE_ENV === 'test'
});

const validateRegistration = [
  body('name').trim().notEmpty().withMessage('Name is required / نام ضروری ہے'),
  body('cnic')
    .trim()
    .matches(/^\d{5}-\d{7}-\d$/)
    .withMessage('Invalid CNIC format (DDDDD-DDDDDDD-D) / شناختی کارڈ نمبر درست نہیں ہے'),
  body('phone')
    .trim()
    .matches(/^\+92\d{10}$/)
    .withMessage('Invalid Pakistani phone format (+92XXXXXXXXXX) / درست پاکستانی موبائل نمبر درج کریں'),
  body('password')
    .isLength({ min: 8 })
    .withMessage('Password must be at least 8 characters long / پاسورڈ کم از کم 8 حروف پر مشتمل ہونا چاہیے'),
  body('factory').trim().notEmpty().withMessage('Factory is required / فیکٹری کا نام ضروری ہے'),
  (req, res, next) => {
    const errors = validationResult(req);
    if (!errors.isEmpty()) {
      return res.status(400).json({ 
        error: errors.array()[0].msg, 
        errors: errors.array() 
      });
    }
    next();
  }
];

router.post('/register', validateRegistration, authController.register);
router.post('/login', loginLimiter, authController.login);
router.post('/refresh', authController.refreshToken);
router.post('/logout', authMiddleware, authController.logout);

module.exports = router;
