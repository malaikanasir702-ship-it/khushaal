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
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Propane
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.WaterDrop
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.UtilityBill
import com.example.ui.theme.AmberBrand
import com.example.ui.theme.AmberDark
import com.example.ui.theme.AmberLight
import com.example.ui.theme.EmeraldBrand
import com.example.ui.theme.EmeraldDark
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
fun UtilityBillsScreen(
  bills: List<UtilityBill>,
  onBack: () -> Unit,
  onTogglePayBill: (String) -> Unit,
  onAddNewBill: (String, String, String, Long, Int, String) -> Unit,
  onShowToast: (String) -> Unit,
  onPlayVoice: (String, String) -> Unit,
  modifier: Modifier = Modifier,
) {
  var showAddDialog by remember { mutableStateOf(false) }

  val totalPending = bills.filter { !it.isPaid }.sumOf { it.amount }
  val totalPaid = bills.filter { it.isPaid }.sumOf { it.amount }

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
          modifier = Modifier.testTag("utility_bills_back_btn")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = SlateTextPrimary
          )
        }

        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "Utility Bills & Lifeline Tariffs",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = SlateTextPrimary
          )
          Text(
            text = "بجلی، گیس و پانی کے بل اور لائف لائن ٹیرف بچت",
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
                "Bijli aur gas ke bil waqt par ada karein taake late surcharge se bacha ja sakay. 200 unit se kam bijli istemaal karne par sasti bijli ka lifeline rate milta hai.",
                "Bijli aur gas ke bil waqt par ada karein taake late surcharge se bacha ja sakay."
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

        // Add Bill button
        IconButton(
          onClick = { showAddDialog = !showAddDialog },
          modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(TealSurface)
            .testTag("add_bill_toggle_btn")
        ) {
          Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add Bill",
            tint = TealPrimary,
            modifier = Modifier.size(20.dp)
          )
        }
      }
    }

    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 14.dp, vertical = 12.dp)
        .testTag("utility_bills_list"),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      // 1. Monthly Bill Summary Banner
      item {
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = TealDarker),
          elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "کل واجب الادا بلز (Total Due Bills)",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.85f)
              )
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(6.dp))
                  .background(if (totalPending > 0) RoseSurface else EmeraldSurface)
                  .padding(horizontal = 8.dp, vertical = 3.dp)
              ) {
                Text(
                  text = if (totalPending > 0) "غیر ادا شدہ (Pending)" else "سب ادا شدہ ✓",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (totalPending > 0) RoseAlert else EmeraldDark
                )
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
              text = "PKR ${String.format("%,d", totalPending)}",
              fontSize = 28.sp,
              fontWeight = FontWeight.Black,
              color = Color.White
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Box(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(10.dp))
                  .background(Color.White.copy(alpha = 0.12f))
                  .padding(8.dp)
              ) {
                Column {
                  Text("اس ماہ ادا شدہ", fontSize = 10.sp, color = Color.White.copy(alpha = 0.8f))
                  Text("PKR ${String.format("%,d", totalPaid)}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = EmeraldBrand)
                }
              }

              Box(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(10.dp))
                  .background(Color.White.copy(alpha = 0.12f))
                  .padding(8.dp)
              ) {
                Column {
                  Text("کل بلز کی تعداد", fontSize = 10.sp, color = Color.White.copy(alpha = 0.8f))
                  Text("${bills.size} بلز رجسٹرڈ", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AmberBrand)
                }
              }
            }
          }
        }
      }

      // 2. Lifeline Tariff Advice Card
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = AmberLight),
          border = androidx.compose.foundation.BorderStroke(1.dp, AmberBrand.copy(alpha = 0.5f)),
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
              imageVector = Icons.Default.Lightbulb,
              contentDescription = null,
              tint = AmberDark,
              modifier = Modifier.size(20.dp)
            )

            Column {
              Text(
                text = "بجلی کا لائف لائن ٹیرف فارمولا (Save up to 40%)",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = SlateDarkBg
              )
              Text(
                text = "حکومتی قواعد کے مطابق ماہانہ 200 یونٹ سے کم بجلی استعمال کرنے پر فی یونٹ رعایت برقرار رہتی ہے۔ پیک آورز (شام 6 تا 10) میں استری اور موٹر چلانے سے گریز کریں۔",
                fontSize = 11.sp,
                color = SlateDarkBg.copy(alpha = 0.9f),
                lineHeight = 15.sp,
                modifier = Modifier.padding(top = 2.dp)
              )
            }
          }
        }
      }

      // Add Bill Form (Inline collapsible)
      if (showAddDialog) {
        item {
          AddBillCard(
            onSave = { comp, cons, type, amt, units, date ->
              onAddNewBill(comp, cons, type, amt, units, date)
              showAddDialog = false
            },
            onCancel = { showAddDialog = false }
          )
        }
      }

      // 3. Bill Items List
      items(bills, key = { it.id }) { bill ->
        UtilityBillCard(
          bill = bill,
          onTogglePaid = { onTogglePayBill(bill.id) },
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
private fun UtilityBillCard(
  bill: UtilityBill,
  onTogglePaid: () -> Unit,
  onShowToast: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  val iconVector = when {
    bill.billType.contains("بجلی") || bill.billType.contains("Electricity") -> Icons.Default.ElectricBolt
    bill.billType.contains("گیس") || bill.billType.contains("Gas") -> Icons.Default.Propane
    else -> Icons.Default.WaterDrop
  }

  val iconColor = when {
    bill.billType.contains("بجلی") || bill.billType.contains("Electricity") -> AmberDark
    bill.billType.contains("گیس") || bill.billType.contains("Gas") -> Color(0xFFE65100)
    else -> TealPrimary
  }

  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    border = androidx.compose.foundation.BorderStroke(
      1.dp,
      if (bill.isPaid) EmeraldBrand.copy(alpha = 0.5f) else SlateBorder
    ),
    modifier = modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp),
          modifier = Modifier.weight(1f)
        ) {
          Box(
            modifier = Modifier
              .size(40.dp)
              .clip(CircleShape)
              .background(iconColor.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = iconVector,
              contentDescription = null,
              tint = iconColor,
              modifier = Modifier.size(22.dp)
            )
          }

          Column {
            Text(
              text = bill.companyUrdu,
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              color = SlateTextPrimary
            )
            Text(
              text = "${bill.companyName} • Consumer: ${bill.consumerNumber}",
              fontSize = 10.sp,
              color = SlateTextSecondary
            )
            if (bill.unitsConsumed > 0) {
              Text(
                text = "استعمال شدہ یونٹس: ${bill.unitsConsumed} Units",
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = if (bill.unitsConsumed <= 200) EmeraldDark else RoseAlert
              )
            }
          }
        }

        Column(horizontalAlignment = Alignment.End) {
          Text(
            text = "PKR ${String.format("%,d", bill.amount)}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Black,
            color = if (bill.isPaid) EmeraldDark else SlateDarkBg
          )
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Icon(Icons.Default.Schedule, contentDescription = null, tint = SlateTextMuted, modifier = Modifier.size(11.dp))
            Text(
              text = bill.dueDate,
              fontSize = 10.sp,
              color = SlateTextMuted
            )
          }
        }
      }

      if (bill.alertTip.isNotBlank()) {
        Spacer(modifier = Modifier.height(8.dp))
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF1F5F9))
            .padding(horizontal = 8.dp, vertical = 5.dp)
        ) {
          Text(
            text = "💡 ${bill.alertTip}",
            fontSize = 10.sp,
            color = SlateTextSecondary,
            lineHeight = 13.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        if (bill.isPaid) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldBrand, modifier = Modifier.size(16.dp))
            Text(
              text = "ادا شدہ (${bill.paidDate ?: "JazzCash"})",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = EmeraldDark
            )
          }
        } else {
          Text(
            text = "آخری تاریخ سے پہلے ادائیگی کریں",
            fontSize = 11.sp,
            color = RoseAlert,
            fontWeight = FontWeight.Medium
          )
        }

        Button(
          onClick = onTogglePaid,
          shape = RoundedCornerShape(8.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = if (bill.isPaid) Color(0xFFE2E8F0) else EmeraldBrand,
            contentColor = if (bill.isPaid) SlateTextPrimary else Color.White
          ),
          modifier = Modifier.height(34.dp)
        ) {
          Text(
            text = if (bill.isPaid) "غیر ادا شدہ کریں" else "بل ادا کریں ✓",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }
  }
}

@Composable
private fun AddBillCard(
  onSave: (String, String, String, Long, Int, String) -> Unit,
  onCancel: () -> Unit,
  modifier: Modifier = Modifier,
) {
  var company by remember { mutableStateOf("K-Electric") }
  var consumerNo by remember { mutableStateOf("") }
  var amountText by remember { mutableStateOf("") }
  var unitsText by remember { mutableStateOf("") }
  var dueDate by remember { mutableStateOf("28 مارچ 2025") }

  Card(
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = androidx.compose.foundation.BorderStroke(1.5.dp, TealPrimary),
    modifier = modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Text(
        text = "نیا بل شامل کریں (Add Utility Bill)",
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = SlateTextPrimary
      )

      Spacer(modifier = Modifier.height(10.dp))

      OutlinedTextField(
        value = company,
        onValueChange = { company = it },
        label = { Text("ادارہ / کمپنی (e.g., K-Electric, SSGC, KW&SB)") },
        modifier = Modifier.fillMaxWidth()
      )

      Spacer(modifier = Modifier.height(8.dp))

      OutlinedTextField(
        value = consumerNo,
        onValueChange = { consumerNo = it },
        label = { Text("کنزیومر یا ریفرنس نمبر (Consumer No)") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth()
      )

      Spacer(modifier = Modifier.height(8.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        OutlinedTextField(
          value = amountText,
          onValueChange = { amountText = it },
          label = { Text("رقم (PKR)") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          modifier = Modifier.weight(1f)
        )

        OutlinedTextField(
          value = unitsText,
          onValueChange = { unitsText = it },
          label = { Text("یونٹس (Units)") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          modifier = Modifier.weight(1f)
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      OutlinedTextField(
        value = dueDate,
        onValueChange = { dueDate = it },
        label = { Text("آخری تاریخ (Due Date)") },
        modifier = Modifier.fillMaxWidth()
      )

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
            val amt = amountText.toLongOrNull() ?: 0L
            val units = unitsText.toIntOrNull() ?: 0
            if (amt > 0 && consumerNo.isNotBlank()) {
              onSave(company, consumerNo, company, amt, units, dueDate)
            }
          },
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
          modifier = Modifier.weight(1f)
        ) {
          Text("بل شامل کریں", fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}
