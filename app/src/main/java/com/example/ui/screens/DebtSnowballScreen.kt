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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DebtItem
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
import com.example.ui.theme.TealDarker
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.TealSurface

@Composable
fun DebtSnowballScreen(
  debts: List<DebtItem>,
  onBack: () -> Unit,
  onRepayDebt: (String, Long) -> Unit,
  onAddNewDebt: (String, String, Long, Long, String) -> Unit,
  onShowToast: (String) -> Unit,
  onPlayVoice: (String, String) -> Unit,
  modifier: Modifier = Modifier,
) {
  var showAddDialog by remember { mutableStateOf(false) }

  val totalRemaining = debts.sumOf { it.remainingAmount }
  val totalInitial = debts.sumOf { it.totalAmount }
  val totalRepaid = (totalInitial - totalRemaining).coerceAtLeast(0L)
  val overallProgress = if (totalInitial > 0) (totalRepaid.toFloat() / totalInitial) else 1f

  // Snowball method sorts by smallest remaining amount first for quick psychological wins
  val sortedDebts = remember(debts) {
    debts.sortedBy { it.remainingAmount }
  }

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
          modifier = Modifier.testTag("debt_snowball_back_btn")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = SlateTextPrimary
          )
        }

        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "Debt Freedom Snowball Plan",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = SlateTextPrimary
          )
          Text(
            text = "قرض سے نجات کا شریعت موافق اسنو بال طریقہ",
            fontSize = 11.sp,
            color = EmeraldDark,
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
                "Snowball tareeqay mein sab se pehle chhota qarz ada karein taake jald azaadi aur hausla miley. Kisi bhi sood ya markup se bachein aur Shariah mutabiq nijaat paayein.",
                "Snowball tareeqay mein sab se pehle chhota qarz ada karein."
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

        Spacer(modifier = Modifier.width(4.dp))

        // Add Debt button
        IconButton(
          onClick = { showAddDialog = !showAddDialog },
          modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(EmeraldSurface)
            .testTag("add_debt_toggle_btn")
        ) {
          Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add Debt",
            tint = EmeraldDark,
            modifier = Modifier.size(20.dp)
          )
        }
      }
    }

    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 14.dp, vertical = 12.dp)
        .testTag("debt_snowball_list"),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      // 1. Debt Freedom Progress Dashboard
      item {
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = SlateDarkBg),
          elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(Icons.Default.Shield, contentDescription = null, tint = EmeraldBrand, modifier = Modifier.size(18.dp))
                Text(
                  text = "کل باقی ماندہ قرض (Remaining Debt)",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White.copy(alpha = 0.85f)
                )
              }

              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(6.dp))
                  .background(if (totalRemaining == 0L) EmeraldDark else Color(0xFF334155))
                  .padding(horizontal = 8.dp, vertical = 3.dp)
              ) {
                Text(
                  text = if (totalRemaining == 0L) "قرض سے پاک ✓" else "${(overallProgress * 100).toInt()}% ادا شدہ",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (totalRemaining == 0L) Color.White else EmeraldBrand
                )
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
              text = "PKR ${String.format("%,d", totalRemaining)}",
              fontSize = 28.sp,
              fontWeight = FontWeight.Black,
              color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Progress bar
            LinearProgressIndicator(
              progress = { overallProgress },
              modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
              color = EmeraldBrand,
              trackColor = Color(0xFF334155),
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Box(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(10.dp))
                  .background(Color.White.copy(alpha = 0.08f))
                  .padding(8.dp)
              ) {
                Column {
                  Text("ادا شدہ رقم", fontSize = 10.sp, color = Color.White.copy(alpha = 0.7f))
                  Text("PKR ${String.format("%,d", totalRepaid)}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = EmeraldBrand)
                }
              }

              Box(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(10.dp))
                  .background(Color.White.copy(alpha = 0.08f))
                  .padding(8.dp)
              ) {
                Column {
                  Text("ماہانہ قسطوں کا بوجھ", fontSize = 10.sp, color = Color.White.copy(alpha = 0.7f))
                  val monthlyTotal = debts.filter { it.remainingAmount > 0 }.sumOf { it.monthlyCommitment }
                  Text("PKR ${String.format("%,d", monthlyTotal)}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AmberBrand)
                }
              }
            }
          }
        }
      }

      // 2. Snowball Rule Explanation Banner
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = EmeraldSurface),
          border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldLight),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Speed,
              contentDescription = null,
              tint = EmeraldDark,
              modifier = Modifier.size(20.dp)
            )

            Column {
              Text(
                text = "اسنو بال اصول: چھوٹا قرض پہلے ختم کریں!",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = EmeraldDark
              )
              Text(
                text = "سب سے کم رقم والے قرضے (مثلاً دکان دار یا دوست کا ادھار) کو اضافی پیسوں سے پہلے ختم کریں تاکہ ایک بوجھ کم ہو اور حوصلہ بلند ہو۔",
                fontSize = 11.sp,
                color = SlateDarkBg,
                lineHeight = 15.sp,
                modifier = Modifier.padding(top = 2.dp)
              )
            }
          }
        }
      }

      // Add Debt Dialog/Card
      if (showAddDialog) {
        item {
          AddDebtCard(
            onSave = { name, rel, tot, mon, urg ->
              onAddNewDebt(name, rel, tot, mon, urg)
              showAddDialog = false
            },
            onCancel = { showAddDialog = false }
          )
        }
      }

      // 3. Debts List sorted by smallest balance (Snowball strategy)
      items(sortedDebts, key = { it.id }) { debt ->
        DebtCard(
          debt = debt,
          onRepay = { amt -> onRepayDebt(debt.id, amt) },
          onShowToast = onShowToast
        )
      }

      item {
        Spacer(modifier = Modifier.height(16.dp))
      }
    }
  }
}

@Composable
private fun DebtCard(
  debt: DebtItem,
  onRepay: (Long) -> Unit,
  onShowToast: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  var showPayDialog by remember { mutableStateOf(false) }
  var customAmountText by remember { mutableStateOf("") }

  val isCleared = debt.remainingAmount == 0L
  val progress = if (debt.totalAmount > 0) {
    ((debt.totalAmount - debt.remainingAmount).toFloat() / debt.totalAmount).coerceIn(0f, 1f)
  } else 1f

  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    border = androidx.compose.foundation.BorderStroke(
      1.dp,
      if (isCleared) EmeraldBrand.copy(alpha = 0.5f) else SlateBorder
    ),
    modifier = modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
              text = debt.creditorUrdu,
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              color = SlateTextPrimary
            )
            if (debt.isShariahFriendly) {
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(4.dp))
                  .background(EmeraldLight)
                  .padding(horizontal = 5.dp, vertical = 1.dp)
              ) {
                Text("شریعت موافق (سود فری)", fontSize = 8.5.sp, fontWeight = FontWeight.Bold, color = EmeraldDark)
              }
            }
          }
          Text(
            text = "${debt.creditorName} • ${debt.relationOrType}",
            fontSize = 10.sp,
            color = SlateTextSecondary
          )
        }

        Column(horizontalAlignment = Alignment.End) {
          Text(
            text = "باقی: PKR ${String.format("%,d", debt.remainingAmount)}",
            fontSize = 14.sp,
            fontWeight = FontWeight.Black,
            color = if (isCleared) EmeraldDark else RoseAlert
          )
          Text(
            text = "کل: PKR ${String.format("%,d", debt.totalAmount)}",
            fontSize = 10.sp,
            color = SlateTextMuted
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Progress bar
      LinearProgressIndicator(
        progress = { progress },
        modifier = Modifier
          .fillMaxWidth()
          .height(6.dp)
          .clip(RoundedCornerShape(3.dp)),
        color = EmeraldBrand,
        trackColor = Color(0xFFF1F5F9),
      )

      if (debt.repaymentStrategyTip.isNotBlank()) {
        Spacer(modifier = Modifier.height(8.dp))
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF8FAFC))
            .padding(horizontal = 8.dp, vertical = 5.dp)
        ) {
          Text(
            text = "💡 ${debt.repaymentStrategyTip}",
            fontSize = 10.sp,
            color = SlateTextSecondary,
            lineHeight = 13.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      if (isCleared) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldBrand, modifier = Modifier.size(16.dp))
          Text(
            text = "یہ قرض مکمل ادا ہو چکا ہے! الحمدللہ",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = EmeraldDark
          )
        }
      } else {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "ماہانہ قسط: PKR ${String.format("%,d", debt.monthlyCommitment)}",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = SlateTextPrimary
            )
            Text(
              text = debt.urgencyLevel,
              fontSize = 9.sp,
              color = SlateTextSecondary
            )
          }

          Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Button(
              onClick = { onRepay(debt.monthlyCommitment.coerceAtMost(debt.remainingAmount)) },
              shape = RoundedCornerShape(8.dp),
              colors = ButtonDefaults.buttonColors(containerColor = EmeraldBrand),
              modifier = Modifier.height(34.dp)
            ) {
              Text("قسط دیں", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            Button(
              onClick = { showPayDialog = !showPayDialog },
              shape = RoundedCornerShape(8.dp),
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F5F9), contentColor = SlateTextPrimary),
              modifier = Modifier.height(34.dp)
            ) {
              Text("دیگر رقم", fontSize = 11.sp)
            }
          }
        }
      }

      // Inline custom payment
      if (showPayDialog && !isCleared) {
        Spacer(modifier = Modifier.height(10.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedTextField(
            value = customAmountText,
            onValueChange = { customAmountText = it },
            placeholder = { Text("کتنی رقم ادا کرنی ہے؟", fontSize = 11.sp) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f).height(46.dp)
          )

          Button(
            onClick = {
              val amt = customAmountText.toLongOrNull() ?: 0L
              if (amt > 0) {
                onRepay(amt.coerceAtMost(debt.remainingAmount))
                showPayDialog = false
                customAmountText = ""
              }
            },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = EmeraldDark),
            modifier = Modifier.height(46.dp)
          ) {
            Text("ادائیگی کریں", fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}

@Composable
private fun AddDebtCard(
  onSave: (String, String, Long, Long, String) -> Unit,
  onCancel: () -> Unit,
  modifier: Modifier = Modifier,
) {
  var name by remember { mutableStateOf("") }
  var relation by remember { mutableStateOf("محلہ دکان دار ادھار") }
  var totalText by remember { mutableStateOf("") }
  var monthlyText by remember { mutableStateOf("") }

  Card(
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = androidx.compose.foundation.BorderStroke(1.5.dp, EmeraldDark),
    modifier = modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Text(
        text = "نیا قرض شامل کریں (Add Debt Entry)",
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = SlateTextPrimary
      )

      Spacer(modifier = Modifier.height(10.dp))

      OutlinedTextField(
        value = name,
        onValueChange = { name = it },
        label = { Text("قرض خواہ کا نام (Creditor Name)") },
        modifier = Modifier.fillMaxWidth()
      )

      Spacer(modifier = Modifier.height(8.dp))

      OutlinedTextField(
        value = relation,
        onValueChange = { relation = it },
        label = { Text("قرض کی قسم (Loan Type / Relation)") },
        modifier = Modifier.fillMaxWidth()
      )

      Spacer(modifier = Modifier.height(8.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        OutlinedTextField(
          value = totalText,
          onValueChange = { totalText = it },
          label = { Text("کل قرض (Total PKR)") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          modifier = Modifier.weight(1f)
        )

        OutlinedTextField(
          value = monthlyText,
          onValueChange = { monthlyText = it },
          label = { Text("ماہانہ قسط (Monthly)") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          modifier = Modifier.weight(1f)
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Button(
          onClick = onCancel,
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE2E8F0), contentColor = SlateTextPrimary),
          modifier = Modifier.weight(1f)
        ) {
          Text("منسوخ", fontSize = 11.sp)
        }

        Button(
          onClick = {
            val tot = totalText.toLongOrNull() ?: 0L
            val mon = monthlyText.toLongOrNull() ?: 0L
            if (name.isNotBlank() && tot > 0) {
              onSave(name, relation, tot, mon, "اعلیٰ ترجیح")
            }
          },
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(containerColor = EmeraldDark),
          modifier = Modifier.weight(1f)
        ) {
          Text("قرض کھاتہ محفوظ کریں", fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}
