package com.gazi.projectnasa

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WSApodInterface {
    @GET("apod")
    fun getAllImages(@Query("api_key") apiKey: String, @Query("start_date") startDate: String) : Call<List<ApodObject>>
}