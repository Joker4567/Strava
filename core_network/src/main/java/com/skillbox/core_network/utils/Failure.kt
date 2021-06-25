package com.skillbox.core_network.utils

sealed class Failure {
    object ServerError : Failure()
    object CommonError : Failure()
    object UnknownError : Failure()
}
