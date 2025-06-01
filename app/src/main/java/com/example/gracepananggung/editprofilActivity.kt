package com.example.gracepananggung

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class EditProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editprofil)

        //inisialisasi komponen
        val editName = findViewById<EditText>(R.id.editName)
        val editBirthday = findViewById<EditText>(R.id.editBirthday)
        val editEmail = findViewById<EditText>(R.id.editEmail)
        val editPhone = findViewById<EditText>(R.id.editPhone)
        val btnSave = findViewById<Button>(R.id.btnSaveProfile)

        // Ambil data sebelumnya dari Intent
        val intent = intent
        editName.setText(intent.getStringExtra("name"))
        editBirthday.setText(intent.getStringExtra("birthday"))
        editEmail.setText(intent.getStringExtra("email"))
        editPhone.setText(intent.getStringExtra("phone"))

        //tombol simpan pembaharuan
        btnSave.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("name", editName.text.toString())
            resultIntent.putExtra("birthday", editBirthday.text.toString())
            resultIntent.putExtra("email", editEmail.text.toString())
            resultIntent.putExtra("phone", editPhone.text.toString())
            setResult(RESULT_OK, resultIntent)//Saat klik "Simpan", data dikirim balik ke ProfileActivity lewat setResult.
            finish()
        }
    }
}
