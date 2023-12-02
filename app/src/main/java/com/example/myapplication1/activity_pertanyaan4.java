package com.example.myapplication1;

import android.content.Intent;
import android.os.Bundle;
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
        String token = getIntent().getStringExtra("token");
        String value1 = getIntent().getStringExtra("value1");
        String value2 = getIntent().getStringExtra("value2");
        String value3 = getIntent().getStringExtra("value3");

        // Periksa nilai null sebelum digunakan
        if (token == null || value1 == null || value2 == null || value3 == null) {
            // Handle kasus di mana ada nilai null
            Toast.makeText(this, "Nilai token atau value null", Toast.LENGTH_SHORT).show();
            return;
        }

        final String dataPertanyaan4 = editTextDataPertanyaan4.getText().toString().trim();

        if (dataPertanyaan4.isEmpty()) {
            Toast.makeText(this, "Isi pertanyaan terlebih dahulu", Toast.LENGTH_SHORT).show();
            return;
        }

        // Ganti URL dengan Db_Contract.urlpertanyaan
        String url = Db_Contract.urlpertanyaan;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("TAG", "Response: " + response);

                        // Handle response dari server jika diperlukan
                        // Misalnya, tampilkan pesan sukses atau lanjut ke halaman berikutnya
                        Toast.makeText(activity_pertanyaan4.this, "Data berhasil dikirim", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(activity_pertanyaan4.this, FragmentActivity.class);
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
                params.put("value1", value1);
                params.put("value2", value2);
                params.put("value3", value3);
                params.put("dataPertanyaan4", dataPertanyaan4);

                // Hapus parameter yang memiliki nilai null
                params.values().removeIf(Objects::isNull);

                return params;
            }
        };

        // Tambahkan request ke antrian Volley
        Volley.newRequestQueue(this).add(stringRequest);
    }
}
