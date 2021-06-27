package com.skillbox.strava.ui.fragment.profile

import com.skillbox.core.platform.BaseViewModel
import com.skillbox.core.utils.SingleLiveEvent
import com.skillbox.core_network.repository.AuthRepository
import com.skillbox.shared_model.Athlete
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
        private val repository: AuthRepository
) : BaseViewModel() {

    val athleteObserver = SingleLiveEvent<Athlete>()

    fun getAthlete() {
        launchIO {
            repository.getAthlete({ athlete ->
                athlete?.let { athleteObserver.postValue(it) }
            }, ::handleState)
        }
    }

}