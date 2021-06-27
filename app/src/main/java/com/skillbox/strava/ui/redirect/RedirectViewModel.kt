package com.skillbox.strava.ui.redirect

import com.skillbox.core.platform.BaseViewModel
import com.skillbox.core.utils.SingleLiveEvent
import com.skillbox.core_db.pref.Pref
import com.skillbox.core_network.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RedirectViewModel @Inject constructor(
        private val authRepository: AuthRepository,
        private val pref: Pref
)  : BaseViewModel() {

    val authStateObserver = SingleLiveEvent<Boolean>()

    fun auth(code: String) {
        launchIO {
            authRepository.postAuth(code, { tokenAccess ->
                pref.accessToken = tokenAccess
                authStateObserver.postValue(true)
            }, ::handleState)
        }
    }
}