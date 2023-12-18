package com.example.myapplication1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class activity_profilepassword extends AppCompatActivity {

    EditText firstPass, secondPass;
    Button btnSave;

    String passFirst, passSecond;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilepass);

        tokenManager token = new tokenManager(getApplicationContext());

        firstPass = findViewById(R.id.firstpassword);
        secondPass = findViewById(R.id.secondpass);
        btnSave = findViewById(R.id.simpan);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passFirst = String.valueOf(firstPass.getText());
                passSecond = String.valueOf(secondPass.getText());

                // Validasi jika email atau password kosong

                if(firstPass.length() != 8 || secondPass.length() != 8){
                    Toast.makeText(activity_profilepassword.this, "Password harus terdiri dari 8 karakter", Toast.LENGTH_SHORT).show();
                } else if(passFirst.equals(passSecond)){
                    // Jika email dan password terisi, lakukan proses login
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    String url = Db_Contract.urlprofilePass;
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        String status = jsonObject.getString("status");
                                        String message = jsonObject.getString("message");
                                        if (status.equals("1")) {
                                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                            onBackPressed();
                                        } else {
                                            throw new JSONException(message);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(activity_profilepassword.this, "Terjadi kesalahan, coba lagi nanti", Toast.LENGTH_SHORT).show();

                        }
                    }) {
                        protected Map<String, String> getParams() {
                            Map<String, String> paramV = new HashMap<>();
                            paramV.put("token", token.getToken());
                            paramV.put("password", passFirst);
                            return paramV;
                        }
                    };
                    queue.add(stringRequest);
                } else {
                    Toast.makeText(activity_profilepassword.this, "kedua input harus bernilai sama", Toast.LENGTH_SHORT).show();
                }
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
    }
}
