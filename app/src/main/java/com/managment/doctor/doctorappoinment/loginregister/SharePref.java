package com.managment.doctor.doctorappoinment.loginregister;

import android.content.Context;
import android.content.SharedPreferences;

import com.managment.doctor.doctorappoinment.loginregister.activities.LoginActivity;

public class SharePref 
{

    private static SharePref mSharedPreferenceHelper = null;
    private final static String PREF_FILE = "Location";
    private static SharedPreferences settings;

    public synchronized static SharePref getInstance(Context context) {
        if (mSharedPreferenceHelper == null) {
            mSharedPreferenceHelper = new SharePref();
        }
        if (context != null) {
            settings = context.getSharedPreferences(PREF_FILE, 0);
        }
        return mSharedPreferenceHelper;
    }

    public String getSharedPreferenceString( String key, String defValue) {
        return settings.getString(key, defValue);
    }

    public void setSharedPreferenceString(String key, String value) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.apply();
    }
}
