package com.skillbox.strava.ui.fragment.profile

import android.util.Log
import com.skillbox.core.platform.BaseViewModel
import com.skillbox.core.utils.SingleLiveEvent
import com.skillbox.core_network.repository.AthleteRepository
import com.skillbox.shared_model.network.Athlete
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
        private val repository: AthleteRepository
) : BaseViewModel() {

    val athleteObserver = SingleLiveEvent<Athlete>()
    val reAuthStateObserver = SingleLiveEvent<Boolean>()

    fun getAthlete() {
        launchIO {
            val athlete = repository.getAthlete(::localData)
            launch {
                athlete?.let { athleteObserver.postValue(it) }
            }
        }
    }

    fun changeWeight(weight: Int) {
        launchIO {
            repository.putWeightAthlete(weight, { isSuccess ->
                isSuccess?.let {
                    if(isSuccess)
                        Log.d("ProfileViewModel", "Вес успешно изменён")
                    else
                        Log.d("ProfileViewModel", "Ошибка сохранения веса на веб-сервер")
                }
            }, ::handleState)
        }
    }

    private fun localData(islocal: Boolean) {
        if(islocal)
            Log.d("ProfileViewModel", "Профиль был загружен из локальной БД")
        else
            Log.d("ProfileViewModel", "Профиль получен с веб-сервера")
    }
}