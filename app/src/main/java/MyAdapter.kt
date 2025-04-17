package com.example.gracepananggung

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val namaList: ArrayList<ItemData>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    var onItemClick: ((ItemData) -> Unit)? = null

    class MyViewHolder(itemData: View) : RecyclerView.ViewHolder(itemData) {
        val gambar: ImageView = itemData.findViewById(R.id.gambar1)
        val judul: TextView = itemData.findViewById(R.id.judul1)
        val genre: TextView = itemData.findViewById(R.id.genre1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemData = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_data, parent, false)
        return MyViewHolder(itemData)
    }

    override fun getItemCount(): Int = namaList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = namaList[position]
        holder.gambar.setImageResource(item.gambar)
        holder.judul.text = item.judul
        holder.genre.text = item.genre

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(item)
        }
    }
}
