package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberBrand
import com.example.ui.theme.AmberDark
import com.example.ui.theme.AmberLight
import com.example.ui.theme.AmberSurface
import com.example.ui.theme.EmeraldBrand
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.EmeraldSurface
import com.example.ui.theme.RoseAlert
import com.example.ui.theme.RoseBorder
import com.example.ui.theme.RoseDark
import com.example.ui.theme.RoseLight
import com.example.ui.theme.RoseSurface
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateBorderSubtle
import com.example.ui.theme.SlateTextMuted
import com.example.ui.theme.SlateTextPrimary
import com.example.ui.theme.SlateTextSecondary
import com.example.ui.theme.TealBorder
import com.example.ui.theme.TealDark
import com.example.ui.theme.TealDarker
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.TealSurface
import kotlin.math.roundToInt

/**
 * Clean, light-themed Smart Envelope Split card without animations.
 */
@Composable
fun SmartEnvelopeSplitLightCard(
    totalSalary: Long = 55_000L,
    onPlayVoice: ((String, String) -> Unit)? = null,
    onOpenFullDetail: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    var needsPct by remember { mutableIntStateOf(70) }
    var commitmentsPct by remember { mutableIntStateOf(6) }
    var emergencyPct by remember { mutableIntStateOf(6) }
    var savingsPct by remember { mutableIntStateOf(18) }
    var showCustomizer by remember { mutableStateOf(false) }

    val needsAmount = (totalSalary * needsPct / 100)
    val commitmentsAmount = (totalSalary * commitmentsPct / 100)
    val emergencyAmount = (totalSalary * emergencyPct / 100)
    val savingsAmount = (totalSalary - needsAmount - commitmentsAmount - emergencyAmount).coerceAtLeast(0L)

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {

            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Smart Monthly Split",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = SlateTextPrimary
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(TealSurface)
                                .border(1.dp, TealBorder, RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(text = "4 Envelopes", fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = TealPrimary)
                        }
                    }
                    Text(
                        text = "حکمت عملی بجٹ تقسیم • برکت والا فارمولا",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = SlateTextSecondary
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    if (onPlayVoice != null) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(AmberLight)
                                .clickable {
                                    onPlayVoice(
                                        "Smart Monthly Split mein kul tankhwah ko chaar mukhtalif lifafon mein smart tareeqay se banta jata hai.",
                                        "Smart envelope split formula se tankhwah chaar lifafon mein taqseem hoti hai."
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.VolumeUp, contentDescription = "Voice Guide", tint = AmberDark, modifier = Modifier.size(16.dp))
                        }
                    }
                    IconButton(
                        onClick = { showCustomizer = !showCustomizer },
                        modifier = Modifier.size(34.dp).clip(CircleShape).background(if (showCustomizer) TealSurface else Color(0xFFF1F5F9))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = "Customize",
                            tint = if (showCustomizer) TealDarker else SlateTextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Income Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Brush.horizontalGradient(listOf(Color(0xFFF0FDFA), Color(0xFFE6FFFA))))
                    .border(1.dp, TealBorder, RoundedCornerShape(16.dp))
                    .padding(horizontal = 14.dp, vertical = 12.dp)
                    .testTag("salary_source_vault")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(modifier = Modifier.size(38.dp).clip(CircleShape).background(TealPrimary), contentAlignment = Alignment.Center) {
                            Icon(imageVector = Icons.Default.AccountBalanceWallet, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                        Column {
                            Text(text = "کل ماہانہ تنخواہ", fontSize = 11.sp, fontWeight = FontWeight.Medium, color = SlateTextSecondary)
                            Text(text = "PKR ${String.format("%,d", totalSalary)}", fontSize = 18.sp, fontWeight = FontWeight.Black, color = TealDarker)
                        }
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(EmeraldSurface)
                            .border(1.dp, EmeraldLight, RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(text = "100% مختص", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = EmeraldDark)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Segmented Progress Bar
            Row(modifier = Modifier.fillMaxWidth().height(10.dp).clip(RoundedCornerShape(5.dp)).background(Color(0xFFF1F5F9))) {
                Box(modifier = Modifier.fillMaxWidth(fraction = (needsPct / 100f).coerceIn(0.1f, 0.9f)).height(10.dp).background(TealPrimary))
                Box(modifier = Modifier.fillMaxWidth(fraction = (commitmentsPct / (100f - needsPct)).coerceIn(0.1f, 0.9f)).height(10.dp).background(AmberBrand))
                Box(modifier = Modifier.fillMaxWidth(fraction = (emergencyPct / (100f - needsPct - commitmentsPct)).coerceIn(0.1f, 0.9f)).height(10.dp).background(RoseAlert))
                Box(modifier = Modifier.fillMaxWidth().height(10.dp).background(EmeraldBrand))
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Legend
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                LightLegendBadge(color = TealPrimary, text = "$needsPct% ضروری")
                LightLegendBadge(color = AmberDark, text = "$commitmentsPct% کمیٹی")
                LightLegendBadge(color = RoseAlert, text = "$emergencyPct% ایمرجنسی")
                LightLegendBadge(color = EmeraldDark, text = "$savingsPct% بچت")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 2x2 Grid
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                LightEnvelopeGridTile(titleUrdu = "ضروری راشن و کرایہ", titleEnglish = "Needs ($needsPct%)", amount = needsAmount, icon = Icons.Default.Home, accentColor = TealPrimary, bgSurface = TealSurface, borderColor = TealBorder, modifier = Modifier.weight(1f))
                LightEnvelopeGridTile(titleUrdu = "کمیٹی و واجبات", titleEnglish = "Kameti ($commitmentsPct%)", amount = commitmentsAmount, icon = Icons.Default.People, accentColor = AmberDark, bgSurface = AmberSurface, borderColor = AmberLight, modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                LightEnvelopeGridTile(titleUrdu = "ہنگامی تحفظ", titleEnglish = "Emergency ($emergencyPct%)", amount = emergencyAmount, icon = Icons.Default.Shield, accentColor = RoseAlert, bgSurface = RoseSurface, borderColor = RoseBorder, modifier = Modifier.weight(1f))
                LightEnvelopeGridTile(titleUrdu = "دستیاب بچت", titleEnglish = "Savings ($savingsPct%)", amount = savingsAmount, icon = Icons.Default.Savings, accentColor = EmeraldDark, bgSurface = EmeraldSurface, borderColor = EmeraldLight, modifier = Modifier.weight(1f))
            }

            // Customizer
            if (showCustomizer) {
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(text = "اپنا تناسب تبدیل کریں:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SlateTextPrimary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(
                                modifier = Modifier.weight(1f).clip(RoundedCornerShape(10.dp))
                                    .background(if (needsPct == 70 && commitmentsPct == 6) TealPrimary else Color.White)
                                    .border(1.dp, if (needsPct == 70 && commitmentsPct == 6) TealDark else SlateBorder, RoundedCornerShape(10.dp))
                                    .clickable { needsPct = 70; commitmentsPct = 6; emergencyPct = 6; savingsPct = 18 }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "70/6/6/18", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (needsPct == 70 && commitmentsPct == 6) Color.White else SlateTextPrimary)
                            }
                            Box(
                                modifier = Modifier.weight(1f).clip(RoundedCornerShape(10.dp))
                                    .background(if (needsPct == 60 && savingsPct == 20) TealPrimary else Color.White)
                                    .border(1.dp, if (needsPct == 60 && savingsPct == 20) TealDark else SlateBorder, RoundedCornerShape(10.dp))
                                    .clickable { needsPct = 60; commitmentsPct = 10; emergencyPct = 10; savingsPct = 20 }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "60/10/10/20", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (needsPct == 60 && savingsPct == 20) Color.White else SlateTextPrimary)
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = "ضروری اخراجات", fontSize = 11.sp, color = SlateTextPrimary)
                                Text(text = "$needsPct%", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TealPrimary)
                            }
                            Slider(value = needsPct.toFloat(), onValueChange = { needsPct = it.roundToInt(); savingsPct = (100 - needsPct - commitmentsPct - emergencyPct).coerceAtLeast(0) }, valueRange = 40f..85f, colors = SliderDefaults.colors(thumbColor = TealPrimary, activeTrackColor = TealPrimary, inactiveTrackColor = Color(0xFFE2E8F0)), modifier = Modifier.height(24.dp))
                        }
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = "کمیٹی و واجبات", fontSize = 11.sp, color = SlateTextPrimary)
                                Text(text = "$commitmentsPct%", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AmberDark)
                            }
                            Slider(value = commitmentsPct.toFloat(), onValueChange = { commitmentsPct = it.roundToInt(); savingsPct = (100 - needsPct - commitmentsPct - emergencyPct).coerceAtLeast(0) }, valueRange = 0f..25f, colors = SliderDefaults.colors(thumbColor = AmberDark, activeTrackColor = AmberDark, inactiveTrackColor = Color(0xFFE2E8F0)), modifier = Modifier.height(24.dp))
                        }
                    }
                }
            }

            // Navigation link
            if (onOpenFullDetail != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
                        .background(TealSurface)
                        .border(1.dp, TealBorder, RoundedCornerShape(12.dp))
                        .clickable { onOpenFullDetail() }
                        .padding(horizontal = 14.dp, vertical = 9.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = TealDark, modifier = Modifier.size(15.dp))
                            Text(text = "لفافوں کا تفصیلی پلان و کھاتہ دیکھیں", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = TealDarker)
                        }
                        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null, tint = TealDark, modifier = Modifier.size(14.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun LightEnvelopeGridTile(
    titleUrdu: String,
    titleEnglish: String,
    amount: Long,
    icon: ImageVector,
    accentColor: Color,
    bgSurface: Color,
    borderColor: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.clip(RoundedCornerShape(16.dp)).background(bgSurface)
            .border(1.dp, borderColor, RoundedCornerShape(16.dp)).padding(12.dp)
    ) {
        Column {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(30.dp).clip(CircleShape).background(Color.White), contentAlignment = Alignment.Center) {
                    Icon(imageVector = icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(16.dp))
                }
                Box(modifier = Modifier.clip(RoundedCornerShape(6.dp)).background(Color.White).border(1.dp, borderColor, RoundedCornerShape(6.dp)).padding(horizontal = 5.dp, vertical = 2.dp)) {
                    Text(text = titleEnglish, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = accentColor)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = titleUrdu, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SlateTextPrimary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "PKR ${String.format("%,d", amount)}", fontSize = 14.5.sp, fontWeight = FontWeight.Black, color = accentColor)
        }
    }
}

@Composable
private fun LightLegendBadge(color: Color, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(color))
        Text(text = text, fontSize = 10.5.sp, fontWeight = FontWeight.Medium, color = SlateTextSecondary)
    }
}
