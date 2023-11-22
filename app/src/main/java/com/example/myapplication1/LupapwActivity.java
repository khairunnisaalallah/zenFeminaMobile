package com.example.myapplication1;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;


public class LupapwActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupapw);

        // Impor ImageButton
        ImageButton backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kembali ke ProfileFragment
                onBackPressed();
            }
        });
        Intent intent = getIntent();
        String email = intent.getStringExtra("Email");

        editTextNewPassword = findViewById(R.id.newPassInputFP);
        editTextConfirmNewPassword = findViewById(R.id.confirmNewPassInputFP);
        btnReset = findViewById(R.id.btnResetPassword);
        txtLogin = findViewById(R.id.txtLogin); // Ganti dengan ID yang sesuai

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = editTextNewPassword.getText().toString();
                String confirmPassword = editTextConfirmNewPassword.getText().toString();

                if (!password.isEmpty() && !confirmPassword.isEmpty()) {
                    // ... (Bagian validasi password)

                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    String url = Db_User.urlNewPassword;

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if ("success".equals(response)) {
                                        Intent intent = new Intent(FP_3_resetpass.this, FP_4_status.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    error.printStackTrace();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> paramV = new HashMap<>();
                            paramV.put("Email", email);
                            paramV.put("newPassword", password);
                            return paramV;
                        }
                    };
                    queue.add(stringRequest);
                }
            }
        });

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FP_3_resetpass.this, SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }
}

    }
}