const dotenv = require('dotenv');
const path = require('path');

dotenv.config({ path: path.resolve(__dirname, '../../.env') });

module.exports = {
  PORT: process.env.PORT || 5000,
  MONGODB_URI: process.env.MONGODB_URI || 'mongodb://127.0.0.1:27017/khushhaal_db',
  JWT_SECRET: process.env.JWT_SECRET || 'khushhaal_jwt_super_secret_key_2025_pakistan_financial_wellness',
  REFRESH_SECRET: process.env.REFRESH_SECRET || 'khushhaal_refresh_super_secret_token_key_2025_secure_rotation',
  FIREBASE_PROJECT_ID: process.env.FIREBASE_PROJECT_ID || 'khushhaal-finance-wbfin',
  NODE_ENV: process.env.NODE_ENV || 'development'
};
