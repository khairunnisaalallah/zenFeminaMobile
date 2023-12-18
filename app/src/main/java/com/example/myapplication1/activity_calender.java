package com.example.myapplication1;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class activity_calender extends AppCompatActivity {

    private TextView monthTitle;
    private CalendarView calendarView;
    private CheckBox checkBox;
    private RadioGroup prayerCheckBoxGroup;

    private String cycleHistoryId;

    ArrayList<item> items = new ArrayList<item>();

    RequestQueue queue;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

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


        ImageView backButton = findViewById(R.id.back);
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
        queue = Volley.newRequestQueue(this);

        // Ambil data dari API
    }
}
