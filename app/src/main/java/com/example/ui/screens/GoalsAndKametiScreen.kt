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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.FamilyGoalItem
import com.example.model.KametiItem
import com.example.ui.theme.AmberBrand
import com.example.ui.theme.AmberDark
import com.example.ui.theme.AmberLight
import com.example.ui.theme.AmberSurface
import com.example.ui.theme.EmeraldBrand
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.EmeraldSurface
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
fun GoalsAndKametiScreen(
  kametis: List<KametiItem>,
  goals: List<FamilyGoalItem>,
  onBack: () -> Unit,
  onMarkKametiPaid: (String) -> Unit,
  onAddNewKameti: (String, Long, Int, Int, String) -> Unit,
  onContributeToGoal: (String, Long) -> Unit,
  onAddNewGoal: (String, Long, String, String) -> Unit,
  onPlayVoice: (String, String) -> Unit,
  modifier: Modifier = Modifier,
) {
  var showAddKametiDialog by remember { mutableStateOf(false) }
  var showAddGoalDialog by remember { mutableStateOf(false) }

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
          modifier = Modifier.testTag("goals_back_btn")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = SlateTextPrimary
          )
        }

        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "Family Goals & Kameti Tracker",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = SlateTextPrimary
          )
          Text(
            text = "خاندانی اہداف و بی سی کمیٹی ریکارڈ",
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
                "Kameti aur khandan ke ahdaf ka page. Yahan aap apni committee ke maheenay aur bachon ki parhai ke goals track kar saktay hain.",
                "Kameti aur khandan ke ahdaf ka hisab kitab yahan darj hai."
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
        .testTag("goals_kameti_scroll"),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // 1. Active Kametis Section
      item {
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
              imageVector = Icons.Default.Groups,
              contentDescription = null,
              tint = TealPrimary,
              modifier = Modifier.size(18.dp)
            )
            Text(
              text = "Active Kametis (فعال کمیٹیاں)",
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              color = SlateTextPrimary
            )
          }

          Button(
            onClick = { showAddKametiDialog = true },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
            modifier = Modifier.height(32.dp)
          ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(3.dp))
            Text("نئی کمیٹی", fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }
        }
      }

      items(kametis) { kameti ->
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
              verticalAlignment = Alignment.Top
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = kameti.name,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  color = SlateTextPrimary
                )
                Text(
                  text = "منتظم: ${kameti.organizer}",
                  fontSize = 11.sp,
                  color = SlateTextSecondary
                )
              }

              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .background(if (kameti.isPaidThisMonth) EmeraldLight else AmberLight)
                  .padding(horizontal = 8.dp, vertical = 3.dp)
              ) {
                Text(
                  text = if (kameti.isPaidThisMonth) "ادا شدہ ✓" else "زیر التواء",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (kameti.isPaidThisMonth) EmeraldDark else AmberDark
                )
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Metrics row
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF8FAFC), RoundedCornerShape(10.dp))
                .padding(8.dp),
              horizontalArrangement = Arrangement.SpaceAround
            ) {
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("ماہانہ قسط", fontSize = 9.sp, color = SlateTextMuted)
                Text("Rs. ${String.format("%,d", kameti.monthlyAmount)}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SlateTextPrimary)
              }
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("کل رقم (Payout)", fontSize = 9.sp, color = SlateTextMuted)
                Text("Rs. ${String.format("%,d", kameti.payoutAmount)}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = EmeraldDark)
              }
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("میری باری", fontSize = 9.sp, color = SlateTextMuted)
                Text("Month ${kameti.myTurnMonth} of ${kameti.totalMembers}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TealDarker)
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Progress: Round ${kameti.currentMonth} / ${kameti.totalMembers}",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = SlateTextSecondary
              )

              if (!kameti.isPaidThisMonth) {
                Button(
                  onClick = { onMarkKametiPaid(kameti.id) },
                  shape = RoundedCornerShape(8.dp),
                  colors = ButtonDefaults.buttonColors(containerColor = EmeraldDark),
                  modifier = Modifier.height(32.dp)
                ) {
                  Text("قسط ادا کی (Mark Paid)", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
              }
            }
          }
        }
      }

      // 2. Family Goals Section
      item {
        Spacer(modifier = Modifier.height(6.dp))
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
              imageVector = Icons.Default.Savings,
              contentDescription = null,
              tint = AmberBrand,
              modifier = Modifier.size(18.dp)
            )
            Text(
              text = "Family Goals (خاندانی اہداف)",
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              color = SlateTextPrimary
            )
          }

          Button(
            onClick = { showAddGoalDialog = true },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AmberDark),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
            modifier = Modifier.height(32.dp)
          ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(3.dp))
            Text("نیا ہدف", fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }
        }
      }

      items(goals) { goal ->
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
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                Text(text = goal.emoji, fontSize = 20.sp)
                Column {
                  Text(
                    text = goal.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = SlateTextPrimary
                  )
                  Text(
                    text = "ہدف کی تاریخ: ${goal.targetDate}",
                    fontSize = 10.sp,
                    color = SlateTextSecondary
                  )
                }
              }

              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(6.dp))
                  .background(TealSurface)
                  .padding(horizontal = 6.dp, vertical = 2.dp)
              ) {
                val pct = ((goal.currentAmount.toFloat() / goal.targetAmount) * 100).toInt()
                Text(
                  text = "$pct%",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = TealDarker
                )
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Progress Bar
            val frac = (goal.currentAmount.toFloat() / goal.targetAmount).coerceIn(0f, 1f)
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color(0xFFE2E8F0))
            ) {
              Box(
                modifier = Modifier
                  .fillMaxWidth(fraction = frac)
                  .height(8.dp)
                  .clip(RoundedCornerShape(4.dp))
                  .background(AmberBrand)
              )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Rs. ${String.format("%,d", goal.currentAmount)} / Rs. ${String.format("%,d", goal.targetAmount)}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = SlateTextPrimary
              )

              Button(
                onClick = { onContributeToGoal(goal.id, 1_000) },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AmberLight, contentColor = AmberDark),
                modifier = Modifier.height(30.dp)
              ) {
                Text("+ Rs. 1,000 شامل کریں", fontSize = 10.sp, fontWeight = FontWeight.Bold)
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

  // Dialog to Add New Kameti
  if (showAddKametiDialog) {
    var kName by remember { mutableStateOf("") }
    var kAmount by remember { mutableStateOf("3000") }
    var kMembers by remember { mutableStateOf("10") }
    var kTurn by remember { mutableStateOf("5") }
    var kOrganizer by remember { mutableStateOf("") }

    Dialog(onDismissRequest = { showAddKametiDialog = false }) {
      Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        modifier = Modifier.padding(8.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text("نئی کمیٹی درج کریں", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = SlateTextPrimary)
          Spacer(modifier = Modifier.height(10.dp))
          OutlinedTextField(
            value = kName,
            onValueChange = { kName = it },
            label = { Text("کمیٹی کا نام") },
            modifier = Modifier.fillMaxWidth()
          )
          Spacer(modifier = Modifier.height(6.dp))
          OutlinedTextField(
            value = kAmount,
            onValueChange = { kAmount = it },
            label = { Text("ماہانہ رقم (PKR)") },
            modifier = Modifier.fillMaxWidth()
          )
          Spacer(modifier = Modifier.height(6.dp))
          OutlinedTextField(
            value = kOrganizer,
            onValueChange = { kOrganizer = it },
            label = { Text("منتظم کا نام (Organizer)") },
            modifier = Modifier.fillMaxWidth()
          )
          Spacer(modifier = Modifier.height(14.dp))
          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Button(
              onClick = { showAddKametiDialog = false },
              colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
            ) {
              Text("منسوخ", color = Color.Black)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
              onClick = {
                if (kName.isNotBlank()) {
                  onAddNewKameti(kName, kAmount.toLongOrNull() ?: 3000L, kMembers.toIntOrNull() ?: 10, kTurn.toIntOrNull() ?: 5, kOrganizer.ifBlank { "Local" })
                  showAddKametiDialog = false
                }
              },
              colors = ButtonDefaults.buttonColors(containerColor = TealPrimary)
            ) {
              Text("محفوظ کریں")
            }
          }
        }
      }
    }
  }

  // Dialog to Add New Goal
  if (showAddGoalDialog) {
    var gTitle by remember { mutableStateOf("") }
    var gTarget by remember { mutableStateOf("30000") }
    var gDate by remember { mutableStateOf("Dec 2025") }

    Dialog(onDismissRequest = { showAddGoalDialog = false }) {
      Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        modifier = Modifier.padding(8.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text("نیا خاندانی ہدف بنائیں", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = SlateTextPrimary)
          Spacer(modifier = Modifier.height(10.dp))
          OutlinedTextField(
            value = gTitle,
            onValueChange = { gTitle = it },
            label = { Text("ہدف کا نام (جیسے: اسکول، شادی، موٹرسائیکل)") },
            modifier = Modifier.fillMaxWidth()
          )
          Spacer(modifier = Modifier.height(6.dp))
          OutlinedTextField(
            value = gTarget,
            onValueChange = { gTarget = it },
            label = { Text("کل مطلوبہ رقم (PKR)") },
            modifier = Modifier.fillMaxWidth()
          )
          Spacer(modifier = Modifier.height(14.dp))
          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Button(
              onClick = { showAddGoalDialog = false },
              colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
            ) {
              Text("منسوخ", color = Color.Black)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
              onClick = {
                if (gTitle.isNotBlank()) {
                  onAddNewGoal(gTitle, gTarget.toLongOrNull() ?: 30000L, gDate, "🎯")
                  showAddGoalDialog = false
                }
              },
              colors = ButtonDefaults.buttonColors(containerColor = AmberDark)
            ) {
              Text("ہدف محفوظ کریں")
            }
          }
        }
      }
    }
  }
}
