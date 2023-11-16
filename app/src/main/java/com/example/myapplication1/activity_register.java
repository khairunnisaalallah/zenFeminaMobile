package com.example.myapplication1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class activity_register extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Impor ImageButton
        ImageButton backButton = findViewById(R.id.imageButtonbackr);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_register.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // ImageButton untuk registrasi dengan nomor telepon
        ImageButton phoneSignUpButton = findViewById(R.id.telepon);
        phoneSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect ke aktivitas yang meminta nomor telepon
                Intent intent = new Intent(activity_register.this, VerifActivity.class);
                startActivity(intent);
            }
        });

        // ImageButton untuk registrasi dengan Google
        ImageButton googleSignUpButton = findViewById(R.id.google);
        googleSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_register.this, VerifActivity.class);
                startActivity(intent);
                // Implementasi registrasi dengan Google
                // Gunakan Firebase Authentication atau Google Sign-In API
            }
        });

        // ImageButton untuk registrasi dengan Google
        Button DaftarButton = findViewById(R.id.daftar);
        DaftarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_register.this, VerifActivity.class);
                startActivity(intent);
            }
        });
    }
}
