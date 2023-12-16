package com.example.myapplication1;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class edit_notif_akhir extends AppCompatActivity {
    private RelativeLayout btnJam, btnpengingat, btnpesan;

    private TextView etjam, etpengingat, etpesan;
    ImageView btnback;

    private int jam,menit;

    private Button btnSimpan;
    String cycleEst_id = "";
    String is_on, pengingat = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_notif_akhir);

        btnpengingat = findViewById(R.id.btnpengingat);
        btnJam = findViewById(R.id.btnwaktupengingat);
        etjam = findViewById(R.id.tvIsiwaktupengingat);
        btnback = findViewById(R.id.back);
        btnpesan = findViewById(R.id.btnpesan);

        etpesan = findViewById(R.id.tvIsipesan);
        etpengingat = findViewById(R.id.tvIsipengingat);
        Switch mySwitch = findViewById(R.id.switchnotifakhir);

        String urlreminder = Db_Contract.urlReminderbyId + "?token=" + LoginActivity.token + "&reminder_id=" + notifActivity.reminderIdEnd;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlreminder, null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray dataArray = response.getJSONArray("data");
                    JSONObject userData = dataArray.getJSONObject(0);
                    // Ambil data profil dari respons JSON
                    String pesan = userData.getString("message");
                    String pengingat = userData.getString("reminderDays");
                    String waktuPengingat = userData.getString("reminder_time");
                    is_on = userData.getString("is_on");
                    cycleEst_id = userData.getString("cycleEst_id");

                    if(is_on.equals("1")){
                        mySwitch.setChecked(true);
                    }

                    etpesan.setHint(pesan);
                    if(pengingat.equals("0")){
                        etpengingat.setHint("Dihari yang sama");
                    } else {
                        etpengingat.setHint(pengingat + " Hari Sebelumnya");
                    }
                    etjam.setHint(waktuPengingat.substring(0, 5));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(edit_notif_akhir.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(edit_notif_akhir.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Menambahkan permintaan estimasi ke dalam antrian
        RequestQueue estimationQueue = Volley.newRequestQueue(edit_notif_akhir.this);
        estimationQueue.add(jsonObjectRequest);

        mySwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(is_on.equals("1")){
                    is_on = "0";
                } else {
                    is_on = "1";
                }
            }
        });

        btnSimpan = findViewById(R.id.simpanakhir);
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = Db_Contract.urlreminderUpdate;
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String status = jsonObject.getString("status");
                                    String message = jsonObject.getString("message");
                                    if (status.equals("1")) {
                                        Toast.makeText(getApplicationContext(), "Berhasil Disimpan", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(edit_notif_akhir.this, notifActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(edit_notif_akhir.this, "Terjadi kesalahan, coba lagi nanti", Toast.LENGTH_SHORT).show();

                    }
                }) {
                    protected Map<String, String> getParams() {

                        Map<String, String> paramV = new HashMap<>();
                        paramV.put("token", LoginActivity.token);
                        paramV.put("reminder_id", notifActivity.reminderIdEnd);
                        paramV.put("message", String.valueOf(etpesan.getHint()));
                        paramV.put("reminderDays", String.valueOf(etpengingat.getHint()).replaceAll("[^\\d.]", ""));
                        paramV.put("reminderTime", String.valueOf(etjam.getHint()));
                        paramV.put("is_on", is_on);
                        paramV.put("cycleEst_id", cycleEst_id);

                        return paramV;
                    }
                };
                queue.add(stringRequest);
            }
        });

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(edit_notif_akhir.this, notifActivity.class);
                startActivity(intent);
            }
        });

        btnpengingat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Panggil metode untuk menampilkan dialog NumberPicker
                showNumberPickerDialog();
            }
        });

        btnJam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                jam = calendar.get(Calendar.HOUR_OF_DAY);
                menit = calendar.get(Calendar.MINUTE);

                TimePickerDialog dialog;
                dialog = new TimePickerDialog(edit_notif_akhir.this, R.style.CustomTimePickerDialogStyle, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        jam = hourOfDay;
                        menit = minute;

                        if (jam <= 12) {
                            etjam.setHint(String.format(Locale.getDefault(), "%d:%d am", jam, menit));
                        } else {
                            etjam.setHint(String.format(Locale.getDefault(), "%d:%d pm", jam, menit));
                        }
                    }
                }, jam, menit, true);
                dialog.show();
            }
        });

        btnpesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();
            }
        });
    }

    private void showAlertDialog() {
        etpesan = findViewById(R.id.tvIsipesan);

        // Create a LayoutInflater to inflate the custom layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.pesan_dialog, null);

        // Find the EditText inside the custom layout
        final EditText customEditText = dialogView.findViewById(R.id.customEditText);

        // Create the AlertDialog with the custom layout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setTitle("Pesan notifikasi : ");

        // Set up the buttons
        alertDialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String message = customEditText.getText().toString();
                etpesan.setHint(message);
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Show the dialog
        alertDialogBuilder.show();
    }

    private void showNumberPickerDialog() {
        // Buat instance NumberPickerDialog dan tunjukkan
        numberpicker dialog = new numberpicker(this, 1, new numberpicker.OnValueChangeListener() {
            @Override
            public void onValueChanged(int value) {
                handleValueChanged(value);
            }
        });
        dialog.show();
    }

    private void handleValueChanged(int value) {
        etpengingat = findViewById(R.id.tvIsipengingat);

        // Konversi nilai integer ke string sebelum diatur sebagai hint
        String hintValue = String.valueOf(value);

        // Set hint pada EditText
        etpengingat.setHint(hintValue + " hari sebelumnya");
    }
}
