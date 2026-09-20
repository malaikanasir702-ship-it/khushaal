package com.example.data.api

import com.example.data.TokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject

class AuthInterceptor(
    private val tokenManager: TokenManager,
    private val baseUrl: String
) : Interceptor {

    private val refreshClient = OkHttpClient.Builder().build()

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val path = originalRequest.url.encodedPath

        // Skip auth injection for login and register endpoints
        if (path.contains("/auth/login") || path.contains("/auth/register")) {
            return chain.proceed(originalRequest)
        }

        val token = tokenManager.getAccessToken()
        val requestBuilder = originalRequest.newBuilder()
        if (!token.isNullOrBlank()) {
            requestBuilder.header("Authorization", "Bearer $token")
        }

        val response = chain.proceed(requestBuilder.build())

        // Handle 401 Unauthorized
        if (response.code == 401 && !path.contains("/auth/refresh")) {
            synchronized(this) {
                // Check if another thread already refreshed
                val currentToken = tokenManager.getAccessToken()
                if (currentToken != token && !currentToken.isNullOrBlank()) {
                    response.close()
                    val newRequest = originalRequest.newBuilder()
                        .header("Authorization", "Bearer $currentToken")
                        .build()
                    return chain.proceed(newRequest)
                }

                val refreshToken = tokenManager.getRefreshToken()
                if (refreshToken.isNullOrBlank()) {
                    tokenManager.clearTokens()
                    return response
                }

                // Attempt token refresh synchronously
                val refreshSuccessful = performTokenRefresh(refreshToken)
                if (refreshSuccessful) {
                    response.close()
                    val newToken = tokenManager.getAccessToken()
                    val retriedRequest = originalRequest.newBuilder()
                        .header("Authorization", "Bearer $newToken")
                        .build()
                    return chain.proceed(retriedRequest)
                } else {
                    tokenManager.clearTokens()
                }
            }
        }

        return response
    }

    private fun performTokenRefresh(refreshToken: String): Boolean {
        return try {
            val json = JSONObject().apply {
                put("refreshToken", refreshToken)
            }
            val body = json.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
            val refreshUrl = if (baseUrl.endsWith("/")) "${baseUrl}api/auth/refresh" else "$baseUrl/api/auth/refresh"

            val refreshRequest = Request.Builder()
                .url(refreshUrl)
                .post(body)
                .build()

            val refreshResponse = refreshClient.newCall(refreshRequest).execute()
            if (refreshResponse.isSuccessful) {
                val responseBody = refreshResponse.body?.string()
                if (!responseBody.isNullOrBlank()) {
                    val resJson = JSONObject(responseBody)
                    val newAccess = resJson.getString("accessToken")
                    val newRefresh = resJson.getString("refreshToken")
                    tokenManager.saveTokens(newAccess, newRefresh)
                    return true
                }
            }
            false
        } catch (e: Exception) {
            false
        }
    }
}
