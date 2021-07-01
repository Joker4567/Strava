package com.skillbox.core_network.repository

import com.skillbox.core_network.utils.Failure

interface AuthRepository {
    suspend fun postAuth(
            code: String,
            onLocal: (Boolean) -> Unit,
            onState: (Failure) -> Unit
    ): String?

    suspend fun reauthorize(
            access_token: String,
            onLocal: (Boolean) -> Unit,
            onState: (Failure) -> Unit
    ) : String?
}