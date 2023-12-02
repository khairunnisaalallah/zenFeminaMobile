package com.example.myapplication1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class activity_pertanyaan3 extends AppCompatActivity {

    private EditText editTextDataPertanyaan3;
    private Button nextButton;

    public static String value3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pertanyaan3);

        editTextDataPertanyaan3 = findViewById(R.id.editTextDate3);
        nextButton = findViewById(R.id.selanjutnya3);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = getIntent().getStringExtra("token");
                String value1 = getIntent().getStringExtra("value1");
                String value2 = getIntent().getStringExtra("value2");
                String value3 = editTextDataPertanyaan3.getText().toString().trim();

                if (value3.isEmpty()) {
                    Toast.makeText(activity_pertanyaan3.this, "Isi pertanyaan terlebih dahulu", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(activity_pertanyaan3.this, activity_pertanyaan4.class);
                    intent.putExtra("value1", value1);
                    intent.putExtra("value2", value2);
                    intent.putExtra("value3", value3);  // Menyertakan value3 dalam Intent
                    intent.putExtra("token", token);
                    startActivity(intent);
                }
            }
        });
    }
}
