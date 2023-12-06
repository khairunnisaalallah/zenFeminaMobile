package com.example.myapplication1;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class mainNotif extends AppCompatActivity {
    Switch button;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.main_notif);
        button = findViewById(R.id.btnNotifications);
        requestQueue = Volley.newRequestQueue(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(mainNotif.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(mainNotif.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeNotification();
                requestHistoryData(LoginActivity.token); // Replace with your token
                requestAllReminders(LoginActivity.token); // Mengambil semua pengingat
                requestReminderById(LoginActivity.token, "reminder_id"); // Mengambil pengingat berdasarkan ID tertentu
            }
        });
    }
    private void requestHistoryData(String token) {
        String url = Db_Contract.urlHistory + "?token=" + LoginActivity.token;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String startDate = jsonObject.getString("start_date");
                            int cycleLength = jsonObject.getInt("cycle_length");
                            calculateNotificationDate(startDate, cycleLength);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mainNotif.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }
    private void calculateNotificationDate(String startDate, int cycleLength) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date start = sdf.parse(startDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(start);
            calendar.add(Calendar.DATE, -1); // Membuat notifikasi H-1
            scheduleNotification(calendar.getTimeInMillis(), "Besok kamu mulai halangan", "Persiapkan diri untuk periode halangan besok.");

            // Notifikasi H-1 Selesai Halangan
            calendar.add(Calendar.DATE, cycleLength);
            scheduleNotification(calendar.getTimeInMillis(), "Besok kamu mulai bersuci", "Persiapkan diri untuk periode bersuci besok.");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    private void scheduleNotification(long time, String title, String content) {
        Intent intent = new Intent(getApplicationContext(), notifActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("data", "some value to be passed here");

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_MUTABLE);
        String chanelID = "CHANNEL_ID_NOTIFICATION";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), chanelID)
                .setSmallIcon(R.drawable.notip)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(chanelID);
            if (notificationChannel == null) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                notificationChannel = new NotificationChannel(chanelID, "Some description", importance);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
                //                notificationChannel(notificationChannel);
            }
        }
        notificationManager.notify(0, builder.build());
    }
    public void makeNotification(){
        String chanelID = "CHANNEL_ID_NOTIFICATION";
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext(),chanelID);
        builder.setSmallIcon(R.drawable.notip)
                .setContentTitle("Siklus haid akan dimulai")
                .setContentText("Jangan lupa mencatat harinya")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent intent = new Intent(getApplicationContext(), notifActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("data", "some value to be passed here");

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                0, intent, PendingIntent.FLAG_MUTABLE);
        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel =
                    notificationManager.getNotificationChannel(chanelID);
            if (notificationChannel == null){
                int importance = NotificationManager.IMPORTANCE_HIGH;
                notificationChannel = new NotificationChannel(chanelID,
                        "Some description", importance);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
        notificationManager.notify(0, builder.build());
    }
    private void requestAllReminders(String token) {
        // Implementasi untuk mengambil semua pengingat
        // Gunakan Volley atau alat lain untuk membuat permintaan ke API Anda
        String url = Db_Contract.urlreminderGet + "?token=" + LoginActivity.token;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray reminders = jsonObject.getJSONArray("data");
                            for (int i = 0; i < reminders.length(); i++) {
                                JSONObject reminder = reminders.getJSONObject(i);
                                // Proses data reminder di sini
                                // Misalnya, Anda bisa mengekstrak data dan menjadwalkan notifikasi
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mainNotif.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }
    private void requestReminderById(String token, String reminderId) {
        // Implementasi untuk mengambil pengingat berdasarkan ID
        // Gunakan Volley atau alat lain untuk membuat permintaan ke API Anda
        String url = Db_Contract.urlReminderbyId + "?token=" + LoginActivity.token + "&reminder_id=" + reminderId;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject reminder = jsonObject.getJSONObject("data");
                            // Proses data reminder di sini
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mainNotif.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }
    private void updateReminder(String token, String reminderId, String message, String reminderDays, String reminderTime, String isOn, String cycleEstId) {
        // Implementasi untuk memperbarui pengingat
        // Gunakan Volley atau alat lain untuk membuat permintaan ke API Anda

        String url = Db_Contract.urlreminderUpdate + "?token=" + LoginActivity.token;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Tangani respons berhasil di sini
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mainNotif.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                params.put("reminder_id", reminderId);
                params.put("message", message);
                params.put("reminderDays", String.valueOf(reminderDays));
                params.put("reminderTime", reminderTime);
                params.put("is_on", String.valueOf(isOn));
                params.put("cycleEst_id", cycleEstId);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}

