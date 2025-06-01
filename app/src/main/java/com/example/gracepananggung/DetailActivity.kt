package com.example.gracepananggung

import Buku
import android.content.Context
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail)

        // Atur mode gelap dari SharedPreferences
        val prefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val isDarkMode = prefs.getBoolean("dark_mode", false)
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        // Inisialisasi view
        val txtJudul: TextView = findViewById(R.id.txtview1)
        val txtDeskripsi: TextView = findViewById(R.id.txtview2)

        // Ambil data buku dari intent
        val buku = intent.getSerializableExtra("item") as? Buku
        if (buku == null) {
            Toast.makeText(this, "Data buku tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Tampilkan data buku
        txtJudul.text = buku.judul
        txtDeskripsi.text = buku.deskripsi

        // Atur padding agar tidak tertutup sistem bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
