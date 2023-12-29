package com.example.myapplication1.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication1.ArticleDetailFragment;
import com.example.myapplication1.Db_Contract;
import com.example.myapplication1.LoginActivity;
import com.example.myapplication1.R;
import com.example.myapplication1.tokenManager;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        View view = inflater.inflate(R.layout.itemqadha, parent, false);
        return new view_holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull view_holder holder, int position) {
        item_qadha currentItem = itemList.get(position);

        holder.judul.setText(currentItem.getJudul());
        holder.tanggal.setText(currentItem.getTanggal());

        holder.lingkaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showlunasConfirmationDialog(position);
            }
        });
    }

    private void showlunasConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Kamu udah bayar hutang shalat?")
                .setPositiveButton("iya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        onConfirmation(position);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void onConfirmation(int position) {
        tokenManager token = new tokenManager(context.getApplicationContext());
        sendPostRequest(itemList.get(position).getPrayerId(), token.getToken());
        itemList.remove(position);
        notifyDataSetChanged();
    }

    private void sendPostRequest(String prayerId, String token) {

        String url = Db_Contract.urlupdatePrayer;

        JSONObject postData = new JSONObject();
        try {
            postData.put("changePrayer_id", prayerId);
            postData.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Tindakan yang perlu dilakukan jika permintaan berhasil

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equals("1")) {
                                Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            } else {
                                throw new JSONException(message);
                            }
                        } catch (JSONException e) {
                            Toast.makeText(context.getApplicationContext().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Tindakan yang perlu dilakukan jika terjadi kesalahan
                        Toast.makeText(context.getApplicationContext(), "Error sending ID to server", Toast.LENGTH_SHORT).show();
                    }
                }) {
            // Override method ini untuk menambahkan parameter ke body permintaan
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                params.put("changePrayer_id", prayerId);
                return params;
            }
        };

        // Tambahkan permintaan ke antrian permintaan Volley
        Volley.newRequestQueue(context.getApplicationContext()).add(stringRequest);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
