package com.example.myapplication1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class activity_pertanyaan3 : AppCompatActivity() {

    private lateinit var editTextDate: EditText
    private lateinit var nextButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pertanyaan3)

        editTextDate = findViewById(R.id.editTextDate2)
        nextButton = findViewById(R.id.button)

        // Tambahkan tindakan untuk tombol "Selanjutnya"
        nextButton.setOnClickListener {
            val selectedDaysText = editTextDate.text.toString()
            val selectedDays = selectedDaysText.toIntOrNull() ?: 1

            val intent = Intent(this, activity_pertanyaan4::class.java)
            intent.putExtra("selected_days", selectedDays)
            startActivity(intent)
        }
    }
}
