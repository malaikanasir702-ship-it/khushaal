package com.example.data.repository

import android.content.Context
import com.example.data.api.*
import com.example.data.local.KhushhaalDatabase
import com.example.data.local.entities.*
import com.example.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException

class KhushhaalRepository(
    private val apiService: KhushhaalApiService,
    private val db: KhushhaalDatabase
) {

    // --- User Profile ---
    fun getUserProfile(): Flow<Result<UserProfile>> = flow {
        emit(Result.Loading)
        val cached = db.userDao().getUserProfile().firstOrNull()
        if (cached != null) {
            emit(Result.Success(cached.toDomain()))
        }

        try {
            val response = apiService.getProfile()
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                val entity = UserEntity(
                    id = "me",
                    name = dto.name,
                    urduName = dto.urduName ?: dto.name,
                    greetingEnglish = dto.greetingEnglish,
                    greetingUrdu = dto.greetingUrdu,
                    organization = dto.organization,
                    verifiedText = dto.verifiedText,
                    factoryId = dto.factoryId,
                    phoneMasked = dto.phoneMasked,
                    cnicMasked = dto.cnicMasked,
                    paymentAccount = dto.paymentAccount,
                    avatarUrl = dto.avatarUrl,
                    preferredLanguage = dto.preferredLanguage
                )
                db.userDao().insertUserProfile(entity)
                emit(Result.Success(entity.toDomain()))
            } else if (cached == null) {
                emit(Result.Error(response.code(), response.message() ?: "Error fetching profile"))
            }
        } catch (e: IOException) {
            if (cached == null) {
                emit(Result.Error(0, "انٹرنیٹ کنیکشن نہیں / No internet connection"))
            }
        } catch (e: Exception) {
            if (cached == null) {
                emit(Result.Error(-1, e.localizedMessage ?: "Unknown error"))
            }
        }
    }.flowOn(Dispatchers.IO)

    suspend fun updateProfile(request: UpdateProfileRequest): Result<UserProfile> {
        return try {
            val response = apiService.updateProfile(request)
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                val entity = UserEntity(
                    id = "me",
                    name = dto.name,
                    urduName = dto.urduName ?: dto.name,
                    greetingEnglish = dto.greetingEnglish,
                    greetingUrdu = dto.greetingUrdu,
                    organization = dto.organization,
                    verifiedText = dto.verifiedText,
                    factoryId = dto.factoryId,
                    phoneMasked = dto.phoneMasked,
                    cnicMasked = dto.cnicMasked,
                    paymentAccount = dto.paymentAccount,
                    avatarUrl = dto.avatarUrl,
                    preferredLanguage = dto.preferredLanguage
                )
                db.userDao().insertUserProfile(entity)
                Result.Success(entity.toDomain())
            } else {
                Result.Error(response.code(), response.message() ?: "Failed to update profile")
            }
        } catch (e: Exception) {
            Result.Error(0, e.localizedMessage ?: "Network error")
        }
    }

    // --- Cash Flow ---
    fun getCashFlow(): Flow<Result<CashFlowData>> = flow {
        emit(Result.Loading)
        val cached = db.cashFlowDao().getCashFlow().firstOrNull()
        if (cached != null) {
            emit(Result.Success(cached.toDomain()))
        }

        try {
            val response = apiService.getCashFlow()
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                val entity = CashFlowEntity(
                    id = "current",
                    month = dto.month,
                    income = dto.income,
                    incomeLabel = dto.incomeLabel ?: "Salary",
                    expenses = dto.expenses,
                    expensesLabel = dto.expensesLabel ?: "Expenses",
                    savings = dto.savings,
                    available = dto.available
                )
                db.cashFlowDao().insertCashFlow(entity)
                emit(Result.Success(entity.toDomain()))
            } else if (cached == null) {
                emit(Result.Error(response.code(), response.message()))
            }
        } catch (e: IOException) {
            if (cached == null) emit(Result.Error(0, "انٹرنیٹ کنیکشن نہیں / No internet connection"))
        } catch (e: Exception) {
            if (cached == null) emit(Result.Error(-1, e.localizedMessage ?: "Error"))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun updateCashFlow(request: UpdateCashFlowRequest): Result<CashFlowData> {
        return try {
            val response = apiService.updateCashFlow(request)
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                val entity = CashFlowEntity(
                    id = "current",
                    month = dto.month,
                    income = dto.income,
                    incomeLabel = dto.incomeLabel ?: "Salary",
                    expenses = dto.expenses,
                    expensesLabel = dto.expensesLabel ?: "Expenses",
                    savings = dto.savings,
                    available = dto.available
                )
                db.cashFlowDao().insertCashFlow(entity)
                Result.Success(entity.toDomain())
            } else {
                Result.Error(response.code(), response.message())
            }
        } catch (e: Exception) {
            Result.Error(0, e.localizedMessage ?: "Network error")
        }
    }

    // --- Transactions ---
    fun getTransactions(month: String? = null): Flow<Result<List<TransactionItem>>> = flow {
        emit(Result.Loading)
        val cached = db.transactionDao().getTransactions().firstOrNull() ?: emptyList()
        if (cached.isNotEmpty()) {
            emit(Result.Success(cached.map { it.toDomain() }))
        }

        try {
            val response = apiService.getTransactions(month = month)
            if (response.isSuccessful && response.body() != null) {
                val items = response.body()!!.items
                val entities = items.map { dto ->
                    TransactionEntity(
                        id = dto.id,
                        title = dto.title,
                        urduTitle = dto.urduTitle ?: dto.title,
                        amount = dto.amount,
                        isExpense = dto.isExpense,
                        category = dto.category,
                        envelopeId = dto.envelopeId ?: "needs",
                        date = dto.date,
                        paymentMethod = dto.paymentMethod ?: "Cash"
                    )
                }
                db.transactionDao().insertTransactions(entities)
                emit(Result.Success(entities.map { it.toDomain() }))
            } else if (cached.isEmpty()) {
                emit(Result.Error(response.code(), response.message()))
            }
        } catch (e: IOException) {
            if (cached.isEmpty()) emit(Result.Error(0, "انٹرنیٹ کنیکشن نہیں / No internet connection"))
        } catch (e: Exception) {
            if (cached.isEmpty()) emit(Result.Error(-1, e.localizedMessage ?: "Error"))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun addTransaction(request: CreateTransactionRequest): Result<TransactionItem> {
        return try {
            val response = apiService.addTransaction(request)
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                val entity = TransactionEntity(
                    id = dto.id,
                    title = dto.title,
                    urduTitle = dto.urduTitle ?: dto.title,
                    amount = dto.amount,
                    isExpense = dto.isExpense,
                    category = dto.category,
                    envelopeId = dto.envelopeId ?: "needs",
                    date = dto.date,
                    paymentMethod = dto.paymentMethod ?: "Cash"
                )
                db.transactionDao().insertTransaction(entity)
                Result.Success(entity.toDomain())
            } else {
                Result.Error(response.code(), response.message())
            }
        } catch (e: Exception) {
            Result.Error(0, e.localizedMessage ?: "Network error")
        }
    }

    // --- Envelopes ---
    fun getEnvelopes(): Flow<Result<List<Envelope>>> = flow {
        emit(Result.Loading)
        val cached = db.envelopeDao().getEnvelopes().firstOrNull() ?: emptyList()
        if (cached.isNotEmpty()) {
            emit(Result.Success(cached.map { it.toDomain() }))
        }

        try {
            val response = apiService.getEnvelopes()
            if (response.isSuccessful && response.body() != null) {
                val dtos = response.body()!!
                val entities = dtos.map { dto ->
                    EnvelopeEntity(
                        id = dto.id,
                        envelopeKey = dto.envelopeKey,
                        titleEnglish = dto.titleEnglish,
                        titleUrdu = dto.titleUrdu ?: "",
                        percentage = dto.percentage,
                        amount = dto.amount,
                        tag = dto.tag ?: ""
                    )
                }
                db.envelopeDao().insertEnvelopes(entities)
                val domainItems = dtos.map { dto ->
                    Envelope(
                        id = dto.id,
                        titleEnglish = dto.titleEnglish,
                        titleUrdu = dto.titleUrdu ?: "",
                        percentage = dto.percentage,
                        amount = dto.amount,
                        tag = dto.tag ?: "",
                        items = dto.items.map { EnvelopeSubItem(it.label, it.amount) }
                    )
                }
                emit(Result.Success(domainItems))
            } else if (cached.isEmpty()) {
                emit(Result.Error(response.code(), response.message()))
            }
        } catch (e: IOException) {
            if (cached.isEmpty()) emit(Result.Error(0, "انٹرنیٹ کنیکشن نہیں / No internet connection"))
        } catch (e: Exception) {
            if (cached.isEmpty()) emit(Result.Error(-1, e.localizedMessage ?: "Error"))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun updateEnvelope(envelopeId: String, request: UpdateEnvelopeRequest): Result<EnvelopeDto> {
        return try {
            val response = apiService.updateEnvelope(envelopeId, request)
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                val entity = EnvelopeEntity(
                    id = dto.id,
                    envelopeKey = dto.envelopeKey,
                    titleEnglish = dto.titleEnglish,
                    titleUrdu = dto.titleUrdu ?: "",
                    percentage = dto.percentage,
                    amount = dto.amount,
                    tag = dto.tag ?: ""
                )
                db.envelopeDao().insertEnvelope(entity)
                Result.Success(dto)
            } else {
                Result.Error(response.code(), response.message())
            }
        } catch (e: Exception) {
            Result.Error(0, e.localizedMessage ?: "Network error")
        }
    }

    // --- Kametis ---
    fun getKametis(): Flow<Result<List<KametiItem>>> = flow {
        emit(Result.Loading)
        val cached = db.kametiDao().getKametis().firstOrNull() ?: emptyList()
        if (cached.isNotEmpty()) {
            emit(Result.Success(cached.map { it.toDomain() }))
        }

        try {
            val response = apiService.getKametis()
            if (response.isSuccessful && response.body() != null) {
                val dtos = response.body()!!
                val entities = dtos.map { dto ->
                    KametiEntity(
                        id = dto.id,
                        name = dto.name,
                        urduName = dto.urduName ?: dto.name,
                        monthlyAmount = dto.monthlyAmount,
                        totalMembers = dto.totalMembers,
                        myTurnMonth = dto.myTurnMonth,
                        currentMonth = dto.currentMonth,
                        payoutAmount = dto.payoutAmount ?: (dto.monthlyAmount * dto.totalMembers),
                        organizer = dto.organizer ?: "",
                        isPaidThisMonth = dto.isPaidThisMonth
                    )
                }
                db.kametiDao().insertKametis(entities)
                emit(Result.Success(entities.map { it.toDomain() }))
            } else if (cached.isEmpty()) {
                emit(Result.Error(response.code(), response.message()))
            }
        } catch (e: IOException) {
            if (cached.isEmpty()) emit(Result.Error(0, "انٹرنیٹ کنیکشن نہیں / No internet connection"))
        } catch (e: Exception) {
            if (cached.isEmpty()) emit(Result.Error(-1, e.localizedMessage ?: "Error"))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun addKameti(request: CreateKametiRequest): Result<KametiItem> {
        return try {
            val response = apiService.addKameti(request)
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                val entity = KametiEntity(
                    id = dto.id,
                    name = dto.name,
                    urduName = dto.urduName ?: dto.name,
                    monthlyAmount = dto.monthlyAmount,
                    totalMembers = dto.totalMembers,
                    myTurnMonth = dto.myTurnMonth,
                    currentMonth = dto.currentMonth,
                    payoutAmount = dto.payoutAmount ?: (dto.monthlyAmount * dto.totalMembers),
                    organizer = dto.organizer ?: "",
                    isPaidThisMonth = dto.isPaidThisMonth
                )
                db.kametiDao().insertKameti(entity)
                Result.Success(entity.toDomain())
            } else {
                Result.Error(response.code(), response.message())
            }
        } catch (e: Exception) {
            Result.Error(0, e.localizedMessage ?: "Network error")
        }
    }

    suspend fun markKametiPaid(kametiId: String): Result<KametiItem> {
        return try {
            val response = apiService.markKametiPaid(kametiId)
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                val entity = KametiEntity(
                    id = dto.id,
                    name = dto.name,
                    urduName = dto.urduName ?: dto.name,
                    monthlyAmount = dto.monthlyAmount,
                    totalMembers = dto.totalMembers,
                    myTurnMonth = dto.myTurnMonth,
                    currentMonth = dto.currentMonth,
                    payoutAmount = dto.payoutAmount ?: (dto.monthlyAmount * dto.totalMembers),
                    organizer = dto.organizer ?: "",
                    isPaidThisMonth = dto.isPaidThisMonth
                )
                db.kametiDao().insertKameti(entity)
                Result.Success(entity.toDomain())
            } else {
                Result.Error(response.code(), response.message())
            }
        } catch (e: Exception) {
            Result.Error(0, e.localizedMessage ?: "Network error")
        }
    }

    // --- Goals ---
    fun getGoals(): Flow<Result<List<FamilyGoalItem>>> = flow {
        emit(Result.Loading)
        val cached = db.goalDao().getGoals().firstOrNull() ?: emptyList()
        if (cached.isNotEmpty()) {
            emit(Result.Success(cached.map { it.toDomain() }))
        }

        try {
            val response = apiService.getGoals()
            if (response.isSuccessful && response.body() != null) {
                val dtos = response.body()!!
                val entities = dtos.map { dto ->
                    GoalEntity(
                        id = dto.id,
                        title = dto.title,
                        urduTitle = dto.urduTitle ?: dto.title,
                        targetAmount = dto.targetAmount,
                        currentAmount = dto.currentAmount,
                        targetDate = dto.targetDate ?: "",
                        emoji = dto.emoji,
                        isCompleted = dto.isCompleted
                    )
                }
                db.goalDao().insertGoals(entities)
                emit(Result.Success(entities.map { it.toDomain() }))
            } else if (cached.isEmpty()) {
                emit(Result.Error(response.code(), response.message()))
            }
        } catch (e: IOException) {
            if (cached.isEmpty()) emit(Result.Error(0, "انٹرنیٹ کنیکشن نہیں / No internet connection"))
        } catch (e: Exception) {
            if (cached.isEmpty()) emit(Result.Error(-1, e.localizedMessage ?: "Error"))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun addGoal(request: CreateGoalRequest): Result<FamilyGoalItem> {
        return try {
            val response = apiService.addGoal(request)
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                val entity = GoalEntity(
                    id = dto.id,
                    title = dto.title,
                    urduTitle = dto.urduTitle ?: dto.title,
                    targetAmount = dto.targetAmount,
                    currentAmount = dto.currentAmount,
                    targetDate = dto.targetDate ?: "",
                    emoji = dto.emoji,
                    isCompleted = dto.isCompleted
                )
                db.goalDao().insertGoal(entity)
                Result.Success(entity.toDomain())
            } else {
                Result.Error(response.code(), response.message())
            }
        } catch (e: Exception) {
            Result.Error(0, e.localizedMessage ?: "Network error")
        }
    }

    suspend fun contributeToGoal(goalId: String, amount: Long): Result<FamilyGoalItem> {
        return try {
            val response = apiService.contributeToGoal(goalId, ContributeRequest(amount))
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                val entity = GoalEntity(
                    id = dto.id,
                    title = dto.title,
                    urduTitle = dto.urduTitle ?: dto.title,
                    targetAmount = dto.targetAmount,
                    currentAmount = dto.currentAmount,
                    targetDate = dto.targetDate ?: "",
                    emoji = dto.emoji,
                    isCompleted = dto.isCompleted
                )
                db.goalDao().insertGoal(entity)
                Result.Success(entity.toDomain())
            } else {
                Result.Error(response.code(), response.message())
            }
        } catch (e: Exception) {
            Result.Error(0, e.localizedMessage ?: "Network error")
        }
    }

    // --- Debts ---
    fun getDebts(): Flow<Result<List<DebtItem>>> = flow {
        emit(Result.Loading)
        val cached = db.debtDao().getDebts().firstOrNull() ?: emptyList()
        if (cached.isNotEmpty()) {
            emit(Result.Success(cached.map { it.toDomain() }))
        }

        try {
            val response = apiService.getDebts()
            if (response.isSuccessful && response.body() != null) {
                val dtos = response.body()!!
                val entities = dtos.map { dto ->
                    DebtEntity(
                        id = dto.id,
                        creditorName = dto.creditorName,
                        creditorUrdu = dto.creditorUrdu ?: dto.creditorName,
                        relationOrType = dto.relationOrType ?: "",
                        totalAmount = dto.totalAmount,
                        remainingAmount = dto.remainingAmount,
                        monthlyCommitment = dto.monthlyCommitment,
                        urgencyLevel = dto.urgencyLevel ?: "MEDIUM",
                        isShariahFriendly = dto.isShariahFriendly,
                        repaymentStrategyTip = dto.repaymentStrategyTip ?: ""
                    )
                }
                db.debtDao().insertDebts(entities)
                emit(Result.Success(entities.map { it.toDomain() }))
            } else if (cached.isEmpty()) {
                emit(Result.Error(response.code(), response.message()))
            }
        } catch (e: IOException) {
            if (cached.isEmpty()) emit(Result.Error(0, "انٹرنیٹ کنیکشن نہیں / No internet connection"))
        } catch (e: Exception) {
            if (cached.isEmpty()) emit(Result.Error(-1, e.localizedMessage ?: "Error"))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun addDebt(request: CreateDebtRequest): Result<DebtItem> {
        return try {
            val response = apiService.addDebt(request)
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                val entity = DebtEntity(
                    id = dto.id,
                    creditorName = dto.creditorName,
                    creditorUrdu = dto.creditorUrdu ?: dto.creditorName,
                    relationOrType = dto.relationOrType ?: "",
                    totalAmount = dto.totalAmount,
                    remainingAmount = dto.remainingAmount,
                    monthlyCommitment = dto.monthlyCommitment,
                    urgencyLevel = dto.urgencyLevel ?: "MEDIUM",
                    isShariahFriendly = dto.isShariahFriendly,
                    repaymentStrategyTip = dto.repaymentStrategyTip ?: ""
                )
                db.debtDao().insertDebt(entity)
                Result.Success(entity.toDomain())
            } else {
                Result.Error(response.code(), response.message())
            }
        } catch (e: Exception) {
            Result.Error(0, e.localizedMessage ?: "Network error")
        }
    }

    suspend fun repayDebt(debtId: String, amount: Long): Result<DebtItem> {
        return try {
            val response = apiService.repayDebt(debtId, RepayRequest(amount))
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                val entity = DebtEntity(
                    id = dto.id,
                    creditorName = dto.creditorName,
                    creditorUrdu = dto.creditorUrdu ?: dto.creditorName,
                    relationOrType = dto.relationOrType ?: "",
                    totalAmount = dto.totalAmount,
                    remainingAmount = dto.remainingAmount,
                    monthlyCommitment = dto.monthlyCommitment,
                    urgencyLevel = dto.urgencyLevel ?: "MEDIUM",
                    isShariahFriendly = dto.isShariahFriendly,
                    repaymentStrategyTip = dto.repaymentStrategyTip ?: ""
                )
                db.debtDao().insertDebt(entity)
                Result.Success(entity.toDomain())
            } else {
                Result.Error(response.code(), response.message())
            }
        } catch (e: Exception) {
            Result.Error(0, e.localizedMessage ?: "Network error")
        }
    }

    // --- Bills ---
    fun getBills(): Flow<Result<List<UtilityBill>>> = flow {
        emit(Result.Loading)
        val cached = db.billDao().getBills().firstOrNull() ?: emptyList()
        if (cached.isNotEmpty()) {
            emit(Result.Success(cached.map { it.toDomain() }))
        }

        try {
            val response = apiService.getBills()
            if (response.isSuccessful && response.body() != null) {
                val dtos = response.body()!!
                val entities = dtos.map { dto ->
                    BillEntity(
                        id = dto.id,
                        companyName = dto.companyName,
                        companyUrdu = dto.companyUrdu ?: dto.companyName,
                        consumerNumber = dto.consumerNumber ?: "",
                        billType = dto.billType ?: "Electricity",
                        month = dto.month ?: "",
                        dueDate = dto.dueDate ?: "",
                        amount = dto.amount,
                        unitsConsumed = dto.unitsConsumed,
                        isPaid = dto.isPaid,
                        paidDate = dto.paidDate,
                        alertTip = dto.alertTip ?: ""
                    )
                }
                db.billDao().insertBills(entities)
                emit(Result.Success(entities.map { it.toDomain() }))
            } else if (cached.isEmpty()) {
                emit(Result.Error(response.code(), response.message()))
            }
        } catch (e: IOException) {
            if (cached.isEmpty()) emit(Result.Error(0, "انٹرنیٹ کنیکشن نہیں / No internet connection"))
        } catch (e: Exception) {
            if (cached.isEmpty()) emit(Result.Error(-1, e.localizedMessage ?: "Error"))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun addBill(request: CreateBillRequest): Result<UtilityBill> {
        return try {
            val response = apiService.addBill(request)
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                val entity = BillEntity(
                    id = dto.id,
                    companyName = dto.companyName,
                    companyUrdu = dto.companyUrdu ?: dto.companyName,
                    consumerNumber = dto.consumerNumber ?: "",
                    billType = dto.billType ?: "Electricity",
                    month = dto.month ?: "",
                    dueDate = dto.dueDate ?: "",
                    amount = dto.amount,
                    unitsConsumed = dto.unitsConsumed,
                    isPaid = dto.isPaid,
                    paidDate = dto.paidDate,
                    alertTip = dto.alertTip ?: ""
                )
                db.billDao().insertBill(entity)
                Result.Success(entity.toDomain())
            } else {
                Result.Error(response.code(), response.message())
            }
        } catch (e: Exception) {
            Result.Error(0, e.localizedMessage ?: "Network error")
        }
    }

    suspend fun markBillPaid(billId: String): Result<UtilityBill> {
        return try {
            val response = apiService.markBillPaid(billId)
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                val entity = BillEntity(
                    id = dto.id,
                    companyName = dto.companyName,
                    companyUrdu = dto.companyUrdu ?: dto.companyName,
                    consumerNumber = dto.consumerNumber ?: "",
                    billType = dto.billType ?: "Electricity",
                    month = dto.month ?: "",
                    dueDate = dto.dueDate ?: "",
                    amount = dto.amount,
                    unitsConsumed = dto.unitsConsumed,
                    isPaid = dto.isPaid,
                    paidDate = dto.paidDate,
                    alertTip = dto.alertTip ?: ""
                )
                db.billDao().insertBill(entity)
                Result.Success(entity.toDomain())
            } else {
                Result.Error(response.code(), response.message())
            }
        } catch (e: Exception) {
            Result.Error(0, e.localizedMessage ?: "Network error")
        }
    }

    // --- Emergency Locker ---
    fun getEmergencyLockerBalance(): Flow<Result<Long>> = flow {
        emit(Result.Loading)
        val cached = db.lockerDao().getLocker().firstOrNull()
        if (cached != null) {
            emit(Result.Success(cached.balance))
        }

        try {
            val response = apiService.getLockerBalance()
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                val entity = LockerEntity(id = "locker", balance = dto.balance, updatedAt = dto.updatedAt)
                db.lockerDao().insertLocker(entity)
                emit(Result.Success(dto.balance))
            } else if (cached == null) {
                emit(Result.Error(response.code(), response.message()))
            }
        } catch (e: IOException) {
            if (cached == null) emit(Result.Error(0, "انٹرنیٹ کنیکشن نہیں / No internet connection"))
        } catch (e: Exception) {
            if (cached == null) emit(Result.Error(-1, e.localizedMessage ?: "Error"))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun depositToLocker(amount: Long): Result<Long> {
        return try {
            val response = apiService.depositToLocker(LockerTransactionRequest(amount))
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                db.lockerDao().insertLocker(LockerEntity(id = "locker", balance = dto.balance, updatedAt = dto.updatedAt))
                Result.Success(dto.balance)
            } else {
                Result.Error(response.code(), response.message())
            }
        } catch (e: Exception) {
            Result.Error(0, e.localizedMessage ?: "Network error")
        }
    }

    suspend fun withdrawFromLocker(amount: Long): Result<Long> {
        return try {
            val response = apiService.withdrawFromLocker(LockerTransactionRequest(amount))
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                db.lockerDao().insertLocker(LockerEntity(id = "locker", balance = dto.balance, updatedAt = dto.updatedAt))
                Result.Success(dto.balance)
            } else {
                Result.Error(response.code(), response.message())
            }
        } catch (e: Exception) {
            Result.Error(0, e.localizedMessage ?: "Network error")
        }
    }

    // --- Customer Orders ---
    fun getCustomerOrders(): Flow<Result<List<CustomerOrder>>> = flow {
        emit(Result.Loading)
        val cached = db.orderDao().getOrders().firstOrNull() ?: emptyList()
        if (cached.isNotEmpty()) {
            emit(Result.Success(cached.map { it.toDomain() }))
        }

        try {
            val response = apiService.getOrders()
            if (response.isSuccessful && response.body() != null) {
                val dtos = response.body()!!
                val entities = dtos.map { dto ->
                    OrderEntity(
                        id = dto.id,
                        customerName = dto.customerName,
                        phone = dto.phone ?: "",
                        serviceTitle = dto.serviceTitle,
                        totalAmount = dto.totalAmount,
                        advancePaid = dto.advancePaid,
                        dueDate = dto.dueDate ?: "",
                        isDelivered = dto.isDelivered,
                        isFullyPaid = dto.isFullyPaid
                    )
                }
                db.orderDao().insertOrders(entities)
                emit(Result.Success(entities.map { it.toDomain() }))
            } else if (cached.isEmpty()) {
                emit(Result.Error(response.code(), response.message()))
            }
        } catch (e: IOException) {
            if (cached.isEmpty()) emit(Result.Error(0, "انٹرنیٹ کنیکشن نہیں / No internet connection"))
        } catch (e: Exception) {
            if (cached.isEmpty()) emit(Result.Error(-1, e.localizedMessage ?: "Error"))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun addOrder(request: CreateOrderRequest): Result<CustomerOrder> {
        return try {
            val response = apiService.addOrder(request)
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                val entity = OrderEntity(
                    id = dto.id,
                    customerName = dto.customerName,
                    phone = dto.phone ?: "",
                    serviceTitle = dto.serviceTitle,
                    totalAmount = dto.totalAmount,
                    advancePaid = dto.advancePaid,
                    dueDate = dto.dueDate ?: "",
                    isDelivered = dto.isDelivered,
                    isFullyPaid = dto.isFullyPaid
                )
                db.orderDao().insertOrder(entity)
                Result.Success(entity.toDomain())
            } else {
                Result.Error(response.code(), response.message())
            }
        } catch (e: Exception) {
            Result.Error(0, e.localizedMessage ?: "Network error")
        }
    }

    suspend fun updateOrder(orderId: String, request: UpdateOrderRequest): Result<CustomerOrder> {
        return try {
            val response = apiService.updateOrder(orderId, request)
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                val entity = OrderEntity(
                    id = dto.id,
                    customerName = dto.customerName,
                    phone = dto.phone ?: "",
                    serviceTitle = dto.serviceTitle,
                    totalAmount = dto.totalAmount,
                    advancePaid = dto.advancePaid,
                    dueDate = dto.dueDate ?: "",
                    isDelivered = dto.isDelivered,
                    isFullyPaid = dto.isFullyPaid
                )
                db.orderDao().insertOrder(entity)
                Result.Success(entity.toDomain())
            } else {
                Result.Error(response.code(), response.message())
            }
        } catch (e: Exception) {
            Result.Error(0, e.localizedMessage ?: "Network error")
        }
    }

    // --- Prosperity Score ---
    fun getProsperityScore(): Flow<Result<ProsperityScore>> = flow {
        emit(Result.Loading)
        val cached = db.prosperityDao().getProsperity().firstOrNull()
        if (cached != null) {
            emit(Result.Success(cached.toDomain()))
        }

        try {
            val response = apiService.getProsperity()
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                val entity = ProsperityEntity(
                    id = "prosperity",
                    score = dto.score,
                    maxScore = dto.maxScore,
                    savingsPct = dto.savingsPct,
                    debtControlPct = dto.debtControlPct,
                    safetyShieldPct = dto.safetyShieldPct,
                    daysRunway = dto.daysRunway,
                    lastCalculatedAt = dto.lastCalculatedAt
                )
                db.prosperityDao().insertProsperity(entity)
                emit(Result.Success(entity.toDomain()))
            } else if (cached == null) {
                emit(Result.Error(response.code(), response.message()))
            }
        } catch (e: IOException) {
            if (cached == null) emit(Result.Error(0, "انٹرنیٹ کنیکشن نہیں / No internet connection"))
        } catch (e: Exception) {
            if (cached == null) emit(Result.Error(-1, e.localizedMessage ?: "Error"))
        }
    }.flowOn(Dispatchers.IO)

    // --- Notifications ---
    fun getNotifications(): Flow<Result<List<AppNotification>>> = flow {
        emit(Result.Loading)
        val cached = db.notificationDao().getNotifications().firstOrNull() ?: emptyList()
        if (cached.isNotEmpty()) {
            emit(Result.Success(cached.map { it.toDomain() }))
        }

        try {
            val response = apiService.getNotifications()
            if (response.isSuccessful && response.body() != null) {
                val dtos = response.body()!!
                val entities = dtos.map { dto ->
                    NotificationEntity(
                        id = dto.id,
                        titleUrdu = dto.titleUrdu,
                        titleEnglish = dto.titleEnglish,
                        descriptionUrdu = dto.descriptionUrdu ?: "",
                        descriptionEnglish = dto.descriptionEnglish ?: "",
                        category = dto.category,
                        isRead = dto.isRead,
                        timestamp = dto.timestamp,
                        destination = dto.destination,
                        spokenText = dto.spokenText ?: ""
                    )
                }
                db.notificationDao().insertNotifications(entities)
                emit(Result.Success(entities.map { it.toDomain() }))
            } else if (cached.isEmpty()) {
                emit(Result.Error(response.code(), response.message()))
            }
        } catch (e: IOException) {
            if (cached.isEmpty()) emit(Result.Error(0, "انٹرنیٹ کنیکشن نہیں / No internet connection"))
        } catch (e: Exception) {
            if (cached.isEmpty()) emit(Result.Error(-1, e.localizedMessage ?: "Error"))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun markNotificationRead(notifId: String) {
        try {
            db.notificationDao().markAsRead(notifId)
            apiService.markNotificationRead(notifId)
        } catch (e: Exception) {
            // Best effort
        }
    }

    suspend fun clearAllNotifications() {
        try {
            db.notificationDao().clear()
            apiService.clearNotifications()
        } catch (e: Exception) {
            // Best effort
        }
    }

    // --- Coach Messages ---
    fun getCoachMessages(): Flow<Result<List<CoachMessage>>> = flow {
        emit(Result.Loading)
        val cached = db.coachDao().getMessages().firstOrNull() ?: emptyList()
        if (cached.isNotEmpty()) {
            emit(Result.Success(cached.map { it.toDomain() }))
        }

        try {
            val response = apiService.getCoachMessages()
            if (response.isSuccessful && response.body() != null) {
                val dtos = response.body()!!
                val entities = dtos.map { dto ->
                    CoachMessageEntity(
                        id = dto.id,
                        textUrdu = dto.textUrdu,
                        textRoman = dto.textRoman ?: "",
                        isFromCoach = dto.isFromCoach,
                        timestamp = dto.timestamp,
                        spokenText = dto.spokenText ?: ""
                    )
                }
                db.coachDao().insertMessages(entities)
                emit(Result.Success(entities.map { it.toDomain() }))
            } else if (cached.isEmpty()) {
                emit(Result.Error(response.code(), response.message()))
            }
        } catch (e: IOException) {
            if (cached.isEmpty()) emit(Result.Error(0, "انٹرنیٹ کنیکشن نہیں / No internet connection"))
        } catch (e: Exception) {
            if (cached.isEmpty()) emit(Result.Error(-1, e.localizedMessage ?: "Error"))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun sendCoachMessage(textUrdu: String, textRoman: String? = null): Result<CoachMessage> {
        return try {
            val response = apiService.sendCoachMessage(SendMessageRequest(textUrdu = textUrdu, textRoman = textRoman))
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                val entity = CoachMessageEntity(
                    id = dto.id,
                    textUrdu = dto.textUrdu,
                    textRoman = dto.textRoman ?: "",
                    isFromCoach = dto.isFromCoach,
                    timestamp = dto.timestamp,
                    spokenText = dto.spokenText ?: ""
                )
                db.coachDao().insertMessage(entity)
                Result.Success(entity.toDomain())
            } else {
                Result.Error(response.code(), response.message())
            }
        } catch (e: Exception) {
            Result.Error(0, e.localizedMessage ?: "Network error")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: KhushhaalRepository? = null

        fun getInstance(context: Context): KhushhaalRepository {
            return INSTANCE ?: synchronized(this) {
                val retrofitClient = RetrofitClient.getInstance(context)
                val database = KhushhaalDatabase.getInstance(context)
                INSTANCE ?: KhushhaalRepository(
                    retrofitClient.khushhaalApiService,
                    database
                ).also { INSTANCE = it }
            }
        }
    }
}
