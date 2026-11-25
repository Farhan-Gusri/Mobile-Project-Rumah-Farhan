
package com.example.fgf_lays

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class OnboardingActivity : AppCompatActivity() {

    private var currentPage = 0

    private val titles = arrayOf(
        "Selamat Datang di Garuda Melayu",
        "Kelola Event Lebih Mudah",
        "Informasi Kegiatan Terorganisir"
    )

    private val descriptions = arrayOf(
        "Aplikasi manajemen event dan informasi kegiatan Garuda Melayu.",
        "Pantau event aktif, pertandingan, dan kegiatan dalam satu aplikasi.",
        "Kelola informasi dan laporan kegiatan secara praktis dan terstruktur."
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        val tvTitle = findViewById<TextView>(R.id.tvOnboardingTitle)
        val tvDescription = findViewById<TextView>(R.id.tvOnboardingDescription)
        val tvIndicator = findViewById<TextView>(R.id.tvIndicator)
        val btnNext = findViewById<Button>(R.id.btnNext)
        val btnSkip = findViewById<Button>(R.id.btnSkip)

        fun updatePage() {
            tvTitle.text = titles[currentPage]
            tvDescription.text = descriptions[currentPage]
            tvIndicator.text = "${currentPage + 1} / 3"

            btnNext.text = if (currentPage == 2) {
                "Mulai Sekarang"
            } else {
                "Selanjutnya"
            }
        }

        fun finishOnboarding() {
            getSharedPreferences("app_pref", MODE_PRIVATE)
                .edit()
                .putBoolean("onboarding_completed", true)
                .apply()

            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }

        btnNext.setOnClickListener {
            if (currentPage < 2) {
                currentPage++
                updatePage()
            } else {
                finishOnboarding()
            }
        }

        btnSkip.setOnClickListener {
            finishOnboarding()
        }

        updatePage()
    }
}
