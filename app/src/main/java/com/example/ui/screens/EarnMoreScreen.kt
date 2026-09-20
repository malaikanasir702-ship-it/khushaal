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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.AppDestination
import com.example.model.RoadmapStep
import com.example.model.SkillOpportunity
import com.example.ui.theme.AmberBrand
import com.example.ui.theme.AmberDark
import com.example.ui.theme.AmberLight
import com.example.ui.theme.EmeraldBrand
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.EmeraldSurface
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateBorderSubtle
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
fun EarnMoreScreen(
  availableSkills: List<SkillOpportunity>,
  selectedSkill: SkillOpportunity,
  roadmapSteps: List<RoadmapStep>,
  onSelectSkill: (SkillOpportunity) -> Unit,
  onToggleRoadmapStep: (Int) -> Unit,
  onActivatePlan: () -> Unit,
  onPlayVoice: (String, String) -> Unit,
  modifier: Modifier = Modifier,
  onNavigateToDestination: (AppDestination) -> Unit = {},
) {
  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFF8FAFC))
      .padding(horizontal = 14.dp, vertical = 12.dp)
      .testTag("earn_more_screen_scroll"),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // 1. Skill Selection Header
    item {
      Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "Aap ya aap ke ghar mein kisi ko kya kaam aata hai?",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = SlateTextPrimary,
                lineHeight = 19.sp
              )
              Text(
                text = "گھر میں موجود مہارت کو ماہانہ آمدنی میں بدلیں",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TealPrimary,
                modifier = Modifier.padding(top = 2.dp)
              )
            }

            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(AmberLight)
                .clickable {
                  onPlayVoice(
                    "Ghar ki aamdani barhanay ke liye kisi aik hunar ka intikhab karein. Khushhaal aap ko tees din ka asaan business blueprint faraham karay ga.",
                    "Ghar ki aamdani barhanay ke liye kisi aik hunar ka intikhab karein."
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

          Spacer(modifier = Modifier.height(14.dp))

          // 8 Skill Grid (4 rows of 2)
          val chunkedSkills = availableSkills.chunked(2)
          chunkedSkills.forEach { pair ->
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              pair.forEach { skill ->
                SkillOptionButton(
                  skill = skill,
                  isSelected = skill.id == selectedSkill.id,
                  onClick = { onSelectSkill(skill) },
                  modifier = Modifier.weight(1f)
                )
              }
            }
            Spacer(modifier = Modifier.height(8.dp))
          }
        }
      }
    }

    // 2. Selected Opportunity Blueprint Card
    item {
      Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, TealBorder),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          // Header
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(42.dp)
                  .clip(RoundedCornerShape(12.dp))
                  .background(TealSurface),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = selectedSkill.iconEmoji,
                  fontSize = 22.sp
                )
              }

              Column {
                Text(
                  text = selectedSkill.roleTitle,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  color = SlateTextPrimary
                )
                Text(
                  text = selectedSkill.roleUrdu,
                  fontSize = 11.sp,
                  color = TealDarker,
                  fontWeight = FontWeight.Medium
                )
              }
            }

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(EmeraldLight)
                .border(1.dp, EmeraldBrand.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
              Text(
                text = "${selectedSkill.matchPercentage}% Match",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = EmeraldDark
              )
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = selectedSkill.idealFor,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = SlateTextSecondary
          )

          Spacer(modifier = Modifier.height(12.dp))

          // 2 Metric Tiles: Starting Investment & Monthly Profit
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            // Starting investment
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFFF8FAFC))
                .border(1.dp, SlateBorder, RoundedCornerShape(14.dp))
                .padding(10.dp)
            ) {
              Column {
                Text(
                  text = "بنیادی خرچ (Start)",
                  fontSize = 10.sp,
                  color = SlateTextSecondary,
                  fontWeight = FontWeight.Bold
                )
                Text(
                  text = selectedSkill.startingInvestment,
                  fontSize = 16.sp,
                  fontWeight = FontWeight.Black,
                  color = SlateTextPrimary
                )
                Text(
                  text = selectedSkill.investmentNote,
                  fontSize = 9.sp,
                  color = SlateTextMuted
                )
              }
            }

            // Monthly profit
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(14.dp))
                .background(EmeraldSurface)
                .border(1.dp, EmeraldLight, RoundedCornerShape(14.dp))
                .padding(10.dp)
            ) {
              Column {
                Text(
                  text = "ماہانہ منافع (Profit)",
                  fontSize = 10.sp,
                  color = EmeraldDark,
                  fontWeight = FontWeight.Bold
                )
                Text(
                  text = selectedSkill.monthlyProfit,
                  fontSize = 16.sp,
                  fontWeight = FontWeight.Black,
                  color = EmeraldDark
                )
                Text(
                  text = selectedSkill.profitNote,
                  fontSize = 9.sp,
                  color = EmeraldDark.copy(alpha = 0.8f)
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Requirements
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(12.dp))
              .background(TealSurface)
              .padding(horizontal = 10.dp, vertical = 8.dp)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = TealPrimary,
                modifier = Modifier.size(14.dp)
              )
              Text(
                text = selectedSkill.requirements,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = TealDarker
              )
            }
          }
        }
      }
    }

    // 3. Success Audio Story
    item {
      Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
        modifier = Modifier.fillMaxWidth()
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
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f)
          ) {
            AsyncImage(
              model = "https://www.gstatic.com/labs-code/stitch/stitch-placeholder-300x300.svg",
              contentDescription = "Razia Story",
              contentScale = ContentScale.Crop,
              modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .border(1.dp, SlateBorder, CircleShape)
            )

            Column {
              Text(
                text = "Razia's Journey (رضیہ آپا کی کہانی)",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = SlateTextPrimary
              )
              Text(
                text = "Naveena Mills worker wife who made Rs. 16,500 in 2nd month",
                fontSize = 10.sp,
                color = SlateTextSecondary,
                lineHeight = 13.sp
              )
            }
          }

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(10.dp))
              .background(AmberLight)
              .border(1.dp, AmberBrand.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
              .clickable {
                onPlayVoice(
                  "Razia Aapa ne Naveena Mills colony mein silai ka kaam shuru kiya aur doosray maheenay 16,500 rupay kamaye. Aap bhi yeh kar saktay hain.",
                  "Razia Aapa ne Naveena Mills colony mein silai ka kaam shuru kiya."
                )
              }
              .padding(horizontal = 8.dp, vertical = 5.dp)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Icon(
                imageVector = Icons.Default.VolumeUp,
                contentDescription = "Story",
                tint = AmberDark,
                modifier = Modifier.size(13.dp)
              )
              Text(
                text = "سنیں",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = AmberDark
              )
            }
          }
        }
      }
    }

    // Microenterprise Business Khata & Order Book Banner
    item {
      Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, TealBorder),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "Microenterprise Khata & Orders",
                fontSize = 13.sp,
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
                .clip(RoundedCornerShape(8.dp))
                .background(EmeraldSurface)
                .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
              Text("Active Orders: 3", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = EmeraldDark)
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = "کسٹمرز کے آرڈرز، ایڈوانس، بقایا رقم اور ڈلیوری کی تاریخیں الگ کاروباری رجسٹر میں محفوظ رکھیں۔",
            fontSize = 11.sp,
            color = SlateTextSecondary,
            lineHeight = 15.sp
          )

          Spacer(modifier = Modifier.height(10.dp))

          Button(
            onClick = { onNavigateToDestination(AppDestination.BusinessKhata) },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
            modifier = Modifier.fillMaxWidth().height(40.dp)
          ) {
            Text("کاروباری کھاتہ کھولیں (Open Khata & Order Book) →", fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }
        }
      }
    }

    // 4. 30-Day Phased Execution Checklist
    item {
      Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "30-Day Phased Execution Checklist",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = SlateTextPrimary
          )
          Text(
            text = "30 دن کا عملی ایکشن پلان (قدم بہ قدم)",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = SlateTextSecondary
          )

          Spacer(modifier = Modifier.height(14.dp))

          roadmapSteps.forEach { step ->
            RoadmapStepItem(
              step = step,
              onToggle = { onToggleRoadmapStep(step.week) }
            )
            Spacer(modifier = Modifier.height(10.dp))
          }
        }
      }
    }

    // 5. Activate 30-Day Plan Button
    item {
      Button(
        onClick = onActivatePlan,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = TealDarker,
          contentColor = Color.White
        ),
        modifier = Modifier
          .fillMaxWidth()
          .height(50.dp)
          .testTag("activate_30_day_plan_btn")
      ) {
        Icon(
          imageVector = Icons.Default.RocketLaunch,
          contentDescription = null,
          tint = AmberBrand,
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Activate 30-Day Plan • پلان شروع کریں",
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold
        )
      }
    }

    item {
      Spacer(modifier = Modifier.height(8.dp))
    }
  }
}

@Composable
private fun SkillOptionButton(
  skill: SkillOpportunity,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(12.dp))
      .background(if (isSelected) TealSurface else Color(0xFFF8FAFC))
      .border(
        width = if (isSelected) 2.dp else 1.dp,
        color = if (isSelected) TealPrimary else SlateBorder,
        shape = RoundedCornerShape(12.dp)
      )
      .clickable { onClick() }
      .padding(horizontal = 8.dp, vertical = 8.dp)
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      Text(
        text = skill.iconEmoji,
        fontSize = 17.sp
      )
      Column {
        Text(
          text = skill.nameEnglish,
          fontSize = 11.sp,
          fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
          color = if (isSelected) TealDarker else SlateTextPrimary,
          maxLines = 1
        )
        Text(
          text = skill.nameUrdu,
          fontSize = 10.sp,
          color = if (isSelected) TealPrimary else SlateTextSecondary,
          maxLines = 1
        )
      }
    }
  }
}

@Composable
private fun RoadmapStepItem(
  step: RoadmapStep,
  onToggle: () -> Unit,
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(14.dp))
      .background(if (step.isCompleted) Color(0xFFF8FAFC) else Color.White)
      .border(1.dp, if (step.isCompleted) EmeraldBrand.copy(alpha = 0.4f) else SlateBorder, RoundedCornerShape(14.dp))
      .clickable { onToggle() }
      .padding(12.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp),
      verticalAlignment = Alignment.Top
    ) {
      // Checkbox circle
      Box(
        modifier = Modifier
          .size(24.dp)
          .clip(CircleShape)
          .background(if (step.isCompleted) EmeraldBrand else Color.Transparent)
          .border(
            2.dp,
            if (step.isCompleted) EmeraldBrand else SlateBorder,
            CircleShape
          ),
        contentAlignment = Alignment.Center
      ) {
        if (step.isCompleted) {
          Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "Completed",
            tint = Color.White,
            modifier = Modifier.size(14.dp)
          )
        }
      }

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = step.weekLabel,
          fontSize = 9.sp,
          fontWeight = FontWeight.Bold,
          color = if (step.isCompleted) EmeraldDark else TealPrimary
        )
        Text(
          text = step.title,
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = SlateTextPrimary
        )
        Text(
          text = step.description,
          fontSize = 11.sp,
          color = SlateTextSecondary,
          lineHeight = 14.sp
        )
      }
    }
  }
}
