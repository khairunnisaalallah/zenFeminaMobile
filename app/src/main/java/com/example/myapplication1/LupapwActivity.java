package com.example.myapplication1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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


public class LupapwActivity extends AppCompatActivity {

    private ImageView btnBack;
    private EditText txtPass1, txtPass2;
    private Button btnReset;
    private String pass1, pass2;
    private String verification_id = activity_verif.verification_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupapw);

        btnBack = findViewById(R.id.back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LupapwActivity.this, activity_otp.class);
                startActivity(intent);
            }
        });

        txtPass1 = findViewById(R.id.Password1);
        txtPass2 = findViewById(R.id.Password2);
        btnReset = findViewById(R.id.btnreset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass1 = String.valueOf(txtPass1.getText());
                pass2 = String.valueOf(txtPass2.getText());

                if(pass1.equals(pass2)){
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    String url = Db_Contract.urlNewPassword;
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                try{
                                    JSONObject jsonObject = new JSONObject(response);
                                    String status = jsonObject.getString("status");
                                    String message = jsonObject.getString("message");
                                    if (status.equals("1")){
                                        Toast.makeText(getApplicationContext(), "Password anda berhasil diperbarui", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent (LupapwActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "eror" + error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }){
                        protected Map<String, String> getParams(){
                            Map<String, String> paramV = new HashMap<>();
                            paramV.put("verification_id", verification_id);
                            paramV.put("firstPassword", pass1);
                            return paramV;
                        }
                    };
                    queue.add(stringRequest);
                } else {
                    Toast.makeText(getApplicationContext(), "Kata sandi yang anda masukkan tidak sama", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }



}


