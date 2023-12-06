package com.example.myapplication1;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    private EditText editTextName, editTextPhone, editTextUsername, editTextEmail, editTextBirthdate, editTextPassword;
    private String name, phone, username, email, birthdate, password;
    Button buttonsave;
    String url = Db_Contract.urlProfile + "?token=" + LoginActivity.token;
    private ImageButton datePicker;
    private Calendar calendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        // Inisialisasi EditText di dalam metode onCreate
        editTextName = findViewById(R.id.name);
        editTextEmail = findViewById(R.id.email);
        editTextPhone = findViewById(R.id.phone);
        editTextUsername = findViewById(R.id.username);
        editTextBirthdate = findViewById(R.id.birthdate);
        buttonsave = findViewById(R.id.simpanp);
        editTextPassword = findViewById(R.id.password);

        //calender

        datePicker = findViewById(R.id.calender);
        calendar = Calendar.getInstance();
        // Menampilkan DatePickerDialog saat ImageButton diklik
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        buttonsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveOrUpdateProfile();
            }
        });
        ImageView backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kembali ke ProfileFragment
                onBackPressed();
            }
        });
        // Membuat request GET untuk mendapatkan data dari server
        getDataFromServer();

    }
    private void getDataFromServer() {
        // URL endpoint API get_profile() dengan parameter token yang sesuai
        String url = Db_Contract.urlProfile + "?token=" + LoginActivity.token;

        // Buat request GET menggunakan Volley
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle response dari server jika permintaan sukses
                        try {
                            JSONArray dataArray = response.getJSONArray("data");
                            JSONObject userData = dataArray.getJSONObject(0);
                            // Ambil data profil dari respons JSON
                            name = userData.getString("name");
                            email = userData.getString("email");
                            phone = userData.getString("phone");
                            username = userData.getString("username");
                            birthdate = userData.getString("birthdate");
                            password = userData.getString("password");

                            // Cek jika nilai kosong atau "null", ganti dengan "-"
                            name = (name.equals("null") || name.isEmpty()) ? "-" : name;
                            email = (email.equals("null") || email.isEmpty()) ? "-" : email;
                            phone = (phone.equals("null") || phone.isEmpty()) ? "-" : phone;
                            username = (username.equals("null") || username.isEmpty()) ? "-" : username;
                            birthdate = (birthdate.equals("null") || birthdate.isEmpty()) ? "-" : birthdate;
                            password = (password.equals("null") || password.isEmpty()) ? "-" : password;

                            // Set nilai EditText dengan data yang diperoleh dari respons server
                            editTextName.setText(name);
                            editTextEmail.setText(email);
                            editTextPhone.setText(phone);
                            editTextUsername.setText(username);
                            editTextBirthdate.setText(birthdate);
                            editTextPassword.setText(password);


                            editTextName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                @Override
                                public void onFocusChange(View v, boolean hasFocus) {
                                    if (!hasFocus && editTextName.getText().toString().isEmpty()) {
                                        name = "-";
                                    } else {
                                        name = editTextName.getText().toString();
                                    }
                                }
                            });


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Error parsing JSON", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error jika permintaan gagal
                Toast.makeText(ProfileActivity.this, "Gagal mendapatkan data!", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Tambahkan request ke queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    // Metode untuk menyimpan atau memperbarui profil
    private void saveOrUpdateProfile() {
        String url = Db_Contract.urlProfile + "?token=" + LoginActivity.token;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        // Mendapatkan data dari EditText di dalam metode onCreate
        name = editTextName.getText().toString();
        phone = editTextPhone.getText().toString();
        username = editTextUsername.getText().toString();
        email = editTextEmail.getText().toString();
        birthdate = editTextBirthdate.getText().toString();
        password = editTextPassword.getText().toString();
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date date = inputFormat.parse(birthdate);
            birthdate = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Membuat objek JSON yang berisi data yang akan dikirim

        final Map<String, String> paramV = new HashMap<>();
        paramV.put("name", name);
        paramV.put("password", password);
        paramV.put("email", email);
        paramV.put("phone", phone);
        paramV.put("username", username);
        paramV.put("birthDate", birthdate);

        // Kirim data ke server
        sendDataToServer(url, paramV);
        // Buat StringRequest untuk mengirim data ke server
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle response dari server jika permintaan sukses
                        Toast.makeText(ProfileActivity.this, "Data berhasil disimpan!!!", Toast.LENGTH_SHORT).show();
                        Log.d("ServerResponse", "Response: " + response); // Tambahkan log respons dari server di sini
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error jika permintaan gagal
                Toast.makeText(ProfileActivity.this, "Gagal menyimpan data!", Toast.LENGTH_SHORT).show();
                Log.e("SendDataToServer", "Error: " + error.toString());
                error.printStackTrace();

                if (error.networkResponse != null && error.networkResponse.data != null) {
                    // Respons dari server
                    String errorResponse = new String(error.networkResponse.data);
                    Log.e("ServerError", "Server response: " + errorResponse);
                    // Lakukan sesuatu dengan respons dari server (mungkin ada informasi kesalahan)
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return paramV;
            }
        };
        // Tambahkan request ke queue
        requestQueue.add(stringRequest);


    }

    private void sendDataToServer(String url, final Map<String, String> paramV) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle response dari server jika permintaan sukses
                        Toast.makeText(ProfileActivity.this, "Data berhasil disimpan!!", Toast.LENGTH_SHORT).show();
//                        Log.d("SendDataToServer", "Params: " + paramV.toString()); // Tambahkan log untuk mengecek parameter yang akan dikirim
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error jika permintaan gagal
                Toast.makeText(ProfileActivity.this, "Gagal menyimpan data!", Toast.LENGTH_SHORT).show();
                Log.e("SendDataToServer", "Error: " + error.toString());
                error.printStackTrace();

                if (error.networkResponse != null && error.networkResponse.data != null) {
                    // Respons dari server
                    String errorResponse = new String(error.networkResponse.data);
                    Log.e("ServerError", "Server response: " + errorResponse);
                    // Lakukan sesuatu dengan respons dari server (mungkin ada informasi kesalahan)
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return paramV;
            }
        };
        Log.d("SendDataToServer", "Params: " + paramV.toString()); // Tambahkan log untuk mengecek parameter yang akan dikirim


        // Menambahkan request ke queue
        requestQueue.add(stringRequest);
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
                        editTextBirthdate.setText(selectedDate);
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


