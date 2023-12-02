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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class activity_pertanyaan4 extends AppCompatActivity {

    private EditText editTextDate;
    private Button nextButton;

    private String value1, value2, value3, value4, token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pertanyaan4);

        editTextDate = findViewById(R.id.editTextDate2);
        nextButton = findViewById(R.id.selanjutnya4);

        // Ambil data dari aktivitas pertanyaan sebelumnya
        Intent intent = getIntent();
        value1 = intent.getStringExtra("value1");
        value2 = intent.getStringExtra("value2");
        value3 = intent.getStringExtra("value3");
        token = intent.getStringExtra("token");

        if (value1 == null || value2 == null || value3 == null || token == null) {
            Toast.makeText(this, "Data tidak lengkap, isi semua pertanyaan", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            // Semua data sudah diisi, lanjutkan ke aktivitas berikutnya
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String editTextValue = editTextDate.getText().toString();
                    value4 = editTextValue;

                    kirimDataKeAPI();
                }
            });
        }
    }

    private void kirimDataKeAPI() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Db_Contract.urlpertanyaan;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("API_RESPONSE", response); // Cetak respon API di logcat
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if (status.equals("1")) {
                                Toast.makeText(getApplicationContext(), "Sukses", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(activity_pertanyaan4.this, FragmentActivity.class);
                                intent.putExtra("value4", value4);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("birthday", value1);
                params.put("lastdate", value2);
                params.put("cycle", value3);
                params.put("period", value4);
                params.put("token", token);
                return params;
            }
        };

        queue.add(stringRequest);
    }
}
