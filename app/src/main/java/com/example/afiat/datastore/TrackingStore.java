package com.example.afiat.datastore;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import static com.example.afiat.datastore.Common.ACHIEVEMENT_KEY;
import static com.example.afiat.datastore.Common.KEY_LAST_TRACKING_TIME;
import static com.example.afiat.datastore.Common.KEY_TRACKING_SYNC;

public class TrackingStore extends BasicStore {
    boolean isSynchronized;

    private Context context;
    private long lastTrackingTime;

    TrackingStore(Context context) {
        this.context = context;
        this.lastTrackingTime = 0;
        this.isSynchronized = true;
    }

    public long getLastTrackingTime() {
        return lastTrackingTime;
    }

    public void setLastTrackingTime(long lastTrackingTime) {
        this.lastTrackingTime = lastTrackingTime;
        isSynchronized = false;
    }

    void persist() {
        SharedPreferences sharedPref = context.getSharedPreferences(ACHIEVEMENT_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(KEY_LAST_TRACKING_TIME, lastTrackingTime);
        editor.putBoolean(KEY_TRACKING_SYNC, isSynchronized);
        editor.apply();
    }

    void recover() {
        SharedPreferences sharedPref = context.getSharedPreferences(ACHIEVEMENT_KEY, Context.MODE_PRIVATE);
        lastTrackingTime = sharedPref.getLong(KEY_LAST_TRACKING_TIME, 0);
        isSynchronized = sharedPref.getBoolean(KEY_TRACKING_SYNC, true);
        String log = "TrackingStore: data recovered: " + lastTrackingTime + " " + isSynchronized;
        Log.i("TEST", log);
    }

    void flush() {
        SharedPreferences sharedPref = context.getSharedPreferences(ACHIEVEMENT_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(KEY_LAST_TRACKING_TIME);
        editor.remove(KEY_TRACKING_SYNC);
        editor.apply();
    }
}
