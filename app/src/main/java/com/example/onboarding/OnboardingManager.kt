package com.example.onboarding

import android.content.Context

class OnboardingManager(context: Context) {
    private val prefs = context.getSharedPreferences("khushhaal_prefs", Context.MODE_PRIVATE)

    fun isCompleted(): Boolean = try {
        prefs.getBoolean("onboarding_completed", false)
    } catch (e: Exception) {
        false
    }

    fun markCompleted() {
        prefs.edit().putBoolean("onboarding_completed", true).apply()
    }
}
