package com.example.ui.screens.onboarding

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.OnboardingPrimaryButton
import com.example.ui.components.OnboardingSecondaryButton
import com.example.ui.components.StepDotIndicator
import com.example.ui.theme.*

@Composable
fun OnboardingStep8Progress(
    onFinish: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StepDotIndicator(totalDots = 7, currentDot = 6)

        Spacer(modifier = Modifier.weight(0.5f))

        Text(
            text = "میری ترقی",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = SlateTextPrimary,
            textAlign = TextAlign.Center
        )
        Text(
            text = "My Progress",
            fontSize = 15.sp,
            color = TealPrimary,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Circular arc score
        val score = 72
        val maxScore = 100
        val sweepAngle = 240f * score / maxScore

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(160.dp)
        ) {
            Canvas(modifier = Modifier.size(160.dp)) {
                val strokeWidth = 16.dp.toPx()
                val arcSize = size.width - strokeWidth
                val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)
                // Background arc
                drawArc(
                    color = TealSurface,
                    startAngle = 150f,
                    sweepAngle = 240f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = Size(arcSize, arcSize),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
                // Progress arc
                drawArc(
                    color = TealPrimary,
                    startAngle = 150f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = topLeft,
                    size = Size(arcSize, arcSize),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "$score",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TealPrimary
                )
                Text(
                    text = "/ $maxScore",
                    fontSize = 14.sp,
                    color = SlateTextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "خاندانی خوشحالی سکور",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = SlateTextPrimary,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Family Prosperity Score",
            fontSize = 13.sp,
            color = SlateTextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = TealSurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "اپنی بہتری دیکھیں اور سنگ میل حاصل کریں",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = TealDark,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "See your improvements and achieve milestones",
                    fontSize = 12.sp,
                    color = SlateTextSecondary,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        OnboardingPrimaryButton(
            text = "شروع کریں / Get Started",
            onClick = onFinish
        )
        Spacer(modifier = Modifier.height(8.dp))
        OnboardingSecondaryButton(
            text = "← پیچھے / Back",
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}
