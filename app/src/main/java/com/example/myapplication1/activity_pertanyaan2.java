package com.example.myapplication1;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class activity_pertanyaan2 extends AppCompatActivity {

    private ImageButton datePicker;
    private Button nextButton;
    private Calendar calendar;

    public static String value2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pertanyaan2);

        datePicker = findViewById(R.id.imageButton2);
        nextButton = findViewById(R.id.selanjutnya2);
        calendar = Calendar.getInstance();

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String selectedDateTime = getSelectedDateTime();
                    String token = getIntent().getStringExtra("token");
                    String value1 = getIntent().getStringExtra("value1");

                    if (selectedDateTime != null) {
                        Intent intent = new Intent(activity_pertanyaan2.this, activity_pertanyaan3.class);
                        intent.putExtra("token", token);
                        intent.putExtra("value1", value1);
                        intent.putExtra("value2", selectedDateTime); // Menyertakan value2 dalam Intent
                        startActivity(intent);
                    } else {
                        Toast.makeText(activity_pertanyaan2.this, "Pilih tanggal terlebih dahulu", Toast.LENGTH_SHORT).show();
                    }
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
                        calendar.set(year, month, dayOfMonth);

                        calendar.set(Calendar.HOUR_OF_DAY, Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
                        calendar.set(Calendar.MINUTE, Calendar.getInstance().get(Calendar.MINUTE));

                        EditText editTextDate2 = findViewById(R.id.editTextDate2);
                        editTextDate2.setText(getSelectedDateTime());
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private String getSelectedDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }
}
