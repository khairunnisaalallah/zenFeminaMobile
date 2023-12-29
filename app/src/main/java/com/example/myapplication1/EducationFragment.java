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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EducationFragment extends Fragment implements MyAdapter.OnItemClickListener{

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

    @Override
    public void onItemClick(item clickedItem) {
        // Handle item click here, e.g., navigate to the article detail fragment
        String id = clickedItem.getId();
        String image = clickedItem.getImage();
        String judul = clickedItem.getJudul();
        String isi = clickedItem.getIsi();

        Log.d("MyAdapter", "Item clicked - Judul: " + clickedItem.getJudul());
        Log.d("MyAdapter", "Item clicked - Isi: " + clickedItem.getIsi());
        Log.d("MyAdapter", "Item clicked - Image: " + clickedItem.getImage());

        // Send ID to server
        String url = Db_Contract.urladdOnClick;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Tindakan yang perlu dilakukan jika permintaan berhasil
                        Log.d(TAG, "ID sent to server successfully");

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equals("1")) {
                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                Fragment detailFragment = ArticleDetailFragment.newInstance(id,image, judul, isi);
                                requireActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.container, detailFragment)
                                        .addToBackStack(null)
                                        .commit();
                            } else {
                                throw new JSONException(message);
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getContext().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Tindakan yang perlu dilakukan jika terjadi kesalahan
                        Log.e(TAG, "Volley Error: " + error.getMessage());
                        Toast.makeText(requireContext(), "Error sending ID to server", Toast.LENGTH_SHORT).show();
                    }
                }) {
            // Override method ini untuk menambahkan parameter ke body permintaan
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", id); // Mengirim ID sebagai parameter
                return params;
            }
        };

        // Tambahkan permintaan ke antrian permintaan Volley
        Volley.newRequestQueue(requireContext()).add(stringRequest);
    }



    private void setupRecyclerView(List<item> items) {
        adapter = new MyAdapter(getActivity(), items);
        adapter.setOnItemClickListener(this);
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
                                    String id = jsonObject.getString("education_id");
                                    String image = jsonObject.getString("img");
                                    String judul = jsonObject.getString("title");
                                    String isi = jsonObject.getString("contents");


                                    // Log statement for image URL
                                    Log.d(TAG, "image URL" + image);

                                    // Convert image URL to an integer (use hash code as an example)

                                    // Add item to the list
                                    items.add(new item(id, "https://zenfemina.com/assetsWeb/img/education/"+ image, judul, isi));
                                }

                                // Save original items
                                originalItems.clear();
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
