package com.example.afiat.datastore;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import static com.example.afiat.datastore.Common.ACHIEVEMENT_KEY;
import static com.example.afiat.datastore.Common.KEY_ACHIEVEMENT_SYNC;
import static com.example.afiat.datastore.Common.KEY_MAX_DISTANCE;
import static com.example.afiat.datastore.Common.KEY_MAX_SPEED;
import static com.example.afiat.datastore.Common.KEY_MAX_STEPS;

public class AchievementStore extends BasicStore {
    private Context context;
    private int maxSteps;
    private float highestSpeed;
    private float longestDistance;

    AchievementStore(Context context) {
        this.context = context;
        this.maxSteps = 0;
        this.highestSpeed = 0;
        this.longestDistance = 0;
        this.isSynchronized = true;
    }

    public int getMaxSteps() {
        return maxSteps;
    }

    public void setMaxSteps(int maxSteps) {
        this.maxSteps = maxSteps;
        isSynchronized = false;
    }

    public float getHighestSpeed() {
        return highestSpeed;
    }

    public void setHighestSpeed(float highestSpeed) {
        this.highestSpeed = highestSpeed;
        isSynchronized = false;
    }

    public float getLongestDistance() {
        return longestDistance;
    }

    public void setLongestDistance(float longestDistance) {
        this.longestDistance = longestDistance;
        isSynchronized = false;
    }

    void persist() {
        SharedPreferences sharedPref = context.getSharedPreferences(ACHIEVEMENT_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(KEY_MAX_STEPS, maxSteps);
        editor.putFloat(KEY_MAX_SPEED, highestSpeed);
        editor.putFloat(KEY_MAX_DISTANCE, longestDistance);
        editor.putBoolean(KEY_ACHIEVEMENT_SYNC, isSynchronized);
        editor.apply();
    }

    void recover() {
        SharedPreferences sharedPref = context.getSharedPreferences(ACHIEVEMENT_KEY, Context.MODE_PRIVATE);
        maxSteps = sharedPref.getInt(KEY_MAX_STEPS, 0);
        highestSpeed = sharedPref.getFloat(KEY_MAX_SPEED, 0);
        longestDistance = sharedPref.getFloat(KEY_MAX_DISTANCE, 0);
        isSynchronized = sharedPref.getBoolean(KEY_ACHIEVEMENT_SYNC, true);
        String log = "AchievementStore: data recovered: " + maxSteps + " " + highestSpeed + " " +
                longestDistance + " " + isSynchronized;
        Log.i("TEST", log);
    }

    void flush() {
        SharedPreferences sharedPref = context.getSharedPreferences(ACHIEVEMENT_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(KEY_MAX_STEPS);
        editor.remove(KEY_MAX_SPEED);
        editor.remove(KEY_MAX_DISTANCE);
        editor.remove(KEY_ACHIEVEMENT_SYNC);
        editor.apply();
    }
}
