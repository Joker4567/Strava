package com.skillbox.core_network.utils

import com.google.gson.Gson
import retrofit2.HttpException
import java.net.SocketTimeoutException

class ErrorHandler(
    private val gsonConverter: Gson
) {
    fun proceedException(exception: Throwable): Failure {
        when (exception) {
            is HttpException -> {
                try {
                    val error = gsonConverter.fromJson(
                        exception.response()?.errorBody()?.string(),
                        ErrorResponse::class.java
                    )
                    return when (error.error) {
                        "UnknownError" -> Failure.UnknownError
                        else -> Failure.CommonError
                    }
                } catch (e: Exception) {
                    Failure.ServerError
                }
            }
            is SocketTimeoutException -> {
                return Failure.ServerError
            }
            else -> Failure.CommonError
        }
        return Failure.CommonError
    }
}