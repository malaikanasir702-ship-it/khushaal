package com.example.data.repository

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val code: Int = 0, val message: String) : Result<Nothing>()
    object Loading : Result<Nothing>()
}
