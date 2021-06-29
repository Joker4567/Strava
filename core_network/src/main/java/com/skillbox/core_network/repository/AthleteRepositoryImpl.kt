package com.skillbox.core_network.repository

import com.skillbox.core_db.pref.Pref
import com.skillbox.core_network.api.AthleteApi
import com.skillbox.core_network.utils.BaseRepository
import com.skillbox.core_network.utils.ErrorHandler
import com.skillbox.core_network.utils.State
import com.skillbox.shared_model.ActivityType
import com.skillbox.shared_model.Athlete
import com.skillbox.shared_model.СreateActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AthleteRepositoryImpl @Inject constructor(
        errorHandler: ErrorHandler,
        private val apiAthlete: AthleteApi,
        private val pref: Pref
) : BaseRepository(errorHandler = errorHandler), AthleteRepository {

    override suspend fun getAthlete(onSuccess: (Athlete?) -> Unit, onState: (State) -> Unit) {
        execute(onSuccess = onSuccess, onState = onState) {
            val response = apiAthlete.getAthlete().execute()
            if(response.isSuccessful){
                val resultModel = response.body() as Athlete
                pref.nameProfile = "${resultModel.lastname} ${resultModel.firstname}"
                pref.photoprofile = resultModel.profile ?: ""
                resultModel
            } else
                null
        }
    }

    override suspend fun getListAthlete(onSuccess: (List<СreateActivity>) -> Unit, onState: (State) -> Unit) {
        execute(onSuccess = onSuccess, onState = onState) {
            val response = apiAthlete.getActivities().execute()
            if(response.isSuccessful){
                val resultModel = response.body() as List<СreateActivity>
                resultModel
            } else
                emptyList()
        }
    }

    override suspend fun postActivities(
            name: String,
            type: ActivityType,
            date: String,
            time: Int,
            description: String?,
            distance: Float) : Boolean {
        return withContext(Dispatchers.IO) {
            val response = apiAthlete.createActivities(name, type.name, date, time, description, distance).execute()
            response.isSuccessful
        }
    }

    override suspend fun putWeightAthlete(weight: Int, onSuccess: (Boolean) -> Unit, onState: (State) -> Unit) {
        execute(onSuccess = onSuccess, onState = onState) {
            val response = apiAthlete.putWeightProfile(weight).execute()
            response.isSuccessful
        }
    }
}