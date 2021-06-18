package com.gazi.projectnasa

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WSRoverInterface {
    @GET("rovers")
    fun getAllRovers(@Query("api_key") apiKey: String) : Call<RoversObject>

    @GET("rovers/{rover}/photos")
    fun getRoverPhotos(@Query("api_key") apiKey: String, @Query("sol") sol: Int, @Path("rover") rover: String) : Call<PhotosObject>
}