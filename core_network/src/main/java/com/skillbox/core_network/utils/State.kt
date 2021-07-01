package com.skillbox.core_network.utils

sealed class State {
    data class Error(val throwable: Failure) : State()
}