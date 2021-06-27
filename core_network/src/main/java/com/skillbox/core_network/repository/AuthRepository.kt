package com.skillbox.core_network.repository

import com.skillbox.core_network.utils.State

interface AuthRepository {
    suspend fun getAthlete(
            onSuccess: (Boolean) -> Unit,
            onState: (State) -> Unit
    )
    suspend fun postAuth(
            code: String,
            onSuccess: (String) -> Unit,
            onState: (State) -> Unit
    )
}