package com.skillbox.core_network.repository

import com.skillbox.core_network.ConstAPI
import com.skillbox.core_network.api.AuthApi
import com.skillbox.core_network.utils.BaseRepository
import com.skillbox.core_network.utils.ErrorHandler
import com.skillbox.core_network.utils.State
import com.skillbox.shared_model.OAuthModel
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

    override suspend fun postAuth(code: String, onSuccess: (String) -> Unit, onState: (State) -> Unit) {
        execute(onSuccess = onSuccess, onState = onState) {
            val response =
                    api.postAuth(
                            client_id = ConstAPI.id_client,
                            client_secret = ConstAPI.client_secret,
                            code = code,
                            grant_type = "authorization_code").execute()
            if(response.isSuccessful) {
                val resultOAuth = response.body() as OAuthModel
                resultOAuth.access_token
            }
            else
                ""
        }
    }

}