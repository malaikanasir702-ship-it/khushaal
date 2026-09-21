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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.PhoneIphone
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.AppDestination
import com.example.model.UserProfile
import com.example.ui.theme.AmberBrand
import com.example.ui.theme.AmberDark
import com.example.ui.theme.AmberLight
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.EmeraldSurface
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateDarkBg
import com.example.ui.theme.SlateTextPrimary
import com.example.ui.theme.SlateTextSecondary
import com.example.ui.theme.TealDark
import com.example.ui.theme.TealDarker
import com.example.ui.theme.TealPrimary

@Composable
fun ProfileSettingsScreen(
  userProfile: UserProfile,
  onBack: () -> Unit,
  onShowToast: (String) -> Unit,
  onPlayVoice: (String, String) -> Unit,
  onLogout: () -> Unit = {},
  onUpdateProfile: ((com.example.data.api.UpdateProfileRequest) -> Unit)? = null,
  factoryName: String = "Factory Welfare",
  welfareHelpline: String = "0800-00000",
  welfareHelplineLabel: String = "Welfare Desk",
  modifier: Modifier = Modifier,
  onNavigateToDestination: (AppDestination) -> Unit = {},
) {
  var voiceGuidanceEnabled by remember { mutableStateOf(true) }
  var fraudAlertsEnabled by remember { mutableStateOf(true) }
  var showLogoutDialog by remember { mutableStateOf(false) }
  var showEditDialog by remember { mutableStateOf(false) }

  var editName by remember(userProfile.name) { mutableStateOf(userProfile.name) }
  var editFactory by remember(userProfile.organization) { mutableStateOf(userProfile.organization) }
  var editJazzCash by remember(userProfile.jazzCashNumber) { mutableStateOf(userProfile.jazzCashNumber) }
  var editBankName by remember(userProfile.bankName) { mutableStateOf(userProfile.bankName) }
  var editBankAccount by remember(userProfile.bankAccountNumber) { mutableStateOf(userProfile.bankAccountNumber) }

  // Edit Profile Dialog
  if (showEditDialog) {
    AlertDialog(
      onDismissRequest = { showEditDialog = false },
      title = {
        Text("اکاؤنٹ تبدیل کریں / Edit Account", fontWeight = FontWeight.Bold, color = SlateTextPrimary)
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          androidx.compose.material3.OutlinedTextField(
            value = editName,
            onValueChange = { editName = it },
            label = { Text("نام / Full Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
          androidx.compose.material3.OutlinedTextField(
            value = editFactory,
            onValueChange = { editFactory = it },
            label = { Text("فیکٹری یا ادارہ / Factory") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
          androidx.compose.material3.OutlinedTextField(
            value = editJazzCash,
            onValueChange = { editJazzCash = it },
            label = { Text("جاز کیش / ایزی پیسہ نمبر") },
            placeholder = { Text("03001234567") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
          androidx.compose.material3.OutlinedTextField(
            value = editBankName,
            onValueChange = { editBankName = it },
            label = { Text("بینک کا نام / Bank Name") },
            placeholder = { Text("HBL / Meezan / UBL") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
          androidx.compose.material3.OutlinedTextField(
            value = editBankAccount,
            onValueChange = { editBankAccount = it },
            label = { Text("بینک اکاؤنٹ / IBAN نمبر") },
            placeholder = { Text("PK64HABB...") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            showEditDialog = false
            onUpdateProfile?.invoke(
              com.example.data.api.UpdateProfileRequest(
                name = editName.trim(),
                factory = editFactory.trim(),
                jazzCashNumber = editJazzCash.trim(),
                bankName = editBankName.trim(),
                bankAccountNumber = editBankAccount.trim()
              )
            )
            onShowToast("پروفائل کامیابی سے اپ ڈیٹ ہو گیا / Profile updated")
          },
          colors = ButtonDefaults.buttonColors(containerColor = TealPrimary)
        ) {
          Text("محفوظ کریں / Save", fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        OutlinedButton(onClick = { showEditDialog = false }) {
          Text("منسوخ کریں")
        }
      }
    )
  }

  // Logout confirmation dialog
  if (showLogoutDialog) {
    AlertDialog(
      onDismissRequest = { showLogoutDialog = false },
      title = {
        Text("Logout / لاگ آؤٹ", fontWeight = FontWeight.Bold, color = SlateTextPrimary)
      },
      text = {
        Text(
          "کیا آپ واقعی لاگ آؤٹ کرنا چاہتے ہیں؟\nAre you sure you want to logout?",
          color = SlateTextSecondary
        )
      },
      confirmButton = {
        Button(
          onClick = {
            showLogoutDialog = false
            onLogout()
          },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))
        ) {
          Text("ہاں، لاگ آؤٹ", fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        OutlinedButton(onClick = { showLogoutDialog = false }) {
          Text("منسوخ کریں")
        }
      }
    )
  }

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
          modifier = Modifier.testTag("profile_back_btn")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = SlateTextPrimary
          )
        }
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "Worker Profile & Factory Settings",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = SlateTextPrimary
          )
          Text(
            text = "ورکر پروفائل و ترتیبات",
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
                "Profile page par aap ki factory ID, JazzCash account aur awaz ki setting mojood hai.",
                "Profile page par aap ki ID aur setting mojood hai."
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

    // Scrollable content
    LazyColumn(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f)
        .padding(horizontal = 14.dp, vertical = 12.dp)
        .testTag("profile_settings_scroll"),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // Official Mill Worker ID Card
      item {
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = Color.Transparent),
          elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .background(Brush.linearGradient(colors = listOf(TealDarker, TealDark)))
              .padding(16.dp)
          ) {
            Column {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = if (userProfile.organization.isNotBlank()) userProfile.organization.uppercase() else "FACTORY WORKER",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = AmberBrand,
                  letterSpacing = 1.sp
                )
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(6.dp))
                      .background(AmberBrand.copy(alpha = 0.2f))
                      .clickable { showEditDialog = true }
                      .padding(horizontal = 6.dp, vertical = 2.dp)
                  ) {
                    Text("ترمیم کریں ✎", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = AmberBrand)
                  }
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(6.dp))
                      .background(EmeraldDark)
                      .padding(horizontal = 6.dp, vertical = 2.dp)
                  ) {
                    Text("مصدقہ ملازم ✓", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                  }
                }
              }

              Spacer(modifier = Modifier.height(14.dp))

              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
              ) {
                AsyncImage(
                  model = userProfile.avatarUrl.ifBlank { "https://www.gstatic.com/labs-code/stitch/stitch-placeholder-300x300.svg" },
                  contentDescription = "Worker Photo",
                  modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .border(2.dp, AmberBrand, CircleShape)
                )
                Column {
                  Text(
                    text = userProfile.name.ifBlank { "Worker" } + (if (userProfile.urduName.isNotBlank() && userProfile.urduName != userProfile.name) " (${userProfile.urduName})" else ""),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                  )
                  Text("Employee ID: ${userProfile.factoryId.ifBlank { "N/A" }}", fontSize = 12.sp, color = Color.White.copy(alpha = 0.85f))
                  Text("CNIC: ${userProfile.cnicMasked.ifBlank { userProfile.rawCnic.ifBlank { "N/A" } }}", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                }
              }

              Spacer(modifier = Modifier.height(12.dp))

              Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                  modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White.copy(alpha = 0.15f))
                    .clickable { onNavigateToDestination(AppDestination.FactorySalarySlip) }
                    .padding(vertical = 6.dp, horizontal = 8.dp),
                  contentAlignment = Alignment.Center
                ) {
                  Text("ماہانہ تنخواہ سلپ دیکھیں →", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = AmberBrand)
                }
                Box(
                  modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White.copy(alpha = 0.15f))
                    .clickable { onNavigateToDestination(AppDestination.RationCalculator) }
                    .padding(vertical = 6.dp, horizontal = 8.dp),
                  contentAlignment = Alignment.Center
                ) {
                  Text("راشن کیلکولیٹر کھولیں →", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
              }

              Spacer(modifier = Modifier.height(6.dp))

              Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                  modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White.copy(alpha = 0.15f))
                    .clickable { onNavigateToDestination(AppDestination.UtilityBills) }
                    .padding(vertical = 6.dp, horizontal = 8.dp),
                  contentAlignment = Alignment.Center
                ) {
                  Text("بجلی و گیس بلز →", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
                Box(
                  modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White.copy(alpha = 0.15f))
                    .clickable { onNavigateToDestination(AppDestination.DebtSnowball) }
                    .padding(vertical = 6.dp, horizontal = 8.dp),
                  contentAlignment = Alignment.Center
                ) {
                  Text("قرض نجات اسنو بال →", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = AmberBrand)
                }
              }
            }
          }
        }
      }

      // Linked Financial Wallets
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
                text = "Linked Financial Accounts (منسلک اکاؤنٹس)",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = SlateTextPrimary
              )
              Text(
                text = "تبدیل کریں ✎",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TealPrimary,
                modifier = Modifier
                  .clip(RoundedCornerShape(6.dp))
                  .clickable { showEditDialog = true }
                  .padding(4.dp)
              )
            }
            Spacer(modifier = Modifier.height(10.dp))

            // JazzCash / Mobile Wallet
            val jazzCashDisplay = userProfile.jazzCashNumber.ifBlank {
              if (userProfile.paymentAccount.contains("03")) userProfile.paymentAccount else userProfile.phoneMasked
            }
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFF8FAFC))
                .padding(10.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.PhoneIphone, contentDescription = null, tint = AmberDark)
                Column {
                  Text("JazzCash / Mobile Wallet", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SlateTextPrimary)
                  Text(jazzCashDisplay.ifBlank { "کوئی نمبر درج نہیں" }, fontSize = 10.sp, color = SlateTextSecondary)
                }
              }
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(4.dp))
                  .background(if (jazzCashDisplay.isNotBlank()) EmeraldLight else Color(0xFFF1F5F9))
                  .padding(horizontal = 6.dp, vertical = 2.dp)
              ) {
                Text(
                  if (jazzCashDisplay.isNotBlank()) "منسلک ہے" else "غیر منسلک",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (jazzCashDisplay.isNotBlank()) EmeraldDark else SlateTextSecondary
                )
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Bank Account
            val bankTitle = userProfile.bankName.ifBlank { "Bank Payroll Account" }
            val bankAccDisplay = userProfile.bankAccountNumber
            val hasBank = userProfile.bankName.isNotBlank() || userProfile.bankAccountNumber.isNotBlank()
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFF8FAFC))
                .padding(10.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.AccountBalance, contentDescription = null, tint = TealPrimary)
                Column {
                  Text(bankTitle, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SlateTextPrimary)
                  Text(
                    if (hasBank) bankAccDisplay.ifBlank { "اکاؤنٹ منسلک ہے" } else "کوئی بینک اکاؤنٹ منسلک نہیں",
                    fontSize = 10.sp,
                    color = SlateTextSecondary
                  )
                }
              }
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(4.dp))
                  .background(if (hasBank) EmeraldLight else AmberLight)
                  .clickable { showEditDialog = true }
                  .padding(horizontal = 6.dp, vertical = 2.dp)
              ) {
                Text(
                  if (hasBank) "تنخواہ اکاؤنٹ" else "+ اکاؤنٹ شامل کریں",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (hasBank) EmeraldDark else AmberDark
                )
              }
            }
          }
        }
      }

      // Preferences & Security
      item {
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Text(
              text = "Preferences & Security (ترتیبات)",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = SlateTextPrimary
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Text("آواز و اردو رہنمائی (Urdu TTS Guidance)", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = SlateTextPrimary)
                Text("ہر صفحے اور کارڈ پر آواز کی مدد", fontSize = 10.sp, color = SlateTextSecondary)
              }
              Switch(
                checked = voiceGuidanceEnabled,
                onCheckedChange = { voiceGuidanceEnabled = it; onShowToast("Voice preference updated.") },
                colors = SwitchDefaults.colors(checkedThumbColor = TealPrimary)
              )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Text("فراڈ سے بچاؤ الرٹس (Fraud SMS Warning)", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = SlateTextPrimary)
                Text("مشکوک کال یا میسج پر انتباہ", fontSize = 10.sp, color = SlateTextSecondary)
              }
              Switch(
                checked = fraudAlertsEnabled,
                onCheckedChange = { fraudAlertsEnabled = it; onShowToast("Fraud shield alerts active.") },
                colors = SwitchDefaults.colors(checkedThumbColor = TealPrimary)
              )
            }
          }
        }
      }

      // Emergency Helpline
      item {
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = EmeraldSurface),
          border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldLight),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text("فیکٹری ویلفیئر ہیلپ لائن", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = EmeraldDark)
              Text("ٹول فری: $welfareHelpline ($welfareHelplineLabel)", fontSize = 11.sp, color = SlateDarkBg)
            }
            Button(
              onClick = { onShowToast("Calling $welfareHelplineLabel: $welfareHelpline") },
              shape = RoundedCornerShape(8.dp),
              colors = ButtonDefaults.buttonColors(containerColor = EmeraldDark)
            ) {
              Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("کال کریں", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
          }
        }
      }

      item { Spacer(modifier = Modifier.height(8.dp)) }
    }

    // ─── LOGOUT BUTTON — always visible at bottom ───
    OutlinedButton(
      onClick = { showLogoutDialog = true },
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 14.dp, vertical = 12.dp)
        .testTag("logout_btn"),
      shape = RoundedCornerShape(12.dp),
      colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFE53935)),
      border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFE53935))
    ) {
      Icon(
        imageVector = Icons.Default.ExitToApp,
        contentDescription = "Logout",
        tint = Color(0xFFE53935),
        modifier = Modifier.size(20.dp)
      )
      Spacer(modifier = Modifier.width(8.dp))
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Logout", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE53935))
        Text("لاگ آؤٹ", fontSize = 11.sp, color = Color(0xFFE53935))
      }
    }
  }
}
