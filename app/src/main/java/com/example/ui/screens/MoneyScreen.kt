package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppDestination
import com.example.model.CashFlowData
import com.example.model.Envelope
import com.example.ui.components.SmartEnvelopeSplitLightCard
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
import com.example.ui.theme.SlateDarkBg
import com.example.ui.theme.SlateTextMuted
import com.example.ui.theme.SlateTextPrimary
import com.example.ui.theme.SlateTextSecondary
import com.example.ui.theme.TealBorder
import com.example.ui.theme.TealDark
import com.example.ui.theme.TealDarker
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.TealSurface

@Composable
fun MoneyScreen(
  cashFlow: CashFlowData,
  envelopes: List<Envelope>,
  wageAdvanceRequested: Boolean,
  onLogExpense: (String, Long) -> Unit,
  onVoiceLogExpense: () -> Unit,
  onRequestWageAdvance: () -> Unit,
  onPlayVoice: (String, String) -> Unit,
  modifier: Modifier = Modifier,
  onNavigateToDestination: (AppDestination) -> Unit = {},
  factoryName: String = "Factory",
) {
  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFF8FAFC))
      .padding(horizontal = 14.dp, vertical = 12.dp)
      .testTag("money_screen_scroll"),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // 1. Salary Month Picker Card
    item {
      Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(36.dp)
                  .clip(RoundedCornerShape(10.dp))
                  .background(TealSurface),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.CalendarMonth,
                  contentDescription = "Salary Month",
                  tint = TealPrimary,
                  modifier = Modifier.size(18.dp)
                )
              }

              Column {
                Text(
                  text = "September 2026 • ستمبر",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  color = SlateTextPrimary
                )
                Text(
                  text = "$factoryName Net Salary Credited",
                  fontSize = 10.sp,
                  color = SlateTextSecondary
                )
              }
            }

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(EmeraldLight)
                .border(1.dp, EmeraldBrand.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
              Text(
                text = "تنخواہ جمع شدہ ✓",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = EmeraldDark
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(1.dp)
              .background(SlateBorderSubtle)
          )
          Spacer(modifier = Modifier.height(12.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
          ) {
            Column {
              Text(
                text = "صاف تنخواہ (Net PKR)",
                fontSize = 11.sp,
                color = SlateTextSecondary,
                fontWeight = FontWeight.Medium
              )
              Text(
                text = "Rs. ${String.format("%,d", cashFlow.income)}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = SlateTextPrimary
              )
            }

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(AmberLight)
                .border(1.dp, AmberBrand.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                .clickable {
                  onPlayVoice(
                    "Aap ki kul tankhwah 55,000 rupay ha. Smart split ke tehat 70 feesad zaroori ikhrajat, 6 feesad committee, 6 feesad emergency aur 18 feesad bachat mein taqseem kia gaya ha.",
                    "Aap ki kul tankhwah 55,000 rupay ha. Smart split formula lagoo hai."
                  )
                }
                .padding(horizontal = 9.dp, vertical = 6.dp)
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.VolumeUp,
                  contentDescription = "Explain",
                  tint = AmberDark,
                  modifier = Modifier.size(13.dp)
                )
                Text(
                  text = "پیسہ کہاں تقسیم ہو رہا ہے؟",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = AmberDark
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(10.dp))
                .background(TealSurface)
                .border(1.dp, TealBorder, RoundedCornerShape(10.dp))
                .clickable { onNavigateToDestination(AppDestination.FactorySalarySlip) }
                .padding(vertical = 8.dp, horizontal = 10.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "فیکٹری تنخواہ سلپ دیکھیں →",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TealDarker
              )
            }

            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(10.dp))
                .background(AmberLight)
                .border(1.dp, AmberBrand.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                .clickable { onNavigateToDestination(AppDestination.RationCalculator) }
                .padding(vertical = 8.dp, horizontal = 10.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "سستا راشن کیلکولیٹر →",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = AmberDark
              )
            }
          }
        }
      }
    }

    // 2. Smart Monthly Split - Clean Light Themed UI (4 Envelopes)
    item {
      SmartEnvelopeSplitLightCard(
        totalSalary = cashFlow.income,
        onPlayVoice = onPlayVoice,
        onOpenFullDetail = { onNavigateToDestination(AppDestination.EnvelopeSplit) }
      )
    }

    // 2b. Detailed Itemized Envelope Sub-Items Card
    item {
      Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "لفافوں کی ذیلی تفصیلات",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = SlateTextPrimary
              )
              Text(
                text = "Itemized Breakdown & Quick Shortcuts",
                fontSize = 10.sp,
                color = SlateTextSecondary
              )
            }

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(TealSurface)
                .clickable { onNavigateToDestination(AppDestination.EnvelopeSplit) }
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Text(
                text = "پورا پلان کھولیں →",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = TealPrimary
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Envelope 1: Needs
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(14.dp))
              .background(Color(0xFFF8FAFC))
              .border(1.dp, SlateBorder, RoundedCornerShape(14.dp))
              .padding(12.dp)
          ) {
            Column {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                  Box(
                    modifier = Modifier
                      .size(28.dp)
                      .clip(RoundedCornerShape(8.dp))
                      .background(TealSurface),
                    contentAlignment = Alignment.Center
                  ) {
                    Icon(
                      imageVector = Icons.Default.Home,
                      contentDescription = "Needs",
                      tint = TealPrimary,
                      modifier = Modifier.size(15.dp)
                    )
                  }
                  Column {
                    Text(
                      text = "1. ضروری اخراجات (Household Needs)",
                      fontSize = 12.sp,
                      fontWeight = FontWeight.Bold,
                      color = SlateTextPrimary
                    )
                    Text(
                      text = "Fixed family requirements (70%)",
                      fontSize = 10.sp,
                      color = SlateTextSecondary
                    )
                  }
                }

                Text(
                  text = "Rs. 38,500",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Black,
                  color = SlateTextPrimary
                )
              }

              Spacer(modifier = Modifier.height(8.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                SubExpenseBox(label = "آٹا راشن", amount = "Rs 20,000", modifier = Modifier.weight(1f))
                SubExpenseBox(label = "کرایہ و بل", amount = "Rs 14,000", modifier = Modifier.weight(1f))
                SubExpenseBox(label = "اسکول فیس", amount = "Rs 4,500", modifier = Modifier.weight(1f))
              }
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          // Envelope 2: Commitments
          SingleEnvelopeCard(
            number = "2",
            title = "کمیٹی و واجبات (Commitments)",
            subtitle = "Local committee / BC (6%) • تفصیلات کھولیں →",
            tag = "تاریخ تک ادا کریں 25",
            amount = "Rs. 3,500",
            icon = Icons.Default.People,
            iconBg = AmberSurface,
            iconColor = AmberDark,
            containerBg = Color(0xFFF8FAFC),
            borderColor = AmberLight,
            onClick = { onNavigateToDestination(AppDestination.GoalsAndKameti) }
          )

          Spacer(modifier = Modifier.height(8.dp))

          // Envelope 3: Emergency Fund
          SingleEnvelopeCard(
            number = "3",
            title = "ہنگامی تحفظ (Emergency Fund)",
            subtitle = "Safe locked JazzCash / Bank (6%) • لاکر کھولیں →",
            tag = "محفوظ رقم",
            amount = "Rs. 3,000",
            icon = Icons.Default.Shield,
            iconBg = RoseLight,
            iconColor = RoseDark,
            containerBg = RoseSurface.copy(alpha = 0.5f),
            borderColor = RoseBorder,
            onClick = { onNavigateToDestination(AppDestination.EmergencyLocker) }
          )

          Spacer(modifier = Modifier.height(8.dp))

          // Envelope 4: Free Savings
          SingleEnvelopeCard(
            number = "4",
            title = "دستیاب بچت (Savings & Buffer)",
            subtitle = "Remaining for goal / growth (18%)",
            tag = "مستقبل کے لیے",
            amount = "Rs. 10,000",
            icon = Icons.Default.Savings,
            iconBg = EmeraldLight,
            iconColor = EmeraldDark,
            containerBg = EmeraldSurface.copy(alpha = 0.5f),
            borderColor = EmeraldLight
          )
        }
      }
    }

    // 3. Fast 1-Tap Daily Outflow Tracker
    item {
      Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "روزانہ خرچہ درج کریں",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = SlateTextPrimary
              )
              Text(
                text = "Fast Daily Outflow Tracker",
                fontSize = 11.sp,
                color = SlateTextSecondary
              )
            }

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(TealSurface)
                .border(1.dp, TealBorder, RoundedCornerShape(12.dp))
                .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
              Text(
                text = "1-Tap Log",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = TealPrimary
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // 4 quick buttons
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            QuickLogButton(
              emoji = "🌾",
              title = "Atta / Ration",
              urdu = "آٹا راشن",
              onClick = { onLogExpense("Atta / Ration", 500) },
              modifier = Modifier.weight(1f)
            )

            QuickLogButton(
              emoji = "⚡",
              title = "Bijli / Gas Bill",
              urdu = "بل ادا کیا",
              onClick = { onLogExpense("Bijli / Gas Bill", 1200) },
              modifier = Modifier.weight(1f)
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            QuickLogButton(
              emoji = "💊",
              title = "Dawa / Medical",
              urdu = "دوائی خرچ",
              onClick = { onLogExpense("Dawa / Medical", 350) },
              modifier = Modifier.weight(1f)
            )

            QuickLogButton(
              emoji = "🎓",
              title = "Bachon ki Fees",
              urdu = "بچوں کی فیس",
              onClick = { onLogExpense("Bachon ki Fees", 800) },
              modifier = Modifier.weight(1f)
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Voice Log button
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(14.dp))
              .background(TealSurface)
              .border(1.dp, TealBorder, RoundedCornerShape(14.dp))
              .clickable { onVoiceLogExpense() }
              .testTag("voice_log_btn")
              .padding(12.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
              ) {
                Box(
                  modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(TealPrimary),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Voice Log",
                    tint = Color.White,
                    modifier = Modifier.size(17.dp)
                  )
                }

                Column {
                  Text(
                    text = "بول کر خرچہ لکھیں (Voice Log)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TealDarker
                  )
                  Text(
                    text = "\"آج 500 روپے کا دودھ لیا\"",
                    fontSize = 10.sp,
                    color = TealPrimary
                  )
                }
              }

              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .background(TealPrimary)
                  .padding(horizontal = 8.dp, vertical = 5.dp)
              ) {
                Text(
                  text = "Tap to Speak",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Button(
            onClick = { onNavigateToDestination(AppDestination.TransactionHistory) },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SlateDarkBg),
            modifier = Modifier.fillMaxWidth().height(42.dp)
          ) {
            Text("مکمل روزنامچہ و حساب کتاب کھولیں (View Full History)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }
        }
      }
    }

    // 4. Factory Wage Protector
    item {
      Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .background(
              Brush.linearGradient(
                colors = listOf(SlateDarkBg, TealDarker)
              )
            )
            .padding(16.dp)
        ) {
          Column {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.Security,
                  contentDescription = "Protector",
                  tint = EmeraldBrand,
                  modifier = Modifier.size(19.dp)
                )
                Text(
                  text = "Factory Wage Protector",
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White
                )
              }

              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(10.dp))
                  .background(EmeraldBrand.copy(alpha = 0.2f))
                  .border(1.dp, EmeraldBrand.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                  .padding(horizontal = 7.dp, vertical = 2.dp)
              ) {
                Text(
                  text = "0% Markup",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  color = EmeraldLight
                )
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
              text = "سود اور مہنگے قرضے سے بچیں! $factoryName HR کے ذریعے Rs. 6,000 ایمرجنسی ایڈوانس بغیر سود دستیاب ہے۔",
              fontSize = 12.sp,
              lineHeight = 18.sp,
              color = Color.White.copy(alpha = 0.85f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
              onClick = onRequestWageAdvance,
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.buttonColors(
                containerColor = if (wageAdvanceRequested) TealPrimary else EmeraldDark,
                contentColor = Color.White
              ),
              modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .testTag("wage_advance_btn")
            ) {
              Icon(
                imageVector = if (wageAdvanceRequested) Icons.Default.CheckCircle else Icons.Default.Security,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = if (wageAdvanceRequested) "Advance Requested (Submitted to HR) ✓" else "(Get Rs. 6,000) ایمرجنسی ایڈوانس حاصل کریں",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(8.dp))
    }
  }
}

@Composable
private fun LegendItem(color: Color, text: String) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(4.dp)
  ) {
    Box(
      modifier = Modifier
        .size(8.dp)
        .clip(CircleShape)
        .background(color)
    )
    Text(
      text = text,
      fontSize = 10.sp,
      fontWeight = FontWeight.Bold,
      color = SlateTextSecondary
    )
  }
}

@Composable
private fun SubExpenseBox(
  label: String,
  amount: String,
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(8.dp))
      .background(Color.White)
      .border(1.dp, SlateBorder, RoundedCornerShape(8.dp))
      .padding(vertical = 6.dp, horizontal = 4.dp),
    contentAlignment = Alignment.Center
  ) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Text(
        text = label,
        fontSize = 10.sp,
        color = SlateTextSecondary
      )
      Text(
        text = amount,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = SlateTextPrimary
      )
    }
  }
}

@Composable
private fun SingleEnvelopeCard(
  number: String,
  title: String,
  subtitle: String,
  tag: String,
  amount: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  iconBg: Color,
  iconColor: Color,
  containerBg: Color,
  borderColor: Color,
  onClick: (() -> Unit)? = null,
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(14.dp))
      .background(containerBg)
      .border(1.dp, borderColor, RoundedCornerShape(14.dp))
      .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
      .padding(12.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Box(
          modifier = Modifier
            .size(28.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(iconBg),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = icon,
            contentDescription = title,
            tint = iconColor,
            modifier = Modifier.size(15.dp)
          )
        }

        Column {
          Text(
            text = "$number. $title",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = SlateTextPrimary
          )
          Text(
            text = subtitle,
            fontSize = 10.sp,
            color = SlateTextSecondary
          )
        }
      }

      Column(horizontalAlignment = Alignment.End) {
        Text(
          text = amount,
          fontSize = 13.sp,
          fontWeight = FontWeight.Black,
          color = SlateTextPrimary
        )
        Text(
          text = tag,
          fontSize = 9.sp,
          fontWeight = FontWeight.SemiBold,
          color = iconColor
        )
      }
    }
  }
}

@Composable
private fun QuickLogButton(
  emoji: String,
  title: String,
  urdu: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(12.dp))
      .background(Color(0xFFF8FAFC))
      .border(1.dp, SlateBorder, RoundedCornerShape(12.dp))
      .clickable { onClick() }
      .padding(10.dp)
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Text(
        text = emoji,
        fontSize = 18.sp
      )
      Column {
        Text(
          text = title,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = SlateTextPrimary,
          lineHeight = 13.sp
        )
        Text(
          text = urdu,
          fontSize = 10.sp,
          color = SlateTextSecondary
        )
      }
    }
  }
}
