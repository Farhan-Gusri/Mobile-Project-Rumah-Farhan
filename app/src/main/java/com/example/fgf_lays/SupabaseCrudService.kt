
package com.example.fgf_lays

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

object SupabaseCrudService {

    private fun request(
        context: Context,
        table: String,
        method: String,
        id: String? = null,
        data: JSONObject? = null
    ): String {
        return try {
            val prefs = context.getSharedPreferences(
                "UserPref",
                Context.MODE_PRIVATE
            )

            val token = prefs.getString("SUPABASE_ACCESS_TOKEN", null)
                ?: return "Token login tidak ditemukan. Silakan login ulang."

            val query = if (id != null) {
                "?id=eq.${URLEncoder.encode(id, "UTF-8")}"
            } else {
                "?select=*"
            }

            val url = URL(
                "${SupabaseConfig.SUPABASE_URL}/rest/v1/$table$query"
            )

            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = method
            connection.connectTimeout = 15000
            connection.readTimeout = 15000

            connection.setRequestProperty("apikey", SupabaseConfig.SUPABASE_KEY)
            connection.setRequestProperty("Authorization", "Bearer $token")
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Accept", "application/json")

            if (method == "POST" || method == "PATCH") {
                connection.doOutput = true
                connection.setRequestProperty("Prefer", "return=representation")

                connection.outputStream.use {
                    it.write((data ?: JSONObject()).toString().toByteArray())
                }
            }

            val code = connection.responseCode
            val response = if (code in 200..299) {
                connection.inputStream.bufferedReader().use { it.readText() }
            } else {
                connection.errorStream?.bufferedReader()?.use {
                    it.readText()
                } ?: "Tidak ada detail kesalahan"
            }

            connection.disconnect()

            if (code in 200..299) {
                "BERHASIL: $response"
            } else {
                "GAGAL HTTP $code: $response"
            }

        } catch (e: Exception) {
            "ERROR: ${e.message}"
        }
    }

    // READ
    fun getEvents(context: Context) =
        request(context, "events", "GET")

    fun getPertandingan(context: Context) =
        request(context, "pertandingan", "GET")

    // CREATE
    fun addEvent(context: Context, data: JSONObject) =
        request(context, "events", "POST", data = data)

    fun addPertandingan(context: Context, data: JSONObject) =
        request(context, "pertandingan", "POST", data = data)

    // UPDATE
    fun updateEvent(context: Context, id: String, data: JSONObject) =
        request(context, "events", "PATCH", id, data)

    fun updatePertandingan(context: Context, id: String, data: JSONObject) =
        request(context, "pertandingan", "PATCH", id, data)

    // DELETE
    fun deleteEvent(context: Context, id: String) =
        request(context, "events", "DELETE", id)

    fun deletePertandingan(context: Context, id: String) =
        request(context, "pertandingan", "DELETE", id)
}
