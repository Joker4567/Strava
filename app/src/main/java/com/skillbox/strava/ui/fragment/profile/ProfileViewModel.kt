package com.skillbox.strava.ui.fragment.profile

import android.content.Context
import android.os.Build
import android.util.Log
import com.skillbox.core.platform.BaseViewModel
import com.skillbox.core.utils.SingleLiveEvent
import com.skillbox.core_db.pref.Pref
import com.skillbox.core_network.repository.AthleteRepository
import com.skillbox.core_network.repository.AuthRepository
import com.skillbox.shared_model.Athlete
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
        private val repository: AthleteRepository,
        @ApplicationContext private val appContext: Context
) : BaseViewModel() {

    val athleteObserver = SingleLiveEvent<Athlete>()
    val reAuthStateObserver = SingleLiveEvent<Boolean>()

    fun getAthlete() {
        launchIO {
            repository.getAthlete({ athlete ->
                athlete?.let { athleteObserver.postValue(it) }
            }, ::handleState)
        }
    }

    fun changeWeight(weight: Int) {
        launchIO {
            repository.putWeightAthlete(weight, { isSuccess ->
                isSuccess?.let {
                    if(isSuccess)
                        Log.d("ProfileViewModel", "Вес успешно изменён")
                }
            }, ::handleState)
        }
    }
}