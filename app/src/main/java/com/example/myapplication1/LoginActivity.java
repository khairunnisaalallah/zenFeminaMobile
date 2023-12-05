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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {

    Button btnmasuk;
    EditText etemail, etpw;
    TextView tvregisterNow;
    ProgressBar progressBar;
    String username, email, password, session_id;
    SharedPreferences sharedPreferences;
    public static String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnmasuk =findViewById(R.id.masuk);
        etemail = findViewById(R.id.email);
        etpw = findViewById(R.id.password);
//        tvErorr = findViewById(R.id.error);
        progressBar = findViewById(R.id.loading);
        tvregisterNow = findViewById(R.id.registerNow);
        sharedPreferences =getSharedPreferences("MyAppName", MODE_PRIVATE);

        if (sharedPreferences.getString("logged", "false").equals("true")){
            Intent intent = new Intent(getApplicationContext(), FragmentActivity.class);
            startActivity(intent);
            finish();
        }
        btnmasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                tvErorr.setVisibility(View.GONE);
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
                                        token = jsonObject.getString("token");
                                        if (status.equals("1")) {
                                            Toast.makeText(getApplicationContext(), "Berhasil Masuk", Toast.LENGTH_SHORT).show();
                                            //habis login masuk ke activity_pertanyaan1
                                            Intent intent = new Intent(LoginActivity.this, FragmentActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
//                                                tvErorr.setText((message));
//                                                tvErorr.setVisibility(View.VISIBLE);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(getApplicationContext(), "Username atau Kata sandi salah", Toast.LENGTH_SHORT).show();
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
}