# Implementation Plan: Khushhaal Backend Integration

## Overview

Is plan mein Node.js + Express + MongoDB backend banana, Android mein JWT auth + Repository pattern implement karna, Login/Registration screens banana, aur `KhushhaalViewModel` ko refactor karna shaamil hai taake sab 13 data entities real per-user dynamic data use karein.

Implementation do parallel tracks mein hogi: **Backend (Node.js)** aur **Android (Kotlin)**. Backend pehle complete hogi taake Android ke paas real endpoints available hon testing ke liye.

---

## Tasks

- [ ] 1. Backend: Project scaffold, configuration, aur database connection
  - [ ] 1.1 `backend/` folder mein Node.js + Express project initialize karo
    - `package.json` create karo exact versions ke saath (design.md Dependencies section dekho)
    - `backend/src/config/env.js`, `backend/src/config/db.js`, aur `backend/src/app.js` create karo
    - `.env` aur `.env.example` files create karo (`JWT_SECRET`, `REFRESH_SECRET`, `MONGODB_URI`, `PORT`, `FIREBASE_PROJECT_ID`)
    - `backend/src/app.js` mein `helmet`, `cors`, `express.json()` middleware setup karo
    - Express router structure create karo: `routes/`, `controllers/`, `services/`, `models/`, `middleware/`
    - _Requirements: 21.1, 21.2, 21.3_

  - [ ] 1.2 `GET /api/health` endpoint implement karo
    - MongoDB connection status check karo aur `{ status, db }` return karo
    - Mongoose connected → HTTP 200; disconnected → HTTP 503
    - _Requirements: 21.1, 21.2, 21.3_

- [ ] 2. Backend: Mongoose models (sab 13 schemas)
  - [ ] 2.1 User, CashFlow, Transaction, aur Envelope Mongoose schemas create karo
    - `backend/src/models/` mein `User.js`, `CashFlow.js`, `Transaction.js`, `Envelope.js` files create karo
    - Design document ke exact field definitions aur indexes use karo
    - CNIC regex validation aur phone regex validation User schema mein add karo
    - _Requirements: 1.2, 1.3, 1.7, 6.1, 7.3, 8.1_

  - [ ] 2.2 Kameti, Goal, Debt, Bill, Order, Locker, Notification, CoachMessage, aur Prosperity Mongoose schemas create karo
    - `backend/src/models/` mein baaki 9 model files create karo
    - Design document ke exact field definitions aur indexes use karo
    - `EmergencyLocker` schema mein `min: 0` constraint ensure karo
    - _Requirements: 9.2, 10.2, 11.2, 12.2, 13.1, 14.2, 15.1, 16.1, 17.1_

- [ ] 3. Backend: Auth system (Registration, Login, Token Refresh, Logout)
  - [ ] 3.1 `POST /api/auth/register` implement karo
    - `express-validator` se CNIC format (`^\d{5}-\d{7}-\d$`), Pakistani phone (`^\+92\d{10}$`), aur password (min 8 chars) validate karo
    - Duplicate phone/CNIC check karo (HTTP 409 bilingual messages ke saath)
    - bcrypt (12 rounds) se password hash karo
    - JWT access token (15m) aur refresh token (30d) generate karo
    - `seedUserDefaults(userId)` call karo (Task 3.3 mein implement)
    - HTTP 201 + `AuthResponse` return karo
    - _Requirements: 1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8_

  - [ ] 3.2 `POST /api/auth/login` implement karo
    - Phone lookup + bcrypt.compare karo
    - Credentials mismatch → HTTP 401 bilingual message
    - Success → access + refresh tokens generate karo, `refreshTokenHash` + `lastLoginAt` update karo
    - `express-rate-limit` se 5 attempts / 15 min / IP rate limiting add karo
    - HTTP 200 + `AuthResponse` return karo
    - _Requirements: 2.1, 2.2, 2.3, 2.4, 2.5_

  - [ ] 3.3 `seedUserDefaults(userId)` service function implement karo
    - 4 default envelopes insert karo (needs 70%, commitments 6%, emergency 6%, savings 18%)
    - Empty emergency locker create karo (balance = 0)
    - Prosperity score document create karo (score = 0)
    - Welcome coach message create karo (Urdu text, isFromCoach = true)
    - Verification assertion: exactly 4 envelopes + locker document exist
    - _Requirements: 1.8, 20.1, 20.2, 20.3, 20.4, 20.5_

  - [ ]* 3.4 Property test: Registration validation (Property 1)
    - **Property 1: Registration input validation rejects malformed identifiers**
    - fast-check se arbitrary strings generate karo jo CNIC/phone format match nahi karte
    - Verify karein ke sab invalid inputs HTTP 400 return karte hain
    - **Validates: Requirements 1.2, 1.3, 1.4**

  - [ ]* 3.5 Property test: Password stored as bcrypt hash (Property 2)
    - **Property 2: Password stored as bcrypt hash, never plaintext**
    - fast-check se arbitrary passwords (length ≥ 8) generate karo
    - Verify: `bcrypt.compare(p, hash) = true` AND `hash ≠ p`
    - **Validates: Requirements 1.7, 19.3**

  - [ ]* 3.6 Property test: New user seeding creates required default records (Property 3)
    - **Property 3: New user registration seeds required default records**
    - fast-check se valid registration payloads generate karo
    - Verify: exactly 4 envelopes, 1 locker (balance=0), 1 prosperity (score=0), 1 coach message (isFromCoach=true)
    - **Validates: Requirements 1.8, 20.1, 20.2, 20.3, 20.4, 20.5**

  - [ ]* 3.7 Property test: Login round-trip after registration (Property 4)
    - **Property 4: Login round-trip — register then login succeeds**
    - fast-check se valid credentials generate karo, register karo, phir same credentials se login karo
    - Verify: valid access token, refresh token, aur user profile DTO return hota hai
    - **Validates: Requirements 2.1, 2.4**

  - [ ]* 3.8 Property test: Wrong password always rejected (Property 5)
    - **Property 5: Login with wrong password is always rejected**
    - fast-check se wrong passwords generate karo
    - Verify: HTTP 401 + bilingual error message
    - **Validates: Requirements 2.2, 2.3**

  - [ ] 3.9 `POST /api/auth/refresh` aur `POST /api/auth/logout` implement karo
    - Refresh: stored `refreshTokenHash` se verify karo, naya token pair return karo, old hash invalidate karo
    - Invalid/expired refresh token → HTTP 401
    - Logout: `refreshTokenHash` clear karo, HTTP 200 return karo
    - _Requirements: 3.2, 3.3, 3.5_

  - [ ]* 3.10 Property test: Refresh token rotation invalidates old token (Property 6)
    - **Property 6: Refresh token rotation invalidates old token**
    - Register + login karke valid refresh token lo
    - First refresh call → naya token pair
    - Second call same old token se → HTTP 401
    - **Validates: Requirements 3.2, 3.3**

- [ ] 4. Backend: JWT auth middleware aur protected routes
  - [ ] 4.1 `backend/src/middleware/auth.js` implement karo
    - `Authorization: Bearer <token>` header parse karo
    - Valid token → `req.userId` set karo, `next()` call karo
    - Missing/malformed → HTTP 401 `{ error: "Unauthorized" }`
    - Expired → HTTP 401 `{ error: "Token expired", code: "TOKEN_EXPIRED" }`
    - _Requirements: 4.1, 4.2, 4.3, 4.4_

  - [ ]* 4.2 Property test: Protected endpoints reject invalid JWT (Property 7)
    - **Property 7: Protected endpoints reject requests without valid JWT**
    - fast-check se arbitrary strings (no valid JWT format) generate karo
    - Sab protected paths pe test karo
    - Verify: HTTP 401 return hota hai
    - **Validates: Requirements 4.1, 4.3, 4.4**

- [ ] 5. Backend: User profile aur CashFlow endpoints
  - [ ] 5.1 `GET /api/users/me` aur `PUT /api/users/me` implement karo
    - GET: CNIC masked format mein return karo (`42101-•••••••-3`)
    - PUT: name, factory, jazzCashNumber, preferredLanguage, avatarUrl update karo; CNIC update forbidden
    - Invalid `preferredLanguage` → HTTP 400
    - _Requirements: 5.1, 5.2, 5.3_

  - [ ] 5.2 `GET /api/cashflow/current` aur `PUT /api/cashflow/current` implement karo
    - GET: current month (`YYYY-MM`) ke liye record fetch karo; agar nahi hai toh zero-value default return karo
    - PUT: upsert karo, phir `calculateProsperityScore(userId)` trigger karo
    - _Requirements: 6.1, 6.2, 6.3, 6.4_

- [ ] 6. Backend: Transactions aur Envelopes endpoints
  - [ ] 6.1 `GET /api/transactions` aur `POST /api/transactions` implement karo
    - GET: paginated list (default 20), `month` query filter, date descending sort
    - POST: title + amount (≥1) validate karo; HTTP 400 agar invalid; HTTP 201 on success
    - Sab queries mein `userId: req.userId` filter ensure karo
    - _Requirements: 7.1, 7.2, 7.3, 7.4_

  - [ ]* 6.2 Property test: Transaction month filter (Property 10)
    - **Property 10: Transaction month filter returns only matching records**
    - fast-check se multiple months mein transactions generate karo
    - Verify: `?month=YYYY-MM` sirf us month ke records return karta hai
    - **Validates: Requirements 7.1, 7.2**

  - [ ] 6.3 `GET /api/envelopes` aur `PUT /api/envelopes/{id}` implement karo
    - GET: authenticated user ke sab 4 envelopes return karo
    - PUT: percentage (0–100) aur amount validate karo; duplicate `(userId, envelopeKey)` check karo
    - _Requirements: 8.1, 8.2, 8.5_

- [ ] 7. Backend: Prosperity Score calculator
  - [ ] 7.1 `backend/src/services/prosperityCalculator.js` implement karo
    - Design document ka exact algorithm implement karo (6 pillars: Budgeting 20, Savings Habit 20, Emergency Buffer 20, Debt Control 15, Digital Safety 10, Income Resilience 15)
    - `daysRunway = floor(locker.balance / (expenses / 30))`; `expenses = 0` → max Emergency Buffer score (20)
    - `cashFlow.income = 0` ya no record → total score = 0
    - Result database mein upsert karo
    - _Requirements: 15.1, 15.2, 15.3, 15.4, 15.5_

  - [ ] 7.2 `GET /api/prosperity` endpoint implement karo
    - Authenticated user ka prosperity record return karo
    - _Requirements: 15.1_

  - [ ]* 7.3 Property test: Prosperity Score bounded in [0, 100] (Property 15)
    - **Property 15: Prosperity Score is bounded in [0, 100]**
    - fast-check se arbitrary financial data combinations generate karo (koi bhi income, expenses, locker balance, debts)
    - Verify: `0 ≤ score ≤ 100` hamesha
    - **Validates: Requirements 15.2, 15.5**

- [ ] 8. Backend: Kameti, Goals, Debts, Bills endpoints
  - [ ] 8.1 Kameti CRUD implement karo (`GET /api/kametis`, `POST /api/kametis`, `PUT /api/kametis/{id}/pay`)
    - monthlyAmount ≥ 1, totalMembers ≥ 2 validate karo
    - `/pay` endpoint: `isPaidThisMonth = true` set karo, prosperity recalculate karo
    - Ownership check: dusre user ka kameti access → HTTP 404
    - _Requirements: 9.1, 9.2, 9.3, 9.4, 9.5_

  - [ ] 8.2 Goals CRUD implement karo (`GET /api/goals`, `POST /api/goals`, `PUT /api/goals/{id}/contribute`)
    - targetAmount ≥ 1, contribution > 0 validate karo
    - Contribution: `currentAmount += amount`, `isCompleted = currentAmount >= targetAmount`
    - Prosperity recalculate karo
    - _Requirements: 10.1, 10.2, 10.3, 10.4, 10.6_

  - [ ]* 8.3 Property test: Goal contribution invariant (Property 12)
    - **Property 12: Goal contribution invariant**
    - fast-check se arbitrary goals aur contribution amounts generate karo
    - Verify: `currentAmount(after) = before + a` AND `isCompleted = (after >= target)`
    - **Validates: Requirements 10.3, 10.4, 10.5**

  - [ ] 8.4 Debts CRUD implement karo (`GET /api/debts`, `POST /api/debts`, `PUT /api/debts/{id}/repay`)
    - totalAmount ≥ 1, repayment > 0 validate karo
    - `a > remainingAmount` → HTTP 400 "Repayment exceeds remaining balance"
    - Prosperity recalculate karo
    - _Requirements: 11.1, 11.2, 11.3, 11.4, 11.5_

  - [ ]* 8.5 Property test: Debt repayment reduces amount exactly (Property 13)
    - **Property 13: Debt repayment reduces remaining amount exactly**
    - fast-check se arbitrary debts aur valid repayment amounts (`0 < a ≤ R`) generate karo
    - Verify: `remainingAmount(after) = R - a`; aur `a > R` → HTTP 400, unchanged
    - **Validates: Requirements 11.3, 11.4**

  - [ ] 8.6 Bills CRUD implement karo (`GET /api/bills`, `POST /api/bills`, `PUT /api/bills/{id}/pay`)
    - amount ≥ 1 validate karo
    - `/pay`: `isPaid = true`, `paidDate = now` set karo
    - Ownership check: HTTP 404 on unauthorized access
    - _Requirements: 12.1, 12.2, 12.3, 12.4_

- [ ] 9. Backend: Emergency Locker, Orders, Notifications, Coach endpoints
  - [ ] 9.1 Emergency Locker endpoints implement karo (`GET`, `POST /deposit`, `POST /withdraw`)
    - Deposit: `balance += amount`
    - Withdraw: `amount > balance` → HTTP 400 "Insufficient balance / رقم ناکافی ہے"; balance unchanged
    - `balance` hamesha non-negative ensure karo (min: 0 constraint)
    - Prosperity recalculate karo on deposit/withdrawal
    - _Requirements: 13.1, 13.2, 13.3, 13.4, 13.5, 13.6_

  - [ ]* 9.2 Property test: Emergency locker balance always non-negative (Property 14)
    - **Property 14: Emergency locker balance is always non-negative**
    - fast-check se arbitrary deposit/withdrawal sequences generate karo
    - Verify: `balance ≥ 0` hamesha; excessive withdrawal → HTTP 400, unchanged balance
    - **Validates: Requirements 13.2, 13.3, 13.4, 13.5**

  - [ ] 9.3 Business Khata (Orders) endpoints implement karo (`GET /api/orders`, `POST /api/orders`, `PUT /api/orders/{id}`)
    - totalAmount ≥ 1 validate karo
    - PUT: `isDelivered`, `isFullyPaid`, `advancePaid`, `dueDate` fields update karo
    - Ownership check: HTTP 404 on unauthorized access
    - _Requirements: 14.1, 14.2, 14.3, 14.4_

  - [ ] 9.4 Notifications endpoints implement karo (`GET /api/notifications`, `PUT /api/notifications/{id}/read`, `DELETE /api/notifications`)
    - GET: `createdAt` descending sort
    - PUT: `isRead = true` set karo
    - DELETE: authenticated user ke sab notifications delete karo
    - Ownership check: HTTP 404 on unauthorized access
    - _Requirements: 16.1, 16.2, 16.3, 16.4_

  - [ ] 9.5 Coach Messages endpoints implement karo (`GET /api/coach/messages`, `POST /api/coach/messages`)
    - GET: `createdAt` ascending sort
    - POST: empty/whitespace-only message → HTTP 400 (auth-independent)
    - User message persist karo, Firebase AI ko financial context ke saath relay karo, AI response persist karo
    - _Requirements: 17.1, 17.2, 17.3, 17.4_

- [ ] 10. Backend: Data isolation checkpoint
  - [ ] 10.1 Supertest integration tests likhو — data isolation verify karo
    - 2 test users register karo, dono se identical records create karo
    - Verify: user A ka GET user B ke records return nahi karta
    - _Requirements: 19.1, 19.2, 19.6_

  - [ ]* 10.2 Property test: Per-user data isolation (Property 8)
    - **Property 8: Per-user data isolation — no cross-user data leakage**
    - fast-check se 2 users + arbitrary data create karo
    - Sab collection endpoints pe verify karo: sirf apna data return hota hai
    - **Validates: Requirements 4.5, 19.1, 19.2, 19.6**

- [ ] 11. Backend Checkpoint — Ensure all tests pass
  - Ensure all Jest + Supertest tests pass, ask the user if questions arise.

- [ ] 12. Android: Security crypto dependency aur project setup
  - [ ] 12.1 `app/build.gradle.kts` mein `androidx.security.crypto` aur `androidx.datastore.preferences` dependencies add karo
    - Exact pinned versions use karo (`1.1.0-alpha06` for security-crypto, `1.1.1` for datastore)
    - KSP annotations Room ke liye already configured hain — ensure Room dependency active hai
    - `app/src/main/java/com/example/` mein `data/` package structure create karo: `api/`, `local/`, `repository/`
    - _Requirements: 18.1_

- [ ] 13. Android: Network layer (Retrofit, OkHttp, TokenManager)
  - [ ] 13.1 `TokenManager.kt` implement karo (`EncryptedSharedPreferences` use karke)
    - Access token, refresh token store/retrieve/clear karo
    - `SessionExpired` event emit karo (SharedFlow) jab tokens clear hon
    - _Requirements: 3.3, 3.4_

  - [ ] 13.2 `AuthInterceptor.kt` (OkHttp Interceptor) implement karo
    - Sab requests mein `Authorization: Bearer <token>` inject karo
    - 401 `TOKEN_EXPIRED` → `POST /api/auth/refresh` call karo, original request retry karo
    - Refresh fail → `TokenManager.clearTokens()` + `SessionExpired` emit
    - Auth endpoints (`/auth/login`, `/auth/register`) skip karo
    - _Requirements: 3.1, 3.3, 3.4, 18.5_

  - [ ] 13.3 `RetrofitClient.kt` + `AuthApiService.kt` + `KhushhaalApiService.kt` implement karo
    - Design document ke exact Retrofit interface definitions use karo (Component 1 aur 2)
    - `AuthInterceptor` + `HttpLoggingInterceptor` OkHttp mein wire karo
    - Moshi converter configure karo
    - Base URL `.env` / `BuildConfig` se load karo
    - _Requirements: 1.1, 2.1, 4.1_

- [ ] 14. Android: Room database (local cache)
  - [ ] 14.1 Room entities create karo (sab 13 data models)
    - `data/local/entities/` mein Room `@Entity` classes create karo: `UserEntity`, `TransactionEntity`, `EnvelopeEntity`, `KametiEntity`, `GoalEntity`, `DebtEntity`, `BillEntity`, `LockerEntity`, `OrderEntity`, `NotificationEntity`, `CoachMessageEntity`, `CashFlowEntity`, `ProsperityEntity`
    - Existing `DataModels.kt` models se map karo (DTO ↔ Entity conversions)
    - _Requirements: 18.1, 18.2_

  - [ ] 14.2 Room DAOs create karo (sab collections ke liye)
    - `data/local/dao/` mein separate DAO interfaces create karo har entity ke liye
    - `Flow<List<T>>` return types use karo reactive cache ke liye
    - `@Query`, `@Insert(onConflict = REPLACE)`, `@Delete` annotate karo
    - _Requirements: 18.1, 18.2, 18.3_

  - [ ] 14.3 `KhushhaalDatabase.kt` (Room database class) create karo
    - Sab 13 entities aur DAOs register karo
    - Singleton pattern use karo
    - _Requirements: 18.1_

- [ ] 15. Android: Repository layer
  - [ ] 15.1 `KhushhaalRepository` interface implement karo — `TransactionRepository` aur `EnvelopeRepository`
    - Design document ka cache-first algorithm implement karo (Component 3 pseudocode)
    - `Flow<Result<T>>`: pehle Room emit karo, phir API fetch karo, Room update karo, fresh data emit karo
    - `IOException` → agar cache empty hai toh `Result.Error("انٹرنیٹ کنیکشن نہیں / No internet connection")` emit karo
    - _Requirements: 7.5, 8.4, 18.1, 18.2, 18.3, 18.4_

  - [ ]* 15.2 Property test: Repository cache-first emission ordering (Property 16)
    - **Property 16: Repository cache-first emission ordering**
    - MockK se API responses mock karo
    - fast-check / kotest se arbitrary non-empty Room caches generate karo
    - Verify: cached data API call se pehle emit hota hai
    - **Validates: Requirements 18.1, 18.2, 18.3, 18.4**

  - [ ] 15.3 Baaki repositories implement karo — `GoalRepository`, `KametiRepository`, `DebtRepository`, `BillRepository`, `LockerRepository`, `OrderRepository`, `NotificationRepository`, `CoachRepository`, `CashFlowRepository`, `ProsperityRepository`, `UserRepository`
    - Same cache-first pattern follow karo
    - Mutation operations (`suspend fun`) result return karen
    - _Requirements: 9.1, 10.1, 11.1, 12.1, 13.1, 14.1, 15.1, 16.1, 17.1, 18.1_

- [ ] 16. Android: Auth screens (Login aur Registration)
  - [ ] 16.1 `LoginScreen.kt` Composable create karo
    - Phone number + password fields, "لاگ ان کریں / Login" button
    - `AuthViewModel` (naya dedicated ViewModel) se connect karo — `loginUseCase` call karo
    - Error states: bilingual messages show karo (wrong credentials, network error)
    - `SessionExpired` event observe karo → is screen par navigate karo
    - _Requirements: 2.1, 2.2, 2.3, 3.4_

  - [ ] 16.2 `RegistrationScreen.kt` Composable create karo
    - Fields: name, CNIC, phone, factory, factoryId, jazzCashNumber, password, preferredLanguage
    - Inline validation: CNIC format, phone format, password length
    - Duplicate phone/CNIC error → HTTP 409 bilingual message show karo
    - Success → Home screen navigate karo
    - _Requirements: 1.1, 1.2, 1.3, 1.4, 1.5, 1.6_

  - [ ] 16.3 `AuthViewModel.kt` implement karo
    - `login(phone, password)` aur `register(RegisterRequest)` suspend functions
    - `TokenManager` mein tokens store karo on success
    - Loading, Error, Success UI states manage karo
    - _Requirements: 1.1, 2.1_

  - [ ] 16.4 `MainActivity.kt` mein navigation logic update karo
    - Token present check karo → Home screen; no token → Login screen
    - `SessionExpired` event observe karo → Login screen navigate karo aur back stack clear karo
    - _Requirements: 3.4_

- [ ] 17. Android: KhushhaalViewModel refactor
  - [ ] 17.1 `KhushhaalViewModel` ko refactor karo — hardcoded data replace karo repositories se
    - Constructor mein sab repositories inject karo (Hilt/manual DI — existing project pattern follow karo)
    - Sab `_stateFlow` private mutable fields repository `Flow` se collect karo
    - `loadUserProfile()`, `loadCashFlow()`, `loadProsperity()` functions add karo
    - _Requirements: 5.4, 6.1, 15.1_

  - [ ] 17.2 Transaction, Envelope, Kameti, Goals actions wire karo
    - `logQuickExpense()` → `transactionRepository.addTransaction()`
    - `markKametiPaid()` → `kametiRepository.markKametiPaid()`
    - `contributeToGoal()` → `goalRepository.contributeToGoal()`
    - `updateEnvelope()` → `envelopeRepository.updateEnvelope()`
    - _Requirements: 7.3, 8.2, 9.3, 10.3_

  - [ ] 17.3 Debt, Bills, Locker, Orders actions wire karo
    - `repayDebt()` → `debtRepository.repayDebt()`
    - `markBillPaid()` → `billRepository.markBillPaid()`
    - `depositToLocker()` / `withdrawFromLocker()` → `lockerRepository.*`
    - `updateOrder()` → `orderRepository.updateOrder()`
    - _Requirements: 11.3, 12.3, 13.2, 13.3, 14.3_

  - [ ] 17.4 Notifications aur Coach Chat wire karo
    - `loadNotifications()` → `notificationRepository.getNotifications()`
    - `markNotificationRead()` / `clearAllNotifications()` implement karo
    - `sendCoachMessage()` → `coachRepository.sendMessage()`
    - _Requirements: 16.1, 16.2, 16.3, 17.1, 17.2_

- [ ] 18. Android: Envelope amount calculation
  - [ ] 18.1 `calculateEnvelopeAmounts()` utility function implement karo
    - `amount = floor(totalIncome × percentage / 100)`
    - `totalIncome = 0` → sab amounts = 0
    - Sum of amounts ≤ totalIncome ensure karo
    - _Requirements: 8.4_

  - [ ]* 18.2 Property test: Envelope amounts never exceed total income (Property 11)
    - **Property 11: Envelope amounts never exceed total income**
    - kotest se arbitrary `totalIncome ≥ 0` aur percentages summing to 100 generate karo
    - Verify: `sum(amounts) ≤ totalIncome`; `income = 0` → all amounts = 0
    - **Validates: Requirements 8.3, 8.4**

- [ ] 19. Android: Profile Settings screen wiring
  - [ ] 19.1 `ProfileSettingsScreen.kt` ko update karo real API se
    - `GET /api/users/me` se load karo, masked CNIC display karo
    - `PUT /api/users/me` se update karo (name, factory, jazzCash, language)
    - _Requirements: 5.1, 5.2, 5.3, 5.4_

- [ ] 20. Integration checkpoint — Android + Backend end-to-end
  - [ ] 20.1 MockWebServer se Android integration tests likhو
    - Auth flow test karo: Register → Login → protected endpoint access
    - Token refresh flow test karo: expired token → auto-refresh → retry
    - Session expired flow test karo: invalid refresh → Login screen navigate
    - _Requirements: 3.1, 3.2, 3.3, 3.4_

  - [ ]* 20.2 Property test: CashFlow upsert round-trip (Property 9)
    - **Property 9: Cash flow upsert round-trip**
    - fast-check se arbitrary valid cash flow values generate karo
    - PUT karo, phir GET karo; verify: returned values match PUT values
    - **Validates: Requirements 6.1, 6.2, 6.3**

- [ ] 21. Final Checkpoint — Ensure all tests pass
  - Ensure all backend Jest tests, Android unit tests, aur integration tests pass. Ask the user if questions arise.

---

## Notes

- Tasks marked with `*` are optional aur MVP ke liye skip kiye ja sakte hain
- Backend aur Android tasks parallel mein develop ho sakti hain (Tasks 1–11 backend, Tasks 12–20 Android)
- Sab tasks specific requirements reference karte hain traceability ke liye
- Property tests fast-check (backend) aur kotest (Android) libraries use karte hain
- Checkpoints incremental validation ensure karte hain
- `KhushhaalViewModel` mein hardcoded "Ahmed Bhai" data tab tak rahega jab tak repositories wire nahi ho jaate (Task 17)

## Task Dependency Graph

```json
{
  "waves": [
    { "id": 0, "tasks": ["1.1"] },
    { "id": 1, "tasks": ["1.2", "2.1", "2.2"] },
    { "id": 2, "tasks": ["3.1", "3.2", "3.3"] },
    { "id": 3, "tasks": ["3.4", "3.5", "3.6", "3.7", "3.8", "3.9", "4.1", "5.1", "5.2"] },
    { "id": 4, "tasks": ["3.10", "4.2", "6.1", "6.3", "7.1", "8.1", "8.2", "8.4", "8.6", "9.1", "9.3", "9.4", "9.5"] },
    { "id": 5, "tasks": ["6.2", "7.2", "7.3", "8.3", "8.5", "9.2", "10.1"] },
    { "id": 6, "tasks": ["10.2", "12.1"] },
    { "id": 7, "tasks": ["13.1", "13.2", "14.1"] },
    { "id": 8, "tasks": ["13.3", "14.2", "14.3"] },
    { "id": 9, "tasks": ["15.1", "16.1", "16.2", "16.3", "18.1"] },
    { "id": 10, "tasks": ["15.2", "15.3", "16.4", "18.2"] },
    { "id": 11, "tasks": ["17.1", "19.1"] },
    { "id": 12, "tasks": ["17.2", "17.3", "17.4"] },
    { "id": 13, "tasks": ["20.1", "20.2"] }
  ]
}
```
