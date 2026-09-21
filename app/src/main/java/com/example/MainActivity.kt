package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.onboarding.OnboardingFlowHost
import com.example.onboarding.OnboardingManager
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.api.UpdateProfileRequest
import com.example.model.AppDestination
import com.example.model.AppTab
import com.example.ui.components.AudioPlayingBanner
import com.example.ui.components.FloatingToast
import com.example.ui.components.FraudShieldDialog
import com.example.ui.components.KhushhaalBottomNav
import com.example.ui.components.KhushhaalHeader
import com.example.ui.components.LanguageSelectionBottomSheet
import com.example.ui.components.QuickExpenseBottomSheet
import com.example.ui.screens.BusinessKhataScreen
import com.example.ui.screens.CoachChatScreen
import com.example.ui.screens.EarnMoreScreen
import com.example.ui.screens.EmergencyLockerScreen
import com.example.ui.screens.FactorySalarySlipScreen
import com.example.ui.screens.FraudAcademyScreen
import com.example.ui.screens.GoalsAndKametiScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.MoneyScreen
import com.example.ui.screens.NotificationScreen
import com.example.ui.screens.ProfileSettingsScreen
import com.example.ui.screens.ProsperityScreen
import com.example.ui.screens.RationCalculatorScreen
import com.example.ui.screens.TransactionHistoryScreen
import com.example.ui.screens.DebtSnowballScreen
import com.example.ui.screens.UtilityBillsScreen
import com.example.ui.screens.EnvelopeSplitScreen
import com.example.ui.theme.KhushhaalTheme
import com.example.viewmodel.AuthUiState
import com.example.viewmodel.AuthViewModel
import com.example.viewmodel.KhushhaalViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      KhushhaalTheme {
        val viewModel: KhushhaalViewModel = viewModel()
        val authViewModel: AuthViewModel = viewModel()
        val isLoggedIn by authViewModel.isLoggedIn.collectAsStateWithLifecycle()
        val authUiState by authViewModel.uiState.collectAsStateWithLifecycle()

        val onboardingManager = remember { OnboardingManager(applicationContext) }
        var onboardingCompleted by remember { mutableStateOf(onboardingManager.isCompleted()) }

        // When login/register succeeds, reload all data
        LaunchedEffect(isLoggedIn) {
          if (isLoggedIn) {
            viewModel.loadAllData()
          } else {
            viewModel.resetAllState()
          }
        }

        if (!onboardingCompleted) {
          OnboardingFlowHost(
            onComplete = { data ->
              onboardingManager.markCompleted()
              if (data.monthlyIncome > 0L) {
                viewModel.setOnboardingIncome(data.monthlyIncome)
              }
              onboardingCompleted = true
            },
            onLoginTapped = {
              onboardingManager.markCompleted()
              onboardingCompleted = true
            }
          )
        } else if (isLoggedIn) {
          KhushhaalApp(viewModel = viewModel, authViewModel = authViewModel)
        } else {
          AuthFlow(authViewModel = authViewModel)
        }
      }
    }
  }
}

@Composable
fun KhushhaalApp(
  viewModel: KhushhaalViewModel,
  authViewModel: AuthViewModel,
  modifier: Modifier = Modifier,
) {
  val currentDestination by viewModel.currentDestination.collectAsStateWithLifecycle()
  val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
  val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
  val cashFlow by viewModel.cashFlow.collectAsStateWithLifecycle()
  val prosperityScore by viewModel.prosperityScore.collectAsStateWithLifecycle()
  val pillars by viewModel.pillars.collectAsStateWithLifecycle()
  val envelopes by viewModel.envelopes.collectAsStateWithLifecycle()
  val selectedSkill by viewModel.selectedSkill.collectAsStateWithLifecycle()
  val roadmapSteps by viewModel.roadmapSteps.collectAsStateWithLifecycle()
  val isChallengeJoined by viewModel.isChallengeJoined.collectAsStateWithLifecycle()
  val emergencyDeposited by viewModel.emergencyDeposited.collectAsStateWithLifecycle()
  val wageAdvanceRequested by viewModel.wageAdvanceRequested.collectAsStateWithLifecycle()
  val isFraudModalOpen by viewModel.isFraudModalOpen.collectAsStateWithLifecycle()
  val isPlayingAudio by viewModel.isPlayingAudio.collectAsStateWithLifecycle()
  val currentAudioCaption by viewModel.currentAudioCaption.collectAsStateWithLifecycle()
  val toastMessage by viewModel.toastMessage.collectAsStateWithLifecycle()

  val currentLanguage by viewModel.currentLanguage.collectAsStateWithLifecycle()
  val isLanguageSheetOpen by viewModel.isLanguageSheetOpen.collectAsStateWithLifecycle()
  val isQuickExpenseSheetOpen by viewModel.isQuickExpenseSheetOpen.collectAsStateWithLifecycle()
  val notifications by viewModel.notifications.collectAsStateWithLifecycle()

  val transactions by viewModel.transactions.collectAsStateWithLifecycle()
  val kametis by viewModel.kametis.collectAsStateWithLifecycle()
  val goals by viewModel.goals.collectAsStateWithLifecycle()
  val emergencyLockerBalance by viewModel.emergencyLockerBalance.collectAsStateWithLifecycle()
  val scamSimulations by viewModel.scamSimulations.collectAsStateWithLifecycle()
  val coachMessages by viewModel.coachMessages.collectAsStateWithLifecycle()
  val customerOrders by viewModel.customerOrders.collectAsStateWithLifecycle()
  val payslip by viewModel.payslip.collectAsStateWithLifecycle()
  val rationEstimates by viewModel.rationEstimates.collectAsStateWithLifecycle()
  val utilityBills by viewModel.utilityBills.collectAsStateWithLifecycle()
  val debts by viewModel.debts.collectAsStateWithLifecycle()

  // Company-configured dynamic content
  val appConfigFactoryName by viewModel.appConfigFactoryName.collectAsStateWithLifecycle()
  val appConfigHelpline by viewModel.appConfigHelpline.collectAsStateWithLifecycle()
  val appConfigHelplineLabel by viewModel.appConfigHelplineLabel.collectAsStateWithLifecycle()

  // Hardware/System back button handling
  BackHandler(enabled = currentDestination != AppDestination.TabView) {
    viewModel.navigateBack()
  }

  Box(modifier = modifier.fillMaxSize().background(Color(0xFFF8FAFC))) {
    AnimatedContent(
      targetState = currentDestination,
      transitionSpec = {
        fadeIn() togetherWith fadeOut()
      },
      label = "destination_transition"
    ) { destination ->
      when (destination) {
        AppDestination.TabView -> {
          Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color(0xFFF8FAFC),
            topBar = {
              Column(modifier = Modifier.fillMaxWidth()) {
                KhushhaalHeader(
                  currentTab = currentTab,
                  isPlayingAudio = isPlayingAudio,
                  unreadNotificationCount = notifications.count { !it.isRead },
                  currentLanguage = currentLanguage,
                  onAudioClick = { viewModel.toggleUniversalAudio() },
                  onLanguageToggle = {
                    viewModel.openLanguageSheet()
                  },
                  onNotificationClick = {
                    viewModel.navigateTo(AppDestination.Notifications)
                  },
                  onProfileClick = {
                    viewModel.navigateTo(AppDestination.ProfileSettings)
                  }
                )

                AudioPlayingBanner(
                  isPlaying = isPlayingAudio,
                  caption = currentAudioCaption,
                  onStop = { viewModel.stopVoice() }
                )

                FloatingToast(
                  message = toastMessage,
                  onDismiss = { viewModel.dismissToast() }
                )
              }
            },
            bottomBar = {
              KhushhaalBottomNav(
                currentTab = currentTab,
                onTabSelected = { tab ->
                  viewModel.selectTab(tab)
                }
              )
            }
          ) { innerPadding ->
            Box(
              modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF8FAFC))
            ) {
              AnimatedContent(
                targetState = currentTab,
                transitionSpec = {
                  fadeIn() togetherWith fadeOut()
                },
                label = "tab_transition"
              ) { targetTab ->
                when (targetTab) {
                  AppTab.HOME -> {
                    HomeScreen(
                      userProfile = userProfile,
                      prosperityScore = prosperityScore,
                      cashFlow = cashFlow,
                      isChallengeJoined = isChallengeJoined,
                      emergencyDeposited = emergencyDeposited,
                      onNavigateToTab = { tab -> viewModel.selectTab(tab) },
                      onOpenFraudModal = { viewModel.openFraudModal() },
                      onDepositEmergency = { viewModel.depositEmergencyFund() },
                      onToggleChallenge = { viewModel.toggleChallenge() },
                      onCoachCall = { viewModel.navigateTo(AppDestination.CoachChat) },
                      onPlayVoice = { caption, spoken -> viewModel.playVoice(caption, spoken) },
                      onNavigateToDestination = { dest -> viewModel.navigateTo(dest) }
                    )
                  }
                  AppTab.MONEY -> {
                    MoneyScreen(
                      cashFlow = cashFlow,
                      envelopes = envelopes,
                      wageAdvanceRequested = wageAdvanceRequested,
                      onLogExpense = { name, amount -> viewModel.logQuickExpense(name, amount) },
                      onVoiceLogExpense = { viewModel.simulateVoiceLog() },
                      onRequestWageAdvance = { viewModel.requestWageAdvance() },
                      onPlayVoice = { caption, spoken -> viewModel.playVoice(caption, spoken) },
                      onNavigateToDestination = { dest -> viewModel.navigateTo(dest) }
                    )
                  }
                  AppTab.PROSPERITY -> {
                    ProsperityScreen(
                      prosperityScore = prosperityScore,
                      pillars = pillars,
                      onNavigateToTab = { tab -> viewModel.selectTab(tab) },
                      onOpenFraudModal = { viewModel.openFraudModal() },
                      onShowToast = { msg -> viewModel.showToast(msg) },
                      onPlayVoice = { caption, spoken -> viewModel.playVoice(caption, spoken) },
                      onNavigateToDestination = { dest -> viewModel.navigateTo(dest) }
                    )
                  }
                  AppTab.EARN_MORE -> {
                    EarnMoreScreen(
                      availableSkills = viewModel.availableSkills,
                      selectedSkill = selectedSkill,
                      roadmapSteps = roadmapSteps,
                      onSelectSkill = { skill -> viewModel.selectSkill(skill) },
                      onToggleRoadmapStep = { week -> viewModel.toggleRoadmapStep(week) },
                      onActivatePlan = { viewModel.orderMarketingKit() },
                      onPlayVoice = { caption, spoken -> viewModel.playVoice(caption, spoken) },
                      onNavigateToDestination = { dest -> viewModel.navigateTo(dest) }
                    )
                  }
                }
              }
            }
          }
        }

        AppDestination.GoalsAndKameti -> {
          GoalsAndKametiScreen(
            goals = goals,
            kametis = kametis,
            onBack = { viewModel.navigateBack() },
            onMarkKametiPaid = { kametiId -> viewModel.markKametiPaid(kametiId) },
            onAddNewKameti = { name, amount, totalMembers, myTurn, organizer ->
              viewModel.addNewKameti(name, amount, totalMembers, myTurn, organizer)
            },
            onContributeToGoal = { goalId, amount -> viewModel.contributeToGoal(goalId, amount) },
            onAddNewGoal = { title, target, date, emoji ->
              viewModel.addNewGoal(title, target, date, emoji)
            },
            onPlayVoice = { c, s -> viewModel.playVoice(c, s) }
          )
        }

        AppDestination.TransactionHistory -> {
          TransactionHistoryScreen(
            transactions = transactions,
            onBack = { viewModel.navigateBack() },
            onAddTransaction = { title, urdu, amount, isExpense, cat, env, pay ->
              viewModel.addTransaction(title, urdu, amount, isExpense, cat, env, pay)
            },
            onPlayVoice = { c, s -> viewModel.playVoice(c, s) }
          )
        }

        AppDestination.EmergencyLocker -> {
          EmergencyLockerScreen(
            balance = emergencyLockerBalance,
            onBack = { viewModel.navigateBack() },
            onDeposit = { amount -> viewModel.depositLocker(amount) },
            onWithdraw = { amount -> viewModel.withdrawLocker(amount) },
            onPlayVoice = { c, s -> viewModel.playVoice(c, s) }
          )
        }

        AppDestination.FraudAcademy -> {
          FraudAcademyScreen(
            scenarios = scamSimulations,
            onBack = { viewModel.navigateBack() },
            onAnswer = { scamId, choseSafe ->
              viewModel.answerScam(scamId, choseSafe)
            },
            onPlayVoice = { c, s -> viewModel.playVoice(c, s) }
          )
        }

        AppDestination.CoachChat -> {
          CoachChatScreen(
            messages = coachMessages,
            onBack = { viewModel.navigateBack() },
            onSendMessage = { text -> viewModel.sendCoachMessage(text) },
            onPlayVoice = { c, s -> viewModel.playVoice(c, s) }
          )
        }

        AppDestination.BusinessKhata -> {
          BusinessKhataScreen(
            orders = customerOrders,
            onBack = { viewModel.navigateBack() },
            onToggleDelivered = { orderId -> viewModel.toggleOrderDelivered(orderId) },
            onTogglePaid = { orderId -> viewModel.toggleOrderPaid(orderId) },
            onAddNewOrder = { name, phone, service, total, adv, due ->
              viewModel.addNewCustomerOrder(name, phone, service, total, adv, due)
            },
            onPlayVoice = { c, s -> viewModel.playVoice(c, s) }
          )
        }

        AppDestination.ProfileSettings -> {
          ProfileSettingsScreen(
            userProfile = userProfile,
            onBack = { viewModel.navigateBack() },
            onShowToast = { msg -> viewModel.showToast(msg) },
            onPlayVoice = { c, s -> viewModel.playVoice(c, s) },
            onLogout = {
              authViewModel.logout {
                viewModel.resetAllState()
              }
            },
            onUpdateProfile = { req ->
              viewModel.updateProfile(req)
            },
            factoryName = appConfigFactoryName,
            welfareHelpline = appConfigHelpline,
            welfareHelplineLabel = appConfigHelplineLabel,
            onNavigateToDestination = { dest -> viewModel.navigateTo(dest) }
          )
        }

        AppDestination.Notifications -> {
          NotificationScreen(
            notifications = notifications,
            currentLanguage = currentLanguage,
            onBack = { viewModel.navigateBack() },
            onNotificationClick = { notif ->
              viewModel.markNotificationAsRead(notif.id)
              if (notif.destination != null) {
                viewModel.navigateTo(notif.destination)
              }
            },
            onMarkAllRead = { viewModel.markAllNotificationsAsRead() },
            onClearAll = { viewModel.clearAllNotifications() },
            onPlayVoice = { c, s -> viewModel.playVoice(c, s) }
          )
        }

        AppDestination.FactorySalarySlip -> {
          FactorySalarySlipScreen(
            payslip = payslip,
            onBack = { viewModel.navigateBack() },
            onShowToast = { msg -> viewModel.showToast(msg) },
            onPlayVoice = { c, s -> viewModel.playVoice(c, s) }
          )
        }

        AppDestination.RationCalculator -> {
          RationCalculatorScreen(
            rationItems = rationEstimates,
            onBack = { viewModel.navigateBack() },
            onSaveToRationEnvelope = { newAmount ->
              viewModel.updateRationEnvelopeAmount(newAmount)
            },
            onShowToast = { msg -> viewModel.showToast(msg) },
            onPlayVoice = { c, s -> viewModel.playVoice(c, s) }
          )
        }

        AppDestination.UtilityBills -> {
          UtilityBillsScreen(
            bills = utilityBills,
            onBack = { viewModel.navigateBack() },
            onTogglePayBill = { billId -> viewModel.togglePayBill(billId) },
            onAddNewBill = { company, consumerNo, type, amount, units, dueDate ->
              viewModel.addNewBill(company, consumerNo, type, amount, units, dueDate)
            },
            onShowToast = { msg -> viewModel.showToast(msg) },
            onPlayVoice = { c, s -> viewModel.playVoice(c, s) }
          )
        }

        AppDestination.DebtSnowball -> {
          DebtSnowballScreen(
            debts = debts,
            onBack = { viewModel.navigateBack() },
            onRepayDebt = { debtId, amount -> viewModel.repayDebt(debtId, amount) },
            onAddNewDebt = { name, relation, total, monthly, urgency ->
              viewModel.addNewDebt(name, relation, total, monthly, urgency)
            },
            onShowToast = { msg -> viewModel.showToast(msg) },
            onPlayVoice = { c, s -> viewModel.playVoice(c, s) }
          )
        }

        AppDestination.EnvelopeSplit -> {
          EnvelopeSplitScreen(
            envelopes = envelopes,
            totalSalary = cashFlow.income,
            onBack = { viewModel.navigateBack() },
            onNavigateToDestination = { dest -> viewModel.navigateTo(dest) },
            onShowToast = { msg -> viewModel.showToast(msg) },
            onPlayVoice = { c, s -> viewModel.playVoice(c, s) }
          )
        }
      }
    }
  }

  // Language Selection Bottom Sheet
  LanguageSelectionBottomSheet(
    isOpen = isLanguageSheetOpen,
    currentLanguage = currentLanguage,
    onLanguageSelected = { lang -> viewModel.setLanguage(lang) },
    onDismiss = { viewModel.closeLanguageSheet() }
  )

  // Quick Expense Bottom Sheet
  QuickExpenseBottomSheet(
    isOpen = isQuickExpenseSheetOpen,
    onDismiss = { viewModel.closeQuickExpenseSheet() },
    onLogExpense = { name, amount ->
      viewModel.addTransaction(
        title = name,
        urduTitle = name,
        amount = amount,
        isExpense = true,
        category = "Needs",
        envelopeId = "needs",
        paymentMethod = "Cash"
      )
    }
  )

  // Interactive Fraud Shield quick modal
  FraudShieldDialog(
    isOpen = isFraudModalOpen,
    onDismiss = { viewModel.closeFraudModal() },
    onAnswer = { isCorrect -> viewModel.answerFraudQuiz(isCorrect) },
    onListenAudio = {
      viewModel.playVoice(
        caption = "Agar koi call kar ke kahe ke OTP ya PIN batao, toh foran call kaat dein. Bank kabhi phone par OTP nahi poochhta.",
        spokenText = "Agar koi call kar ke kahe ke OTP ya PIN batao, toh foran call kaat dein."
      )
    }
  )
}


@Composable
fun AuthFlow(
  authViewModel: AuthViewModel,
  modifier: Modifier = Modifier
) {
  var showRegister by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }

  if (showRegister) {
    com.example.ui.screens.RegistrationScreen(
      authViewModel = authViewModel,
      onNavigateToLogin = {
        authViewModel.resetState()
        showRegister = false
      },
      onRegisterSuccess = {
        // isLoggedIn flow handles the transition automatically
      },
      modifier = modifier
    )
  } else {
    com.example.ui.screens.LoginScreen(
      authViewModel = authViewModel,
      onNavigateToRegister = {
        authViewModel.resetState()
        showRegister = true
      },
      onLoginSuccess = {
        // isLoggedIn flow handles the transition automatically
      },
      modifier = modifier
    )
  }
}
