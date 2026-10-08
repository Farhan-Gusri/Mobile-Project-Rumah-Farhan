package com.example.fgf_lays

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.fgf_lays.databinding.ActivityLays1Binding

class Lays1 : AppCompatActivity() {

    private lateinit var binding: ActivityLays1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLays1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSubmit.setOnClickListener {
            val intent = Intent(this, AboutLays::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, HalamanKedua::class.java)
            startActivity(intent)
        }

        // Tombol menuju AuthActivity (otomatis cek SharedPreferences ke MainActivity)
        binding.btnToMain.setOnClickListener {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }
    }
}