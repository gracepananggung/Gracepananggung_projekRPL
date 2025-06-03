package com.example.gracepananggung

import com.example.gracepananggung.Buku
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(
    private val bukuList: List<Buku>,
    private val isAdmin: Boolean = false,
    private val onEditClick: ((Buku) -> Unit)? = null,
    private val onDeleteClick: ((Buku) -> Unit)? = null,
    private val onItemClick: ((Buku) -> Unit)? = null
) : RecyclerView.Adapter<MyAdapter.BukuViewHolder>() {

    inner class BukuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val judulTextView: TextView = itemView.findViewById(R.id.judul1)
        val deskripsiTextView: TextView = itemView.findViewById(R.id.deskripsi1)
        val btnEdit: Button = itemView.findViewById(R.id.btn_edit)
        val btnHapus: Button = itemView.findViewById(R.id.btn_hapus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BukuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_data, parent, false)
        return BukuViewHolder(view)
    }

    override fun onBindViewHolder(holder: BukuViewHolder, position: Int) {
        val buku = bukuList[position]

        holder.judulTextView.text = buku.judul
        holder.deskripsiTextView.text = buku.deskripsi

        if (isAdmin) {
            holder.btnEdit.visibility = View.VISIBLE
            holder.btnHapus.visibility = View.VISIBLE

            holder.btnEdit.setOnClickListener { onEditClick?.invoke(buku) }
            holder.btnHapus.setOnClickListener { onDeleteClick?.invoke(buku) }
        } else {
            holder.btnEdit.visibility = View.GONE
            holder.btnHapus.visibility = View.GONE
        }

        // ⬇️ Tambahkan klik item untuk user
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(buku)
        }
    }

    override fun getItemCount(): Int = bukuList.size
}
