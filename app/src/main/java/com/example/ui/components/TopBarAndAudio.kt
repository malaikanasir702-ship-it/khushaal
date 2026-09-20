package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.AppLanguage
import com.example.model.AppTab
import com.example.ui.theme.AmberBrand
import com.example.ui.theme.AmberDark
import com.example.ui.theme.AmberLight
import com.example.ui.theme.EmeraldBrand
import com.example.ui.theme.RoseAlert
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateDarkBg
import com.example.ui.theme.SlateTextPrimary
import com.example.ui.theme.SlateTextSecondary
import com.example.ui.theme.TealDark
import com.example.ui.theme.TealDarker
import com.example.ui.theme.TealPrimary

@Composable
fun KhushhaalHeader(
  currentTab: AppTab,
  isPlayingAudio: Boolean,
  unreadNotificationCount: Int = 0,
  currentLanguage: AppLanguage = AppLanguage.BILINGUAL,
  onAudioClick: () -> Unit,
  onLanguageToggle: () -> Unit,
  onNotificationClick: () -> Unit,
  onProfileClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val subtitle = when (currentTab) {
    AppTab.HOME -> when (currentLanguage) {
      AppLanguage.URDU -> "گھرانہ مالی بہبودی • بجٹ و فیکٹری کھاتہ"
      AppLanguage.ENGLISH -> "Household Financial Wellbeing • Budget"
      AppLanguage.BILINGUAL -> "Gharana Mali Behboodi • گھرانہ مالی بہبودی"
    }
    AppTab.MONEY -> when (currentLanguage) {
      AppLanguage.URDU -> "برکت والا فارمولا • 5 لفافے تقسیم"
      AppLanguage.ENGLISH -> "Smart Monthly Split • 5 Envelopes"
      AppLanguage.BILINGUAL -> "Smart Monthly Split • برکت والا فارمولا"
    }
    AppTab.PROSPERITY -> when (currentLanguage) {
      AppLanguage.URDU -> "مالی تحفظ کے 6 ستون • حفاظتی ڈھال"
      AppLanguage.ENGLISH -> "6 Core Wellbeing Pillars • Safety Shield"
      AppLanguage.BILINGUAL -> "6 Core Wellbeing Pillars • مالی حفاظت"
    }
    AppTab.EARN_MORE -> when (currentLanguage) {
      AppLanguage.URDU -> "گھریلو ہنر و کاروبار • 30 روزہ آمدنی پلان"
      AppLanguage.ENGLISH -> "Microenterprise • 30-Day Income Plan"
      AppLanguage.BILINGUAL -> "Microenterprise • 30-Day Income Plan"
    }
  }

  Surface(
    modifier = modifier.fillMaxWidth(),
    color = Color.White,
    shadowElevation = 2.dp,
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .statusBarsPadding()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 10.dp, vertical = 7.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        // Logo and brand - with weight(1f) to guarantee it never pushes the actions row off-screen
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .weight(1f)
            .padding(end = 4.dp)
            .clickable { /* Tap logo */ }
        ) {
          Box(
            modifier = Modifier
              .size(34.dp)
              .clip(CircleShape)
              .background(TealPrimary)
              .border(1.dp, TealDark, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            AsyncImage(
              model = "https://lh3.googleusercontent.com/aida/AEtjO1WmY5aYrT1VivzS1G-GZzp8hqTbpLu7JVcDho67bBqg4I2AsvLyz4z_cY4W-H9SwNgTqU1BbVVnnVKwJPwgqD1RcYilFUpRdi-tD8syidhn7pmE6t_HdCWZmpJev2PsPta7ZrdQ_sFL5Py48LVT2T0WrxX94IISJ1_r2Ay0i-UfJCUEOir3BYTEWukj3cAUhPVG42e6-cVaTfi1Shb-KgBFzdyI5yPtQMXAEscwYqTfpLKm3TXc3E1RBg",
              contentDescription = "Khushhaal Logo",
              contentScale = ContentScale.Crop,
              modifier = Modifier.size(34.dp),
            )
          }

          Spacer(modifier = Modifier.width(7.dp))

          Column(
            modifier = Modifier.weight(1f, fill = false)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              if (currentLanguage != AppLanguage.URDU) {
                Text(
                  text = "Khushhaal",
                  fontWeight = FontWeight.ExtraBold,
                  fontSize = 15.sp,
                  color = TealDarker,
                  letterSpacing = (-0.3).sp,
                  maxLines = 1
                )
              }
              Text(
                text = "خوشحال",
                fontWeight = FontWeight.Bold,
                fontSize = if (currentLanguage == AppLanguage.URDU) 16.sp else 12.sp,
                color = TealPrimary,
                maxLines = 1
              )
            }
            Text(
              text = subtitle,
              fontSize = 9.sp,
              color = SlateTextSecondary,
              fontWeight = FontWeight.Medium,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          }
        }

        // Actions - compact, nicely spaced, guaranteed visible on every screen size
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          // Voice assist badge with audio pulse animation
          val infiniteTransition = rememberInfiniteTransition(label = "audio_pulse")
          val pulseScale by infiniteTransition.animateFloat(
            initialValue = 1.0f,
            targetValue = if (isPlayingAudio) 1.08f else 1.0f,
            animationSpec = infiniteRepeatable(
              animation = tween(600),
              repeatMode = RepeatMode.Reverse
            ),
            label = "pulse"
          )

          Box(
            modifier = Modifier
              .scale(pulseScale)
              .clip(RoundedCornerShape(16.dp))
              .background(if (isPlayingAudio) AmberBrand else AmberLight)
              .border(1.dp, AmberBrand.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
              .clickable { onAudioClick() }
              .testTag("global_audio_btn")
              .padding(horizontal = 6.dp, vertical = 5.dp)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
              Icon(
                imageVector = Icons.Default.VolumeUp,
                contentDescription = "Listen",
                tint = if (isPlayingAudio) Color.White else AmberDark,
                modifier = Modifier.size(13.dp)
              )
              Text(
                text = if (currentLanguage == AppLanguage.ENGLISH) "Audio" else "سنیں",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (isPlayingAudio) Color.White else AmberDark
              )
            }
          }

          // Language toggle
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(Color(0xFFF1F5F9))
              .border(1.dp, SlateBorder, RoundedCornerShape(8.dp))
              .clickable { onLanguageToggle() }
              .testTag("language_toggle_btn")
              .padding(horizontal = 6.dp, vertical = 5.dp)
          ) {
            Text(
              text = when (currentLanguage) {
                AppLanguage.URDU -> "اردو"
                AppLanguage.ENGLISH -> "EN"
                AppLanguage.BILINGUAL -> "اردو/EN"
              },
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              color = SlateTextPrimary
            )
          }

          // Notification Bell with unread badge
          Box(
            modifier = Modifier
              .size(33.dp)
              .clip(CircleShape)
              .background(Color(0xFFF1F5F9))
              .clickable { onNotificationClick() }
              .testTag("notification_btn"),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Notifications,
              contentDescription = "Notifications",
              tint = SlateTextSecondary,
              modifier = Modifier.size(17.dp)
            )
            if (unreadNotificationCount > 0) {
              Box(
                modifier = Modifier
                  .align(Alignment.TopEnd)
                  .padding(top = 2.dp, end = 2.dp)
                  .size(if (unreadNotificationCount > 9) 14.dp else 12.dp)
                  .clip(CircleShape)
                  .background(RoseAlert),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = if (unreadNotificationCount > 9) "9+" else unreadNotificationCount.toString(),
                  color = Color.White,
                  fontSize = 7.5.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }
          }

          // Profile icon
          Box(
            modifier = Modifier
              .size(33.dp)
              .clip(CircleShape)
              .background(TealDark)
              .clickable { onProfileClick() }
              .testTag("profile_avatar_btn"),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Person,
              contentDescription = "Profile",
              tint = Color.White,
              modifier = Modifier.size(17.dp)
            )
          }
        }
      }
    }
  }
}

@Composable
fun AudioPlayingBanner(
  isPlaying: Boolean,
  caption: String,
  onStop: () -> Unit,
  modifier: Modifier = Modifier,
) {
  AnimatedVisibility(
    visible = isPlaying,
    enter = expandVertically() + fadeIn(),
    exit = shrinkVertically() + fadeOut(),
    modifier = modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .background(AmberBrand)
        .padding(horizontal = 14.dp, vertical = 7.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.weight(1f)
      ) {
        Icon(
          imageVector = Icons.Default.GraphicEq,
          contentDescription = "Audio playing",
          tint = Color.White,
          modifier = Modifier.size(16.dp)
        )
        Text(
          text = caption.ifEmpty { "رہنمائی جاری ہے..." },
          color = Color.White,
          fontSize = 12.sp,
          fontWeight = FontWeight.SemiBold,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }

      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(6.dp))
          .background(AmberDark)
          .clickable { onStop() }
          .padding(horizontal = 8.dp, vertical = 3.dp)
      ) {
        Text(
          text = "بند کریں",
          color = Color.White,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold
        )
      }
    }
  }
}

@Composable
fun FloatingToast(
  message: String?,
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier,
) {
  AnimatedVisibility(
    visible = message != null,
    enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
    exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 8.dp)
  ) {
    Surface(
      color = SlateDarkBg.copy(alpha = 0.96f),
      shape = RoundedCornerShape(14.dp),
      shadowElevation = 8.dp,
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.weight(1f)
        ) {
          Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Status",
            tint = EmeraldBrand,
            modifier = Modifier.size(18.dp)
          )
          Text(
            text = message.orEmpty(),
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
          )
        }

        IconButton(
          onClick = onDismiss,
          modifier = Modifier.size(24.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Dismiss",
            tint = Color.White.copy(alpha = 0.7f),
            modifier = Modifier.size(14.dp)
          )
        }
      }
    }
  }
}
