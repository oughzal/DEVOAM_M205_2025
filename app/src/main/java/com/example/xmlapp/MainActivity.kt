package com.example.xmlapp

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.xmlapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCalculer.setOnClickListener {
            val nb1 = binding.etNb1.text.toString().toIntOrNull() ?: 0
            val nb2 = binding.etNb2.text.toString().toIntOrNull() ?: 0
            val somme = nb1 + nb2
            binding.tvResult.text = "$somme"
        }
        binding.cbHideResult.setOnCheckedChangeListener { _,checked ->
            if (checked) {
                binding.tvResult.visibility = View.INVISIBLE

            } else {
                binding.tvResult.visibility = View.VISIBLE
            }
        }
    }
}