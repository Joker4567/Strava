package com.skillbox.core_network.api

import com.skillbox.shared_model.Athlete
import com.skillbox.shared_model.СreateActivity
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AthleteApi {
    @GET("api/v3/athlete")
    fun getAthlete(): Call<Athlete>

    @GET("api/v3/athlete/activities")

    fun getActivities(
            @Query("page") page: Int = 1,
            @Query("per_page") per_page: Int = 30
    ): Call<List<Athlete>>

    @POST("api/v3/activities")
    fun createActivities(
            @Query("name") name: String,
            @Query("type") type: String, //"Ride"
            @Query("start_date_local") date: String, //"2018-02-20T10:02:13Z"
            @Query("elapsed_time") time: Int, //18373
            @Query("description") description: String?, //null or "text desc"
            @Query("distance") distance: Float = 0F
    ) : Call<СreateActivity>
}