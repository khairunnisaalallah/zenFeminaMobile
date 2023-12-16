package com.example.myapplication1.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication1.R;

import java.util.List;

public class QadhaAdapter extends RecyclerView.Adapter<view_holder> {

    private List<item_qadha> itemList;
    private Context context;

    public QadhaAdapter(Context context, List<item_qadha> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    public void setItems(List<item_qadha> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged(); // You may want to notify the adapter that the data has changed
    }

    @NonNull
    @Override
    public view_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate layout for each list item
        View view = inflater.inflate(R.layout.itemqadha, parent, false);
        return new view_holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull view_holder holder, int position) {

            item_qadha currentItem = itemList.get(position);

            holder.judul.setText(currentItem.getJudul());
            holder.tanggal.setHint(currentItem.getTanggal());

            holder.lingkaran.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showlunasConfirmationDialog();
                }
            });
        }


        private void showlunasConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Kamu udah bayar hutang shalat?")
                .setPositiveButton("iya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Handle 'Yes' button click
                        // Add your logic for handling 'Yes' here
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Handle 'No' button click
                        // Add your logic for handling 'No' here
                        dialog.dismiss();
                    }
                });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
