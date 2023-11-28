package com.example.myapplication1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> implements Filterable {

    private Context context;
    private List<item> items;
    private List<item> itemsFiltered;

    public MyAdapter(Context context, List<item> items) {
        this.context = context;
        this.items = items;
        this.itemsFiltered = new ArrayList<>(items);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.judulView.setText(itemsFiltered.get(position).getJudul());
        holder.isiView.setText(itemsFiltered.get(position).getIsi());
        holder.imageView.setImageResource(itemsFiltered.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return itemsFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String searchText = charSequence.toString().toLowerCase().trim();

                List<item> tempList = new ArrayList<>();

                if (searchText.isEmpty()) {
                    tempList.addAll(items);
                } else {
                    for (item item : items) {
                        if (item.getJudul().toLowerCase().contains(searchText) ||
                                item.getIsi().toLowerCase().contains(searchText)) {
                            tempList.add(item);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = tempList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                itemsFiltered.clear();
                itemsFiltered.addAll((List<item>) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }

    // Metode ini digunakan untuk mengatur data yang telah difilter
    public void filterList(List<item> filteredList) {
        itemsFiltered = filteredList;
        notifyDataSetChanged();
    }
}
