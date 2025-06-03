package com.example.gracepananggung

import com.example.gracepananggung.Buku
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var bukuAdapter: MyAdapter
    private lateinit var searchView: SearchView

    private val semuaBuku = mutableListOf<Buku>()
    private val bukuFiltered = mutableListOf<Buku>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Atur dark mode berdasarkan SharedPreferences
        val prefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val isDarkMode = prefs.getBoolean("dark_mode", false)
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        // Inisialisasi tampilan
        recyclerView = findViewById(R.id.recyclerViewUser)
        searchView = findViewById(R.id.searchView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        bukuAdapter = MyAdapter(
            bukuFiltered,
            isAdmin = false,
            onEditClick = {},
            onDeleteClick = {},
            onItemClick = { buku ->
                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra("item", buku)
                startActivity(intent)
            }
        )
        recyclerView.adapter = bukuAdapter

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ambilDataDariFirestore()
        setupSearchView()

        // Cek apakah ada search_query dari intent
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
                Log.e("HomeActivity", "Gagal ambil data dari Firestore", e)
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
            bukuFiltered.addAll(
                semuaBuku.filter { it.judul.lowercase().contains(lowerKeyword) }
            )
        }

        bukuAdapter.notifyDataSetChanged()
    }
}
