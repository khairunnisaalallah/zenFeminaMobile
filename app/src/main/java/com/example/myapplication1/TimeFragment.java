package com.example.myapplication1;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.myapplication1.model.DaftarKota;
import com.example.myapplication1.utils.ClientAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressLint("SimpleDateFormat")
@SuppressWarnings("deprecation")
public class TimeFragment extends Fragment {

    private List<DaftarKota> listDaftarKota;
    private ArrayAdapter<DaftarKota> mDaftarKotaAdapter;
    private ProgressDialog progressDialog;
    private TextView mSubuhTv, mDzuhurTv, mAsharTv, mMaghribTv, mIshaTv, mDateTv;
    private Spinner spKota;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_time, container, false);
        mSubuhTv = rootView.findViewById(R.id.tv_subuh);
        mDzuhurTv = rootView.findViewById(R.id.tv_dzuhur);
        mAsharTv = rootView.findViewById(R.id.tv_ashar);
        mMaghribTv = rootView.findViewById(R.id.tv_maghrib);
        mIshaTv = rootView.findViewById(R.id.tv_isya);
        mDateTv = rootView.findViewById(R.id.dateTv);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Mohon Tunggu");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Sedang menampilkan data...");

        spKota = rootView.findViewById(R.id.spinKota);

        listDaftarKota = new ArrayList<>();
        mDaftarKotaAdapter = new ArrayAdapter<>(
                getActivity().getApplicationContext(),
                android.R.layout.simple_spinner_item,
                listDaftarKota);
        mDaftarKotaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spKota.setAdapter(mDaftarKotaAdapter);
        // Set posisi Spinner dan load jadwal sesuai dengan SharedPreferences
        int selectedCityId = loadSelectedCityId();
        int selectedPosition = loadSelectedSpinnerPosition();
        spKota.setSelection(selectedPosition);
        loadJadwal(selectedCityId);
        if (listDaftarKota.isEmpty()) {
            loadKota();
        }

        spKota.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> p0, View view, int position, long id) {
                DaftarKota spinKota = mDaftarKotaAdapter.getItem(position);
                saveSelectedData(spinKota.getId(), position);
                saveSelectedPosition(position);
                saveCityPreference(spinKota.getNama());
                saveSelectedCity(spinKota.getNama());
                loadJadwal(spinKota.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> p0) {
            }
        });
        loadKota();
        return rootView;
    }

    private void saveSelectedPosition(int position) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("selectedSpinnerPosition", position);
        editor.apply();
    }

    private int loadSelectedCityId() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        return preferences.getInt("selectedCityId", -1);
    }

    private int loadSelectedSpinnerPosition() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        return preferences.getInt("selectedSpinnerPosition", 0);
    }
    private void saveSelectedCity(String cityName) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("selectedCityName", cityName);
        editor.apply();
    }
    private String getSelectedCity() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        return preferences.getString("selectedCityName", "");
    }

    private void saveSelectedData(int cityId, int position) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("selectedCityId", cityId);
        editor.putInt("selectedSpinnerPosition", position);
        editor.apply();
    }
    public void saveCityPreference(String city){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("city", city);
        editor.apply();
    }
    private void loadJadwal(Integer id) {
        try {
            progressDialog.show();
            String idKota = String.valueOf(id);
            SimpleDateFormat current = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d MMMM yyyy"); //format baru yg kumasukkan
            String currentDate = sdf.format(new Date());
            String tanggal = current.format(new Date());
            String url = "https://api.banghasan.com/sholat/format/json/jadwal/kota/" + idKota + "/tanggal/" + tanggal;

            ClientAsyncTask task = new ClientAsyncTask(this, new ClientAsyncTask.OnPostExecuteListener() {
                @Override
                public void onPostExecute(String result) {
                    try {
                        progressDialog.dismiss();
                        JSONObject jsonObj = new JSONObject(result);
                        JSONObject objJadwal = jsonObj.getJSONObject("jadwal");
                        JSONObject obData = objJadwal.getJSONObject("data");

                        mSubuhTv.setText(obData.getString("subuh"));
                        mDzuhurTv.setText(obData.getString("dzuhur"));
                        mAsharTv.setText(obData.getString("ashar"));
                        mMaghribTv.setText(obData.getString("maghrib"));
                        mIshaTv.setText(obData.getString("isya"));
                        mDateTv.setText(currentDate);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            task.execute(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadKota() {
        try {
            progressDialog.show();
            String url = "https://api.banghasan.com/sholat/format/json/kota";
            ClientAsyncTask task = new ClientAsyncTask(this, new ClientAsyncTask.OnPostExecuteListener() {
                @Override
                public void onPostExecute(String result) {
                    try {
                        progressDialog.dismiss();
                        JSONObject jsonObj = new JSONObject(result);
                        for (int i = 0; i < jsonObj.getJSONArray("kota").length(); i++) {
                            JSONObject obj = jsonObj.getJSONArray("kota").getJSONObject(i);
                            DaftarKota daftarKota = new DaftarKota();
                            daftarKota.setId(obj.getInt("id"));
                            daftarKota.setNama(obj.getString("nama"));
                            listDaftarKota.add(daftarKota);
                        }

                        // Setelah mengisi daftar kota, cek apakah ada kota yang dipilih sebelumnya
                        String lastSelectedCity = getSelectedCity();
                        if (!lastSelectedCity.isEmpty()) {
                            int spinnerPosition = -1;
                            for (int i = 0; i < listDaftarKota.size(); i++) {
                                if (listDaftarKota.get(i).getNama().equals(lastSelectedCity)) {
                                    spinnerPosition = i;
                                    break;
                                }
                            }
                            if (spinnerPosition != -1) {
                                spKota.setSelection(spinnerPosition);
                            }
                        }
                        mDaftarKotaAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            task.execute(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
