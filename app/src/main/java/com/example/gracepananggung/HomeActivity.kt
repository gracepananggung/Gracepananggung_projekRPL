package com.example.gracepananggung

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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

        // Inisialisasi RecyclerView
        novelRecyclerView = findViewById(R.id.novelRV)
        listNovel = ArrayList()

        // Tambahkan data ke dalam daftar novel
        listNovel.add(ItemData(R.drawable.novel, "Ternyata Tanpamu..", "Puisi Refleksi"))
        listNovel.add(ItemData(R.drawable.novelii, "Timun Jelita", "Fiksi"))
        listNovel.add(ItemData(R.drawable.noveliii, "Laut Bercerita", "Fiksi Sejarah"))
        listNovel.add(ItemData(R.drawable.noveliv, "Seporsi mie ayam", "Mental Health"))
        listNovel.add(ItemData(R.drawable.novelv, "Sisi Tergelap Surga", "Literary Fiction"))


        novelRecyclerView.layoutManager = LinearLayoutManager(this)
        novelRecyclerView.setHasFixedSize(true)

        // Inisialisasi adapter dan hubungkan dengan RecyclerView
        novelAdapter = MyAdapter(listNovel)
        novelRecyclerView.adapter = novelAdapter
    }
}
