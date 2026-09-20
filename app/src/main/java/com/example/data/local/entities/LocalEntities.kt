package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.*

@Entity(tableName = "user_profile")
data class UserEntity(
    @PrimaryKey val id: String = "me",
    val name: String,
    val urduName: String,
    val greetingEnglish: String = "Assalam-o-Alaikum!",
    val greetingUrdu: String = "سلام علیکم",
    val organization: String,
    val verifiedText: String = "تصدیق شدہ",
    val factoryId: String,
    val phoneMasked: String,
    val cnicMasked: String,
    val paymentAccount: String,
    val avatarUrl: String,
    val preferredLanguage: String = "BILINGUAL"
) {
    fun toDomain(): UserProfile = UserProfile(
        name = name,
        urduName = urduName,
        greetingEnglish = greetingEnglish,
        greetingUrdu = greetingUrdu,
        organization = organization,
        verifiedText = verifiedText,
        factoryId = factoryId,
        phoneMasked = phoneMasked,
        cnicMasked = cnicMasked,
        paymentAccount = paymentAccount,
        avatarUrl = avatarUrl
    )
}

@Entity(tableName = "cash_flow")
data class CashFlowEntity(
    @PrimaryKey val id: String = "current",
    val month: String,
    val income: Long,
    val incomeLabel: String,
    val expenses: Long,
    val expensesLabel: String,
    val savings: Long,
    val available: Long
) {
    fun toDomain(): CashFlowData = CashFlowData(
        month = month,
        income = income,
        incomeLabel = incomeLabel,
        expenses = expenses,
        expensesLabel = expensesLabel,
        savings = savings,
        available = available
    )
}

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey val id: String,
    val title: String,
    val urduTitle: String,
    val amount: Long,
    val isExpense: Boolean,
    val category: String,
    val envelopeId: String,
    val date: String,
    val paymentMethod: String
) {
    fun toDomain(): TransactionItem = TransactionItem(
        id = id,
        title = title,
        urduTitle = urduTitle,
        amount = amount,
        isExpense = isExpense,
        category = category,
        envelopeId = envelopeId,
        date = date,
        paymentMethod = paymentMethod
    )
}

@Entity(tableName = "envelopes")
data class EnvelopeEntity(
    @PrimaryKey val id: String,
    val envelopeKey: String,
    val titleEnglish: String,
    val titleUrdu: String,
    val percentage: Int,
    val amount: Long,
    val tag: String
) {
    fun toDomain(items: List<EnvelopeSubItem> = emptyList()): Envelope = Envelope(
        id = id,
        titleEnglish = titleEnglish,
        titleUrdu = titleUrdu,
        percentage = percentage,
        amount = amount,
        tag = tag,
        items = items
    )
}

@Entity(tableName = "kametis")
data class KametiEntity(
    @PrimaryKey val id: String,
    val name: String,
    val urduName: String,
    val monthlyAmount: Long,
    val totalMembers: Int,
    val myTurnMonth: Int,
    val currentMonth: Int,
    val payoutAmount: Long,
    val organizer: String,
    val isPaidThisMonth: Boolean
) {
    fun toDomain(): KametiItem = KametiItem(
        id = id,
        name = name,
        urduName = urduName,
        monthlyAmount = monthlyAmount,
        totalMembers = totalMembers,
        myTurnMonth = myTurnMonth,
        currentMonth = currentMonth,
        payoutAmount = payoutAmount,
        organizer = organizer,
        isPaidThisMonth = isPaidThisMonth
    )
}

@Entity(tableName = "goals")
data class GoalEntity(
    @PrimaryKey val id: String,
    val title: String,
    val urduTitle: String,
    val targetAmount: Long,
    val currentAmount: Long,
    val targetDate: String,
    val emoji: String,
    val isCompleted: Boolean
) {
    fun toDomain(): FamilyGoalItem = FamilyGoalItem(
        id = id,
        title = title,
        urduTitle = urduTitle,
        targetAmount = targetAmount,
        currentAmount = currentAmount,
        targetDate = targetDate,
        emoji = emoji
    )
}

@Entity(tableName = "debts")
data class DebtEntity(
    @PrimaryKey val id: String,
    val creditorName: String,
    val creditorUrdu: String,
    val relationOrType: String,
    val totalAmount: Long,
    val remainingAmount: Long,
    val monthlyCommitment: Long,
    val urgencyLevel: String,
    val isShariahFriendly: Boolean,
    val repaymentStrategyTip: String
) {
    fun toDomain(): DebtItem = DebtItem(
        id = id,
        creditorName = creditorName,
        creditorUrdu = creditorUrdu,
        relationOrType = relationOrType,
        totalAmount = totalAmount,
        remainingAmount = remainingAmount,
        monthlyCommitment = monthlyCommitment,
        urgencyLevel = urgencyLevel,
        isShariahFriendly = isShariahFriendly,
        repaymentStrategyTip = repaymentStrategyTip
    )
}

@Entity(tableName = "bills")
data class BillEntity(
    @PrimaryKey val id: String,
    val companyName: String,
    val companyUrdu: String,
    val consumerNumber: String,
    val billType: String,
    val month: String,
    val dueDate: String,
    val amount: Long,
    val unitsConsumed: Int,
    val isPaid: Boolean,
    val paidDate: String?,
    val alertTip: String
) {
    fun toDomain(): UtilityBill = UtilityBill(
        id = id,
        companyName = companyName,
        companyUrdu = companyUrdu,
        consumerNumber = consumerNumber,
        billType = billType,
        month = month,
        dueDate = dueDate,
        amount = amount,
        unitsConsumed = unitsConsumed,
        isPaid = isPaid,
        paidDate = paidDate,
        alertTip = alertTip
    )
}

@Entity(tableName = "locker")
data class LockerEntity(
    @PrimaryKey val id: String = "locker",
    val balance: Long,
    val updatedAt: String?
)

@Entity(tableName = "customer_orders")
data class OrderEntity(
    @PrimaryKey val id: String,
    val customerName: String,
    val phone: String,
    val serviceTitle: String,
    val totalAmount: Long,
    val advancePaid: Long,
    val dueDate: String,
    val isDelivered: Boolean,
    val isFullyPaid: Boolean
) {
    fun toDomain(): CustomerOrder = CustomerOrder(
        id = id,
        customerName = customerName,
        phone = phone,
        serviceTitle = serviceTitle,
        totalAmount = totalAmount,
        advancePaid = advancePaid,
        dueDate = dueDate,
        isDelivered = isDelivered,
        isFullyPaid = isFullyPaid
    )
}

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey val id: String,
    val titleUrdu: String,
    val titleEnglish: String,
    val descriptionUrdu: String,
    val descriptionEnglish: String,
    val category: String,
    val isRead: Boolean,
    val timestamp: String,
    val destination: String?,
    val spokenText: String
) {
    fun toDomain(): AppNotification = AppNotification(
        id = id,
        titleUrdu = titleUrdu,
        titleEnglish = titleEnglish,
        descriptionUrdu = descriptionUrdu,
        descriptionEnglish = descriptionEnglish,
        category = try { NotificationCategory.valueOf(category) } catch (e: Exception) { NotificationCategory.FINANCE },
        timestamp = timestamp,
        isRead = isRead,
        destination = null,
        spokenText = spokenText
    )
}

@Entity(tableName = "coach_messages")
data class CoachMessageEntity(
    @PrimaryKey val id: String,
    val textUrdu: String,
    val textRoman: String,
    val isFromCoach: Boolean,
    val timestamp: String,
    val spokenText: String
) {
    fun toDomain(): CoachMessage = CoachMessage(
        id = id,
        textUrdu = textUrdu,
        textRoman = textRoman,
        isFromCoach = isFromCoach,
        timestamp = timestamp,
        spokenText = spokenText
    )
}

@Entity(tableName = "prosperity")
data class ProsperityEntity(
    @PrimaryKey val id: String = "prosperity",
    val score: Int,
    val maxScore: Int,
    val savingsPct: Float,
    val debtControlPct: Float,
    val safetyShieldPct: Float,
    val daysRunway: Int,
    val targetRunway: Int = 30,
    val lastCalculatedAt: String?
) {
    fun toDomain(deltaMonth: String = "+6 This Month!", statusUrdu: String = "مستحکم کی طرف", statusEnglish: String = "Building Resilience", description: String = "Keep steady emergency savings to reach 70 points goal."): ProsperityScore = ProsperityScore(
        score = score,
        maxScore = maxScore,
        deltaMonth = deltaMonth,
        statusUrdu = statusUrdu,
        statusEnglish = statusEnglish,
        description = description,
        savingsPct = savingsPct,
        debtControlPct = debtControlPct,
        safetyShieldPct = safetyShieldPct,
        daysRunway = daysRunway,
        targetRunway = targetRunway
    )
}
