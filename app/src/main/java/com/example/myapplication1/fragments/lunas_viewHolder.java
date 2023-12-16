package com.example.myapplication1.fragments;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication1.R;

public class lunas_viewHolder extends RecyclerView.ViewHolder {

    TextView title;
    TextView date;
    ImageView done;

    public lunas_viewHolder(@NonNull View itemView) {
        super(itemView);


        title =  itemView.findViewById(R.id.textViewJudul);
        date =  itemView.findViewById(R.id.textViewTanggal);
        done =  itemView.findViewById(R.id.lunas);

    }
}
