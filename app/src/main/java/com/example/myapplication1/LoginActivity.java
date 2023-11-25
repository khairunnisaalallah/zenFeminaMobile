package com.example.myapplication1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
    TextView tvErorr, tvregisterNow;
    ProgressBar progressBar;
    String username, email, password, session_id;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnmasuk =findViewById(R.id.masuk);
        etemail = findViewById(R.id.email);
        etpw = findViewById(R.id.password);
        tvErorr = findViewById(R.id.error);
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
                tvErorr.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                email = String.valueOf(etemail.getText());
                password = String.valueOf(etpw.getText());
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = Db_Contract.urlLogin;
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressBar.setVisibility(View.GONE);
                                try{
                                    JSONObject jsonObject = new JSONObject(response);
                                    String status = jsonObject.getString("status");
                                    String message = jsonObject.getString("message");
                                    if (status.equals("1")){
                                        Toast.makeText(getApplicationContext(), "Berhasil Masuk", Toast.LENGTH_SHORT).show();
                                        //habis login masuk ke activity_pertanyaan1
                                        Intent intent = new Intent (LoginActivity.this, activity_pertanyaan1.class);
                                        startActivity(intent);
                                        finish();
//                                        username = jsonObject.getString("username");
//                                        email = jsonObject.getString("email");
//                                        session_id = jsonObject.getString("session_id");
//                                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                                        editor.putString("logged", "true");
//                                        editor.putString("username", username);
//                                        editor.putString("email", email);
//                                        editor.putString("session_id", session_id);
//                                        editor.apply();
                                    }
                                    else {
                                        tvErorr.setText((message));
                                        tvErorr.setVisibility(View.VISIBLE);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);


                    }
                }){
                    protected Map<String, String> getParams(){
                        Map<String, String> paramV = new HashMap<>();
                        paramV.put("email", email);
                        paramV.put("password", password);
                        return paramV;
                    }
                };
                queue.add(stringRequest);
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
                Intent intent = new Intent(LoginActivity.this, LupapwActivity.class);
                startActivity(intent);
            }
        });
        }
}