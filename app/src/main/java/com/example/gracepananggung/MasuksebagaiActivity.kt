package com.example.gracepananggung

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gracepananggung.databinding.ActivityMasuksebagaiBinding

class MasuksebagaiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMasuksebagaiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMasuksebagaiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Navigasi ke login sebagai admin
        binding.btnAdmin.setOnClickListener {
            navigateToLogin("admin")
        }

        // Navigasi ke login sebagai user
        binding.btnUser.setOnClickListener {
            navigateToLogin("user")
        }
    }

    private fun navigateToLogin(role: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("role", role)
        }
        startActivity(intent)
    }
}
