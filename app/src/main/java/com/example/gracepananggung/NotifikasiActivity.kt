package com.example.gracepananggung

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class NotifikasiActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var notifikasiList: ArrayList<Notifikasi>
    private lateinit var adapter: NotifikasiAdapter
    private val db = FirebaseFirestore.getInstance()
    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifikasi)

        recyclerView = findViewById(R.id.recyclerViewNotifikasi)
        recyclerView.layoutManager = LinearLayoutManager(this)
        notifikasiList = ArrayList()
        adapter = NotifikasiAdapter(notifikasiList)
        recyclerView.adapter = adapter

        // Ubah sesuai role: true untuk admin, false untuk user
        val isAdmin = intent.getBooleanExtra("IS_ADMIN", false)

        if (isAdmin) {
            ambilNotifikasiAdmin()
        } else {
            ambilNotifikasiUser()
        }
    }

    private fun ambilNotifikasiAdmin() {
        db.collection("notifikasi")
            .whereEqualTo("untukAdmin", true)
            .orderBy("tanggal", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                notifikasiList.clear()
                for (doc in result) {
                    val notif = doc.toObject(Notifikasi::class.java)
                    notifikasiList.add(notif)
                }
                adapter.notifyDataSetChanged()
            }
    }

    private fun ambilNotifikasiUser() {
        db.collection("notifikasi")
            .whereEqualTo("untukAdmin", false)
            .whereEqualTo("userId", currentUserId)
            .orderBy("tanggal", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                notifikasiList.clear()
                for (doc in result) {
                    val notif = doc.toObject(Notifikasi::class.java)
                    notifikasiList.add(notif)
                }
                adapter.notifyDataSetChanged()
            }
    }
}
