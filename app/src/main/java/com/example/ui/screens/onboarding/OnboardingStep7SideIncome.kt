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

private data class IncomeOpportunity(
    val emoji: String,
    val urdu: String,
    val english: String,
)

private val INCOME_OPPORTUNITIES = listOf(
    IncomeOpportunity("🪡", "سلائی و درزی", "Stitching & Tailoring"),
    IncomeOpportunity("🍱", "گھر میں کھانا بنانا", "Home-based Food"),
    IncomeOpportunity("📚", "ٹیوشن", "Tutoring"),
    IncomeOpportunity("🚗", "ڈرائیونگ و ٹرانسپورٹ", "Driving & Transport"),
    IncomeOpportunity("💻", "ڈیجیٹل کام", "Digital Work"),
    IncomeOpportunity("🛍️", "خوردہ و چھوٹا کاروبار", "Retail & Small Business"),
)

@Composable
fun OnboardingStep7SideIncome(
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
        StepDotIndicator(totalDots = 7, currentDot = 5)

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "آمدن کے مواقع",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = SlateTextPrimary
            )
            Text(
                text = "Income Opportunities",
                fontSize = 15.sp,
                color = TealPrimary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "مواقع دریافت کریں، وسائل حاصل کریں اور اضافی آمدن بنائیں",
                fontSize = 13.sp,
                color = SlateTextSecondary
            )
            Text(
                text = "Discover opportunities, get resources and build additional income",
                fontSize = 12.sp,
                color = SlateTextMuted
            )

            Spacer(modifier = Modifier.height(14.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    INCOME_OPPORTUNITIES.forEachIndexed { idx, opp ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp, horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(opp.emoji, fontSize = 24.sp)
                            Column(modifier = Modifier.weight(1f)) {
                                Text(opp.urdu, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = SlateTextPrimary)
                                Text(opp.english, fontSize = 12.sp, color = SlateTextSecondary)
                            }
                            Box(
                                modifier = Modifier
                                    .background(TealSurface, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text("→", fontSize = 12.sp, color = TealPrimary, fontWeight = FontWeight.Bold)
                            }
                        }
                        if (idx < INCOME_OPPORTUNITIES.lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                color = SlateBorder.copy(alpha = 0.4f)
                            )
                        }
                    }
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
