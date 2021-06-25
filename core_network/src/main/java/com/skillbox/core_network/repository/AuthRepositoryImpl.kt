package com.skillbox.core_network.repository

import com.skillbox.core_network.api.AuthApi
import com.skillbox.core_network.utils.BaseRepository
import com.skillbox.core_network.utils.ErrorHandler
import com.skillbox.core_network.utils.State
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
        errorHandler: ErrorHandler,
        private val api: AuthApi
) : BaseRepository(errorHandler = errorHandler), AuthRepository {

    override suspend fun getAthlete(onSuccess: (Boolean) -> Unit, onState: (State) -> Unit) {
        execute(onSuccess = onSuccess, onState = onState) {
            val response = api.getAthlete().execute()
            true
        }
    }

}