package com.skillbox.strava.ui.fragment.activities

import android.util.Log
import com.skillbox.core.platform.BaseViewModel
import com.skillbox.core.utils.SingleLiveEvent
import com.skillbox.core_network.repository.AthleteRepository
import com.skillbox.shared_model.ToastModel
import com.skillbox.shared_model.network.СreateActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ActivitiesViewModel @Inject constructor(
        private val repository: AthleteRepository
)  : BaseViewModel() {

    val runnerItemsObserver = SingleLiveEvent<List<СreateActivity>>()
    val toastObserver = SingleLiveEvent<ToastModel>()

    fun getAthleteActivities() {
        launchIO {
            val resultList = repository.getListAthlete(::localData)
            launch {
                list(resultList)
            }
        }
    }

    private fun list(list: List<СreateActivity>) {
        runnerItemsObserver.postValue(list)
    }

    private fun localData(islocal: Boolean) {
        if(islocal) {
            toastObserver.postValue(ToastModel("Loaded feed from cache", isLocal = true))
            Log.d("ActivitiesViewModel", "Список активносей загрузился из локальной БД")
        }
        else {
            Log.d("ActivitiesViewModel", "Список активносей получен из веб-сервера")
        }
    }
}