package com.skillbox.core_network.repository

import android.util.Log
import com.skillbox.core_db.pref.Pref
import com.skillbox.core_db.room.dao.AthleteDao
import com.skillbox.core_network.api.AthleteApi
import com.skillbox.core_network.utils.BaseRepository
import com.skillbox.core_network.utils.ErrorHandler
import com.skillbox.core_network.utils.State
import com.skillbox.shared_model.map.mapToAthlete
import com.skillbox.shared_model.map.mapToAthleteEntities
import com.skillbox.shared_model.map.mapToСreateActivities
import com.skillbox.shared_model.map.mapToСreateActivitiesEntity
import com.skillbox.shared_model.network.ActivityType
import com.skillbox.shared_model.network.Athlete
import com.skillbox.shared_model.network.СreateActivity
import com.skillbox.shared_model.room.CreateActivitiesEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AthleteRepositoryImpl @Inject constructor(
        errorHandler: ErrorHandler,
        private val apiAthlete: AthleteApi,
        private val pref: Pref,
        private val athleteDao: AthleteDao
) : BaseRepository(errorHandler = errorHandler), AthleteRepository {

    override suspend fun getAthlete(onLocal: (Boolean) -> Unit): Athlete? =
            try {
                val response = apiAthlete.getAthlete().execute()
                if (response.isSuccessful) {
                    val resultModel = response.body() as Athlete

                    athleteDao.insertAthlete(resultModel.mapToAthleteEntities())

                    pref.nameProfile = "${resultModel.lastname} ${resultModel.firstname}"
                    pref.photoprofile = resultModel.profile ?: ""
                    onLocal.invoke(false)
                    resultModel
                } else {
                    onLocal.invoke(true)
                    athleteDao.getAthlete()?.mapToAthlete()
                }

            } catch (ex: Exception) {
                onLocal.invoke(true)
                athleteDao.getAthlete()?.mapToAthlete()
            }

    override suspend fun getListAthlete(onLocal: (Boolean) -> Unit): List<СreateActivity> =
            try {
                val response = apiAthlete.getActivities().execute()
                if (response.isSuccessful) {
                    var resultModel = parseResponse(response)
                    athleteDao.deleteAthleteActivities()
                    if (resultModel.isNotEmpty()) {
                        athleteDao.insertAthleteActivities(resultModel.map { it.mapToСreateActivitiesEntity() }.toList())
                    }
                    onLocal.invoke(false)
                    resultModel
                } else {
                    onLocal.invoke(true)
                    athleteDao.getAthleteActivities().map { it.mapToСreateActivities() }
                }
            } catch (ex: Exception) {
                onLocal.invoke(true)
                athleteDao.getAthleteActivities().map { it.mapToСreateActivities() }
            }

    override suspend fun postActivities(
            name: String,
            type: ActivityType,
            date: String,
            time: Int,
            description: String?,
            distance: Float): Boolean =
            withContext(Dispatchers.IO) {
                try {
                    val response = apiAthlete.createActivities(name, type.name, date, time, description, distance).execute()
                    response.isSuccessful
                } catch (ex: Exception) {
                    false
                }
            }

    override suspend fun saveLocalActivities(
            name: String,
            type: ActivityType,
            date: String,
            time: Int,
            description: String?,
            distance: Float
    ) = withContext(Dispatchers.IO) {
        athleteDao.insertAthleteActivities(listOf(CreateActivitiesEntity(0, name, type.name, date, time, description
                ?: "", distance)))
    }


    override suspend fun putWeightAthlete(weight: Int, onSuccess: (Boolean) -> Unit, onState: (State) -> Unit) {
        execute(onSuccess = onSuccess, onState = onState) {
            val response = apiAthlete.putWeightProfile(weight).execute()
            response.isSuccessful
        }
    }

    override suspend fun clearProfile() {
        athleteDao.deleteAthleteActivities()
        athleteDao.deleteAthlete()
    }

    private fun <T> parseResponse(response: retrofit2.Response<List<T>>): List<T> = when {
        response.raw().cacheResponse != null -> {
            ((response.raw().cacheResponse?.body) ?: response.body()) as List<T>
        }
        response.isSuccessful -> {
            response.body() as List<T>
        }
        else -> {
            Log.e("AthleteRepositoryImpl", "Code: ${response.code()}, error: ${response.errorBody()}")
            emptyList()
        }
    }
}