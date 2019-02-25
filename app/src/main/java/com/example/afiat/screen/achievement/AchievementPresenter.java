package com.example.afiat.screen.achievement;

import android.content.Context;
import android.util.Log;

import com.example.afiat.datastore.AchievementStore;
import com.example.afiat.datastore.StoreManager;

import java.text.DecimalFormat;

public class AchievementPresenter implements AchievementEvent.Presenter {
    private AchievementContract.View view;
    private Context context;

    public AchievementPresenter(AchievementContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void onStart() {
        StoreManager manager = StoreManager.getInstance(context);
        AchievementStore store = manager.getAchievement();
        view.setMaxSteps(store.getMaxSteps());
        view.setHighestSpeed(store.getHighestSpeed());
        view.setLongestDistance(store.getLongestDistance());
    }

    @Override
    public void onShare() {
        StoreManager manager = StoreManager.getInstance(context);
        AchievementStore store = manager.getAchievement();
        DecimalFormat df = new DecimalFormat("#.00");
        String twit = "I have been done " +
                store.getMaxSteps() + " steps, with average speed of" +
                df.format(store.getHighestSpeed()) + " km/h over " +
                df.format(store.getLongestDistance()) + " km. Yeay!";
        view.sendTwitterPost(twit);
    }

    @Override
    public void onDestroy() { }
}
