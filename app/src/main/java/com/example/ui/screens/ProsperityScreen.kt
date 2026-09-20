package com.example.ui.screens

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppDestination
import com.example.model.AppTab
import com.example.model.ProsperityPillar
import com.example.model.ProsperityScore
import com.example.model.StatusType
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
import com.example.ui.theme.SlateTextMuted
import com.example.ui.theme.SlateTextPrimary
import com.example.ui.theme.SlateTextSecondary
import com.example.ui.theme.TealBorder
import com.example.ui.theme.TealDark
import com.example.ui.theme.TealDarker
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.TealSurface

@Composable
fun ProsperityScreen(
  prosperityScore: ProsperityScore,
  pillars: List<ProsperityPillar>,
  onNavigateToTab: (AppTab) -> Unit,
  onOpenFraudModal: () -> Unit,
  onShowToast: (String) -> Unit,
  onPlayVoice: (String, String) -> Unit,
  modifier: Modifier = Modifier,
  onNavigateToDestination: (AppDestination) -> Unit = {},
) {
  var selectedRunwayOption by remember { mutableStateOf("1 Month ✓") }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFF8FAFC))
      .padding(horizontal = 14.dp, vertical = 12.dp)
      .testTag("prosperity_screen_scroll"),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // 1. Score Title Banner & Donut Gauge
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
            verticalAlignment = Alignment.Top
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "FAMILY PROSPERITY SCORE",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = TealPrimary,
                letterSpacing = 0.5.sp
              )
              Text(
                text = "خاندانی خوشحالی اسکور",
                fontSize = 17.sp,
                fontWeight = FontWeight.Black,
                color = SlateTextPrimary
              )
              Text(
                text = "Yeh koi credit score nahi hai, yeh aap ki family ki maliyati hifazat ka paimana hai.",
                fontSize = 11.sp,
                color = SlateTextSecondary,
                lineHeight = 15.sp,
                modifier = Modifier.padding(top = 2.dp)
              )
            }

            Box(
              modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(AmberLight)
                .clickable {
                  onPlayVoice(
                    "Family prosperity score aap ki family ki mali hifazat ka paimana ha. Yeh 6 bunyadi hisson par mushtamil ha: Budgeting, Bachat, Emergency readiness, Qarz control, Fraud hifazat, aur Aamdani.",
                    "Family prosperity score aap ki family ki mali hifazat ka paimana ha."
                  )
                },
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.VolumeUp,
                contentDescription = "Listen",
                tint = AmberDark,
                modifier = Modifier.size(18.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Gauge Banner Box
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(18.dp))
              .background(TealSurface)
              .border(1.dp, TealBorder, RoundedCornerShape(18.dp))
              .padding(14.dp)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
              // Circular Donut Gauge
              Box(
                modifier = Modifier.size(76.dp),
                contentAlignment = Alignment.Center
              ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                  val strokeWidth = 9.dp.toPx()
                  drawCircle(
                    color = Color(0xFFE2E8F0),
                    style = Stroke(width = strokeWidth)
                  )
                  val sweep = (prosperityScore.score.toFloat() / prosperityScore.maxScore) * 360f
                  drawArc(
                    color = TealPrimary,
                    startAngle = -90f,
                    sweepAngle = sweep,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                  )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                  Text(
                    text = "${prosperityScore.score}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = TealDarker,
                    lineHeight = 22.sp
                  )
                  Text(
                    text = "از 100",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = TealPrimary
                  )
                }
              }

              Column(modifier = Modifier.weight(1f)) {
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(TealBorder)
                    .padding(horizontal = 7.dp, vertical = 2.dp)
                ) {
                  Text(
                    text = "Resilient (41–70) • بہتری کا سفر",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = TealDarker
                  )
                }

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                  text = "+14 Points Boost in 45 Days",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  color = SlateTextPrimary
                )

                Text(
                  text = "Single earner status holding back score. Adding spouse income will jump score to 75+.",
                  fontSize = 11.sp,
                  color = SlateTextSecondary,
                  lineHeight = 15.sp
                )
              }
            }
          }
        }
      }
    }

    // 2. The 6 Core Pillars Breakdown
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
                text = "6 Core Pillars Breakdown",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = SlateTextPrimary
              )
              Text(
                text = "مالی حفاظت کے 6 بنیادی ستون",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = SlateTextSecondary
              )
            }

            Text(
              text = "Weighted 100%",
              fontSize = 11.sp,
              color = SlateTextMuted,
              fontWeight = FontWeight.Medium
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          // 6 pillars
          pillars.forEach { pillar ->
            PillarItem(
              pillar = pillar,
              onFixIncome = { onNavigateToTab(AppTab.EARN_MORE) }
            )
            Spacer(modifier = Modifier.height(8.dp))
          }
        }
      }
    }

    // 3. Interactive Health Check Question Simulation
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
            Text(
              text = "Health Check Verification",
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              color = SlateTextPrimary
            )
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(TealSurface)
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(
                text = "Live Sample",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = TealPrimary
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Question 1
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
                Text(
                  text = "Agar aglay maheenay tankhwah ruk jaye, toh ghar kitnay din chal sakta hai?",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = SlateTextPrimary,
                  lineHeight = 16.sp,
                  modifier = Modifier.weight(1f)
                )
                Icon(
                  imageVector = Icons.Default.VolumeUp,
                  contentDescription = "Listen",
                  tint = AmberDark,
                  modifier = Modifier
                    .size(18.dp)
                    .clickable {
                      onPlayVoice(
                        "Agar aglay maheenay tankhwah ruk jaye, toh ghar kitnay din chal sakta hai?",
                        "Agar aglay maheenay tankhwah ruk jaye, toh ghar kitnay din chal sakta hai?"
                      )
                    }
                )
              }

              Spacer(modifier = Modifier.height(10.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                RunwayOptionButton(
                  text = "< 15 Din",
                  isSelected = selectedRunwayOption == "< 15 Din",
                  onClick = {
                    selectedRunwayOption = "< 15 Din"
                    onShowToast("Selected: < 15 Din runway.")
                  },
                  modifier = Modifier.weight(1f)
                )

                RunwayOptionButton(
                  text = "1 Month ✓",
                  isSelected = selectedRunwayOption == "1 Month ✓",
                  onClick = {
                    selectedRunwayOption = "1 Month ✓"
                    onShowToast("Selected: 1 Month runway.")
                  },
                  modifier = Modifier.weight(1f)
                )

                RunwayOptionButton(
                  text = "3 Months+",
                  isSelected = selectedRunwayOption == "3 Months+",
                  onClick = {
                    selectedRunwayOption = "3 Months+"
                    onShowToast("Selected: 3 Months+ runway.")
                  },
                  modifier = Modifier.weight(1f)
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Question 2: OTP Question
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
                Text(
                  text = "Kya aap bank ya kisi anjan call par OTP / PIN share kartay hain?",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = SlateTextPrimary,
                  lineHeight = 16.sp,
                  modifier = Modifier.weight(1f)
                )
                Icon(
                  imageVector = Icons.Default.VolumeUp,
                  contentDescription = "Listen",
                  tint = AmberDark,
                  modifier = Modifier
                    .size(18.dp)
                    .clickable {
                      onPlayVoice(
                        "Kya aap bank ya kisi anjan call par OTP ya PIN share kartay hain?",
                        "Kya aap bank ya kisi anjan call par OTP ya PIN share kartay hain?"
                      )
                    }
                )
              }

              Spacer(modifier = Modifier.height(10.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                Box(
                  modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(EmeraldLight)
                    .border(1.dp, EmeraldBrand.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                    .clickable { onShowToast("Correct! Kabhi kisi se apna PIN ya OTP share mat karein.") }
                    .padding(vertical = 8.dp, horizontal = 6.dp),
                  contentAlignment = Alignment.Center
                ) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                  ) {
                    Icon(
                      imageVector = Icons.Default.Lock,
                      contentDescription = "Never",
                      tint = EmeraldDark,
                      modifier = Modifier.size(13.dp)
                    )
                    Text(
                      text = "Never / کبھی نہیں",
                      fontSize = 11.sp,
                      fontWeight = FontWeight.Bold,
                      color = EmeraldDark
                    )
                  }
                }

                Box(
                  modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
                    .border(1.dp, SlateBorder, RoundedCornerShape(10.dp))
                    .clickable { onOpenFraudModal() }
                    .padding(vertical = 8.dp, horizontal = 6.dp),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = "Agar zaroorat ho?",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = SlateTextSecondary
                  )
                }
              }
            }
          }
        }
      }
    }

    // Dedicated Management Tools
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Text(
            text = "Prosperity Toolkits • خوشحالی کے ٹولز",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = SlateTextPrimary
          )
          Spacer(modifier = Modifier.height(10.dp))

          // 1. Goals and Kameti
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(10.dp))
              .background(AmberSurface)
              .clickable { onNavigateToDestination(AppDestination.GoalsAndKameti) }
              .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text("اہداف و کمیٹی مینیجر (Goals & Kameti Hub)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AmberDark)
              Text("بچوں کی تعلیم، بائیک اور بی سی کی اقساط", fontSize = 10.sp, color = SlateTextSecondary)
            }
            Text("کھولیں →", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AmberDark)
          }

          Spacer(modifier = Modifier.height(8.dp))

          // 2. Emergency Locker
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(10.dp))
              .background(EmeraldSurface)
              .clickable { onNavigateToDestination(AppDestination.EmergencyLocker) }
              .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text("ہنگامی لاکر اور سیف والٹ (Emergency Locker)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = EmeraldDark)
              Text("15 دن سے 3 ماہ کا خاندانی رن وے لاک کریں", fontSize = 10.sp, color = SlateTextSecondary)
            }
            Text("کھولیں →", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EmeraldDark)
          }

          Spacer(modifier = Modifier.height(8.dp))

          // 3. Fraud Academy
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(10.dp))
              .background(RoseSurface)
              .clickable { onNavigateToDestination(AppDestination.FraudAcademy) }
              .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text("تحفظ اکیڈمی (Fraud Shield Academy)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = RoseAlert)
              Text("مشق کریں اور اپنا پروٹیکشن اسکور 100/100 کریں", fontSize = 10.sp, color = SlateTextSecondary)
            }
            Text("مشق کریں →", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = RoseAlert)
          }
        }
      }
    }

    // 4. CTA to Income Builder
    item {
      Button(
        onClick = { onNavigateToTab(AppTab.EARN_MORE) },
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = TealDarker,
          contentColor = Color.White
        ),
        modifier = Modifier
          .fillMaxWidth()
          .height(50.dp)
          .testTag("open_income_builder_cta_btn")
      ) {
        Icon(
          imageVector = Icons.Default.RocketLaunch,
          contentDescription = null,
          tint = AmberBrand,
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Open Income Builder • آمدنی بڑھانے کا پلان",
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold
        )
      }
    }

    item {
      Spacer(modifier = Modifier.height(8.dp))
    }
  }
}

@Composable
private fun PillarItem(
  pillar: ProsperityPillar,
  onFixIncome: () -> Unit,
) {
  val (cardBg, barColor, badgeColor, badgeText) = when (pillar.statusColorType) {
    StatusType.SUCCESS -> Quad(Color(0xFFF8FAFC), EmeraldBrand, EmeraldDark, TealPrimary)
    StatusType.WARNING -> Quad(Color(0xFFF8FAFC), AmberBrand, AmberDark, AmberDark)
    StatusType.URGENT -> Quad(RoseSurface.copy(alpha = 0.6f), RoseAlert, RoseDark, RoseDark)
  }

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(14.dp))
      .background(cardBg)
      .border(1.dp, if (pillar.statusColorType == StatusType.URGENT) RoseBorder else SlateBorder, RoundedCornerShape(14.dp))
      .padding(12.dp)
  ) {
    Column {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "${pillar.id}. ${pillar.titleEnglish} • ${pillar.weightPercent}%",
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = if (pillar.statusColorType == StatusType.URGENT) RoseDark else SlateTextPrimary
        )

        Text(
          text = "${pillar.currentScore} / ${pillar.maxScore}",
          fontSize = 12.sp,
          fontWeight = FontWeight.Black,
          color = badgeColor
        )
      }

      Spacer(modifier = Modifier.height(6.dp))

      // Progress bar
      val ratio = (pillar.currentScore.toFloat() / pillar.maxScore).coerceIn(0f, 1f)
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(6.dp)
          .clip(RoundedCornerShape(3.dp))
          .background(Color(0xFFE2E8F0))
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth(fraction = ratio)
            .height(6.dp)
            .clip(RoundedCornerShape(3.dp))
            .background(barColor)
        )
      }

      Spacer(modifier = Modifier.height(6.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = pillar.note,
          fontSize = 10.sp,
          color = if (pillar.statusColorType == StatusType.URGENT) RoseDark else SlateTextSecondary,
          modifier = Modifier.weight(1f)
        )

        if (pillar.id == 6) {
          Text(
            text = "Fix with Income Builder →",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = TealPrimary,
            modifier = Modifier.clickable { onFixIncome() }
          )
        } else {
          Text(
            text = pillar.statusText,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = badgeColor
          )
        }
      }
    }
  }
}

private data class Quad<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

@Composable
private fun RunwayOptionButton(
  text: String,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(8.dp))
      .background(if (isSelected) TealPrimary else Color.White)
      .border(1.dp, if (isSelected) TealPrimary else SlateBorder, RoundedCornerShape(8.dp))
      .clickable { onClick() }
      .padding(vertical = 7.dp, horizontal = 4.dp),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = text,
      fontSize = 11.sp,
      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
      color = if (isSelected) Color.White else SlateTextPrimary
    )
  }
}
