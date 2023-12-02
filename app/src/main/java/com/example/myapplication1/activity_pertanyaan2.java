package com.example.myapplication1;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class activity_pertanyaan2 extends AppCompatActivity {

    private ImageButton datePicker;
    private Button nextButton;
    private Calendar calendar;
    public static String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pertanyaan2);

        datePicker = findViewById(R.id.imageView);
        nextButton = findViewById(R.id.selanjutnya2);
        calendar = Calendar.getInstance();

        // Menampilkan DatePickerDialog saat ImageButton tanggal diklik
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // Handle navigasi ke layout pertanyaan selanjutnya saat tombol "Selanjutnya" diklik
        // Handle navigasi ke layout pertanyaan selanjutnya saat tombol "Selanjutnya" diklik
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedDateTime = getSelectedDateTime();
                value=selectedDateTime;

                Intent intent = new Intent(activity_pertanyaan2.this, activity_pertanyaan3.class);
                intent.putExtra("value2", value);
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

                        // Tambahkan waktu otomatis (jam saat ini)
                        calendar.set(Calendar.HOUR_OF_DAY, Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
                        calendar.set(Calendar.MINUTE, Calendar.getInstance().get(Calendar.MINUTE));

                        String selectedDateTime = getSelectedDateTime();
                        EditText editTextDate2 = findViewById(R.id.editTextDate2);
                        editTextDate2.setText(selectedDateTime);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()); // Batasi tanggal yang bisa dipilih menjadi hari ini atau sebelumnya
        datePickerDialog.show();
    }

    private String getSelectedDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }
}
