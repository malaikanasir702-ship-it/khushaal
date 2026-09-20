package com.example.model

data class UserProfile(
  val name: String = "Ahmed Bhai",
  val urduName: String = "احمد بھائی",
  val greetingEnglish: String = "Assalam-o-Alaikum!",
  val greetingUrdu: String = "سلام علیکم",
  val organization: String = "Naveena Mills Unit 4",
  val verifiedText: String = "تصدیق شدہ",
  val factoryId: String = "NVM-4892",
  val phoneMasked: String = "+92 300 ••••491",
  val cnicMasked: String = "42101-•••••••-3",
  val paymentAccount: String = "JazzCash (0300491...)",
  val avatarUrl: String = "https://www.gstatic.com/labs-code/stitch/stitch-placeholder-300x300.svg",
)

data class CashFlowData(
  val month: String = "March 2025",
  val urduSubtitle: String = "ماہانہ آمدن و خرچ کا حساب",
  val income: Long = 55_000,
  val incomeLabel: String = "Naveena Mills Salary",
  val expenses: Long = 42_000,
  val expensesLabel: String = "Ration + Rent + Bills",
  val savings: Long = 5_000,
  val savingsLabel: String = "Kameti & Emergency",
  val available: Long = 8_000,
  val availableLabel: String = "Free for household",
)

data class ProsperityScore(
  val score: Int = 62,
  val maxScore: Int = 100,
  val deltaMonth: String = "+6 This Month!",
  val statusUrdu: String = "مستحکم کی طرف",
  val statusEnglish: String = "Building Resilience",
  val description: String = "Keep steady emergency savings to reach 70 points goal.",
  val savingsPct: Float = 0.55f,
  val debtControlPct: Float = 0.80f,
  val safetyShieldPct: Float = 0.50f,
  val daysRunway: Int = 12,
  val targetRunway: Int = 30,
)

data class ProsperityPillar(
  val id: Int,
  val titleEnglish: String,
  val titleUrdu: String,
  val weightPercent: Int,
  val currentScore: Int,
  val maxScore: Int,
  val statusText: String,
  val statusColorType: StatusType,
  val note: String,
)

enum class StatusType {
  SUCCESS,
  WARNING,
  URGENT,
}

data class Envelope(
  val id: String,
  val titleEnglish: String,
  val titleUrdu: String,
  val percentage: Int,
  val amount: Long,
  val tag: String,
  val items: List<EnvelopeSubItem> = emptyList(),
)

data class EnvelopeSubItem(
  val label: String,
  val amount: Long,
)

data class SkillOpportunity(
  val id: String,
  val nameEnglish: String,
  val nameUrdu: String,
  val iconEmoji: String,
  val roleTitle: String,
  val roleUrdu: String,
  val idealFor: String,
  val startingInvestment: String,
  val investmentNote: String,
  val monthlyProfit: String,
  val profitNote: String,
  val requirements: String,
  val matchPercentage: Int,
)

data class RoadmapStep(
  val week: Int,
  val weekLabel: String,
  val title: String,
  val description: String,
  val isCompleted: Boolean,
)

// Dedicated Page Data Models

data class TransactionItem(
  val id: String,
  val title: String,
  val urduTitle: String,
  val amount: Long,
  val isExpense: Boolean,
  val category: String,
  val envelopeId: String,
  val date: String,
  val paymentMethod: String,
)

data class KametiItem(
  val id: String,
  val name: String,
  val urduName: String,
  val monthlyAmount: Long,
  val totalMembers: Int,
  val myTurnMonth: Int,
  val currentMonth: Int,
  val payoutAmount: Long,
  val organizer: String,
  val isPaidThisMonth: Boolean,
)

data class FamilyGoalItem(
  val id: String,
  val title: String,
  val urduTitle: String,
  val targetAmount: Long,
  val currentAmount: Long,
  val targetDate: String,
  val emoji: String,
)

data class ScamSimulation(
  val id: String,
  val titleUrdu: String,
  val titleEnglish: String,
  val scamText: String,
  val optionSafe: String,
  val optionTrap: String,
  val audioExplanation: String,
  val isCompleted: Boolean = false,
  val isCorrect: Boolean? = null,
)

data class CoachMessage(
  val id: String,
  val textUrdu: String,
  val textRoman: String,
  val isFromCoach: Boolean,
  val timestamp: String,
  val spokenText: String,
)

data class CustomerOrder(
  val id: String,
  val customerName: String,
  val phone: String,
  val serviceTitle: String,
  val totalAmount: Long,
  val advancePaid: Long,
  val dueDate: String,
  val isDelivered: Boolean,
  val isFullyPaid: Boolean,
)

enum class AppTab {
  HOME,
  MONEY,
  PROSPERITY,
  EARN_MORE,
}

enum class AppLanguage {
  BILINGUAL,
  URDU,
  ENGLISH,
}

enum class NotificationCategory {
  FACTORY,
  FINANCE,
  SECURITY,
  COACH,
}

data class AppNotification(
  val id: String,
  val titleUrdu: String,
  val titleEnglish: String,
  val descriptionUrdu: String,
  val descriptionEnglish: String,
  val category: NotificationCategory,
  val timestamp: String,
  val isRead: Boolean = false,
  val actionLabelUrdu: String? = null,
  val actionLabelEnglish: String? = null,
  val destination: AppDestination? = null,
  val spokenText: String,
)

data class FactoryPayslip(
  val month: String = "مارچ 2025 (March 2025)",
  val employeeName: String = "Ahmed Raza (احمد رضا)",
  val employeeId: String = "NVM-4892",
  val department: String = "Weaving Unit 4 • شفٹ بی",
  val daysPresent: Int = 26,
  val daysAbsent: Int = 0,
  val overtimeHours: Double = 18.5,
  val baseWage: Long = 38_000,
  val overtimePay: Long = 8_200,
  val attendanceBonus: Long = 4_500,
  val productionBonus: Long = 4_300,
  val totalGrossWage: Long = 55_000,
  val eobiDeduction: Long = 650,
  val messAdvanceDeduction: Long = 1_500,
  val unionFundDeduction: Long = 100,
  val totalDeductions: Long = 2_250,
  val netTakeHome: Long = 52_750,
  val paymentStatus: String = "منتقل شدہ (Credited to JazzCash)",
  val creditedDate: String = "01 مارچ 2025",
  val disbursementAccount: String = "JazzCash ••••491"
)

data class RationItemEstimate(
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

data class UtilityBill(
  val id: String,
  val companyName: String,
  val companyUrdu: String,
  val consumerNumber: String,
  val billType: String,
  val month: String,
  val dueDate: String,
  val amount: Long,
  val unitsConsumed: Int = 0,
  val isPaid: Boolean = false,
  val paidDate: String? = null,
  val alertTip: String = ""
)

data class DebtItem(
  val id: String,
  val creditorName: String,
  val creditorUrdu: String,
  val relationOrType: String,
  val totalAmount: Long,
  val remainingAmount: Long,
  val monthlyCommitment: Long,
  val urgencyLevel: String,
  val isShariahFriendly: Boolean = true,
  val repaymentStrategyTip: String = ""
)

sealed class AppDestination {
  data object TabView : AppDestination()
  data object GoalsAndKameti : AppDestination()
  data object TransactionHistory : AppDestination()
  data object EmergencyLocker : AppDestination()
  data object FraudAcademy : AppDestination()
  data object CoachChat : AppDestination()
  data object ProfileSettings : AppDestination()
  data object BusinessKhata : AppDestination()
  data object Notifications : AppDestination()
  data object FactorySalarySlip : AppDestination()
  data object RationCalculator : AppDestination()
  data object UtilityBills : AppDestination()
  data object DebtSnowball : AppDestination()
  data object EnvelopeSplit : AppDestination()
}
