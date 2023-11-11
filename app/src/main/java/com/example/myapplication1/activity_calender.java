package com.example.myapplication1;

import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class activity_calender extends AppCompatActivity {

    private TextView monthTitle;
    private CalendarView calendarView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender); // Gantilah "your_layout" dengan nama layout Anda

        // Inisialisasi komponen UI
        monthTitle = findViewById(R.id.monthTitle);
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
    }
}
