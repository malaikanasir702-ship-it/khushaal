package com.example.ui.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.OnboardingPrimaryButton
import com.example.ui.components.OnboardingSkipButton
import com.example.ui.components.StepDotIndicator
import com.example.ui.theme.*

@Composable
fun OnboardingStep2FinancialHealth(
    incomeText: String,
    incomeError: Boolean,
    onIncomeChanged: (String) -> Unit,
    onNext: () -> Unit,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(horizontal = 20.dp)
    ) {
        StepDotIndicator(totalDots = 7, currentDot = 0)

        Spacer(modifier = Modifier.height(8.dp))

        // Title
        Text(
            text = "مالی صحت کا جائزہ",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = SlateTextPrimary
        )
        Text(
            text = "Financial Health Check",
            fontSize = 15.sp,
            color = TealPrimary,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "آمدن، اخراجات، قرض اور اہداف سمجھنے کے لیے",
            fontSize = 13.sp,
            color = SlateTextSecondary
        )
        Text(
            text = "Understand income, expenses, debts and goals",
            fontSize = 12.sp,
            color = SlateTextMuted
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Income input card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "آپ کی ماہانہ گھریلو آمدن کیا ہے؟",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = SlateTextPrimary
                )
                Text(
                    text = "What is your monthly household income?",
                    fontSize = 12.sp,
                    color = SlateTextSecondary
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = incomeText,
                    onValueChange = onIncomeChanged,
                    modifier = Modifier.fillMaxWidth(),
                    prefix = { Text("Rs. ", fontWeight = FontWeight.Bold, color = TealPrimary) },
                    placeholder = { Text("55,000") },
                    isError = incomeError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TealPrimary,
                        errorBorderColor = RoseAlert
                    )
                )
                if (incomeError) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "براہ کرم درست رقم درج کریں / Please enter a valid amount",
                        fontSize = 12.sp,
                        color = RoseAlert
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        OnboardingPrimaryButton(
            text = "اگلا / Next",
            onClick = onNext
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            OnboardingSkipButton(onClick = onSkip)
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}
