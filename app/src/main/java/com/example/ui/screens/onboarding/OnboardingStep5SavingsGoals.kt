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

private data class SampleGoal(
    val emoji: String,
    val urdu: String,
    val english: String,
    val saved: Long,
    val target: Long,
)

private val SAMPLE_GOALS = listOf(
    SampleGoal("🛡️", "ایمرجنسی فنڈ", "Emergency Fund", 15_000L, 50_000L),
    SampleGoal("👨‍👩‍👧", "خاندانی ہدف", "Family Goal", 25_000L, 100_000L),
    SampleGoal("🎓", "تعلیمی ہدف", "Education Goal", 10_000L, 50_000L),
)

@Composable
fun OnboardingStep5SavingsGoals(
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
        StepDotIndicator(totalDots = 7, currentDot = 3)

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "میرے اہداف",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = SlateTextPrimary
            )
            Text(
                text = "My Goals",
                fontSize = 15.sp,
                color = TealPrimary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(14.dp))

            SAMPLE_GOALS.forEach { goal ->
                val progress = goal.saved.toFloat() / goal.target.toFloat()
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(goal.emoji, fontSize = 20.sp)
                            Column(modifier = Modifier.weight(1f)) {
                                Text(goal.urdu, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = SlateTextPrimary)
                                Text(goal.english, fontSize = 12.sp, color = SlateTextSecondary)
                            }
                            Text(
                                "${(progress * 100).toInt()}%",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TealPrimary
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp),
                            color = TealPrimary,
                            trackColor = TealSurface,
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("بچت: Rs. %,d".format(goal.saved), fontSize = 11.sp, color = EmeraldDark)
                            Text("ہدف: Rs. %,d".format(goal.target), fontSize = 11.sp, color = SlateTextSecondary)
                        }
                    }
                }
            }

            // Note card
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = TealSurface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "ایمرجنسی فنڈ اور طویل المدت بچت کے اثاثے بنائیں\nBuild emergency fund and long-term savings assets",
                    fontSize = 12.sp,
                    color = TealDark,
                    modifier = Modifier.padding(12.dp)
                )
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
