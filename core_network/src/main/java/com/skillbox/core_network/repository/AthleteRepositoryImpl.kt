package com.skillbox.core_network.repository

import android.util.Log
import com.skillbox.core_db.pref.Pref
import com.skillbox.core_db.room.dao.AthleteDao
import com.skillbox.core_network.api.AthleteApi
import com.skillbox.core_network.utils.BaseRepository
import com.skillbox.core_network.utils.ErrorHandler
import com.skillbox.core_network.utils.Failure
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

    override suspend fun getAthlete(onLocal: (Boolean) -> Unit, onState: (Failure) -> Unit) : Athlete?  =
        execute(onState = onState, onLocal = onLocal, func =  {
            val response = apiAthlete.getAthlete().execute()
            val resultModel = response.body() as Athlete
            pref.nameProfile = "${resultModel.lastname} ${resultModel.firstname}"
            pref.photoprofile = resultModel.profile ?: ""
            resultModel
        }, funcLocal = {
            athleteDao.getAthlete()?.mapToAthlete()
        }, funcOther = { resultModel ->
            athleteDao.insertAthlete(resultModel!!.mapToAthleteEntities())
            null
        })

    override suspend fun getListAthlete(onLocal: (Boolean) -> Unit, onState: (Failure) -> Unit): List<СreateActivity>? =
            execute(onState = onState, onLocal = onLocal, func =  {
                val response = apiAthlete.getActivities().execute()
                var resultModel = parseResponse(response)
                resultModel
            }, funcLocal = {
                athleteDao.getAthleteActivities().map { it.mapToСreateActivities() }
            }, funcOther = { resultModel ->
                athleteDao.deleteAthleteActivities()
                if (resultModel.isNotEmpty()) {
                    athleteDao.insertAthleteActivities(resultModel.map { it.mapToСreateActivitiesEntity() }.toList())
                }
                emptyList()
            })

    override suspend fun postActivities(
            name: String,
            type: ActivityType,
            date: String,
            time: Int,
            description: String?,
            distance: Float,
            onLocal: (Boolean) -> Unit, onState: (Failure) -> Unit): Boolean? =
            execute(onState = onState, onLocal = onLocal, func =  {
                val response = apiAthlete.createActivities(name, type.name, date, time, description, distance).execute()
                response.isSuccessful
            }, funcLocal = {
                false
            }, funcOther = { _ ->
                true
            })

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


    override suspend fun putWeightAthlete(weight: Int, onLocal: (Boolean) -> Unit, onState: (Failure) -> Unit) : Boolean? =
            execute(onState = onState, onLocal = onLocal, func =  {
                val response = apiAthlete.putWeightProfile(weight).execute()
                response.isSuccessful
            }, funcLocal = {
                false
            }, funcOther = { _ ->
                true
            })

    override suspend fun clearProfile() {
        athleteDao.deleteAthleteActivities()
        athleteDao.deleteAthlete()
    }

    override suspend fun getLastAthleteDate() : CreateActivitiesEntity? =
            athleteDao.getAthleteLastDate()

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