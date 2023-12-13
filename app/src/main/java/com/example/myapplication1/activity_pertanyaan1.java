package com.example.myapplication1;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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

                final String value = editTextDate.getText().toString().trim();

                try{

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());

                    Date selecteddate = sdf.parse(selectedDate);
                    Date currentDate = new Date();


                    long diffInMillisBegin = currentDate.getTime() - selecteddate.getTime();
                    long result = diffInMillisBegin / (24 * 60 * 60 * 1000);
                    long minimalHaid = 3285;

                    if (TextUtils.isEmpty(value)) {
                        Toast.makeText(activity_pertanyaan1.this, "Isi pertanyaan terlebih dahulu", Toast.LENGTH_SHORT).show();
                        return;
                    }else if( result < minimalHaid){
                        Toast.makeText(activity_pertanyaan1.this, "Anda tidak memenuhi batas minimal wanita haid", Toast.LENGTH_SHORT).show();
                        Logout();
                        return;
                    }

                    // Mengambil nilai token dari Intent
                    String token = getIntent().getStringExtra("token");
//
//                    // Membuat Intent baru dan menyertakan nilai token
                    Intent intent = new Intent(activity_pertanyaan1.this, activity_pertanyaan2.class);
                    intent.putExtra("token", token);
                    intent.putExtra("value1", value1);
                    startActivity(intent);

                }catch (ParseException exception){
                Toast.makeText(activity_pertanyaan1.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                return;
                }

            }

        });
    }

    private void Logout() {

        RequestQueue queue = Volley.newRequestQueue(activity_pertanyaan1.this);
        String url = Db_Contract.urlLogout;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    int status = jsonResponse.getInt("status");
                    String message = jsonResponse.getString("message");
                    if (status == 1) {
                        Intent intent = new Intent(activity_pertanyaan1.this, WelcomeActivity.class);
                        startActivity(intent);
                    } else {
                        throw new JSONException(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(activity_pertanyaan1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(activity_pertanyaan1.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> paramV = new HashMap<>();
                paramV.put("token", LoginActivity.token);
                return paramV;
            }
        };
        queue.add(stringRequest);
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }
}
