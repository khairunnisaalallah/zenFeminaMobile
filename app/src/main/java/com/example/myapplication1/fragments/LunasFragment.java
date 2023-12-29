package com.example.myapplication1.fragments;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication1.Db_Contract;
import com.example.myapplication1.HomeFragment;
import com.example.myapplication1.LoginActivity;
import com.example.myapplication1.R;
import com.example.myapplication1.tokenManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LunasFragment extends Fragment {

    private LunasAdapter lunasAdapter;
    private RecyclerView recyclerView;
    private List<item_lunas> items;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lunas, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewQadha2);
        items = new ArrayList<>();

        // Initialize adapter
        lunasAdapter= new LunasAdapter(requireContext(), items);
        recyclerView.setAdapter(lunasAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Fetch data from API
        fetchData();

        return view;
    }

    private void fetchData() {
        tokenManager token = new tokenManager(getContext());
        String url = Db_Contract.urlprayerdone + "?cycleHistory_id=" + HomeFragment.cycleHistory_id + "&token=" + token.getToken();
        Log.d(TAG, "URL API: " + url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Respons API: " + response); // Tambahkan pernyataan log ini

                        // Tambahkan penanganan kesalahan di sini
                        if (response.trim().startsWith("<")) {
                            // Respons berupa HTML, bukan JSON
                            Log.e(TAG, "Kesalahan: Respons HTML diterima dari server");
                            Toast.makeText(requireContext(), "Kesalahan respons dari server", Toast.LENGTH_SHORT).show();

                            // Jangan lanjutkan dengan parsing JSON karena respons tidak sesuai
                            return;
                        }

                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String message = jsonResponse.getString("message");

                            if (jsonResponse.has("data")) {
                                JSONArray jsonArray = jsonResponse.getJSONArray("data");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String id_prayer = jsonObject.getString("changePrayer_id");
                                    String title = "Sholat " +  jsonObject.getString("prayer");
                                    String date = jsonObject.getString("end_date");

                                    items.add(new item_lunas(id_prayer, title, date.substring(0, 10)));
                                }

                                // Set data to the adapter
                                lunasAdapter.setItems(items);

                            } else {
                                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, "Kesalahan Parsing JSON: " + e.getMessage());
                            Toast.makeText(requireContext(), "Kesalahan parsing data JSON", Toast.LENGTH_SHORT).show();
                        }
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.e(TAG, "Kesalahan Volley: " + error.getMessage());
                        Toast.makeText(requireContext(), "Kesalahan mengambil data", Toast.LENGTH_SHORT).show();
                    }
                });

        Volley.newRequestQueue(requireContext()).add(stringRequest);
    }
}



