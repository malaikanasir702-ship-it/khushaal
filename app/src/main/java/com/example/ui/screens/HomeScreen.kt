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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.AppDestination
import com.example.model.AppTab
import com.example.model.CashFlowData
import com.example.model.ProsperityScore
import com.example.model.UserProfile
import com.example.ui.theme.AmberBrand
import com.example.ui.theme.AmberDark
import com.example.ui.theme.AmberDarker
import com.example.ui.theme.AmberLight
import com.example.ui.theme.AmberSurface
import com.example.ui.theme.EmeraldBrand
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.EmeraldSurface
import com.example.ui.theme.RoseAlert
import com.example.ui.theme.RoseBorder
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
import com.example.ui.theme.TealLight
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.TealSurface

@Composable
fun HomeScreen(
  userProfile: UserProfile,
  prosperityScore: ProsperityScore,
  cashFlow: CashFlowData,
  isChallengeJoined: Boolean,
  emergencyDeposited: Boolean,
  onNavigateToTab: (AppTab) -> Unit,
  onOpenFraudModal: () -> Unit,
  onDepositEmergency: () -> Unit,
  onToggleChallenge: () -> Unit,
  onCoachCall: () -> Unit,
  onPlayVoice: (String, String) -> Unit,
  modifier: Modifier = Modifier,
  onNavigateToDestination: (AppDestination) -> Unit = {},
) {
  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFF8FAFC))
      .padding(horizontal = 14.dp, vertical = 12.dp)
      .testTag("home_screen_scroll"),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // 1. Worker Profile Greeting Card
    item {
      Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("worker_greeting_card")
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .background(
              Brush.horizontalGradient(
                colors = listOf(TealDarker, TealDark)
              )
            )
            .padding(16.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(12.dp),
              modifier = Modifier.weight(1f)
            ) {
              // Avatar with verified online dot
              Box(modifier = Modifier.size(50.dp)) {
                AsyncImage(
                  model = userProfile.avatarUrl,
                  contentDescription = "User Avatar",
                  contentScale = ContentScale.Crop,
                  modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.White.copy(alpha = 0.85f), CircleShape)
                )
                Box(
                  modifier = Modifier
                    .size(14.dp)
                    .align(Alignment.BottomEnd)
                    .clip(CircleShape)
                    .background(EmeraldBrand)
                    .border(2.dp, TealDarker, CircleShape)
                )
              }

              Column {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                  Text(
                    text = userProfile.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White
                  )
                  Text(
                    text = userProfile.urduName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = TealBorder
                  )
                }

                Text(
                  text = "${userProfile.greetingEnglish} • ${userProfile.greetingUrdu}",
                  fontSize = 12.sp,
                  color = Color.White.copy(alpha = 0.85f)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(TealDarker.copy(alpha = 0.6f))
                    .border(1.dp, TealLight.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                  ) {
                    Icon(
                      imageVector = Icons.Default.CheckCircle,
                      contentDescription = "Verified",
                      tint = EmeraldBrand,
                      modifier = Modifier.size(11.dp)
                    )
                    Text(
                      text = "${userProfile.organization} • ${userProfile.verifiedText}",
                      fontSize = 10.sp,
                      color = Color.White,
                      fontWeight = FontWeight.Medium
                    )
                  }
                }
              }
            }

            // Amber Speaker Trigger
            Box(
              modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(AmberBrand)
                .clickable {
                  onPlayVoice(
                    "Assalam-o-Alaikum Ahmed Bhai! Aap ka prosperity score ${prosperityScore.score} hay. Is mahinay 3,000 rupay emergency fund mein lazmi shamil karein.",
                    "Assalam-o-Alaikum Ahmed Bhai! Aap ka prosperity score ${prosperityScore.score} hay."
                  )
                }
                .testTag("greeting_audio_btn"),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.VolumeUp,
                contentDescription = "Listen Greeting",
                tint = SlateDarkBg,
                modifier = Modifier.size(20.dp)
              )
            }
          }
        }
      }
    }

    // 2. Family Prosperity Score Highlight Card
    item {
      Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, TealBorder),
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onNavigateToTab(AppTab.PROSPERITY) }
          .testTag("prosperity_highlight_card")
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .background(
              Brush.linearGradient(
                colors = listOf(
                  EmeraldSurface.copy(alpha = 0.7f),
                  TealSurface.copy(alpha = 0.8f),
                  Color(0xFFE0F7FA).copy(alpha = 0.4f)
                )
              )
            )
            .padding(16.dp)
        ) {
          // Top row
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
          ) {
            Column {
              Text(
                text = "FAMILY PROSPERITY INDEX",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = TealPrimary,
                letterSpacing = 0.5.sp
              )
              Text(
                text = "خاندانی خوشحالی اسکور",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = SlateTextPrimary
              )
            }

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White.copy(alpha = 0.95f))
                .border(1.dp, TealBorder, RoundedCornerShape(16.dp))
                .padding(horizontal = 9.dp, vertical = 4.dp)
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
              ) {
                Text(
                  text = "6 Dimensions",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = TealPrimary
                )
                Icon(
                  imageVector = Icons.Default.ChevronRight,
                  contentDescription = "Details",
                  tint = TealPrimary,
                  modifier = Modifier.size(13.dp)
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Donut and text
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
          ) {
            // Donut Progress
            Box(
              modifier = Modifier.size(76.dp),
              contentAlignment = Alignment.Center
            ) {
              Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 8.dp.toPx()
                // Background circle
                drawCircle(
                  color = Color(0xFFE2E8F0),
                  style = Stroke(width = strokeWidth)
                )
                // Progress arc
                val sweep = (prosperityScore.score.toFloat() / prosperityScore.maxScore) * 360f
                drawArc(
                  color = TealPrimary,
                  startAngle = -90f,
                  sweepAngle = sweep,
                  useCenter = false,
                  style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
              }

              Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
              ) {
                Text(
                  text = "${prosperityScore.score}",
                  fontSize = 21.sp,
                  fontWeight = FontWeight.Black,
                  color = SlateTextPrimary,
                  lineHeight = 22.sp
                )
                Text(
                  text = "/ ${prosperityScore.maxScore}",
                  fontSize = 9.sp,
                  fontWeight = FontWeight.Bold,
                  color = SlateTextMuted
                )
              }
            }

            // Description
            Column(modifier = Modifier.weight(1f)) {
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(6.dp))
                  .background(EmeraldLight)
                  .padding(horizontal = 7.dp, vertical = 2.dp)
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                  Icon(
                    imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                    contentDescription = "Trending Up",
                    tint = EmeraldDark,
                    modifier = Modifier.size(12.dp)
                  )
                  Text(
                    text = prosperityScore.deltaMonth,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldDark
                  )
                }
              }

              Spacer(modifier = Modifier.height(4.dp))

              Text(
                text = "${prosperityScore.statusUrdu} (${prosperityScore.statusEnglish})",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TealDarker
              )

              Text(
                text = prosperityScore.description,
                fontSize = 11.sp,
                color = SlateTextSecondary,
                lineHeight = 15.sp
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Micro indicator bars
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .border(
                width = 1.dp,
                color = TealBorder.copy(alpha = 0.6f),
                shape = RoundedCornerShape(12.dp)
              )
              .background(Color.White.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
              .padding(horizontal = 10.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            MicroBar(
              label = "بچت",
              percent = prosperityScore.savingsPct,
              color = TealPrimary,
              modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(10.dp))
            MicroBar(
              label = "قرض کنٹرول",
              percent = prosperityScore.debtControlPct,
              color = EmeraldBrand,
              modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(10.dp))
            MicroBar(
              label = "تحفظ",
              percent = prosperityScore.safetyShieldPct,
              color = AmberBrand,
              modifier = Modifier.weight(1f)
            )
          }
        }
      }
    }

    // 3. This Month's Cash Flow Grid
    item {
      Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("cash_flow_section")
      ) {
        Column(
          modifier = Modifier.padding(16.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "This Month's Cash Flow",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = SlateTextPrimary
              )
              Text(
                text = cashFlow.urduSubtitle,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = SlateTextSecondary
              )
            }

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(TealSurface)
                .border(1.dp, TealBorder, RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
              Text(
                text = cashFlow.month,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TealPrimary
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // 2x2 Grid
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            // Income
            CashFlowTile(
              title = "آمدنی (Income)",
              amount = "PKR ${String.format("%,d", cashFlow.income)}",
              subtitle = cashFlow.incomeLabel,
              containerColor = TealSurface,
              borderColor = TealBorder,
              accentColor = EmeraldDark,
              icon = Icons.Default.ArrowDownward,
              onClick = { onNavigateToTab(AppTab.MONEY) },
              modifier = Modifier.weight(1f)
            )

            // Expenses
            CashFlowTile(
              title = "اخراجات (Expenses)",
              amount = "PKR ${String.format("%,d", cashFlow.expenses)}",
              subtitle = cashFlow.expensesLabel,
              containerColor = RoseSurface,
              borderColor = RoseBorder,
              accentColor = RoseAlert,
              icon = Icons.Default.ReceiptLong,
              onClick = { onNavigateToTab(AppTab.MONEY) },
              modifier = Modifier.weight(1f)
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            // Savings
            CashFlowTile(
              title = "بچت (Savings)",
              amount = "PKR ${String.format("%,d", cashFlow.savings)}",
              subtitle = cashFlow.savingsLabel,
              containerColor = AmberSurface,
              borderColor = AmberLight,
              accentColor = AmberDark,
              icon = Icons.Default.Savings,
              onClick = { onNavigateToTab(AppTab.MONEY) },
              modifier = Modifier.weight(1f)
            )

            // Available Buffer
            CashFlowTile(
              title = "باقی رقم (Available)",
              amount = "PKR ${String.format("%,d", cashFlow.available)}",
              subtitle = cashFlow.availableLabel,
              containerColor = EmeraldSurface,
              borderColor = EmeraldLight,
              accentColor = EmeraldDark,
              icon = Icons.Default.Wallet,
              onClick = { onNavigateToTab(AppTab.MONEY) },
              modifier = Modifier.weight(1f)
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(10.dp))
              .background(TealSurface)
              .clickable { onNavigateToDestination(AppDestination.TransactionHistory) }
              .padding(horizontal = 10.dp, vertical = 8.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text("مکمل روزنامچہ و خرچ کا کھاتہ (View All Transactions)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TealDarker)
              Text("کھولیں →", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TealPrimary)
            }
          }
        }
      }
    }

    // 4. High-Priority Worker Next Step (Emergency Fund Action)
    item {
      Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("emergency_next_step_card")
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .background(
              Brush.horizontalGradient(
                colors = listOf(AmberBrand, AmberDark)
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
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(12.dp))
                  .background(AmberDarker.copy(alpha = 0.5f))
                  .padding(horizontal = 8.dp, vertical = 3.dp)
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.Flag,
                    contentDescription = "Flag",
                    tint = Color.White,
                    modifier = Modifier.size(12.dp)
                  )
                  Text(
                    text = "Your Next Step • اگلا قدم",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                  )
                }
              }

              Box(
                modifier = Modifier
                  .size(32.dp)
                  .clip(CircleShape)
                  .background(Color.White)
                  .clickable {
                    onPlayVoice(
                      "Agla qadam: Teen hazaar rupay emergency fund mein jama karein taakay mushkil waqt mein kisi se sood par qarza na lena paray.",
                      "Agla qadam: Teen hazaar rupay emergency fund mein jama karein."
                    )
                  },
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.VolumeUp,
                  contentDescription = "Listen",
                  tint = AmberDark,
                  modifier = Modifier.size(16.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
              text = if (emergencyDeposited) "Rs. 3,000 Shield Active in Fund!" else "Add Rs. 3,000 to Emergency Fund",
              fontSize = 16.sp,
              fontWeight = FontWeight.Black,
              color = Color.White
            )

            Text(
              text = "ہنگامی فنڈ میں 3,000 روپے جمع کریں",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = AmberLight
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
              text = "This completes your 15-day family rainy-day shield (حفاظتی ڈھال).",
              fontSize = 12.sp,
              color = Color.White.copy(alpha = 0.95f),
              lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
              onClick = onDepositEmergency,
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.buttonColors(
                containerColor = if (emergencyDeposited) EmeraldDark else SlateDarkBg,
                contentColor = Color.White
              ),
              modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .testTag("emergency_fund_add_btn")
            ) {
              Icon(
                imageVector = if (emergencyDeposited) Icons.Default.CheckCircle else Icons.Default.AddCircle,
                contentDescription = null,
                tint = if (emergencyDeposited) Color.White else EmeraldBrand,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = if (emergencyDeposited) "Deposited to Shield ✓" else "Add to Fund Now / ابھی شامل کریں",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigateToDestination(AppDestination.EmergencyLocker) }
                .padding(vertical = 4.dp),
              horizontalArrangement = Arrangement.Center
            ) {
              Text(
                text = "ہنگامی والٹ و لاکر کا بیلنس دیکھیں →",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.9f)
              )
            }
          }
        }
      }
    }

    // 5. 4 Large Tactile Hub Buttons (Action Hub)
    item {
      Column(modifier = Modifier.fillMaxWidth()) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp, vertical = 4.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "ACTION HUB",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = SlateTextSecondary,
            letterSpacing = 0.5.sp
          )
          Text(
            text = "بنیادی سہولیات",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = SlateTextSecondary
          )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          // 1. My Money
          ActionHubTile(
            titleEnglish = "My Money",
            titleUrdu = "میرے پیسے",
            subtitle = "Envelope budget & salary",
            icon = Icons.Default.AccountBalanceWallet,
            iconBg = TealSurface,
            iconColor = TealPrimary,
            onClick = { onNavigateToTab(AppTab.MONEY) },
            testTag = "hub_my_money_btn",
            modifier = Modifier.weight(1f)
          )

          // 2. Goals & Score
          ActionHubTile(
            titleEnglish = "My Goals",
            titleUrdu = "میرے اہداف",
            subtitle = "Schooling & Kameti plans",
            icon = Icons.Default.TrackChanges,
            iconBg = AmberSurface,
            iconColor = AmberDark,
            onClick = { onNavigateToDestination(AppDestination.GoalsAndKameti) },
            testTag = "hub_my_goals_btn",
            modifier = Modifier.weight(1f)
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          // 3. Fraud Shield
          ActionHubTile(
            titleEnglish = "Fraud Shield",
            titleUrdu = "فراڈ سے بچیں",
            subtitle = "OTP & fake call training",
            icon = Icons.Default.Security,
            iconBg = RoseSurface,
            iconColor = RoseAlert,
            onClick = { onNavigateToDestination(AppDestination.FraudAcademy) },
            testTag = "hub_fraud_shield_btn",
            modifier = Modifier.weight(1f)
          )

          // 4. Earn More
          ActionHubTile(
            titleEnglish = "Earn More",
            titleUrdu = "اضافی آمدنی",
            subtitle = "Tailoring & side business",
            icon = Icons.Default.Spa,
            iconBg = Color(0xFFE0F7FA),
            iconColor = TealDark,
            onClick = { onNavigateToTab(AppTab.EARN_MORE) },
            testTag = "hub_earn_more_btn",
            modifier = Modifier.weight(1f)
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          // 5. Factory Payslip & Wage Slip
          ActionHubTile(
            titleEnglish = "Factory Payslip",
            titleUrdu = "تنخواہ سلپ",
            subtitle = "Wages, overtime & bonuses",
            icon = Icons.Default.Badge,
            iconBg = EmeraldSurface,
            iconColor = EmeraldDark,
            onClick = { onNavigateToDestination(AppDestination.FactorySalarySlip) },
            testTag = "hub_payslip_btn",
            modifier = Modifier.weight(1f)
          )

          // 6. Ration & Grocery Calculator
          ActionHubTile(
            titleEnglish = "Ration Planner",
            titleUrdu = "راشن کیلکولیٹر",
            subtitle = "Atta, ghee & mandi rates",
            icon = Icons.Default.Calculate,
            iconBg = AmberSurface,
            iconColor = AmberDark,
            onClick = { onNavigateToDestination(AppDestination.RationCalculator) },
            testTag = "hub_ration_calc_btn",
            modifier = Modifier.weight(1f)
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          // 7. Utility Bills & Meter Tracking
          ActionHubTile(
            titleEnglish = "Utility Bills",
            titleUrdu = "بجلی و گیس بل",
            subtitle = "KE, SSGC & due dates",
            icon = Icons.Default.ReceiptLong,
            iconBg = Color(0xFFE8F5E9),
            iconColor = Color(0xFF2E7D32),
            onClick = { onNavigateToDestination(AppDestination.UtilityBills) },
            testTag = "hub_utility_bills_btn",
            modifier = Modifier.weight(1f)
          )

          // 8. Debt Freedom & Qarz Mukti
          ActionHubTile(
            titleEnglish = "Debt Relief",
            titleUrdu = "قرض نجات پلان",
            subtitle = "Snowball & Shariah payoff",
            icon = Icons.Default.Savings,
            iconBg = Color(0xFFFFF3E0),
            iconColor = Color(0xFFE65100),
            onClick = { onNavigateToDestination(AppDestination.DebtSnowball) },
            testTag = "hub_debt_relief_btn",
            modifier = Modifier.weight(1f)
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          // 9. Budget Envelope Spring Split
          ActionHubTile(
            titleEnglish = "Envelope Split",
            titleUrdu = "بجٹ اسپلٹ اینیمیشن",
            subtitle = "Spring physics 4-envelope allocation",
            icon = Icons.Default.AccountBalanceWallet,
            iconBg = Color(0xFFE0F2FE),
            iconColor = Color(0xFF0284C7),
            onClick = { onNavigateToDestination(AppDestination.EnvelopeSplit) },
            testTag = "hub_envelope_split_btn",
            modifier = Modifier.weight(1f)
          )

          // 10. Digital Khata / Ledger
          ActionHubTile(
            titleEnglish = "History & Khata",
            titleUrdu = "روزنامچہ کھاتہ",
            subtitle = "Transaction history & records",
            icon = Icons.Default.ReceiptLong,
            iconBg = Color(0xFFF3E8FF),
            iconColor = Color(0xFF7E22CE),
            onClick = { onNavigateToDestination(AppDestination.TransactionHistory) },
            testTag = "hub_history_khata_btn",
            modifier = Modifier.weight(1f)
          )
        }
      }
    }

    // 6. Factory Coworker Challenge Card
    item {
      Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = TealSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, TealBorder),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f)
          ) {
            Box(
              modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(TealPrimary),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Groups,
                contentDescription = "Challenge",
                tint = Color.White,
                modifier = Modifier.size(22.dp)
              )
            }

            Column {
              Text(
                text = "Mill Challenge • 78 Coworkers Joined",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = TealPrimary
              )
              Text(
                text = "Save Rs. 1,000 extra this month",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = SlateTextPrimary
              )
              Text(
                text = "اضافی 1000 روپے بچت مقابلہ",
                fontSize = 10.sp,
                color = SlateTextSecondary
              )
            }
          }

          Button(
            onClick = onToggleChallenge,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = if (isChallengeJoined) EmeraldDark else TealDarker,
              contentColor = Color.White
            ),
            modifier = Modifier.height(36.dp)
          ) {
            Text(
              text = if (isChallengeJoined) "Joined ✓" else "Join",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }

    // 7. Dedicated Financial Coach Fatima Card
    item {
      Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Box(modifier = Modifier.size(44.dp)) {
              AsyncImage(
                model = "https://www.gstatic.com/labs-code/stitch/stitch-placeholder-300x300.svg",
                contentDescription = "Coach Fatima",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                  .size(44.dp)
                  .clip(CircleShape)
                  .border(1.dp, SlateBorder, CircleShape)
              )
              Box(
                modifier = Modifier
                  .size(12.dp)
                  .align(Alignment.BottomEnd)
                  .clip(CircleShape)
                  .background(EmeraldBrand)
                  .border(2.dp, Color.White, CircleShape)
              )
            }

            Column {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Text(
                  text = "Coach Fatima",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  color = SlateTextPrimary
                )
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(EmeraldLight)
                    .padding(horizontal = 5.dp, vertical = 1.dp)
                ) {
                  Text(
                    text = "آن لائن",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldDark
                  )
                }
              }

              Text(
                text = "مفت مالیاتی رہنمائی اور مشورہ",
                fontSize = 11.sp,
                color = SlateTextSecondary
              )
            }
          }

          Button(
            onClick = onCoachCall,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = TealPrimary,
              contentColor = Color.White
            ),
            modifier = Modifier.height(36.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Phone,
              contentDescription = "Call",
              tint = Color.White,
              modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "بات کریں",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
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
private fun MicroBar(
  label: String,
  percent: Float,
  color: Color,
  modifier: Modifier = Modifier,
) {
  Column(modifier = modifier) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text(
        text = label,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        color = SlateTextSecondary
      )
      Text(
        text = "${(percent * 100).toInt()}%",
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        color = SlateTextPrimary
      )
    }
    Spacer(modifier = Modifier.height(3.dp))
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(5.dp)
        .clip(RoundedCornerShape(3.dp))
        .background(Color(0xFFE2E8F0))
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth(fraction = percent)
          .height(5.dp)
          .clip(RoundedCornerShape(3.dp))
          .background(color)
      )
    }
  }
}

@Composable
private fun CashFlowTile(
  title: String,
  amount: String,
  subtitle: String,
  containerColor: Color,
  borderColor: Color,
  accentColor: Color,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(16.dp))
      .background(containerColor)
      .border(1.dp, borderColor, RoundedCornerShape(16.dp))
      .clickable { onClick() }
      .padding(12.dp)
  ) {
    Column {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = title,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = accentColor,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          modifier = Modifier.weight(1f)
        )
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = accentColor,
          modifier = Modifier.size(15.dp)
        )
      }

      Spacer(modifier = Modifier.height(5.dp))

      Text(
        text = amount,
        fontSize = 17.sp,
        fontWeight = FontWeight.Black,
        color = SlateTextPrimary
      )

      Text(
        text = subtitle,
        fontSize = 10.sp,
        color = accentColor,
        fontWeight = FontWeight.Medium,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
    }
  }
}

@Composable
private fun ActionHubTile(
  titleEnglish: String,
  titleUrdu: String,
  subtitle: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  iconBg: Color,
  iconColor: Color,
  onClick: () -> Unit,
  testTag: String,
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier
      .height(108.dp)
      .clip(RoundedCornerShape(18.dp))
      .background(Color.White)
      .border(1.dp, SlateBorder, RoundedCornerShape(18.dp))
      .clickable { onClick() }
      .testTag(testTag)
      .padding(12.dp)
  ) {
    Column(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Box(
        modifier = Modifier
          .size(36.dp)
          .clip(RoundedCornerShape(10.dp))
          .background(iconBg),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = titleEnglish,
          tint = iconColor,
          modifier = Modifier.size(18.dp)
        )
      }

      Column {
        Text(
          text = titleEnglish,
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          color = SlateTextPrimary
        )
        Text(
          text = titleUrdu,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = iconColor
        )
        Text(
          text = subtitle,
          fontSize = 10.sp,
          color = SlateTextSecondary,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }
    }
  }
}
