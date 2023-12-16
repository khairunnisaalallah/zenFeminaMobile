package com.example.myapplication1;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

public class edit_notif_akhir extends AppCompatActivity {
    private RelativeLayout btnJam, btnpengingat, btnpesan;

    private TextView etjam, etpengingat, etpesan;
    ImageView btnback;

    private int jam,menit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_notif_akhir);

        btnpengingat = findViewById(R.id.btnpengingat);
        btnJam = findViewById(R.id.btnwaktupengingat);
        etjam = findViewById(R.id.tvIsiwaktupengingat);
        btnback = findViewById(R.id.back);
        btnpesan = findViewById(R.id.btnpesan);

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(edit_notif_akhir.this, notifActivity.class);
                startActivity(intent);
            }
        });

        btnpengingat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Panggil metode untuk menampilkan dialog NumberPicker
                showNumberPickerDialog();
            }
        });

        btnJam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                jam = calendar.get(Calendar.HOUR_OF_DAY);
                menit = calendar.get(Calendar.MINUTE);

                TimePickerDialog dialog;
                dialog = new TimePickerDialog(edit_notif_akhir.this, R.style.CustomTimePickerDialogStyle, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        jam = hourOfDay;
                        menit = minute;

                        if (jam <= 12) {
                            etjam.setHint(String.format(Locale.getDefault(), "%d:%d am", jam, menit));
                        } else {
                            etjam.setHint(String.format(Locale.getDefault(), "%d:%d pm", jam, menit));
                        }
                    }
                }, jam, menit, true);
                dialog.show();
            }
        });

        btnpesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();
            }
        });
    }

    private void showAlertDialog() {
        etpesan = findViewById(R.id.tvIsipesan);

        // Create a LayoutInflater to inflate the custom layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.pesan_dialog, null);

        // Find the EditText inside the custom layout
        final EditText customEditText = dialogView.findViewById(R.id.customEditText);

        // Create the AlertDialog with the custom layout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setTitle("Pesan notifikasi : ");

        // Set up the buttons
        alertDialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String message = customEditText.getText().toString();
                etpesan.setHint(message);
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Show the dialog
        alertDialogBuilder.show();
    }

    private void showNumberPickerDialog() {
        // Buat instance NumberPickerDialog dan tunjukkan
        numberpicker dialog = new numberpicker(this, 1, new numberpicker.OnValueChangeListener() {
            @Override
            public void onValueChanged(int value) {
                handleValueChanged(value);
            }
        });
        dialog.show();
    }

    private void handleValueChanged(int value) {
        etpengingat = findViewById(R.id.tvIsipengingat);

        // Konversi nilai integer ke string sebelum diatur sebagai hint
        String hintValue = String.valueOf(value);

        // Set hint pada EditText
        etpengingat.setHint(hintValue + " hari sebelumnya");
    }
}
