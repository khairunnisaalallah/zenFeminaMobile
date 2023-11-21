package com.example.myapplication1;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Mengambil referensi ke ImageButton
        ImageButton imageButtonCalendar = view.findViewById(R.id.imageButtoncalender);

        // Menambahkan listener untuk ImageButton
        imageButtonCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCalendarActivity();
            }
        });

        return view;
    }

    // Metode untuk membuka activity_calender
    private void openCalendarActivity() {
        Intent intent = new Intent(getActivity(), activity_calender.class);
        startActivity(intent);
    }
}
