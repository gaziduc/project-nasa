package com.gazi.projectnasa

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RoverActivity : AppCompatActivity()  {
    private lateinit var roverRecyclerView : RecyclerView;
    private lateinit var roverDatas : MutableList<RoverData>;
    private lateinit var roversData : RoversObject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rover)

        roverRecyclerView = findViewById(R.id.activity_rover_recycler_view);
        roverDatas = arrayListOf();

        roverRecyclerView.setHasFixedSize(true);

        roverRecyclerView.layoutManager = LinearLayoutManager(this@RoverActivity,
            LinearLayoutManager.VERTICAL, false);

        roverRecyclerView.addItemDecoration(DividerItemDecoration(this@RoverActivity,
            LinearLayoutManager.VERTICAL)
        );
        val baseUrl = "https://api.nasa.gov/mars-photos/api/v1/";
        val token = "Eql0R2RKwAT3SUvJmXPivXpQeeYgCfUp5DAULXam"

        val roverSpinner : Spinner = findViewById(R.id.activity_rover_spinner_rover)
        val cameraSpinner : Spinner = findViewById(R.id.activity_rover_spinner_camera)

        roverRecyclerView.adapter = RoverAdapter(roverDatas);

        val jsonConverter = GsonConverterFactory.create(GsonBuilder().create())
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(jsonConverter)
            .build();

        val service = retrofit.create(WSRoverInterface::class.java)

        roversData = RoversObject(rovers = listOf())

        val callback: Callback<RoversObject> = object : Callback<RoversObject> {
            override fun onResponse(
                call: Call<RoversObject>?,
                response: Response<RoversObject>?
            ) {
                if (response != null) {
                    if (response.isSuccessful) {
                        response.body()?.let { data ->
                            roversData = data
                            val roverNameList = data.rovers.toSet().map { rover -> rover.name }
                            val roverSpinnerAdapter = ArrayAdapter(this@RoverActivity, android.R.layout.simple_spinner_dropdown_item, roverNameList)
                            val roverClickListener = object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                    val cameraNameList : MutableList<String> = data.rovers[position].cameras.toSet().map { camera -> camera.name } as MutableList<String>
                                    cameraNameList.set(0, "None")
                                    val cameraSpinnerAdapter = ArrayAdapter(this@RoverActivity, android.R.layout.simple_spinner_dropdown_item, cameraNameList)

                                    cameraSpinner.adapter = cameraSpinnerAdapter
                                }

                                override fun onNothingSelected(parent: AdapterView<*>?) {
                                    val cameraNameList : MutableList<String> = data.rovers[0].cameras.toSet().map { camera -> camera.name } as MutableList<String>
                                    cameraNameList.set(0, "None")
                                    val cameraSpinnerAdapter = ArrayAdapter(this@RoverActivity, android.R.layout.simple_spinner_dropdown_item, cameraNameList)

                                    cameraSpinner.adapter = cameraSpinnerAdapter
                                }

                            }

                            roverSpinner.adapter = roverSpinnerAdapter
                            roverSpinner.onItemSelectedListener = roverClickListener
                        }
                    } else {
                        Log.d("MyWSMessage", "WS Server Error " + response.code().toString())

                    }
                }
            }

            override fun onFailure(call: Call<RoversObject>?, t: Throwable?) {
                if (t != null) {
                    Log.d("MyWSMessage", "WS Error " + t.message)
                }
            }
        }
        service.getAllRovers(token).enqueue(callback)


        
        val searchButton : Button = findViewById(R.id.activity_rover_btn_search)
        searchButton.setOnClickListener {

            val selectedRover = if (roverSpinner.selectedItemPosition != -1)
                roversData.rovers[roverSpinner.selectedItemPosition]
            else
                roversData.rovers[0]

            val selectedCamera = if (cameraSpinner.selectedItemPosition != -1)
                selectedRover.cameras[cameraSpinner.selectedItemPosition].name
            else
                ""

            roverDatas = arrayListOf();

            val callbackCameraPhotos : Callback<PhotosObject> = object : Callback<PhotosObject> {
                override fun onResponse(
                    call: Call<PhotosObject>,
                    response: Response<PhotosObject>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { data ->
                            val photosOfSelectedCamera = data.photos.filter { photo -> photo.camera.name == selectedCamera }.take(25)
                            val additionalPhotos = if (photosOfSelectedCamera.size < 25)
                                data.photos.filter { photo -> photo.camera.name != selectedCamera }.take(25 - photosOfSelectedCamera.size)
                            else listOf()

                            for (elem in photosOfSelectedCamera + additionalPhotos) {
                                roverDatas.add(RoverData(elem.img_src, selectedRover.name, elem.earth_date, elem.camera.name))
                            }

                            roverRecyclerView.adapter = RoverAdapter(roverDatas)
                        }
                    } else {
                        Log.d("MyWSMessage", "WS Server Error " + response.code().toString())

                    }
                }

                override fun onFailure(call: Call<PhotosObject>, t: Throwable) {
                    Log.d("MyWSMessage", "WS Error " + t.message)
                }

            }

            val callbackRoverManifest : Callback<ManifestObject> = object : Callback<ManifestObject> {
                override fun onResponse(
                    call: Call<ManifestObject>,
                    response: Response<ManifestObject>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { data ->
                            val selectedCameraData = if (selectedCamera != "")
                                data.photo_manifest.photos.filter { sol -> sol.cameras.contains(selectedCamera) && sol.total_photos > 25 }
                            else
                                data.photo_manifest.photos.filter { sol -> sol.total_photos > 25 }
                            service.getRoverPhotos(selectedRover.name, token, selectedCameraData.last().sol).enqueue(callbackCameraPhotos)
                        }
                    } else {
                        Log.d("MyWSMessage", "WS Server Error " + response.code().toString())
                    }
                }

                override fun onFailure(call: Call<ManifestObject>, t: Throwable) {
                    Log.d("MyWSMessage", "WS Error " + t.message)
                }

            }
            service.getRoverManifest(selectedRover.name, token).enqueue(callbackRoverManifest)
        }
    }
}