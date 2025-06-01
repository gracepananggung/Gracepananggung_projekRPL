import com.google.firebase.Timestamp
import java.io.Serializable

// Data untuk daftar peminjaman buku
data class Peminjaman(
    var id: String? = null,
    var nama: String = "",
    var judul: String = "",
    var jumlah: Int = 0,
    var tanggalPinjam: Timestamp? = null,
    var tanggalKembali: Timestamp? = null
)

// Data untuk buku (tanpa gambar)
data class Buku(
    var id: String? = null,
    var judul: String = "",
    var deskripsi: String = ""
) : Serializable
