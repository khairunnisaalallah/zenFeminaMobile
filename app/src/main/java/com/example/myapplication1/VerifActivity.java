package com.example.myapplication1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class VerifActivity extends AppCompatActivity {

    String otp, phone;
    String message = "ini kode verifikasi anda.";
    Button button;
    EditText etotp, etphone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verif);

        button = findViewById(R.id.send);
        etotp = findViewById(R.id.etotp);
        etphone = findViewById(R.id.etphone);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(VerifActivity.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {

                    sendOTP();
                } else {
                    ActivityCompat.requestPermissions(VerifActivity.this, new String[]{Manifest.permission.SEND_SMS}, 100);
                }
            }
        });
    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResult) {
        if (requestCode == 100) {
            if (grantResult.length > 0 && grantResult[0] == PackageManager.PERMISSION_GRANTED) {
                sendOTP();
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*private void sendOTP(){

            otp = etotp.getText().toString();
            phone = etphone.getText().toString();

            SmsManager smsManager = SmsManager.getDefault();
            ArrayList<String> parts = smsManager.divideMessage(otp+" "+message);
            String phoneNumber = phone;
            smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null, null);
        }*/
    private void sendOTP() {
        otp = etotp.getText().toString();
        phone = etphone.getText().toString();

        try {
            SmsManager smsManager = SmsManager.getDefault();
            ArrayList<String> parts = smsManager.divideMessage(otp + " " + message);
            String phoneNumber = phone;
            smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null, null);

            // SMS berhasil dikirim
            Toast.makeText(this, "SMS Berhasil Dikirim", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            // Kesalahan dalam pengiriman SMS
            Toast.makeText(this, "Gagal Mengirim SMS", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
