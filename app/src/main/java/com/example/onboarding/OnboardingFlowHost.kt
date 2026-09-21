package com.example.onboarding

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.OnboardingData
import com.example.ui.screens.onboarding.OnboardingStep1Welcome
import com.example.ui.screens.onboarding.OnboardingStep2FinancialHealth
import com.example.ui.screens.onboarding.OnboardingStep3MonthlyPlan
import com.example.ui.screens.onboarding.OnboardingStep4SalaryBudgeting
import com.example.ui.screens.onboarding.OnboardingStep5SavingsGoals
import com.example.ui.screens.onboarding.OnboardingStep6FraudAwareness
import com.example.ui.screens.onboarding.OnboardingStep7SideIncome
import com.example.ui.screens.onboarding.OnboardingStep8Progress

@Composable
fun OnboardingFlowHost(
    onComplete: (OnboardingData) -> Unit,
    onLoginTapped: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: OnboardingViewModel = viewModel()
    val currentStep = viewModel.currentStep

    // BackHandler: Step 1 = no-op, Steps 2-8 = go back
    BackHandler(enabled = currentStep > 1) {
        viewModel.goBack()
    }

    when (currentStep) {
        1 -> OnboardingStep1Welcome(
            onGetStarted = { viewModel.goNext() },
            onLogin = { onLoginTapped() },
            modifier = modifier
        )
        2 -> OnboardingStep2FinancialHealth(
            incomeText = viewModel.incomeInputText,
            incomeError = viewModel.incomeError,
            onIncomeChanged = { viewModel.onIncomeTextChanged(it) },
            onNext = { viewModel.validateAndGoNext() },
            onSkip = { onComplete(OnboardingData(monthlyIncome = 0L)) },
            modifier = modifier
        )
        3 -> OnboardingStep3MonthlyPlan(
            monthlyIncome = viewModel.monthlyIncome,
            onNext = { viewModel.goNext() },
            onBack = { viewModel.goBack() },
            onSkip = { onComplete(OnboardingData(monthlyIncome = 0L)) },
            modifier = modifier
        )
        4 -> OnboardingStep4SalaryBudgeting(
            onNext = { viewModel.goNext() },
            onBack = { viewModel.goBack() },
            onSkip = { onComplete(OnboardingData(monthlyIncome = 0L)) },
            modifier = modifier
        )
        5 -> OnboardingStep5SavingsGoals(
            onNext = { viewModel.goNext() },
            onBack = { viewModel.goBack() },
            onSkip = { onComplete(OnboardingData(monthlyIncome = 0L)) },
            modifier = modifier
        )
        6 -> OnboardingStep6FraudAwareness(
            expandedCardIndex = viewModel.expandedFraudCardIndex,
            onToggleCard = { index -> viewModel.toggleFraudCard(index) },
            onNext = { viewModel.goNext() },
            onBack = { viewModel.goBack() },
            onSkip = { onComplete(OnboardingData(monthlyIncome = 0L)) },
            modifier = modifier
        )
        7 -> OnboardingStep7SideIncome(
            onNext = { viewModel.goNext() },
            onBack = { viewModel.goBack() },
            onSkip = { onComplete(OnboardingData(monthlyIncome = 0L)) },
            modifier = modifier
        )
        8 -> OnboardingStep8Progress(
            onFinish = { onComplete(OnboardingData(monthlyIncome = viewModel.monthlyIncome)) },
            onBack = { viewModel.goBack() },
            modifier = modifier
        )
    }
}
