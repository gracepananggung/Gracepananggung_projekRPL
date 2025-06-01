package com.example.gracepananggung

import Peminjaman
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class PeminjamanAdapter(
    private val isAdmin: Boolean = false,
    private val onDeleteClick: ((Peminjaman) -> Unit)? = null
) : RecyclerView.Adapter<PeminjamanAdapter.PeminjamanViewHolder>() {

    private val dataList = mutableListOf<Peminjaman>()

    inner class PeminjamanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNama: TextView = itemView.findViewById(R.id.tvNama)
        val tvJudul: TextView = itemView.findViewById(R.id.tvJudul)
        val tvJumlah: TextView = itemView.findViewById(R.id.tvJumlah)
        val tvTanggalPinjam: TextView = itemView.findViewById(R.id.tvTanggalPinjam)
        val tvTanggalKembali: TextView = itemView.findViewById(R.id.tvTanggalKembali)
        val btnHapus: Button = itemView.findViewById(R.id.btnHapus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeminjamanViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_peminjamaan, parent, false)
        return PeminjamanViewHolder(view)
    }

    override fun onBindViewHolder(holder: PeminjamanViewHolder, position: Int) {
        val data = dataList[position]
        holder.tvNama.text = "Nama: ${data.nama}"
        holder.tvJudul.text = "Judul: ${data.judul}"
        holder.tvJumlah.text = "Jumlah: ${data.jumlah}"

        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.ROOT)
        val tanggalPinjamStr = data.tanggalPinjam?.toDate()?.let { formatter.format(it) } ?: "-"
        val tanggalKembaliStr = data.tanggalKembali?.toDate()?.let { formatter.format(it) } ?: "-"

        holder.tvTanggalPinjam.text = "Tanggal Pinjam: $tanggalPinjamStr"
        holder.tvTanggalKembali.text = "Tanggal Kembali: $tanggalKembaliStr"

        holder.btnHapus.visibility = if (isAdmin) View.VISIBLE else View.GONE
        holder.btnHapus.setOnClickListener {
            if (holder.adapterPosition != RecyclerView.NO_POSITION) {
                onDeleteClick?.invoke(data)
            }
        }
    }

    override fun getItemCount(): Int = dataList.size

    fun setData(newList: List<Peminjaman>) {
        dataList.clear()
        dataList.addAll(newList)
        notifyDataSetChanged()
    }
}
