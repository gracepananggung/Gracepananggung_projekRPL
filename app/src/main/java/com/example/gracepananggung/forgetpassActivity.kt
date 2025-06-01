package com.example.gracepananggung

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.gracepananggung.databinding.ActivityForgetpassBinding
import com.google.firebase.auth.FirebaseAuth

class ForgetPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgetpassBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Terapkan dark mode dari SharedPreferences
        applyDarkMode()

        // View Binding dan Firebase Auth
        binding = ActivityForgetpassBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        // Tombol kirim email reset password
        binding.btnSendReset.setOnClickListener {
            val email = binding.emailInput.text.toString().trim()
            handlePasswordReset(email)
        }
    }

    private fun applyDarkMode() {
        val prefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val isDarkMode = prefs.getBoolean("dark_mode", false)
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    private fun handlePasswordReset(email: String) {
        when {
            email.isEmpty() -> {
                binding.emailInput.error = "Email tidak boleh kosong"
                binding.emailInput.requestFocus()
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.emailInput.error = "Format email tidak valid"
                binding.emailInput.requestFocus()
            }
            else -> {
                firebaseAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this,
                                "Email reset password telah dikirim. Silakan periksa inbox atau folder spam.",
                                Toast.LENGTH_LONG
                            ).show()
                            // Arahkan kembali ke halaman login
                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        } else {
                            Toast.makeText(
                                this,
                                "Gagal mengirim email: ${task.exception?.localizedMessage}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            }
        }
    }
}
