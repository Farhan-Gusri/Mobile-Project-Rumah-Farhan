
package com.example.fgf_lays

import android.content.Context
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

object SupabaseAuthService {

    fun register(
        nama: String,
        email: String,
        password: String,
        username: String,
        tglLahir: String,
        gender: String
    ): String {
        return try {
            val redirectUrl = URLEncoder.encode(
                "com.example.fgf_lays://login-callback",
                "UTF-8"
            )

            val url = URL(
                "${SupabaseConfig.SUPABASE_URL}/auth/v1/signup?redirect_to=$redirectUrl"
            )

            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.doOutput = true
            connection.connectTimeout = 15000
            connection.readTimeout = 15000

            connection.setRequestProperty("apikey", SupabaseConfig.SUPABASE_KEY)
            connection.setRequestProperty("Content-Type", "application/json")

            val metadata = JSONObject().apply {
                put("nama", nama)
                put("username", username)
                put("tgl_lahir", tglLahir)
                put("jenis_kelamin", gender)
            }

            val body = JSONObject().apply {
                put("email", email)
                put("password", password)
                put("data", metadata)
            }

            connection.outputStream.use {
                it.write(body.toString().toByteArray(Charsets.UTF_8))
            }

            val responseCode = connection.responseCode

            val response = if (responseCode in 200..299) {
                connection.inputStream.bufferedReader().use { it.readText() }
            } else {
                connection.errorStream?.bufferedReader()?.use {
                    it.readText()
                } ?: "Tidak ada detail kesalahan"
            }

            connection.disconnect()

            if (responseCode in 200..299) {
                "BERHASIL"
            } else {
                "Gagal HTTP $responseCode: $response"
            }

        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }

    fun login(
        context: Context,
        email: String,
        password: String
    ): String {
        return try {
            val url = URL(
                "${SupabaseConfig.SUPABASE_URL}/auth/v1/token?grant_type=password"
            )

            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.doOutput = true
            connection.connectTimeout = 15000
            connection.readTimeout = 15000

            connection.setRequestProperty("apikey", SupabaseConfig.SUPABASE_KEY)
            connection.setRequestProperty("Content-Type", "application/json")

            val body = JSONObject().apply {
                put("email", email)
                put("password", password)
            }

            connection.outputStream.use {
                it.write(body.toString().toByteArray(Charsets.UTF_8))
            }

            val responseCode = connection.responseCode

            val response = if (responseCode in 200..299) {
                connection.inputStream.bufferedReader().use { it.readText() }
            } else {
                connection.errorStream?.bufferedReader()?.use {
                    it.readText()
                } ?: "Tidak ada detail kesalahan"
            }

            connection.disconnect()

            if (responseCode in 200..299) {
                val result = JSONObject(response)
                val accessToken = result.optString("access_token")
                val userId = result.optJSONObject("user")?.optString("id") ?: ""

                context.getSharedPreferences(
                    "UserPref",
                    Context.MODE_PRIVATE
                ).edit().apply {
                    putString("SUPABASE_ACCESS_TOKEN", accessToken)
                    putString("SUPABASE_USER_ID", userId)
                    putString("EMAIL", email)
                    apply()
                }

                "BERHASIL"
            } else {
                "Gagal HTTP $responseCode: $response"
            }

        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }
}
