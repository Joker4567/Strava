package com.skillbox.strava.ui.fragment.activities

import com.skillbox.core.platform.BaseViewModel
import com.skillbox.core.utils.SingleLiveEvent
import com.skillbox.core_network.repository.AthleteRepository
import com.skillbox.shared_model.СreateActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ActivitiesViewModel @Inject constructor(
        private val repository: AthleteRepository
)  : BaseViewModel() {

    val runnerItemsObserver = SingleLiveEvent<List<СreateActivity>>()

    fun getAthleteActivities() {
        launchIO {
            repository.getListAthlete(::list, ::handleState)
        }
    }

    private fun list(list: List<СreateActivity>) {
        runnerItemsObserver.postValue(list)
    }
}