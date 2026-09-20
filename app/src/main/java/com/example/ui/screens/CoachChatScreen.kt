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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.model.CoachMessage
import com.example.ui.theme.AmberBrand
import com.example.ui.theme.AmberDark
import com.example.ui.theme.AmberLight
import com.example.ui.theme.EmeraldBrand
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldLight
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
fun CoachChatScreen(
  messages: List<CoachMessage>,
  onBack: () -> Unit,
  onSendMessage: (String) -> Unit,
  onPlayVoice: (String, String) -> Unit,
  modifier: Modifier = Modifier,
) {
  var inputText by remember { mutableStateOf("") }
  val listState = rememberLazyListState()

  LaunchedEffect(messages.size) {
    if (messages.isNotEmpty()) {
      listState.animateScrollToItem(messages.size - 1)
    }
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
          modifier = Modifier.testTag("coach_back_btn")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = SlateTextPrimary
          )
        }

        Box(
          modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(TealSurface),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.SupportAgent,
            contentDescription = null,
            tint = TealDark,
            modifier = Modifier.size(22.dp)
          )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "Coach Fatima (کوچ فاطمہ)",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = SlateTextPrimary
          )
          Text(
            text = "Naveena Mills Certified • آن لائن",
            fontSize = 10.sp,
            color = EmeraldDark,
            fontWeight = FontWeight.Medium
          )
        }

        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(AmberLight)
            .clickable {
              onPlayVoice(
                "Coach Fatima se rabta karein. Aap bachat, committee aur nayi aamdani ke baray mein sawal pooch saktay hain.",
                "Coach Fatima se maliyati mashwara hasil karein."
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

    // Chat Message List
    LazyColumn(
      state = listState,
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
        .padding(horizontal = 14.dp, vertical = 8.dp)
        .testTag("coach_chat_scroll"),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      items(messages) { msg ->
        if (msg.isFromCoach) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
          ) {
            Card(
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              border = androidx.compose.foundation.BorderStroke(1.dp, TealBorder),
              modifier = Modifier.fillMaxWidth(0.85f)
            ) {
              Column(modifier = Modifier.padding(12.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text("Coach Fatima", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TealDark)
                  Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = "Listen",
                    tint = AmberDark,
                    modifier = Modifier
                      .size(16.dp)
                      .clickable { onPlayVoice(msg.textRoman, msg.spokenText) }
                  )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = msg.textUrdu,
                  fontSize = 12.sp,
                  lineHeight = 17.sp,
                  fontWeight = FontWeight.Medium,
                  color = SlateTextPrimary
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                  text = msg.timestamp,
                  fontSize = 9.sp,
                  color = SlateTextMuted,
                  modifier = Modifier.align(Alignment.End)
                )
              }
            }
          }
        } else {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
          ) {
            Card(
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(containerColor = TealPrimary),
              modifier = Modifier.fillMaxWidth(0.8f)
            ) {
              Column(modifier = Modifier.padding(12.dp)) {
                Text(
                  text = msg.textUrdu,
                  fontSize = 12.sp,
                  lineHeight = 17.sp,
                  color = Color.White,
                  fontWeight = FontWeight.Normal
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                  text = msg.timestamp,
                  fontSize = 9.sp,
                  color = Color.White.copy(alpha = 0.7f),
                  modifier = Modifier.align(Alignment.End)
                )
              }
            }
          }
        }
      }
    }

    // Suggestion Chips Row
    LazyRow(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 14.dp, vertical = 6.dp),
      horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      val suggestions = listOf(
        "Bachat kaisay barhayen?" to "بچت کیسے بڑھائیں؟",
        "Kameti theek hai ya Bank?" to "کمیٹی یا بینک؟",
        "Emergency fund kitna ho?" to "ہنگامی فنڈ کتنا ہو؟",
        "Silai machine ka mashwara" to "سلائی مشین مشورہ"
      )
      items(suggestions) { (eng, urdu) ->
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(1.dp, SlateBorder, RoundedCornerShape(12.dp))
            .clickable {
              onSendMessage("$eng ($urdu)")
            }
            .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
          Text(text = urdu, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = TealDark)
        }
      }
    }

    // Input Bar
    Surface(
      color = Color.White,
      shadowElevation = 4.dp,
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = {
            onSendMessage("Coach Fatima, mujhe aamdani barhanay ka tareeqa batayein.")
          },
          modifier = Modifier.size(38.dp)
        ) {
          Icon(Icons.Default.Mic, contentDescription = "Voice Input", tint = TealPrimary)
        }

        OutlinedTextField(
          value = inputText,
          onValueChange = { inputText = it },
          placeholder = { Text("سوال لکھیں یا بولیں...", fontSize = 12.sp) },
          shape = RoundedCornerShape(20.dp),
          modifier = Modifier
            .weight(1f)
            .height(48.dp),
          colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF8FAFC),
            unfocusedContainerColor = Color(0xFFF8FAFC),
            focusedIndicatorColor = TealPrimary,
            unfocusedIndicatorColor = SlateBorder
          )
        )

        Spacer(modifier = Modifier.width(6.dp))

        IconButton(
          onClick = {
            if (inputText.isNotBlank()) {
              onSendMessage(inputText)
              inputText = ""
            }
          },
          modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(TealPrimary)
            .testTag("send_coach_msg_btn")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.Send,
            contentDescription = "Send",
            tint = Color.White,
            modifier = Modifier.size(18.dp)
          )
        }
      }
    }
  }
}
