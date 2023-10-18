package com.example.myapplication1  // Ganti dengan package sesuai proyek Anda

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class activity_pertanyaan1 : AppCompatActivity() {

    private lateinit var datePicker: ImageButton
    private lateinit var nextButton: ImageButton
    private lateinit var calendar: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pertanyaan1)

        datePicker = findViewById(R.id.imageView)
        nextButton = findViewById(R.id.button)
        calendar = Calendar.getInstance()

        // Menampilkan DatePickerDialog saat ImageButton diklik
        datePicker.setOnClickListener {
            showDatePickerDialog()
        }

        // Handle navigasi ke layout pertanyaan selanjutnya saat tombol "Selanjutnya" diklik
        nextButton.setOnClickListener {
            val selectedDate = getSelectedDate()
            // Lakukan sesuatu dengan tanggal yang dipilih (misalnya, tampilkan di log)
            println("Tanggal yang dipilih: $selectedDate")
            // Navigasi ke layout pertanyaan selanjutnya (ActivityPertanyaan2)
            val intent = Intent(this, activity_pertanyaan2::class.java)
            startActivity(intent)
        }
    }

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                // Tangani pemilihan tanggal disini
                calendar.set(year, month, dayOfMonth)

                val selectedDate = getSelectedDate()
                val editTextDate2 = findViewById<EditText>(R.id.editTextDate2)
                editTextDate2.setText(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.datePicker.maxDate = System.currentTimeMillis() // Batasi tanggal yang bisa dipilih menjadi hari ini atau sebelumnya
        datePickerDialog.show()
    }

    private fun getSelectedDate(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)

    }
}