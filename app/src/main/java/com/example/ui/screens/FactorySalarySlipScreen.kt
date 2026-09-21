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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FactoryPayslip
import com.example.ui.theme.AmberBrand
import com.example.ui.theme.AmberDark
import com.example.ui.theme.AmberLight
import com.example.ui.theme.EmeraldBrand
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.EmeraldSurface
import com.example.ui.theme.RoseAlert
import com.example.ui.theme.RoseSurface
import com.example.ui.theme.SlateBorder
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
fun FactorySalarySlipScreen(
  payslip: FactoryPayslip,
  onBack: () -> Unit,
  onShowToast: (String) -> Unit,
  onPlayVoice: (String, String) -> Unit,
  modifier: Modifier = Modifier,
  factoryName: String = "Factory",
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFF8FAFC))
  ) {
    // Top Bar
    Surface(
      color = Color.White,
      shadowElevation = 2.dp,
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 8.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = onBack,
          modifier = Modifier.testTag("payslip_back_btn")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = SlateTextPrimary
          )
        }

        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "Factory Wage Slip & Overtime",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = SlateTextPrimary
          )
          Text(
            text = "فیکٹری تنخواہ سلپ، اوور ٹائم و الاؤنسز",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = TealPrimary
          )
        }

        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(AmberLight)
            .clickable {
              onPlayVoice(
                "$factoryName ki janib se aap ki kul aamdani ${payslip.totalGrossWage.let { if (it > 0) "%,d".format(it) else "55,000" }} rupay bani ha. EOBI aur peshgi katoti ke baad ${payslip.netTakeHome.let { if (it > 0) "%,d".format(it) else "52,750" }} rupay JazzCash mein transfer ho chukay hain.",
                "Aap ki factory payslip aur overtime tafseelat yahan mojood hain."
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
    }

    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 14.dp, vertical = 12.dp)
        .testTag("payslip_scroll_list"),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // 1. Official Header & Take-Home Hero Card
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
                  colors = listOf(TealDarker, TealDark, Color(0xFF0D9488))
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
                  horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.Badge,
                    contentDescription = null,
                    tint = AmberBrand,
                    modifier = Modifier.size(16.dp)
                  )
                  Text(
                    text = factoryName.uppercase(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = AmberBrand,
                    letterSpacing = 0.5.sp
                  )
                }

                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.White.copy(alpha = 0.2f))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                  Text(
                    text = payslip.month,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                  )
                }
              }

              Spacer(modifier = Modifier.height(14.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
              ) {
                Column {
                  Text(
                    text = "خالص ماہانہ تنخواہ (Net Take-Home)",
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.9f)
                  )
                  Text(
                    text = "PKR ${String.format("%,d", payslip.netTakeHome)}",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                  )
                }

                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(EmeraldBrand)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                  ) {
                    Icon(
                      imageVector = Icons.Default.CheckCircle,
                      contentDescription = null,
                      tint = TealDarker,
                      modifier = Modifier.size(12.dp)
                    )
                    Text(
                      text = "منتقل شدہ",
                      fontSize = 10.sp,
                      fontWeight = FontWeight.Bold,
                      color = TealDarker
                    )
                  }
                }
              }

              Spacer(modifier = Modifier.height(12.dp))

              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(10.dp))
                  .background(Color.Black.copy(alpha = 0.2f))
                  .padding(horizontal = 10.dp, vertical = 6.dp)
              ) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = "${payslip.employeeName} • ID: ${payslip.employeeId}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                  )
                  Text(
                    text = payslip.department,
                    fontSize = 10.sp,
                    color = Color.White.copy(alpha = 0.85f)
                  )
                }
              }
            }
          }
        }
      }

      // 2. Attendance & Duty Meter
      item {
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "حاضری و ڈیوٹی ریکارڈ (Attendance & Shifts)",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = SlateTextPrimary
              )
              Icon(
                imageVector = Icons.Default.Schedule,
                contentDescription = null,
                tint = TealPrimary,
                modifier = Modifier.size(18.dp)
              )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              DutyPill(
                title = "کل حاضری",
                subtitle = "Present Days",
                value = "${payslip.daysPresent} دن",
                containerColor = EmeraldSurface,
                accentColor = EmeraldDark,
                modifier = Modifier.weight(1f)
              )
              DutyPill(
                title = "غیر حاضری",
                subtitle = "Absents",
                value = "${payslip.daysAbsent} دن",
                containerColor = if (payslip.daysAbsent == 0) TealSurface else RoseSurface,
                accentColor = if (payslip.daysAbsent == 0) TealPrimary else RoseAlert,
                modifier = Modifier.weight(1f)
              )
              DutyPill(
                title = "اوور ٹائم",
                subtitle = "Overtime Hours",
                value = "${payslip.overtimeHours} گھنٹے",
                containerColor = AmberLight,
                accentColor = AmberDark,
                modifier = Modifier.weight(1f)
              )
            }
          }
        }
      }

      // 3. Detailed Earnings Breakdown
      item {
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Text(
              text = "آمدنی کی تفصیل (Gross Earnings Breakdown)",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = SlateTextPrimary
            )
            Spacer(modifier = Modifier.height(10.dp))

            WageRow(labelUrdu = "بنیادی تنخواہ", labelEnglish = "Base Wage", amount = payslip.baseWage)
            WageRow(labelUrdu = "اوور ٹائم معاوضہ", labelEnglish = "Overtime Wages (18.5 hrs)", amount = payslip.overtimePay)
            WageRow(labelUrdu = "پابندی حاضری بونس", labelEnglish = "Full Attendance Bonus", amount = payslip.attendanceBonus)
            WageRow(labelUrdu = "پروڈکشن ہدف انعام", labelEnglish = "Weaving Target Bonus", amount = payslip.productionBonus)

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = SlateBorder)

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "مجموعی آمدنی (Total Gross Earnings)",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TealDarker
              )
              Text(
                text = "PKR ${String.format("%,d", payslip.totalGrossWage)}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                color = TealDarker
              )
            }
          }
        }
      }

      // 4. Statutory & Meal Deductions
      item {
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "قانونی و فیکٹری کٹوتیاں (Deductions)",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = SlateTextPrimary
              )
              Text(
                text = "- PKR ${String.format("%,d", payslip.totalDeductions)}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = RoseAlert
              )
            }

            Spacer(modifier = Modifier.height(10.dp))

            DeductionRow(
              titleUrdu = "EOBI پینشن فنڈ",
              titleEnglish = "Employees Old-Age Benefits",
              amount = payslip.eobiDeduction,
              note = "حکومت پاکستان کی رجسٹرڈ پینشن شراکت"
            )
            DeductionRow(
              titleUrdu = "میس کینٹین و پیشگی",
              titleEnglish = "Factory Mess / Canteen Advance",
              amount = payslip.messAdvanceDeduction,
              note = "ماہانہ کینٹین کوپن کی ایڈجسٹمنٹ"
            )
            DeductionRow(
              titleUrdu = "ورکرز ویلفیئر فنڈ",
              titleEnglish = "Mill Workers Union & Welfare",
              amount = payslip.unionFundDeduction,
              note = "طبی و ویلفیئر تعاون"
            )
          }
        }
      }

      // 5. Direct Credit Verification
      item {
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = EmeraldSurface),
          border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldLight),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            Box(
              modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(EmeraldDark),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Payments,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(22.dp)
              )
            }

            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "پیمنٹ اسٹیٹس: ${payslip.paymentStatus}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = EmeraldDark
              )
              Text(
                text = "ٹرانسفر تاریخ: ${payslip.creditedDate} بذریعہ ${payslip.disbursementAccount}",
                fontSize = 10.sp,
                color = SlateTextSecondary
              )
            }
          }
        }
      }

      // Action Buttons
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Button(
            onClick = { onShowToast("Official payslip PDF saved to device downloads.") },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
            modifier = Modifier
              .weight(1f)
              .height(44.dp)
              .testTag("download_payslip_btn")
          ) {
            Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("ڈاؤن لوڈ سلپ (PDF)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }

          Button(
            onClick = { onShowToast("$factoryName HR Wage Helpdesk — براہ کرم HR آفس سے رابطہ کریں") },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SlateDarkBg),
            modifier = Modifier
              .weight(1f)
              .height(44.dp)
              .testTag("hr_query_btn")
          ) {
            Icon(Icons.Default.HelpOutline, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("کوئی سوال؟ HR ہیلپ", fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }
        }
      }

      item {
        Spacer(modifier = Modifier.height(16.dp))
      }
    }
  }
}

@Composable
private fun DutyPill(
  title: String,
  subtitle: String,
  value: String,
  containerColor: Color,
  accentColor: Color,
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(12.dp))
      .background(containerColor)
      .padding(horizontal = 8.dp, vertical = 10.dp),
    contentAlignment = Alignment.Center
  ) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Text(text = value, fontSize = 15.sp, fontWeight = FontWeight.Black, color = accentColor)
      Text(text = title, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = SlateTextPrimary)
      Text(text = subtitle, fontSize = 8.sp, color = SlateTextSecondary)
    }
  }
}

@Composable
private fun WageRow(
  labelUrdu: String,
  labelEnglish: String,
  amount: Long,
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column {
      Text(text = labelUrdu, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SlateTextPrimary)
      Text(text = labelEnglish, fontSize = 10.sp, color = SlateTextSecondary)
    }
    Text(
      text = "PKR ${String.format("%,d", amount)}",
      fontSize = 12.sp,
      fontWeight = FontWeight.Bold,
      color = SlateTextPrimary
    )
  }
}

@Composable
private fun DeductionRow(
  titleUrdu: String,
  titleEnglish: String,
  amount: Long,
  note: String,
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 5.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column(modifier = Modifier.weight(1f)) {
      Text(text = titleUrdu, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SlateTextPrimary)
      Text(text = "$titleEnglish • $note", fontSize = 9.sp, color = SlateTextSecondary)
    }
    Text(
      text = "- PKR ${String.format("%,d", amount)}",
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold,
      color = RoseAlert
    )
  }
}
