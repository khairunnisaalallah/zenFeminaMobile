package com.example.myapplication1;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class activity_pertanyaan4 extends AppCompatActivity {

    private EditText editTextDataPertanyaan4;
    private Button kirimButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pertanyaan4);

        editTextDataPertanyaan4 = findViewById(R.id.editTextDate4);
        kirimButton = findViewById(R.id.selanjutnya4);

        kirimButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToServer();
            }
        });
    }

    private void sendDataToServer() {
        // Gunakan Bundle untuk mengirim data ke Intent
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Toast.makeText(this, "Data yang dibutuhkan tidak tersedia", Toast.LENGTH_SHORT).show();
            return;
        }

        String token = extras.getString("token");
        String value1 = extras.getString("value1");
        String value2 = extras.getString("value2");
        String value3 = extras.getString("value3");

        // Periksa null menggunakan TextUtils.isEmpty()
        if (TextUtils.isEmpty(token) || TextUtils.isEmpty(value1) || TextUtils.isEmpty(value2) || TextUtils.isEmpty(value3)) {
            Toast.makeText(this, "Nilai token atau value null", Toast.LENGTH_SHORT).show();
            return;
        }

        final String value4 = editTextDataPertanyaan4.getText().toString().trim();

        if (TextUtils.isEmpty(value4)) {
            Toast.makeText(this, "Isi pertanyaan terlebih dahulu", Toast.LENGTH_SHORT).show();
            return;
        }

        // Ganti URL dengan Db_Contract.urlpertanyaan
        String url = Db_Contract.urlpertanyaan + "?token=" + LoginActivity.token;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("TAG", "Response: " + response);

                        // Handle response dari server jika diperlukan
                        // Misalnya, tampilkan pesan sukses atau lanjut ke halaman berikutnya
                        Toast.makeText(activity_pertanyaan4.this, "Data berhasil dikirim", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(activity_pertanyaan4.this, FragmentActivity.class);

                        intent.putExtra("value4", value4);
                        intent.putExtra("value3", value3);
                        intent.putExtra("value1", value1);
                        intent.putExtra("value2", value2);
                        intent.putExtra("token", token);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", "Error: " + error.getMessage());
                        // Handle error, tampilkan pesan kesalahan jika diperlukan
                        Toast.makeText(activity_pertanyaan4.this, "Gagal mengirim data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Tambahkan parameter yang diperlukan oleh API
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                params.put("birthDate", value1);
                params.put("lastDate", value2);
                params.put("cycle", value3);
                params.put("period", value4);

                // Hapus parameter yang memiliki nilai null
                params.values().removeIf(Objects::isNull);

                return params;
            }
        };

        // Tambahkan request ke antrian Volley
        Volley.newRequestQueue(this).add(stringRequest);
    }
}
