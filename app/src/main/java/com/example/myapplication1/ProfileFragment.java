package com.example.myapplication1;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {
    Button moreButton, buttonLogout;
    TextView tvUsername, tvEmail;
    View rootView;
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        moreButton = rootView.findViewById(R.id.bmore);
        buttonLogout = rootView.findViewById(R.id.logout);
        tvEmail = rootView.findViewById(R.id.email);
        tvUsername = rootView.findViewById(R.id.username);

        sharedPreferences =requireActivity().getSharedPreferences("MyAppName", MODE_PRIVATE);
        if (sharedPreferences.getString("logged", "false").equals("false")) {
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
        tvUsername.setText(sharedPreferences.getString("username",""));
        tvEmail.setText(sharedPreferences.getString("email",""));
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestQueue queue = Volley.newRequestQueue(requireContext());
                String url = Db_Contract.urlLogout;

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.equals("Sukses")) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("logged", "");
                                    editor.putString("username", "");
                                    editor.putString("email", "");
                                    editor.putString("session_id", "");
                                    editor.apply();
                                    Intent intent = new Intent(requireContext(), LoginActivity.class);
                                    startActivity(intent);
                                    requireActivity().finish();
                                } else {
                                    Toast.makeText(requireContext(), response, Toast.LENGTH_SHORT).show();
                                }
                            }
                        },new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse (VolleyError error){
                        error.printStackTrace();
                    }
                }){
                    protected Map<String, String> getParams () {
                        Map<String, String> paramV = new HashMap<>();
                        paramV.put("email", sharedPreferences.getString("email", ""));
                        paramV.put("session_id", sharedPreferences.getString("session_id", ""));
                        return paramV;
                    }
                };
                queue.add(stringRequest);
            }
        });
        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to ProfileActivity
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }
}

