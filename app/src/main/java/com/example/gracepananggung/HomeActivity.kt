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

        listNovel.add(ItemData(R.drawable.novel, "Ternyata Tanpamu", "Puisi Refleksi", "Tahun 2024"))
        listNovel.add(ItemData(R.drawable.novelii, "Timun Jelita", "Fiksi", ""))
        listNovel.add(ItemData(R.drawable.noveliii, "Laut Bercerita", "Fiksi Sejarah", ""))
        listNovel.add(ItemData(R.drawable.noveliv, "Seporsi Mie Ayam", "Mental Health", ""))
        listNovel.add(ItemData(R.drawable.novelv, "Sisi Tergelap Surga", "Literary Fiction", ""))


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

