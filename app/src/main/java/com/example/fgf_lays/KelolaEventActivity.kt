
package com.example.fgf_lays

import android.os.Bundle
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject

class KelolaEventActivity : AppCompatActivity() {

    private lateinit var nama: EditText
    private lateinit var tanggal: EditText
    private lateinit var lokasi: EditText
    private lateinit var deskripsi: EditText
    private lateinit var status: EditText
    private lateinit var id: EditText
    private lateinit var hasil: TextView

    private fun field(hint: String): EditText {
        return EditText(this).apply {
            this.hint = hint
            textSize = 15f
            setPadding(20, 12, 20, 12)
        }
    }

    private fun button(text: String, action: () -> Unit) {
        val btn = Button(this)
        btn.text = text
        btn.setOnClickListener { action() }
        layout.addView(btn)
    }

    private lateinit var layout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 24, 24, 24)
        }

        val scroll = ScrollView(this)
        scroll.addView(layout)
        setContentView(scroll)

        val title = TextView(this).apply {
            text = "Kelola Data Events"
            textSize = 24f
            setPadding(0, 0, 0, 20)
        }
        layout.addView(title)

        id = field("ID Event (untuk Ubah/Hapus)")
        nama = field("Nama Event")
        tanggal = field("Tanggal (YYYY-MM-DD)")
        lokasi = field("Lokasi")
        deskripsi = field("Deskripsi")
        status = field("Status (Mendatang/Selesai)")

        layout.addView(id)
        layout.addView(nama)
        layout.addView(tanggal)
        layout.addView(lokasi)
        layout.addView(deskripsi)
        layout.addView(status)

        button("TAMBAH EVENT") { tambahEvent() }
        button("TAMPILKAN EVENT") { tampilkanEvent() }
        button("UBAH EVENT") { ubahEvent() }
        button("HAPUS EVENT") { hapusEvent() }

        hasil = TextView(this).apply {
            textSize = 15f
            setPadding(0, 20, 0, 20)
        }
        layout.addView(hasil)

        tampilkanEvent()
    }

    private fun dataEvent(): JSONObject {
        return JSONObject().apply {
            put("nama_event", nama.text.toString())
            put("tanggal", tanggal.text.toString())
            put("lokasi", lokasi.text.toString())
            put("deskripsi", deskripsi.text.toString())
            put("status", status.text.toString())
        }
    }

    private fun jalankan(aksi: () -> String) {
        hasil.text = "Memproses..."
        Thread {
            val response = aksi()
            runOnUiThread {
                hasil.text = response
                if (response.startsWith("BERHASIL")) {
                    if (response.contains("[")) {
                        tampilkanJson(response)
                    }
                }
            }
        }.start()
    }

    private fun tambahEvent() {
        jalankan {
            SupabaseCrudService.addEvent(this, dataEvent())
        }
    }

    private fun tampilkanEvent() {
        jalankan {
            SupabaseCrudService.getEvents(this)
        }
    }

    private fun ubahEvent() {
        val eventId = id.text.toString().trim()
        if (eventId.isEmpty()) {
            Toast.makeText(this, "Masukkan ID Event", Toast.LENGTH_SHORT).show()
            return
        }
        jalankan {
            SupabaseCrudService.updateEvent(this, eventId, dataEvent())
        }
    }

    private fun hapusEvent() {
        val eventId = id.text.toString().trim()
        if (eventId.isEmpty()) {
            Toast.makeText(this, "Masukkan ID Event", Toast.LENGTH_SHORT).show()
            return
        }
        jalankan {
            SupabaseCrudService.deleteEvent(this, eventId)
        }
    }

    private fun tampilkanJson(response: String) {
        try {
            val jsonText = response.substringAfter("BERHASIL:").trim()
            val array = JSONArray(jsonText)
            val teks = StringBuilder("DAFTAR EVENTS\n\n")

            for (i in 0 until array.length()) {
                val event = array.getJSONObject(i)
                teks.append("ID: ${event.optString("id")}\n")
                teks.append("Nama: ${event.optString("nama_event")}\n")
                teks.append("Tanggal: ${event.optString("tanggal")}\n")
                teks.append("Lokasi: ${event.optString("lokasi")}\n")
                teks.append("Deskripsi: ${event.optString("deskripsi")}\n")
                teks.append("Status: ${event.optString("status")}\n\n")
            }

            hasil.text = teks.toString()
        } catch (e: Exception) {
            hasil.text = response
        }
    }
}
