package com.gazi.projectnasa

import retrofit2.Call
import retrofit2.http.GET

interface WSEonetInterface {

    @GET("events")
    fun getAllEonetList() : Call<EonetObject>
}