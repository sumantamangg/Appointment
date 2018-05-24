package com.suman.appointment;

import android.content.Context;
import android.content.SharedPreferences;

public class MyPreferences {
    public static final String PREF_NAME = "app-pref";
    private static final String LOGGED_IN = "loggedin";
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public MyPreferences(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(LOGGED_IN, false);
    }

    public void setLoggedIn(Boolean value) {
        editor.putBoolean(LOGGED_IN, value).apply();
    }
}
