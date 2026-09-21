package com.example.viewmodel

import android.app.Application
import android.speech.tts.TextToSpeech
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.api.ContributeRequest
import com.example.data.api.CreateBillRequest
import com.example.data.api.CreateDebtRequest
import com.example.data.api.CreateGoalRequest
import com.example.data.api.CreateKametiRequest
import com.example.data.api.CreateOrderRequest
import com.example.data.api.CreateTransactionRequest
import com.example.data.api.LockerTransactionRequest
import com.example.data.api.RepayRequest
import com.example.data.api.RetrofitClient
import com.example.data.api.UpdateOrderRequest
import com.example.data.repository.KhushhaalRepository
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
import com.example.data.repository.Result

class KhushhaalViewModel(application: Application) : AndroidViewModel(application), TextToSpeech.OnInitListener {

  private val repository = KhushhaalRepository.getInstance(application)

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

  private val _rationEstimates = MutableStateFlow<List<RationItemEstimate>>(emptyList())
  val rationEstimates: StateFlow<List<RationItemEstimate>> = _rationEstimates.asStateFlow()

  private val _utilityBills = MutableStateFlow<List<UtilityBill>>(emptyList())
  val utilityBills: StateFlow<List<UtilityBill>> = _utilityBills.asStateFlow()

  private val _debts = MutableStateFlow<List<DebtItem>>(emptyList())
  val debts: StateFlow<List<DebtItem>> = _debts.asStateFlow()

  private val _notifications = MutableStateFlow<List<AppNotification>>(emptyList())
  val notifications: StateFlow<List<AppNotification>> = _notifications.asStateFlow()

  private val _pillars = MutableStateFlow<List<ProsperityPillar>>(emptyList())
  val pillars: StateFlow<List<ProsperityPillar>> = _pillars.asStateFlow()

  private val _envelopes = MutableStateFlow<List<Envelope>>(emptyList())
  val envelopes: StateFlow<List<Envelope>> = _envelopes.asStateFlow()

  // Transactions History & Khata State
  private val _transactions = MutableStateFlow<List<TransactionItem>>(emptyList())
  val transactions: StateFlow<List<TransactionItem>> = _transactions.asStateFlow()

  // Kametis State
  private val _kametis = MutableStateFlow<List<KametiItem>>(emptyList())
  val kametis: StateFlow<List<KametiItem>> = _kametis.asStateFlow()

  // Goals State
  private val _goals = MutableStateFlow<List<FamilyGoalItem>>(emptyList())
  val goals: StateFlow<List<FamilyGoalItem>> = _goals.asStateFlow()

  // Emergency Locker State
  private val _emergencyLockerBalance = MutableStateFlow(0L)
  val emergencyLockerBalance: StateFlow<Long> = _emergencyLockerBalance.asStateFlow()

  // App Config — company-controlled content loaded from backend
  private val _appConfigFactoryName = MutableStateFlow("Naveena Mills Ltd.")
  val appConfigFactoryName: StateFlow<String> = _appConfigFactoryName.asStateFlow()

  private val _appConfigHelpline = MutableStateFlow("0800-64557")
  val appConfigHelpline: StateFlow<String> = _appConfigHelpline.asStateFlow()

  private val _appConfigHelplineLabel = MutableStateFlow("Naveena Welfare")
  val appConfigHelplineLabel: StateFlow<String> = _appConfigHelplineLabel.asStateFlow()

  // Fraud Academy Scenarios — loaded from backend
  private val _scamSimulations = MutableStateFlow<List<ScamSimulation>>(emptyList())
  val scamSimulations: StateFlow<List<ScamSimulation>> = _scamSimulations.asStateFlow()

  // Coach Fatima Chat Messages
  private val _coachMessages = MutableStateFlow<List<CoachMessage>>(emptyList())
  val coachMessages: StateFlow<List<CoachMessage>> = _coachMessages.asStateFlow()

  // Customer Orders (Microenterprise Khata)
  private val _customerOrders = MutableStateFlow<List<CustomerOrder>>(emptyList())
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
    // Load all data from backend/cache on startup
    loadAllData()
  }

  // ─── Data Loading ────────────────────────────────────────────────────────────

  // ─── Profile Update ──────────────────────────────────────────────────────────

  private fun loadAppConfig() {
    viewModelScope.launch {
      try {
        val apiService = RetrofitClient.getInstance(getApplication()).khushhaalApiService
        val response = apiService.getAppConfig()
        if (response.isSuccessful && response.body() != null) {
          val cfg = response.body()!!
          _appConfigFactoryName.value = cfg.factoryName
          _appConfigHelpline.value = cfg.welfareHelpline
          _appConfigHelplineLabel.value = cfg.welfareHelplineLabel
          if (cfg.rationItems.isNotEmpty()) {
            _rationEstimates.value = cfg.rationItems.map {
              RationItemEstimate(
                id = it.id,
                nameUrdu = it.nameUrdu,
                nameEnglish = it.nameEnglish,
                category = it.category,
                defaultQty = it.defaultQty,
                unitPriceEstimate = it.unitPriceEstimate,
                marketPriceRange = it.marketPriceRange,
                isEssential = it.isEssential,
                savingsTip = it.savingsTip
              )
            }
          }
          if (cfg.scamSimulations.isNotEmpty()) {
            _scamSimulations.value = cfg.scamSimulations.map {
              ScamSimulation(
                id = it.id,
                titleUrdu = it.titleUrdu,
                titleEnglish = it.titleEnglish,
                scamText = it.scamText,
                optionSafe = it.optionSafe,
                optionTrap = it.optionTrap,
                audioExplanation = it.audioExplanation
              )
            }
          }
        }
      } catch (e: Exception) {
        // Config load failure is non-critical — app works with empty lists
      }
    }
  }

  fun updateProfile(request: com.example.data.api.UpdateProfileRequest) {
    viewModelScope.launch {
      val result = repository.updateProfile(request)
      if (result is Result.Success) {
        _userProfile.value = result.data
        showToast("پروفائل کامیابی سے اپ ڈیٹ ہو گیا / Profile updated successfully")
      } else if (result is Result.Error) {
        showToast("پروفائل اپ ڈیٹ نہیں ہوا: ${result.message}")
      }
    }
  }

  fun loadAllData() {
    loadAppConfig()
    loadUserProfile()
    loadCashFlow()
    loadTransactions()
    loadEnvelopes()
    loadKametis()
    loadGoals()
    loadDebts()
    loadBills()
    loadLockerBalance()
    loadProsperity()
    loadNotifications()
    loadCustomerOrders()
    loadCoachMessages()
  }

  private fun loadUserProfile() {
    viewModelScope.launch {
      repository.getUserProfile().collect { result ->
        if (result is Result.Success) {
          _userProfile.value = result.data
        }
      }
    }
  }

  private fun loadCashFlow() {
    viewModelScope.launch {
      repository.getCashFlow().collect { result ->
        if (result is Result.Success) {
          _cashFlow.value = result.data
        }
      }
    }
  }

  private fun loadTransactions() {
    viewModelScope.launch {
      repository.getTransactions().collect { result ->
        if (result is Result.Success) {
          _transactions.value = result.data
        }
      }
    }
  }

  private fun loadEnvelopes() {
    viewModelScope.launch {
      repository.getEnvelopes().collect { result ->
        if (result is Result.Success) {
          _envelopes.value = result.data
        }
      }
    }
  }

  private fun loadKametis() {
    viewModelScope.launch {
      repository.getKametis().collect { result ->
        if (result is Result.Success) {
          _kametis.value = result.data
        }
      }
    }
  }

  private fun loadGoals() {
    viewModelScope.launch {
      repository.getGoals().collect { result ->
        if (result is Result.Success) {
          _goals.value = result.data
        }
      }
    }
  }

  private fun loadDebts() {
    viewModelScope.launch {
      repository.getDebts().collect { result ->
        if (result is Result.Success) {
          _debts.value = result.data
        }
      }
    }
  }

  private fun loadBills() {
    viewModelScope.launch {
      repository.getBills().collect { result ->
        if (result is Result.Success) {
          _utilityBills.value = result.data
        }
      }
    }
  }

  private fun loadLockerBalance() {
    viewModelScope.launch {
      repository.getEmergencyLockerBalance().collect { result ->
        if (result is Result.Success) {
          _emergencyLockerBalance.value = result.data
        }
      }
    }
  }

  private fun loadProsperity() {
    viewModelScope.launch {
      repository.getProsperityScore().collect { result ->
        if (result is Result.Success) {
          _prosperityScore.value = result.data
        }
      }
    }
  }

  private fun loadNotifications() {
    viewModelScope.launch {
      repository.getNotifications().collect { result ->
        if (result is Result.Success) {
          _notifications.value = result.data
        }
      }
    }
  }

  private fun loadCustomerOrders() {
    viewModelScope.launch {
      repository.getCustomerOrders().collect { result ->
        if (result is Result.Success) {
          _customerOrders.value = result.data
        }
      }
    }
  }

  private fun loadCoachMessages() {
    viewModelScope.launch {
      repository.getCoachMessages().collect { result ->
        if (result is Result.Success) {
          _coachMessages.value = result.data
        }
      }
    }
  }

  // Called when onboarding completes with income > 0
  fun setOnboardingIncome(amount: Long) {
    if (amount > 0L) {
      _cashFlow.update { it.copy(income = amount) }
    }
  }

  // Called after logout to reset all in-memory state
  fun resetAllState() {
    _userProfile.value = UserProfile()
    _cashFlow.value = CashFlowData()
    _prosperityScore.value = ProsperityScore()
    _transactions.value = emptyList()
    _envelopes.value = emptyList()
    _kametis.value = emptyList()
    _goals.value = emptyList()
    _debts.value = emptyList()
    _utilityBills.value = emptyList()
    _emergencyLockerBalance.value = 0L
    _notifications.value = emptyList()
    _customerOrders.value = emptyList()
    _coachMessages.value = emptyList()
    _emergencyDeposited.value = false
    _wageAdvanceRequested.value = false
    _isChallengeJoined.value = false
    _currentDestination.value = AppDestination.TabView
    _currentTab.value = AppTab.HOME
  }

  // ─── TTS ─────────────────────────────────────────────────────────────────────

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

  // ─── Navigation ──────────────────────────────────────────────────────────────

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

  // ─── Language ────────────────────────────────────────────────────────────────

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

  fun openLanguageSheet() { _isLanguageSheetOpen.value = true }
  fun closeLanguageSheet() { _isLanguageSheetOpen.value = false }
  fun openQuickExpenseSheet() { _isQuickExpenseSheetOpen.value = true }
  fun closeQuickExpenseSheet() { _isQuickExpenseSheetOpen.value = false }

  // ─── Notifications ────────────────────────────────────────────────────────────

  fun markNotificationAsRead(id: String) {
    viewModelScope.launch {
      _notifications.update { list -> list.map { if (it.id == id) it.copy(isRead = true) else it } }
      repository.markNotificationRead(id)
    }
  }

  fun markAllNotificationsAsRead() {
    _notifications.update { list -> list.map { it.copy(isRead = true) } }
    showToast("تمام الرٹس پڑھے ہوئے نشان زد ہو گئے")
  }

  fun clearAllNotifications() {
    viewModelScope.launch {
      _notifications.value = emptyList()
      repository.clearAllNotifications()
      showToast("تمام الرٹس کلیئر کر دیے گئے")
    }
  }

  // ─── Toast & Audio ────────────────────────────────────────────────────────────

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
    try { tts?.stop() } catch (_: Exception) {}
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

  // ─── Emergency Fund ───────────────────────────────────────────────────────────

  fun depositEmergencyFund() {
    if (_emergencyDeposited.value) {
      showToast("Rs. 3,000 pehlay se emergency fund mein jama hai!")
      return
    }
    viewModelScope.launch {
      val result = repository.depositToLocker(3_000)
      if (result is Result.Success) {
        _emergencyDeposited.value = true
        _emergencyLockerBalance.value = result.data
        showToast("Mubarak! Rs. 3,000 Emergency Fund mein jama ho gaye.")
        playVoice(
          caption = "Mubarak! Aap ke 3,000 rupay emergency fund mein mahfooz ho chukay hain.",
          spokenText = "Mubarak! Aap ke teen hazaar rupay emergency fund mein mahfooz ho chukay hain."
        )
        // Reload cashflow after deposit
        loadCashFlow()
        loadProsperity()
      } else {
        showToast("Emergency fund mein jama karne mein masla aaya, dobara koshish karein.")
      }
    }
  }

  // ─── Transactions ─────────────────────────────────────────────────────────────

  fun addTransaction(
    title: String,
    urduTitle: String,
    amount: Long,
    isExpense: Boolean,
    category: String,
    envelopeId: String,
    paymentMethod: String = "Cash",
  ) {
    viewModelScope.launch {
      val dateStr = SimpleDateFormat("dd MMM yyyy", Locale.US).format(Date())
      val request = CreateTransactionRequest(
        title = title,
        urduTitle = urduTitle,
        amount = amount,
        isExpense = isExpense,
        category = category,
        envelopeId = envelopeId,
        paymentMethod = paymentMethod,
        date = dateStr
      )
      val result = repository.addTransaction(request)
      if (result is Result.Success) {
        _transactions.update { listOf(result.data) + it }
        // Reload cashflow to reflect updated totals
        loadCashFlow()
      } else {
        // Optimistic local update as fallback
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
    }
  }

  fun logQuickExpense(name: String, amount: Long) {
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

  // ─── Wage Advance ─────────────────────────────────────────────────────────────

  fun requestWageAdvance() {
    _wageAdvanceRequested.value = true
    showToast("Factory Wage Advance Request submitted to HR!")
    playVoice(
      caption = "Factory HR ko advance ki darkhwast bhej di gayi hai.",
      spokenText = "Factory HR ko advance ki darkhwast bhej di gayi hai."
    )
  }

  // ─── Challenge ────────────────────────────────────────────────────────────────

  fun toggleChallenge() {
    val newState = !_isChallengeJoined.value
    _isChallengeJoined.value = newState
    if (newState) {
      showToast("Challenge Accepted! Target: Save Rs. 1,000 extra this month.")
      playVoice("Zabardast! Is maheenay 1,000 rupay mazeed bachat karein.")
    } else {
      showToast("Challenge cancelled.")
    }
  }

  // ─── Coach Chat ──────────────────────────────────────────────────────────────

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
      // Try to send to backend first
      val serverResult = repository.sendCoachMessage(textUrdu = userText, textRoman = userText)
      delay(1200)
      // Generate local reply regardless (AI coach)
      val reply = generateCoachReply(userText)
      _coachMessages.update { it + reply }
      playVoice(reply.textRoman, reply.spokenText)
    }
  }

  private fun generateCoachReply(prompt: String): CoachMessage {
    val time = SimpleDateFormat("h:mm a", Locale.US).format(Date())
    val userName = _userProfile.value.name.ifBlank { "آپ" }
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
          textUrdu = "مالی تحفظ کے لیے کم از کم 30 دن کا ایمرجنسی خرچہ اور گھر کی اضافی آمدنی بہت فائدہ مند رہے گی۔",
          textRoman = "Mali tahaffuz ke liye tees din ka emergency kharcha aur ghar ki aamdani bohat faidamand rahay gi.",
          isFromCoach = true,
          timestamp = time,
          spokenText = "Tees din ka emergency buffer aur ghar ki nayi aamdani bohat faidamand rahay gi."
        )
      }
    }
  }

  // ─── Kameti ──────────────────────────────────────────────────────────────────

  fun markKametiPaid(kametiId: String) {
    viewModelScope.launch {
      val result = repository.markKametiPaid(kametiId)
      if (result is Result.Success) {
        _kametis.update { list -> list.map { if (it.id == kametiId) result.data else it } }
      } else {
        _kametis.update { list -> list.map { if (it.id == kametiId) it.copy(isPaidThisMonth = true) else it } }
      }
      showToast("MashaAllah! Kameti installment marked as paid.")
    }
  }

  fun addNewKameti(name: String, amount: Long, totalMembers: Int, myTurn: Int, organizer: String) {
    viewModelScope.launch {
      val request = CreateKametiRequest(
        name = name,
        urduName = name,
        monthlyAmount = amount,
        totalMembers = totalMembers,
        myTurnMonth = myTurn,
        currentMonth = 1,
        organizer = organizer
      )
      val result = repository.addKameti(request)
      if (result is Result.Success) {
        _kametis.update { it + result.data }
      } else {
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
      }
      showToast("New Kameti ($name) added successfully!")
    }
  }

  // ─── Goals ───────────────────────────────────────────────────────────────────

  fun contributeToGoal(goalId: String, amount: Long) {
    viewModelScope.launch {
      val result = repository.contributeToGoal(goalId, amount)
      if (result is Result.Success) {
        _goals.update { list -> list.map { if (it.id == goalId) result.data else it } }
      } else {
        _goals.update { list ->
          list.map { if (it.id == goalId) it.copy(currentAmount = (it.currentAmount + amount).coerceAtMost(it.targetAmount)) else it }
        }
      }
      showToast("Rs. $amount contributed to goal!")
    }
  }

  fun addNewGoal(title: String, targetAmount: Long, targetDate: String, emoji: String) {
    viewModelScope.launch {
      val request = CreateGoalRequest(
        title = title,
        urduTitle = title,
        targetAmount = targetAmount,
        currentAmount = 0,
        targetDate = targetDate,
        emoji = emoji
      )
      val result = repository.addGoal(request)
      if (result is Result.Success) {
        _goals.update { it + result.data }
      } else {
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
      }
      showToast("Goal ($title) created!")
    }
  }

  // ─── Emergency Locker ─────────────────────────────────────────────────────────

  fun depositLocker(amount: Long) {
    viewModelScope.launch {
      val result = repository.depositToLocker(amount)
      if (result is Result.Success) {
        _emergencyLockerBalance.value = result.data
        showToast("Rs. $amount added to Emergency Vault!")
      } else {
        _emergencyLockerBalance.update { it + amount }
        showToast("Rs. $amount added to Emergency Vault!")
      }
    }
  }

  fun withdrawLocker(amount: Long) {
    viewModelScope.launch {
      if (_emergencyLockerBalance.value < amount) {
        showToast("Insufficient balance in vault!")
        return@launch
      }
      val result = repository.withdrawFromLocker(amount)
      if (result is Result.Success) {
        _emergencyLockerBalance.value = result.data
        showToast("Rs. $amount withdrawn for emergency.")
      } else {
        _emergencyLockerBalance.update { it - amount }
        showToast("Rs. $amount withdrawn for emergency.")
      }
    }
  }

  // ─── Fraud Academy ────────────────────────────────────────────────────────────

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

  // ─── Business Khata ───────────────────────────────────────────────────────────

  fun toggleOrderDelivered(orderId: String) {
    viewModelScope.launch {
      val order = _customerOrders.value.firstOrNull { it.id == orderId } ?: return@launch
      val newVal = !order.isDelivered
      val result = repository.updateOrder(orderId, UpdateOrderRequest(isDelivered = newVal))
      if (result is Result.Success) {
        _customerOrders.update { list -> list.map { if (it.id == orderId) result.data else it } }
      } else {
        _customerOrders.update { list -> list.map { if (it.id == orderId) it.copy(isDelivered = newVal) else it } }
      }
      showToast("Order delivery status updated!")
    }
  }

  fun toggleOrderPaid(orderId: String) {
    viewModelScope.launch {
      val order = _customerOrders.value.firstOrNull { it.id == orderId } ?: return@launch
      val newVal = !order.isFullyPaid
      val result = repository.updateOrder(orderId, UpdateOrderRequest(isFullyPaid = newVal))
      if (result is Result.Success) {
        _customerOrders.update { list -> list.map { if (it.id == orderId) result.data else it } }
      } else {
        _customerOrders.update { list -> list.map { if (it.id == orderId) it.copy(isFullyPaid = newVal) else it } }
      }
      showToast("Payment status updated!")
    }
  }

  fun addNewCustomerOrder(
    name: String,
    phone: String,
    service: String,
    total: Long,
    advance: Long,
    dueDate: String,
  ) {
    viewModelScope.launch {
      val request = CreateOrderRequest(
        customerName = name,
        phone = phone,
        serviceTitle = service,
        totalAmount = total,
        advancePaid = advance,
        dueDate = dueDate
      )
      val result = repository.addOrder(request)
      if (result is Result.Success) {
        _customerOrders.update { listOf(result.data) + it }
      } else {
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
      }
      showToast("Customer order saved to Business Khata!")
    }
  }

  // ─── Fraud Modal ──────────────────────────────────────────────────────────────

  fun openFraudModal() { _isFraudModalOpen.value = true }
  fun closeFraudModal() { _isFraudModalOpen.value = false }

  fun answerFraudQuiz(isCorrect: Boolean) {
    if (isCorrect) {
      _prosperityScore.update { current -> current.copy(score = (current.score + 2).coerceAtMost(100)) }
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

  // ─── Ration Calculator ───────────────────────────────────────────────────────

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

  // ─── Skills ──────────────────────────────────────────────────────────────────

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

  // ─── Utility Bills ────────────────────────────────────────────────────────────

  fun togglePayBill(billId: String) {
    viewModelScope.launch {
      val bill = _utilityBills.value.firstOrNull { it.id == billId } ?: return@launch
      if (!bill.isPaid) {
        val result = repository.markBillPaid(billId)
        if (result is Result.Success) {
          _utilityBills.update { bills -> bills.map { if (it.id == billId) result.data else it } }
          showToast("${bill.companyUrdu} ادا ہو گیا!")
          addTransaction(
            title = "Paid: ${bill.companyName}",
            urduTitle = "${bill.companyUrdu} کی ادائیگی",
            amount = bill.amount,
            isExpense = true,
            category = "بلز و یوٹیلیٹی",
            envelopeId = "needs",
            paymentMethod = "JazzCash"
          )
        } else {
          val dateStr = SimpleDateFormat("dd MMMM yyyy", Locale("ur", "PK")).format(Date())
          _utilityBills.update { bills ->
            bills.map { b -> if (b.id == billId) b.copy(isPaid = true, paidDate = dateStr) else b }
          }
          showToast("${bill.companyUrdu} ادا ہو گیا!")
        }
      } else {
        _utilityBills.update { bills -> bills.map { b -> if (b.id == billId) b.copy(isPaid = false, paidDate = null) else b } }
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
    viewModelScope.launch {
      val request = CreateBillRequest(
        companyName = company,
        companyUrdu = "$company بل",
        consumerNumber = consumerNo,
        billType = type,
        month = SimpleDateFormat("MMMM yyyy", Locale.US).format(Date()),
        dueDate = dueDate,
        amount = amount,
        unitsConsumed = units,
        alertTip = "بروقت ادائیگی پر جرمانہ اور لیٹ سرچارج سے بچیں۔"
      )
      val result = repository.addBill(request)
      if (result is Result.Success) {
        _utilityBills.update { listOf(result.data) + it }
      } else {
        val newB = UtilityBill(
          id = "bill-${System.currentTimeMillis()}",
          companyName = company,
          companyUrdu = "$company بل",
          consumerNumber = consumerNo,
          billType = type,
          month = SimpleDateFormat("MMMM yyyy", Locale.US).format(Date()),
          dueDate = dueDate,
          amount = amount,
          unitsConsumed = units,
          isPaid = false,
          alertTip = "بروقت ادائیگی پر جرمانہ اور لیٹ سرچارج سے بچیں۔"
        )
        _utilityBills.update { listOf(newB) + it }
      }
      showToast("نیا بل $company کامیابی سے شامل ہو گیا!")
    }
  }

  // ─── Debt Snowball ────────────────────────────────────────────────────────────

  fun repayDebt(debtId: String, paymentAmount: Long) {
    viewModelScope.launch {
      val result = repository.repayDebt(debtId, paymentAmount)
      if (result is Result.Success) {
        _debts.update { list -> list.map { if (it.id == debtId) result.data else it } }
        val updated = result.data
        if (updated.remainingAmount == 0L) {
          showToast("مبارک ہو! ${updated.creditorUrdu} کا قرض مکمل ادا ہو گیا! 🎉")
          playVoice("Mubarak ho! Aap ne qarz mukammal ada kar diya hai.", "Mubarak ho!")
        } else {
          showToast("روپے $paymentAmount ادا کر دیے گئے۔ باقی قرض اپ ڈیٹ ہو گیا۔")
        }
      } else {
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
        if (fullyPaid) {
          showToast("مبارک ہو! $debtTitle کا قرض مکمل ادا ہو گیا! 🎉")
          playVoice("Mubarak ho! Qarz mukammal ada ho gaya.", "Mubarak ho!")
        } else {
          showToast("روپے $paymentAmount ادا کر دیے گئے۔")
        }
      }
      addTransaction(
        title = "Debt Payment",
        urduTitle = "قرض ادائیگی",
        amount = paymentAmount,
        isExpense = true,
        category = "قرض نجات",
        envelopeId = "savings",
        paymentMethod = "نقد کیش"
      )
    }
  }

  fun addNewDebt(
    name: String,
    relation: String,
    total: Long,
    monthly: Long,
    urgency: String
  ) {
    viewModelScope.launch {
      val request = CreateDebtRequest(
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
      val result = repository.addDebt(request)
      if (result is Result.Success) {
        _debts.update { it + result.data }
      } else {
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
      }
      showToast("قرض کھاتہ شامل کر لیا گیا۔")
    }
  }

  // ─── Lifecycle ───────────────────────────────────────────────────────────────

  override fun onCleared() {
    super.onCleared()
    try {
      tts?.stop()
      tts?.shutdown()
    } catch (_: Exception) {}
  }
}
