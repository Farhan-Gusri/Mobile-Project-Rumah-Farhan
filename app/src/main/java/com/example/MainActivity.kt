package com.example.fgf_lays

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.example.HomeFragment
import com.example.ProfileFragment
import com.example.fgf_lays.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        applySavedTheme()

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tampilkan status teks tema pada tombol kapsul header
        updateThemeButtonUI()

        // Event Klik Tombol Tema Kapsul di Header
        binding.btnToggleTheme.setOnClickListener {
            toggleDarkMode()
        }

        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.nav_explore -> {
                    loadFragment(ExploreFragment())
                    true
                }
                R.id.nav_laporan -> {
                    loadFragment(LaporanFragment())
                    true
                }
                R.id.nav_profile -> {
                    loadFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun applySavedTheme() {
        val sharedPref = getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val isDarkMode = sharedPref.getBoolean("is_dark_mode", false)

        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun updateThemeButtonUI() {
        val sharedPref = getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val isDarkMode = sharedPref.getBoolean("is_dark_mode", false)

        if (isDarkMode) {
            binding.tvThemeText.text = "Gelap"
        } else {
            binding.tvThemeText.text = "Terang"
        }
    }

    private fun toggleDarkMode() {
        val sharedPref = getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val currentIsDark = sharedPref.getBoolean("is_dark_mode", false)
        val newMode = !currentIsDark

        sharedPref.edit {
            putBoolean("is_dark_mode", newMode)
        }

        if (newMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            Toast.makeText(this, "Mode Gelap Aktif", Toast.LENGTH_SHORT).show()
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        recreate()
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}