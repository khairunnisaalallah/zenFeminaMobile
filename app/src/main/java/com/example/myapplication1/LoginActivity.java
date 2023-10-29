package com.example.myapplication1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity { //ini berubah

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = findViewById(R.id.buttonlogin2);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Arahkan ke LoginActivity
                Intent intent = new Intent(LoginActivity.this, activity_pertanyaan1.class);
                startActivity(intent);
            }
        });
        // Inisialisasi TextView
        TextView registerTextView = findViewById(R.id.textViewregis);
        // Menambahkan OnClickListener ke TextView
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Arahkan ke RegisterActivity
                Intent intent = new Intent(LoginActivity.this, activity_register.class);
                startActivity(intent);
            }
        });
        // Impor ImageButton
        ImageButton backButton = findViewById(R.id.imageButtonbackl);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                startActivity(intent);
            }
        });
        // Inisialisasi TextView
        TextView lupapwTextView = findViewById(R.id.lupapw);
        // Menambahkan OnClickListener ke TextView
        lupapwTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Arahkan ke lupa pw
                Intent intent = new Intent(LoginActivity.this, LupapwActivity.class);
                startActivity(intent);
            }
        });
    }


}
