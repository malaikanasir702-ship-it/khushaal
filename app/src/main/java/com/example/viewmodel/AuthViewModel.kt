package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.TokenManager
import com.example.data.api.AuthApiService
import com.example.data.api.LoginRequest
import com.example.data.api.RegisterRequest
import com.example.data.api.RetrofitClient
import com.example.data.api.UserProfileDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val user: UserProfileDto) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val authService: AuthApiService = RetrofitClient.getInstance(application).authApiService
    private val tokenManager: TokenManager = TokenManager.getInstance(application)

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(tokenManager.hasTokens())
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    init {
        viewModelScope.launch {
            tokenManager.sessionExpiredEvents.collect {
                _isLoggedIn.value = false
                _uiState.value = AuthUiState.Idle
            }
        }
    }

    fun login(phone: String, pass: String) {
        if (phone.isBlank() || pass.isBlank()) {
            _uiState.value = AuthUiState.Error("فون نمبر اور پاسورڈ درج کریں / Enter phone and password")
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                val formattedPhone = if (phone.startsWith("+92")) phone else if (phone.startsWith("0")) "+92" + phone.substring(1) else "+92$phone"
                val response = authService.login(LoginRequest(phone = formattedPhone, password = pass))
                if (response.isSuccessful && response.body() != null) {
                    val authBody = response.body()!!
                    tokenManager.saveTokens(authBody.accessToken, authBody.refreshToken)
                    _isLoggedIn.value = true
                    _uiState.value = AuthUiState.Success(authBody.user)
                } else {
                    val errorMsg = parseErrorMessage(response.errorBody()?.string())
                        ?: "غلط نمبر یا پاسورڈ / Invalid credentials"
                    _uiState.value = AuthUiState.Error(errorMsg)
                }
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error("انٹرنیٹ سے رابطہ نہیں ہو سکا / Network connection error: ${e.localizedMessage}")
            }
        }
    }

    fun register(request: RegisterRequest) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                val formattedPhone = if (request.phone.startsWith("+92")) request.phone else if (request.phone.startsWith("0")) "+92" + request.phone.substring(1) else "+92${request.phone}"
                val response = authService.register(request.copy(phone = formattedPhone))
                if (response.isSuccessful && response.body() != null) {
                    val authBody = response.body()!!
                    tokenManager.saveTokens(authBody.accessToken, authBody.refreshToken)
                    _isLoggedIn.value = true
                    _uiState.value = AuthUiState.Success(authBody.user)
                } else {
                    val errorMsg = parseErrorMessage(response.errorBody()?.string())
                        ?: "رجسٹریشن میں خرابی پیش آئی ہے / Registration failed"
                    _uiState.value = AuthUiState.Error(errorMsg)
                }
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error("انٹرنیٹ سے رابطہ نہیں ہو سکا / Network connection error: ${e.localizedMessage}")
            }
        }
    }

    fun logout(onLoggedOut: (() -> Unit)? = null) {
        viewModelScope.launch {
            try {
                val token = tokenManager.getAccessToken()
                if (!token.isNullOrBlank()) {
                    authService.logout("Bearer $token")
                }
            } catch (e: Exception) {
                // Ignore network error on logout
            } finally {
                tokenManager.clearTokens()
                try {
                    com.example.data.repository.KhushhaalRepository.getInstance(getApplication()).clearCache()
                } catch (e: Exception) {}
                _isLoggedIn.value = false
                _uiState.value = AuthUiState.Idle
                onLoggedOut?.invoke()
            }
        }
    }

    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }

    private fun parseErrorMessage(errorBody: String?): String? {
        if (errorBody.isNullOrBlank()) return null
        return try {
            val json = JSONObject(errorBody)
            json.optString("error", json.optString("message", null))
        } catch (e: Exception) {
            null
        }
    }
}
