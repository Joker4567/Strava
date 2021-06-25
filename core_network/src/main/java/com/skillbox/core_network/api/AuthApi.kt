package com.skillbox.core_network.api

import com.skillbox.shared_model.Athlete
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface AuthApi {
    @GET("api/v3/athlete")
    fun getAthlete(): Call<Athlete>
}