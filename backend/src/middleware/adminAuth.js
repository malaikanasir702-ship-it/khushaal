const jwt = require('jsonwebtoken');
const env = require('../config/env');
const User = require('../models/User');

const adminAuthMiddleware = async (req, res, next) => {
  const authHeader = req.headers.authorization;
  if (!authHeader || !authHeader.startsWith('Bearer ')) {
    return res.status(401).json({ 
      error: 'Unauthorized', 
      message: 'ایڈمن لاگ ان درکار ہے / Admin authorization required' 
    });
  }

  const token = authHeader.split(' ')[1];
  if (!token) {
    return res.status(401).json({ 
      error: 'Unauthorized', 
      message: 'ٹوکن غائب ہے / Token is missing' 
    });
  }

  try {
    const decoded = jwt.verify(token, env.JWT_SECRET);
    const user = await User.findById(decoded.userId);

    if (!user) {
      return res.status(401).json({ 
        error: 'Unauthorized', 
        message: 'ایڈمن اکاؤنٹ نہیں ملا / Admin account not found' 
      });
    }

    if (user.role !== 'admin') {
      return res.status(403).json({ 
        error: 'Forbidden', 
        message: 'اس کارروائی کے لیے ایڈمن رسائی درکار ہے / Admin privileges required' 
      });
    }

    if (!user.isActive) {
      return res.status(403).json({ 
        error: 'Forbidden', 
        message: 'اکاؤنٹ معطل ہے / Account is deactivated' 
      });
    }

    req.userId = user._id;
    req.user = user;
    next();
  } catch (error) {
    if (error.name === 'TokenExpiredError') {
      return res.status(401).json({ 
        error: 'Token expired', 
        code: 'TOKEN_EXPIRED',
        message: 'سیشن ختم ہو گیا / Session expired' 
      });
    }
    return res.status(401).json({ 
      error: 'Unauthorized', 
      message: 'غلط ٹوکن / Invalid or malformed token' 
    });
  }
};

module.exports = adminAuthMiddleware;
