package com.skillbox.core_network.repository

import com.skillbox.core_db.pref.Pref
import com.skillbox.core_network.ConstAPI
import com.skillbox.core_network.api.AthleteApi
import com.skillbox.core_network.api.AuthApi
import com.skillbox.core_network.utils.BaseRepository
import com.skillbox.core_network.utils.ErrorHandler
import com.skillbox.core_network.utils.State
import com.skillbox.shared_model.Athlete
import com.skillbox.shared_model.OAuthModel
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
        errorHandler: ErrorHandler,
        private val api: AuthApi,
        private val pref: Pref
) : BaseRepository(errorHandler = errorHandler), AuthRepository {

    override suspend fun postAuth(code: String, onSuccess: (String) -> Unit, onState: (State) -> Unit) {
        execute(onSuccess = onSuccess, onState = onState) {
            val response =
                    api.postAuth(
                            client_id = ConstAPI.id_client,
                            client_secret = ConstAPI.client_secret,
                            code = code,
                            grant_type = "authorization_code").execute()
            if (response.isSuccessful) {
                val resultOAuth = response.body() as OAuthModel
                pref.accessToken = resultOAuth.access_token
                resultOAuth.access_token
            } else
                ""
        }
    }

    override suspend fun reauthorize(access_token: String, onSuccess: (String) -> Unit, onState: (State) -> Unit) {
        execute(onSuccess = onSuccess, onState = onState) {
            val response = api.reauthorization(
                    access_token = access_token
            ).execute()
            if (response.isSuccessful) {
                val resultOAuth = response.body() as OAuthModel
                resultOAuth.access_token
            } else
                ""
        }
    }
}