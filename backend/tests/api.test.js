const request = require('supertest');
const app = require('../src/app');
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

describe('Task 4, 5, 6, 7, 8, 9, 10: Financial Endpoints, Prosperity & Data Isolation', () => {
  let tokenA, userAId;
  let tokenB, userBId;

  const userAPayload = {
    name: 'Ahmed Raza',
    cnic: '42101-1111111-1',
    phone: '+923001111111',
    factory: 'Naveena Mills',
    password: 'password123'
  };

  const userBPayload = {
    name: 'Bashir Khan',
    cnic: '42101-2222222-2',
    phone: '+923002222222',
    factory: 'Artistic Milliners',
    password: 'password456'
  };

  beforeEach(async () => {
    const resA = await request(app).post('/api/auth/register').send(userAPayload);
    tokenA = resA.body.accessToken;
    userAId = resA.body.user.id;

    const resB = await request(app).post('/api/auth/register').send(userBPayload);
    tokenB = resB.body.accessToken;
    userBId = resB.body.user.id;
  });

  test('Property 7: Protected endpoints reject requests without valid JWT', async () => {
    const endpoints = [
      '/api/users/me',
      '/api/cashflow/current',
      '/api/transactions',
      '/api/envelopes',
      '/api/prosperity',
      '/api/kametis',
      '/api/goals',
      '/api/debts',
      '/api/bills',
      '/api/emergency-locker',
      '/api/orders',
      '/api/notifications',
      '/api/coach/messages'
    ];

    for (const ep of endpoints) {
      // No header
      const resNoHeader = await request(app).get(ep);
      expect(resNoHeader.status).toBe(401);

      // Malformed header
      const resBadHeader = await request(app)
        .get(ep)
        .set('Authorization', 'Bearer invalid.token.structure');
      expect(resBadHeader.status).toBe(401);
    }
  });

  test('Property 9: Cash flow upsert round-trip', async () => {
    const payload = {
      income: 65000,
      incomeLabel: 'Overtime + Basic Salary',
      expenses: 45000,
      expensesLabel: 'Ration, School Fees, Rent',
      savings: 8000,
      available: 12000
    };

    const putRes = await request(app)
      .put('/api/cashflow/current')
      .set('Authorization', `Bearer ${tokenA}`)
      .send(payload);

    expect(putRes.status).toBe(200);
    expect(putRes.body.income).toBe(payload.income);
    expect(putRes.body.expenses).toBe(payload.expenses);

    const getRes = await request(app)
      .get('/api/cashflow/current')
      .set('Authorization', `Bearer ${tokenA}`);

    expect(getRes.status).toBe(200);
    expect(getRes.body.income).toBe(payload.income);
    expect(getRes.body.expenses).toBe(payload.expenses);
    expect(getRes.body.savings).toBe(payload.savings);
    expect(getRes.body.available).toBe(payload.available);
  });

  test('Property 10: Transaction month filter returns only matching records', async () => {
    // Add transaction in March 2025
    await request(app)
      .post('/api/transactions')
      .set('Authorization', `Bearer ${tokenA}`)
      .send({
        title: 'March Ration',
        amount: 5000,
        isExpense: true,
        category: 'Needs',
        date: '2025-03-15T10:00:00.000Z'
      });

    // Add transaction in April 2025
    await request(app)
      .post('/api/transactions')
      .set('Authorization', `Bearer ${tokenA}`)
      .send({
        title: 'April Rent',
        amount: 15000,
        isExpense: true,
        category: 'Needs',
        date: '2025-04-05T10:00:00.000Z'
      });

    // Filter by March 2025
    const marchRes = await request(app)
      .get('/api/transactions?month=2025-03')
      .set('Authorization', `Bearer ${tokenA}`);

    expect(marchRes.status).toBe(200);
    expect(marchRes.body.items.length).toBe(1);
    expect(marchRes.body.items[0].title).toBe('March Ration');

    // Filter by April 2025
    const aprilRes = await request(app)
      .get('/api/transactions?month=2025-04')
      .set('Authorization', `Bearer ${tokenA}`);

    expect(aprilRes.status).toBe(200);
    expect(aprilRes.body.items.length).toBe(1);
    expect(aprilRes.body.items[0].title).toBe('April Rent');
  });

  test('Property 12: Goal contribution invariant', async () => {
    const goalRes = await request(app)
      .post('/api/goals')
      .set('Authorization', `Bearer ${tokenA}`)
      .send({
        title: 'New Sewing Machine',
        targetAmount: 20000,
        currentAmount: 5000
      });

    expect(goalRes.status).toBe(201);
    const goalId = goalRes.body.id;
    expect(goalRes.body.isCompleted).toBe(false);

    // Contribution 1: 5000 -> 10000
    const contrib1 = await request(app)
      .put(`/api/goals/${goalId}/contribute`)
      .set('Authorization', `Bearer ${tokenA}`)
      .send({ amount: 5000 });

    expect(contrib1.status).toBe(200);
    expect(contrib1.body.currentAmount).toBe(10000);
    expect(contrib1.body.isCompleted).toBe(false);

    // Contribution 2: 10000 -> 20000 (reaches target, isCompleted = true)
    const contrib2 = await request(app)
      .put(`/api/goals/${goalId}/contribute`)
      .set('Authorization', `Bearer ${tokenA}`)
      .send({ amount: 10000 });

    expect(contrib2.status).toBe(200);
    expect(contrib2.body.currentAmount).toBe(20000);
    expect(contrib2.body.isCompleted).toBe(true);
  });

  test('Property 13: Debt repayment reduces remaining amount exactly', async () => {
    const debtRes = await request(app)
      .post('/api/debts')
      .set('Authorization', `Bearer ${tokenA}`)
      .send({
        creditorName: 'Shopkeeper Karim',
        totalAmount: 10000,
        remainingAmount: 10000
      });

    const debtId = debtRes.body.id;

    // Valid repayment
    const repayRes = await request(app)
      .put(`/api/debts/${debtId}/repay`)
      .set('Authorization', `Bearer ${tokenA}`)
      .send({ amount: 4000 });

    expect(repayRes.status).toBe(200);
    expect(repayRes.body.remainingAmount).toBe(6000);

    // Excessive repayment (> remaining) -> rejected with 400, balance unchanged
    const excessiveRes = await request(app)
      .put(`/api/debts/${debtId}/repay`)
      .set('Authorization', `Bearer ${tokenA}`)
      .send({ amount: 7000 });

    expect(excessiveRes.status).toBe(400);
    expect(excessiveRes.body.error).toContain('Repayment exceeds remaining balance');

    // Verify balance was unchanged
    const debtsList = await request(app)
      .get('/api/debts')
      .set('Authorization', `Bearer ${tokenA}`);
    const debtItem = debtsList.body.find(d => d.id === debtId);
    expect(debtItem.remainingAmount).toBe(6000);
  });

  test('Property 14: Emergency locker balance is always non-negative', async () => {
    // Deposit 5000
    const depRes = await request(app)
      .post('/api/emergency-locker/deposit')
      .set('Authorization', `Bearer ${tokenA}`)
      .send({ amount: 5000 });

    expect(depRes.status).toBe(200);
    expect(depRes.body.balance).toBe(5000);

    // Withdraw 2000 -> 3000
    const withRes = await request(app)
      .post('/api/emergency-locker/withdraw')
      .set('Authorization', `Bearer ${tokenA}`)
      .send({ amount: 2000 });

    expect(withRes.status).toBe(200);
    expect(withRes.body.balance).toBe(3000);

    // Excessive withdrawal (5000 > 3000) -> 400 rejected, balance unchanged
    const badWithdraw = await request(app)
      .post('/api/emergency-locker/withdraw')
      .set('Authorization', `Bearer ${tokenA}`)
      .send({ amount: 5000 });

    expect(badWithdraw.status).toBe(400);
    expect(badWithdraw.body.error).toContain('Insufficient balance');

    // Balance remains 3000
    const checkRes = await request(app)
      .get('/api/emergency-locker')
      .set('Authorization', `Bearer ${tokenA}`);
    expect(checkRes.body.balance).toBe(3000);
  });

  test('Property 15: Prosperity Score bounded in [0, 100]', async () => {
    // Set cashflow
    await request(app)
      .put('/api/cashflow/current')
      .set('Authorization', `Bearer ${tokenA}`)
      .send({ income: 55000, expenses: 35000 });

    // Deposit to locker
    await request(app)
      .post('/api/emergency-locker/deposit')
      .set('Authorization', `Bearer ${tokenA}`)
      .send({ amount: 15000 });

    const scoreRes = await request(app)
      .get('/api/prosperity')
      .set('Authorization', `Bearer ${tokenA}`);

    expect(scoreRes.status).toBe(200);
    expect(scoreRes.body.score).toBeGreaterThanOrEqual(0);
    expect(scoreRes.body.score).toBeLessThanOrEqual(100);
    expect(scoreRes.body.pillars.length).toBe(6);
  });

  test('Property 8 & Task 10: Per-user data isolation — no cross-user data leakage', async () => {
    // User A creates a transaction, a kameti, a goal, a debt, and an order
    await request(app)
      .post('/api/transactions')
      .set('Authorization', `Bearer ${tokenA}`)
      .send({ title: 'User A Secret Expense', amount: 9999, isExpense: true, category: 'Needs' });

    await request(app)
      .post('/api/kametis')
      .set('Authorization', `Bearer ${tokenA}`)
      .send({ name: 'User A Committee', monthlyAmount: 3000, totalMembers: 5, myTurnMonth: 2 });

    await request(app)
      .post('/api/goals')
      .set('Authorization', `Bearer ${tokenA}`)
      .send({ title: 'User A Private Goal', targetAmount: 50000 });

    await request(app)
      .post('/api/debts')
      .set('Authorization', `Bearer ${tokenA}`)
      .send({ creditorName: 'User A Creditor', totalAmount: 12000 });

    await request(app)
      .post('/api/orders')
      .set('Authorization', `Bearer ${tokenA}`)
      .send({ customerName: 'User A Customer', serviceTitle: 'Stitching Dress', totalAmount: 4000 });

    // User B fetches all collections: MUST NOT see any records created by User A
    const bTransactions = await request(app)
      .get('/api/transactions')
      .set('Authorization', `Bearer ${tokenB}`);
    expect(bTransactions.body.items.some(i => i.title === 'User A Secret Expense')).toBe(false);

    const bKametis = await request(app)
      .get('/api/kametis')
      .set('Authorization', `Bearer ${tokenB}`);
    expect(bKametis.body.some(k => k.name === 'User A Committee')).toBe(false);

    const bGoals = await request(app)
      .get('/api/goals')
      .set('Authorization', `Bearer ${tokenB}`);
    expect(bGoals.body.some(g => g.title === 'User A Private Goal')).toBe(false);

    const bDebts = await request(app)
      .get('/api/debts')
      .set('Authorization', `Bearer ${tokenB}`);
    expect(bDebts.body.some(d => d.creditorName === 'User A Creditor')).toBe(false);

    const bOrders = await request(app)
      .get('/api/orders')
      .set('Authorization', `Bearer ${tokenB}`);
    expect(bOrders.body.some(o => o.customerName === 'User A Customer')).toBe(false);
  });
});
