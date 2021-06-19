package com.gazi.projectnasa

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WSRoverInterface {
    @GET("rovers")
    fun getAllRovers(@Query("api_key") apiKey: String) : Call<RoversObject>

    @GET("manifests/{rover}")
    fun getRoverManifest(@Path("rover") rover: String, @Query("api_key") apiKey: String) : Call<ManifestObject>

    @GET("rovers/{rover}/photos")
    fun getRoverPhotos(@Path("rover") rover: String, @Query("api_key") apiKey: String, @Query("sol") sol: Int) : Call<PhotosObject>
}