package com.example.myapplication1;

import static android.content.Context.MODE_PRIVATE;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {
    Button buttonLogout;
    RelativeLayout editProfil, editPass;
    TextView tvname, tvcycle, tvperiod;
    View rootView;
    SharedPreferences sharedPreferences;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        editProfil = rootView.findViewById(R.id.cvubahprofil);
        editPass = rootView.findViewById(R.id.cvubahpass);
        buttonLogout = rootView.findViewById(R.id.logout);
        builder = new AlertDialog.Builder(requireContext());
        tvname = rootView.findViewById(R.id.name);
        tvcycle = rootView.findViewById(R.id.cycleDays);
        tvperiod = rootView.findViewById(R.id.periodDays);

        sharedPreferences = requireActivity().getSharedPreferences("MyAppName", MODE_PRIVATE);
        RequestQueue queue = Volley.newRequestQueue(requireActivity());
        String url = Db_Contract.urlProfile + "?token=" + LoginActivity.token;
        StringRequest profileRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Di sini Anda bisa memproses response dari API
                // Misalnya, parsing data JSON dan menampilkan di TextView
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    // Misalnya, jika Anda ingin mengambil nilai dari elemen pertama di dalam array:
                    JSONObject dataObject = jsonArray.getJSONObject(0);
                    String name = dataObject.getString("name"); // Menggunakan optString untuk mendapatkan string kosong jika nilai "name" null
                    String username = dataObject.getString("username");
                    String email = dataObject.getString("email");

                    if (!name.isEmpty() && !username.isEmpty() && !email.isEmpty()) {
                        tvname.setText(name);
                        //LIAT DI SINI
                        // Setelah menampilkan data profil, lakukan permintaan ke getEstimation di sini
                        String urlEstimation = Db_Contract.urlgetEst + "?token=" + LoginActivity.token;
                        StringRequest estimationRequest = new StringRequest(Request.Method.GET, urlEstimation, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("EstimationResponse", response);
                                try {
                                    JSONObject jsonObjectEstimation = new JSONObject(response);
                                    int status = jsonObjectEstimation.getInt("status");
                                    //boolean success = jsonObjectEstimation.getBoolean("success");

                                    if (status == 1) { // Ganti dari success ke status
                                        JSONArray dataArray = jsonObjectEstimation.getJSONArray("data");
                                        JSONObject dataObjectEstimation = dataArray.getJSONObject(0);

                                        // Ambil data siklus dan periode dari response
                                        String cycleLength = dataObjectEstimation.optString("cycle_length");
                                        String periodLength = dataObjectEstimation.optString("period_length");

                                        // Tampilkan data siklus dan periode di TextView yang relevan
                                        tvcycle.setHint(cycleLength + " Hari");
                                        tvperiod.setHint(periodLength + " Hari");
                                    } else {
                                        // Tampilkan pesan kesalahan jika ada masalah dalam mendapatkan data
                                       // String message = jsonObjectEstimation.getString("message");
                                       // Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getContext(), "Error parsing JSON", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                                Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                        // Menambahkan permintaan estimasi ke dalam antrian
                        RequestQueue estimationQueue = Volley.newRequestQueue(requireActivity());
                        estimationQueue.add(estimationRequest);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> paramV = new HashMap<>();
                String token = LoginActivity.token;
                paramV.put("token", token);
                return paramV;
            }
        };
        RequestQueue profileQueue = Volley.newRequestQueue(requireActivity());
        profileQueue.add(profileRequest);

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // buat notif logout
                //setting message manualy and perform action on button click
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setMessage("Kamu ingin keluar?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                performLogout();
                                Toast.makeText(requireContext(), "Anda berhasil keluar", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                //creating dialog box
                alertDialog = builder.create();
                //setting the title manualy
                alertDialog.setTitle("Perhatian!");
                alertDialog.show();
            }});
        editProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to ProfileActivity
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
            }
        });


        editPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), activity_profilepassword.class);
                startActivity(intent);
            }
        });

        return rootView;
    }
    private void performLogout() {

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = Db_Contract.urlLogout;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    int status = jsonResponse.getInt("status");
                    if (status == 1) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear(); // Bersihkan semua data yang disimpan
                        editor.apply();
                        if (alertDialog != null && alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }

                        Intent intent = new Intent(requireContext(), WelcomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);


                    } else {
                        // Tampilkan pesan kesalahan jika logout gagal
                        String message = jsonResponse.getString("message");
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(requireContext(), "Error parsing JSON", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(requireContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> paramV = new HashMap<>();
                paramV.put("token", LoginActivity.token);
                return paramV;
            }
        };
        queue.add(stringRequest);
    }
}