package com.hlabexamples.commonmvp.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * <p>
 * Purpose of this class is to save data in preference and retrieve values from preference throughout the lifecycle of application
 * This class is hold methods for storing and retrieving values from preference.
 * </p>
 */
public class PreferenceUtils {

    private SharedPreferences sharedPreferences;

    public PreferenceUtils(Context context) {
        sharedPreferences = context.getSharedPreferences("Pref", Context.MODE_PRIVATE);
    }

    public void setData(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    public void getData(String key, String defaultValue) {
        sharedPreferences.getString(key, defaultValue);
    }

    public void setBoolean(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    /**
     * clearAllPreferenceData : it will clear all data from preference
     */
    public void clearAllPreferenceData() {
        sharedPreferences.edit().clear().apply();
    }


}
