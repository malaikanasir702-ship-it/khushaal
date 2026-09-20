package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Envelope
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
import com.example.ui.theme.RoseDark
import com.example.ui.theme.RoseLight
import com.example.ui.theme.RoseSurface
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * Data representation for an animated flying money particle that travels with spring physics.
 */
data class FlyingMoneyToken(
  val id: Int,
  val targetEnvelopeIndex: Int,
  val startXFraction: Float,
  val targetXFraction: Float,
  val targetYFraction: Float,
  val denominationText: String,
  val rotationDeg: Float,
  val color: Color
)

/**
 * High-fidelity Budget Envelope Splitting Component powered by Compose Spring Physics.
 * Shows money physically bursting and bouncing from the central Salary Vault into 4 category envelopes.
 */
@Composable
fun EnvelopeSpringAllocationView(
  totalSalary: Long = 55_000L,
  onPlayVoice: ((String, String) -> Unit)? = null,
  onOpenFullDetail: (() -> Unit)? = null,
  modifier: Modifier = Modifier,
) {
  val scope = rememberCoroutineScope()

  // Percentage splits (defaults: 70% needs, 6% BC, 6% emergency, 18% savings)
  var needsPct by remember { mutableIntStateOf(70) }
  var commitmentsPct by remember { mutableIntStateOf(6) }
  var emergencyPct by remember { mutableIntStateOf(6) }
  var savingsPct by remember { mutableIntStateOf(18) }

  val needsAmount = (totalSalary * needsPct / 100)
  val commitmentsAmount = (totalSalary * commitmentsPct / 100)
  val emergencyAmount = (totalSalary * emergencyPct / 100)
  val savingsAmount = totalSalary - needsAmount - commitmentsAmount - emergencyAmount

  // Animation states
  var isAllocating by remember { mutableStateOf(false) }
  var isCompleted by remember { mutableStateOf(false) }
  var showCustomizeSliders by remember { mutableStateOf(false) }
  var animationSpeed by remember { mutableStateOf(1.0f) } // 1.0x or 1.5x

  // Spring physics animatables for the Central Salary Vault
  val vaultScale = remember { Animatable(1f) }
  val remainingSalaryDisplay = remember { Animatable(totalSalary.toFloat()) }

  // Spring physics animatables for each of the 4 envelopes
  val envelopeScales = remember { List(4) { Animatable(1f) } }
  val envelopeAmounts = remember { List(4) { Animatable(0f) } }
  val envelopeProgress = remember { List(4) { Animatable(0f) } }
  val envelopeGlow = remember { List(4) { Animatable(0f) } }

  // Flying token spring animatables
  val tokenTravelAnimatables = remember { List(8) { Animatable(0f) } }
  val tokenScaleAnimatables = remember { List(8) { Animatable(0f) } }

  // Define 8 flying banknotes across the 4 envelopes (2 per envelope)
  val tokens = remember {
    listOf(
      // Tokens for Envelope 0 (Needs)
      FlyingMoneyToken(0, 0, 0f, -0.32f, 0.45f, "Rs. 20k", -12f, EmeraldBrand),
      FlyingMoneyToken(1, 0, 0f, -0.18f, 0.52f, "Rs. 18.5k", 8f, EmeraldDark),
      // Tokens for Envelope 1 (Commitments)
      FlyingMoneyToken(2, 1, 0f, 0.20f, 0.46f, "Rs. 2k", -8f, AmberBrand),
      FlyingMoneyToken(3, 1, 0f, 0.35f, 0.54f, "Rs. 1.5k", 14f, AmberDark),
      // Tokens for Envelope 2 (Emergency)
      FlyingMoneyToken(4, 2, 0f, -0.28f, 0.85f, "Rs. 2k", -15f, RoseAlert),
      FlyingMoneyToken(5, 2, 0f, -0.12f, 0.90f, "Rs. 1k", 6f, RoseDark),
      // Tokens for Envelope 3 (Savings)
      FlyingMoneyToken(6, 3, 0f, 0.18f, 0.86f, "Rs. 5k", 10f, TealPrimary),
      FlyingMoneyToken(7, 3, 0f, 0.32f, 0.92f, "Rs. 5k", -6f, TealDark)
    )
  }

  // Infinite pulsing glow for active state
  val infiniteTransition = rememberInfiniteTransition(label = "pulse")
  val pulseGlow by infiniteTransition.animateFloat(
    initialValue = 0.92f,
    targetValue = 1.08f,
    animationSpec = infiniteRepeatable(
      animation = tween(900, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulseGlow"
  )

  // Spring allocation runner
  fun runSpringAllocation() {
    if (isAllocating) return
    isAllocating = true
    isCompleted = false

    scope.launch {
      // 1. Reset all animatables
      envelopeScales.forEach { it.snapTo(1f) }
      envelopeAmounts.forEach { it.snapTo(0f) }
      envelopeProgress.forEach { it.snapTo(0f) }
      envelopeGlow.forEach { it.snapTo(0f) }
      tokenTravelAnimatables.forEach { it.snapTo(0f) }
      tokenScaleAnimatables.forEach { it.snapTo(0f) }
      remainingSalaryDisplay.snapTo(totalSalary.toFloat())

      // 2. Central Vault Spring Release (anticipation bounce)
      vaultScale.animateTo(
        targetValue = 1.18f,
        animationSpec = spring(
          dampingRatio = Spring.DampingRatioMediumBouncy,
          stiffness = Spring.StiffnessMedium
        )
      )

      val targetAmounts = listOf(
        needsAmount.toFloat(),
        commitmentsAmount.toFloat(),
        emergencyAmount.toFloat(),
        savingsAmount.toFloat()
      )

      val targetPcts = listOf(
        needsPct / 100f,
        commitmentsPct / 100f,
        emergencyPct / 100f,
        savingsPct / 100f
      )

      // 3. Staggered envelope allocation with spring physics
      for (envIndex in 0..3) {
        // Vault pulses downward as money leaves
        launch {
          val currentVault = (totalSalary - targetAmounts.take(envIndex + 1).sum()).coerceAtLeast(0f)
          remainingSalaryDisplay.animateTo(
            targetValue = currentVault,
            animationSpec = spring(
              dampingRatio = Spring.DampingRatioNoBouncy,
              stiffness = Spring.StiffnessLow
            )
          )
        }

        // Animate the 2 tokens for this envelope
        val tokenA = envIndex * 2
        val tokenB = envIndex * 2 + 1

        launch {
          tokenScaleAnimatables[tokenA].snapTo(1f)
          tokenTravelAnimatables[tokenA].animateTo(
            targetValue = 1f,
            animationSpec = spring(
              dampingRatio = Spring.DampingRatioMediumBouncy,
              stiffness = Spring.StiffnessLow
            )
          )
          tokenScaleAnimatables[tokenA].animateTo(0f, animationSpec = tween(120))
        }

        delay((140 / animationSpeed).toLong())

        launch {
          tokenScaleAnimatables[tokenB].snapTo(1f)
          tokenTravelAnimatables[tokenB].animateTo(
            targetValue = 1f,
            animationSpec = spring(
              dampingRatio = Spring.DampingRatioHighBouncy,
              stiffness = Spring.StiffnessMediumLow
            )
          )
          tokenScaleAnimatables[tokenB].animateTo(0f, animationSpec = tween(120))
        }

        delay((240 / animationSpeed).toLong())

        // 4. Spring impact on the target envelope!
        launch {
          // Bouncy envelope scale reaction
          envelopeScales[envIndex].animateTo(
            targetValue = 1.09f,
            animationSpec = spring(
              dampingRatio = Spring.DampingRatioHighBouncy,
              stiffness = Spring.StiffnessMedium
            )
          )
          envelopeScales[envIndex].animateTo(
            targetValue = 1.0f,
            animationSpec = spring(
              dampingRatio = Spring.DampingRatioMediumBouncy,
              stiffness = Spring.StiffnessLow
            )
          )
        }

        // Animated amount counter with spring
        launch {
          envelopeAmounts[envIndex].animateTo(
            targetValue = targetAmounts[envIndex],
            animationSpec = spring(
              dampingRatio = Spring.DampingRatioNoBouncy,
              stiffness = Spring.StiffnessLow
            )
          )
        }

        // Animated progress fill with spring
        launch {
          envelopeProgress[envIndex].animateTo(
            targetValue = targetPcts[envIndex],
            animationSpec = spring(
              dampingRatio = Spring.DampingRatioMediumBouncy,
              stiffness = Spring.StiffnessLow
            )
          )
        }

        // Glow trigger
        launch {
          envelopeGlow[envIndex].animateTo(1f, animationSpec = tween(180))
          envelopeGlow[envIndex].animateTo(0f, animationSpec = tween(350))
        }

        delay((300 / animationSpeed).toLong())
      }

      // 5. Vault returns to resting scale with soft spring
      vaultScale.animateTo(
        targetValue = 1.0f,
        animationSpec = spring(
          dampingRatio = Spring.DampingRatioMediumBouncy,
          stiffness = Spring.StiffnessLow
        )
      )

      isAllocating = false
      isCompleted = true
    }
  }

  // Auto-run on first appearance
  LaunchedEffect(Unit) {
    delay(400)
    runSpringAllocation()
  }

  Card(
    shape = RoundedCornerShape(24.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
    border = androidx.compose.foundation.BorderStroke(1.5.dp, SlateBorder),
    modifier = modifier.fillMaxWidth()
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      // Header Bar
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
              text = "Smart Envelope Split",
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = SlateTextPrimary
            )
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(TealSurface)
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(
                text = "Spring Physics ✨",
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = TealPrimary
              )
            }
          }
          Text(
            text = "حکمت عملی بجٹ تقسیم • برکت والا اسپرنگ فارمولا",
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = SlateTextSecondary
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
          if (onPlayVoice != null) {
            Box(
              modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(AmberLight)
                .clickable {
                  onPlayVoice(
                    "Spring allocation engine aap ki kul tankhwah 55,000 rupay ko chaar mukhtalif lifafon mein smart tareeqay se bhejta ha: zarooriyat, committee, emergency aur bachat.",
                    "Spring physics se tankhwah chaar lifafon mein taqseem ho rahi hai."
                  )
                },
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.VolumeUp, contentDescription = "Voice", tint = AmberDark, modifier = Modifier.size(16.dp))
            }
          }

          IconButton(
            onClick = { showCustomizeSliders = !showCustomizeSliders },
            modifier = Modifier
              .size(32.dp)
              .clip(CircleShape)
              .background(if (showCustomizeSliders) TealSurface else Color(0xFFF1F5F9))
          ) {
            Icon(
              imageVector = Icons.Default.Tune,
              contentDescription = "Customize",
              tint = if (showCustomizeSliders) TealDarker else SlateTextMuted,
              modifier = Modifier.size(16.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Visual Physics Stage
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(20.dp))
          .background(
            Brush.verticalGradient(
              listOf(
                Color(0xFF0F172A),
                Color(0xFF1E293B)
              )
            )
          )
          .padding(14.dp)
      ) {
        Column(
          modifier = Modifier.fillMaxWidth(),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          // Central Master Vault (Salary Source)
          Box(
            modifier = Modifier
              .scale(vaultScale.value)
              .clip(RoundedCornerShape(16.dp))
              .background(
                Brush.horizontalGradient(
                  listOf(TealDarker, Color(0xFF0D9488))
                )
              )
              .border(1.5.dp, Color(0xFF2DD4BF).copy(alpha = 0.6f), RoundedCornerShape(16.dp))
              .padding(horizontal = 16.dp, vertical = 10.dp)
              .testTag("salary_source_vault"),
            contentAlignment = Alignment.Center
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(34.dp)
                  .clip(CircleShape)
                  .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.AccountBalanceWallet,
                  contentDescription = null,
                  tint = Color.White,
                  modifier = Modifier.size(18.dp)
                )
              }

              Column {
                Text(
                  text = "صاف تنخواہ پول (Salary Pool)",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White.copy(alpha = 0.85f)
                )
                Text(
                  text = "PKR ${String.format("%,d", remainingSalaryDisplay.value.roundToInt())}",
                  fontSize = 17.sp,
                  fontWeight = FontWeight.Black,
                  color = Color.White
                )
              }

              if (isAllocating) {
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(AmberBrand.copy(alpha = 0.25f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                  Text(
                    text = "تقسیم جاری...",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = AmberLight
                  )
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // 4 Envelopes Grid in 2x2 layout
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            // Envelope 0: Household Needs
            AnimatedEnvelopeTile(
              titleUrdu = "ضروری اخراجات",
              titleEnglish = "Needs ($needsPct%)",
              amount = envelopeAmounts[0].value.toLong(),
              targetAmount = needsAmount,
              progress = envelopeProgress[0].value,
              icon = Icons.Default.Home,
              accentColor = Color(0xFF2DD4BF),
              surfaceColor = Color(0xFF134E4A),
              scale = envelopeScales[0].value,
              glow = envelopeGlow[0].value,
              isCompleted = isCompleted,
              modifier = Modifier.weight(1f)
            )

            // Envelope 1: Kameti & Commitments
            AnimatedEnvelopeTile(
              titleUrdu = "کمیٹی و واجبات",
              titleEnglish = "Kameti ($commitmentsPct%)",
              amount = envelopeAmounts[1].value.toLong(),
              targetAmount = commitmentsAmount,
              progress = envelopeProgress[1].value,
              icon = Icons.Default.People,
              accentColor = AmberBrand,
              surfaceColor = Color(0xFF78350F),
              scale = envelopeScales[1].value,
              glow = envelopeGlow[1].value,
              isCompleted = isCompleted,
              modifier = Modifier.weight(1f)
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            // Envelope 2: Emergency Fund
            AnimatedEnvelopeTile(
              titleUrdu = "ہنگامی تحفظ",
              titleEnglish = "Emergency ($emergencyPct%)",
              amount = envelopeAmounts[2].value.toLong(),
              targetAmount = emergencyAmount,
              progress = envelopeProgress[2].value,
              icon = Icons.Default.Shield,
              accentColor = Color(0xFFFB7185),
              surfaceColor = Color(0xFF881337),
              scale = envelopeScales[2].value,
              glow = envelopeGlow[2].value,
              isCompleted = isCompleted,
              modifier = Modifier.weight(1f)
            )

            // Envelope 3: Savings & Buffer
            AnimatedEnvelopeTile(
              titleUrdu = "دستیاب بچت",
              titleEnglish = "Savings ($savingsPct%)",
              amount = envelopeAmounts[3].value.toLong(),
              targetAmount = savingsAmount,
              progress = envelopeProgress[3].value,
              icon = Icons.Default.Savings,
              accentColor = Color(0xFF34D399),
              surfaceColor = Color(0xFF064E3B),
              scale = envelopeScales[3].value,
              glow = envelopeGlow[3].value,
              isCompleted = isCompleted,
              modifier = Modifier.weight(1f)
            )
          }

          // Flying money animation overlay
          if (isAllocating) {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .padding(top = 4.dp),
              contentAlignment = Alignment.Center
            ) {
              Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text("💸", fontSize = 16.sp, modifier = Modifier.scale(pulseGlow))
                Text("✨", fontSize = 14.sp)
                Text("💵", fontSize = 16.sp, modifier = Modifier.scale(pulseGlow))
                Text("✨", fontSize = 14.sp)
                Text("🪙", fontSize = 16.sp, modifier = Modifier.scale(pulseGlow))
              }
            }
          }
        }
      }

      // Success Banner when Completed
      if (isCompleted) {
        Spacer(modifier = Modifier.height(12.dp))
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(EmeraldSurface)
            .border(1.dp, EmeraldLight, RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldDark, modifier = Modifier.size(16.dp))
              Text(
                text = "ماشاءاللہ! تمام روپے اسپرنگ فارمولا کے تحت تقسیم ہو گئے۔",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = EmeraldDark
              )
            }

            Text(
              text = "100% Balanced",
              fontSize = 10.sp,
              fontWeight = FontWeight.Black,
              color = EmeraldBrand
            )
          }
        }
      }

      // Customizable Sliders Panel (collapsible)
      if (showCustomizeSliders) {
        Spacer(modifier = Modifier.height(12.dp))
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
          border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Text(
              text = "اپنا تناسب تبدیل کریں (Custom Split Percentages):",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = SlateTextPrimary
            )
            Text(
              text = "فیصد تبدیل کرنے پر اسپرنگ فزکس دوبارہ رقم ازخود برابر کرے گی۔",
              fontSize = 10.sp,
              color = SlateTextSecondary
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Preset Buttons
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              PresetSplitChip(
                label = "70/6/6/18 (برکت کلاسک)",
                isSelected = needsPct == 70 && commitmentsPct == 6,
                onClick = {
                  needsPct = 70
                  commitmentsPct = 6
                  emergencyPct = 6
                  savingsPct = 18
                  runSpringAllocation()
                },
                modifier = Modifier.weight(1f)
              )

              PresetSplitChip(
                label = "60/10/10/20 (زیادہ بچت)",
                isSelected = needsPct == 60 && savingsPct == 20,
                onClick = {
                  needsPct = 60
                  commitmentsPct = 10
                  emergencyPct = 10
                  savingsPct = 20
                  runSpringAllocation()
                },
                modifier = Modifier.weight(1f)
              )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Slider 1: Needs
            SplitSliderRow(
              label = "1. ضروری راشن و کرایہ",
              pct = needsPct,
              color = TealPrimary,
              onValueChange = {
                needsPct = it
                runSpringAllocation()
              }
            )

            // Slider 2: Commitments
            SplitSliderRow(
              label = "2. کمیٹی و واجبات",
              pct = commitmentsPct,
              color = AmberDark,
              onValueChange = {
                commitmentsPct = it
                runSpringAllocation()
              }
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Action Controls
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Button(
          onClick = { runSpringAllocation() },
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
          modifier = Modifier
            .weight(1f)
            .height(42.dp)
            .testTag("re_allocate_spring_btn")
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Icon(
              imageVector = if (isCompleted) Icons.Default.Replay else Icons.Default.PlayArrow,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(16.dp)
            )
            Text(
              text = if (isCompleted) "دوبارہ تقسیم اینیمیشن (Replay)" else "رقم تقسیم کریں (Allocate)",
              fontSize = 11.5.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
          }
        }

        // Speed Toggle (1x vs 1.5x)
        Button(
          onClick = {
            animationSpeed = if (animationSpeed == 1.0f) 1.5f else 1.0f
          },
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFF1F5F9),
            contentColor = SlateTextPrimary
          ),
          modifier = Modifier.height(42.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Icon(Icons.Default.Speed, contentDescription = null, tint = SlateTextSecondary, modifier = Modifier.size(14.dp))
            Text(
              text = "${animationSpeed}x",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }

        if (onOpenFullDetail != null) {
          Button(
            onClick = onOpenFullDetail,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = EmeraldSurface,
              contentColor = EmeraldDark
            ),
            modifier = Modifier.height(42.dp)
          ) {
            Text(
              text = "تفصیل →",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }
  }
}

@Composable
private fun AnimatedEnvelopeTile(
  titleUrdu: String,
  titleEnglish: String,
  amount: Long,
  targetAmount: Long,
  progress: Float,
  icon: ImageVector,
  accentColor: Color,
  surfaceColor: Color,
  scale: Float,
  glow: Float,
  isCompleted: Boolean,
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier
      .scale(scale)
      .clip(RoundedCornerShape(16.dp))
      .background(surfaceColor)
      .border(
        width = if (glow > 0.05f) 2.dp else 1.dp,
        color = if (glow > 0.05f) accentColor else accentColor.copy(alpha = 0.35f),
        shape = RoundedCornerShape(16.dp)
      )
      .padding(12.dp)
  ) {
    Column {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(accentColor.copy(alpha = 0.2f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = icon,
            contentDescription = null,
            tint = accentColor,
            modifier = Modifier.size(15.dp)
          )
        }

        if (isCompleted) {
          Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = accentColor,
            modifier = Modifier.size(14.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = titleUrdu,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White
      )
      Text(
        text = titleEnglish,
        fontSize = 9.sp,
        color = Color.White.copy(alpha = 0.7f)
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = "PKR ${String.format("%,d", amount)}",
        fontSize = 14.sp,
        fontWeight = FontWeight.Black,
        color = accentColor
      )

      Spacer(modifier = Modifier.height(6.dp))

      // Smooth Progress Bar
      LinearProgressIndicator(
        progress = { progress.coerceIn(0f, 1f) },
        modifier = Modifier
          .fillMaxWidth()
          .height(5.dp)
          .clip(RoundedCornerShape(3.dp)),
        color = accentColor,
        trackColor = Color.White.copy(alpha = 0.15f)
      )
    }
  }
}

@Composable
private fun PresetSplitChip(
  label: String,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(10.dp))
      .background(if (isSelected) TealPrimary else Color.White)
      .border(
        1.dp,
        if (isSelected) TealDark else SlateBorder,
        RoundedCornerShape(10.dp)
      )
      .clickable { onClick() }
      .padding(vertical = 6.dp, horizontal = 6.dp),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = label,
      fontSize = 10.sp,
      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
      color = if (isSelected) Color.White else SlateTextPrimary
    )
  }
}

@Composable
private fun SplitSliderRow(
  label: String,
  pct: Int,
  color: Color,
  onValueChange: (Int) -> Unit,
) {
  Column(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text(text = label, fontSize = 11.sp, color = SlateTextPrimary)
      Text(text = "$pct%", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = color)
    }
    Slider(
      value = pct.toFloat(),
      onValueChange = { onValueChange(it.roundToInt()) },
      valueRange = 5f..80f,
      colors = SliderDefaults.colors(
        thumbColor = color,
        activeTrackColor = color,
        inactiveTrackColor = Color(0xFFE2E8F0)
      ),
      modifier = Modifier.height(24.dp)
    )
  }
}
