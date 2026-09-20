package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Adjust
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppTab
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateTextMuted
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.TealSurface

@Composable
fun KhushhaalBottomNav(
  currentTab: AppTab,
  onTabSelected: (AppTab) -> Unit,
  modifier: Modifier = Modifier,
) {
  Surface(
    modifier = modifier.fillMaxWidth(),
    color = Color.White.copy(alpha = 0.98f),
    shadowElevation = 12.dp,
    border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder.copy(alpha = 0.7f))
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .navigationBarsPadding()
        .padding(horizontal = 8.dp, vertical = 6.dp),
      horizontalArrangement = Arrangement.SpaceAround,
      verticalAlignment = Alignment.CenterVertically
    ) {
      NavItem(
        titleEnglish = "Home",
        titleUrdu = "گھر",
        icon = Icons.Default.Home,
        isSelected = currentTab == AppTab.HOME,
        onClick = { onTabSelected(AppTab.HOME) },
        testTag = "tab_home"
      )

      NavItem(
        titleEnglish = "My Money",
        titleUrdu = "پیسے",
        icon = Icons.Default.AccountBalanceWallet,
        isSelected = currentTab == AppTab.MONEY,
        onClick = { onTabSelected(AppTab.MONEY) },
        testTag = "tab_money"
      )

      NavItem(
        titleEnglish = "Prosperity",
        titleUrdu = "خوشحالی",
        icon = Icons.Default.Adjust,
        isSelected = currentTab == AppTab.PROSPERITY,
        onClick = { onTabSelected(AppTab.PROSPERITY) },
        testTag = "tab_score"
      )

      NavItem(
        titleEnglish = "Earn More",
        titleUrdu = "آمدنی",
        icon = Icons.AutoMirrored.Filled.TrendingUp,
        isSelected = currentTab == AppTab.EARN_MORE,
        onClick = { onTabSelected(AppTab.EARN_MORE) },
        testTag = "tab_builder"
      )
    }
  }
}

@Composable
private fun NavItem(
  titleEnglish: String,
  titleUrdu: String,
  icon: ImageVector,
  isSelected: Boolean,
  onClick: () -> Unit,
  testTag: String,
  modifier: Modifier = Modifier,
) {
  val iconColor by animateColorAsState(
    targetValue = if (isSelected) TealPrimary else SlateTextMuted,
    label = "icon_color"
  )
  val backgroundColor by animateColorAsState(
    targetValue = if (isSelected) TealSurface else Color.Transparent,
    label = "pill_bg"
  )

  Column(
    modifier = modifier
      .clip(RoundedCornerShape(12.dp))
      .clickable { onClick() }
      .testTag(testTag)
      .padding(horizontal = 12.dp, vertical = 4.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Box(
      modifier = Modifier
        .size(width = 44.dp, height = 26.dp)
        .clip(RoundedCornerShape(14.dp))
        .background(backgroundColor),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = icon,
        contentDescription = titleEnglish,
        tint = iconColor,
        modifier = Modifier.size(19.dp)
      )
    }

    Text(
      text = titleEnglish,
      fontSize = 11.sp,
      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
      color = iconColor,
      modifier = Modifier.padding(top = 1.dp)
    )

    Text(
      text = titleUrdu,
      fontSize = 9.sp,
      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
      color = iconColor.copy(alpha = if (isSelected) 1f else 0.8f),
      lineHeight = 10.sp
    )
  }
}
