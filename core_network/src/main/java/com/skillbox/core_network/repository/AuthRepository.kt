package com.skillbox.core_network.repository

import com.skillbox.core_network.utils.State

interface AuthRepository {
    suspend fun postAuth(
            code: String,
            onSuccess: (String) -> Unit,
            onState: (State) -> Unit
    )

    suspend fun reauthorize(
            access_token: String,
            onSuccess: (String) -> Unit,
            onState: (State) -> Unit
    )
}