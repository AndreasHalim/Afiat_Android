package com.example.afiat.datastore;

import android.content.Context;

class ApiUtils {
    static void persistAchievement(String identifier, AchievementStore achievement, PersistListener callback){
        callback.onFinish(false);
    }

    static void persistProfile(String identifier, ProfileStore profile, PersistListener callback){
        callback.onFinish(false);
    }

    static void persistTracking(String identifier, TrackingStore tracking, PersistListener callback){
        callback.onFinish(false);
    }

    static void fetchAccount(Context context, String identifier, FetchStoreListener callback) {
        BasicStore []stores = new BasicStore[3];
        stores[0] = new AchievementStore(context);
        stores[1] = new ProfileStore(context);
        stores[2] = new TrackingStore(context);
        callback.onFinish(false, stores);
    }
}
