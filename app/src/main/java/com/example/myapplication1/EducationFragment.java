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

        items = new ArrayList<>();
        for (int i = 0; i <= 10; i++) {
            items.add(new item("Ketentuan Haid Dalam Islam", "isiiiya", R.drawable.edu));
        }

        // Menyimpan data asli
        originalItems = new ArrayList<>(items);

        setupRecyclerView(items);

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

    private void filter(String text) {
        List<item> filteredList = new ArrayList<>();

        for (item item : originalItems) {
            if (item.getJudul().toLowerCase().contains(text) ||
                    item.getIsi().toLowerCase().contains(text)) {
                filteredList.add(item);
            }
        }

        // Memfilter data di dalam adapter dan memperbarui tampilan RecyclerView
        adapter.filterList(filteredList);
    }
}
