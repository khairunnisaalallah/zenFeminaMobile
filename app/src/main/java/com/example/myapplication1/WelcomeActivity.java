package com.example.myapplication1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    public void onButtonClick(View view) {
        // Memulai aktivitas MainActivity saat tombol ditekan
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
}
