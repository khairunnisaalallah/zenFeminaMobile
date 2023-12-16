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
import android.widget.TextView;
import android.widget.Toast;

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
    private TextView hutangShalat;

    SharedPreferences sharedPreferences;

    public static String cycleHistory_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Mengambil referensi ke ImageButton
        ImageButton imageButtonCalendar = view.findViewById(R.id.imageButtoncalender);
        ImageButton imageButtonQadha = view.findViewById(R.id.buttonhaid);
        ImageButton imageButtonAwali = view.findViewById(R.id.imageButtonAwali);
        TextView hutangShalat = view.findViewById(R.id.hutangshalat);

        ImageButton imageButtonNotif = view.findViewById(R.id.imageButtonNotifikasi);

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



        hutangShalat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the CalendarActivity
                Intent intent = new Intent(requireActivity(),MainActivityHutang.class);
                startActivity(intent);
            }
        });




        // Mengambil referensi ke TextView
        firstDateTextView = view.findViewById(R.id.firstdate2);
        lastDateTextView = view.findViewById(R.id.lastdate2);
        sharedPreferences = requireActivity().getSharedPreferences("MyAppName", MODE_PRIVATE);
        RequestQueue queue = Volley.newRequestQueue(requireActivity());
        String url = Db_Contract.urlgetEst + "?token=" + LoginActivity.token;
        StringRequest profileRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (jsonArray.length() > 0) {
                        JSONObject dataObject = jsonArray.getJSONObject(0);
                        String startdate = dataObject.optString("start_date", ""); // Menggunakan optString untuk mendapatkan string kosong jika nilai "start_date" null
                        String startdatenew = convertDateFormat(startdate.substring(0,10));

                        String enddate = dataObject.optString("end_date", "");

                        if (!startdate.isEmpty() ) {
                            lastDateTextView.setText(startdatenew);

                            // Setelah menampilkan data profil, lakukan permintaan ke getEstimation di sini
                            try {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                                Date startDate = sdf.parse(startdate);
                                Date endDate = sdf.parse(enddate);
                                Date currentDate = new Date();


                                long diffInMillisBegin = startDate.getTime() - currentDate.getTime();
                                long beginCycle = diffInMillisBegin / (24 * 60 * 60 * 1000);

                                long diffInMillisEnd = endDate.getTime() - currentDate.getTime();
                                long endCycle = diffInMillisEnd / (24 * 60 * 60 * 1000);

                                String days = null;
                                String nextWords = null;
                                String cycleWords = null;
                                if(startDate.after(currentDate)){
                                    days = beginCycle + " Hari Lagi";
                                    nextWords = "menuju periode Haid selanjutnya";
                                    cycleWords = "Siklus selanjutnya";
                                } else {
                                    days = endCycle + " Hari lagi";
                                    nextWords = "Kemungkinan periode Haid berakhir";
                                    cycleWords = "Siklus saat ini";
                                }

                                TextView hariTextView = view.findViewById(R.id.hari);
                                TextView nextTextView = view.findViewById(R.id.menujusiklus);
                                TextView lastDateWords = view.findViewById(R.id.lastdateWords);
                                hariTextView.setText(days);
                                nextTextView.setText(nextWords);
                                lastDateWords.setText(cycleWords);

                            } catch (ParseException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), "Error parsing date", Toast.LENGTH_SHORT).show();
                            }

                            // Setelah menampilkan data profil, lakukan permintaan ke getEstimation di sini
                            String urlEstimation = Db_Contract.urlgetEst + "?token=" + LoginActivity.token;
                            StringRequest estimationRequest = new StringRequest(Request.Method.GET, urlEstimation, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String responseEstimation) {
                                    try {
                                        JSONObject jsonObjectEstimation = new JSONObject(responseEstimation);

                                        // Proses data estimasi di sini
                                        // ...

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(getContext(), "Error parsing Estimation JSON", Toast.LENGTH_SHORT).show();
                                    }
                                }


                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    error.printStackTrace();
                                    Toast.makeText(getContext(), "Error in Estimation request: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                            // Menambahkan permintaan estimasi ke dalam antrian
                            RequestQueue estimationQueue = Volley.newRequestQueue(requireActivity());
                            estimationQueue.add(estimationRequest);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error parsing Profile JSON", Toast.LENGTH_SHORT).show();
                }


                String historyUrl = Db_Contract.getUrlHistory + "?token=" + LoginActivity.token;
                StringRequest historyRequest = new StringRequest(Request.Method.GET, historyUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responseHistory) {
                        try {
                            JSONObject jsonObject = new JSONObject(responseHistory);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            if (jsonArray.length() > 0) {
                                JSONObject dataObject = jsonArray.getJSONObject(0);
                                String enddate = dataObject.optString("end_date", "");

                                String newenddate = convertDateFormat(enddate.substring(0, 10));

                                cycleHistory_id = dataObject.getString("cycleHistory_id");
                                firstDateTextView.setText(newenddate);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error parsing History JSON", Toast.LENGTH_SHORT).show();
                        }
                    } //done
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getContext(), "Error in History request: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                queue.add(historyRequest);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getContext(), "Error in Profile request: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> paramV = new HashMap<>();
                String token = LoginActivity.token;
                paramV.put("token", token);
                return paramV;
            }
        };

        // Menambahkan permintaan profil ke dalam antrian
        queue.add(profileRequest);

        // Return the inflated view
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




}