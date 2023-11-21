package com.example.myapplication1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class activity_register extends AppCompatActivity {

    private EditText etEmail, etUsername, etPassword;
    private Button btnRegister;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etEmail = findViewById(R.id.editTextEmail);
        etUsername = findViewById(R.id.username);
        etPassword = findViewById(R.id.sandi);
        btnRegister = findViewById(R.id.daftar);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString().trim();
                String username = etUsername.getText().toString().trim();
                String sandi = etPassword.getText().toString().trim();

                if (!(username.isEmpty() || sandi.isEmpty())) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Db_Constract.urlRegister,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    if (error instanceof AuthFailureError) {
                                        Toast.makeText(getApplicationContext(), "Auth Failure Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("email", email);
                            params.put("username", username);
                            params.put("sandi", sandi);
                            return params;
                        }

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> headers = new HashMap<>();
                            // Tambahkan header otentikasi jika diperlukan
                            // Contoh: headers.put("Authorization", "Bearer YOUR_ACCESS_TOKEN");
                            return headers;
                        }
                    };

                    // Set a custom timeout (e.g., 10 seconds)
                    int customTimeout = 10000; // 10 seconds in milliseconds
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                            customTimeout,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);

                } else {
                    Toast.makeText(getApplicationContext(), "Ada Data Yang Masih Kosong", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // ImageButton untuk registrasi dengan nomor telepon
        ImageButton phoneSignUpButton = findViewById(R.id.telepon);
        phoneSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect ke aktivitas yang meminta nomor telepon
                Intent intent = new Intent(activity_register.this, VerifActivity.class);
                startActivity(intent);
            }
        });

        // ImageButton untuk registrasi dengan Google
        ImageButton googleSignUpButton = findViewById(R.id.google);
        googleSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_register.this, VerifActivity.class);
                startActivity(intent);
                // Implementasi registrasi dengan Google
                // Gunakan Firebase Authentication atau Google Sign-In API
            }
        });

        // ImageButton untuk registrasi dengan Google
        Button daftarButton = findViewById(R.id.daftar);
        daftarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_register.this, VerifActivity.class);
                startActivity(intent);
            }
        });
    }
}