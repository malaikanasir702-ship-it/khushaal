/**
 * Seed Script: Creates the Super Admin account in MongoDB
 * Run: node src/scripts/seedAdmin.js
 * 
 * Credentials:
 *   Phone: +923001234567
 *   Password: Khushhaal@Admin2025!
 */

require('dotenv').config({ path: require('path').resolve(__dirname, '../../.env') });

const mongoose = require('mongoose');
const bcrypt = require('bcryptjs');
const User = require('../models/User');
const env = require('../config/env');

const ADMIN_CREDENTIALS = {
  name: process.env.SEED_ADMIN_NAME || 'Khushhaal Admin',
  urduName: 'خوشحال ایڈمن',
  phone: process.env.SEED_ADMIN_PHONE || '+923001234567',
  cnic: process.env.SEED_ADMIN_CNIC || '35201-0000001-1',
  factory: 'Khushhaal Head Office',
  factoryId: 'KHQ-ADMIN-001',
  password: process.env.SEED_ADMIN_PASSWORD,
  role: 'admin',
  isActive: true
};

if (!ADMIN_CREDENTIALS.password) {
  console.error('❌ SEED_ADMIN_PASSWORD is not set in .env. Aborting.');
  process.exit(1);
}

async function seed() {
  try {
    await mongoose.connect(env.MONGODB_URI);
    console.log('✅ Connected to MongoDB');

    const existing = await User.findOne({ phone: ADMIN_CREDENTIALS.phone });
    if (existing) {
      console.log('⚠️  Admin already exists:', ADMIN_CREDENTIALS.phone);
      console.log('   To reset, delete the user from MongoDB and run again.');
      process.exit(0);
    }

    const passwordHash = await bcrypt.hash(ADMIN_CREDENTIALS.password, 12);
    const admin = new User({
      name: ADMIN_CREDENTIALS.name,
      urduName: ADMIN_CREDENTIALS.urduName,
      phone: ADMIN_CREDENTIALS.phone,
      cnic: ADMIN_CREDENTIALS.cnic,
      passwordHash,
      factory: ADMIN_CREDENTIALS.factory,
      factoryId: ADMIN_CREDENTIALS.factoryId,
      role: 'admin',
      isActive: true
    });

    await admin.save();

    console.log('\n🎉 Super Admin created successfully!');
    console.log('━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━');
    console.log(`   Phone:    ${ADMIN_CREDENTIALS.phone}`);
    console.log(`   Password: (as set in SEED_ADMIN_PASSWORD env var)`);
    console.log(`   URL:      ${process.env.BACKEND_URL || 'https://your-deployment-url/admin/'}`);
    console.log('━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n');
    process.exit(0);
  } catch (error) {
    console.error('❌ Seed error:', error.message);
    process.exit(1);
  }
}

seed();
