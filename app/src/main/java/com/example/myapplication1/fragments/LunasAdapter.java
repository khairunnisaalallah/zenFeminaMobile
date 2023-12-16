package com.example.myapplication1.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication1.R;

import java.util.List;

public class LunasAdapter extends RecyclerView.Adapter<lunas_viewHolder> {

    private List<item_lunas> itemList;
    private Context context;

    public LunasAdapter(Context context, List<item_lunas> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    public void setItems(List<item_lunas> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged(); // You may want to notify the adapter that the data has changed
    }

    @NonNull
    @Override
    public lunas_viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lunas, parent, false);
        return new lunas_viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull lunas_viewHolder holder, int position) {
        item_lunas currentItem = itemList.get(position);

        holder.title.setText(currentItem.getTitle());
        holder.date.setText(currentItem.getDate());
        holder.done.setImageResource(R.drawable.elipsqadha2); // Ganti dengan sumber gambar yang sesuai
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}

