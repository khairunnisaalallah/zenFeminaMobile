package com.example.myapplication1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class activity_pertanyaan4 extends AppCompatActivity {

    private EditText editTextDate;
    private ImageButton nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pertanyaan4);

        editTextDate = findViewById(R.id.editTextDate2);
        nextButton = findViewById(R.id.selanjutnya4);

        // Tambahkan tindakan untuk tombol "Selanjutnya"
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedDaysText = editTextDate.getText().toString();
                int selectedDays = 1;

                try {
                    selectedDays = Integer.parseInt(selectedDaysText);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(activity_pertanyaan4.this, FragmentActivity.class);
                intent.putExtra("selected_days", selectedDays);
                startActivity(intent);
            }
        });
    }
}
