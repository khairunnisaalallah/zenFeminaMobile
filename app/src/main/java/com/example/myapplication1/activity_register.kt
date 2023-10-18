package com.example.myapplication1

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class activity_register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Impor ImageButton
        val backButton = findViewById<ImageButton>(R.id.imageButtonback)
        backButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // ImageButton untuk registrasi dengan nomor telepon
        val phoneSignUpButton = findViewById<ImageButton>(R.id.imageButtonNohp)
        phoneSignUpButton.setOnClickListener {
            // Redirect ke aktivitas yang meminta nomor telepon
            val intent = Intent(this, activity_register::class.java)
            startActivity(intent)
        }

        // ImageButton untuk registrasi dengan Google
        val googleSignUpButton = findViewById<ImageButton>(R.id.imageButtonGoogle)
        googleSignUpButton.setOnClickListener {
            // Implementasi registrasi dengan Google
            // Gunakan Firebase Authentication atau Google Sign-In API
        }
    }
}
