package com.example.myapplication1;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class activity_calender extends AppCompatActivity {

    private TextView monthTitle;
    private CalendarView calendarView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender); // Gantilah "your_layout" dengan nama layout Anda

        // Inisialisasi komponen UI
        monthTitle = findViewById(R.id.judul);
        calendarView = findViewById(R.id.calendarView);

        // Atur listener untuk perubahan tanggal di CalendarView
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                // Tambahkan logika yang sesuai dengan perubahan tanggal yang dipilih
                // Misalnya, Anda dapat memperbarui teks di TextView monthTitle
                monthTitle.setText("Selected Date: " + dayOfMonth + "/" + (month + 1) + "/" + year);
            }
        });

        ImageButton backButton = findViewById(R.id.imageButtonbackkal);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tambahkan logika sesuai dengan kebutuhan Anda
                // Contoh: Kembali ke HomeFragment jika ada back stack
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                } else {
                    // Jika tidak ada fragment di dalam back stack, panggil onBackPressed biasa
                    onBackPressed();
                }
            }
        });

    }
            }






