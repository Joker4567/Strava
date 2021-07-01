package com.skillbox.core_network.utils

import retrofit2.HttpException
import java.net.SocketTimeoutException

class ErrorHandler() {
    fun proceedException(exception: Throwable): Failure {
        when (exception) {
            is HttpException -> {
                return when (exception.response()?.code()) {
                    502 -> Failure.CacheError
                    else -> Failure.UnknownError
                }
            }
            is SocketTimeoutException -> {
                return Failure.ServerError
            }
            else -> Failure.UnknownError
        }
        return Failure.UnknownError
    }
}