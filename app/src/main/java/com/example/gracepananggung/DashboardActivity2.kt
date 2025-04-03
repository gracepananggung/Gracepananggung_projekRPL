package com.example.gracepananggung

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DashboardActivity2 : AppCompatActivity() {

    private lateinit var buttonCamera: Button
    private lateinit var buttonLogout: Button
    private lateinit var imageView: ImageView
    private lateinit var textSelamatDatang: TextView

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard2)

        // Menemukan komponen yang diperlukan
        buttonCamera = findViewById(R.id.buttonCamera)
        buttonLogout = findViewById(R.id.buttonLogout)
        imageView = findViewById(R.id.imageView3) // Ganti ID sesuai yang ada di XML
        textSelamatDatang = findViewById(R.id.textSelamatDatang)

        // Menambahkan listener untuk tombol kamera
        buttonCamera.setOnClickListener {
            openCamera()
        }

        // Menambahkan listener untuk tombol logout
        buttonLogout.setOnClickListener {
            val intent = Intent(this@DashboardActivity2, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // Menambahkan listener untuk TextView "Selamat Datang"
        textSelamatDatang.setOnClickListener {
            // Intent untuk membuka HomeActivity
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    // Fungsi untuk membuka kamera
    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    // Fungsi untuk menangani hasil gambar dari kamera
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap) // Menampilkan gambar ke ImageView
        }
    }
}
