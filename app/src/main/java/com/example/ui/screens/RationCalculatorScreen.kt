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
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.RationItemEstimate
import com.example.ui.theme.AmberBrand
import com.example.ui.theme.AmberDark
import com.example.ui.theme.AmberLight
import com.example.ui.theme.EmeraldBrand
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.EmeraldSurface
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
fun RationCalculatorScreen(
  rationItems: List<RationItemEstimate>,
  onBack: () -> Unit,
  onSaveToRationEnvelope: (Long) -> Unit,
  onShowToast: (String) -> Unit,
  onPlayVoice: (String, String) -> Unit,
  modifier: Modifier = Modifier,
) {
  // Map of item ID to multiplier (default 1)
  val itemQuantities = remember {
    mutableStateMapOf<String, Int>().apply {
      rationItems.forEach { item ->
        put(item.id, if (item.isEssential) 1 else 0)
      }
    }
  }

  val totalEstimatedBudget = rationItems.sumOf { item ->
    val qty = itemQuantities[item.id] ?: 0
    item.unitPriceEstimate * qty
  }

  val selectedCount = itemQuantities.values.count { it > 0 }

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
          modifier = Modifier.testTag("ration_calc_back_btn")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = SlateTextPrimary
          )
        }

        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "Smart Ration & Grocery Planner",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = SlateTextPrimary
          )
          Text(
            text = "ماہانہ سستا راشن و گھریلو تخمینہ",
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
                "Ration calculator aap ko aatey, ghee aur daalon ke sastey tareen rate faraham karta hai. Apni family ki zaroorat mutabiq tadaad select karein.",
                "Ration calculator se apna mahana budget calculate karein."
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
      }
    }

    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 14.dp, vertical = 12.dp)
        .testTag("ration_calc_scroll_list"),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      // 1. Live Budget Calculator Summary
      item {
        Card(
          shape = RoundedCornerShape(22.dp),
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
              Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(Icons.Default.Calculate, contentDescription = null, tint = AmberBrand, modifier = Modifier.size(18.dp))
                Text(
                  text = "کل تخمینہ راشن (Estimated Total)",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White.copy(alpha = 0.9f)
                )
              }

              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(6.dp))
                  .background(TealDark)
                  .padding(horizontal = 8.dp, vertical = 2.dp)
              ) {
                Text(
                  text = "$selectedCount اشیاء شامل",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  color = EmeraldBrand
                )
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
              text = "PKR ${String.format("%,d", totalEstimatedBudget)}",
              fontSize = 28.sp,
              fontWeight = FontWeight.Black,
              color = Color.White
            )

            Text(
              text = "آٹا راشن لفافے کا تجویز کردہ ماہانہ ہدف: PKR 20,000",
              fontSize = 11.sp,
              color = Color.White.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
              onClick = {
                onSaveToRationEnvelope(totalEstimatedBudget)
                onShowToast("Ration budget Rs. ${String.format("%,d", totalEstimatedBudget)} synced with Household Needs envelope!")
              },
              shape = RoundedCornerShape(10.dp),
              colors = ButtonDefaults.buttonColors(containerColor = AmberBrand),
              modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .testTag("apply_ration_budget_btn")
            ) {
              Icon(Icons.Default.Check, contentDescription = null, tint = SlateDarkBg, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("یہ رقم بجٹ لفافے میں سیٹ کریں", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SlateDarkBg)
            }
          }
        }
      }

      // 2. Utility Store & Mandi Savings Tip Banner
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
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "سستا راشن ٹپ (Wholesale & Mandi Guide)",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = AmberDark
              )
              Text(
                text = "تنخواہ ملتے ہی پہلے 3 دن میں پورے مہینے کا گھی، آٹا اور چاول اکٹھا غلہ منڈی یا یوٹیلیٹی اسٹور سے خریدنے پر 1,800 سے 2,500 روپے کی خالص بچت ہوتی ہے۔",
                fontSize = 11.sp,
                color = SlateDarkBg,
                lineHeight = 15.sp
              )
            }
          }
        }
      }

      // 3. Section Title
      item {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 2.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "ضروری گھریلو اشیاء (Grocery Items)",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = SlateTextPrimary
          )
          Text(
            text = "تعداد منتخب کریں",
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = SlateTextSecondary
          )
        }
      }

      // 4. Ration Item Cards
      items(rationItems, key = { it.id }) { item ->
        val currentQty = itemQuantities[item.id] ?: 0
        RationItemRow(
          item = item,
          quantity = currentQty,
          onQuantityChange = { newQty ->
            itemQuantities[item.id] = newQty
          }
        )
      }

      item {
        Spacer(modifier = Modifier.height(20.dp))
      }
    }
  }
}

@Composable
private fun RationItemRow(
  item: RationItemEstimate,
  quantity: Int,
  onQuantityChange: (Int) -> Unit,
) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = androidx.compose.foundation.BorderStroke(
      1.dp,
      if (quantity > 0) TealBorder else SlateBorder
    ),
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
            text = item.nameUrdu,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = SlateTextPrimary
          )
          Text(
            text = "${item.nameEnglish} • ${item.defaultQty}",
            fontSize = 11.sp,
            color = SlateTextSecondary
          )
        }

        // Stepper Controls
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Box(
            modifier = Modifier
              .size(32.dp)
              .clip(CircleShape)
              .background(if (quantity > 0) TealSurface else Color(0xFFF1F5F9))
              .clickable(enabled = quantity > 0) { onQuantityChange(quantity - 1) },
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Remove,
              contentDescription = "Decrease",
              tint = if (quantity > 0) TealPrimary else Color.LightGray,
              modifier = Modifier.size(16.dp)
            )
          }

          Text(
            text = "$quantity",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = if (quantity > 0) TealDarker else SlateTextSecondary
          )

          Box(
            modifier = Modifier
              .size(32.dp)
              .clip(CircleShape)
              .background(TealPrimary)
              .clickable { onQuantityChange(quantity + 1) },
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Add,
              contentDescription = "Increase",
              tint = Color.White,
              modifier = Modifier.size(16.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
          Icon(Icons.Default.LocalOffer, contentDescription = null, tint = EmeraldDark, modifier = Modifier.size(12.dp))
          Text(
            text = "تخمینہ ریٹ: PKR ${String.format("%,d", item.unitPriceEstimate)}",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = EmeraldDark
          )
        }

        Text(
          text = "مارکیٹ: ${item.marketPriceRange}",
          fontSize = 10.sp,
          color = SlateTextSecondary
        )
      }

      if (item.savingsTip.isNotBlank()) {
        Spacer(modifier = Modifier.height(6.dp))
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(EmeraldSurface.copy(alpha = 0.6f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Text(
            text = "💡 ${item.savingsTip}",
            fontSize = 10.sp,
            color = EmeraldDark,
            lineHeight = 13.sp
          )
        }
      }
    }
  }
}
