package com.example.ui.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.OnboardingPrimaryButton
import com.example.ui.components.OnboardingSecondaryButton
import com.example.ui.components.OnboardingSkipButton
import com.example.ui.components.StepDotIndicator
import com.example.ui.theme.*

private data class FraudCard(
    val emoji: String,
    val urdu: String,
    val english: String,
    val tipUrdu: String,
    val tipEnglish: String,
)

private val FRAUD_CARDS = listOf(
    FraudCard(
        "⚠️", "دھوکہ دہی سے بچیں", "Beware of scams",
        "نامعلوم نمبروں سے آنے والی کالوں پر اعتبار نہ کریں",
        "Don't trust calls from unknown numbers"
    ),
    FraudCard(
        "💳", "محفوظ ڈیجیٹل ادائیگی", "Safe digital payments",
        "صرف سرکاری ایپس سے ادائیگی کریں",
        "Only pay through official apps"
    ),
    FraudCard(
        "🔐", "اپنا OTP محفوظ رکھیں", "Protect your OTP",
        "OTP کسی سے شیئر نہ کریں، یہاں تک کہ بینک سے بھی نہیں",
        "Never share your OTP, not even with your bank"
    ),
    FraudCard(
        "📢", "مشکوک سرگرمی کی اطلاع دیں", "Report suspicious activity",
        "فوری طور پر اپنے بینک سے رابطہ کریں",
        "Contact your bank immediately"
    ),
)

@Composable
fun OnboardingStep6FraudAwareness(
    expandedCardIndex: Int,
    onToggleCard: (Int) -> Unit,
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
        StepDotIndicator(totalDots = 7, currentDot = 4)

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "محفوظ رہیں",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = SlateTextPrimary
            )
            Text(
                text = "Stay Safe",
                fontSize = 15.sp,
                color = TealPrimary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(14.dp))

            FRAUD_CARDS.forEachIndexed { index, card ->
                val isExpanded = expandedCardIndex == index
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isExpanded) TealSurface else Color.White
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isExpanded) TealBorder else SlateBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { onToggleCard(index) }
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(card.emoji, fontSize = 18.sp)
                                Column {
                                    Text(card.urdu, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = SlateTextPrimary)
                                    Text(card.english, fontSize = 11.sp, color = SlateTextSecondary)
                                }
                            }
                            Icon(
                                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = null,
                                tint = TealPrimary
                            )
                        }
                        if (isExpanded) {
                            Spacer(modifier = Modifier.height(10.dp))
                            HorizontalDivider(color = TealBorder)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(card.tipUrdu, fontSize = 13.sp, color = TealDark, fontWeight = FontWeight.Medium)
                            Text(card.tipEnglish, fontSize = 11.sp, color = SlateTextSecondary)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Static tip
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = RoseSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, RoseBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("🏦 یاد رکھیں!", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = RoseDark)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "آپ کا بینک کبھی بھی فون پر OTP نہیں مانگتا",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = SlateTextPrimary
                    )
                    Text(
                        "Your bank will never ask for OTP over the phone",
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
