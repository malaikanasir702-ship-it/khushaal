package com.example.ui.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.OnboardingPrimaryButton
import com.example.ui.components.OnboardingSecondaryButton
import com.example.ui.components.OnboardingSkipButton
import com.example.ui.components.StepDotIndicator
import com.example.ui.theme.*

private data class ExpenseCategory(
    val urdu: String,
    val english: String,
    val pct: Float,
    val emoji: String,
)

private val EXPENSE_CATEGORIES = listOf(
    ExpenseCategory("کھانا", "Food", 0.45f, "🍱"),
    ExpenseCategory("ٹرانسپورٹ", "Transport", 0.20f, "🚌"),
    ExpenseCategory("تعلیم", "Education", 0.10f, "📚"),
    ExpenseCategory("صحت", "Health", 0.10f, "💊"),
    ExpenseCategory("دیگر", "Others", 0.15f, "📦"),
)

@Composable
fun OnboardingStep4SalaryBudgeting(
    onNext: () -> Unit,
    onBack: () -> Unit,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(horizontal = 20.dp)
    ) {
        StepDotIndicator(totalDots = 7, currentDot = 2)

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "اخراجات ٹریک کریں",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = SlateTextPrimary
            )
            Text(
                text = "Track Expenses",
                fontSize = 15.sp,
                color = TealPrimary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(14.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    EXPENSE_CATEGORIES.forEach { cat ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(cat.emoji, fontSize = 18.sp, modifier = Modifier.width(32.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(cat.urdu, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = SlateTextPrimary)
                                    Text("${(cat.pct * 100).toInt()}%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TealPrimary)
                                }
                                Text(cat.english, fontSize = 11.sp, color = SlateTextSecondary)
                                Spacer(modifier = Modifier.height(4.dp))
                                LinearProgressIndicator(
                                    progress = { cat.pct },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp),
                                    color = TealPrimary,
                                    trackColor = TealSurface,
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Tip card
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = AmberSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, AmberBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("💡 ٹپ / Tip", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AmberDark)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "اپنی روزانہ خرچ کی نگرانی سے بجٹ پر قابو رکھیں",
                        fontSize = 13.sp,
                        color = SlateTextPrimary
                    )
                    Text(
                        "Tracking daily expenses keeps you in control of your budget",
                        fontSize = 11.sp,
                        color = SlateTextSecondary
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        OnboardingPrimaryButton(text = "اگلا / Next", onClick = onNext)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OnboardingSecondaryButton(text = "← پیچھے / Back", onClick = onBack)
            OnboardingSkipButton(onClick = onSkip)
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}
