
package com.example.fgf_lays

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TestSupabaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val resultText = TextView(this)
        resultText.text = "Menguji koneksi Supabase..."
        resultText.textSize = 18f
        resultText.setPadding(30, 50, 30, 30)

        setContentView(resultText)

        Thread {
            val result = SupabaseService.testConnection()

            runOnUiThread {
                resultText.text = result
            }
        }.start()
    }
}
