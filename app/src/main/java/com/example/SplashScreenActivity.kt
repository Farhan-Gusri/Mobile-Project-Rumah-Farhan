package com.example.fgf_lays

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val sharedPref = getSharedPreferences("user_pref", Context.MODE_PRIVATE)

        lifecycleScope.launch {
            delay(2000) // Simulasi loading 2 detik

            val isLogin = sharedPref.getBoolean("isLogin", false)

            val nextActivity = if (isLogin) {
                MainActivity::class.java
            } else {
                AuthActivity::class.java
            }

            val intent = Intent(this@SplashScreenActivity, nextActivity)
            startActivity(intent)
            finish()
        }
    }
}