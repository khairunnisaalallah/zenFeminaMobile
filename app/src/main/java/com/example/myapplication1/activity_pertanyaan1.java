package com.example.myapplication1;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class activity_pertanyaan1 extends AppCompatActivity {
    private ImageButton datePicker;
    private Button nextButton;
    private EditText editTextDate;
    private Calendar calendar;

    public static String value1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pertanyaan1);

        datePicker = findViewById(R.id.imageButton1);
        nextButton = findViewById(R.id.selanjutnya1);
        editTextDate = findViewById(R.id.editTextDate1);
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
                value1 = selectedDate;

                // Mengambil nilai token dari Intent
                String token = getIntent().getStringExtra("token");

                // Membuat Intent baru dan menyertakan nilai token
                Intent intent = new Intent(activity_pertanyaan1.this, activity_pertanyaan2.class);
                intent.putExtra("token", token);
                intent.putExtra("value1", value1);
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
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Tangani pemilihan tanggal disini
                        calendar.set(year, month, dayOfMonth);

                        String selectedDate = getSelectedDate();
                        editTextDate.setText(selectedDate);
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
