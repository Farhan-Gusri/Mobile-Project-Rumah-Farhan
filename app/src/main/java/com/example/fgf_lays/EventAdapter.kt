
package com.example.fgf_lays

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EventAdapter(
    private val eventList: List<Event>
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    class EventViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val gambar: ImageView =
            itemView.findViewById(R.id.ivEvent)

        val nama: TextView =
            itemView.findViewById(R.id.tvNamaEvent)

        val tanggal: TextView =
            itemView.findViewById(R.id.tvTanggalEvent)

        val lokasi: TextView =
            itemView.findViewById(R.id.tvLokasiEvent)

        val status: TextView =
            itemView.findViewById(R.id.tvStatusEvent)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event, parent, false)

        return EventViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: EventViewHolder,
        position: Int
    ) {
        val event = eventList[position]

        holder.gambar.setImageResource(event.gambar)
        holder.nama.text = event.nama
        holder.tanggal.text = event.tanggal
        holder.lokasi.text = event.lokasi
        holder.status.text = event.status

        when (event.status) {
            "Aktif" -> holder.status.setBackgroundColor(
                android.graphics.Color.rgb(76, 175, 80)
            )

            "Mendatang" -> holder.status.setBackgroundColor(
                android.graphics.Color.rgb(33, 150, 243)
            )

            else -> holder.status.setBackgroundColor(
                android.graphics.Color.rgb(158, 158, 158)
            )
        }
    }

    override fun getItemCount(): Int = eventList.size
}
