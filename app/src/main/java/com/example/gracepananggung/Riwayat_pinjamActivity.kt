package com.example.gracepananggung

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class RiwayatActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RiwayatPeminjamanAdapter
    private val riwayatList = mutableListOf<RiwayatPeminjaman>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat_pinjam)

        recyclerView = findViewById(R.id.recyclerViewRiwayat)
        adapter = RiwayatPeminjamanAdapter(riwayatList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        ambilDataRiwayat()
    }

    private fun ambilDataRiwayat() {
        val db = FirebaseFirestore.getInstance()
        db.collection("riwayat_peminjaman")
            .get()
            .addOnSuccessListener { result ->
                riwayatList.clear()
                for (document in result) {
                    val data = document.toObject(RiwayatPeminjaman::class.java)
                    data.id = document.id
                    riwayatList.add(data)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                // Tambahkan penanganan error jika perlu
            }
    }
}
