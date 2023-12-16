package com.example.myapplication1.fragments;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication1.R;

public class view_holder extends RecyclerView.ViewHolder {

    TextView judul;
    TextView tanggal;
    ImageButton lingkaran;
    public view_holder(@NonNull View itemView) {
        super(itemView);

        judul =  itemView.findViewById(R.id.textViewTitle);
        tanggal = itemView.findViewById(R.id.textViewDate);
        lingkaran = itemView.findViewById(R.id.checkBox);
    }
}

