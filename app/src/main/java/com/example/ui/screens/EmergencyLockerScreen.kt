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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import com.example.ui.theme.AmberBrand
import com.example.ui.theme.AmberDark
import com.example.ui.theme.AmberLight
import com.example.ui.theme.AmberSurface
import com.example.ui.theme.EmeraldBrand
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.EmeraldSurface
import com.example.ui.theme.RoseAlert
import com.example.ui.theme.RoseDark
import com.example.ui.theme.RoseLight
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateDarkBg
import com.example.ui.theme.SlateTextPrimary
import com.example.ui.theme.SlateTextSecondary
import com.example.ui.theme.TealBorder
import com.example.ui.theme.TealDark
import com.example.ui.theme.TealDarker
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.TealSurface

@Composable
fun EmergencyLockerScreen(
  balance: Long,
  onBack: () -> Unit,
  onDeposit: (Long) -> Unit,
  onWithdraw: (Long) -> Unit,
  onPlayVoice: (String, String) -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFF8FAFC))
  ) {
    // Header
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
          modifier = Modifier.testTag("locker_back_btn")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = SlateTextPrimary
          )
        }

        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "Emergency Fund Shield & Safe Locker",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = SlateTextPrimary
          )
          Text(
            text = "ہنگامی فنڈ و محفوظ ڈیجیٹل لاکر",
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
                "Yeh aap ka emergency locker hai. Yeh raqam aisi jagah mahfooz hai jahan se bina sakht zaroorat ke kharch na ho sakay.",
                "Yeh aap ka emergency locker hai jahan mahfooz raqam mojood hai."
              )
            },
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.VolumeUp,
            contentDescription = "Listen",
            tint = AmberDark,
            modifier = Modifier.size(17.dp)
          )
        }
      }
    }

    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 14.dp, vertical = 12.dp)
        .testTag("emergency_locker_scroll"),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // Big Visual Vault Card
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
                  colors = listOf(TealDarker, SlateDarkBg)
                )
              )
              .padding(18.dp)
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
              Box(
                modifier = Modifier
                  .size(54.dp)
                  .clip(CircleShape)
                  .background(TealDark.copy(alpha = 0.8f))
                  .border(2.dp, AmberBrand, CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Lock,
                  contentDescription = null,
                  tint = AmberBrand,
                  modifier = Modifier.size(26.dp)
                )
              }

              Spacer(modifier = Modifier.height(10.dp))

              Text(
                text = "محفوظ رقم (Vault Balance)",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.85f),
                fontWeight = FontWeight.Medium
              )

              Text(
                text = "Rs. ${String.format("%,d", balance)}",
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
              )

              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(12.dp))
                  .background(EmeraldDark)
                  .padding(horizontal = 10.dp, vertical = 4.dp)
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                  Icon(Icons.Default.Shield, contentDescription = null, tint = Color.White, modifier = Modifier.size(13.dp))
                  Text(
                    text = "15 Days Safety Buffer Active",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                  )
                }
              }
            }
          }
        }
      }

      // Quick Deposit Section
      item {
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Text(
              text = "Quick Deposit (فوری رقم شامل کریں)",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = SlateTextPrimary
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Button(
                onClick = { onDeposit(500) },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TealSurface, contentColor = TealDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, TealBorder),
                modifier = Modifier.weight(1f).height(40.dp)
              ) {
                Text("+ Rs. 500", fontSize = 11.sp, fontWeight = FontWeight.Bold)
              }

              Button(
                onClick = { onDeposit(1_000) },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TealSurface, contentColor = TealDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, TealBorder),
                modifier = Modifier.weight(1f).height(40.dp)
              ) {
                Text("+ Rs. 1,000", fontSize = 11.sp, fontWeight = FontWeight.Bold)
              }

              Button(
                onClick = { onDeposit(3_000) },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TealPrimary, contentColor = Color.White),
                modifier = Modifier.weight(1f).height(40.dp)
              ) {
                Text("+ Rs. 3,000", fontSize = 11.sp, fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }

      // Emergency Runway Simulator Card
      item {
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Text(
              text = "Emergency Runway Calculator",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = SlateTextPrimary
            )
            Text(
              text = "اگر خدانخواستہ تنخواہ رک جائے تو راشن اور کرایہ کتنے دن چلے گا؟",
              fontSize = 11.sp,
              color = SlateTextSecondary
            )

            Spacer(modifier = Modifier.height(12.dp))

            RunwayLevelRow(title = "15 Days Shield", amount = "Rs. 10,000", isReached = balance >= 10_000)
            Spacer(modifier = Modifier.height(8.dp))
            RunwayLevelRow(title = "30 Days Buffer (Golden Target)", amount = "Rs. 20,000", isReached = balance >= 20_000)
            Spacer(modifier = Modifier.height(8.dp))
            RunwayLevelRow(title = "60 Days Stronghold", amount = "Rs. 40,000", isReached = balance >= 40_000)
          }
        }
      }

      // Emergency Withdraw / Unlock Section
      item {
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = RoseLight.copy(alpha = 0.4f)),
          border = androidx.compose.foundation.BorderStroke(1.dp, RoseDark.copy(alpha = 0.3f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              Icon(Icons.Default.LockOpen, contentDescription = null, tint = RoseAlert, modifier = Modifier.size(16.dp))
              Text(
                text = "ہنگامی ضرورت میں رقم نکالیں (Emergency Withdraw)",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = RoseDark
              )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "نوٹ: یہ رقم صرف اسپتال، ادویات یا غیر متوقع ہنگامی اخراجات کے لیے استعمال کریں۔",
              fontSize = 10.sp,
              color = SlateTextSecondary
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(
              onClick = { onWithdraw(2_000) },
              shape = RoundedCornerShape(10.dp),
              colors = ButtonDefaults.outlinedButtonColors(contentColor = RoseDark),
              border = androidx.compose.foundation.BorderStroke(1.dp, RoseAlert),
              modifier = Modifier.fillMaxWidth().height(42.dp)
            ) {
              Text("Rs. 2,000 ہنگامی نکلوائیں (Withdraw)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
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
private fun RunwayLevelRow(title: String, amount: String, isReached: Boolean) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(10.dp))
      .background(if (isReached) EmeraldSurface else Color(0xFFF8FAFC))
      .border(1.dp, if (isReached) EmeraldLight else SlateBorder, RoundedCornerShape(10.dp))
      .padding(10.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
      Icon(
        imageVector = if (isReached) Icons.Default.CheckCircle else Icons.Default.Lock,
        contentDescription = null,
        tint = if (isReached) EmeraldDark else Color.Gray,
        modifier = Modifier.size(15.dp)
      )
      Text(title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isReached) EmeraldDark else SlateTextPrimary)
    }
    Text(amount, fontSize = 12.sp, fontWeight = FontWeight.Black, color = if (isReached) EmeraldDark else SlateTextSecondary)
  }
}
