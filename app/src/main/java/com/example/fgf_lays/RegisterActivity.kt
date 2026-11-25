
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
        setContentView(R.layout.activity_register)

        val etNama = findViewById<EditText>(R.id.etNama)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etTanggalLahir = findViewById<EditText>(R.id.etTanggalLahir)
        val rgJenisKelamin = findViewById<RadioGroup>(R.id.rgJenisKelamin)
        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etConfirmPassword = findViewById<EditText>(R.id.etConfirmPassword)
        val btnSelanjutnya = findViewById<Button>(R.id.btnSelanjutnya)

        etTanggalLahir.setOnClickListener {
            val calendar = Calendar.getInstance()

            val datePicker = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val dateStr =
                        "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    etTanggalLahir.setText(dateStr)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            datePicker.show()
        }

        btnSelanjutnya.setOnClickListener {
            val nama = etNama.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val tglLahir = etTanggalLahir.text.toString().trim()
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()

            val selectedGenderId = rgJenisKelamin.checkedRadioButtonId

            val gender = if (selectedGenderId != -1) {
                findViewById<RadioButton>(selectedGenderId).text.toString()
            } else {
                ""
            }

            if (nama.isEmpty() || email.isEmpty() || tglLahir.isEmpty() ||
                gender.isEmpty() || username.isEmpty() ||
                password.isEmpty() || confirmPassword.isEmpty()
            ) {
                Toast.makeText(
                    this,
                    "Harap isi semua kolom!",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(
                    this,
                    "Password tidak cocok!",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(
                    this,
                    "Password minimal 6 karakter!",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            btnSelanjutnya.isEnabled = false
            btnSelanjutnya.text = "Mendaftarkan..."

            Thread {
                val result = SupabaseAuthService.register(
                    nama = nama,
                    email = email,
                    password = password,
                    username = username,
                    tglLahir = tglLahir,
                    gender = gender
                )

                runOnUiThread {
                    btnSelanjutnya.isEnabled = true
                    btnSelanjutnya.text = "Selanjutnya"

                    if (result == "BERHASIL") {
                        val sharedPref = getSharedPreferences(
                            "UserPref",
                            Context.MODE_PRIVATE
                        )

                        sharedPref.edit().apply {
                            putString("NAMA", nama)
                            putString("EMAIL", email)
                            putString("TGL_LAHIR", tglLahir)
                            putString("JENIS_KELAMIN", gender)
                            putString("USERNAME", username)
                            remove("PASSWORD")
                            apply()
                        }

                        Toast.makeText(
                            this,
                            "Akun berhasil dibuat. Periksa email kamu.",
                            Toast.LENGTH_LONG
                        ).show()

                        val intent = Intent(
                            this,
                            VerificationActivity::class.java
                        )
                        startActivity(intent)
                        finish()

                    } else {
                        Toast.makeText(
                            this,
                            result,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }.start()
        }
    }
}
