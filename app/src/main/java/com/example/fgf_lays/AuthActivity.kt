package com.example.fgf_lays

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnLogin?.setOnClickListener {
            val inputUser = etUsername.text.toString().trim()
            val inputPass = etPassword.text.toString().trim()

            // Jika inputan masih kosong, langsung tolak
            if (inputUser.isEmpty() || inputPass.isEmpty()) {
                Toast.makeText(this, "Username dan Password tidak boleh kosong!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Ambil data user yang pernah mendaftar di SharedPreferences
            val sharedPref = getSharedPreferences("UserPref", Context.MODE_PRIVATE)
            val spUser = sharedPref.getString("USERNAME", "") ?: ""
            val spPass = sharedPref.getString("PASSWORD", "") ?: ""

            // Pengecekan 2 Aturan Soal:
            // Aturan 1: Username sama dengan Password
            val isRule1Valid = (inputUser == inputPass)

            // Aturan 2: Username & Password cocok dengan data SharedPreferences
            val isRule2Valid = (spUser.isNotEmpty() && inputUser == spUser && inputPass == spPass)

            // Jalankan salah satu aturan
            if (isRule1Valid || isRule2Valid) {
                Toast.makeText(this, "Login Berhasil!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Login Gagal: Username atau Password tidak sesuai!", Toast.LENGTH_SHORT).show()
            }
        }

        // Navigasi ke Halaman Registrasi
        btnRegister?.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}