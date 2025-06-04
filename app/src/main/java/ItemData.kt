package com.example.gracepananggung

import com.google.firebase.Timestamp
import java.io.Serializable

// Data untuk daftar peminjaman buku
data class Peminjaman(
    var id: String? = null,
    var nama: String = "",
    var judul: String = "",
    var jumlah: Int = 0,
    var tanggalPinjam: Timestamp? = null,
    var tanggalKembali: Timestamp? = null,
    var uidPeminjam: String? = null
)

data class RiwayatPeminjaman(
    var id: String? = null,
    var nama: String = "",
    var judul: String = "",
    var jumlah: Int = 0,
    var tanggalPinjam: Timestamp? = null,
    var tanggalKembali: Timestamp? = null,
    var tanggalDihapus: Timestamp? = null // waktu data dipindah ke riwayat
)

data class Notifikasi(
    val pesan: String = "",
    val tanggal: String = "",
    val untukAdmin: Boolean = false,
    val userId: String = "",
    val status: String = "baru"
)

// Data untuk buku (tanpa gambar)
data class Buku(
    var id: String? = null,
    var judul: String = "",
    var deskripsi: String = ""
) : Serializable
