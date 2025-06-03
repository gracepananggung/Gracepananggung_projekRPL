package com.example.gracepananggung

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
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
    private val semuaPeminjaman = mutableListOf<Peminjaman>()
    private val filteredPeminjaman = mutableListOf<Peminjaman>()
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var textlanjut: TextView
    private lateinit var searchView: SearchView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboardadmin)

        firebaseAuth = FirebaseAuth.getInstance()

        recyclerView = findViewById(R.id.recyclerViewAdmin)
        recyclerView.layoutManager = LinearLayoutManager(this)
        textlanjut = findViewById(R.id.textlanjut)
        searchView = findViewById(R.id.searchdashadmin)

        textlanjut.setOnClickListener {
            Log.d("DashboardAdmin", "Tombol halaman berikut diklik")
            startActivity(Intent(this, HomeadminActivity::class.java))
        }

        peminjamanAdapter = PeminjamanAdapter(
            isAdmin = true,
            onDeleteClick = { peminjaman ->
                hapusPeminjaman(peminjaman)
            }
        )

        recyclerView.adapter = peminjamanAdapter

        ambilDataPeminjaman()
        setupSearch()
    }

    private fun ambilDataPeminjaman() {
        FirebaseFirestore.getInstance().collection("peminjaman")
            .get()
            .addOnSuccessListener { result ->
                semuaPeminjaman.clear()
                for (document in result) {
                    val data = document.toObject(Peminjaman::class.java)
                    data.id = document.id
                    semuaPeminjaman.add(data)
                }
                filterData("") // tampilkan semua data awal
            }
            .addOnFailureListener { exception ->
                Log.e("DashboardAdmin", "Gagal mengambil data: ${exception.message}", exception)
                Toast.makeText(this, "Gagal memuat data", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setupSearch() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterData(query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterData(newText ?: "")
                return true
            }
        })
    }

    private fun filterData(keyword: String) {
        val keywordLower = keyword.lowercase().trim()
        val result = if (keywordLower.isEmpty()) {
            semuaPeminjaman
        } else {
            semuaPeminjaman.filter {
                it.judul.lowercase().contains(keywordLower)
            }
        }
        peminjamanAdapter.setData(result)
    }

    private fun hapusPeminjaman(peminjaman: Peminjaman) {
        val id = peminjaman.id ?: return

        val dataRiwayat = hashMapOf(
            "nama" to peminjaman.nama,
            "judul" to peminjaman.judul,
            "jumlah" to peminjaman.jumlah,
            "tanggalPinjam" to peminjaman.tanggalPinjam,
            "tanggalKembali" to peminjaman.tanggalKembali
        )

        FirebaseFirestore.getInstance().collection("riwayat_peminjaman")
            .add(dataRiwayat)
            .addOnSuccessListener {
                FirebaseFirestore.getInstance().collection("peminjaman")
                    .document(id)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(this, "Dipindahkan ke riwayat", Toast.LENGTH_SHORT).show()
                        ambilDataPeminjaman()
                    }
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
