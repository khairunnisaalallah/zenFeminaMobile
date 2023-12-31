package com.example.myapplication1;

import android.content.Intent;
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

public class activity_register extends AppCompatActivity {
    //INISIASI NYA DISINI YAA JANGAN LUPA
    EditText etusername, etemail, etpw;
    String username, email, password;
    Button buttondaftar;
    TextView tvloginNow;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etusername = findViewById(R.id.username);
        etemail = findViewById(R.id.email);
        etpw = findViewById(R.id.password);
        buttondaftar = findViewById(R.id.daftar);
        progressBar = findViewById(R.id.loading);
        tvloginNow = findViewById(R.id.loginNow);

        tvloginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Arahkan ke RegisterActivity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        buttondaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                username = String.valueOf(etusername.getText());
                email = String.valueOf(etemail.getText());
                password = etpw.getText().toString();
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = Db_Contract.urlRegister;
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressBar.setVisibility(View.GONE);
                                if (username.isEmpty() && email.isEmpty() && password.isEmpty()) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), "Username, Email, dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                                } else if (username.isEmpty()) {
                                    Toast.makeText(getApplicationContext(), "Username tidak boleh kosong", Toast.LENGTH_SHORT).show();
                                } else if (email.isEmpty()) {
                                    Toast.makeText(getApplicationContext(), "Email tidak boleh kosong", Toast.LENGTH_SHORT).show();
                                } else if (password.isEmpty()) {
                                    Toast.makeText(getApplicationContext(), "Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                                }
                                try {
                                    JSONObject JSONObject = new JSONObject(response);
                                    String status = JSONObject.getString("status");
                                    String message = JSONObject.getString("message");
                                    if(status.equals("1")){
                                        Toast.makeText(getApplicationContext(), "Registrasi berhasil", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(activity_register.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }{
                                    }

                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Error: " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(activity_register.this, "Terjadi kesalahan, coba lagi nanti", Toast.LENGTH_SHORT).show();

                    }
                }) {
                    protected Map<String, String> getParams() {
                        Map<String, String> paramV = new HashMap<>();
                        paramV.put("username", username);
                        paramV.put("email", email);
                        paramV.put("password", password);
                        return paramV;
                    }
                };
                queue.add(stringRequest);
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
    }
}