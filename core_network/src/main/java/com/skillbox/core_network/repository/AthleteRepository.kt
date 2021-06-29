package com.skillbox.core_network.repository

import com.skillbox.core_network.utils.State
import com.skillbox.shared_model.ActivityType
import com.skillbox.shared_model.Athlete
import com.skillbox.shared_model.СreateActivity

interface AthleteRepository {
    suspend fun getAthlete(
            onSuccess: (Athlete?) -> Unit,
            onState: (State) -> Unit
    )

    suspend fun getListAthlete(
            onSuccess: (List<СreateActivity>) -> Unit,
            onState: (State) -> Unit
    )

    suspend fun postActivities(
            name: String,
            type: ActivityType,
            date: String,
            time: Int,
            description: String?,
            distance: Float) : Boolean
}