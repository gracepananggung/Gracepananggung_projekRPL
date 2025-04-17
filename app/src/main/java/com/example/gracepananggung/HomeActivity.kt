package com.example.gracepananggung

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeActivity : AppCompatActivity() {

    private lateinit var novelRecyclerView: RecyclerView
    private lateinit var novelAdapter: MyAdapter
    private lateinit var listNovel: ArrayList<ItemData>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        novelRecyclerView = findViewById(R.id.novelRV)
        listNovel = ArrayList()

        listNovel.add(ItemData(R.drawable.novel, "Bayangan di Negeri Aerath", "Heroic Fantasy",
            "Di dunia Aerath, sihir bukan sekadar dongeng ia adalah denyut kehidupan. Namun, keseimbangan magis terganggu saat Bayangan," +
                    " makhluk kegelapan purba, mulai bangkit dari tidur panjangnya." +
                    " Desa-desa menghilang tanpa jejak, dan langit tak lagi biru di wilayah-wilayah terkutuk.\n" +
                    "Aeryn, seorang gadis muda dari desa pegunungan yang damai, tanpa sengaja membangkitkan artefak kuno: Jiwa Cahaya." +
                    " Artefak itu memilihnya sebagai Penjaga Baru, memberi Aeryn kekuatan besar namun juga tanggung jawab yang mengerikan." +
                    " Bersama Thalen, seorang penyihir muda yang angkuh, dan Kael, prajurit bayaran yang menyembunyikan masa lalu kelam," +
                    " Aeryn memulai perjalanan untuk membangkitkan Penjaga Lama dan menemukan asal-usul Bayangan.\n" + "Namun, di balik kegelapan yang menyerang Aerath, tersembunyi rahasia yang lebih dalam, bahwa kebaikan dan kejahatan tak selalu bisa dibedakan... dan bahwa tak semua pahlawan ingin menyelamatkan dunia.\n"))
        listNovel.add(ItemData(R.drawable.novelii, "Langit Ketiga di Atas Elvyrra", "High Fantasy",
            "Di benua terapung Elvyrra, langit dibagi menjadi tiga lapisan, langit pertama untuk manusia, langit kedua untuk para penjaga langit, dan langit ketiga langit yang dilarang. Ketika seorang pemuda pembaca bintang bernama Lior tanpa sengaja membuka gerbang menuju langit ketiga, ia menemukan rahasia besar para dewa telah dikurung di sana.\n" +
                    "Kini, ia harus memilih, menjaga dunia tetap damai, atau membebaskan kekuatan yang bisa mengubah takdir semua makhluk hidup.\n"))
        listNovel.add(ItemData(R.drawable.noveliii, "Perjalanan Sang Putri", "Heroic Fantasy",
            "Putri Elira dari Kerajaan Velmora selalu hidup di balik tembok istana dilatih untuk menjadi ratu, bukan petarung. Namun ketika ayahnya jatuh sakit akibat kutukan misterius dan kerajaannya di ambang perang, Elira mengambil keputusan berani ia meninggalkan istana untuk mencari \"Obor Ilahi\", artefak kuno yang diyakini dapat menyembuhkan segala penyakit dan memulihkan kedamaian.\n" +
                    "Tanpa pengalaman dunia luar, Elira menjelajahi hutan terlarang, pegunungan es, hingga reruntuhan kota yang tenggelam. Dalam perjalanannya, ia bertemu dengan para sekutu tak terduga Kael, pemburu bayaran yang kehilangan keluarganya; Mira, penyihir muda dengan rahasia kelam; dan Seekor griffin tua yang dulu pernah melayani raja-raja kuno.\n" +
                    "Namun di balik pencarian itu tersembunyi kebenaran mengejutkan: bahwa kutukan ayahnya berasal dari darah kerajaan sendiri, dan Obor Ilahi bukan sekadar alat penyembuh melainkan kunci untuk membangkitkan kekuatan purba yang sudah lama tersegel.\n" +
                    "Dalam perjalanan ini, Elira tidak hanya belajar menjadi pemimpin, tetapi juga menantang takdirnya sendiri.\n"))
        listNovel.add(ItemData(R.drawable.noveliv, "Nyanyian Akar Dunia", "Epic Fantasy",
            "Setiap seratus tahun, pohon raksasa Sennara menyanyikan lagu yang menentukan nasib dunia. Aria, seorang gadis tuli, justru menjadi satu-satunya yang bisa “mendengar” lagu itu melalui getaran tanah.\n" + "Namun nyanyian kali ini adalah jeritan minta tolong. Bersama seekor naga yang sudah melupakan cara terbang dan seorang penyair buta, Aria harus menyusuri akar dunia yang menembus masa lalu, untuk menyelamatkan pohon terakhir dari kehancuran.\n"))
        listNovel.add(ItemData(R.drawable.novelv, "Sang Penjahit Takdir", "Urban Fantasy",
            "Di kota Rahvar, takdir manusia dijahit dalam gulungan benang kehidupan oleh seorang penjahit tua. Tapi ketika penjahit itu menghilang, benang-benang mulai saling kusut dan takdir orang-orang berubah liar.\n" + "Kaia, muridnya yang dulu lari dari tanggung jawab, dipaksa kembali. Dengan jarum perak dan benang emas, ia harus menjahit ulang takdir dunia—sebelum semuanya terurai jadi kekacauan.\n"))


        novelRecyclerView.layoutManager = LinearLayoutManager(this)
        novelRecyclerView.setHasFixedSize(true)
        novelAdapter = MyAdapter(listNovel)
        novelRecyclerView.adapter = novelAdapter

        novelAdapter.onItemClick = { selectedItem ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("item", selectedItem)
            startActivity(intent)
        }
    }
}

