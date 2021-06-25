package com.skillbox.core_network.utils

sealed class State {
    object Loading : State()
    object Loaded : State()
    data class Error(val throwable: Failure) : State()
}