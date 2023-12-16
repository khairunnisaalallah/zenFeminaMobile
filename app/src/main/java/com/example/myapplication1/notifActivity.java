package com.example.myapplication1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class notifActivity extends AppCompatActivity {
//    TextView textView;
    RelativeLayout btnAwal, btnAkhir;
    ImageView btnback;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif);
//        textView = findViewById(R.id.textviewData);
//        String data = getIntent().getStringExtra("data");
//        textView.setText(data);

        btnAwal = findViewById(R.id.btnawalsiklus);
        btnAwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(notifActivity.this, edit_notif_awal.class);
                startActivity(intent);
            }
        });

        btnAkhir = findViewById(R.id.btnakhirsiklus);
        btnAkhir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(notifActivity.this, edit_notif_akhir.class);
                startActivity(intent);
            }
        });

        btnback = findViewById(R.id.back);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(notifActivity.this, FragmentActivity.class);
                startActivity(intent);
            }
        });
    }
    //hati2 buat notif muncul

}
