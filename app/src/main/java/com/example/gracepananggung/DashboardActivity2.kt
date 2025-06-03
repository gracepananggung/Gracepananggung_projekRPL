package com.example.gracepananggung

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
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

        adapter = PeminjamanAdapter { peminjaman -> pindahkanKeRiwayat(peminjaman) }
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)
        }
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
                    id = null,
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
                        adapter.setData(daftarPeminjaman)
                        Toast.makeText(this, "Peminjaman berhasil disimpan", Toast.LENGTH_SHORT).show()
                        tampilkanNotifikasi(nama, judul)
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

    private fun pindahkanKeRiwayat(peminjaman: Peminjaman) {
        val riwayatData = hashMapOf(
            "nama" to peminjaman.nama,
            "judul" to peminjaman.judul,
            "jumlah" to peminjaman.jumlah,
            "tanggalPinjam" to peminjaman.tanggalPinjam,
            "tanggalKembali" to peminjaman.tanggalKembali
        )

        db.collection("riwayat_peminjaman")
            .add(riwayatData)
            .addOnSuccessListener {
                db.collection("peminjaman").document(peminjaman.id!!)
                    .delete()
                    .addOnSuccessListener {
                        daftarPeminjaman.remove(peminjaman)
                        adapter.setData(daftarPeminjaman)
                        Toast.makeText(this, "Buku dikembalikan dan dipindahkan ke riwayat", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Gagal menghapus data lama", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal menyimpan ke riwayat", Toast.LENGTH_SHORT).show()
            }
    }

    private fun tampilkanNotifikasi(nama: String, judul: String) {
        val channelId = "peminjaman_channel"
        val channelName = "Notifikasi Peminjaman"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Peminjaman Buku Berhasil")
            .setContentText("$nama meminjam buku: $judul")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this@DashboardActivity2)) {
            if (ActivityCompat.checkSelfPermission(
                    this@DashboardActivity2,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(System.currentTimeMillis().toInt(), builder.build())
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
