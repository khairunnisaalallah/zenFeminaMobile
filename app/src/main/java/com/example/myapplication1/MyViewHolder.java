package com.example.myapplication1;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView judulView;
    TextView isiView;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageviewItem);
        judulView = itemView.findViewById(R.id.judul);
        isiView = itemView.findViewById(R.id.isi);
    }
}
