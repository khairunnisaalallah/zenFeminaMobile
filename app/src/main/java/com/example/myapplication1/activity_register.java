package com.example.myapplication1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.util.HashMap;
import java.util.Map;

public class activity_register extends AppCompatActivity {
    //INISIASI NYA DISINI YAA JANGAN LUPA
    EditText etusername,etemail, etpw;
    String username, email, password;
    Button buttondaftar;
    TextView tvErorr, tvloginNow;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etusername = findViewById(R.id.username);
        etemail = findViewById(R.id.email);
        etpw = findViewById(R.id.password);
        buttondaftar = findViewById(R.id.daftar);
        tvErorr = findViewById(R.id.error);
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
            public void onClick(View view){
                tvErorr.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

                username = String.valueOf(etusername.getText());
                email = String.valueOf(etemail.getText());
                password =  etpw.getText().toString();
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = Db_Contract.urlRegister;

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressBar.setVisibility(View.GONE);
                                if(response.equals("Sukses")){
                                    Toast.makeText(getApplicationContext(), "Registrasi berhasil", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(activity_register.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    tvErorr.setText(response);
                                    tvErorr.setVisibility(View.VISIBLE);
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        tvErorr.setText(error.getLocalizedMessage());
                        tvErorr.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), "Error: " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){
                    protected Map<String, String> getParams(){
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
        ImageButton backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kembali ke ProfileFragment
                onBackPressed();
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
            }
        });

    }

    private class
    ErrorListener {
    }
}
