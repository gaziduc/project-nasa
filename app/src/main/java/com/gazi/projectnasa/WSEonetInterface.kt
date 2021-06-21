package com.gazi.projectnasa

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WSEonetInterface {

    @GET("events")
    fun getAllEonetList(@Query("limit") limit : Int = 25) : Call<EonetObject>
}