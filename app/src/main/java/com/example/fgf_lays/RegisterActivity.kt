package com.example.fgf_lays

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Pastikan nama layout XML kamu activity_register
        setContentView(R.layout.activity_register)

        val etNama = findViewById<EditText>(R.id.etNama)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etTanggalLahir = findViewById<EditText>(R.id.etTanggalLahir)
        val rgJenisKelamin = findViewById<RadioGroup>(R.id.rgJenisKelamin)
        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etConfirmPassword = findViewById<EditText>(R.id.etConfirmPassword)
        val btnSelanjutnya = findViewById<Button>(R.id.btnSelanjutnya)

        // DatePicker Tanggal Lahir
        etTanggalLahir.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val dateStr = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                etTanggalLahir.setText(dateStr)
            }, year, month, day)

            datePicker.show()
        }

        // Simpan ke SharedPreferences
        btnSelanjutnya.setOnClickListener {
            val nama = etNama.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val tglLahir = etTanggalLahir.text.toString().trim()
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            val selectedGenderId = rgJenisKelamin.checkedRadioButtonId
            val gender = if (selectedGenderId != -1) {
                findViewById<RadioButton>(selectedGenderId).text.toString()
            } else ""

            if (nama.isEmpty() || email.isEmpty() || tglLahir.isEmpty() ||
                gender.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Harap isi semua kolom!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Password tidak cocok!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sharedPref = getSharedPreferences("UserPref", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString("NAMA", nama)
            editor.putString("EMAIL", email)
            editor.putString("TGL_LAHIR", tglLahir)
            editor.putString("JENIS_KELAMIN", gender)
            editor.putString("USERNAME", username)
            editor.putString("PASSWORD", password)
            editor.apply()

            Toast.makeText(this, "Data Berhasil Disimpan!", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, VerificationActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}