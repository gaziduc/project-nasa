package com.gazi.projectnasa

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
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
import java.math.RoundingMode
import java.text.DecimalFormat

val nmToKmh = 1.8519984;
val knotToKmh = 1.852;

class EONETActivity: AppCompatActivity() {

    private lateinit var eventList : RecyclerView;
    private lateinit var EONETdatas : MutableList<EONETData>;
    private lateinit var tempEONETdatas : MutableList<EONETData>;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eonet)

        eventList = findViewById(R.id.activity_eonet_event_list);
        EONETdatas = arrayListOf();
        tempEONETdatas = arrayListOf();

        eventList.setHasFixedSize(true);

        eventList.layoutManager = LinearLayoutManager(this@EONETActivity,
            LinearLayoutManager.VERTICAL, false);

        eventList.addItemDecoration(DividerItemDecoration(this@EONETActivity,
            LinearLayoutManager.VERTICAL));

        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        parser.setTimeZone(TimeZone.getTimeZone("GMT"))
        val formatter = SimpleDateFormat("yyyy-MM-dd")

        val baseUrl = "https://eonet.sci.gsfc.nasa.gov/api/v3/";



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
            val df = DecimalFormat("#.#")
            df.roundingMode = RoundingMode.FLOOR
            if (unit == "NM^2"){
                return df.format(magnitude * nmToKmh).toDouble()
            } else if (unit == "kts"){
                return df.format(magnitude * knotToKmh).toDouble()
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

                            tempEONETdatas.addAll(EONETdatas)
                            eventList.adapter = EONETAdapter(this@EONETActivity, tempEONETdatas);
                            Log.d("eventList", EONETdatas.toString());
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.eonet_menu, menu);
        val item : MenuItem? = menu?.findItem(R.id.action_search);
        val searchView : SearchView? = item?.actionView as SearchView?;
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                tempEONETdatas.clear();
                val searchText = newText!!.lowercase(Locale.getDefault())
                if (searchText.isNotEmpty()) {
                    EONETdatas.forEach {
                        if (it.type.lowercase(Locale.getDefault()).contains(searchText)) {
                            tempEONETdatas.add(it)
                        }
                    }
                    eventList.adapter!!.notifyDataSetChanged()
                } else {
                    tempEONETdatas.clear()
                    tempEONETdatas.addAll(EONETdatas)
                    eventList.adapter!!.notifyDataSetChanged()
                }
                return false;
            }

        })
        return super.onCreateOptionsMenu(menu)
    }
}