package com.skillbox.core_network.repository

import com.skillbox.core_network.utils.State
import com.skillbox.shared_model.network.ActivityType
import com.skillbox.shared_model.network.Athlete
import com.skillbox.shared_model.network.СreateActivity

interface AthleteRepository {
    suspend fun getAthlete(onLocal: (Boolean) -> Unit): Athlete?

    suspend fun getListAthlete(onLocal: (Boolean) -> Unit) : List<СreateActivity>

    suspend fun postActivities(
            name: String,
            type: ActivityType,
            date: String,
            time: Int,
            description: String?,
            distance: Float) : Boolean

    suspend fun saveLocalActivities(
            name: String,
            type: ActivityType,
            date: String,
            time: Int,
            description: String?,
            distance: Float
    )

    suspend fun putWeightAthlete(weight: Int, onSuccess: (Boolean) -> Unit, onState: (State) -> Unit)

    suspend fun clearProfile()
}