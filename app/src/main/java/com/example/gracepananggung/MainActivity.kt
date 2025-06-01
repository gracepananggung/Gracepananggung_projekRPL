package com.example.gracepananggung

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.gracepananggung.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private var userRole: String = "user" // Default

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        applyDarkModeSetting()

        // Ambil role dari intent
        userRole = intent.getStringExtra("role") ?: "user"

        binding.textForgetPassword.setOnClickListener {
            startActivity(Intent(this, ForgetPasswordActivity::class.java))
        }

        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextTextEmailAddress2.text.toString().trim()
            val password = binding.editTextTextPassword2.text.toString().trim()
            validateAndLogin(email, password)
        }

        binding.textRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity2::class.java)
            intent.putExtra("role", userRole)
            startActivity(intent)
        }
    }

    private fun applyDarkModeSetting() {
        val prefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val isDarkMode = prefs.getBoolean("dark_mode", false)
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    private fun validateAndLogin(email: String, password: String) {
        when {
            email.isEmpty() -> {
                binding.editTextTextEmailAddress2.error = "Email tidak boleh kosong"
                binding.editTextTextEmailAddress2.requestFocus()
            }
            password.isEmpty() -> {
                binding.editTextTextPassword2.error = "Password tidak boleh kosong"
                binding.editTextTextPassword2.requestFocus()
            }
            else -> {
                loginUser(email, password)
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    checkUserRoleAndRedirect()
                } else {
                    Toast.makeText(this, "Login gagal: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun checkUserRoleAndRedirect() {
        val uid = firebaseAuth.currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

        db.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                val role = document.getString("role") ?: "user"

                val intent = if (role == "admin") {
                    Intent(this, DashboardAdminActivity::class.java)
                } else {
                    Intent(this, DashboardActivity2::class.java)
                }

                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)

            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal mengambil role pengguna", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onStart() {
        super.onStart()
        if (firebaseAuth.currentUser != null) {
            checkUserRoleAndRedirect()
        }
    }
}
