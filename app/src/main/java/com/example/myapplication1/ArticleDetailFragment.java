package com.example.myapplication1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;

public class ArticleDetailFragment extends Fragment {

    public static ArticleDetailFragment newInstance(String image, String judul, String isi) {
        ArticleDetailFragment fragment = new ArticleDetailFragment();
        Bundle args = new Bundle();
        args.putString("Image", image);
        args.putString("Judul", judul);
        args.putString("Isi", isi);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_articel_detail, container, false);

        // Dapatkan argumen
        String image = getArguments().getString("Image");
        String judul = getArguments().getString("Judul");
        String isi = getArguments().getString("Isi");

        // Tampilkan artikel penuh di fragment
        ImageView imageView = view.findViewById(R.id.imageView);
        Glide.with(this).load(image).into(imageView);

        TextView judulTextView = view.findViewById(R.id.judulTextView);
        judulTextView.setText(judul);

        TextView isiTextView = view.findViewById(R.id.isiTextView);
        isiTextView.setText(isi);

        return view;
    }
}
