package com.gazi.projectnasa

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Calendar

class ApodActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apod)

        var baseURL = "https://api.nasa.gov/planetary/"
        val token = "Eql0R2RKwAT3SUvJmXPivXpQeeYgCfUp5DAULXam"

        val calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -6)

        val date = SimpleDateFormat("yyyy-MM-dd").format(calendar.time)

        val jsonConverter = GsonConverterFactory.create(GsonBuilder().create())
        val retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(jsonConverter)
            .build()

        val service = retrofit.create(WSInterface::class.java)

        val callback : Callback<List<ApodObject>> = object : Callback<List<ApodObject>> {
            override fun onResponse(
                call: Call<List<ApodObject>>,
                response: Response<List<ApodObject>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        Glide
                            .with(this@ApodActivity)
                            .load(data[6].url)
                            .centerCrop()
                            .into(findViewById(R.id.apod_activity_main_image))

                        val apodRecyclerView: RecyclerView = findViewById(R.id.recycler_view_apod)
                        apodRecyclerView.layoutManager = LinearLayoutManager(this@ApodActivity)
                        apodRecyclerView.adapter = ImageAdapter(data)

                    }
                } else {
                    Log.d("MyWSMessage", "WS Server Error " + response.code().toString())

                }
            }

            override fun onFailure(call: Call<List<ApodObject>>, t: Throwable) {
                Log.d("MyWSMessage", "WS Error " + t.message)
            }

        }

        service.getAllImages(token, date).enqueue(callback)
    }
}