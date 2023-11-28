package com.example.myapplication1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
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

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<item> items;
    private List<item> originalItems; // Menyimpan data asli sebelum pencarian
    private androidx.appcompat.widget.SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_education, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        searchView = view.findViewById(R.id.searchView);
        EditText searchEditText = searchView.findViewById(R.id.searchEditText);

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
        adapter = new MyAdapter(getActivity().getApplicationContext(), items);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    private void fetchData() {
        String url = Db_Contract.urlEdukasi;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Parse the JSON response
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String title = jsonObject.getString("title"); // replace with actual field name
                                String content = jsonObject.getString("content"); // replace with actual field name
                                // Assuming there's an image URL in your JSON response
                                // String imageUrl = jsonObject.getString("imageUrl");

                                // Add item to the list
                                items.add(new item(title, content, R.drawable.edu));
                            }

                            // Save original items
                            originalItems.addAll(items);

                            // Notify the adapter that data has changed
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        error.printStackTrace();
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
