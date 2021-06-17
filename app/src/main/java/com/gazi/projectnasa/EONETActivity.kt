package com.gazi.projectnasa

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

val nmToKmh = 1.8519984;
val knotToKmh = 1.852;

class EONETActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eonet)

        val eventList : RecyclerView = findViewById(R.id.activity_eonet_event_list);
        eventList.setHasFixedSize(true);

        eventList.layoutManager = LinearLayoutManager(this@EONETActivity,
            LinearLayoutManager.VERTICAL, false);

        eventList.addItemDecoration(DividerItemDecoration(this@EONETActivity,
            LinearLayoutManager.VERTICAL));

        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        parser.setTimeZone(TimeZone.getTimeZone("GMT"))
        val formatter = SimpleDateFormat("yyyy-MM-dd")

        val baseUrl = "https://eonet.sci.gsfc.nasa.gov/api/v3/";

        val EONETdatas : MutableList<EONETData> = arrayListOf();

        eventList.adapter = EONETAdapter(this@EONETActivity, EONETdatas);

        val jsonConverter = GsonConverterFactory.create(GsonBuilder().create())
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(jsonConverter)
            .build();

        val service = retrofit.create(WSEONETInterface::class.java)

        fun getMagnitude(magnitude : Int?, unit: String?) : Double? {
            if (magnitude == null)
                return null;
            if (unit == "NM^2"){
                return magnitude * nmToKmh
            } else if (unit == "kts"){
                return magnitude * knotToKmh
            }
            return null;
        }

        val callback: Callback<EONETObject> = object : Callback<EONETObject> {
            override fun onResponse(
                call: Call<EONETObject>?,
                response: Response<EONETObject>?
            ) {
                if (response != null) {
                    if (response.isSuccessful) {
                        response.body()?.let { data ->
                            for (elem in data.events){

                                var parsedDate : String = formatter.format(parser.parse(elem.geometry[0].date));
                                EONETdatas.add(EONETData(elem.title, elem.categories[0].title,
                                    parsedDate,
                                    getMagnitude(elem.geometry[0].magnitudeValue, elem.geometry[0].magnitudeUnit)))
                            }
                            eventList.adapter = EONETAdapter(this@EONETActivity, EONETdatas);
                            Log.d("eventList", EONETdatas.toString());


                            Log.d("onresponse", "adapter")
                        }
                    } else {
                        Log.d("MyWSMessage", "WS Server Error " + response.code().toString())

                    }
                }
            }

            override fun onFailure(call: Call<EONETObject>?, t: Throwable?) {
                if (t != null) {
                    Log.d("MyWSMessage", "WS Error " + t.message)
                }
            }
        }
        service.getAllEONETList().enqueue(callback)

    }
}