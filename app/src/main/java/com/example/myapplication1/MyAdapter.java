package com.example.myapplication1;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> implements Filterable {

    private Context context;
    private List<item> items;
    private List<item> itemsFiltered;

    private List<item> originalItems;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(item clickedItem);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public MyAdapter(Context context, List<item> items) {
        this.context = context;
        this.items = items;
        this.itemsFiltered = new ArrayList<>(items);
        this.originalItems = new ArrayList<>(items); // Tambahkan ini
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

        // Load image using Glide with error handling
        Glide.with(context)
                .load(itemsFiltered.get(position).getImage())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e("Glide", "Load failed", e);
                        // You can handle the error here, for example, set a placeholder image
                        holder.imageView.setImageResource(R.drawable.profile); // Replace "placeholder" with your placeholder image
                        return true; // Indicate that the error is handled
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        // You can perform additional actions when the resource is ready if needed
                        return false;
                    }
                })
                .into(holder.imageView);

        // Set click listener for each item
        holder.itemView.setOnClickListener(view -> {
            int clickedPosition = holder.getAdapterPosition();
            if (mListener != null && clickedPosition != RecyclerView.NO_POSITION) {
                mListener.onItemClick(itemsFiltered.get(clickedPosition));
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemsFiltered.size();
    }

    // ...

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String searchText = charSequence.toString().toLowerCase().trim();

                List<item> tempList = new ArrayList<>();

                if (searchText.isEmpty()) {
                    tempList.addAll(originalItems);
                } else {
                    for (item item : originalItems) {
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

// ...

    // This method is used to set the filtered data
    public void filterList(List<item> filteredList) {
        itemsFiltered = filteredList;
        notifyDataSetChanged();
    }
}
