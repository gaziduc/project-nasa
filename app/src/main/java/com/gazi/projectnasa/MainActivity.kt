package com.gazi.projectnasa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonApod: ImageButton = findViewById(R.id.apod_button)
        val buttonEONET: ImageButton = findViewById(R.id.eonet_button)
        val buttonRover: ImageButton = findViewById(R.id.rover_button)

        buttonApod.setOnClickListener {
            val intent = Intent(this@MainActivity, ApodActivity::class.java)
            startActivity(intent)
        }

        buttonEONET.setOnClickListener {
            val intent = Intent(this@MainActivity, EonetActivity::class.java)
            startActivity(intent);
        }

        buttonRover.setOnClickListener {
            val intent = Intent(this@MainActivity, RoverActivity::class.java)
            startActivity(intent);
        }
    }


}