package com.example.gracepananggung

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gracepananggung.R
import com.example.gracepananggung.RiwayatPeminjaman
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale


class RiwayatPeminjamanAdapter(
    private val listRiwayat: List<RiwayatPeminjaman>
) : RecyclerView.Adapter<RiwayatPeminjamanAdapter.RiwayatViewHolder>() {

    inner class RiwayatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNama: TextView = itemView.findViewById(R.id.tvNamaRiwayat)
        val tvJudul: TextView = itemView.findViewById(R.id.tvJudulRiwayat)
        val tvJumlah: TextView = itemView.findViewById(R.id.tvJumlahRiwayat)
        val tvTglPinjam: TextView = itemView.findViewById(R.id.tvTanggalPinjamRiwayat)
        val tvTglKembali: TextView = itemView.findViewById(R.id.tvTanggalKembaliRiwayat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiwayatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_riwayat_peminjaman, parent, false)
        return RiwayatViewHolder(view)
    }

    override fun onBindViewHolder(holder: RiwayatViewHolder, position: Int) {
        val data = listRiwayat[position]
        holder.tvNama.text = "Nama: ${data.nama}"
        holder.tvJudul.text = "Judul: ${data.judul}"
        holder.tvJumlah.text = "Jumlah: ${data.jumlah}"

        val format = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        holder.tvTglPinjam.text = "Tanggal Pinjam: ${data.tanggalPinjam?.toDate()?.let { format.format(it) }}"
        holder.tvTglKembali.text = "Tanggal Kembali: ${data.tanggalKembali?.toDate()?.let { format.format(it) }}"
    }

    override fun getItemCount(): Int = listRiwayat.size
}
