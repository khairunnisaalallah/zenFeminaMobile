package com.example.myapplication1;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
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

public class notifSetting extends AppCompatActivity {
    ImageButton bback;
    Switch notificationSwitch; // Deklarasi switch
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif_set);
        bback = findViewById(R.id.back);
        bback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kembali ke ProfileFragment
                onBackPressed();
            }
        });

        // Inisialisasi switch
        notificationSwitch = findViewById(R.id.switchnotif);
        requestQueue = Volley.newRequestQueue(this);

        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    updateReminder(LoginActivity.token, "reminder_id", "message", "1", "08:00", true, "cycleEstId");
                } else {
                    updateReminder(LoginActivity.token, "reminder_id", "message", "0", "08:00", false, "cycleEstId");
                }
            }
        });
    }

    private void updateReminder(String token, String reminderId, String message, String reminderDays, String reminderTime, boolean isOn, String cycleEstId) {
        String url = Db_Contract.urlreminderUpdate + "?token=" + token;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle response here
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(notifSetting.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                params.put("reminder_id", reminderId);
                params.put("message", message);
                params.put("reminderDays", reminderDays);
                params.put("reminderTime", reminderTime);
                params.put("is_on", String.valueOf(isOn));
                params.put("cycleEst_id", cycleEstId);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
