package com.example.gracepananggung

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {

    // Deklarasi variabel
    private lateinit var profileTitle: TextView
    private lateinit var birthday: TextView
    private lateinit var email: TextView
    private lateinit var noHp: TextView
    private lateinit var editTextView: TextView
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Dark mode
        val prefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val isDarkMode = prefs.getBoolean("dark_mode", false)
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        // mengakses komponen layout
        profileTitle = findViewById(R.id.profileTitle)
        birthday = findViewById(R.id.birthday)
        email = findViewById(R.id.email)
        noHp = findViewById(R.id.no_hp)
        editTextView = findViewById(R.id.edit)

        // Inisialisasi FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        // Ambil dan tampilkan email user yang sedang login
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            email.text = currentUser.email
        } else {
            email.text = "Email tidak ditemukan"
        }

        // Aksi saat "Edit Profile" diklik
        editTextView.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            intent.putExtra("name", profileTitle.text.toString())
            intent.putExtra("birthday", birthday.text.toString())
            intent.putExtra("email", email.text.toString())
            intent.putExtra("phone", noHp.text.toString())
            startActivityForResult(intent, 100)
        }
    }

    // Menangani Data Balik dari Edit
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
            profileTitle.text = data.getStringExtra("name")
            birthday.text = data.getStringExtra("birthday")
            // Email tidak perlu diubah manual karena sudah dari FirebaseAuth
            noHp.text = data.getStringExtra("phone")
        }
    }
}
