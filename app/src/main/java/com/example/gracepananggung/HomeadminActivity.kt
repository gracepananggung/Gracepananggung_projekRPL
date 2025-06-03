package com.example.gracepananggung

import com.example.gracepananggung.Buku
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class HomeadminActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var bukuAdapter: MyAdapter
    private lateinit var searchView: SearchView
    private lateinit var btnTambahBuku: Button
    private lateinit var textRiwayat: TextView

    private val semuaBuku = mutableListOf<Buku>()
    private val bukuFiltered = mutableListOf<Buku>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homeadmin)

        val prefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val isDarkMode = prefs.getBoolean("dark_mode", false)
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )

        recyclerView = findViewById(R.id.recyclerViewAdmin)
        searchView = findViewById(R.id.searchViewadmin)
        btnTambahBuku = findViewById(R.id.btnTambahBuku)
        textRiwayat = findViewById(R.id.textRiwayat)

        textRiwayat.setOnClickListener {
            Log.d("HomedAdmin", "Tombol lihat riwayat di klik")
            startActivity(Intent(this, RiwayatActivity::class.java))
        }

        recyclerView.layoutManager = LinearLayoutManager(this)

        // ✅ Inisialisasi adapter dengan fungsi hapus
        bukuAdapter = MyAdapter(
            bukuFiltered,
            isAdmin = true,
            onEditClick = { buku ->
                showEditDialog(buku)
            },
            onDeleteClick = { buku ->
                AlertDialog.Builder(this)
                    .setTitle("Hapus Buku")
                    .setMessage("Yakin ingin menghapus buku \"${buku.judul}\"?")
                    .setPositiveButton("Hapus") { _, _ ->
                        val id = buku.id
                        if (id.isNullOrEmpty()) {
                            Toast.makeText(this, "ID buku tidak valid", Toast.LENGTH_SHORT).show()
                            return@setPositiveButton
                        }

                        FirebaseFirestore.getInstance().collection("buku")
                            .document(id)
                            .delete()
                            .addOnSuccessListener {
                                Toast.makeText(this, "Buku berhasil dihapus", Toast.LENGTH_SHORT).show()
                                ambilDataDariFirestore()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Gagal menghapus buku", Toast.LENGTH_SHORT).show()
                            }
                    }
                    .setNegativeButton("Batal", null)
                    .show()
            },
            onItemClick = {} // Tidak digunakan
        )
        recyclerView.adapter = bukuAdapter

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ambilDataDariFirestore()
        setupSearchView()

        btnTambahBuku.setOnClickListener {
            showTambahBukuDialog()
        }

        intent.getStringExtra("search_query")?.trim()?.let {
            searchView.setQuery(it, false)
            filterData(it)
        }
    }

    private fun ambilDataDariFirestore() {
        FirebaseFirestore.getInstance().collection("buku")
            .get()
            .addOnSuccessListener { result ->
                semuaBuku.clear()
                for (document in result) {
                    val buku = document.toObject(Buku::class.java)
                    buku.id = document.id
                    semuaBuku.add(buku)
                }
                bukuFiltered.clear()
                bukuFiltered.addAll(semuaBuku)
                bukuAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Log.e("HomeAdminActivity", "Gagal ambil data dari Firestore", e)
            }
    }

    private fun setupSearchView() {
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
        val lowerKeyword = keyword.lowercase().trim()
        bukuFiltered.clear()
        if (lowerKeyword.isEmpty()) {
            bukuFiltered.addAll(semuaBuku)
        } else {
            bukuFiltered.addAll(semuaBuku.filter {
                it.judul.lowercase().contains(lowerKeyword)
            })
        }
        bukuAdapter.notifyDataSetChanged()
    }
    
    //tombol edit
    private fun showEditDialog(buku: Buku) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_buku, null)
        val etEditJudul = dialogView.findViewById<EditText>(R.id.etEditJudul)
        val etEditDeskripsi = dialogView.findViewById<EditText>(R.id.etEditDeskripsi)

        etEditJudul.setText(buku.judul)
        etEditDeskripsi.setText(buku.deskripsi)

        AlertDialog.Builder(this)
            .setTitle("Edit Buku")
            .setView(dialogView)
            .setPositiveButton("Simpan") { dialog, _ ->
                val judulBaru = etEditJudul.text.toString().trim()
                val deskripsiBaru = etEditDeskripsi.text.toString().trim()

                if (judulBaru.isNotEmpty() && deskripsiBaru.isNotEmpty()) {
                    val db = FirebaseFirestore.getInstance()
                    db.collection("buku").document(buku.id ?: "")
                        .update("judul", judulBaru, "deskripsi", deskripsiBaru)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Buku diperbarui", Toast.LENGTH_SHORT).show()
                            ambilDataDariFirestore() // ✅ Perbaikan di sini
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Gagal update", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "Form tidak boleh kosong", Toast.LENGTH_SHORT).show()
                }

                dialog.dismiss()
            }
            .setNegativeButton("Batal") { dialog, _ -> dialog.dismiss() }
            .show()
    }


    //tombol tambah buku
    private fun showTambahBukuDialog() {
        val dialogView = layoutInflater.inflate(R.layout.tambah_buku, null)
        val inputJudul = dialogView.findViewById<EditText>(R.id.etJudul)
        val inputDeskripsi = dialogView.findViewById<EditText>(R.id.etdeskripsi)

        AlertDialog.Builder(this)
            .setTitle("Tambah Buku")
            .setView(dialogView)
            .setPositiveButton("Simpan") { _, _ ->
                val judul = inputJudul.text.toString().trim()
                val deskripsi = inputDeskripsi.text.toString().trim()

                if (judul.isNotEmpty() && deskripsi.isNotEmpty()) {
                    val buku = Buku(judul = judul, deskripsi = deskripsi)
                    FirebaseFirestore.getInstance().collection("buku")
                        .add(buku)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Buku ditambahkan", Toast.LENGTH_SHORT).show()
                            ambilDataDariFirestore()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Gagal menambahkan buku", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "Field tidak boleh kosong", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}
