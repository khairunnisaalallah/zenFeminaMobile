package com.example.myapplication1;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class notifActivity extends AppCompatActivity {
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif);
        textView = findViewById(R.id.textviewData);
        String data = getIntent().getStringExtra("data");
        textView.setText(data);
    }
}
