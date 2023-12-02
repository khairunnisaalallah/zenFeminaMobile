package com.example.myapplication1;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EducationFragment extends Fragment {

    private static final String TAG = "EducationFragment";

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<item> items;
    private List<item> originalItems;
    private androidx.appcompat.widget.SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_education, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        searchView = view.findViewById(R.id.searchView);

        // Initialize items list
        items = new ArrayList<>();
        originalItems = new ArrayList<>();

        // Set up RecyclerView
        setupRecyclerView(items);

        // Make network request using Volley
        fetchData();

        // Setup Search functionality
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handling search text change
                filter(newText);
                return true;
            }
        });

        return view;
    }

    private void setupRecyclerView(List<item> items) {
        adapter = new MyAdapter(getActivity(), items);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    // ...

    private void fetchData() {
        String url = Db_Contract.urlEdukasi;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Parse the JSON response
                            JSONObject jsonResponse = new JSONObject(response);

                            // Check if the response is an object and contains the "data" property
                            if (jsonResponse.has("data")) {
                                JSONArray jsonArray = jsonResponse.getJSONArray("data");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String image = jsonObject.getString("img");
                                    String judul = jsonObject.getString("title");
                                    String isi = jsonObject.getString("contents");


                                    // Log statement for image URL
                                    Log.d(TAG, "image URL" + image);

                                    // Convert image URL to an integer (use hash code as an example)

                                    // Add item to the list
                                    items.add(new item(   "https://zenfemina.com/assetsWeb/img/education/"+ image, judul, isi));
                                }

                                // Save original items
                                originalItems.addAll(items);

                                // Notify the adapter that data has changed
                                adapter.notifyDataSetChanged();

                            } else {
                                // Handle the case when "data" property is not present in the response
                                Log.e(TAG, "JSON Parsing Error: 'data' property not found in the response");
                                Toast.makeText(requireContext(),"image error", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Log error
                            Log.e(TAG, "JSON Parsing Error: " + e.getMessage());

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        error.printStackTrace();
                        // Log error
                        Log.e(TAG, "Volley Error: " + error.getMessage());

                        // Add error message to logcat
                        Log.e(TAG, "Volley Error: " + error.getMessage());

                        // You can show an error message to the user if needed
                         Toast.makeText(requireContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the RequestQueue
        Volley.newRequestQueue(requireContext()).add(stringRequest);
    }

// ...


    private void filter(String text) {
        List<item> filteredList = new ArrayList<>();

        for (item item : originalItems) {
            if (item.getJudul().toLowerCase().contains(text) ||
                    item.getIsi().toLowerCase().contains(text)) {
                filteredList.add(item);
            }
        }

        // Filter data in the adapter and update the RecyclerView
        adapter.filterList(filteredList);
    }
}
