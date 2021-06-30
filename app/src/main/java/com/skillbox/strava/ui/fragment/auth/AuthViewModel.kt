package com.skillbox.strava.ui.fragment.auth

import com.skillbox.core.platform.BaseViewModel
import com.skillbox.core.utils.SingleLiveEvent
import com.skillbox.core_network.repository.AthleteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
        private val repository: AthleteRepository
) : BaseViewModel() {

    val authObserver = SingleLiveEvent<Boolean>()

    fun getIsAthlete() {
        launchIO {
            repository.getAthlete({ athlete ->
                athlete?.let {
                    authObserver.postValue(true)
                }
            }, ::handleState)
        }
    }
}