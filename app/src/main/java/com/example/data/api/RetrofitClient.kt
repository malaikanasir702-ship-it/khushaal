package com.example.data.api

import android.content.Context
import com.example.data.TokenManager
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient private constructor(private val appContext: Context) {

    private val tokenManager = TokenManager.getInstance(appContext)

    var baseUrl: String = appContext.getSharedPreferences("khushhaal_prefs", Context.MODE_PRIVATE)
        .getString("api_base_url", DEFAULT_BASE_URL) ?: DEFAULT_BASE_URL
        private set

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val authInterceptor = AuthInterceptor(tokenManager, baseUrl)

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build()
    }

    private val moshi: Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    val authApiService: AuthApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }

    val khushhaalApiService: KhushhaalApiService by lazy {
        retrofit.create(KhushhaalApiService::class.java)
    }

    companion object {
        // Standard Android emulator loopback to host PC on port 5000
        const val DEFAULT_BASE_URL = "http://10.0.2.2:5000/"

        @Volatile
        private var INSTANCE: RetrofitClient? = null

        fun getInstance(context: Context): RetrofitClient {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: RetrofitClient(context.applicationContext).also { INSTANCE = it }
            }
        }

        fun updateBaseUrl(context: Context, newUrl: String) {
            val formatted = if (newUrl.endsWith("/")) newUrl else "$newUrl/"
            context.getSharedPreferences("khushhaal_prefs", Context.MODE_PRIVATE)
                .edit()
                .putString("api_base_url", formatted)
                .apply()
            synchronized(this) {
                INSTANCE = null // Re-create client with new URL on next call
            }
        }
    }
}
