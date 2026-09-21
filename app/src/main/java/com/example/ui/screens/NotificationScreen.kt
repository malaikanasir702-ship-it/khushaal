package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SupportAgent
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppDestination
import com.example.model.AppLanguage
import com.example.model.AppNotification
import com.example.model.NotificationCategory
import com.example.ui.theme.AmberBrand
import com.example.ui.theme.AmberDark
import com.example.ui.theme.AmberLight
import com.example.ui.theme.AmberSurface
import com.example.ui.theme.EmeraldBrand
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.EmeraldSurface
import com.example.ui.theme.RoseAlert
import com.example.ui.theme.RoseBorder
import com.example.ui.theme.RoseSurface
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateDarkBg
import com.example.ui.theme.SlateTextMuted
import com.example.ui.theme.SlateTextPrimary
import com.example.ui.theme.SlateTextSecondary
import com.example.ui.theme.TealBorder
import com.example.ui.theme.TealDark
import com.example.ui.theme.TealDarker
import com.example.ui.theme.TealLight
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.TealSurface

@Composable
fun NotificationScreen(
  notifications: List<AppNotification>,
  currentLanguage: AppLanguage,
  onBack: () -> Unit,
  onNotificationClick: (AppNotification) -> Unit,
  onMarkAllRead: () -> Unit,
  onClearAll: () -> Unit,
  onPlayVoice: (String, String) -> Unit,
  modifier: Modifier = Modifier,
  factoryName: String = "Factory",
) {
  var selectedCategory by remember { mutableStateOf<NotificationCategory?>(null) }

  val filteredNotifications = remember(notifications, selectedCategory) {
    if (selectedCategory == null) notifications
    else notifications.filter { it.category == selectedCategory }
  }

  val unreadCount = remember(notifications) {
    notifications.count { !it.isRead }
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
      Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
          ) {
            IconButton(
              onClick = onBack,
              modifier = Modifier
                .size(40.dp)
                .testTag("notification_back_btn")
            ) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = SlateDarkBg
              )
            }

            Spacer(modifier = Modifier.width(6.dp))

            Column {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Text(
                  text = if (currentLanguage == AppLanguage.URDU) "اطلاعات و الرٹس" else "Notifications & Alerts",
                  fontSize = 16.sp,
                  fontWeight = FontWeight.Bold,
                  color = SlateDarkBg,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis
                )

                if (unreadCount > 0) {
                  Box(
                    modifier = Modifier
                      .clip(CircleShape)
                      .background(RoseAlert)
                      .padding(horizontal = 6.dp, vertical = 2.dp),
                    contentAlignment = Alignment.Center
                  ) {
                    Text(
                      text = "$unreadCount New",
                      fontSize = 10.sp,
                      fontWeight = FontWeight.Bold,
                      color = Color.White
                    )
                  }
                }
              }

              Text(
                text = "$factoryName, JazzCash & SBP Updates",
                fontSize = 11.sp,
                color = SlateTextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
            }
          }

          // Top action buttons
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            IconButton(
              onClick = {
                onPlayVoice(
                  "Aap ki $unreadCount nayi itlaa'at hain. Factory tankhwah jama ho gayi hai aur committee installment due hai.",
                  "Aap ki nayi itlaa'at hain. Factory tankhwah jama ho gayi hai aur committee installment due hai."
                )
              },
              modifier = Modifier.size(36.dp)
            ) {
              Icon(
                imageVector = Icons.Default.VolumeUp,
                contentDescription = "Listen All",
                tint = AmberDark,
                modifier = Modifier.size(20.dp)
              )
            }

            IconButton(
              onClick = onMarkAllRead,
              modifier = Modifier.size(36.dp)
            ) {
              Icon(
                imageVector = Icons.Default.DoneAll,
                contentDescription = "Mark All Read",
                tint = TealPrimary,
                modifier = Modifier.size(20.dp)
              )
            }

            IconButton(
              onClick = onClearAll,
              modifier = Modifier.size(36.dp)
            ) {
              Icon(
                imageVector = Icons.Default.ClearAll,
                contentDescription = "Clear All",
                tint = SlateTextMuted,
                modifier = Modifier.size(20.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Filter chips
        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          item {
            FilterChipItem(
              label = "سبھی (All)",
              isSelected = selectedCategory == null,
              onClick = { selectedCategory = null }
            )
          }
          item {
            FilterChipItem(
              label = "مل و فیکٹری (Factory)",
              isSelected = selectedCategory == NotificationCategory.FACTORY,
              onClick = { selectedCategory = NotificationCategory.FACTORY }
            )
          }
          item {
            FilterChipItem(
              label = "مالیات و کمیٹی (Finance)",
              isSelected = selectedCategory == NotificationCategory.FINANCE,
              onClick = { selectedCategory = NotificationCategory.FINANCE }
            )
          }
          item {
            FilterChipItem(
              label = "سیکیورٹی (Security)",
              isSelected = selectedCategory == NotificationCategory.SECURITY,
              onClick = { selectedCategory = NotificationCategory.SECURITY }
            )
          }
          item {
            FilterChipItem(
              label = "کوچ فاطمہ (Coach)",
              isSelected = selectedCategory == NotificationCategory.COACH,
              onClick = { selectedCategory = NotificationCategory.COACH }
            )
          }
        }
      }
    }

    // List of Notifications
    if (filteredNotifications.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(24.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Box(
            modifier = Modifier
              .size(64.dp)
              .clip(CircleShape)
              .background(TealSurface),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = "No alerts",
              tint = TealPrimary,
              modifier = Modifier.size(36.dp)
            )
          }

          Spacer(modifier = Modifier.height(16.dp))

          Text(
            text = "کوئی نئی اطلاع نہیں ہے",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = SlateTextPrimary
          )

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = "آپ کا کھاتہ، فیکٹری ریکارڈ اور الرٹس بالکل اپ ٹو ڈیٹ ہیں۔",
            fontSize = 12.sp,
            color = SlateTextSecondary
          )
        }
      }
    } else {
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        items(filteredNotifications, key = { it.id }) { notif ->
          NotificationCard(
            notification = notif,
            currentLanguage = currentLanguage,
            onClick = { onNotificationClick(notif) },
            onPlayVoice = onPlayVoice,
            factoryName = factoryName
          )
        }
      }
    }
  }
}

@Composable
private fun FilterChipItem(
  label: String,
  isSelected: Boolean,
  onClick: () -> Unit,
) {
  Box(
    modifier = Modifier
      .clip(RoundedCornerShape(16.dp))
      .background(if (isSelected) TealPrimary else Color(0xFFF1F5F9))
      .border(1.dp, if (isSelected) TealDark else SlateBorder, RoundedCornerShape(16.dp))
      .clickable { onClick() }
      .padding(horizontal = 10.dp, vertical = 6.dp)
  ) {
    Text(
      text = label,
      fontSize = 11.sp,
      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
      color = if (isSelected) Color.White else SlateTextPrimary
    )
  }
}

@Composable
private fun NotificationCard(
  notification: AppNotification,
  currentLanguage: AppLanguage,
  onClick: () -> Unit,
  onPlayVoice: (String, String) -> Unit,
  factoryName: String = "Factory",
) {
  val (categoryColor, categorySurface, categoryIcon, categoryLabel) = when (notification.category) {
    NotificationCategory.FACTORY -> Quadruple(TealPrimary, TealSurface, Icons.Default.Business, "فیکٹری الرٹ • $factoryName")
    NotificationCategory.FINANCE -> Quadruple(EmeraldDark, EmeraldSurface, Icons.Default.AccountBalanceWallet, "کھاتہ و کمیٹی • Finance")
    NotificationCategory.SECURITY -> Quadruple(RoseAlert, RoseSurface, Icons.Default.Security, "تحفظ الرٹ • State Bank")
    NotificationCategory.COACH -> Quadruple(AmberDark, AmberSurface, Icons.Default.SupportAgent, "کوچ فاطمہ • Guidance")
  }

  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (notification.isRead) Color.White else categorySurface.copy(alpha = 0.35f)
    ),
    border = androidx.compose.foundation.BorderStroke(
      1.dp,
      if (notification.isRead) SlateBorder else categoryColor.copy(alpha = 0.4f)
    ),
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() }
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      // Header: Category badge & Timestamp & Voice button
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Box(
            modifier = Modifier
              .size(24.dp)
              .clip(CircleShape)
              .background(categorySurface),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = categoryIcon,
              contentDescription = null,
              tint = categoryColor,
              modifier = Modifier.size(14.dp)
            )
          }

          Text(
            text = categoryLabel,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = categoryColor
          )

          if (!notification.isRead) {
            Box(
              modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(RoseAlert)
            )
          }
        }

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Text(
            text = notification.timestamp,
            fontSize = 10.sp,
            color = SlateTextMuted
          )

          IconButton(
            onClick = {
              onPlayVoice(notification.titleUrdu, notification.spokenText)
            },
            modifier = Modifier.size(26.dp)
          ) {
            Icon(
              imageVector = Icons.Default.VolumeUp,
              contentDescription = "Listen",
              tint = AmberDark,
              modifier = Modifier.size(15.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(6.dp))

      // Title
      Text(
        text = if (currentLanguage == AppLanguage.ENGLISH) notification.titleEnglish else notification.titleUrdu,
        fontSize = 13.5.sp,
        fontWeight = FontWeight.Bold,
        color = SlateTextPrimary,
        lineHeight = 18.sp
      )

      if (currentLanguage == AppLanguage.BILINGUAL && notification.titleEnglish.isNotEmpty()) {
        Text(
          text = notification.titleEnglish,
          fontSize = 11.sp,
          fontWeight = FontWeight.Medium,
          color = SlateTextSecondary,
          lineHeight = 15.sp
        )
      }

      Spacer(modifier = Modifier.height(6.dp))

      // Description
      Text(
        text = if (currentLanguage == AppLanguage.ENGLISH) notification.descriptionEnglish else notification.descriptionUrdu,
        fontSize = 11.5.sp,
        color = SlateTextSecondary,
        lineHeight = 16.sp
      )

      // Action Button if applicable
      if (notification.actionLabelUrdu != null && notification.destination != null) {
        Spacer(modifier = Modifier.height(10.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End
        ) {
          Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(containerColor = categoryColor),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.height(32.dp)
          ) {
            Text(
              text = if (currentLanguage == AppLanguage.ENGLISH) notification.actionLabelEnglish.orEmpty() else "${notification.actionLabelUrdu} →",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
          }
        }
      }
    }
  }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
