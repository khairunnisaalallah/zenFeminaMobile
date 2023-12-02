package com.example.myapplication1; // Ganti dengan package sesuai proyek Anda

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class activity_pertanyaan1 extends AppCompatActivity {
    private ImageButton datePicker;
    private Button nextButton;
    private Calendar calendar;

    public static String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pertanyaan1);

        datePicker = findViewById(R.id.imageView);
        nextButton = findViewById(R.id.selanjutnya1);
        calendar = Calendar.getInstance();

        // Menampilkan DatePickerDialog saat ImageButton diklik
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // Handle navigasi ke layout pertanyaan selanjutnya saat tombol "Selanjutnya" diklik
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedDate = getSelectedDate();
                value = selectedDate;

                Intent intent = new Intent(activity_pertanyaan1.this, activity_pertanyaan2.class);
                intent.putExtra("value1", value);
                startActivity(intent);
            }
        });
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                R.style.CustomDatePickerDialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                        // Tangani pemilihan tanggal disini
                        calendar.set(year, month, dayOfMonth);

                        String selectedDate = getSelectedDate();
                        EditText editTextDate2 = findViewById(R.id.editTextDate2);
                        editTextDate2.setText(selectedDate);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()); // Batasi tanggal yang bisa dipilih menjadi hari ini atau sebelumnya
        datePickerDialog.show();

    }

    private String getSelectedDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }
}
