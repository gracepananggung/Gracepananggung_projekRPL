package com.example.gracepananggung

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.gracepananggung.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Klik tombol "Login"
        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextTextEmailAddress2.text.toString().trim()
            val password = binding.editTextTextPassword2.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                Toast.makeText(this, "Login Berhasil!", Toast.LENGTH_SHORT).show()

                // Pindah ke halaman Dashboard
                val intent = Intent(this, DashboardActivity2::class.java)
                intent.putExtra("EMAIL", email) // Mengirim data email ke Dashboard
                startActivity(intent)
                finish() // Menutup MainActivity agar tidak bisa kembali
            } else {
                Toast.makeText(this, "Harap isi Semua!", Toast.LENGTH_SHORT).show()
            }
        }


        binding.textRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity2::class.java)
            startActivity(intent)

        }
    }
}
