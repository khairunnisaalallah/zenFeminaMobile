package com.example.myapplication1;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

// ... (Your imports)

public class HomeFragment extends Fragment {

    private TextView firstDateTextView;
    private TextView lastDateTextView;

    SharedPreferences sharedPreferences;

    public static String cycleHistory_id;
    public String StartDateEst, StartDateHist, EndDateEst, EndDateHist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tokenManager token = new tokenManager(getContext());

        //get history and get Est
        getHistory();
        getEst();


        // Mengambil referensi ke ImageButton
        RelativeLayout imageButtonCalendar = view.findViewById(R.id.imageButtoncalender);
        RelativeLayout imageButtonQadha = view.findViewById(R.id.btnhutangsholat);
        RelativeLayout imageButtonAwali = view.findViewById(R.id.imageButtonAwali);
        RelativeLayout imageButtonNotif = view.findViewById(R.id.imageButtonNotifikasi);

        imageButtonNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), notifActivity.class);
                startActivity(intent);
            }
        });

        imageButtonAwali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAwaliConfirmationDialog();
            }
        });

        // Set OnClickListener for the calendar button
        imageButtonCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the CalendarActivity
                Intent intent = new Intent(requireActivity(), activity_calender.class);
                startActivity(intent);
            }
        });



        imageButtonQadha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the CalendarActivity
                Intent intent = new Intent(requireActivity(),MainActivityHutang.class);
                startActivity(intent);
            }
        });

       return view;
    }



    private void showAwaliConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("Apakah kamu ingin mengawali menstruasimu?")
                .setPositiveButton("iya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Handle 'Yes' button click
                        // Add your logic for handling 'Yes' here
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Handle 'No' button click
                        // Add your logic for handling 'No' here
                        dialog.dismiss();
                    }
                });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private String convertDateFormat(String inputDate) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("d MMM yyyy", Locale.getDefault());
            Date date = inputFormat.parse(inputDate);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return inputDate; // Jika gagal, kembalikan tanggal tanpa perubahan
        }
    }

    private void getHistory(){
        tokenManager token = new tokenManager(getContext());

        // URL endpoint API get_profile() dengan parameter token yang sesuai
        String url = Db_Contract.getUrlHistory + "?token=" + token.getToken();

        // Buat request GET menggunakan Volley
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle response dari server jika permintaan sukses
                        try {
                            String status = response.getString("status");
                            String message = response.getString("message");

                            if(status.equals("1")){
                                JSONArray dataArray = response.getJSONArray("data");
                                JSONObject data = dataArray.getJSONObject(0);

                                StartDateHist = data.getString("start_date");
                                EndDateHist = data.getString("end_date");
                            }else {
                                throw new JSONException(message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Tambahkan request ke queue
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonObjectRequest);
    }

    private void getEst(){
        tokenManager token = new tokenManager(getContext());

        // URL endpoint API get_profile() dengan parameter token yang sesuai
        String url = Db_Contract.urlgetEst + "?token=" + token.getToken();

        // Buat request GET menggunakan Volley
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle response dari server jika permintaan sukses
                        try {
                            String status = response.getString("status");
                            String message = response.getString("message");

                            if(status.equals("1")){
                                JSONArray dataArray = response.getJSONArray("data");
                                JSONObject data = dataArray.getJSONObject(0);

                                StartDateEst = data.getString("start_date");
                                EndDateEst = data.getString("end_date");
                            }else {
                                throw new JSONException(message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Tambahkan request ke queue
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonObjectRequest);
    }




}