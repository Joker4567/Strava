package com.skillbox.strava.ui.redirect

import android.util.Log
import com.skillbox.core.platform.BaseViewModel
import com.skillbox.core_db.pref.Pref
import com.skillbox.core_network.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RedirectViewModel @Inject constructor(
        private val authRepository: AuthRepository,
        private val pref: Pref
)  : BaseViewModel() {

    fun auth() {
        launchIO {
            authRepository.getAthlete({
                Log.d("", "")
            }, ::handleState)
        }
    }

    fun saveToken(token: String) {
        pref.authToken = token
    }
}