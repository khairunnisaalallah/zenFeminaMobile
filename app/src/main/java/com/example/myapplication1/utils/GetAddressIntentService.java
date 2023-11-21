package com.example.myapplication1.utils;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GetAddressIntentService extends IntentService {
    private ResultReceiver addressResultReceiver;

    public GetAddressIntentService() {
        super(IDENTIFIER);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String msg = "";

        addressResultReceiver = intent.getParcelableExtra("add_receiver");
        if (addressResultReceiver == null) {
            return;
        }
        Location location = intent.getParcelableExtra("add_location");

        if (location == null) {
            msg = "No location, can't go further without location";
            sendResultsToReceiver(0, msg);
            return;
        }
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    1);
        } catch (IOException ignored) {
        }
        if (addresses == null || addresses.size() == 0) {
            msg = "No address found for the location";
            sendResultsToReceiver(1, msg);
        } else {
            Address address = addresses.get(0);
            StringBuilder addressDetails = new StringBuilder();

            addressDetails.append(address.getFeatureName());
            addressDetails.append("\n");
            addressDetails.append(address.getLocality());
            addressDetails.append("\n");
            addressDetails.append(address.getSubAdminArea());
            addressDetails.append("\n");
            addressDetails.append(address.getPostalCode());
            addressDetails.append("\n");
            addressDetails.append(address.getThoroughfare());
            addressDetails.append("\n");
            addressDetails.append(address.getCountryName());
            addressDetails.append("\n");

            addressDetails.append(address.getAdminArea());
            addressDetails.append("\n");
            sendResultsToReceiver(2, addressDetails.toString());
        }
    }

    private void sendResultsToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString("address_result", message);
        addressResultReceiver.send(resultCode, bundle);
    }

    private static final String IDENTIFIER = "GetAddressIntentService";
}

