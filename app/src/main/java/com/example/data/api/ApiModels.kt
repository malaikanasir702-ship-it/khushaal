package com.example.data.api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegisterRequest(
    val name: String,
    val cnic: String,
    val phone: String,
    val factory: String,
    val factoryId: String? = null,
    val jazzCashNumber: String? = null,
    val bankName: String? = null,
    val bankAccountNumber: String? = null,
    val password: String,
    val preferredLanguage: String = "BILINGUAL"
)

@JsonClass(generateAdapter = true)
data class LoginRequest(
    val phone: String,
    val password: String
)

@JsonClass(generateAdapter = true)
data class RefreshTokenRequest(
    val refreshToken: String
)

@JsonClass(generateAdapter = true)
data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val user: UserProfileDto
)

@JsonClass(generateAdapter = true)
data class UserProfileDto(
    val id: String,
    val name: String,
    val urduName: String? = null,
    val greetingEnglish: String = "Assalam-o-Alaikum!",
    val greetingUrdu: String = "سلام علیکم",
    val organization: String = "",
    val verifiedText: String = "تصدیق شدہ",
    val factoryId: String = "",
    val phoneMasked: String = "",
    val cnicMasked: String = "",
    val rawPhone: String? = null,
    val rawCnic: String? = null,
    val paymentAccount: String = "JazzCash",
    val jazzCashNumber: String? = null,
    val bankName: String? = null,
    val bankAccountNumber: String? = null,
    val avatarUrl: String = "",
    val preferredLanguage: String = "BILINGUAL"
)

@JsonClass(generateAdapter = true)
data class UpdateProfileRequest(
    val name: String? = null,
    val urduName: String? = null,
    val factory: String? = null,
    val jazzCashNumber: String? = null,
    val bankName: String? = null,
    val bankAccountNumber: String? = null,
    val preferredLanguage: String? = null,
    val avatarUrl: String? = null
)

@JsonClass(generateAdapter = true)
data class CashFlowDto(
    val id: String? = null,
    val month: String,
    val income: Long,
    val incomeLabel: String? = "Salary",
    val expenses: Long,
    val expensesLabel: String? = "Expenses",
    val savings: Long,
    val available: Long
)

@JsonClass(generateAdapter = true)
data class UpdateCashFlowRequest(
    val income: Long,
    val incomeLabel: String? = null,
    val expenses: Long,
    val expensesLabel: String? = null,
    val savings: Long? = null,
    val available: Long? = null,
    val month: String? = null
)

@JsonClass(generateAdapter = true)
data class TransactionDto(
    val id: String,
    val title: String,
    val urduTitle: String? = null,
    val amount: Long,
    val isExpense: Boolean,
    val category: String,
    val envelopeId: String? = null,
    val paymentMethod: String? = "Cash",
    val date: String
)

@JsonClass(generateAdapter = true)
data class CreateTransactionRequest(
    val title: String,
    val urduTitle: String? = null,
    val amount: Long,
    val isExpense: Boolean = true,
    val category: String = "Needs",
    val envelopeId: String? = "needs",
    val paymentMethod: String? = "Cash",
    val date: String? = null
)

@JsonClass(generateAdapter = true)
data class PagedResponse<T>(
    val items: List<T>,
    val total: Int,
    val page: Int,
    val pages: Int
)

@JsonClass(generateAdapter = true)
data class EnvelopeSubItemDto(
    val label: String,
    val amount: Long
)

@JsonClass(generateAdapter = true)
data class EnvelopeDto(
    val id: String,
    val envelopeKey: String,
    val titleEnglish: String,
    val titleUrdu: String? = null,
    val percentage: Int,
    val amount: Long,
    val tag: String? = null,
    val items: List<EnvelopeSubItemDto> = emptyList()
)

@JsonClass(generateAdapter = true)
data class UpdateEnvelopeRequest(
    val percentage: Int? = null,
    val amount: Long? = null,
    val tag: String? = null,
    val titleEnglish: String? = null,
    val titleUrdu: String? = null
)

@JsonClass(generateAdapter = true)
data class KametiDto(
    val id: String,
    val name: String,
    val urduName: String? = null,
    val monthlyAmount: Long,
    val totalMembers: Int,
    val myTurnMonth: Int,
    val currentMonth: Int,
    val payoutAmount: Long? = null,
    val organizer: String? = null,
    val isPaidThisMonth: Boolean = false
)

@JsonClass(generateAdapter = true)
data class CreateKametiRequest(
    val name: String,
    val urduName: String? = null,
    val monthlyAmount: Long,
    val totalMembers: Int,
    val myTurnMonth: Int,
    val currentMonth: Int = 1,
    val organizer: String? = null
)

@JsonClass(generateAdapter = true)
data class GoalDto(
    val id: String,
    val title: String,
    val urduTitle: String? = null,
    val targetAmount: Long,
    val currentAmount: Long,
    val targetDate: String? = null,
    val emoji: String = "🎯",
    val isCompleted: Boolean = false
)

@JsonClass(generateAdapter = true)
data class CreateGoalRequest(
    val title: String,
    val urduTitle: String? = null,
    val targetAmount: Long,
    val currentAmount: Long = 0,
    val targetDate: String? = null,
    val emoji: String = "🎯"
)

@JsonClass(generateAdapter = true)
data class ContributeRequest(
    val amount: Long
)

@JsonClass(generateAdapter = true)
data class DebtDto(
    val id: String,
    val creditorName: String,
    val creditorUrdu: String? = null,
    val relationOrType: String? = null,
    val totalAmount: Long,
    val remainingAmount: Long,
    val monthlyCommitment: Long = 0,
    val urgencyLevel: String? = "MEDIUM",
    val isShariahFriendly: Boolean = true,
    val repaymentStrategyTip: String? = null
)

@JsonClass(generateAdapter = true)
data class CreateDebtRequest(
    val creditorName: String,
    val creditorUrdu: String? = null,
    val relationOrType: String? = null,
    val totalAmount: Long,
    val remainingAmount: Long? = null,
    val monthlyCommitment: Long = 0,
    val urgencyLevel: String? = "MEDIUM",
    val isShariahFriendly: Boolean = true,
    val repaymentStrategyTip: String? = null
)

@JsonClass(generateAdapter = true)
data class RepayRequest(
    val amount: Long
)

@JsonClass(generateAdapter = true)
data class BillDto(
    val id: String,
    val companyName: String,
    val companyUrdu: String? = null,
    val consumerNumber: String? = null,
    val billType: String? = "Electricity",
    val month: String? = null,
    val dueDate: String? = null,
    val amount: Long,
    val unitsConsumed: Int = 0,
    val isPaid: Boolean = false,
    val paidDate: String? = null,
    val alertTip: String? = null
)

@JsonClass(generateAdapter = true)
data class CreateBillRequest(
    val companyName: String,
    val companyUrdu: String? = null,
    val consumerNumber: String? = null,
    val billType: String? = "Electricity",
    val month: String? = null,
    val dueDate: String? = null,
    val amount: Long,
    val unitsConsumed: Int = 0,
    val alertTip: String? = null
)

@JsonClass(generateAdapter = true)
data class LockerDto(
    val balance: Long,
    val updatedAt: String? = null
)

@JsonClass(generateAdapter = true)
data class LockerTransactionRequest(
    val amount: Long
)

@JsonClass(generateAdapter = true)
data class OrderDto(
    val id: String,
    val customerName: String,
    val phone: String? = null,
    val serviceTitle: String,
    val totalAmount: Long,
    val advancePaid: Long = 0,
    val dueDate: String? = null,
    val isDelivered: Boolean = false,
    val isFullyPaid: Boolean = false
)

@JsonClass(generateAdapter = true)
data class CreateOrderRequest(
    val customerName: String,
    val phone: String? = null,
    val serviceTitle: String,
    val totalAmount: Long,
    val advancePaid: Long = 0,
    val dueDate: String? = null
)

@JsonClass(generateAdapter = true)
data class UpdateOrderRequest(
    val isDelivered: Boolean? = null,
    val isFullyPaid: Boolean? = null,
    val advancePaid: Long? = null,
    val dueDate: String? = null
)

@JsonClass(generateAdapter = true)
data class ProsperityPillarDto(
    val id: Int,
    val titleEnglish: String,
    val titleUrdu: String,
    val weightPercent: Int,
    val currentScore: Int,
    val maxScore: Int,
    val statusText: String? = "",
    val statusColorType: String = "SUCCESS"
)

@JsonClass(generateAdapter = true)
data class ProsperityDto(
    val score: Int,
    val maxScore: Int = 100,
    val savingsPct: Float = 0f,
    val debtControlPct: Float = 0f,
    val safetyShieldPct: Float = 0f,
    val daysRunway: Int = 0,
    val pillars: List<ProsperityPillarDto> = emptyList(),
    val lastCalculatedAt: String? = null
)

@JsonClass(generateAdapter = true)
data class NotificationDto(
    val id: String,
    val titleUrdu: String,
    val titleEnglish: String,
    val descriptionUrdu: String? = null,
    val descriptionEnglish: String? = null,
    val category: String = "FINANCE",
    val isRead: Boolean = false,
    val timestamp: String,
    val destination: String? = null,
    val spokenText: String? = null
)

@JsonClass(generateAdapter = true)
data class CoachMessageDto(
    val id: String,
    val textUrdu: String,
    val textRoman: String? = null,
    val isFromCoach: Boolean,
    val timestamp: String,
    val spokenText: String? = null
)

@JsonClass(generateAdapter = true)
data class SendMessageRequest(
    val textUrdu: String? = null,
    val textRoman: String? = null
)

// ─── App Config (fetched from /api/config) ────────────────────────────────────

@JsonClass(generateAdapter = true)
data class RationItemDto(
    val id: String,
    val nameUrdu: String,
    val nameEnglish: String,
    val category: String,
    val defaultQty: String,
    val unitPriceEstimate: Long,
    val marketPriceRange: String,
    val isEssential: Boolean = true,
    val savingsTip: String
)

@JsonClass(generateAdapter = true)
data class ScamSimulationDto(
    val id: String,
    val titleUrdu: String,
    val titleEnglish: String,
    val scamText: String,
    val optionSafe: String,
    val optionTrap: String,
    val audioExplanation: String
)

@JsonClass(generateAdapter = true)
data class AppConfigDto(
    val factoryName: String = "Naveena Mills Ltd.",
    val factoryUrdu: String = "نویینا ملز لمیٹڈ",
    val welfareHelpline: String = "0800-64557",
    val welfareHelplineLabel: String = "Naveena Welfare",
    val rationItems: List<RationItemDto> = emptyList(),
    val scamSimulations: List<ScamSimulationDto> = emptyList()
)
