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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.filled.Share
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
import com.example.model.CustomerOrder
import com.example.ui.theme.AmberBrand
import com.example.ui.theme.AmberDark
import com.example.ui.theme.AmberLight
import com.example.ui.theme.AmberSurface
import com.example.ui.theme.EmeraldBrand
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.EmeraldSurface
import com.example.ui.theme.RoseAlert
import com.example.ui.theme.RoseLight
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
fun BusinessKhataScreen(
  orders: List<CustomerOrder>,
  onBack: () -> Unit,
  onToggleDelivered: (String) -> Unit,
  onTogglePaid: (String) -> Unit,
  onAddNewOrder: (String, String, String, Long, Long, String) -> Unit,
  onPlayVoice: (String, String) -> Unit,
  modifier: Modifier = Modifier,
) {
  var showAddOrderDialog by remember { mutableStateOf(false) }

  val totalRevenue = orders.sumOf { it.totalAmount }
  val pendingReceivables = orders.filter { !it.isFullyPaid }.sumOf { it.totalAmount - it.advancePaid }

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
          modifier = Modifier.testTag("khata_back_btn")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = SlateTextPrimary
          )
        }

        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "Business Khata & Order Book",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = SlateTextPrimary
          )
          Text(
            text = "کاروباری کھاتہ و کسٹمر رجسٹر",
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
                "Karobari Khata page par aap apnay gahak aur orders ka hisab kitab darj kar saktay hain. Har order ki delivery aur baqaya raqam track karein.",
                "Karobari khata par gahak aur orders ka hisab darj hai."
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
        .testTag("business_khata_scroll"),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // Metric Summary Cards
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = EmeraldSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldLight),
            modifier = Modifier.weight(1f)
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              Text("کل آرڈرز ویلیو", fontSize = 10.sp, color = EmeraldDark, fontWeight = FontWeight.Bold)
              Text("Rs. ${String.format("%,d", totalRevenue)}", fontSize = 15.sp, fontWeight = FontWeight.Black, color = SlateDarkBg)
            }
          }

          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = AmberSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, AmberLight),
            modifier = Modifier.weight(1f)
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              Text("بقایا وصولی (Receivables)", fontSize = 10.sp, color = AmberDark, fontWeight = FontWeight.Bold)
              Text("Rs. ${String.format("%,d", pendingReceivables)}", fontSize = 15.sp, fontWeight = FontWeight.Black, color = AmberDark)
            }
          }
        }
      }

      // Add New Customer Order Action
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Customer Orders (${orders.size})",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = SlateTextPrimary
          )

          Button(
            onClick = { showAddOrderDialog = true },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
            modifier = Modifier.height(32.dp)
          ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(3.dp))
            Text("نیا آرڈر لکھیں", fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }
        }
      }

      // Customer Orders List
      items(orders) { ord ->
        val remaining = ord.totalAmount - ord.advancePaid
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
                  text = ord.customerName,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  color = SlateTextPrimary
                )
                Text(
                  text = "آرڈر: ${ord.serviceTitle}",
                  fontSize = 11.sp,
                  color = TealDarker,
                  fontWeight = FontWeight.Medium
                )
                Text(
                  text = "رابطہ: ${ord.phone} • تاریخ: ${ord.dueDate}",
                  fontSize = 10.sp,
                  color = SlateTextSecondary
                )
              }

              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .background(if (ord.isDelivered) EmeraldLight else AmberLight)
                  .padding(horizontal = 8.dp, vertical = 3.dp)
              ) {
                Text(
                  text = if (ord.isDelivered) "ڈلیور شدہ ✓" else "جاری ہے (In Progress)",
                  fontSize = 9.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (ord.isDelivered) EmeraldDark else AmberDark
                )
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Money details
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF8FAFC), RoundedCornerShape(10.dp))
                .padding(8.dp),
              horizontalArrangement = Arrangement.SpaceAround
            ) {
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("کل رقم", fontSize = 9.sp, color = SlateTextMuted)
                Text("Rs. ${String.format("%,d", ord.totalAmount)}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SlateTextPrimary)
              }
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("ایڈوانس", fontSize = 9.sp, color = SlateTextMuted)
                Text("Rs. ${String.format("%,d", ord.advancePaid)}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EmeraldDark)
              }
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("بقایا رقم", fontSize = 9.sp, color = SlateTextMuted)
                Text("Rs. ${String.format("%,d", remaining)}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (remaining > 0) RoseAlert else EmeraldDark)
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action toggles
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Button(
                onClick = { onToggleDelivered(ord.id) },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                  containerColor = if (ord.isDelivered) SlateBorder else TealSurface,
                  contentColor = if (ord.isDelivered) SlateTextPrimary else TealDark
                ),
                modifier = Modifier.height(30.dp)
              ) {
                Text(if (ord.isDelivered) "Undo Delivery" else "Mark Delivered", fontSize = 10.sp, fontWeight = FontWeight.Bold)
              }

              Button(
                onClick = { onTogglePaid(ord.id) },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                  containerColor = if (ord.isFullyPaid) EmeraldLight else AmberLight,
                  contentColor = if (ord.isFullyPaid) EmeraldDark else AmberDark
                ),
                modifier = Modifier.height(30.dp)
              ) {
                Text(if (ord.isFullyPaid) "مکمل وصول شدہ ✓" else "بقایا وصول کریں", fontSize = 10.sp, fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }

      // Golden Rule of Business Separation Card
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = SlateDarkBg),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Text(
              text = "کاروباری کھاتہ کا بنیادی اصول",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = AmberBrand
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "سلائی یا ہنر کی آمدنی کو گھر کے راشن کے پیسوں سے الگ رکھیں۔ جب ماہانہ منافع کا حساب ہو جائے تو صرف طے شدہ حصہ گھر کے خرچ میں ڈالیں۔",
              fontSize = 10.sp,
              lineHeight = 15.sp,
              color = Color.White.copy(alpha = 0.9f)
            )
          }
        }
      }

      item {
        Spacer(modifier = Modifier.height(16.dp))
      }
    }
  }

  // Dialog to Add Customer Order
  if (showAddOrderDialog) {
    var cName by remember { mutableStateOf("") }
    var cPhone by remember { mutableStateOf("") }
    var cService by remember { mutableStateOf("") }
    var cTotal by remember { mutableStateOf("1500") }
    var cAdvance by remember { mutableStateOf("500") }
    var cDueDate by remember { mutableStateOf("28 Mar 2025") }

    Dialog(onDismissRequest = { showAddOrderDialog = false }) {
      Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        modifier = Modifier.padding(8.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text("نیا کسٹمر آرڈر درج کریں", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = SlateTextPrimary)
          Spacer(modifier = Modifier.height(10.dp))
          OutlinedTextField(
            value = cName,
            onValueChange = { cName = it },
            label = { Text("گاہک کا نام (Customer Name)") },
            modifier = Modifier.fillMaxWidth()
          )
          Spacer(modifier = Modifier.height(6.dp))
          OutlinedTextField(
            value = cPhone,
            onValueChange = { cPhone = it },
            label = { Text("فون نمبر (WhatsApp / Call)") },
            modifier = Modifier.fillMaxWidth()
          )
          Spacer(modifier = Modifier.height(6.dp))
          OutlinedTextField(
            value = cService,
            onValueChange = { cService = it },
            label = { Text("آرڈر کی تفصیل (مثلاً: 2 سوٹ سلائی)") },
            modifier = Modifier.fillMaxWidth()
          )
          Spacer(modifier = Modifier.height(6.dp))
          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            OutlinedTextField(
              value = cTotal,
              onValueChange = { cTotal = it },
              label = { Text("کل رقم") },
              modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
              value = cAdvance,
              onValueChange = { cAdvance = it },
              label = { Text("ایڈوانس") },
              modifier = Modifier.weight(1f)
            )
          }
          Spacer(modifier = Modifier.height(14.dp))
          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Button(
              onClick = { showAddOrderDialog = false },
              colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
            ) {
              Text("منسوخ", color = Color.Black)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
              onClick = {
                if (cName.isNotBlank() && cService.isNotBlank()) {
                  onAddNewOrder(
                    cName,
                    cPhone.ifBlank { "0300-0000000" },
                    cService,
                    cTotal.toLongOrNull() ?: 1500L,
                    cAdvance.toLongOrNull() ?: 500L,
                    cDueDate
                  )
                  showAddOrderDialog = false
                }
              },
              colors = ButtonDefaults.buttonColors(containerColor = TealPrimary)
            ) {
              Text("کھاتہ میں شامل کریں")
            }
          }
        }
      }
    }
  }
}
