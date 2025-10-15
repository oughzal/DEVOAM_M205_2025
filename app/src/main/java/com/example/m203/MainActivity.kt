package com.example.m203

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    lateinit var btnOk : Button
    lateinit var img : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnOk = findViewById(R.id.btnOk)
        img = findViewById(R.id.img)
        btnOk.setText("DEVOAM")
        btnOk.setTextColor(Color.RED)
        btnOk.setOnClickListener {
            img.setImageResource(R.drawable.ofppt)
        }

    }
}