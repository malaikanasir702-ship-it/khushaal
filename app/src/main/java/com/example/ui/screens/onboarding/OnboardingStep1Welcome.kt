package com.example.ui.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.OnboardingPrimaryButton
import com.example.ui.theme.*

@Composable
fun OnboardingStep1Welcome(
    onGetStarted: () -> Unit,
    onLogin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(1f))

        // Logo / Branding
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(TealSurface, RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("🌿", fontSize = 40.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Khushhaal",
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TealPrimary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Urdu tagline (primary)
        Text(
            text = "آپ کا خوشحال کل، ہمارا ساتھ",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = SlateTextPrimary,
            textAlign = TextAlign.Center,
            lineHeight = 32.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // English tagline (secondary)
        Text(
            text = "Your Prosperous Tomorrow, Our Commitment",
            fontSize = 14.sp,
            color = SlateTextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Subtitle
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = TealSurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "ملازمین اور ان کے خاندان کی مالی بہتری کے لیے",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = TealDark,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "For employees and family financial wellness",
                    fontSize = 12.sp,
                    color = SlateTextSecondary,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Get Started button
        OnboardingPrimaryButton(
            text = "شروع کریں / Get Started",
            onClick = onGetStarted
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Login outlined button
        OutlinedButton(
            onClick = onLogin,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = TealPrimary),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, TealPrimary)
        ) {
            Text(
                text = "لاگ اِن / Login",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
