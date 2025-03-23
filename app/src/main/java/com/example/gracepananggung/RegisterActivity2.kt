package com.example.gracepananggung

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gracepananggung.databinding.ActivityRegister2Binding

class RegisterActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityRegister2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegister2Binding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.textLogin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.buttonRegister.setOnClickListener {
            val name = binding.editTextPersonName.text.toString().trim()
            val email = binding.editTextTextEmailAddress.text.toString().trim()
            val password = binding.editTextTextPassword.text.toString().trim()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                // Menampilkan pesan registrasi berhasil
                Toast.makeText(this, "Registrasi Berhasil!", Toast.LENGTH_SHORT).show()

                // Pindah ke halaman Dashboard
                val intent = Intent(this, DashboardActivity2::class.java)
                intent.putExtra("USERNAME", name) // Kirim data nama ke Dashboard
                startActivity(intent)
                finish() // Menutup RegisterActivity2 agar tidak bisa kembali
            } else {
                Toast.makeText(this, "Harap isi semua!", Toast.LENGTH_SHORT).show()
            }

        }

    }
}

