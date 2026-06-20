package com.example.domain.model

sealed class Result<out T> {
    data class Success<T>(val data: T): Result<T>()
    data class Error(val errorType: ErrorType) : Result<Nothing>()
}