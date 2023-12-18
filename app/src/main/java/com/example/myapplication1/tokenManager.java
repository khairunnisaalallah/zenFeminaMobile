package com.example.myapplication1;

import android.content.Context;
import android.content.SharedPreferences;

public class tokenManager {
    private static final String PREF_NAME = "zenfemina";
    private  static final String KEY_TOKEN = "token";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public tokenManager(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveToken(String session){
        editor.putString(KEY_TOKEN, session);
        editor.apply();
    }

    public String getToken(){
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    public void deleteToken(){
        editor.remove(KEY_TOKEN);
        editor.apply();
    }
}
