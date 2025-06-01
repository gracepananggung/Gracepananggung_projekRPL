package com.example.gracepananggung

import Peminjaman
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DashboardAdminActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var peminjamanAdapter: PeminjamanAdapter
    private val peminjamanList = mutableListOf<Peminjaman>()
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var textlanjut: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboardadmin)

        firebaseAuth = FirebaseAuth.getInstance()

        recyclerView = findViewById(R.id.recyclerViewAdmin)
        recyclerView.layoutManager = LinearLayoutManager(this)
        textlanjut = findViewById(R.id.textlanjut)

        textlanjut.setOnClickListener {
            Log.d("DashboardAdmin", "Tombol halaman berikut diklik")
            startActivity(Intent(this, HomeadminActivity::class.java))
        }

        peminjamanAdapter = PeminjamanAdapter(
            isAdmin = true,
            onDeleteClick = { peminjaman ->
                peminjaman.id?.let { id -> hapusDataPeminjaman(id) }
            }
        )
        recyclerView.adapter = peminjamanAdapter

        ambilDataPeminjaman()
    }

    private fun ambilDataPeminjaman() {
        FirebaseFirestore.getInstance().collection("peminjaman")
            .get()
            .addOnSuccessListener { result ->
                val list = mutableListOf<Peminjaman>()
                for (document in result) {
                    val data = document.toObject(Peminjaman::class.java)
                    data.id = document.id
                    list.add(data)
                }
                peminjamanAdapter.setData(list)
            }
            .addOnFailureListener { exception ->
                Log.e("DashboardAdmin", "Gagal mengambil data: ${exception.message}", exception)
                Toast.makeText(this, "Gagal memuat data", Toast.LENGTH_SHORT).show()
            }
    }

    private fun hapusDataPeminjaman(id: String) {
        FirebaseFirestore.getInstance().collection("peminjaman")
            .document(id)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show()
                ambilDataPeminjaman()
            }
            .addOnFailureListener { exception ->
                Log.e("DashboardAdmin", "Gagal menghapus data: ${exception.message}", exception)
                Toast.makeText(this, "Gagal menghapus data", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_admin, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.setting -> {
                startActivity(Intent(this, SettingActivity::class.java))
                true
            }
            R.id.logout -> {
                firebaseAuth.signOut()
                val intent = Intent(this, MasuksebagaiActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
