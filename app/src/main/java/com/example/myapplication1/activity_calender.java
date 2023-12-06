package com.example.myapplication1;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class activity_calender extends AppCompatActivity {

    private TextView monthTitle;
    private CalendarView calendarView;
    private CheckBox checkBox;
    private RadioGroup prayerCheckBoxGroup;

    private String cycleHistoryId;

    ArrayList<item> items = new ArrayList<item>();

    RequestQueue queue;





    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        // Inisialisasi komponen UI
        monthTitle = findViewById(R.id.judul);
        calendarView = findViewById(R.id.calendarView);
        checkBox = findViewById(R.id.checkBox);
        prayerCheckBoxGroup = findViewById(R.id.prayerCheckBoxGroup);

        // Atur listener untuk perubahan tanggal di CalendarView
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                // Tambahkan logika yang sesuai dengan perubahan tanggal yang dipilih
                // Misalnya, Anda dapat memperbarui teks di TextView monthTitle
                monthTitle.setText("Selected Date: " + dayOfMonth + "/" + (month + 1) + "/" + year);
            }
        });


        ImageButton backButton = findViewById(R.id.imageButtonbackkal);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tambahkan logika sesuai dengan kebutuhan Anda
                // Contoh: Kembali ke HomeFragment jika ada back stack
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                } else {
                    // Jika tidak ada fragment di dalam back stack, panggil onBackPressed biasa
                    onBackPressed();
                }
            }
        });



        queue = Volley.newRequestQueue(this);

        // Ambil data dari API
        getDataFromApi();
    }

    private void getDataFromApi() {


                        Log.d("CycleHistoryId", cycleHistoryId);

                        // Gantilah dengan URL API yang sesuai
                        String apiUrl = Db_Contract.urlqadha +"?cycleHistory_id"+HomeFragment.history_id +"&token=" + LoginActivity.token;

                        StringRequest request = new StringRequest(Request.Method.GET, apiUrl, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    Log.d("Raw Respon", response);

                                    if (!response.trim().isEmpty()) {
                                        if (response.startsWith("{")) {
                                            JSONObject jsonResponse = new JSONObject(response);

                                            // Log the entire JSON response from the second request
                                            Log.d("JSON Response (Qadha)", jsonResponse.toString());

                                            int status = jsonResponse.getInt("status");
                                            String message = jsonResponse.getString("message");

                                            if (status == 1) {
                                                if (jsonResponse.has("data")) {
                                                    JSONArray dataArray = jsonResponse.getJSONArray("data");

                                                    prayerCheckBoxGroup.removeAllViews();

                                                    for (int i = 0; i < dataArray.length(); i++) {
                                                        JSONObject dataObject = dataArray.getJSONObject(i);

                                                        // Perhatikan perubahan pada bagian ini
                                                        String nilai = dataObject.getString("cycleHistory_id");
                                                        String prayer = dataObject.getString("prayer");
                                                        String changePrayerId = dataObject.getString("changePrayer_id");

                                                        Log.d("Data", "Prayer: " + prayer + ", ChangePrayerID: " + changePrayerId);



                                                        CheckBox dynamicCheckBox = new CheckBox(activity_calender.this);
                                                        dynamicCheckBox.setText(prayer);
                                                        prayerCheckBoxGroup.addView(dynamicCheckBox);



                                                    }
                                                } else {
                                                    Toast.makeText(activity_calender.this, "Invalid JSON response: Missing 'data' property", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(activity_calender.this, "API response status: " + status + ", Message: " + message, Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(activity_calender.this, "Invalid JSON response: Response does not start with '{'", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(activity_calender.this, "Empty response from the API", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                    Toast.makeText(activity_calender.this, "Error parsing JSON (Qadha): " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                                Toast.makeText(activity_calender.this, "Error in API request (Qadha): " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }) {

                            @Override
                            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                                try {
                                    String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                                    return Response.success(jsonString, HttpHeaderParser.parseCacheHeaders(response));
                                } catch (UnsupportedEncodingException e) {
                                    return Response.error(new ParseError(e));
                                }
                            }

                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<>();
                                params.put("token", LoginActivity.token);
                                params.put("cycleHistory_id", cycleHistoryId);
                                return params;
                            }
                        };

                        queue.add(request);
                    } {



    }
}