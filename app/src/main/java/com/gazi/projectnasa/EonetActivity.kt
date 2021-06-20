package com.gazi.projectnasa

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
import java.util.*
import java.math.RoundingMode
import java.text.DecimalFormat

val nmToKmh = 1.8519984
val knotToKmh = 1.852

class EonetActivity: AppCompatActivity() {

    private lateinit var eventList : RecyclerView
    private lateinit var EonetDatas : MutableList<EonetData>
    private lateinit var tempEonetDatas : MutableList<EonetData>

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eonet)

        val loadingScreen : LoadingScreen = LoadingScreen(this@EonetActivity);
        loadingScreen.startLoadingDialog();

        eventList = findViewById(R.id.activity_eonet_event_list);
        EonetDatas = arrayListOf();
        tempEonetDatas = arrayListOf();

        eventList.setHasFixedSize(true);

        eventList.layoutManager = LinearLayoutManager(this@EonetActivity,
            LinearLayoutManager.VERTICAL, false);

        eventList.addItemDecoration(DividerItemDecoration(this@EonetActivity,
            LinearLayoutManager.VERTICAL));

        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        parser.setTimeZone(TimeZone.getTimeZone("GMT"))
        val formatter = SimpleDateFormat("yyyy-MM-dd")

        val baseUrl = "https://eonet.sci.gsfc.nasa.gov/api/v3/"

        eventList.adapter = EonetAdapter(EonetDatas)

        val jsonConverter = GsonConverterFactory.create(GsonBuilder().create())
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(jsonConverter)
            .build()

        val service = retrofit.create(WSEonetInterface::class.java)

        val callback: Callback<EonetObject> = object : Callback<EonetObject> {
            override fun onResponse(
                call: Call<EonetObject>?,
                response: Response<EonetObject>?
            ) {
                if (response != null) {
                    if (response.isSuccessful) {
                        response.body()?.let { data ->
                            for (elem in data.events){

                                var parsedDate : String = formatter.format(parser.parse(elem.geometry[0].date));
                                EonetDatas.add(EonetData(elem.title, elem.categories[0].title,
                                    parsedDate,
                                    getMagnitude(elem.geometry[0].magnitudeValue, elem.geometry[0].magnitudeUnit)))
                            }

                            tempEonetDatas.addAll(EonetDatas)
                            eventList.adapter = EonetAdapter(tempEonetDatas);
                        }
                    } else {
                        Log.d("MyWSMessage", "WS Server Error " + response.code().toString())
                    }
                    loadingScreen.dismissDialog()
                }
            }

            override fun onFailure(call: Call<EonetObject>?, t: Throwable?) {
                if (t != null) {
                    Log.d("MyWSMessage", "WS Error " + t.message)
                }
            }
        }
        service.getAllEonetList().enqueue(callback)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.eonet_menu, menu);
        val item : MenuItem? = menu?.findItem(R.id.action_search);
        val searchView : SearchView? = item?.actionView as SearchView?;
        searchView?.queryHint = "Search for a specific type"
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                tempEonetDatas.clear();
                val searchText = newText!!.lowercase(Locale.getDefault())
                if (searchText.isNotEmpty()) {
                    EonetDatas.forEach {
                        if (it.type.lowercase(Locale.getDefault()).contains(searchText)) {
                            tempEonetDatas.add(it)
                        }
                    }
                    eventList.adapter!!.notifyDataSetChanged()
                } else {
                    tempEonetDatas.clear()
                    tempEonetDatas.addAll(EonetDatas)
                    eventList.adapter!!.notifyDataSetChanged()
                }
                return false;
            }

        })
        return super.onCreateOptionsMenu(menu)
    }
}