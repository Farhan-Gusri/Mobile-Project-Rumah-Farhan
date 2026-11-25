
package com.example.fgf_lays

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray

class ExploreFragment : Fragment() {

    private lateinit var rvEvent: RecyclerView
    private lateinit var adapter: EventAdapter
    private lateinit var eventList: ArrayList<Event>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(
            R.layout.fragment_explore,
            container,
            false
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        rvEvent = view.findViewById(R.id.rvEvent)
        val etCari = view.findViewById<EditText>(R.id.etCariEvent)
        val btnKelola = view.findViewById<Button>(R.id.btnKelolaEvent)

        eventList = arrayListOf()

        adapter = EventAdapter(eventList)

        rvEvent.layoutManager = LinearLayoutManager(requireContext())
        rvEvent.adapter = adapter

        btnKelola.setOnClickListener {
            startActivity(
                Intent(requireContext(), KelolaEventActivity::class.java)
            )
        }

        etCari.addTextChangedListener(
            object : android.text.TextWatcher {

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    val keyword = s.toString()

                    val filteredList = eventList.filter {
                        it.nama.contains(keyword, ignoreCase = true)
                    }

                    rvEvent.adapter = EventAdapter(filteredList)
                }

                override fun afterTextChanged(
                    s: android.text.Editable?
                ) {}
            }
        )

        ambilDataEvent()
    }

    private fun ambilDataEvent() {

        val contextApp = requireContext().applicationContext

        Thread {
            val response = SupabaseCrudService.getEvents(contextApp)

            activity?.runOnUiThread {

                if (!isAdded) return@runOnUiThread

                if (response.startsWith("BERHASIL")) {

                    try {
                        val jsonText = response.substringAfter("BERHASIL:").trim()
                        val jsonArray = JSONArray(jsonText)

                        eventList.clear()

                        for (i in 0 until jsonArray.length()) {

                            val item = jsonArray.getJSONObject(i)

                            eventList.add(
                                Event(
                                    nama = item.optString("nama_event"),
                                    tanggal = item.optString("tanggal"),
                                    lokasi = item.optString("lokasi"),
                                    status = item.optString("status"),
                                    gambar = R.drawable.logo_lays
                                )
                            )
                        }

                        adapter = EventAdapter(eventList)
                        rvEvent.adapter = adapter

                    } catch (e: Exception) {
                        Toast.makeText(
                            requireContext(),
                            "Gagal membaca data: ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                } else {
                    Toast.makeText(
                        requireContext(),
                        response,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }.start()
    }
}
