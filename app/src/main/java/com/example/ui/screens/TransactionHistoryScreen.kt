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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
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
import com.example.model.TransactionItem
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
import com.example.ui.theme.SlateTextMuted
import com.example.ui.theme.SlateTextPrimary
import com.example.ui.theme.SlateTextSecondary
import com.example.ui.theme.TealBorder
import com.example.ui.theme.TealDark
import com.example.ui.theme.TealDarker
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.TealSurface

@Composable
fun TransactionHistoryScreen(
  transactions: List<TransactionItem>,
  onBack: () -> Unit,
  onAddTransaction: (String, String, Long, Boolean, String, String, String) -> Unit,
  onPlayVoice: (String, String) -> Unit,
  modifier: Modifier = Modifier,
) {
  var selectedFilter by remember { mutableStateOf("All") }
  var showAddDialog by remember { mutableStateOf(false) }

  val filteredList = when (selectedFilter) {
    "Needs" -> transactions.filter { it.envelopeId == "needs" }
    "Kameti" -> transactions.filter { it.envelopeId == "commitments" }
    "Emergency" -> transactions.filter { it.envelopeId == "emergency" }
    "Income" -> transactions.filter { !it.isExpense }
    else -> transactions
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFF8FAFC))
  ) {
    Column(modifier = Modifier.fillMaxSize()) {
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
            modifier = Modifier.testTag("transactions_back_btn")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back",
              tint = SlateTextPrimary
            )
          }

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Roznamcha & Full Ledger",
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = SlateTextPrimary
            )
            Text(
              text = "مکمل روزنامچہ و خرچ کا حساب",
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
                  "Roznamcha page par aap ka tamam kharcha aur tankhwah ki tafseel mojood ha. Aap naya kharcha bhi darj kar saktay hain.",
                  "Roznamcha page par tamam kharcha darj hai."
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

      // Filter chips
      LazyRow(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 14.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        val filters = listOf(
          "All" to "تمام",
          "Needs" to "ضروری",
          "Kameti" to "کمیٹی",
          "Emergency" to "تحفظ",
          "Income" to "آمدنی"
        )
        items(filters) { (key, urdu) ->
          val isSel = selectedFilter == key
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(12.dp))
              .background(if (isSel) TealPrimary else Color.White)
              .border(1.dp, if (isSel) TealPrimary else SlateBorder, RoundedCornerShape(12.dp))
              .clickable { selectedFilter = key }
              .padding(horizontal = 12.dp, vertical = 6.dp)
          ) {
            Text(
              text = "$key ($urdu)",
              fontSize = 11.sp,
              fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
              color = if (isSel) Color.White else SlateTextPrimary
            )
          }
        }
      }

      // Transaction items list
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 14.dp)
          .testTag("transactions_list_scroll"),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(filteredList) { tx ->
          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
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
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (tx.isExpense) RoseLight else EmeraldLight),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = if (tx.isExpense) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                    contentDescription = null,
                    tint = if (tx.isExpense) RoseAlert else EmeraldDark,
                    modifier = Modifier.size(16.dp)
                  )
                }

                Column {
                  Text(
                    text = tx.title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = SlateTextPrimary
                  )
                  Text(
                    text = "${tx.urduTitle} • ${tx.date}",
                    fontSize = 10.sp,
                    color = SlateTextSecondary
                  )
                  Text(
                    text = "ذریعہ: ${tx.paymentMethod}",
                    fontSize = 9.sp,
                    color = SlateTextMuted
                  )
                }
              }

              Column(horizontalAlignment = Alignment.End) {
                Text(
                  text = "${if (tx.isExpense) "-" else "+"}Rs. ${String.format("%,d", tx.amount)}",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Black,
                  color = if (tx.isExpense) RoseAlert else EmeraldDark
                )
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(TealSurface)
                    .padding(horizontal = 5.dp, vertical = 1.dp)
                ) {
                  Text(
                    text = tx.category,
                    fontSize = 9.sp,
                    color = TealDarker,
                    fontWeight = FontWeight.SemiBold
                  )
                }
              }
            }
          }
        }

        item {
          Spacer(modifier = Modifier.height(72.dp))
        }
      }
    }

    // Floating Button to Add Expense
    FloatingActionButton(
      onClick = { showAddDialog = true },
      containerColor = TealPrimary,
      contentColor = Color.White,
      shape = CircleShape,
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(16.dp)
        .testTag("add_custom_expense_fab")
    ) {
      Icon(Icons.Default.Add, contentDescription = "Add Expense")
    }
  }

  // Dialog to Add Expense
  if (showAddDialog) {
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Groceries") }
    var paymentMethod by remember { mutableStateOf("Cash") }

    Dialog(onDismissRequest = { showAddDialog = false }) {
      Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        modifier = Modifier.padding(8.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text("نیا خرچہ درج کریں", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = SlateTextPrimary)
          Spacer(modifier = Modifier.height(10.dp))
          OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("خرچ کی تفصیل (مثلاً: سبزی، بل)") },
            modifier = Modifier.fillMaxWidth()
          )
          Spacer(modifier = Modifier.height(6.dp))
          OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("رقم (PKR)") },
            modifier = Modifier.fillMaxWidth()
          )
          Spacer(modifier = Modifier.height(6.dp))
          OutlinedTextField(
            value = paymentMethod,
            onValueChange = { paymentMethod = it },
            label = { Text("طریقہ ادائیگی (Cash / JazzCash)") },
            modifier = Modifier.fillMaxWidth()
          )
          Spacer(modifier = Modifier.height(14.dp))
          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Button(
              onClick = { showAddDialog = false },
              colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
            ) {
              Text("منسوخ", color = Color.Black)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
              onClick = {
                val amt = amount.toLongOrNull() ?: 0L
                if (title.isNotBlank() && amt > 0) {
                  onAddTransaction(title, title, amt, true, category, "needs", paymentMethod)
                  showAddDialog = false
                }
              },
              colors = ButtonDefaults.buttonColors(containerColor = TealPrimary)
            ) {
              Text("درج کریں")
            }
          }
        }
      }
    }
  }
}
