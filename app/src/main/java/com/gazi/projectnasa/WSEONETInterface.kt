package com.gazi.projectnasa

import retrofit2.Call
import retrofit2.http.GET

interface WSEONETInterface {

    @GET("events")
    fun getAllEONETList() : Call<EONETObject>
}