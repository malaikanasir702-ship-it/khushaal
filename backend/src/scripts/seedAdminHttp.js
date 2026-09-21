/**
 * HTTP-based seed script — creates admin via Railway live API
 * Run: node src/scripts/seedAdminHttp.js
 */

const https = require('https');

const RAILWAY_URL = 'khushaal-production.up.railway.app';

const adminData = JSON.stringify({
  name: 'Khushhaal Admin',
  urduName: 'خوشحال ایڈمن',
  phone: '+923001234567',
  cnic: '35201-0000001-1',
  factory: 'Khushhaal Head Office',
  factoryId: 'KHQ-ADMIN-001',
  password: 'Khushhaal@Admin2025!',
  role: 'admin'
});

const options = {
  hostname: RAILWAY_URL,
  path: '/api/auth/register',
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'Content-Length': Buffer.byteLength(adminData)
  }
};

const req = https.request(options, (res) => {
  let data = '';
  res.on('data', (chunk) => { data += chunk; });
  res.on('end', () => {
    console.log('Status:', res.statusCode);
    console.log('Response:', data);
    if (res.statusCode === 201 || res.statusCode === 200) {
      console.log('\n✅ Admin registered via API!');
      console.log('Phone:    +923001234567');
      console.log('Password: Khushhaal@Admin2025!');
    } else {
      console.log('\n❌ Registration failed — check response above');
    }
  });
});

req.on('error', (e) => console.error('Request error:', e.message));
req.write(adminData);
req.end();
