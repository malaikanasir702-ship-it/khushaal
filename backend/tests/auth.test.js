const request = require('supertest');
const bcrypt = require('bcryptjs');
const app = require('../src/app');
const User = require('../src/models/User');
const Envelope = require('../src/models/Envelope');
const Locker = require('../src/models/Locker');
const Prosperity = require('../src/models/Prosperity');
const CoachMessage = require('../src/models/CoachMessage');
const { connectTestDB, clearTestDB, closeTestDB } = require('./testHelper');

beforeAll(async () => {
  await connectTestDB();
});

afterEach(async () => {
  await clearTestDB();
});

afterAll(async () => {
  await closeTestDB();
});

describe('Task 1.2: Health Check', () => {
  test('GET /api/health returns 200 when database connected', async () => {
    const res = await request(app).get('/api/health');
    expect(res.status).toBe(200);
    expect(res.body.status).toBe('ok');
    expect(res.body.db).toBe('connected');
  });
});

describe('Task 3: Auth System (Registration, Login, Refresh, Seeding)', () => {
  const validUser = {
    name: 'Ahmed Raza',
    urduName: 'احمد رضا',
    cnic: '42101-1234567-3',
    phone: '+923001234567',
    factory: 'Naveena Mills Unit 4',
    factoryId: 'NVM-4892',
    jazzCashNumber: '03001234567',
    password: 'password123',
    preferredLanguage: 'BILINGUAL'
  };

  test('Property 1: Registration input validation rejects malformed identifiers', async () => {
    // Malformed CNIC
    const badCnicRes = await request(app)
      .post('/api/auth/register')
      .send({ ...validUser, cnic: '12345-invalid' });
    expect(badCnicRes.status).toBe(400);

    // Malformed Phone
    const badPhoneRes = await request(app)
      .post('/api/auth/register')
      .send({ ...validUser, phone: '03001234567' }); // Missing +92 prefix
    expect(badPhoneRes.status).toBe(400);

    // Short password (< 8 chars)
    const shortPassRes = await request(app)
      .post('/api/auth/register')
      .send({ ...validUser, password: 'short' });
    expect(shortPassRes.status).toBe(400);
  });

  test('Property 2: Password stored as bcrypt hash, never plaintext', async () => {
    const res = await request(app)
      .post('/api/auth/register')
      .send(validUser);
    expect(res.status).toBe(201);

    const userInDb = await User.findOne({ phone: validUser.phone });
    expect(userInDb).not.toBeNull();
    expect(userInDb.passwordHash).not.toBe(validUser.password);
    const isValidHash = await bcrypt.compare(validUser.password, userInDb.passwordHash);
    expect(isValidHash).toBe(true);
  });

  test('Property 3: New user registration seeds required default records', async () => {
    const res = await request(app)
      .post('/api/auth/register')
      .send(validUser);
    expect(res.status).toBe(201);

    const userId = res.body.user.id;

    // Verify 4 envelopes
    const envelopes = await Envelope.find({ userId });
    expect(envelopes.length).toBe(4);
    const keys = envelopes.map(e => e.envelopeKey).sort();
    expect(keys).toEqual(['commitments', 'emergency', 'needs', 'savings']);

    // Verify emergency locker
    const locker = await Locker.findOne({ userId });
    expect(locker).not.toBeNull();
    expect(locker.balance).toBe(0);

    // Verify prosperity record
    const prosperity = await Prosperity.findOne({ userId });
    expect(prosperity).not.toBeNull();
    expect(prosperity.pillars.length).toBe(6);

    // Verify coach message
    const coachMsg = await CoachMessage.findOne({ userId, isFromCoach: true });
    expect(coachMsg).not.toBeNull();
    expect(coachMsg.textUrdu).toContain('السلام علیکم');
  });

  test('Property 4: Login round-trip after registration succeeds', async () => {
    // Register
    await request(app).post('/api/auth/register').send(validUser);

    // Login with same credentials
    const loginRes = await request(app)
      .post('/api/auth/login')
      .send({ phone: validUser.phone, password: validUser.password });

    expect(loginRes.status).toBe(200);
    expect(loginRes.body.accessToken).toBeDefined();
    expect(loginRes.body.refreshToken).toBeDefined();
    expect(loginRes.body.user.name).toBe(validUser.name);
    expect(loginRes.body.user.cnicMasked).toBe('42101-•••••••-3');
  });

  test('Property 5: Login with wrong password is always rejected', async () => {
    await request(app).post('/api/auth/register').send(validUser);

    const loginRes = await request(app)
      .post('/api/auth/login')
      .send({ phone: validUser.phone, password: 'wrongPassword123' });

    expect(loginRes.status).toBe(401);
    expect(loginRes.body.error).toContain('Invalid credentials');
  });

  test('Duplicate phone or CNIC returns 409 Conflict', async () => {
    await request(app).post('/api/auth/register').send(validUser);

    // Duplicate phone
    const dupPhone = await request(app)
      .post('/api/auth/register')
      .send({ ...validUser, cnic: '42101-9999999-1' });
    expect(dupPhone.status).toBe(409);

    // Duplicate CNIC
    const dupCnic = await request(app)
      .post('/api/auth/register')
      .send({ ...validUser, phone: '+923009999999' });
    expect(dupCnic.status).toBe(409);
  });

  test('Property 6: Refresh token rotation invalidates old token', async () => {
    const regRes = await request(app).post('/api/auth/register').send(validUser);
    const oldRefreshToken = regRes.body.refreshToken;

    // First refresh: succeeds and rotates token
    const refreshRes = await request(app)
      .post('/api/auth/refresh')
      .send({ refreshToken: oldRefreshToken });

    expect(refreshRes.status).toBe(200);
    expect(refreshRes.body.accessToken).toBeDefined();
    expect(refreshRes.body.refreshToken).toBeDefined();
    expect(refreshRes.body.refreshToken).not.toBe(oldRefreshToken);

    // Second refresh with OLD token: must be rejected (401)
    const secondCall = await request(app)
      .post('/api/auth/refresh')
      .send({ refreshToken: oldRefreshToken });
    expect(secondCall.status).toBe(401);
  });
});
