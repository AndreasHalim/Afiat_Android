package com.example.afiat.datastore;

import android.content.Context;
import android.content.SharedPreferences;

import static com.example.afiat.datastore.Common.ACHIEVEMENT_KEY;
import static com.example.afiat.datastore.Common.KEY_HOME_LATITUDE;
import static com.example.afiat.datastore.Common.KEY_HOME_LONGITUDE;
import static com.example.afiat.datastore.Common.KEY_PROFILE_SYNC;

public class ProfileStore extends BasicStore {
    boolean isSynchronized;

    private Context context;
    private float homeLat;
    private float homeLng;

    ProfileStore(Context context) {
        this.context = context;
        this.homeLat = 0;
        this.homeLng = 0;
        this.isSynchronized = true;
    }

    public float getHomeLat() {
        return homeLat;
    }

    public void setHomeLat(float homeLat) {
        this.homeLat = homeLat;
        isSynchronized = false;
    }

    public float getHomeLng() {
        return homeLng;
    }

    public void setHomeLng(float homeLng) {
        this.homeLng = homeLng;
        isSynchronized = false;
    }

    void persist() {
        SharedPreferences sharedPref = context.getSharedPreferences(ACHIEVEMENT_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putFloat(KEY_HOME_LATITUDE, homeLat);
        editor.putFloat(KEY_HOME_LONGITUDE, homeLng);
        editor.apply();
    }

    void recover() {
        SharedPreferences sharedPref = context.getSharedPreferences(ACHIEVEMENT_KEY, Context.MODE_PRIVATE);
        homeLat = sharedPref.getFloat(KEY_HOME_LATITUDE, 0);
        homeLng = sharedPref.getFloat(KEY_HOME_LONGITUDE, 0);
        isSynchronized = sharedPref.getBoolean(KEY_PROFILE_SYNC, true);
    }

    void flush() {
        SharedPreferences sharedPref = context.getSharedPreferences(ACHIEVEMENT_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(KEY_HOME_LATITUDE);
        editor.remove(KEY_HOME_LONGITUDE);
        editor.remove(KEY_PROFILE_SYNC);
        editor.apply();
    }
}
