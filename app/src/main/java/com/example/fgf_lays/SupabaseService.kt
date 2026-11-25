
package com.example.fgf_lays

import java.net.HttpURLConnection
import java.net.URL

object SupabaseService {

    fun testConnection(): String {
        return try {
            val url = URL(
                "${SupabaseConfig.SUPABASE_URL}/auth/v1/health"
            )

            val connection = url.openConnection() as HttpURLConnection

            connection.requestMethod = "GET"
            connection.connectTimeout = 30000
            connection.readTimeout = 30000

            connection.setRequestProperty(
                "apikey",
                SupabaseConfig.SUPABASE_KEY
            )

            val responseCode = connection.responseCode

            val responseMessage = if (responseCode in 200..299) {
                connection.inputStream.bufferedReader().use {
                    it.readText()
                }
            } else {
                connection.errorStream?.bufferedReader()?.use {
                    it.readText()
                } ?: "Tidak ada detail dari server"
            }

            connection.disconnect()

            if (responseCode in 200..299) {
                "Koneksi Supabase berhasil! HTTP: $responseCode"
            } else {
                "Koneksi gagal HTTP: $responseCode\n$responseMessage"
            }

        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }
}
