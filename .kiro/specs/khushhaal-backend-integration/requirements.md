# Requirements Document

## Introduction

Khushhaal ek bilingual (Urdu/English) financial wellness app hai jo Pakistani factory workers ke liye design ki gayi hai. Is waqt Android app 100% static mockup hai — sab data `KhushhaalViewModel` mein hardcoded hai. Is feature ka maqsad ek complete backend system banana hai: Node.js + Express REST API with MongoDB Atlas database, JWT-based authentication, aur Android ke existing lekin unused Retrofit/OkHttp/Room infrastructure ko activate karna, taake har registered factory worker ka apna unique, personalized, aur persistent financial data ho.

Is integration ke baad app ka har screen — Home, Money, Prosperity, Goals, Transactions, Envelopes, Debts, Bills, Emergency Locker, Business Khata, Coach Chat, Notifications — real per-user data dikhayega instead of hardcoded "Ahmed Bhai" data.

---

## Glossary

- **API_Server**: Node.js + Express backend server providing all REST endpoints
- **Auth_Service**: Authentication subsystem handling registration, login, JWT issuance, and token refresh
- **Repository**: Android-side data access layer bridging ViewModel with API and Room cache
- **Room_Cache**: Android local SQLite database (via Room) used for offline caching
- **Token_Manager**: Android component that stores, retrieves, and refreshes JWT tokens using EncryptedSharedPreferences
- **Auth_Interceptor**: OkHttp interceptor that injects JWT Bearer tokens into every protected API request and handles 401 refresh
- **Prosperity_Calculator**: Backend service that computes a user's Prosperity Score from their financial data
- **User**: A registered Pakistani factory worker using the Khushhaal app
- **CNIC**: Pakistani National Identity Card number in format `DDDDD-DDDDDDD-D`
- **JWT**: JSON Web Token used for stateless authentication (access token: 15 min, refresh token: 30 days)
- **Envelope**: A budget allocation bucket (Needs, Commitments, Emergency, Savings) with a percentage of income
- **Kameti**: A rotating savings committee common in Pakistani communities
- **Locker**: Emergency Locker — a per-user savings balance for emergency funds
- **Prosperity_Score**: A 0–100 composite score measuring a user's financial health across 6 pillars
- **Coach_Fatima**: AI financial coach powered by Firebase AI that responds to user messages
- **Seed_Data**: Default records (envelopes, locker, welcome message) created automatically on new user registration

---

## Requirements

### Requirement 1: User Registration

**User Story:** As a factory worker, I want to create a new account using my CNIC and phone number, so that I can access my personalized financial data securely.

#### Acceptance Criteria

1. WHEN a registration request is submitted with name, CNIC, phone, factory, factoryId, JazzCash number, password, and preferred language, THE Auth_Service SHALL create a new user account and return a JWT access token, refresh token, and user profile.
2. WHEN a registration request contains a CNIC that does not match the format `DDDDD-DDDDDDD-D`, THE Auth_Service SHALL reject the request with HTTP 400 and a bilingual error message.
3. WHEN a registration request contains a phone number that is not a valid Pakistani mobile number, THE Auth_Service SHALL reject the request with HTTP 400 and a bilingual error message.
4. WHEN a registration request contains a password shorter than 8 characters, THE Auth_Service SHALL reject the request with HTTP 400.
5. WHEN a registration request contains a phone number already registered in the database, THE Auth_Service SHALL reject the request with HTTP 409 and the bilingual message "Phone number already registered / نمبر پہلے سے رجسٹرڈ ہے".
6. WHEN a registration request contains a CNIC already registered in the database, THE Auth_Service SHALL reject the request with HTTP 409 and the bilingual message "CNIC already registered / شناختی کارڈ پہلے سے موجود ہے".
7. WHEN a new user account is successfully created, THE Auth_Service SHALL hash the password using bcrypt with 12 rounds before storing it.
8. WHEN a new user account is successfully created, THE Auth_Service SHALL invoke the Seed_Data algorithm to create 4 default envelopes, an empty emergency locker, an empty prosperity record, and a welcome coach message for that user.

---

### Requirement 2: User Login

**User Story:** As a registered factory worker, I want to log in using my phone number and password, so that I can access my account on any device.

#### Acceptance Criteria

1. WHEN a login request is submitted with a valid phone number and matching password, THE Auth_Service SHALL return a JWT access token (15-minute expiry), a refresh token (30-day expiry), and the user profile DTO.
2. WHEN a login request is submitted with a phone number not found in the database, THE Auth_Service SHALL return HTTP 401 with the bilingual message "غلط نمبر یا پاسورڈ / Invalid credentials".
3. WHEN a login request is submitted with a correct phone number but incorrect password, THE Auth_Service SHALL return HTTP 401 with the bilingual message "غلط نمبر یا پاسورڈ / Invalid credentials".
4. WHEN a login succeeds, THE Auth_Service SHALL store a bcrypt hash of the new refresh token in the user's database record and update the `lastLoginAt` timestamp.
5. WHEN a login request is submitted to the login endpoint more than 5 times within 15 minutes from the same IP address, THE API_Server SHALL return HTTP 429 (Too Many Requests).

---

### Requirement 3: Token Refresh and Session Management

**User Story:** As a logged-in user, I want my session to stay active without re-logging in, so that I do not have to enter my password repeatedly throughout the day.

#### Acceptance Criteria

1. WHEN the Auth_Interceptor receives an HTTP 401 response with error code "TOKEN_EXPIRED", THE Auth_Interceptor SHALL call `POST /api/auth/refresh` with the stored refresh token and retry the original request exactly once using the new access token.
2. WHEN a token refresh request is submitted with a valid, unexpired refresh token, THE Auth_Service SHALL return a new access token and a new refresh token, and SHALL invalidate the old refresh token by updating its hash in the database.
3. WHEN a token refresh request is submitted with an expired or invalid refresh token, THE Auth_Service SHALL return HTTP 401, and THE Token_Manager SHALL clear all stored tokens and emit a `SessionExpired` event.
4. WHEN a `SessionExpired` event is emitted, THE Repository SHALL navigate the user to the Login screen and display the Urdu message "سیشن ختم ہو گیا، دوبارہ لاگ ان کریں".
5. WHEN a logout request is submitted with a valid JWT, THE Auth_Service SHALL invalidate the user's stored refresh token hash and return HTTP 200.

---

### Requirement 4: JWT Authentication Middleware

**User Story:** As a system operator, I want all protected API endpoints to verify caller identity, so that no user can access another user's data.

#### Acceptance Criteria

1. THE API_Server SHALL protect all endpoints under `/api/` (except `/api/auth/login`, `/api/auth/register`, `/api/auth/refresh`, and `/api/health`) with JWT verification middleware.
2. WHEN a request arrives at a protected endpoint with a valid JWT in the `Authorization: Bearer <token>` header, THE API_Server SHALL set `req.userId` to the authenticated user's ObjectId and proceed to the route handler.
3. WHEN a request arrives at a protected endpoint without an `Authorization` header or with a malformed token, THE API_Server SHALL return HTTP 401 `{ "error": "Unauthorized" }`.
4. WHEN a request arrives at a protected endpoint with an expired access token, THE API_Server SHALL return HTTP 401 `{ "error": "Token expired", "code": "TOKEN_EXPIRED" }`.
5. WHEN a protected route handler returns data, THE API_Server SHALL filter all MongoDB queries by `userId: req.userId` so that responses contain only the authenticated user's records.

---

### Requirement 5: User Profile Management

**User Story:** As a registered user, I want to view and update my profile information, so that my name, factory, and language preference are always current.

#### Acceptance Criteria

1. WHEN an authenticated request is made to `GET /api/users/me`, THE API_Server SHALL return the authenticated user's profile DTO with CNIC displayed in masked format (`42101-•••••••-3`).
2. WHEN an authenticated request is made to `PUT /api/users/me` with valid fields (name, factory, jazzCashNumber, preferredLanguage, avatarUrl), THE API_Server SHALL update the user record and return the updated profile DTO. THE API_Server SHALL NOT allow the CNIC field to be updated via this endpoint.
3. WHEN a profile update request contains an invalid `preferredLanguage` value (not one of `BILINGUAL`, `URDU`, `ENGLISH`), THE API_Server SHALL return HTTP 400.
4. WHEN the Android app loads the Profile Settings screen, THE Repository SHALL fetch and display the latest user profile from `GET /api/users/me`.

---

### Requirement 6: Cash Flow Management

**User Story:** As a factory worker, I want to record my monthly income and track my expenses, so that I can see how much money I have available each month.

#### Acceptance Criteria

1. WHEN an authenticated request is made to `GET /api/cashflow/current`, THE API_Server SHALL return the cash flow record for the current month (`YYYY-MM`) for the authenticated user.
2. IF no cash flow record exists for the current month for the authenticated user, THEN THE API_Server SHALL return a default cash flow record with zero values.
3. WHEN an authenticated request is made to `PUT /api/cashflow/current` with valid income, expenses, savings, and available values, THE API_Server SHALL upsert the cash flow record for the current month and return the updated record.
4. WHEN a cash flow update is successfully saved, THE API_Server SHALL trigger a recalculation of the user's Prosperity Score within the same request cycle.

---

### Requirement 7: Transaction Management

**User Story:** As a factory worker, I want to log my income and expense transactions, so that I can track exactly where my money goes each month.

#### Acceptance Criteria

1. WHEN an authenticated request is made to `GET /api/transactions`, THE API_Server SHALL return a paginated list (default 20 per page) of transactions for the authenticated user, sorted by date descending.
2. WHEN `GET /api/transactions` is called with a `month` query parameter (format `YYYY-MM`), THE API_Server SHALL return only transactions within that calendar month.
3. WHEN an authenticated request is made to `POST /api/transactions` with title, amount (≥ 1), isExpense, category, and optional envelopeId and paymentMethod, THE API_Server SHALL create the transaction and return HTTP 201 with the created record.
4. WHEN a transaction creation request omits title or contains amount < 1, THE API_Server SHALL return HTTP 400.
5. WHEN the Android app loads the Transaction History screen, THE Repository SHALL emit cached transactions immediately and refresh from `GET /api/transactions` in the background.

---

### Requirement 8: Envelope (Budget) Management

**User Story:** As a factory worker, I want to split my salary into budget envelopes, so that I can plan my spending across Needs, Commitments, Emergency Fund, and Savings.

#### Acceptance Criteria

1. WHEN an authenticated request is made to `GET /api/envelopes`, THE API_Server SHALL return all 4 budget envelopes for the authenticated user.
2. WHEN an authenticated request is made to `PUT /api/envelopes/{id}` with valid percentage (0–100) and amount values, THE API_Server SHALL update the specified envelope and return the updated record.
3. WHEN a new user is registered, THE Auth_Service SHALL create 4 default envelopes with keys `needs` (70%), `commitments` (6%), `emergency` (6%), and `savings` (18%).
4. WHEN the Android app calculates envelope amounts, THE Repository SHALL compute each envelope's amount as `floor(totalIncome × percentage / 100)`, ensuring the sum of all envelope amounts does not exceed total income. WHEN `totalIncome` is 0, each envelope amount SHALL be 0.
5. WHEN envelope percentages are updated, THE API_Server SHALL validate that the update does not create a duplicate `(userId, envelopeKey)` combination.

---

### Requirement 9: Kameti (Rotating Savings Committee) Management

**User Story:** As a factory worker, I want to manage my rotating savings committees (Kameti), so that I can track my monthly payments and know when I will receive my payout.

#### Acceptance Criteria

1. WHEN an authenticated request is made to `GET /api/kametis`, THE API_Server SHALL return all Kameti records for the authenticated user.
2. WHEN an authenticated request is made to `POST /api/kametis` with name, monthlyAmount (≥ 1), totalMembers (≥ 2), myTurnMonth, and currentMonth, THE API_Server SHALL create the Kameti record and return HTTP 201.
3. WHEN an authenticated request is made to `PUT /api/kametis/{id}/pay`, THE API_Server SHALL set `isPaidThisMonth = true` for the specified Kameti and return the updated record.
4. WHILE the request is authenticated, IF a `PUT /api/kametis/{id}/pay` request references a Kameti that does not belong to the authenticated user, THEN THE API_Server SHALL return HTTP 404.
5. WHEN the Kameti payment status is updated, THE API_Server SHALL trigger a recalculation of the user's Prosperity Score.

---

### Requirement 10: Family Goals Management

**User Story:** As a factory worker, I want to create and track savings goals for my family, so that I can systematically save towards important milestones like education or gold.

#### Acceptance Criteria

1. WHEN an authenticated request is made to `GET /api/goals`, THE API_Server SHALL return all goal records for the authenticated user.
2. WHEN an authenticated request is made to `POST /api/goals` with title, targetAmount (≥ 1), and optional targetDate and emoji, THE API_Server SHALL create the goal and return HTTP 201.
3. WHEN an authenticated request is made to `PUT /api/goals/{id}/contribute` with a contribution amount > 0, THE API_Server SHALL add the amount to `goal.currentAmount`.
4. WHEN `goal.currentAmount` equals or exceeds `goal.targetAmount` after a contribution, THE API_Server SHALL set `goal.isCompleted = true`.
5. WHEN the Android app displays the Goals screen, THE Repository SHALL show the progress percentage as `(currentAmount / targetAmount) × 100`, capped at 100%.
6. WHEN a goal contribution is saved, THE API_Server SHALL trigger a recalculation of the user's Prosperity Score.

---

### Requirement 11: Debt Snowball Management

**User Story:** As a factory worker, I want to track my debts and record repayments, so that I can pay off my obligations systematically using the debt snowball method.

#### Acceptance Criteria

1. WHEN an authenticated request is made to `GET /api/debts`, THE API_Server SHALL return all debt records for the authenticated user.
2. WHEN an authenticated request is made to `POST /api/debts` with creditorName, totalAmount (≥ 1), and remainingAmount, THE API_Server SHALL create the debt record and return HTTP 201.
3. WHEN an authenticated request is made to `PUT /api/debts/{id}/repay` with a repayment amount `a > 0`, THE API_Server SHALL set `remainingAmount = remainingAmount - a`.
4. IF a repayment amount `a` would make `remainingAmount` negative, THEN THE API_Server SHALL return HTTP 400 with message "Repayment exceeds remaining balance".
5. WHEN a debt repayment is recorded, THE API_Server SHALL trigger a recalculation of the user's Prosperity Score.

---

### Requirement 12: Utility Bills Management

**User Story:** As a factory worker, I want to record my utility bills and mark them as paid, so that I never miss a due date and avoid late surcharges.

#### Acceptance Criteria

1. WHEN an authenticated request is made to `GET /api/bills`, THE API_Server SHALL return all utility bill records for the authenticated user.
2. WHEN an authenticated request is made to `POST /api/bills` with companyName, billType, amount (≥ 1), and optional dueDate and consumerNumber, THE API_Server SHALL create the bill record and return HTTP 201.
3. WHEN an authenticated request is made to `PUT /api/bills/{id}/pay`, THE API_Server SHALL set `isPaid = true` and `paidDate` to the current date, and return the updated bill record.
4. IF a `PUT /api/bills/{id}/pay` request references a bill that does not belong to the authenticated user, THEN THE API_Server SHALL return HTTP 404.

---

### Requirement 13: Emergency Locker

**User Story:** As a factory worker, I want to maintain a digital emergency savings locker, so that I have a protected fund for unexpected family expenses.

#### Acceptance Criteria

1. WHEN an authenticated request is made to `GET /api/emergency-locker`, THE API_Server SHALL return the Locker record for the authenticated user, including the current balance.
2. WHEN an authenticated request is made to `POST /api/emergency-locker/deposit` with amount > 0, THE API_Server SHALL add the amount to `locker.balance` and return the updated Locker record.
3. WHEN an authenticated request is made to `POST /api/emergency-locker/withdraw` with amount > 0 and the amount does not exceed the current `locker.balance`, THE API_Server SHALL subtract the amount from `locker.balance` and return the updated Locker record.
4. IF a withdrawal request specifies an amount greater than the current `locker.balance`, THEN THE API_Server SHALL return HTTP 400 with bilingual message "Insufficient balance / رقم ناکافی ہے", and the locker balance SHALL remain unchanged.
5. THE API_Server SHALL ensure `locker.balance` is never stored as a negative number.
6. WHEN a deposit or withdrawal is completed, THE API_Server SHALL trigger a recalculation of the user's Prosperity Score.

---

### Requirement 14: Business Khata (Customer Order Ledger)

**User Story:** As a factory worker with a side business, I want to track customer orders and their payment status, so that I know what is owed to me and what is due for delivery.

#### Acceptance Criteria

1. WHEN an authenticated request is made to `GET /api/orders`, THE API_Server SHALL return all customer order records for the authenticated user.
2. WHEN an authenticated request is made to `POST /api/orders` with customerName, serviceTitle, and totalAmount (≥ 1), THE API_Server SHALL create the order and return HTTP 201.
3. WHEN an authenticated request is made to `PUT /api/orders/{id}` with updates to `isDelivered`, `isFullyPaid`, `advancePaid`, or `dueDate`, THE API_Server SHALL update the specified order and return the updated record.
4. IF a `PUT /api/orders/{id}` request references an order that does not belong to the authenticated user, THEN THE API_Server SHALL return HTTP 404.

---

### Requirement 15: Prosperity Score Calculation

**User Story:** As a factory worker, I want to see a single financial health score with detailed pillar breakdowns, so that I understand where I stand and what to improve.

#### Acceptance Criteria

1. WHEN an authenticated request is made to `GET /api/prosperity`, THE API_Server SHALL return the Prosperity Score record including the total score (0–100), pillar breakdown, daysRunway, savingsPct, and debtControlPct.
2. WHEN the Prosperity_Calculator runs, THE API_Server SHALL compute a score in the range [0, 100] by summing the scores of 6 weighted pillars: Budgeting (20), Savings Habit (20), Emergency Buffer (20), Debt Control (15), Digital Safety (10), and Income Resilience (15).
3. WHEN the Emergency Buffer pillar score is calculated, THE API_Server SHALL compute `daysRunway = floor(locker.balance / (cashFlow.expenses / 30))` and cap the pillar score at 20. IF `cashFlow.expenses` is 0, THE API_Server SHALL treat the runway as effectively infinite and assign the Emergency Buffer pillar its maximum score of 20.
4. WHEN financial data changes (cash flow update, Kameti payment, goal contribution, debt repayment, locker deposit/withdrawal), THE API_Server SHALL recalculate and persist the updated Prosperity Score before returning the HTTP response to the client.
5. THE Prosperity_Calculator SHALL produce a score of 0 when a user has no income recorded (cashFlow.income = 0 or no cashFlow record exists).

---

### Requirement 16: Notifications

**User Story:** As a factory worker, I want to receive contextual notifications about my salary, bills, and financial tips, so that I can take timely action on important financial events.

#### Acceptance Criteria

1. WHEN an authenticated request is made to `GET /api/notifications`, THE API_Server SHALL return all notifications for the authenticated user, sorted by `createdAt` descending.
2. WHEN an authenticated request is made to `PUT /api/notifications/{id}/read`, THE API_Server SHALL set `isRead = true` for the specified notification and return HTTP 200.
3. WHEN an authenticated request is made to `DELETE /api/notifications`, THE API_Server SHALL delete all notifications for the authenticated user and return HTTP 200.
4. IF a `PUT /api/notifications/{id}/read` request references a notification that does not belong to the authenticated user, THEN THE API_Server SHALL return HTTP 404.
5. WHEN the Android app loads the Notifications screen, THE Repository SHALL display unread notifications prominently and show the `destination` field to enable deep-link navigation.

---

### Requirement 17: Coach Fatima Chat

**User Story:** As a factory worker, I want to chat with Coach Fatima for personalized financial advice in Urdu, so that I can get guidance tailored to my situation.

#### Acceptance Criteria

1. WHEN an authenticated request is made to `GET /api/coach/messages`, THE API_Server SHALL return all coach message records for the authenticated user, sorted by `createdAt` ascending.
2. WHEN an authenticated request is made to `POST /api/coach/messages` with a non-empty user message, THE API_Server SHALL persist the user's message, relay it to Firebase AI with the user's financial context, persist the AI response as a coach message, and return the coach's reply.
3. WHEN a new user account is created, THE Auth_Service SHALL persist a welcome coach message in Urdu from Coach Fatima as the first message in the user's conversation.
4. WHEN a `POST /api/coach/messages` request contains an empty or whitespace-only message body, THE API_Server SHALL return HTTP 400, regardless of whether the request is authenticated.

---

### Requirement 18: Repository Cache-First Strategy (Android)

**User Story:** As a factory worker on a slow mobile network, I want the app to load data instantly from cache and refresh in the background, so that I am never stuck staring at a loading spinner.

#### Acceptance Criteria

1. WHEN the Repository fetches any data collection (transactions, envelopes, goals, etc.), THE Repository SHALL emit cached Room data immediately before initiating the API call.
2. WHEN a fresh API response is received successfully, THE Repository SHALL update the Room cache and emit the fresh data to the ViewModel.
3. WHEN a network call fails due to no internet connectivity and Room cache is non-empty, THE Repository SHALL continue displaying the cached data and SHALL NOT emit a terminal error state.
4. WHEN a network call fails due to no internet connectivity and Room cache is empty, THE Repository SHALL emit `Result.Error` with the bilingual message "انٹرنیٹ کنیکشن نہیں / No internet connection".
5. WHEN the API returns HTTP 401 during a data fetch, THE Auth_Interceptor SHALL attempt token refresh exactly once; if the refresh succeeds, THE Auth_Interceptor SHALL automatically retry the original failed request with the new access token before propagating any error to the caller.

---

### Requirement 19: Data Isolation and Security

**User Story:** As a system administrator, I want every user's financial data to be completely isolated from other users' data, so that no factory worker can accidentally or intentionally view another worker's records.

#### Acceptance Criteria

1. THE API_Server SHALL include `userId: req.userId` in every MongoDB query that reads or writes user-owned data (transactions, envelopes, kametis, goals, debts, bills, locker, orders, notifications, coach messages, prosperity, cash flow).
2. WHEN two different users create records with identical content, THE API_Server SHALL store them as separate documents associated with their respective `userId` values.
3. THE API_Server SHALL hash all passwords with bcrypt (12 rounds) before storage and SHALL never return or log plaintext passwords.
4. THE API_Server SHALL send CNIC values to the Android app in masked format only (e.g., `42101-•••••••-3`).
5. THE API_Server SHALL enforce HTTPS for all API communication and SHALL reject unencrypted HTTP requests.
6. WHEN a request to modify or read a record by ID is made (e.g., `/api/debts/{id}`), THE API_Server SHALL verify that the record's `userId` matches `req.userId` and, if the verification fails, SHALL return HTTP 404 (hiding the existence of the record from the unauthorized requestor).

---

### Requirement 20: New User Data Seeding

**User Story:** As a new user registering for the first time, I want the app to be immediately usable with sensible defaults, so that I do not have to set up everything from scratch.

#### Acceptance Criteria

1. WHEN a new user account is successfully created, THE Auth_Service SHALL create exactly 4 envelope documents for that user with keys and default percentages: `needs` (70%), `commitments` (6%), `emergency` (6%), `savings` (18%).
2. WHEN a new user account is successfully created, THE Auth_Service SHALL create an Emergency Locker document for that user with `balance = 0`.
3. WHEN a new user account is successfully created, THE Auth_Service SHALL create a Prosperity Score document for that user with `score = 0`.
4. WHEN a new user account is successfully created, THE Auth_Service SHALL create one coach message record with the welcome text in Urdu from Coach Fatima, with `isFromCoach = true`.
5. WHEN seed data creation is complete, THE Auth_Service SHALL verify that exactly 4 envelope documents exist for the new user and that an Emergency Locker document exists, and SHALL block the registration response until this verification succeeds.

---

### Requirement 21: Health Check Endpoint

**User Story:** As a system operator, I want a health check endpoint, so that I can monitor backend availability and MongoDB connectivity.

#### Acceptance Criteria

1. WHEN an unauthenticated request is made to `GET /api/health`, THE API_Server SHALL return HTTP 200 with a JSON body indicating server status and MongoDB connection state.
2. WHEN the MongoDB connection is unavailable at the time of the health check, THE API_Server SHALL return HTTP 503 in the health check response.
3. WHEN the MongoDB Atlas connection fails during a regular API request, THE API_Server SHALL return HTTP 503 to the client and attempt automatic reconnection via Mongoose.
