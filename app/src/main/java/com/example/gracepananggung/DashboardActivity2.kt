package com.example.gracepananggung

import com.example.gracepananggung.Peminjaman
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class DashboardActivity2 : AppCompatActivity() {

    private lateinit var buttonPinjam: Button
    private lateinit var textSelamatDatang: TextView
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PeminjamanAdapter
    private val daftarPeminjaman = ArrayList<Peminjaman>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard2)

        supportActionBar?.title = ""
        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val prefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val isDarkMode = prefs.getBoolean("dark_mode", false)
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        buttonPinjam = findViewById(R.id.buttonpinjam)
        textSelamatDatang = findViewById(R.id.textSelamatDatang)
        recyclerView = findViewById(R.id.recyclerViewPeminjaman)

        adapter = PeminjamanAdapter()
        adapter.setData(daftarPeminjaman)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        textSelamatDatang.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        buttonPinjam.setOnClickListener {
            tampilkanDialogPinjamBuku()
        }

        ambilDataFirestore()
    }

    private fun tampilkanDialogPinjamBuku() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.pinjambuku, null)

        val etNama = dialogView.findViewById<EditText>(R.id.etNama)
        val etJudul = dialogView.findViewById<EditText>(R.id.etJudul)
        val etJmlj = dialogView.findViewById<EditText>(R.id.etJmlj)
        val etTanggalPinjam = dialogView.findViewById<EditText>(R.id.etTanggalPinjam)
        val etTanggalKembali = dialogView.findViewById<EditText>(R.id.etTanggalKembali)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .setPositiveButton("Simpan", null)
            .setNegativeButton("Batal") { d, _ -> d.dismiss() }
            .create()

        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val nama = etNama.text.toString().trim()
            val judul = etJudul.text.toString().trim()
            val jumlah = etJmlj.text.toString().trim().toIntOrNull() ?: 0
            val tglPinjamStr = etTanggalPinjam.text.toString().trim()
            val tglKembaliStr = etTanggalKembali.text.toString().trim()

            if (nama.isEmpty() || judul.isEmpty() || jumlah == 0 || tglPinjamStr.isEmpty() || tglKembaliStr.isEmpty()) {
                Toast.makeText(this, "Mohon lengkapi semua data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val tglPinjam = Timestamp(sdf.parse(tglPinjamStr)!!)
                val tglKembali = Timestamp(sdf.parse(tglKembaliStr)!!)

                val dataPeminjaman = Peminjaman(
                    id = null, // Firestore akan mengisi ID saat data ditambahkan
                    nama = nama,
                    judul = judul,
                    jumlah = jumlah,
                    tanggalPinjam = tglPinjam,
                    tanggalKembali = tglKembali
                )

                db.collection("peminjaman")
                    .add(dataPeminjaman)
                    .addOnSuccessListener { docRef ->
                        dataPeminjaman.id = docRef.id
                        daftarPeminjaman.add(dataPeminjaman)
                        adapter.setData(daftarPeminjaman) // ✅ Ganti dengan setData agar RecyclerView diperbarui
                        Toast.makeText(this, "Peminjaman berhasil disimpan", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }

                    .addOnFailureListener {
                        Toast.makeText(this, "Gagal menyimpan ke Firestore", Toast.LENGTH_SHORT).show()
                    }

            } catch (e: Exception) {
                Toast.makeText(this, "Format tanggal salah. Gunakan dd/MM/yyyy", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun ambilDataFirestore() {
        db.collection("peminjaman")
            .get()
            .addOnSuccessListener { result ->
                daftarPeminjaman.clear()
                for (doc in result) {
                    val peminjaman = doc.toObject(Peminjaman::class.java)
                    peminjaman.id = doc.id
                    daftarPeminjaman.add(peminjaman)
                }
                adapter.setData(daftarPeminjaman)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onStart() {
        super.onStart()
        val user = firebaseAuth.currentUser
        if (user == null) {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.profile -> {
                startActivity(Intent(this, ProfileActivity::class.java))
                true
            }
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
