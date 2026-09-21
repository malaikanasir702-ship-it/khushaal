package com.example.onboarding

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class OnboardingViewModel : ViewModel() {

    var currentStep by mutableIntStateOf(1)
        private set

    var incomeInputText by mutableStateOf("")
        private set

    var monthlyIncome by mutableLongStateOf(0L)
        private set

    var incomeError by mutableStateOf(false)
        private set

    // Fraud accordion — which card index is expanded (-1 = none)
    var expandedFraudCardIndex by mutableIntStateOf(-1)
        private set

    fun onIncomeTextChanged(text: String) {
        incomeInputText = text
        incomeError = false
        monthlyIncome = text.trim().toLongOrNull() ?: 0L
    }

    fun validateAndGoNext(): Boolean {
        val income = incomeInputText.trim().toLongOrNull()
        return if (income == null || income <= 0L) {
            incomeError = true
            false
        } else {
            incomeError = false
            monthlyIncome = income
            goNext()
            true
        }
    }

    fun goNext() {
        if (currentStep < 8) currentStep++
    }

    fun goBack() {
        if (currentStep > 1) currentStep--
    }

    fun toggleFraudCard(index: Int) {
        expandedFraudCardIndex = if (expandedFraudCardIndex == index) -1 else index
    }
}
