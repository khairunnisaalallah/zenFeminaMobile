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

public class activity_otp extends AppCompatActivity {

    private ImageView btnBack;
    private Button btnOtp;
    private EditText txtOtp;
    private String otp;
    public static String verification_id = activity_verif.verification_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        btnBack = findViewById(R.id.Buttonback);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_otp.this, activity_verif.class);
                startActivity(intent);
            }
        });

        txtOtp = findViewById(R.id.otp);
        btnOtp = findViewById(R.id.sms);
        btnOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otp = String.valueOf(txtOtp.getText());

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = Db_Contract.urlOtpVerification;
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try{
                                    JSONObject jsonObject = new JSONObject(response);
                                    String status = jsonObject.getString("status");
                                    String message = jsonObject.getString("message");
                                    if (status.equals("1")){
                                        Toast.makeText(getApplicationContext(), "Kode yang anda masukkan benar", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent (activity_otp.this, LupapwActivity.class);
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
                        paramV.put("code", otp);
                        return paramV;
                    }
                };
                queue.add(stringRequest);
            }
        });

    }
}
