package com.example.gracepananggung

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.gracepananggung.databinding.ActivityRegister2Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityRegister2Binding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private var userRole: String = "user" // Default role

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ambil role dari Intent (default: user)
        userRole = intent.getStringExtra("role") ?: "user"

        // Setup ViewBinding dan Firebase
        binding = ActivityRegister2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Terapkan Dark Mode dari preferensi
        applyDarkMode()

        // Navigasi ke Login
        binding.textLogin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("role", userRole)
            startActivity(intent)
            finish()
        }

        // Tombol Daftar
        binding.buttonRegister.setOnClickListener {
            val name = binding.editTextPersonName.text.toString().trim()
            val email = binding.editTextTextEmailAddress.text.toString().trim()
            val password = binding.editTextTextPassword.text.toString().trim()
            validateAndRegister(name, email, password)
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

    private fun validateAndRegister(name: String, email: String, password: String) {
        when {
            name.isEmpty() -> {
                binding.editTextPersonName.error = "Nama tidak boleh kosong"
                binding.editTextPersonName.requestFocus()
            }
            email.isEmpty() -> {
                binding.editTextTextEmailAddress.error = "Email tidak boleh kosong"
                binding.editTextTextEmailAddress.requestFocus()
            }
            password.isEmpty() -> {
                binding.editTextTextPassword.error = "Password tidak boleh kosong"
                binding.editTextTextPassword.requestFocus()
            }
            password.length < 6 -> {
                binding.editTextTextPassword.error = "Password minimal 6 karakter"
                binding.editTextTextPassword.requestFocus()
            }
            else -> {
                registerUser(name, email, password)
            }
        }
    }

    private fun registerUser(name: String, email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = firebaseAuth.currentUser?.uid ?: return@addOnCompleteListener
                    val userMap = hashMapOf(
                        "name" to name,
                        "email" to email,
                        "role" to userRole
                    )

                    // Simpan ke Firestore
                    firestore.collection("users").document(userId).set(userMap)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Registrasi Berhasil!", Toast.LENGTH_SHORT).show()

                            // Arahkan ke dashboard sesuai role
                            val intent = if (userRole == "admin") {
                                Intent(this, DashboardAdminActivity::class.java)
                            } else {
                                Intent(this, DashboardActivity2::class.java)
                            }

                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            intent.putExtra("USERNAME", name)
                            intent.putExtra("role", userRole)
                            startActivity(intent)
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Gagal menyimpan data ke Firestore", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "Registrasi Gagal: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            val uid = currentUser.uid
            firestore.collection("users").document(uid).get()
                .addOnSuccessListener { document ->
                    val role = document.getString("role") ?: "user"
                    val intent = if (role == "admin") {
                        Intent(this, DashboardAdminActivity::class.java)
                    } else {
                        Intent(this, DashboardActivity2::class.java)
                    }
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    intent.putExtra("role", role)
                    startActivity(intent)
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Gagal mengambil role pengguna", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
