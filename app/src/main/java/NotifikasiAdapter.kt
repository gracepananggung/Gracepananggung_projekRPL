package com.example.gracepananggung

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NotifikasiAdapter(private val notifikasiList: List<Notifikasi>) :
    RecyclerView.Adapter<NotifikasiAdapter.NotifikasiViewHolder>() {

    class NotifikasiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPesan: TextView = itemView.findViewById(R.id.tvPesan)
        val tvTanggal: TextView = itemView.findViewById(R.id.tvTanggal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifikasiViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notifikasi, parent, false)
        return NotifikasiViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotifikasiViewHolder, position: Int) {
        val notifikasi = notifikasiList[position]
        holder.tvPesan.text = notifikasi.pesan
        holder.tvTanggal.text = notifikasi.tanggal
    }

    override fun getItemCount(): Int = notifikasiList.size
}
