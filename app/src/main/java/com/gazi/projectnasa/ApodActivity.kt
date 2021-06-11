package com.gazi.projectnasa

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Calendar;

class ApodActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apod)

        var baseURL = "https://api.nasa.gov/planetary/apod?api_key=Eql0R2RKwAT3SUvJmXPivXpQeeYgCfUp5DAULXam&start_date="

        val calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -7)

        val formatter = SimpleDateFormat("yyyy-MM-dd")
        baseURL += formatter.format(calendar.time)
        println(baseURL);

        val jsonConverter = GsonConverterFactory.create(GsonBuilder().create())
        val retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(jsonConverter)
            .build()
/*
        val service : WSInterface = retrofit.create(WSInterface::class.java)

        val callback : Callback<List<ToDoObject>> = object : Callback<List<ToDoObject>> {
            override fun onResponse(
                call: Call<List<ToDoObject>>,
                response: Response<List<ToDoObject>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        Toast.makeText(this@MainActivity, "nb todo = " + it.size, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.d("MyWSMessage", "WS Server Error " + response.code().toString())

                }
            }

            override fun onFailure(call: Call<List<ToDoObject>>, t: Throwable) {
                Log.d("MyWSMessage", "WS Error " + t.message)
            }

        }

        service.getAllTodoList().enqueue(callback)*/
    }
}