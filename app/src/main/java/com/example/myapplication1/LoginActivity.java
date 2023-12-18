package com.example.myapplication1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class LoginActivity extends AppCompatActivity {

    Button btnmasuk;
    EditText etemail, etpw;
    TextView tvregisterNow;
    ProgressBar progressBar;
    String username, email, password, birthDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tokenManager token = new tokenManager(getApplicationContext());

        btnmasuk = findViewById(R.id.masuk);
        etemail = findViewById(R.id.email);
        etpw = findViewById(R.id.password);
        progressBar = findViewById(R.id.loading);
        tvregisterNow = findViewById(R.id.registerNow);

        btnmasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                email = String.valueOf(etemail.getText());
                password = String.valueOf(etpw.getText());

                // Validasi jika email atau password kosong
                if (email.isEmpty() || password.isEmpty()) {
                    progressBar.setVisibility(View.GONE);
                    if (email.isEmpty() && password.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Email dan password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    } else if (email.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Email tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    // Jika email dan password terisi, lakukan proses login
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    String url = Db_Contract.urlLogin;
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    progressBar.setVisibility(View.GONE);
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        String status = jsonObject.getString("status");
                                        String message = jsonObject.getString("message");
                                        token.saveToken(jsonObject.getString("token"));
                                        if (status.equals("1")) {
                                            Toast.makeText(getApplicationContext(), "Berhasil Masuk", Toast.LENGTH_SHORT).show();
                                            getbirthdate(token.getToken());
                                        } else {
                                           Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, "Terjadi kesalahan, coba lagi nanti", Toast.LENGTH_SHORT).show();

                        }
                    }) {
                        protected Map<String, String> getParams() {
                            Map<String, String> paramV = new HashMap<>();
                            paramV.put("email", email);
                            paramV.put("password", password);
                            return paramV;
                        }
                    };
                    queue.add(stringRequest);
                }
            }
        });
        tvregisterNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Arahkan ke RegisterActivity
                Intent intent = new Intent(LoginActivity.this, activity_register.class);
                startActivity(intent);
                finish();
            }

        });
        // Impor ImageButton
        ImageView backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kembali ke ProfileFragment
                onBackPressed();
            }
        });
        TextView lupapw = findViewById(R.id.lupapw);
        lupapw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, activity_verif.class);
                startActivity(intent);
            }
        });
    }

    private void getbirthdate(String token) {
        // URL endpoint API get_profile() dengan parameter token yang sesuai
        String url = Db_Contract.urlProfile + "?token=" + token;

        // Buat request GET menggunakan Volley
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle response dari server jika permintaan sukses
                        try {
                            JSONArray dataArray = response.getJSONArray("data");
                            JSONObject userData = dataArray.getJSONObject(0);
                            birthDate = userData.getString("birthdate");

                            Intent intent;
                            if (birthDate.length() == 4) {
                                intent = new Intent(LoginActivity.this, activity_pertanyaan1.class);
                            } else {
                                intent = new Intent(LoginActivity.this, FragmentActivity.class);
                            }

                            if (token != null && !token.equals("")) {
                                intent.putExtra("token", token);
                            }
                            startActivity(intent);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Error parsing JSON", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error jika permintaan gagal
                error.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Tambahkan request ke queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

}
