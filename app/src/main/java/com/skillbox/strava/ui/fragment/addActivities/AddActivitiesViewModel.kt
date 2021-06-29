package com.skillbox.strava.ui.fragment.addActivities

import android.util.Log
import com.skillbox.core.platform.BaseViewModel
import com.skillbox.core.utils.SingleLiveEvent
import com.skillbox.core_network.repository.AthleteRepository
import com.skillbox.shared_model.ActivityType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddActivitiesViewModel @Inject constructor(
        private val repository: AthleteRepository
) : BaseViewModel() {

}