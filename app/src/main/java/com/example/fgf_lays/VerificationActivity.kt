package com.example.fgf_lays

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class VerificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)

        val tvNama = findViewById<TextView>(R.id.tvNama)
        val tvEmail = findViewById<TextView>(R.id.tvEmail)
        val tvTglLahir = findViewById<TextView>(R.id.tvTglLahir)
        val tvJenisKelamin = findViewById<TextView>(R.id.tvJenisKelamin)
        val tvUsername = findViewById<TextView>(R.id.tvUsername)
        val tvPassword = findViewById<TextView>(R.id.tvPassword)

        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val btnKembali = findViewById<Button>(R.id.btnKembali)

        // Ambil data dari SharedPreferences
        val sharedPref = getSharedPreferences("UserPref", Context.MODE_PRIVATE)
        val nama = sharedPref.getString("NAMA", "") ?: ""
        val email = sharedPref.getString("EMAIL", "") ?: ""
        val tglLahir = sharedPref.getString("TGL_LAHIR", "") ?: ""
        val gender = sharedPref.getString("JENIS_KELAMIN", "") ?: ""
        val username = sharedPref.getString("USERNAME", "") ?: ""
        val password = sharedPref.getString("PASSWORD", "") ?: ""

        // Tampilkan ke layar
        tvNama.text = "Nama: $nama"
        tvEmail.text = "Email: $email"
        tvTglLahir.text = "Tanggal Lahir: $tglLahir"
        tvJenisKelamin.text = "Jenis Kelamin: $gender"
        tvUsername.text = "Username: $username"
        tvPassword.text = "Password: $password"

        // Tombol Kembali
        btnKembali.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Tombol Submit (Validasi Data SP)
        btnSubmit.setOnClickListener {
            if (nama.isEmpty() || email.isEmpty() || tglLahir.isEmpty() ||
                gender.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Validasi Gagal: Ada data yang kosong!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(this, "Registrasi Berhasil!", Toast.LENGTH_LONG).show()

            // Lanjut ke AuthActivity/Login
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}