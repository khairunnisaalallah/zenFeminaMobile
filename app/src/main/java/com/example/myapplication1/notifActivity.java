package com.example.myapplication1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class notifActivity extends AppCompatActivity {
//    TextView textView;
    RelativeLayout btnAwal, btnAkhir;
    ImageView btnback;

    TextView isiAwalSiklus, isiAkhirSiklus;

    public static String reminderIdStart, reminderIdEnd;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif);

        tokenManager token = new tokenManager(getApplicationContext());

        String urlEstimation = Db_Contract.urlGetAllReminder + "?token=" + token.getToken();
        StringRequest estimationRequest = new StringRequest(Request.Method.GET, urlEstimation, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isiAwalSiklus = findViewById(R.id.tvIsiAwalsiklus);
                isiAkhirSiklus = findViewById(R.id.tvIsiAkhirsiklus);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray dataArray = jsonObject.getJSONArray("data");

                    reminderIdStart = dataArray.getJSONObject(0).getString("reminder_id");
                    reminderIdEnd = dataArray.getJSONObject(1).getString("reminder_id");
                    String isiAwal = dataArray.getJSONObject(0).getString("message");
                    String isiAkhir = dataArray.getJSONObject(1).getString("message");

                    isiAwalSiklus.setHint(isiAwal);
                    isiAkhirSiklus.setHint(isiAkhir);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(notifActivity.this, "Error parsing JSON", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(notifActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Menambahkan permintaan estimasi ke dalam antrian
        RequestQueue estimationQueue = Volley.newRequestQueue(notifActivity.this);
        estimationQueue.add(estimationRequest);


        btnAwal = findViewById(R.id.btnawalsiklus);
        btnAwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(notifActivity.this, edit_notif_awal.class);
                startActivity(intent);
            }
        });

        btnAkhir = findViewById(R.id.btnakhirsiklus);
        btnAkhir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(notifActivity.this, edit_notif_akhir.class);
                startActivity(intent);
            }
        });

        btnback = findViewById(R.id.back);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(notifActivity.this, FragmentActivity.class);
                startActivity(intent);
            }
        });
    }
    //hati2 buat notif muncul

}
