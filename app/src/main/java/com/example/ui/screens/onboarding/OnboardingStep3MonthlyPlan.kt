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

private data class BudgetCategory(
    val urdu: String,
    val english: String,
    val pct: Int,
)

private val BUDGET_CATEGORIES = listOf(
    BudgetCategory("گھریلو ضروریات", "Household Needs", 50),
    BudgetCategory("بچت", "Savings", 20),
    BudgetCategory("ایمرجنسی فنڈ", "Emergency Fund", 10),
    BudgetCategory("تعلیم", "Education", 10),
    BudgetCategory("دیگر", "Others", 10),
)

@Composable
fun OnboardingStep3MonthlyPlan(
    monthlyIncome: Long,
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
        StepDotIndicator(totalDots = 7, currentDot = 1)

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "میرا ماہانہ پلان",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = SlateTextPrimary
            )
            Text(
                text = "My Monthly Plan",
                fontSize = 15.sp,
                color = TealPrimary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Income display
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = TealSurface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("ماہانہ آمدن / Monthly Income", fontSize = 12.sp, color = TealDark)
                    }
                    Text(
                        text = "Rs. ${if (monthlyIncome > 0) "%,d".format(monthlyIncome) else "0"}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TealPrimary
                    )
                }
            }

            if (monthlyIncome <= 0L) {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = AmberSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AmberBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "⬅ پیچھے جا کر آمدن درج کریں / Go back and enter your income",
                        fontSize = 12.sp,
                        color = AmberDarker,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Budget breakdown
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    BUDGET_CATEGORIES.forEach { cat ->
                        val amount = monthlyIncome * cat.pct / 100
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(cat.urdu, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = SlateTextPrimary)
                                Text("${cat.english} — ${cat.pct}%", fontSize = 11.sp, color = SlateTextSecondary)
                            }
                            Text(
                                text = "Rs. %,d".format(amount),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TealPrimary
                            )
                        }
                        if (cat != BUDGET_CATEGORIES.last()) {
                            HorizontalDivider(color = SlateBorder.copy(alpha = 0.5f))
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Buttons
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
