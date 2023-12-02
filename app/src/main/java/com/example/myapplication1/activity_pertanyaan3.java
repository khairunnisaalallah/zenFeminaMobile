package com.example.myapplication1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class activity_pertanyaan3 extends AppCompatActivity {

    private EditText editTextDate;
    private Button nextButton;
    public static String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pertanyaan3);

        editTextDate = findViewById(R.id.editTextDate2);
        nextButton = findViewById(R.id.selanjutnya3);

        // Tambahkan tindakan untuk tombol "Selanjutnya"
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value = editTextDate.getText().toString();
                Log.d("MY_TAG", "Value3: " + value);

                Intent intent = new Intent(activity_pertanyaan3.this, activity_pertanyaan4.class);
                intent.putExtra("value3", value);
                startActivity(intent);
            }
        });
    }
}
