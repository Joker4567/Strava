package com.skillbox.core_network.repository

import com.skillbox.core_network.utils.Failure
import com.skillbox.core_network.utils.State
import com.skillbox.shared_model.network.ActivityType
import com.skillbox.shared_model.network.Athlete
import com.skillbox.shared_model.network.СreateActivity

interface AthleteRepository {
    suspend fun getAthlete(onLocal: (Boolean) -> Unit, onState: (Failure) -> Unit) : Athlete?

    suspend fun getListAthlete(onLocal: (Boolean) -> Unit, onState: (Failure) -> Unit): List<СreateActivity>?

    suspend fun postActivities(
            name: String,
            type: ActivityType,
            date: String,
            time: Int,
            description: String?,
            distance: Float,
            onLocal: (Boolean) -> Unit,
            onState: (Failure) -> Unit) : Boolean?

    suspend fun saveLocalActivities(
            name: String,
            type: ActivityType,
            date: String,
            time: Int,
            description: String?,
            distance: Float
    )

    suspend fun putWeightAthlete(weight: Int, onLocal: (Boolean) -> Unit, onState: (Failure) -> Unit) : Boolean?

    suspend fun clearProfile()
}