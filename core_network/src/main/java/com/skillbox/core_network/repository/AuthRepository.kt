package com.skillbox.core_network.repository

import com.skillbox.core_network.utils.State
import com.skillbox.shared_model.Athlete

interface AuthRepository {
    suspend fun postAuth(
            code: String,
            onSuccess: (String) -> Unit,
            onState: (State) -> Unit
    )
}