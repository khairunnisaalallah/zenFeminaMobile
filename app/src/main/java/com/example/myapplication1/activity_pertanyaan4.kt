package com.example.myapplication1

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class activity_pertanyaan4 : AppCompatActivity() {

    private lateinit var editTextDate: EditText
    private lateinit var nextButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pertanyaan4)

        editTextDate = findViewById(R.id.editTextDate2)
        nextButton = findViewById(R.id.button)

        // Tambahkan tindakan untuk tombol "Selanjutnya"
        nextButton.setOnClickListener {
            val selectedDaysText = editTextDate.text.toString()
            val selectedDays = selectedDaysText.toIntOrNull() ?: 1

            val intent = Intent(this, activity_dashboard::class.java)
            intent.putExtra("selected_days", selectedDays)
            startActivity(intent)
        }
    }
}
