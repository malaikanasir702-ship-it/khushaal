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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
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
import com.example.model.AppLanguage
import com.example.ui.theme.AmberDark
import com.example.ui.theme.EmeraldBrand
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldSurface
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateDarkBg
import com.example.ui.theme.SlateTextMuted
import com.example.ui.theme.SlateTextPrimary
import com.example.ui.theme.SlateTextSecondary
import com.example.ui.theme.TealDark
import com.example.ui.theme.TealDarker
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.TealSurface

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSelectionBottomSheet(
  isOpen: Boolean,
  currentLanguage: AppLanguage,
  onLanguageSelected: (AppLanguage) -> Unit,
  onDismiss: () -> Unit,
) {
  if (!isOpen) return

  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = Color.White,
    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Box(
            modifier = Modifier
              .size(34.dp)
              .clip(CircleShape)
              .background(TealSurface),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Language,
              contentDescription = "Language",
              tint = TealPrimary,
              modifier = Modifier.size(18.dp)
            )
          }

          Column {
            Text(
              text = "زبان منتخب کریں • Choose Language",
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = SlateTextPrimary
            )
            Text(
              text = "Choose your preferred language for the app",
              fontSize = 11.sp,
              color = SlateTextSecondary
            )
          }
        }

        IconButton(onClick = onDismiss) {
          Icon(Icons.Default.Close, contentDescription = "Close", tint = SlateTextMuted)
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Language Options
      LanguageOptionCard(
        title = "اردو (مکمل اردو)",
        subtitle = "تمام اسکرینز، رہنمائی، بٹنز اور اعداد و شمار خالص اردو میں",
        flagEmoji = "🇵🇰",
        isSelected = currentLanguage == AppLanguage.URDU,
        onClick = {
          onLanguageSelected(AppLanguage.URDU)
          onDismiss()
        }
      )

      Spacer(modifier = Modifier.height(10.dp))

      LanguageOptionCard(
        title = "Urdu + English (دو لسانی / Dual Mode)",
        subtitle = "Urdu text with standard English labels for factory terminology",
        flagEmoji = "🌐",
        isSelected = currentLanguage == AppLanguage.BILINGUAL,
        onClick = {
          onLanguageSelected(AppLanguage.BILINGUAL)
          onDismiss()
        }
      )

      Spacer(modifier = Modifier.height(10.dp))

      LanguageOptionCard(
        title = "English (Only English)",
        subtitle = "Full English interface for easy international browsing",
        flagEmoji = "🇬🇧",
        isSelected = currentLanguage == AppLanguage.ENGLISH,
        onClick = {
          onLanguageSelected(AppLanguage.ENGLISH)
          onDismiss()
        }
      )

      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}

@Composable
private fun LanguageOptionCard(
  title: String,
  subtitle: String,
  flagEmoji: String,
  isSelected: Boolean,
  onClick: () -> Unit,
) {
  Surface(
    shape = RoundedCornerShape(16.dp),
    color = if (isSelected) TealSurface else Color(0xFFF8FAFC),
    border = androidx.compose.foundation.BorderStroke(
      1.5.dp,
      if (isSelected) TealPrimary else SlateBorder
    ),
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() }
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.weight(1f)
      ) {
        Text(text = flagEmoji, fontSize = 22.sp)

        Column {
          Text(
            text = title,
            fontSize = 13.5.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) TealDarker else SlateTextPrimary
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = subtitle,
            fontSize = 10.5.sp,
            color = SlateTextSecondary,
            lineHeight = 14.sp
          )
        }
      }

      RadioButton(
        selected = isSelected,
        onClick = onClick,
        colors = RadioButtonDefaults.colors(
          selectedColor = TealPrimary,
          unselectedColor = SlateTextMuted
        )
      )
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickExpenseBottomSheet(
  isOpen: Boolean,
  onDismiss: () -> Unit,
  onLogExpense: (String, Long) -> Unit,
) {
  if (!isOpen) return

  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  var expenseName by remember { mutableStateOf("") }
  var expenseAmountText by remember { mutableStateOf("") }
  var selectedCategory by remember { mutableStateOf("ضروری راشن") }

  val presetCategories = listOf(
    "ضروری راشن و آٹا",
    "سبزی و گوشت",
    "بائیک پیٹرول",
    "ڈاکٹر و دوا",
    "بجلی و گیس بل",
    "بچوں کی پاکٹ منی"
  )

  val presetAmounts = listOf(100L, 250L, 500L, 1000L, 2000L, 5000L)

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = Color.White,
    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Box(
            modifier = Modifier
              .size(34.dp)
              .clip(CircleShape)
              .background(EmeraldSurface),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Payments,
              contentDescription = "Log Expense",
              tint = EmeraldDark,
              modifier = Modifier.size(18.dp)
            )
          }

          Column {
            Text(
              text = "فوری خرچہ درج کریں • Quick Expense",
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = SlateTextPrimary
            )
            Text(
              text = "روزانہ اخراجات خودکار لفافے سے منہا ہوں گے",
              fontSize = 11.sp,
              color = SlateTextSecondary
            )
          }
        }

        IconButton(onClick = onDismiss) {
          Icon(Icons.Default.Close, contentDescription = "Close", tint = SlateTextMuted)
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Category chips
      Text(
        text = "مقبول مدات (Common Categories):",
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        color = SlateTextSecondary
      )
      Spacer(modifier = Modifier.height(6.dp))

      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        items(presetCategories) { cat ->
          val isSelected = selectedCategory == cat
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(12.dp))
              .background(if (isSelected) TealPrimary else Color(0xFFF1F5F9))
              .border(1.dp, if (isSelected) TealDark else SlateBorder, RoundedCornerShape(12.dp))
              .clickable {
                selectedCategory = cat
                expenseName = cat
              }
              .padding(horizontal = 10.dp, vertical = 6.dp)
          ) {
            Text(
              text = cat,
              fontSize = 11.sp,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
              color = if (isSelected) Color.White else SlateTextPrimary
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Text field for custom name
      OutlinedTextField(
        value = expenseName,
        onValueChange = { expenseName = it },
        label = { Text("تفصیل (مثلاً دودھ دہی، رکشہ کرایہ)", fontSize = 12.sp) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = TealPrimary,
          unfocusedBorderColor = SlateBorder
        )
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Text field for Amount
      OutlinedTextField(
        value = expenseAmountText,
        onValueChange = { expenseAmountText = it.filter { ch -> ch.isDigit() } },
        label = { Text("رقم روپے میں (Amount in PKR)", fontSize = 12.sp) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        prefix = { Text("Rs. ", fontWeight = FontWeight.Bold, color = SlateDarkBg) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = EmeraldDark,
          unfocusedBorderColor = SlateBorder
        )
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Preset amount buttons
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        items(presetAmounts) { amt ->
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(10.dp))
              .background(Color(0xFFF1F5F9))
              .border(1.dp, SlateBorder, RoundedCornerShape(10.dp))
              .clickable {
                expenseAmountText = amt.toString()
              }
              .padding(horizontal = 10.dp, vertical = 6.dp)
          ) {
            Text(
              text = "+Rs. $amt",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = TealDarker
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      Button(
        onClick = {
          val amt = expenseAmountText.toLongOrNull() ?: 0L
          if (amt > 0) {
            val name = expenseName.ifBlank { selectedCategory }
            onLogExpense(name, amt)
            onDismiss()
          }
        },
        enabled = (expenseAmountText.toLongOrNull() ?: 0L) > 0,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = EmeraldDark),
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
          .testTag("submit_quick_expense_btn")
      ) {
        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "کھاتے میں درج کریں (Save Expense)",
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold
        )
      }

      Spacer(modifier = Modifier.height(20.dp))
    }
  }
}
