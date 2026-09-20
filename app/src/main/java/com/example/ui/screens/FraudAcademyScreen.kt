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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ScamSimulation
import com.example.ui.theme.AmberBrand
import com.example.ui.theme.AmberDark
import com.example.ui.theme.AmberLight
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
import com.example.ui.theme.SlateTextPrimary
import com.example.ui.theme.SlateTextSecondary
import com.example.ui.theme.TealBorder
import com.example.ui.theme.TealDark
import com.example.ui.theme.TealDarker
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.TealSurface

@Composable
fun FraudAcademyScreen(
  scenarios: List<ScamSimulation>,
  onBack: () -> Unit,
  onAnswer: (String, Boolean) -> Unit,
  onPlayVoice: (String, String) -> Unit,
  modifier: Modifier = Modifier,
) {
  val completedCount = scenarios.count { it.isCompleted }

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
          modifier = Modifier.testTag("fraud_academy_back_btn")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = SlateTextPrimary
          )
        }

        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "Fraud Defense Academy",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = SlateTextPrimary
          )
          Text(
            text = "فراڈ سے بچاؤ اکیڈمی و اسکام سمیلیٹر",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = RoseDark
          )
        }

        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(AmberLight)
            .clickable {
              onPlayVoice(
                "Fraud Academy mein 4 aam fone dhokay darj hain. Sahi jawab chun kar apna score barhayen aur digital dhaal hasil karein.",
                "Fraud Academy mein aam fone aur SMS dhokay pehchanain."
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
        .testTag("fraud_academy_scroll"),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // Certification Banner
      item {
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = if (completedCount >= 3) EmeraldSurface else TealSurface),
          border = androidx.compose.foundation.BorderStroke(1.dp, if (completedCount >= 3) EmeraldLight else TealBorder),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Box(
              modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(if (completedCount >= 3) EmeraldDark else TealDark),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.Shield, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
            }

            Column {
              Text(
                text = if (completedCount >= 3) "Certified Cyber Shield Active ✓" else "Digital Fraud Shield Progress",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (completedCount >= 3) EmeraldDark else TealDarker
              )
              Text(
                text = "$completedCount of ${scenarios.size} Scam Scenarios Mastered",
                fontSize = 11.sp,
                color = SlateTextSecondary
              )
            }
          }
        }
      }

      // Scenarios List
      items(scenarios) { scam ->
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(
                  modifier = Modifier
                    .size(28.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(RoseLight),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(Icons.Default.Warning, contentDescription = null, tint = RoseAlert, modifier = Modifier.size(15.dp))
                }
                Column {
                  Text(text = scam.titleEnglish, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SlateTextPrimary)
                  Text(text = scam.titleUrdu, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = RoseDark)
                }
              }

              Icon(
                imageVector = Icons.Default.VolumeUp,
                contentDescription = "Listen",
                tint = AmberDark,
                modifier = Modifier
                  .size(20.dp)
                  .clickable { onPlayVoice(scam.scamText, scam.scamText) }
              )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Scam text bubble
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(RoseSurface)
                .border(1.dp, RoseBorder, RoundedCornerShape(10.dp))
                .padding(10.dp)
            ) {
              Text(
                text = scam.scamText,
                fontSize = 12.sp,
                lineHeight = 17.sp,
                color = SlateTextPrimary,
                fontWeight = FontWeight.Medium
              )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Safe Choice Button
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(if (scam.isCompleted && scam.isCorrect == true) EmeraldLight else Color(0xFFF8FAFC))
                .border(1.dp, if (scam.isCompleted && scam.isCorrect == true) EmeraldDark else TealBorder, RoundedCornerShape(10.dp))
                .clickable { onAnswer(scam.id, true) }
                .padding(10.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = scam.optionSafe,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (scam.isCompleted && scam.isCorrect == true) EmeraldDark else TealDark,
                  modifier = Modifier.weight(1f)
                )
                if (scam.isCompleted && scam.isCorrect == true) {
                  Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldDark, modifier = Modifier.size(16.dp))
                }
              }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Trap Choice Button
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(if (scam.isCompleted && scam.isCorrect == false) RoseLight else Color(0xFFF8FAFC))
                .border(1.dp, if (scam.isCompleted && scam.isCorrect == false) RoseAlert else SlateBorder, RoundedCornerShape(10.dp))
                .clickable { onAnswer(scam.id, false) }
                .padding(10.dp)
            ) {
              Text(
                text = scam.optionTrap,
                fontSize = 11.sp,
                color = if (scam.isCompleted && scam.isCorrect == false) RoseDark else SlateTextSecondary
              )
            }
          }
        }
      }

      // 3 Golden Safety Rules Card
      item {
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = SlateDarkBg),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Text(
              text = "پاکستان میں مالی تحفظ کے 3 سنہری اصول",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = AmberBrand
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("1. فون پر کبھی بھی OTP یا پن کوڈ کسی کو نہ بتائیں (چاہے کوئی خود کو اسٹیٹ بینک کہے)۔", fontSize = 11.sp, color = Color.White.copy(alpha = 0.9f))
            Spacer(modifier = Modifier.height(4.dp))
            Text("2. کسی بھی سرکاری وظیفے یا انعام کے لیے پہلے پیسے یا ایزی لوڈ نہیں دینا پڑتا۔", fontSize = 11.sp, color = Color.White.copy(alpha = 0.9f))
            Spacer(modifier = Modifier.height(4.dp))
            Text("3. شک کی صورت میں فیکٹری سپروائزر یا بینک کی 4 ہندسوں کی ہیلپ لائن ملائیں۔", fontSize = 11.sp, color = Color.White.copy(alpha = 0.9f))
          }
        }
      }

      item {
        Spacer(modifier = Modifier.height(16.dp))
      }
    }
  }
}
