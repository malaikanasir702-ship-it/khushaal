package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateTextSecondary
import com.example.ui.theme.TealPrimary

/**
 * Step dot indicator for onboarding steps 2–8.
 * totalDots = 7 (representing steps 2–8)
 * currentDot = 0-indexed (0 = step 2, 6 = step 8)
 */
@Composable
fun StepDotIndicator(
    totalDots: Int = 7,
    currentDot: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalDots) { index ->
            val isActive = index == currentDot
            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(if (isActive) 10.dp else 8.dp)
                    .clip(CircleShape)
                    .background(if (isActive) TealPrimary else SlateBorder)
            )
        }
    }
}

/**
 * Primary action button — TealPrimary fill (Next, Get Started, Finish)
 */
@Composable
fun OnboardingPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = TealPrimary,
            contentColor = Color.White,
            disabledContainerColor = TealPrimary.copy(alpha = 0.4f),
            disabledContentColor = Color.White.copy(alpha = 0.6f)
        )
    ) {
        Text(
            text = text,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Secondary outlined button — Back button style
 */
@Composable
fun OnboardingSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .height(48.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = SlateTextSecondary
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = SlateTextSecondary
        )
    }
}

/**
 * Skip button — text style, subtle
 */
@Composable
fun OnboardingSkipButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier.height(40.dp)
    ) {
        Text(
            text = "چھوڑیں / Skip",
            fontSize = 13.sp,
            color = SlateTextSecondary,
            fontWeight = FontWeight.Normal
        )
    }
}
