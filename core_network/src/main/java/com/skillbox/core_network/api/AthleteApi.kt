package com.skillbox.core_network.api

import com.skillbox.shared_model.Athlete
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AthleteApi {
    @GET("api/v3/athlete")
    fun getAthlete(): Call<Athlete>

    @GET("api/v3/athlete/activities")

    fun getActivities(
            @Query("page") page: Int = 1,
            @Query("per_page") per_page: Int = 30
    ): Call<List<Athlete>>
}