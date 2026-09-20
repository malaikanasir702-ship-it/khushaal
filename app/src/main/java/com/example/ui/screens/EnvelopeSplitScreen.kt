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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VolumeUp
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppDestination
import com.example.model.Envelope
import com.example.ui.components.EnvelopeSpringAllocationView
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
import com.example.ui.theme.SlateDarkBg
import com.example.ui.theme.SlateTextMuted
import com.example.ui.theme.SlateTextPrimary
import com.example.ui.theme.SlateTextSecondary
import com.example.ui.theme.TealDarker
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.TealSurface

@Composable
fun EnvelopeSplitScreen(
  envelopes: List<Envelope>,
  totalSalary: Long,
  onBack: () -> Unit,
  onNavigateToDestination: (AppDestination) -> Unit,
  onShowToast: (String) -> Unit,
  onPlayVoice: (String, String) -> Unit,
  modifier: Modifier = Modifier,
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
          modifier = Modifier.testTag("envelope_split_back_btn")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = SlateTextPrimary
          )
        }

        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "Budget Envelope Splitting",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = SlateTextPrimary
          )
          Text(
            text = "چار لفافوں کی اسپرنگ فزکس بجٹ تقسیم • Barakah Split",
            fontSize = 11.sp,
            color = TealPrimary,
            fontWeight = FontWeight.Medium
          )
        }

        // Voice button
        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(AmberLight)
            .clickable {
              onPlayVoice(
                "Barakah smart split ke tehat tankhwah milte hi 4 lifafon mein taqseem karein: zarooriyat 70 feesad, committee 6 feesad, emergency 6 feesad aur bachat 18 feesad. Is se maheenay ke aakhir mein udhaar nahi lena parta.",
                "Barakah smart split formula se tankhwah chaar lifafon mein taqseem hoti hai."
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
        .testTag("envelope_split_scroll"),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // 1. Interactive Spring Physics Visual Allocation Card
      item {
        EnvelopeSpringAllocationView(
          totalSalary = totalSalary,
          onPlayVoice = onPlayVoice,
          onOpenFullDetail = null
        )
      }

      // 2. Barakah Advice Banner
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
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Lightbulb,
              contentDescription = null,
              tint = EmeraldDark,
              modifier = Modifier.size(22.dp)
            )

            Column {
              Text(
                text = "لفافہ سسٹم کی برکت (Why Physical Envelope Splitting Works)",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = EmeraldDark
              )
              Text(
                text = "تنخواہ ملتے ہی پہلے دن ہر خرچے کا پیسہ الگ کر کے رکھ دینے سے آخری دس دنوں میں پریشانی یا کریانہ والے سے نیا ادھار لینے کی نوبت نہیں آتی۔ ہر لفافے کی ایک واضح حد ہے۔",
                fontSize = 11.5.sp,
                color = SlateDarkBg,
                lineHeight = 16.sp,
                modifier = Modifier.padding(top = 3.dp)
              )
            }
          }
        }
      }

      // 3. Itemized Envelopes Breakdown List
      items(envelopes) { envelope ->
        EnvelopeDetailCard(
          envelope = envelope,
          onNavigate = onNavigateToDestination
        )
      }

      // 4. Quick Action Footer
      item {
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Text(
              text = "فوری رسائی اور کارروائی (Quick Actions)",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = SlateTextPrimary
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Box(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(10.dp))
                  .background(TealSurface)
                  .border(1.dp, SlateBorder, RoundedCornerShape(10.dp))
                  .clickable { onNavigateToDestination(AppDestination.FactorySalarySlip) }
                  .padding(vertical = 10.dp, horizontal = 8.dp),
                contentAlignment = Alignment.Center
              ) {
                Text("فیکٹری سلپ →", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TealDarker)
              }

              Box(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(10.dp))
                  .background(AmberLight)
                  .border(1.dp, AmberBrand.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                  .clickable { onNavigateToDestination(AppDestination.GoalsAndKameti) }
                  .padding(vertical = 10.dp, horizontal = 8.dp),
                contentAlignment = Alignment.Center
              ) {
                Text("کمیٹی ریکارڈ →", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AmberDark)
              }

              Box(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(10.dp))
                  .background(RoseSurface)
                  .border(1.dp, RoseBorder, RoundedCornerShape(10.dp))
                  .clickable { onNavigateToDestination(AppDestination.EmergencyLocker) }
                  .padding(vertical = 10.dp, horizontal = 8.dp),
                contentAlignment = Alignment.Center
              ) {
                Text("ہنگامی لاکر →", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = RoseDark)
              }
            }
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
private fun EnvelopeDetailCard(
  envelope: Envelope,
  onNavigate: (AppDestination) -> Unit,
  modifier: Modifier = Modifier,
) {
  val icon = when (envelope.id) {
    "needs" -> Icons.Default.Home
    "commitments" -> Icons.Default.People
    "emergency" -> Icons.Default.Shield
    else -> Icons.Default.Savings
  }

  val accentColor = when (envelope.id) {
    "needs" -> TealPrimary
    "commitments" -> AmberDark
    "emergency" -> RoseAlert
    else -> EmeraldBrand
  }

  val bgSurface = when (envelope.id) {
    "needs" -> TealSurface
    "commitments" -> AmberSurface
    "emergency" -> RoseSurface
    else -> EmeraldSurface
  }

  Card(
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
    modifier = modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
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
              .size(38.dp)
              .clip(CircleShape)
              .background(bgSurface),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = icon,
              contentDescription = null,
              tint = accentColor,
              modifier = Modifier.size(20.dp)
            )
          }

          Column {
            Text(
              text = envelope.titleEnglish,
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = SlateTextPrimary
            )
            Text(
              text = "${envelope.percentage}% of total income • ${envelope.tag}",
              fontSize = 10.sp,
              color = SlateTextSecondary
            )
          }
        }

        Column(horizontalAlignment = Alignment.End) {
          Text(
            text = "PKR ${String.format("%,d", envelope.amount)}",
            fontSize = 15.sp,
            fontWeight = FontWeight.Black,
            color = SlateTextPrimary
          )
          Text(
            text = "${envelope.percentage}% الاٹ شدہ",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = accentColor
          )
        }
      }

      if (envelope.items.isNotEmpty()) {
        Spacer(modifier = Modifier.height(10.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          envelope.items.forEach { subItem ->
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFF8FAFC))
                .border(1.dp, SlateBorder, RoundedCornerShape(8.dp))
                .padding(horizontal = 6.dp, vertical = 6.dp)
            ) {
              Column {
                Text(subItem.label, fontSize = 10.sp, color = SlateTextSecondary, fontWeight = FontWeight.Medium)
                Text("Rs. ${String.format("%,d", subItem.amount)}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SlateTextPrimary)
              }
            }
          }
        }
      }

      // Link to appropriate destination if exists
      if (envelope.id == "commitments") {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "کمیٹیوں کی تفصیلی قسطیں اور تاریخیں دیکھیں →",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = AmberDark,
          modifier = Modifier
            .clickable { onNavigate(AppDestination.GoalsAndKameti) }
            .padding(vertical = 2.dp)
        )
      } else if (envelope.id == "emergency") {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "ہنگامی لاکر اور محفوظ ڈیپازٹ کھولیں →",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = RoseDark,
          modifier = Modifier
            .clickable { onNavigate(AppDestination.EmergencyLocker) }
            .padding(vertical = 2.dp)
        )
      }
    }
  }
}
