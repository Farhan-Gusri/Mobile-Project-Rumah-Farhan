package com.example.fgf_lays

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fgf_lays.databinding.ActivityLays1Binding

class Lays1 : AppCompatActivity() {
    private lateinit var binding: ActivityLays1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLays1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSubmit.setOnClickListener {
            val nomor = binding.inputNama.text.toString()
            if (nomor.isNotEmpty()) {
                val intent = Intent(this, AboutLays::class.java)
                intent.putExtra("NOMOR_TUJUAN", nomor)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Silakan masukkan nomor terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, HalamanKedua::class.java)
            startActivity(intent)
        }

        binding.btnToMain.setOnClickListener {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }
    }
}