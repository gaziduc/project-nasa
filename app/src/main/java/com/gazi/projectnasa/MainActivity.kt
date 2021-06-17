package com.gazi.projectnasa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var button: Button = findViewById(R.id.apod_button);
        button.setOnClickListener {
            val intent = Intent(this@MainActivity, ApodActivity::class.java)
            startActivity(intent)
        }
    }


}