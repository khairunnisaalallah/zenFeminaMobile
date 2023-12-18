package com.example.myapplication1;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    private EditText editTextName, editTextPhone, editTextUsername, editTextEmail, editTextBirthdate;
    private String name, phone, username, email, birthDate, password;
    Button buttonsave;
    Bitmap bitmap;
    private Uri selectedImageUri;
    FloatingActionButton buttoncam;
    ImageView imageView;
    private ImageButton datePicker;
    SharedPreferences sharedPreferences;
    private Calendar calendar;
    private String errorMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        // Inisialisasi EditText di dalam metode onCreate
        imageView = findViewById(R.id.profile);
        buttoncam = findViewById(R.id.floatcam);
        editTextName = findViewById(R.id.name);
        editTextEmail = findViewById(R.id.email);
        editTextPhone = findViewById(R.id.phone);
        editTextUsername = findViewById(R.id.username);
        editTextBirthdate = findViewById(R.id.birthDate);
        buttonsave = findViewById(R.id.simpanp);
        ImageView imageView = findViewById(R.id.profile);

        sharedPreferences = this.getSharedPreferences("MyAppName", MODE_PRIVATE);

        ActivityResultLauncher<Intent> activityResultLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            Uri uri = data.getData();
                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                                imageView.setImageBitmap(bitmap);
                            } catch (IOException e) {
                                //throw new RuntimeException(e);
                                e.printStackTrace();
                            }
                        }
                    }
                });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intent);
            }
        });
        // Tombol untuk memilih gambar
        buttoncam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(ProfileActivity.this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start();
            }
        });

        //calender

        datePicker = findViewById(R.id.calender);
        calendar = Calendar.getInstance();
        // Menampilkan DatePickerDialog saat ImageButton diklik
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        buttonsave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                saveProfileAndImage();


            }
        });

        ImageView backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kembali ke ProfileFragment
                onBackPressed();
            }

        });
        // Membuat request GET untuk mendapatkan data dari server
        getDataFromServer();

    }

    private void getDataFromServer() {

        tokenManager token = new tokenManager(getApplicationContext());

        // URL endpoint API get_profile() dengan parameter token yang sesuai
        String url = Db_Contract.urlProfile + "?token=" + token.getToken();

        // Buat request GET menggunakan Volley
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle response dari server jika permintaan sukses
                        try {
                            JSONArray dataArray = response.getJSONArray("data");
                            JSONObject userData = dataArray.getJSONObject(0);
                            // Ambil data profil dari respons JSON
                            name = userData.getString("name");
                            email = userData.getString("email");
                            phone = userData.getString("phone");
                            username = userData.getString("username");
                            birthDate = userData.getString("birthdate");
                            password = userData.getString("password");
                            String img = userData.getString("profileImg");

                            // Cek jika nilai kosong atau "null", ganti dengan "-"
                            name = (name.equals("null") || name.isEmpty()) ? "-" : name;
                            email = (email.equals("null") || email.isEmpty()) ? "-" : email;
                            phone = (phone.equals("null") || phone.isEmpty()) ? "-" : phone;
                            username = (username.equals("null") || username.isEmpty()) ? "-" : username;
                            birthDate = (birthDate.equals("null") || birthDate.isEmpty()) ? "-" : birthDate;

                            // Set nilai EditText dengan data yang diperoleh dari respons server
                            editTextName.setText(name);
                            editTextEmail.setText(email);
                            editTextPhone.setText(phone);
                            editTextUsername.setText(username);
                            editTextBirthdate.setText(birthDate);

                            // Menetapkan gambar default dari URL
                            Glide.with(ProfileActivity.this)
                                    .load("https://zenfemina.com/assetsWeb/img/profile/" + img)
                                    .into(imageView);

                            editTextName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                @Override
                                public void onFocusChange(View v, boolean hasFocus) {
                                    if (!hasFocus && editTextName.getText().toString().isEmpty()) {
                                        name = "-";
                                    } else {
                                        name = editTextName.getText().toString();
                                    }
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Error parsing JSON", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error jika permintaan gagal
                Toast.makeText(ProfileActivity.this, "Gagal mendapatkan data!", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Tambahkan request ke queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                R.style.CustomDatePickerDialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Tangani pemilihan tanggal disini
                        calendar.set(year, month, dayOfMonth);

                        String selectedDate = getSelectedDate();
                        editTextBirthdate.setText(selectedDate);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()); // Batasi tanggal yang bisa dipilih menjadi hari ini atau sebelumnya
        datePickerDialog.show();
    }

    private String getSelectedDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ImagePicker.REQUEST_CODE) {
                Uri uri = data.getData();
                imageView.setImageURI(uri);
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void saveProfileAndImage() {
        tokenManager token = new tokenManager(getApplicationContext());
        String img;
        //image
        ByteArrayOutputStream byteArrayOutputStream;
        byteArrayOutputStream = new ByteArrayOutputStream();
        if(bitmap != null){
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            img = Base64.encodeToString(bytes, Base64.DEFAULT);
        } else {
            Toast.makeText(getApplicationContext(), "Gmbar tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        // Ambil data dari EditText
        String nameText = editTextName.getText().toString().trim();
        String phoneText = editTextPhone.getText().toString().trim();
        String usernameText = editTextUsername.getText().toString().trim();
        String emailText = editTextEmail.getText().toString().trim();
        String birthDateText = editTextBirthdate.getText().toString().trim();

        if(TextUtils.isEmpty(nameText) ||
                TextUtils.isEmpty(phoneText) ||
                TextUtils.isEmpty(usernameText) ||
                TextUtils.isEmpty(emailText)
                || TextUtils.isEmpty(birthDateText))
        {
            Toast.makeText(getApplicationContext(), "Input tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        // Konversi tanggal ke format yang diinginkan
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date date = inputFormat.parse(birthDateText);
            birthDateText = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        // Buat StringRequest untuk mengirim data profil dan gambar ke server
        String finalBirthDateText = birthDateText;
        String url = Db_Contract.urlProfile;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            if(status.equals("1")){
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ProfileActivity.this, FragmentActivity.class);
                                startActivity(intent);
                            } else {
                                throw new JSONException(message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage = "Ada kesalahan dalam permintaan";
                if (error != null && error.getMessage() != null) {
                    errorMessage = error.getMessage();
                }
                error.printStackTrace();
                Log.e("Error", errorMessage);
                Toast.makeText(getApplicationContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> paramV = new HashMap<>();
                paramV.put("token", token.getToken());
                paramV.put("email", emailText);
                paramV.put("name", nameText);
                paramV.put("username", usernameText);
                paramV.put("phone", phoneText);
                paramV.put("birthDate", finalBirthDateText);
                paramV.put("password", password);
                paramV.put("profileImg" ,String.valueOf(img));
                return paramV;
            }
        };

        // Tambahkan request ke queue
        queue.add(stringRequest);
    }
}


