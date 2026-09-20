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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.AmberBrand
import com.example.ui.theme.AmberDark
import com.example.ui.theme.AmberLight
import com.example.ui.theme.RoseAlert
import com.example.ui.theme.RoseBorder
import com.example.ui.theme.RoseDark
import com.example.ui.theme.RoseLight
import com.example.ui.theme.RoseSurface
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateDarkBg
import com.example.ui.theme.SlateTextPrimary
import com.example.ui.theme.SlateTextSecondary
import com.example.ui.theme.TealDark
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.TealSurface

@Composable
fun FraudShieldDialog(
  isOpen: Boolean,
  onDismiss: () -> Unit,
  onAnswer: (Boolean) -> Unit,
  onListenAudio: () -> Unit,
) {
  if (!isOpen) return

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(24.dp),
      color = Color.White,
      shadowElevation = 16.dp,
      modifier = Modifier
        .fillMaxWidth()
        .padding(4.dp)
        .testTag("fraud_shield_dialog")
    ) {
      Column(
        modifier = Modifier.padding(18.dp)
      ) {
        // Header
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
                .clip(RoundedCornerShape(10.dp))
                .background(RoseLight),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Security,
                contentDescription = "Fraud Shield",
                tint = RoseAlert,
                modifier = Modifier.size(20.dp)
              )
            }
            Column {
              Text(
                text = "Fraud Shield Training",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = SlateTextPrimary
              )
              Text(
                text = "فراڈ سے حفاظت کی مشق",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = RoseDark
              )
            }
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier
              .size(32.dp)
              .clip(CircleShape)
              .background(Color(0xFFF1F5F9))
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Close",
              tint = SlateTextSecondary,
              modifier = Modifier.size(16.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Scenario Box
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(RoseSurface)
            .border(1.dp, RoseBorder, RoundedCornerShape(16.dp))
            .padding(12.dp)
        ) {
          Column {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Warning",
                tint = RoseDark,
                modifier = Modifier.size(15.dp)
              )
              Text(
                text = "جعلی بینک کال سے ہوشیار!",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = RoseDark
              )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
              text = "اگر کوئی کال کرکے کہے: \"میں اسٹیٹ بینک سے بول رہا ہوں، آپ کا JazzCash یا تنخواہ اکاؤنٹ بلاک ہو رہا ہے، فوراً OTP بتائیں\" تو کیا کریں؟",
              fontSize = 12.sp,
              lineHeight = 18.sp,
              color = SlateTextPrimary,
              fontWeight = FontWeight.Medium
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Choice 1: Correct Answer
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(TealSurface)
            .border(2.dp, TealPrimary, RoundedCornerShape(14.dp))
            .clickable { onAnswer(true) }
            .testTag("fraud_correct_answer_btn")
            .padding(12.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "فون کاٹ دیں اور فیکٹری سپروائزر کو بتائیں ✓",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = TealDark,
              modifier = Modifier.weight(1f)
            )
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = "Correct",
              tint = TealPrimary,
              modifier = Modifier.size(18.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Choice 2: Trap Answer
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFFF8FAFC))
            .border(1.dp, SlateBorder, RoundedCornerShape(14.dp))
            .clickable { onAnswer(false) }
            .testTag("fraud_wrong_answer_btn")
            .padding(12.dp)
        ) {
          Text(
            text = "جلدی میں میسج کا کوڈ بتا دیں",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = SlateTextSecondary
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Actions
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedButton(
            onClick = onListenAudio,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(
              containerColor = AmberLight,
              contentColor = AmberDark
            ),
            border = androidx.compose.foundation.BorderStroke(1.dp, AmberBrand.copy(alpha = 0.5f)),
            modifier = Modifier
              .weight(1f)
              .height(44.dp)
          ) {
            Icon(
              imageVector = Icons.Default.VolumeUp,
              contentDescription = "Listen",
              tint = AmberDark,
              modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "آواز میں سنیں",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold
            )
          }

          Button(
            onClick = onDismiss,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = SlateDarkBg,
              contentColor = Color.White
            ),
            modifier = Modifier
              .weight(1f)
              .height(44.dp)
          ) {
            Text(
              text = "سمجھ گیا (Done)",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }
  }
}
