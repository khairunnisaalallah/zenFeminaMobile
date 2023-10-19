package com.example.myapplication1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ktx.Firebase;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        // Impor ImageButton
        ImageButton backButton = findViewById(R.id.imageButtonback);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, WelcomeActivity.class);
                startActivity(intent);
            }
        });

        // ImageButton untuk registrasi dengan nomor telepon
        ImageButton phoneSignUpButton = findViewById(R.id.imageButtonNohp);
        phoneSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect ke aktivitas yang meminta nomor telepon
                Intent intent = new Intent(RegisterActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // ImageButton untuk registrasi dengan Google
        ImageButton googleSignUpButton = findViewById(R.id.imageButtonGoogle);
        googleSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implementasi registrasi dengan Google
                // Gunakan Firebase Authentication atau Google Sign-In API
            }
        });
    }
}