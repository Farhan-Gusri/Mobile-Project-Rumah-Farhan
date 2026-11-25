
package com.example.fgf_lays

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
        val tvJenisKelamin =
            findViewById<TextView>(R.id.tvJenisKelamin)
        val tvUsername = findViewById<TextView>(R.id.tvUsername)
        val tvPassword = findViewById<TextView>(R.id.tvPassword)

        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val btnKembali = findViewById<Button>(R.id.btnKembali)

        val sharedPref = getSharedPreferences(
            "UserPref",
            MODE_PRIVATE
        )

        val nama = sharedPref.getString("NAMA", "") ?: ""
        val email = sharedPref.getString("EMAIL", "") ?: ""
        val tglLahir = sharedPref.getString("TGL_LAHIR", "") ?: ""
        val gender = sharedPref.getString("JENIS_KELAMIN", "") ?: ""
        val username = sharedPref.getString("USERNAME", "") ?: ""

        tvNama.text = "Nama: $nama"
        tvEmail.text = "Email: $email"
        tvTglLahir.text = "Tanggal Lahir: $tglLahir"
        tvJenisKelamin.text = "Jenis Kelamin: $gender"
        tvUsername.text = "Username: $username"
        tvPassword.text = "Password: tidak ditampilkan demi keamanan"

        btnKembali.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnSubmit.setOnClickListener {
            if (nama.isEmpty() || email.isEmpty() ||
                tglLahir.isEmpty() || gender.isEmpty() ||
                username.isEmpty()
            ) {
                Toast.makeText(
                    this,
                    "Data pendaftaran tidak lengkap!",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            Toast.makeText(
                this,
                "Pendaftaran dikirim. Silakan periksa email untuk konfirmasi jika diminta.",
                Toast.LENGTH_LONG
            ).show()

            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
