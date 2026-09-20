package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.api.RegisterRequest
import com.example.viewmodel.AuthUiState
import com.example.viewmodel.AuthViewModel

@Composable
fun RegistrationScreen(
    authViewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    var cnic by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var factory by remember { mutableStateOf("") }
    var factoryId by remember { mutableStateOf("") }
    var jazzCash by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf("BILINGUAL") }

    val uiState by authViewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success) {
            onRegisterSuccess()
        }
    }

    val primaryGreen = Color(0xFF0F5132)
    val accentGold = Color(0xFFD4AF37)
    val bgGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF072A1B), Color(0xFF0E3D27), Color(0xFFF4F7F5))
    )

    // Format CNIC with dashes automatically: 42101-1234567-3
    fun onCnicChange(input: String) {
        val digits = input.filter { it.isDigit() }
        if (digits.length <= 13) {
            val formatted = buildString {
                for (i in digits.indices) {
                    append(digits[i])
                    if (i == 4 || i == 11) {
                        if (i < digits.length - 1) append('-')
                    }
                }
            }
            cnic = formatted
        }
    }

    val isCnicValid = cnic.matches(Regex("^\\d{5}-\\d{7}-\\d$"))
    val isPhoneValid = phone.matches(Regex("^(?:\\+92|0)?3\\d{9}$"))
    val isPasswordValid = password.length >= 8
    val isFormValid = name.isNotBlank() && factory.isNotBlank() && isCnicValid && isPhoneValid && isPasswordValid

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bgGradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "خوشحال اکاؤنٹ",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "نیا ورکر اکاؤنٹ رجسٹر کریں / Register Worker",
                fontSize = 14.sp,
                color = accentGold
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Name
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("پورا نام / Full Name") },
                        placeholder = { Text("مثلاً: احمد رضا") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = primaryGreen) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // CNIC
                    OutlinedTextField(
                        value = cnic,
                        onValueChange = { onCnicChange(it) },
                        label = { Text("شناختی کارڈ نمبر / CNIC") },
                        placeholder = { Text("42101-1234567-3") },
                        leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null, tint = primaryGreen) },
                        isError = cnic.isNotBlank() && !isCnicValid,
                        supportingText = {
                            if (cnic.isNotBlank() && !isCnicValid) {
                                Text("فارمیٹ: 42101-1234567-3 (13 ہندسے)", color = MaterialTheme.colorScheme.error)
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Phone
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("موبائل نمبر / Mobile Number") },
                        placeholder = { Text("03001234567") },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = primaryGreen) },
                        isError = phone.isNotBlank() && !isPhoneValid,
                        supportingText = {
                            if (phone.isNotBlank() && !isPhoneValid) {
                                Text("درست پاکستانی موبائل نمبر درج کریں", color = MaterialTheme.colorScheme.error)
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Factory Name
                    OutlinedTextField(
                        value = factory,
                        onValueChange = { factory = it },
                        label = { Text("فیکٹری یا مل کا نام / Factory Name") },
                        placeholder = { Text("Naveena Mills / Artistic Milliners") },
                        leadingIcon = { Icon(Icons.Default.Business, contentDescription = null, tint = primaryGreen) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Factory ID (optional)
                    OutlinedTextField(
                        value = factoryId,
                        onValueChange = { factoryId = it },
                        label = { Text("ورکر کارڈ نمبر / Factory ID (اختیاری)") },
                        placeholder = { Text("NVM-4892") },
                        leadingIcon = { Icon(Icons.Default.CreditCard, contentDescription = null, tint = primaryGreen) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // JazzCash (optional)
                    OutlinedTextField(
                        value = jazzCash,
                        onValueChange = { jazzCash = it },
                        label = { Text("جاز کیش / ایزی پیسہ نمبر (اختیاری)") },
                        placeholder = { Text("03001234567") },
                        leadingIcon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = primaryGreen) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Password
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("پاسورڈ / Password (کم از کم 8 حروف)") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = primaryGreen) },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = null,
                                    tint = Color.Gray
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        isError = password.isNotBlank() && !isPasswordValid,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Error Message Banner
                    if (uiState is AuthUiState.Error) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = Color(0xFFF8D7DA),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = (uiState as AuthUiState.Error).message,
                                color = Color(0xFF842029),
                                fontSize = 13.sp,
                                modifier = Modifier.padding(10.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Register Submit Button
                    Button(
                        onClick = {
                            val formattedPhone = if (phone.startsWith("+92")) phone else if (phone.startsWith("0")) "+92" + phone.substring(1) else "+92$phone"
                            authViewModel.register(
                                RegisterRequest(
                                    name = name.trim(),
                                    cnic = cnic.trim(),
                                    phone = formattedPhone,
                                    factory = factory.trim(),
                                    factoryId = factoryId.trim().ifBlank { null },
                                    jazzCashNumber = jazzCash.trim().ifBlank { null },
                                    password = password.trim(),
                                    preferredLanguage = selectedLanguage
                                )
                            )
                        },
                        enabled = uiState !is AuthUiState.Loading && isFormValid,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primaryGreen,
                            contentColor = Color.White
                        )
                    ) {
                        if (uiState is AuthUiState.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "رجسٹر کریں / Create Account",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    TextButton(onClick = {
                        authViewModel.resetState()
                        onNavigateToLogin()
                    }) {
                        Text(
                            text = "پہلے سے اکاؤنٹ ہے؟ لاگ ان کریں / Login",
                            color = primaryGreen,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
