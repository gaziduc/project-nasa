package com.gazi.projectnasa

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WSInterface {
    @GET("")
    fun getAllImages()
}