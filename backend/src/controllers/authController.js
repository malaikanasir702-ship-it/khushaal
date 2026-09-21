const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const User = require('../models/User');
const env = require('../config/env');
const { seedUserDefaults } = require('../services/seedService');

const register = async (req, res) => {
  try {
    const { 
      name, 
      urduName, 
      cnic, 
      phone, 
      factory, 
      factoryId, 
      jazzCashNumber, 
      bankName,
      bankAccountNumber,
      password, 
      preferredLanguage 
    } = req.body;

    // Check duplicate phone
    const existingPhone = await User.findOne({ phone });
    if (existingPhone) {
      return res.status(409).json({ 
        error: 'Phone number already registered / نمبر پہلے سے رجسٹرڈ ہے' 
      });
    }

    // Check duplicate CNIC
    const existingCnic = await User.findOne({ cnic });
    if (existingCnic) {
      return res.status(409).json({ 
        error: 'CNIC already registered / شناختی کارڈ پہلے سے موجود ہے' 
      });
    }

    // Hash password with 12 rounds
    const passwordHash = await bcrypt.hash(password, 12);

    const newUser = new User({
      name,
      urduName: urduName || name,
      cnic,
      phone,
      passwordHash,
      factory,
      factoryId: factoryId || '',
      jazzCashNumber: jazzCashNumber || '',
      bankName: bankName || '',
      bankAccountNumber: bankAccountNumber || '',
      preferredLanguage: preferredLanguage || 'BILINGUAL'
    });

    await newUser.save();

    // Seed default records
    await seedUserDefaults(newUser._id);

    // Generate JWT tokens (tv=0 = initial token version)
    const accessToken = jwt.sign({ userId: newUser._id }, env.JWT_SECRET, { expiresIn: '15m' });
    const refreshToken = jwt.sign({ userId: newUser._id, tv: 0, nonce: Math.random() }, env.REFRESH_SECRET, { expiresIn: '30d' });

    newUser.refreshTokenHash = await bcrypt.hash(refreshToken, 10);
    newUser.tokenVersion = 0;
    newUser.lastLoginAt = new Date();
    await newUser.save();

    return res.status(201).json({
      accessToken,
      refreshToken,
      user: newUser.toProfileDto()
    });
  } catch (error) {
    console.error('Registration error:', error);
    return res.status(500).json({ error: 'Server error during registration', message: error.message });
  }
};

const login = async (req, res) => {
  try {
    const { phone, password } = req.body;

    if (!phone || !password) {
      return res.status(400).json({ error: 'Phone and password are required' });
    }

    const user = await User.findOne({ phone });
    if (!user) {
      return res.status(401).json({ 
        error: 'غلط نمبر یا پاسورڈ / Invalid credentials' 
      });
    }

    const isMatch = await bcrypt.compare(password, user.passwordHash);
    if (!isMatch) {
      return res.status(401).json({ 
        error: 'غلط نمبر یا پاسورڈ / Invalid credentials' 
      });
    }

    const accessToken = jwt.sign({ userId: user._id }, env.JWT_SECRET, { expiresIn: '15m' });
    // On login, reset tokenVersion to 0 so that the new token can be refreshed
    const tv = 0;
    const refreshToken = jwt.sign({ userId: user._id, tv, nonce: Math.random() }, env.REFRESH_SECRET, { expiresIn: '30d' });

    user.refreshTokenHash = await bcrypt.hash(refreshToken, 10);
    user.tokenVersion = tv;
    user.lastLoginAt = new Date();
    await user.save();

    return res.status(200).json({
      accessToken,
      refreshToken,
      user: user.toProfileDto()
    });
  } catch (error) {
    console.error('Login error:', error);
    return res.status(500).json({ error: 'Server error during login', message: error.message });
  }
};

const refreshToken = async (req, res) => {
  try {
    const { refreshToken: token } = req.body;
    if (!token) {
      return res.status(401).json({ error: 'Refresh token is required' });
    }

    let decoded;
    try {
      decoded = jwt.verify(token, env.REFRESH_SECRET);
    } catch (err) {
      return res.status(401).json({ error: 'Invalid or expired refresh token' });
    }

    // Use the tokenVersion embedded in the JWT payload for replay detection
    // tv = token version; if absent, treat as 0
    const tokenVersion = decoded.tv ?? 0;

    // Fetch directly from MongoDB collection (bypasses all Mongoose caching)
    const { Types } = require('mongoose');
    const rawUser = await User.collection.findOne({
      _id: new Types.ObjectId(decoded.userId.toString())
    });

    if (!rawUser || rawUser.refreshTokenHash == null) {
      return res.status(401).json({ error: 'Invalid refresh token session' });
    }

    // Check token version matches — prevents replay after rotation
    const storedVersion = rawUser.tokenVersion ?? 0;
    if (tokenVersion !== storedVersion) {
      return res.status(401).json({ error: 'Refresh token has been rotated / ٹوکن تبدیل ہو چکا ہے' });
    }

    // Validate the bcrypt hash
    const isMatch = await bcrypt.compare(token, rawUser.refreshTokenHash);
    if (!isMatch) {
      return res.status(401).json({ error: 'Invalid refresh token' });
    }

    // Atomically increment tokenVersion and clear old hash — this invalidates the old token
    const newVersion = storedVersion + 1;
    await User.collection.updateOne(
      { _id: rawUser._id },
      { $set: { refreshTokenHash: null, tokenVersion: newVersion } }
    );

    // Issue new tokens with the updated version embedded
    const newAccessToken = jwt.sign({ userId: rawUser._id }, env.JWT_SECRET, { expiresIn: '15m' });
    const newRefreshToken = jwt.sign(
      { userId: rawUser._id, tv: newVersion, nonce: Math.random() },
      env.REFRESH_SECRET,
      { expiresIn: '30d' }
    );

    const newHash = await bcrypt.hash(newRefreshToken, 10);
    await User.collection.updateOne(
      { _id: rawUser._id },
      { $set: { refreshTokenHash: newHash } }
    );

    // Fetch user via Mongoose for DTO methods
    const user = await User.findById(rawUser._id);

    return res.status(200).json({
      accessToken: newAccessToken,
      refreshToken: newRefreshToken,
      user: user.toProfileDto()
    });
  } catch (error) {
    console.error('Refresh error:', error);
    return res.status(500).json({ error: 'Server error during token refresh' });
  }
};

const logout = async (req, res) => {
  try {
    if (req.userId) {
      await User.findByIdAndUpdate(req.userId, { refreshTokenHash: null });
    }
    return res.status(200).json({ message: 'Logged out successfully' });
  } catch (error) {
    return res.status(500).json({ error: 'Server error during logout' });
  }
};

module.exports = {
  register,
  login,
  refreshToken,
  logout
};
