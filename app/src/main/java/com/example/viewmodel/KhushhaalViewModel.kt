package com.example.viewmodel

import android.app.Application
import android.speech.tts.TextToSpeech
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.AppDestination
import com.example.model.AppLanguage
import com.example.model.AppNotification
import com.example.model.AppTab
import com.example.model.CashFlowData
import com.example.model.CoachMessage
import com.example.model.CustomerOrder
import com.example.model.DebtItem
import com.example.model.Envelope
import com.example.model.EnvelopeSubItem
import com.example.model.FactoryPayslip
import com.example.model.FamilyGoalItem
import com.example.model.KametiItem
import com.example.model.NotificationCategory
import com.example.model.ProsperityPillar
import com.example.model.ProsperityScore
import com.example.model.RationItemEstimate
import com.example.model.RoadmapStep
import com.example.model.ScamSimulation
import com.example.model.SkillOpportunity
import com.example.model.StatusType
import com.example.model.TransactionItem
import com.example.model.UserProfile
import com.example.model.UtilityBill
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class KhushhaalViewModel(application: Application) : AndroidViewModel(application), TextToSpeech.OnInitListener {

  private val _currentTab = MutableStateFlow(AppTab.HOME)
  val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

  private val _currentDestination = MutableStateFlow<AppDestination>(AppDestination.TabView)
  val currentDestination: StateFlow<AppDestination> = _currentDestination.asStateFlow()

  private val _userProfile = MutableStateFlow(UserProfile())
  val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

  private val _cashFlow = MutableStateFlow(CashFlowData())
  val cashFlow: StateFlow<CashFlowData> = _cashFlow.asStateFlow()

  private val _prosperityScore = MutableStateFlow(ProsperityScore())
  val prosperityScore: StateFlow<ProsperityScore> = _prosperityScore.asStateFlow()

  private val _currentLanguage = MutableStateFlow(AppLanguage.BILINGUAL)
  val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

  private val _isQuickExpenseSheetOpen = MutableStateFlow(false)
  val isQuickExpenseSheetOpen: StateFlow<Boolean> = _isQuickExpenseSheetOpen.asStateFlow()

  private val _isLanguageSheetOpen = MutableStateFlow(false)
  val isLanguageSheetOpen: StateFlow<Boolean> = _isLanguageSheetOpen.asStateFlow()

  private val _payslip = MutableStateFlow(FactoryPayslip())
  val payslip: StateFlow<FactoryPayslip> = _payslip.asStateFlow()

  private val _rationEstimates = MutableStateFlow(
    listOf(
      RationItemEstimate(
        id = "r-1",
        nameUrdu = "گندم کا آٹا (چکی والا)",
        nameEnglish = "Chakki Wheat Flour (20kg Bag)",
        category = "بنیادی اناج (Grains)",
        defaultQty = "20 کلو",
        unitPriceEstimate = 2_700,
        marketPriceRange = "Rs. 2,600 - 2,850",
        isEssential = true,
        savingsTip = "یوٹیلیٹی اسٹور یا سستی اناج دکان سے خریدنے پر 300 روپے کی بچت ہوتی ہے۔"
      ),
      RationItemEstimate(
        id = "r-2",
        nameUrdu = "کوکنگ آئل / بناسپتی گھی",
        nameEnglish = "Cooking Oil / Banaspati Ghee (5 Litre)",
        category = "روغن و چکنائی (Oils)",
        defaultQty = "5 لیٹر پیک",
        unitPriceEstimate = 2_450,
        marketPriceRange = "Rs. 2,350 - 2,600",
        isEssential = true,
        savingsTip = "کھلے گھی کی بجائے معیاری تصدیق شدہ پیک لیں، کم مقدار میں استعمال صحت اور جیب دونوں کے لیے بہتر ہے۔"
      ),
      RationItemEstimate(
        id = "r-3",
        nameUrdu = "چاول (کرنل باسمتی ٹوٹا)",
        nameEnglish = "Basmati Broken Rice (5kg)",
        category = "بنیادی اناج (Grains)",
        defaultQty = "5 کلو",
        unitPriceEstimate = 1_400,
        marketPriceRange = "Rs. 1,300 - 1,550",
        isEssential = true,
        savingsTip = "ماہانہ تھوک مارکیٹ (جوڑیا بازار / غلہ منڈی) سے 10 کلو کا تھیلا لینے سے فی کلو 30 روپے بچتے ہیں۔"
      ),
      RationItemEstimate(
        id = "r-4",
        nameUrdu = "دال چنا و دال مونگ",
        nameEnglish = "Chana & Moong Pulses (2kg)",
        category = "دالیں و پروٹین (Pulses)",
        defaultQty = "2 کلو",
        unitPriceEstimate = 720,
        marketPriceRange = "Rs. 680 - 780",
        isEssential = true,
        savingsTip = "ہفتے میں 3 دن دال اور سبزی کا استعمال گوشت کے اخراجات میں 4,000 روپے ماہانہ کمی لاتا ہے۔"
      ),
      RationItemEstimate(
        id = "r-5",
        nameUrdu = "چینی (صاف سفید)",
        nameEnglish = "White Sugar (3kg)",
        category = "مٹھاس و چائے (Pantry)",
        defaultQty = "3 کلو",
        unitPriceEstimate = 480,
        marketPriceRange = "Rs. 450 - 520",
        isEssential = true,
        savingsTip = "شکر اور گڑ کا متبادل استعمال صحت مند ہے اور چینی پر انحصار کم کرتا ہے۔"
      ),
      RationItemEstimate(
        id = "r-6",
        nameUrdu = "دانے دار چائے کی پتی",
        nameEnglish = "Danedar Black Tea (400g)",
        category = "مٹھاس و چائے (Pantry)",
        defaultQty = "400 گرام",
        unitPriceEstimate = 650,
        marketPriceRange = "Rs. 620 - 700",
        isEssential = true,
        savingsTip = "بڑے فیملی پیک میں بچت زیادہ ہوتی ہے۔"
      ),
      RationItemEstimate(
        id = "r-7",
        nameUrdu = "خشک دودھ / تازہ دودھ",
        nameEnglish = "Fresh & Powder Milk",
        category = "ڈیری و دودھ (Dairy)",
        defaultQty = "ماہانہ تخمینہ",
        unitPriceEstimate = 4_800,
        marketPriceRange = "Rs. 4,500 - 5,200",
        isEssential = true,
        savingsTip = "بچوں کی غذائیت کے لیے لازمی ہے، اس میں کٹوتی مت کریں۔"
      ),
      RationItemEstimate(
        id = "r-8",
        nameUrdu = "صابن، سرف و صفائی کا سامان",
        nameEnglish = "Washing Powder & Soaps",
        category = "گھریلو صفائی (Hygiene)",
        defaultQty = "1 ماہ پیک",
        unitPriceEstimate = 1_650,
        marketPriceRange = "Rs. 1,500 - 1,800",
        isEssential = false,
        savingsTip = "لوکل معیاری بار سوپ اور بڑا واشنگ پاؤڈر پیک لینے پر 25% کم خرچ آتا ہے۔"
      )
    )
  )
  val rationEstimates: StateFlow<List<RationItemEstimate>> = _rationEstimates.asStateFlow()

  private val _utilityBills = MutableStateFlow(
    listOf(
      UtilityBill(
        id = "bill-1",
        companyName = "K-Electric (Electricity)",
        companyUrdu = "کے الیکٹرک بجلی بل",
        consumerNumber = "040001893421",
        billType = "بجلی (Electricity)",
        month = "مارچ 2025",
        dueDate = "28 مارچ 2025",
        amount = 5_840,
        unitsConsumed = 184,
        isPaid = false,
        alertTip = "200 یونٹ سے کم رہنے پر لائف لائن ٹیرف سبسڈی برقرار رہتی ہے اور فی یونٹ 12 روپے کم چارج ہوتا ہے۔"
      ),
      UtilityBill(
        id = "bill-2",
        companyName = "SSGC (Sui Southern Gas)",
        companyUrdu = "سوئی سدرن گیس بل",
        consumerNumber = "9182347102",
        billType = "سوئی گیس (Gas)",
        month = "مارچ 2025",
        dueDate = "24 مارچ 2025",
        amount = 1_420,
        unitsConsumed = 58,
        isPaid = true,
        paidDate = "18 مارچ 2025",
        alertTip = "گیس کی ادائیگی بروقت ادا ہو چکی ہے، لیٹ سرچارج کی بچت ہوئی۔"
      ),
      UtilityBill(
        id = "bill-3",
        companyName = "KW&SB (Karachi Water)",
        companyUrdu = "پانی کا سرکاری بل",
        consumerNumber = "KW-8841-KOR",
        billType = "پانی (Water)",
        month = "فروری-مارچ 2025",
        dueDate = "30 مارچ 2025",
        amount = 650,
        unitsConsumed = 0,
        isPaid = false,
        alertTip = "تین ماہ اکٹھا بل ادا کرنے کی بجائے ماہانہ جمع کرائیں تاکہ بڑا بوجھ نہ بنے۔"
      )
    )
  )
  val utilityBills: StateFlow<List<UtilityBill>> = _utilityBills.asStateFlow()

  private val _debts = MutableStateFlow(
    listOf(
      DebtItem(
        id = "debt-1",
        creditorName = "Chacha Rasheed (Kiryana Store)",
        creditorUrdu = "چچا رشید کریانہ اسٹور",
        relationOrType = "محلہ دکان دار ادھار (Pantry Credit)",
        totalAmount = 8_500,
        remainingAmount = 3_500,
        monthlyCommitment = 2_000,
        urgencyLevel = "اعلیٰ (ضروری برائے عزت و ساکھ)",
        isShariahFriendly = true,
        repaymentStrategyTip = "سنو بال کا پہلا ہدف: یہ رقم چھوٹی ہے، اگلے پندرہ دن میں ختم کر کے سود فری ذہنی سکون حاصل کریں۔"
      ),
      DebtItem(
        id = "debt-2",
        creditorName = "Tariq Brother (Motorcycle Repair)",
        creditorUrdu = "طارق بھائی (موٹرسائیکل کام)",
        relationOrType = "ورکشاپ ہنگامی کام (Workshop Due)",
        totalAmount = 4_000,
        remainingAmount = 2_000,
        monthlyCommitment = 1_000,
        urgencyLevel = "درمیانہ (دوستانہ قرض)",
        isShariahFriendly = true,
        repaymentStrategyTip = "تنخواہ کے اوور ٹائم سے 2,000 روپے دے کر اس کھاتے کو مکمل بند کر دیں۔"
      ),
      DebtItem(
        id = "debt-3",
        creditorName = "Akhuwat Islamic Microfinance",
        creditorUrdu = "اخوت بلاسود قرضہ حسنہ",
        relationOrType = "بلاسود چھوٹی فنانسنگ (Zero-Markup Loan)",
        totalAmount = 30_000,
        remainingAmount = 14_000,
        monthlyCommitment = 2_000,
        urgencyLevel = "باقاعدہ ماہانہ قسط",
        isShariahFriendly = true,
        repaymentStrategyTip = "ماہانہ 2,000 بروقت قسط دینے سے اخوت میں آئندہ کاروبار کے لیے 1 لاکھ روپے کا قرض حسنہ اہل ہو جائے گا۔"
      )
    )
  )
  val debts: StateFlow<List<DebtItem>> = _debts.asStateFlow()

  private val _notifications = MutableStateFlow(
    listOf(
      AppNotification(
        id = "notif-1",
        titleUrdu = "نوینہ ٹیکسٹائل ملز: ماہانہ تنخواہ اور بونس منتقل ہو گئی",
        titleEnglish = "Naveena Mills: Monthly Salary & Production Bonus Credited",
        descriptionUrdu = "آپ کے رجسٹرڈ JazzCash اکاؤنٹ میں روپے 58,000 بحفاظت ٹرانسفر کر دیے گئے ہیں۔ اوور ٹائم روپے 3,000 شامل ہے۔",
        descriptionEnglish = "Rs. 58,000 transferred to JazzCash including Rs. 3,000 overtime bonus.",
        category = NotificationCategory.FACTORY,
        timestamp = "آج صبح 9:15",
        isRead = false,
        actionLabelUrdu = "ٹرانزیکشن دیکھیں",
        actionLabelEnglish = "View Ledger",
        destination = AppDestination.TransactionHistory,
        spokenText = "Naveena Mills monthly salary 58 thousand rupees credited to your JazzCash account."
      ),
      AppNotification(
        id = "notif-2",
        titleUrdu = "محلہ کمیٹی کی قسط کی تاریخ قریب ہے",
        titleEnglish = "Kameti Installment Due Alert",
        descriptionUrdu = "کمیٹی نمبر 2 (روپے 3,500) کی ادائیگی کی آخری تاریخ 25 مارچ ہے۔ بروقت ادائیگی سے ساکھ برقرار رہتی ہے۔",
        descriptionEnglish = "Committee installment Rs. 3,500 due on 25th March. Timely payment protects financial trust.",
        category = NotificationCategory.FINANCE,
        timestamp = "آج صبح 11:30",
        isRead = false,
        actionLabelUrdu = "کمیٹی مینیجر",
        actionLabelEnglish = "Open Kameti",
        destination = AppDestination.GoalsAndKameti,
        spokenText = "Kameti installment 3,500 rupees due on 25th March. Remember to pay on time."
      ),
      AppNotification(
        id = "notif-3",
        titleUrdu = "اسٹیٹ بینک آف پاکستان: جعلی OTP کالز سے ہوشیار رہیں",
        titleEnglish = "State Bank Fraud Advisory: Fake OTP Calls",
        descriptionUrdu = "کسی بھی صورت میں فون کال پر اپنا 4 یا 6 ہندسوں کا پن کوڈ یا او ٹی پی مت بتائیں۔ بینک یا فیکٹری انتظامیہ کبھی فون پر پاسورڈ نہیں مانگتی۔",
        descriptionEnglish = "Never share OTP or PIN over telephone call. Official banks never ask for passwords.",
        category = NotificationCategory.SECURITY,
        timestamp = "کل شام 6:00",
        isRead = false,
        actionLabelUrdu = "فراڈ شیلڈ ٹیسٹ",
        actionLabelEnglish = "Shield Academy",
        destination = AppDestination.FraudAcademy,
        spokenText = "State bank advisory: Never share OTP or PIN codes over mobile calls."
      ),
      AppNotification(
        id = "notif-4",
        titleUrdu = "کوچ فاطمہ: ہنگامی فنڈ کا سنگ میل",
        titleEnglish = "Coach Fatima: Emergency Locker Milestone",
        descriptionUrdu = "ماشاءاللہ آپ کا 12 دن کا فنڈ محفوظ ہو چکا ہے۔ اگر اس ماہ 1,000 روپے مزید شامل کریں تو 15 دن کا سنگ میل مکمل ہو جائے گا۔",
        descriptionEnglish = "MashaAllah you have achieved 12 days safety runway! Add Rs. 1,000 to reach the 15-day mark.",
        category = NotificationCategory.COACH,
        timestamp = "2 دن قبل",
        isRead = true,
        actionLabelUrdu = "ہنگامی لاکر",
        actionLabelEnglish = "Open Locker",
        destination = AppDestination.EmergencyLocker,
        spokenText = "Coach Fatima advice: Add 1,000 rupees to reach 15 days family emergency buffer."
      ),
      AppNotification(
        id = "notif-5",
        titleUrdu = "کاروباری رجسٹر: زبیر بھائی کے سوٹ کی تاریخ",
        titleEnglish = "Customer Order Due: Cotton Kurtas",
        descriptionUrdu = "زبیر بھائی (ویونگ ڈیپارٹمنٹ) کا 3 کاٹن کرتے کی سلائی کا آرڈر کل 22 مارچ کو مکمل کر کے دینا ہے۔ بقایا رقم روپے 2,100 وصول کرنی ہے۔",
        descriptionEnglish = "Zubair Bhai 3 cotton kurta order due on 22nd March. Remaining balance to collect: Rs. 2,100.",
        category = NotificationCategory.FINANCE,
        timestamp = "3 دن قبل",
        isRead = true,
        actionLabelUrdu = "آرڈر بک",
        actionLabelEnglish = "Open Khata",
        destination = AppDestination.BusinessKhata,
        spokenText = "Customer order reminder: Zubair Bhai suits due on 22nd March with balance 2,100 rupees."
      )
    )
  )
  val notifications: StateFlow<List<AppNotification>> = _notifications.asStateFlow()

  private val _pillars = MutableStateFlow(
    listOf(
      ProsperityPillar(
        id = 1,
        titleEnglish = "Budgeting (بجٹ پلان)",
        titleUrdu = "بجٹ پلان",
        weightPercent = 20,
        currentScore = 16,
        maxScore = 20,
        statusText = "On Track ✓",
        statusColorType = StatusType.SUCCESS,
        note = "Monthly tracking active: All major expenses recorded."
      ),
      ProsperityPillar(
        id = 2,
        titleEnglish = "Savings Habit (بچت کی عادت)",
        titleUrdu = "بچت کی عادت",
        weightPercent = 20,
        currentScore = 11,
        maxScore = 20,
        statusText = "Growing",
        statusColorType = StatusType.WARNING,
        note = "Goal: 10% of monthly salary (Kameti or Bank account)."
      ),
      ProsperityPillar(
        id = 3,
        titleEnglish = "Emergency Buffer (ہنگامی تحفظ)",
        titleUrdu = "ہنگامی تحفظ",
        weightPercent = 20,
        currentScore = 10,
        maxScore = 20,
        statusText = "12 Days Safe",
        statusColorType = StatusType.WARNING,
        note = "Current: 12 days safety buffer; Target: 30 days."
      ),
      ProsperityPillar(
        id = 4,
        titleEnglish = "Debt Control (قرض کی حالت)",
        titleUrdu = "قرض کی حالت",
        weightPercent = 15,
        currentScore = 12,
        maxScore = 15,
        statusText = "Protected ✓",
        statusColorType = StatusType.SUCCESS,
        note = "No high-interest committee or private loans active."
      ),
      ProsperityPillar(
        id = 5,
        titleEnglish = "Digital Safety (فراڈ سے حفاظت)",
        titleUrdu = "فراڈ سے حفاظت",
        weightPercent = 10,
        currentScore = 9,
        maxScore = 10,
        statusText = "Shield On ✓",
        statusColorType = StatusType.SUCCESS,
        note = "Passed OTP fraud shield quiz successfully."
      ),
      ProsperityPillar(
        id = 6,
        titleEnglish = "Income Resilience (آمدنی کا تنوع)",
        titleUrdu = "آمدنی کا تنوع",
        weightPercent = 15,
        currentScore = 4,
        maxScore = 15,
        statusText = "Action Needed",
        statusColorType = StatusType.URGENT,
        note = "Single earner in household: Ghar mein sirf 1 kafalat hai."
      )
    )
  )
  val pillars: StateFlow<List<ProsperityPillar>> = _pillars.asStateFlow()

  private val _envelopes = MutableStateFlow(
    listOf(
      Envelope(
        id = "needs",
        titleEnglish = "1. ضروری اخراجات (Household Needs)",
        titleUrdu = "ضروری اخراجات",
        percentage = 70,
        amount = 38_500,
        tag = "Fixed family requirements",
        items = listOf(
          EnvelopeSubItem("آٹا راشن", 20_000),
          EnvelopeSubItem("کرایہ و بل", 14_000),
          EnvelopeSubItem("اسکول فیس", 4_500)
        )
      ),
      Envelope(
        id = "commitments",
        titleEnglish = "2. کمیٹی و واجبات (Commitments)",
        titleUrdu = "کمیٹی و واجبات",
        percentage = 6,
        amount = 3_500,
        tag = "تاریخ تک ادا کریں 25",
        items = listOf(
          EnvelopeSubItem("محلہ کمیٹی", 3_500)
        )
      ),
      Envelope(
        id = "emergency",
        titleEnglish = "3. ہنگامی تحفظ (Emergency Fund)",
        titleUrdu = "ہنگامی تحفظ",
        percentage = 6,
        amount = 3_000,
        tag = "محفوظ رقم JazzCash / Bank",
        items = listOf(
          EnvelopeSubItem("حفاظتی ڈھال", 3_000)
        )
      ),
      Envelope(
        id = "savings",
        titleEnglish = "4. دستیاب بچت (Savings & Buffer)",
        titleUrdu = "دستیاب بچت",
        percentage = 18,
        amount = 10_000,
        tag = "مستقبل کے لیے",
        items = listOf(
          EnvelopeSubItem("گھرانہ بچت", 10_000)
        )
      )
    )
  )
  val envelopes: StateFlow<List<Envelope>> = _envelopes.asStateFlow()

  // Transactions History & Khata State
  private val _transactions = MutableStateFlow(
    listOf(
      TransactionItem("tx-1", "Naveena Mills Salary Credited", "فیکٹری تنخواہ موصول", 55_000, false, "Salary", "income", "15 Mar 2025", "HBL Direct"),
      TransactionItem("tx-2", "Atta & Monthly Ration", "آٹا و ماہانہ راشن", 20_000, true, "Groceries", "needs", "16 Mar 2025", "Cash"),
      TransactionItem("tx-3", "House Rent & Electricity Bill", "مکان کرایہ و بجلی بل", 14_000, true, "Utilities", "needs", "17 Mar 2025", "JazzCash"),
      TransactionItem("tx-4", "Mahalla Kameti Payment", "محلہ کمیٹی قسط", 3_500, true, "Kameti", "commitments", "18 Mar 2025", "Cash"),
      TransactionItem("tx-5", "Emergency Buffer Deposit", "ہنگامی فنڈ جمع", 3_000, true, "Emergency", "emergency", "18 Mar 2025", "JazzCash Vault"),
      TransactionItem("tx-6", "Children School Books & Fee", "اسکول فیس و کتابیں", 4_500, true, "Education", "needs", "19 Mar 2025", "Cash"),
      TransactionItem("tx-7", "Medical / Clinic Visit", "کلینک معائنہ و دوا", 1_200, true, "Healthcare", "needs", "19 Mar 2025", "Cash")
    )
  )
  val transactions: StateFlow<List<TransactionItem>> = _transactions.asStateFlow()

  // Kametis State
  private val _kametis = MutableStateFlow(
    listOf(
      KametiItem(
        id = "k-1",
        name = "Mahalla Elders 10-Month Kameti",
        urduName = "محلہ کمیٹی (دس ماہانہ)",
        monthlyAmount = 3_500,
        totalMembers = 10,
        myTurnMonth = 7,
        currentMonth = 4,
        payoutAmount = 35_000,
        organizer = "Chachi Nighat (نکہت چچی)",
        isPaidThisMonth = true
      ),
      KametiItem(
        id = "k-2",
        name = "Naveena Mills Weaving Section Kameti",
        urduName = "فیکٹری ویونگ یونٹ کمیٹی",
        monthlyAmount = 5_000,
        totalMembers = 12,
        myTurnMonth = 9,
        currentMonth = 3,
        payoutAmount = 60_000,
        organizer = "Ustad Tariq (استاد طارق)",
        isPaidThisMonth = false
      )
    )
  )
  val kametis: StateFlow<List<KametiItem>> = _kametis.asStateFlow()

  // Goals State
  private val _goals = MutableStateFlow(
    listOf(
      FamilyGoalItem("g-1", "Children Matriculation Education Fund", "بچوں کی میٹرک فیس", 25_000, 14_500, "Nov 2025", "🎓"),
      FamilyGoalItem("g-2", "Gold Sovereign Reserve (1 Tola)", "ایک تولہ سونے کا تحفظ", 280_000, 95_000, "Dec 2026", "🪙"),
      FamilyGoalItem("g-3", "Home Solar Backup Plate & Battery", "سولر پنکھا و بیٹری", 45_000, 22_000, "Jun 2025", "☀️")
    )
  )
  val goals: StateFlow<List<FamilyGoalItem>> = _goals.asStateFlow()

  // Emergency Locker State
  private val _emergencyLockerBalance = MutableStateFlow(18_500L)
  val emergencyLockerBalance: StateFlow<Long> = _emergencyLockerBalance.asStateFlow()

  // Fraud Academy Scenarios
  private val _scamSimulations = MutableStateFlow(
    listOf(
      ScamSimulation(
        id = "scam-1",
        titleUrdu = "بینک منیجر کی جعلی کال",
        titleEnglish = "Fake Bank Manager OTP Call",
        scamText = "کال کرنے والا بولتا ہے: 'میں اسٹیٹ بینک ہیڈ آفس سے ہوں، آپ کا ATM کارڈ بلاک ہو گیا ہے، ابھی آئے ہوئے 4 ہندسوں کا کوڈ بتائیں!'",
        optionSafe = "فوراً کال کاٹیں! بینک کبھی فون پر OTP نہیں مانگتا ✓",
        optionTrap = "جلدی میں کارڈ چالو کروانے کے لیے کوڈ بتا دیں",
        audioExplanation = "Khabardaar! Kisi ko bhi call par OTP ya PIN mat dein. Bank ya JazzCash kabhi phone par password nahi maangtay."
      ),
      ScamSimulation(
        id = "scam-2",
        titleUrdu = "بے نظیر انکم سپورٹ یا انعامی میسج",
        titleEnglish = "Fake BISP / Cash Prize SMS",
        scamText = "میسج: 'مبارک ہو! آپ کا 25,000 روپے کا وظیفہ منظور ہو گیا ہے۔ حاصل کرنے کے لیے اس نمبر پر 1,000 کا ایزی لوڈ بھیجیں۔'",
        optionSafe = "فراڈ میسج ڈیلیٹ کریں اور کوئی پیسے نہ بھیجیں ✓",
        optionTrap = "پچیس ہزار کے لالچ میں ایک ہزار کا لوڈ کروا دیں",
        audioExplanation = "Yeh jaali SMS hota ha. Kisi bhi sarkari imdad ya inaam ke liye pehlay paisay nahi maangay jatay."
      ),
      ScamSimulation(
        id = "scam-3",
        titleUrdu = "غلطی سے رقم ٹرانسفر کا فراڈ",
        titleEnglish = "Fake Accidental Transfer Trap",
        scamText = "ایک اجنبی کہتا ہے: 'بھائی غلطی سے آپ کے اکاؤنٹ میں 5,000 روپے آ گئے ہیں، برائے مہربانی مجھے واپس بھیج دیں۔' جبکہ اصلی SMS میں پیسے نہیں آئے۔",
        optionSafe = "پہلے اپنا اصلی بینک بیلنس ایپ یا ہیلپ لائن سے چیک کریں ✓",
        optionTrap = "بغیر بیلنس دیکھے اپنے اصل پیسے واپس بھیج دیں",
        audioExplanation = "Fasadi log farzi SMS bhej kar aap se asli paisay mangwatay hain. Hamesha apna balance pehlay check karein."
      ),
      ScamSimulation(
        id = "scam-4",
        titleUrdu = "آن لائن سود خور ایپس کا شکنجہ",
        titleEnglish = "Predatory Quick Loan App Trap",
        scamText = "فیس بک اشتہار: 'صرف شناختی کارڈ پر 20 ہزار فوری قرض حاصل کریں۔' پھر ہفتے بعد 40 ہزار مانگتے ہیں اور رشتہ داروں کو کالیں کرتے ہیں۔",
        optionSafe = "ایسی غیر قانونی ایپس ہرگز ڈاؤنلوڈ نہ کریں ✓",
        optionTrap = "جلدی میں ذاتی کانٹیکٹس اور تصویریں دے کر قرض لے لیں",
        audioExplanation = "Soodi qarz apps aap ke mobile ka data chura kar blackmail karti hain. Sirf factory ya ba-zabitah idaron se rabta karein."
      )
    )
  )
  val scamSimulations: StateFlow<List<ScamSimulation>> = _scamSimulations.asStateFlow()

  // Coach Fatima Chat Messages
  private val _coachMessages = MutableStateFlow(
    listOf(
      CoachMessage(
        id = "msg-1",
        textUrdu = "السلام علیکم احمد بھائی! میں آپ کی مالیاتی کوچ فاطمہ ہوں۔ آپ اپنے بجٹ، کمیٹی، یا نئی آمدنی کے بارے میں کچھ بھی پوچھ سکتے ہیں۔",
        textRoman = "Assalam-o-Alaikum Ahmed Bhai! Main aap ki maliyati coach Fatima hoon. Aap budget, kameti, ya aamdani barhanay ka mashwara le saktay hain.",
        isFromCoach = true,
        timestamp = "10:30 AM",
        spokenText = "Assalam-o-Alaikum Ahmed Bhai! Main aap ki maliyati coach Fatima hoon."
      ),
      CoachMessage(
        id = "msg-2",
        textUrdu = "ماشاءاللہ آپ نے اس ماہ راشن اور کرائے کا حساب صحیح درج کیا ہے۔ اگلا ہدف ہنگامی فنڈ کو 30 دن تک لے جانا ہے۔",
        textRoman = "MashaAllah aap ne is maah ration aur kiraye ka hisab sahi darj kia hai. Agla hadaf emergency fund ko tees din tak le jana hai.",
        isFromCoach = true,
        timestamp = "10:32 AM",
        spokenText = "MashaAllah aap ne is maah ration aur kiraye ka hisab sahi darj kia hai."
      )
    )
  )
  val coachMessages: StateFlow<List<CoachMessage>> = _coachMessages.asStateFlow()

  // Customer Orders (Microenterprise Khata)
  private val _customerOrders = MutableStateFlow(
    listOf(
      CustomerOrder("ord-1", "Zubair Bhai (Weaving Dept)", "0301-7821941", "3 Cotton Kurta Stitching", 3_600, 1_500, "22 Mar 2025", isDelivered = false, isFullyPaid = false),
      CustomerOrder("ord-2", "Baji Nasreen (Colony 2)", "0322-4419201", "Bridal Suit Alteration & Lace", 1_800, 1_800, "20 Mar 2025", isDelivered = true, isFullyPaid = true),
      CustomerOrder("ord-3", "Supervisor Aslam", "0345-9921021", "2 School Uniforms Suit", 2_400, 1_000, "25 Mar 2025", isDelivered = false, isFullyPaid = false)
    )
  )
  val customerOrders: StateFlow<List<CustomerOrder>> = _customerOrders.asStateFlow()

  val availableSkills = listOf(
    SkillOpportunity(
      id = "tailoring",
      nameEnglish = "Silai Karhai",
      nameUrdu = "سلائی کڑھائی",
      iconEmoji = "✂️",
      roleTitle = "Home Tailoring & Alteration",
      roleUrdu = "گھریلو سلائی اور الٹریشن سروس",
      idealFor = "Ideal for: Spouse (بیگم) • 2–3 hrs/day",
      startingInvestment = "Rs. 4,500",
      investmentNote = "Machine tune-up + threads",
      monthlyProfit = "Rs. 12k – 18k",
      profitNote = "15–20 suits / month",
      requirements = "ضروری سامان: گھر پر سلائی مشین اور محلے کے 10 جاننے والے افراد",
      matchPercentage = 94
    ),
    SkillOpportunity(
      id = "food",
      nameEnglish = "Khana & Baking",
      nameUrdu = "کھانا پکانا و نان بائی",
      iconEmoji = "🍲",
      roleTitle = "Home Catering & Dabba Service",
      roleUrdu = "گھریلو کھانا و لنچ ڈبہ سروس",
      idealFor = "Ideal for: Household • Morning Prep",
      startingInvestment = "Rs. 3,000",
      investmentNote = "Utensils & first batch ration",
      monthlyProfit = "Rs. 10k – 15k",
      profitNote = "10–12 regular mill lunch boxes",
      requirements = "ضروری سامان: صاف کچن، ڈبے اور فیکٹری ورکرز نیٹ ورک",
      matchPercentage = 88
    ),
    SkillOpportunity(
      id = "tuition",
      nameEnglish = "Tuition / Teaching",
      nameUrdu = "ٹیوشن پڑھانا",
      iconEmoji = "🎓",
      roleTitle = "Neighborhood Primary Tuition Center",
      roleUrdu = "محلہ پرائمری ٹیوشن سینٹر",
      idealFor = "Ideal for: Educated Child / Sister",
      startingInvestment = "Rs. 1,000",
      investmentNote = "Whiteboard & markers",
      monthlyProfit = "Rs. 8k – 14k",
      profitNote = "6–10 neighborhood students",
      requirements = "ضروری سامان: بیٹھنے کی چٹائی، وائٹ بورڈ اور پرائمری نصاب",
      matchPercentage = 85
    ),
    SkillOpportunity(
      id = "driving",
      nameEnglish = "Driving / Delivery",
      nameUrdu = "ڈرائیونگ و سواری",
      iconEmoji = "🛵",
      roleTitle = "Part-time Ride & Delivery Route",
      roleUrdu = "پارٹ ٹائم سواری و ڈلیوری سروس",
      idealFor = "Ideal for: Worker evening shifts",
      startingInvestment = "Rs. 2,500",
      investmentNote = "Helmet + mobile mount + fuel",
      monthlyProfit = "Rs. 15k – 22k",
      profitNote = "2–3 hrs daily after factory shift",
      requirements = "ضروری سامان: موٹر سائیکل، ڈرائیونگ لائسنس اور اسمارٹ فون",
      matchPercentage = 82
    ),
    SkillOpportunity(
      id = "beauty",
      nameEnglish = "Beauty Parlor",
      nameUrdu = "بیوٹی پارلر سروسز",
      iconEmoji = "✨",
      roleTitle = "Home Beauty & Mehendi Service",
      roleUrdu = "گھر پر بیوٹی و مہندی سروس",
      idealFor = "Ideal for: Female household members",
      startingInvestment = "Rs. 5,000",
      investmentNote = "Basic parlor kit & mehendi cones",
      monthlyProfit = "Rs. 12k – 20k",
      profitNote = "Weddings, Eid and daily styling",
      requirements = "ضروری سامان: مہندی، فیشل کٹ اور خواتین گاہکوں کا اعتماد",
      matchPercentage = 80
    ),
    SkillOpportunity(
      id = "mobile",
      nameEnglish = "Mobile Repair",
      nameUrdu = "موبائل ریپئرنگ",
      iconEmoji = "📱",
      roleTitle = "Screen & Accessory Replacement",
      roleUrdu = "موبائل اسکرین و پارٹس مرمت",
      idealFor = "Ideal for: Tech-inclined worker/youth",
      startingInvestment = "Rs. 6,000",
      investmentNote = "Precision toolkit & glue",
      monthlyProfit = "Rs. 14k – 25k",
      profitNote = "Factory coworker quick fixes",
      requirements = "ضروری سامان: اسکرو ڈرائیور کٹ اور بنیادی پارٹس سپلائر",
      matchPercentage = 78
    ),
    SkillOpportunity(
      id = "crafts",
      nameEnglish = "Handicrafts",
      nameUrdu = "دستکاری و سجاوٹ",
      iconEmoji = "🎨",
      roleTitle = "Embroidery & Crochet Boutique",
      roleUrdu = "ہاتھ کی کڑھائی و کروشیا اشیاء",
      idealFor = "Ideal for: Home craft artisans",
      startingInvestment = "Rs. 3,500",
      investmentNote = "Wool, threads and frames",
      monthlyProfit = "Rs. 9k – 16k",
      profitNote = "Boutique orders and local bazar",
      requirements = "ضروری سامان: دھاگے، کڑھائی فریم اور ڈیزائن کیٹلاگ",
      matchPercentage = 75
    ),
    SkillOpportunity(
      id = "trading",
      nameEnglish = "Small Trading",
      nameUrdu = "چھوٹا کیبن / کریانہ",
      iconEmoji = "🏪",
      roleTitle = "Neighborhood Mini Kiryana Box",
      roleUrdu = "گھر کی کھڑکی سے بنیادی کریانہ",
      idealFor = "Ideal for: Elders / Family at home",
      startingInvestment = "Rs. 8,000",
      investmentNote = "Dry candies, spices, pulses",
      monthlyProfit = "Rs. 15k – 25k",
      profitNote = "Quick neighborhood daily needs",
      requirements = "ضروری سامان: الماری، ترازو اور تھوک مارکیٹ خریداری",
      matchPercentage = 70
    )
  )

  private val _selectedSkill = MutableStateFlow(availableSkills.first())
  val selectedSkill: StateFlow<SkillOpportunity> = _selectedSkill.asStateFlow()

  private val _roadmapSteps = MutableStateFlow(
    listOf(
      RoadmapStep(
        week = 1,
        weekLabel = "WEEK 1 • مکمل شد",
        title = "Identify services & set competitive prices",
        description = "Suit stitching Rs. 800, Alteration Rs. 100 per piece. Simple rate sheet confirmed.",
        isCompleted = true
      ),
      RoadmapStep(
        week = 2,
        weekLabel = "WEEK 2 • جاری ہے",
        title = "Prepare 3–5 sample design suits & display at home",
        description = "Stitch sample neck designs and pipe finishes so visitors see quality in person.",
        isCompleted = false
      ),
      RoadmapStep(
        week = 3,
        weekLabel = "WEEK 3 • اگلا قدم",
        title = "Share flyer with neighbors & factory network",
        description = "Hand 20 customized flyers to factory co-workers' families and community elders.",
        isCompleted = false
      ),
      RoadmapStep(
        week = 4,
        weekLabel = "WEEK 4 • مالی نظم و ضبط",
        title = "Record orders & separate business earnings",
        description = "Keep tailoring cash separate from house groceries in the provided Khushhaal purse.",
        isCompleted = false
      )
    )
  )
  val roadmapSteps: StateFlow<List<RoadmapStep>> = _roadmapSteps.asStateFlow()

  private val _isChallengeJoined = MutableStateFlow(false)
  val isChallengeJoined: StateFlow<Boolean> = _isChallengeJoined.asStateFlow()

  private val _emergencyDeposited = MutableStateFlow(false)
  val emergencyDeposited: StateFlow<Boolean> = _emergencyDeposited.asStateFlow()

  private val _wageAdvanceRequested = MutableStateFlow(false)
  val wageAdvanceRequested: StateFlow<Boolean> = _wageAdvanceRequested.asStateFlow()

  private val _isFraudModalOpen = MutableStateFlow(false)
  val isFraudModalOpen: StateFlow<Boolean> = _isFraudModalOpen.asStateFlow()

  private val _isPlayingAudio = MutableStateFlow(false)
  val isPlayingAudio: StateFlow<Boolean> = _isPlayingAudio.asStateFlow()

  private val _currentAudioCaption = MutableStateFlow("")
  val currentAudioCaption: StateFlow<String> = _currentAudioCaption.asStateFlow()

  private val _toastMessage = MutableStateFlow<String?>(null)
  val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

  private var tts: TextToSpeech? = null
  private var isTtsReady = false
  private var audioAutoStopJob: Job? = null
  private var toastAutoDismissJob: Job? = null

  init {
    try {
      tts = TextToSpeech(application, this)
    } catch (_: Exception) {
      isTtsReady = false
    }
  }

  override fun onInit(status: Int) {
    if (status == TextToSpeech.SUCCESS) {
      isTtsReady = true
      try {
        val result = tts?.setLanguage(Locale("ur", "PK"))
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
          tts?.setLanguage(Locale.ENGLISH)
        }
      } catch (_: Exception) {
        tts?.setLanguage(Locale.ENGLISH)
      }
    }
  }

  // Navigation Methods
  fun selectTab(tab: AppTab) {
    _currentDestination.value = AppDestination.TabView
    _currentTab.value = tab
  }

  fun navigateTo(destination: AppDestination) {
    _currentDestination.value = destination
  }

  fun navigateBack() {
    _currentDestination.value = AppDestination.TabView
  }

  // Language & Localization Methods
  fun setLanguage(language: AppLanguage) {
    _currentLanguage.value = language
    val msg = when (language) {
      AppLanguage.URDU -> "زبان: مکمل اردو منتخب ہو گئی"
      AppLanguage.ENGLISH -> "Language set to English"
      AppLanguage.BILINGUAL -> "Language: اردو / English Dual Mode"
    }
    showToast(msg)
  }

  fun toggleLanguage() {
    val next = when (_currentLanguage.value) {
      AppLanguage.BILINGUAL -> AppLanguage.URDU
      AppLanguage.URDU -> AppLanguage.ENGLISH
      AppLanguage.ENGLISH -> AppLanguage.BILINGUAL
    }
    setLanguage(next)
  }

  fun openLanguageSheet() {
    _isLanguageSheetOpen.value = true
  }

  fun closeLanguageSheet() {
    _isLanguageSheetOpen.value = false
  }

  fun openQuickExpenseSheet() {
    _isQuickExpenseSheetOpen.value = true
  }

  fun closeQuickExpenseSheet() {
    _isQuickExpenseSheetOpen.value = false
  }

  // Notification Methods
  fun markNotificationAsRead(id: String) {
    _notifications.update { list ->
      list.map { if (it.id == id) it.copy(isRead = true) else it }
    }
  }

  fun markAllNotificationsAsRead() {
    _notifications.update { list ->
      list.map { it.copy(isRead = true) }
    }
    showToast("تمام الرٹس پڑھے ہوئے نشان زد ہو گئے")
  }

  fun clearAllNotifications() {
    _notifications.value = emptyList()
    showToast("تمام الرٹس کلیئر کر دیے گئے")
  }

  fun showToast(message: String) {
    _toastMessage.value = message
    toastAutoDismissJob?.cancel()
    toastAutoDismissJob = viewModelScope.launch {
      delay(3500)
      _toastMessage.value = null
    }
  }

  fun dismissToast() {
    _toastMessage.value = null
    toastAutoDismissJob?.cancel()
  }

  fun playVoice(caption: String, spokenText: String = caption) {
    _currentAudioCaption.value = caption
    _isPlayingAudio.value = true

    if (isTtsReady && tts != null) {
      try {
        tts?.stop()
        tts?.speak(spokenText, TextToSpeech.QUEUE_FLUSH, null, "KhushhaalAudio")
      } catch (_: Exception) {}
    }

    audioAutoStopJob?.cancel()
    audioAutoStopJob = viewModelScope.launch {
      delay(7500)
      stopVoice()
    }
  }

  fun stopVoice() {
    _isPlayingAudio.value = false
    _currentAudioCaption.value = ""
    try {
      tts?.stop()
    } catch (_: Exception) {}
    audioAutoStopJob?.cancel()
  }

  fun toggleUniversalAudio() {
    if (_isPlayingAudio.value) {
      stopVoice()
    } else {
      playVoice(
        caption = "Assalam-o-Alaikum! Khushhaal app mein aap apni tankhwah, bachat, committee aur nayi aamdani ke mansoobay asaani se bana saktay hain.",
        spokenText = "Assalam-o-Alaikum! Khushhaal app mein aap apni tankhwah, bachat, committee aur nayi aamdani ke mansoobay asaani se bana saktay hain."
      )
    }
  }

  fun depositEmergencyFund() {
    if (_emergencyDeposited.value) {
      showToast("Rs. 3,000 pehlay se emergency fund mein jama hai!")
      return
    }
    _emergencyDeposited.value = true
    _emergencyLockerBalance.update { it + 3_000 }
    _cashFlow.update { current ->
      current.copy(
        savings = current.savings + 3_000,
        available = (current.available - 3_000).coerceAtLeast(0)
      )
    }
    _prosperityScore.update { current ->
      current.copy(
        score = (current.score + 3).coerceAtMost(100),
        savingsPct = 0.65f,
        safetyShieldPct = 0.65f,
        daysRunway = 15
      )
    }
    addTransaction(
      title = "Emergency Fund Deposit",
      urduTitle = "ہنگامی فنڈ جمع",
      amount = 3_000,
      isExpense = true,
      category = "Emergency",
      envelopeId = "emergency",
      paymentMethod = "JazzCash Vault"
    )
    showToast("Mubarak! Rs. 3,000 Emergency Fund mein jama ho gaye.")
    playVoice(
      caption = "Mubarak! Aap ke 3,000 rupay emergency fund mein mahfooz ho chukay hain. Aap ka safety buffer ab 15 din ka ho gaya hai.",
      spokenText = "Mubarak! Aap ke teen hazaar rupay emergency fund mein mahfooz ho chukay hain."
    )
  }

  fun addTransaction(
    title: String,
    urduTitle: String,
    amount: Long,
    isExpense: Boolean,
    category: String,
    envelopeId: String,
    paymentMethod: String = "Cash",
  ) {
    val dateStr = SimpleDateFormat("dd MMM yyyy", Locale.US).format(Date())
    val item = TransactionItem(
      id = "tx-${UUID.randomUUID().toString().take(6)}",
      title = title,
      urduTitle = urduTitle,
      amount = amount,
      isExpense = isExpense,
      category = category,
      envelopeId = envelopeId,
      date = dateStr,
      paymentMethod = paymentMethod
    )
    _transactions.update { listOf(item) + it }
  }

  fun logQuickExpense(name: String, amount: Long) {
    _cashFlow.update { current ->
      current.copy(
        expenses = current.expenses + amount,
        available = (current.available - amount).coerceAtLeast(0)
      )
    }
    addTransaction(
      title = name,
      urduTitle = name,
      amount = amount,
      isExpense = true,
      category = "Daily Outflow",
      envelopeId = "needs",
      paymentMethod = "Cash"
    )
    showToast("Darj ho gaya: $name - Rs. $amount")
  }

  fun simulateVoiceLog() {
    playVoice(
      caption = "ریکارڈنگ شروع... بولیں: کتنا خرچہ ہوا؟",
      spokenText = "Recording started. Bolain: Kitna kharcha hua?"
    )
    viewModelScope.launch {
      delay(2800)
      logQuickExpense("Doodh aur sabzi", 420)
      showToast("Recorded: 'Doodh aur sabzi - Rs. 420' added to daily expense.")
    }
  }

  fun requestWageAdvance() {
    _wageAdvanceRequested.value = true
    showToast("Factory Wage Advance (Rs. 6,000) Request submitted to Naveena Mills HR!")
    playVoice(
      caption = "Naveena Mills HR ko 6,000 rupay baghair sood advance ki darkhwast bhej di gayi hai.",
      spokenText = "Naveena Mills HR ko chhey hazaar rupay advance ki darkhwast bhej di gayi hai."
    )
  }

  fun toggleChallenge() {
    val newState = !_isChallengeJoined.value
    _isChallengeJoined.value = newState
    if (newState) {
      showToast("Challenge Accepted! Target: Save Rs. 1,000 extra this month.")
      playVoice("Zabardast! Mill challenge join ho gaya. Is maheenay 1,000 rupay mazeed bachat karein.")
    } else {
      showToast("Challenge cancelled.")
    }
  }

  fun callCoach() {
    navigateTo(AppDestination.CoachChat)
    playVoice(
      caption = "Coach Fatima se live consultation chat open ho gaya hai. Sawal poochiye.",
      spokenText = "Coach Fatima se consultation chat open ho gaya hai."
    )
  }

  fun sendCoachMessage(userText: String) {
    val time = SimpleDateFormat("h:mm a", Locale.US).format(Date())
    val userMsg = CoachMessage(
      id = "msg-${UUID.randomUUID().toString().take(6)}",
      textUrdu = userText,
      textRoman = userText,
      isFromCoach = false,
      timestamp = time,
      spokenText = userText
    )
    _coachMessages.update { it + userMsg }

    viewModelScope.launch {
      delay(1200)
      val reply = generateCoachReply(userText)
      _coachMessages.update { it + reply }
      playVoice(reply.textRoman, reply.spokenText)
    }
  }

  private fun generateCoachReply(prompt: String): CoachMessage {
    val time = SimpleDateFormat("h:mm a", Locale.US).format(Date())
    val p = prompt.lowercase()
    return when {
      "kameti" in p || "committee" in p -> {
        CoachMessage(
          id = "msg-reply-${UUID.randomUUID().toString().take(4)}",
          textUrdu = "کمیٹی ایک بہترین روایتی بچت ہے۔ بس دھیان رکھیں کہ منتظم قابلِ اعتماد ہو اور کمیٹی کا مہینہ اپنے کسی اہم خرچ کے ساتھ ملائیں۔",
          textRoman = "Kameti behtareen bachat hai. Bas munazim qabil-e-aitmad hona chahiye aur mahina apnay zaroori kharch se match karein.",
          isFromCoach = true,
          timestamp = time,
          spokenText = "Kameti behtareen bachat hai. Bas munazim qabil-e-aitmad hona chahiye."
        )
      }
      "bachat" in p || "save" in p -> {
        CoachMessage(
          id = "msg-reply-${UUID.randomUUID().toString().take(4)}",
          textUrdu = "پہلے بچت کریں پھر خرچ! تنخواہ ملتے ہی 10% رقم فوری طور پر JazzCash کے لاکر میں چھپا دیں۔",
          textRoman = "Pehlay bachat karein phir kharch! Tankhwah miltay hi das feesad raqam alag kar lein.",
          isFromCoach = true,
          timestamp = time,
          spokenText = "Pehlay bachat karein phir kharch! Tankhwah miltay hi das feesad alag karein."
        )
      }
      else -> {
        CoachMessage(
          id = "msg-reply-${UUID.randomUUID().toString().take(4)}",
          textUrdu = "احمد بھائی، مالی تحفظ کے لیے کم از کم 30 دن کا ایمرجنسی خرچہ اور گھر کی اضافی سلائی یا ہنر بہت فائدہ مند رہے گا۔",
          textRoman = "Ahmed Bhai, mali tahaffuz ke liye tees din ka emergency kharcha aur ghar ki aamdani bohat faidamand rahay gi.",
          isFromCoach = true,
          timestamp = time,
          spokenText = "Ahmed Bhai, tees din ka emergency buffer aur ghar ki nayi aamdani bohat faidamand rahay gi."
        )
      }
    }
  }

  fun markKametiPaid(kametiId: String) {
    _kametis.update { list ->
      list.map { if (it.id == kametiId) it.copy(isPaidThisMonth = true) else it }
    }
    showToast("MashaAllah! Kameti installment marked as paid.")
  }

  fun addNewKameti(name: String, amount: Long, totalMembers: Int, myTurn: Int, organizer: String) {
    val newK = KametiItem(
      id = "k-${UUID.randomUUID().toString().take(4)}",
      name = name,
      urduName = name,
      monthlyAmount = amount,
      totalMembers = totalMembers,
      myTurnMonth = myTurn,
      currentMonth = 1,
      payoutAmount = amount * totalMembers,
      organizer = organizer,
      isPaidThisMonth = true
    )
    _kametis.update { it + newK }
    showToast("New Kameti ($name) added successfully!")
  }

  fun contributeToGoal(goalId: String, amount: Long) {
    _goals.update { list ->
      list.map { if (it.id == goalId) it.copy(currentAmount = (it.currentAmount + amount).coerceAtMost(it.targetAmount)) else it }
    }
    _emergencyLockerBalance.update { (it - amount).coerceAtLeast(0) }
    showToast("Rs. $amount contributed to goal!")
  }

  fun addNewGoal(title: String, targetAmount: Long, targetDate: String, emoji: String) {
    val newG = FamilyGoalItem(
      id = "g-${UUID.randomUUID().toString().take(4)}",
      title = title,
      urduTitle = title,
      targetAmount = targetAmount,
      currentAmount = 0,
      targetDate = targetDate,
      emoji = emoji
    )
    _goals.update { it + newG }
    showToast("Goal ($title) created!")
  }

  fun depositLocker(amount: Long) {
    _emergencyLockerBalance.update { it + amount }
    _prosperityScore.update { it.copy(daysRunway = (it.daysRunway + (amount / 400).toInt()).coerceAtMost(60)) }
    showToast("Rs. $amount added to Emergency Vault!")
  }

  fun withdrawLocker(amount: Long) {
    if (_emergencyLockerBalance.value < amount) {
      showToast("Insufficient balance in vault!")
      return
    }
    _emergencyLockerBalance.update { it - amount }
    showToast("Rs. $amount withdrawn for emergency.")
  }

  fun answerScam(scamId: String, choseSafe: Boolean) {
    _scamSimulations.update { list ->
      list.map { if (it.id == scamId) it.copy(isCompleted = true, isCorrect = choseSafe) else it }
    }
    if (choseSafe) {
      _prosperityScore.update { it.copy(score = (it.score + 2).coerceAtMost(100)) }
      showToast("Zabardast! Sahi faisla. Score +2 points!")
      val scenario = _scamSimulations.value.firstOrNull { it.id == scamId }
      scenario?.let { playVoice(it.audioExplanation) }
    } else {
      showToast("Khabardaar! Yeh fraud phanda tha.")
      val scenario = _scamSimulations.value.firstOrNull { it.id == scamId }
      scenario?.let { playVoice(it.audioExplanation) }
    }
  }

  fun toggleOrderDelivered(orderId: String) {
    _customerOrders.update { list ->
      list.map { if (it.id == orderId) it.copy(isDelivered = !it.isDelivered) else it }
    }
    showToast("Order delivery status updated!")
  }

  fun toggleOrderPaid(orderId: String) {
    _customerOrders.update { list ->
      list.map { if (it.id == orderId) it.copy(isFullyPaid = !it.isFullyPaid) else it }
    }
    showToast("Payment status updated!")
  }

  fun addNewCustomerOrder(
    name: String,
    phone: String,
    service: String,
    total: Long,
    advance: Long,
    dueDate: String,
  ) {
    val ord = CustomerOrder(
      id = "ord-${UUID.randomUUID().toString().take(4)}",
      customerName = name,
      phone = phone,
      serviceTitle = service,
      totalAmount = total,
      advancePaid = advance,
      dueDate = dueDate,
      isDelivered = false,
      isFullyPaid = advance >= total
    )
    _customerOrders.update { listOf(ord) + it }
    showToast("Customer order saved to Business Khata!")
  }

  fun openFraudModal() {
    _isFraudModalOpen.value = true
  }

  fun closeFraudModal() {
    _isFraudModalOpen.value = false
  }

  fun answerFraudQuiz(isCorrect: Boolean) {
    if (isCorrect) {
      _prosperityScore.update { current ->
        current.copy(score = (current.score + 2).coerceAtMost(100))
      }
      _pillars.update { list ->
        list.map { pillar ->
          if (pillar.id == 5) pillar.copy(currentScore = 10, statusText = "Passed (10/10) ✓") else pillar
        }
      }
      closeFraudModal()
      showToast("Shabash! Sahi jawab. Digital Safety score +2 barh gaya.")
      playVoice(
        caption = "Shabash! Kabhi kisi se apna PIN ya OTP share mat karein chahay koi khud ko bank officer kahe.",
        spokenText = "Shabash! Kabhi kisi se apna PIN ya OTP share mat karein."
      )
    } else {
      showToast("Khabardaar! Kabhi bhi call par kisi ko OTP mat dein.")
      playVoice(
        caption = "Khabardaar! Bank ya factory kabhi bhi call par OTP nahi maangti. Phone foran kaat dein.",
        spokenText = "Khabardaar! Bank ya factory kabhi bhi call par OTP nahi maangti."
      )
    }
  }

  fun updateRationEnvelopeAmount(newAmount: Long) {
    _envelopes.update { list ->
      list.map { envelope ->
        if (envelope.id == "needs") {
          val updatedItems = envelope.items.map { subItem ->
            if (subItem.label.contains("راشن") || subItem.label.contains("آٹا")) {
              subItem.copy(amount = newAmount)
            } else subItem
          }
          val totalItemsSum = updatedItems.sumOf { it.amount }
          envelope.copy(amount = totalItemsSum, items = updatedItems)
        } else envelope
      }
    }
  }

  fun selectSkill(skill: SkillOpportunity) {
    _selectedSkill.value = skill
    showToast("Selected: ${skill.roleTitle} blueprint loaded!")
  }

  fun toggleRoadmapStep(week: Int) {
    _roadmapSteps.update { steps ->
      steps.map { step ->
        if (step.week == week) {
          val updated = !step.isCompleted
          if (updated) showToast("Milestone Week $week updated! Keep going.")
          step.copy(isCompleted = updated)
        } else step
      }
    }
  }

  fun orderMarketingKit() {
    showToast("Shandar! 30-Day Microenterprise Plan activate ho chuka hai.")
    playVoice(
      caption = "Shandar! Aap ka 30 din ka maliyati aamdani plan shuru ho chuka hai. Hum aap ko free marketing kit bhejh rahe hain.",
      spokenText = "Shandar! Aap ka tees din ka maliyati aamdani plan shuru ho chuka hai."
    )
  }

  fun togglePayBill(billId: String) {
    val dateStr = SimpleDateFormat("dd MMMM yyyy", Locale("ur", "PK")).format(Date())
    _utilityBills.update { bills ->
      bills.map { b ->
        if (b.id == billId) {
          val willBePaid = !b.isPaid
          if (willBePaid) {
            showToast("${b.companyUrdu} ادا ہو گیا! لیجر اپ ڈیٹ کر دیا گیا۔")
            addTransaction(
              title = "Paid: ${b.companyName}",
              urduTitle = "${b.companyUrdu} کی ادائیگی",
              amount = b.amount,
              isExpense = true,
              category = "بلز و یوٹیلیٹی",
              envelopeId = "needs",
              paymentMethod = "JazzCash"
            )
          }
          b.copy(
            isPaid = willBePaid,
            paidDate = if (willBePaid) dateStr else null
          )
        } else b
      }
    }
  }

  fun addNewBill(
    company: String,
    consumerNo: String,
    type: String,
    amount: Long,
    units: Int,
    dueDate: String
  ) {
    val newB = UtilityBill(
      id = "bill-${System.currentTimeMillis()}",
      companyName = company,
      companyUrdu = "$company بل",
      consumerNumber = consumerNo,
      billType = type,
      month = "مارچ 2025",
      dueDate = dueDate,
      amount = amount,
      unitsConsumed = units,
      isPaid = false,
      alertTip = "بروقت ادائیگی پر جرمانہ اور لیٹ سرچارج سے بچیں۔"
    )
    _utilityBills.update { listOf(newB) + it }
    showToast("نیا بل $company کامیابی سے شامل ہو گیا!")
  }

  fun repayDebt(debtId: String, paymentAmount: Long) {
    var fullyPaid = false
    var debtTitle = ""
    _debts.update { list ->
      list.map { d ->
        if (d.id == debtId) {
          val remaining = (d.remainingAmount - paymentAmount).coerceAtLeast(0)
          if (remaining == 0L) fullyPaid = true
          debtTitle = d.creditorUrdu
          d.copy(remainingAmount = remaining)
        } else d
      }
    }

    addTransaction(
      title = "Debt Payment: $debtTitle",
      urduTitle = "قرض ادائیگی: $debtTitle",
      amount = paymentAmount,
      isExpense = true,
      category = "قرض نجات",
      envelopeId = "savings",
      paymentMethod = "نقد کیش"
    )

    if (fullyPaid) {
      showToast("مبارک ہو! $debtTitle کا قرض مکمل ادا ہو گیا! 🎉")
      playVoice(
        caption = "Mubarak ho! Aap ne $debtTitle ka qarz mukammal ada kar diya hai. Snowball plan se aap ki aazadi mazeed kareeb aa gayi.",
        spokenText = "Mubarak ho! Aap ne qarz mukammal ada kar diya hai."
      )
    } else {
      showToast("روپے $paymentAmount ادا کر دیے گئے۔ باقی قرض اپ ڈیٹ ہو گیا۔")
    }
  }

  fun addNewDebt(
    name: String,
    relation: String,
    total: Long,
    monthly: Long,
    urgency: String
  ) {
    val newD = DebtItem(
      id = "debt-${System.currentTimeMillis()}",
      creditorName = name,
      creditorUrdu = name,
      relationOrType = relation,
      totalAmount = total,
      remainingAmount = total,
      monthlyCommitment = monthly,
      urgencyLevel = urgency,
      isShariahFriendly = true,
      repaymentStrategyTip = "سنو بال پلان کے تحت اس قرض کو مرحلہ وار ختم کریں۔"
    )
    _debts.update { it + newD }
    showToast("قرض کھاتہ شامل کر لیا گیا۔")
  }

  override fun onCleared() {
    super.onCleared()
    try {
      tts?.stop()
      tts?.shutdown()
    } catch (_: Exception) {}
  }
}
