const bcrypt = require('bcryptjs');
const { connectDB, disconnectDB } = require('../config/db');
const User = require('../models/User');

const seedAdmin = async () => {
  try {
    console.log('[Admin Seeder] Connecting to database...');
    await connectDB();

    const adminPhone = '+923000000000';
    const adminCnic = '00000-0000000-0';
    const adminPassword = 'AdminPassword123!';

    let admin = await User.findOne({ 
      $or: [{ phone: adminPhone }, { cnic: adminCnic }, { role: 'admin' }] 
    });

    if (!admin) {
      console.log('[Admin Seeder] Creating default Super Admin...');
      const passwordHash = await bcrypt.hash(adminPassword, 12);
      admin = new User({
        name: 'Khushhaal Super Admin',
        urduName: 'خوشحال سپرا ایڈمن',
        phone: adminPhone,
        cnic: adminCnic,
        passwordHash,
        factory: 'Khushhaal Head Office',
        factoryId: 'HQ-ADMIN-01',
        jazzCashNumber: '03000000000',
        role: 'admin',
        preferredLanguage: 'BILINGUAL',
        isActive: true
      });
      await admin.save();
      console.log('====================================================');
      console.log(' Super Admin successfully created!');
      console.log(` Phone:    ${adminPhone}`);
      console.log(` Password: ${adminPassword}`);
      console.log(` CNIC:     ${adminCnic}`);
      console.log('====================================================');
    } else {
      console.log(`[Admin Seeder] Admin account exists: ${admin.phone} (Role: ${admin.role})`);
      if (admin.role !== 'admin') {
        admin.role = 'admin';
        await admin.save();
        console.log('[Admin Seeder] Promoted existing account to admin.');
      }
    }

    await disconnectDB();
    console.log('[Admin Seeder] Done.');
  } catch (error) {
    console.error('[Admin Seeder] Error seeding admin:', error);
    process.exit(1);
  }
};

if (require.main === module) {
  seedAdmin();
}

module.exports = seedAdmin;
