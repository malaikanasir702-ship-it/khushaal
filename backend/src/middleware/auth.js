const jwt = require('jsonwebtoken');
const env = require('../config/env');

const authMiddleware = (req, res, next) => {
  const authHeader = req.headers.authorization;
  if (!authHeader || !authHeader.startsWith('Bearer ')) {
    return res.status(401).json({ error: 'Unauthorized', message: 'غیر مجاز رسائی / Unauthorized access' });
  }

  const token = authHeader.split(' ')[1];
  if (!token) {
    return res.status(401).json({ error: 'Unauthorized', message: 'ٹوکن غائب ہے / Token is missing' });
  }

  try {
    const decoded = jwt.verify(token, env.JWT_SECRET);
    req.userId = decoded.userId;
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

module.exports = authMiddleware;
