package com.example.afiat.datastore;

import android.content.Context;
import android.content.SharedPreferences;

import static com.example.afiat.datastore.Common.ACHIEVEMENT_KEY;
import static com.example.afiat.datastore.Common.KEY_IDENTIFIER;

public class StoreManager {
    private static StoreManager instance;

    private SharedPreferences sharedPref;
    private String identifier;
    private AchievementStore achievement;
    private TrackingStore tracking;
    private ProfileStore profile;
    private PersistListener flushListener;
    private int flushFailedCount;
    private int flushFinishCount;

    public static StoreManager getInstance(Context ctx) {
        if (instance == null) {
            SharedPreferences sharedPref = ctx.getSharedPreferences(ACHIEVEMENT_KEY, Context.MODE_PRIVATE);
            String currentIdentifier = sharedPref.getString(KEY_IDENTIFIER, null);

            if (currentIdentifier != null) {
                instance = new StoreManager(ctx, currentIdentifier);
                instance.achievement.recover();
                instance.profile.recover();
                instance.tracking.recover();
            }
        }

        return instance;
    }

    public static void newInstance(Context ctx, String identifier, final StoreManagerListener listener) {
        getInstance(ctx);

        if (instance != null) {
            instance.achievement.flush();
            instance.tracking.flush();
            instance.profile.flush();
            instance.identifier = identifier;
            instance.persistIdentifier();
        } else {
            instance = new StoreManager(ctx, identifier);
            instance.persistIdentifier();
        }

        ApiUtils.fetchAccount(ctx, identifier, new FetchStoreListener() {
            @Override
            public void onFinish(boolean success, BasicStore[] stores) {
                if (success) {
                    if (stores[0] != null) {
                        instance.achievement = (AchievementStore) stores[0];
                    }
                    if (stores[1] != null) {
                        instance.profile = (ProfileStore) stores[1];
                    }
                    if (stores[2] != null) {
                        instance.tracking = (TrackingStore) stores[2];
                    }
                    listener.onCreatedNewInstance(instance);
                }
            }
        });
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public AchievementStore getAchievement() {
        return this.achievement;
    }

    public ProfileStore getProfile() {
        return this.profile;
    }

    public TrackingStore getTracking() {
        return this.tracking;
    }

    public void persistAndFlush(PersistListener listener) {
        flushListener = listener;
        flushFailedCount = 0;
        flushFinishCount = 0;

        if (!achievement.isSynchronized) {
            ApiUtils.persistAchievement(identifier, achievement, new FlushCallback(achievement));
        } else {
            flushFinishHandler(true);
        }

        if (!profile.isSynchronized) {
            ApiUtils.persistProfile(identifier, profile, new FlushCallback(profile));
        } else {
            flushFinishHandler(true);
        }

        if (!tracking.isSynchronized) {
            ApiUtils.persistTracking(identifier, tracking, new FlushCallback(tracking));
        } else {
            flushFinishHandler(true);
        }
    }

    public void persistAchievement(final PersistListener listener) {
        achievement.persist();
        ApiUtils.persistAchievement(identifier, achievement, new PersistListener() {
            @Override
            public void onFinish(boolean success) {
                achievement.isSynchronized = success;
                listener.onFinish(success);
            }
        });
    }

    public void persistProfile(final PersistListener listener) {
        profile.persist();
        ApiUtils.persistProfile(identifier, profile, new PersistListener() {
            @Override
            public void onFinish(boolean success) {
                profile.isSynchronized = success;
                listener.onFinish(success);
            }
        });
    }

    public void persistTracking(final PersistListener listener) {
        tracking.persist();
        ApiUtils.persistTracking(identifier, tracking, new PersistListener() {
            @Override
            public void onFinish(boolean success) {
                tracking.isSynchronized = success;
                listener.onFinish(success);
            }
        });
    }

    private void flushFinishHandler(boolean success) {
        flushFinishCount++;
        if (!success) {
            flushFailedCount++;
        }
        if (flushFinishCount == 3) {
            if (flushFailedCount == 0) {
                flushIdentifier();
                flushListener.onFinish(true);
            } else {
                flushListener.onFinish(false);
            }
        }
    }

    private void persistIdentifier() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(KEY_IDENTIFIER, identifier);
        editor.apply();
    }

    private void flushIdentifier() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(KEY_IDENTIFIER);
        editor.apply();
    }

    private class FlushCallback implements PersistListener {
        private BasicStore store;

        FlushCallback(BasicStore store) {
            this.store = store;
        }

        @Override
        public void onFinish(boolean success) {
            if (success) {
                store.flush();
            } else {
                store.persist();
            }
            flushFinishHandler(success);
        }
    }

    private StoreManager(Context ctx, String identifier) {
        this.sharedPref = ctx.getSharedPreferences(ACHIEVEMENT_KEY, Context.MODE_PRIVATE);
        this.identifier = identifier;
        this.achievement = new AchievementStore(ctx);
        this.profile = new ProfileStore(ctx);
        this.tracking = new TrackingStore(ctx);
    }
}
