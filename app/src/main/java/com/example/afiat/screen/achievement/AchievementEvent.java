package com.example.afiat.screen.achievement;

public interface AchievementEvent {
    interface Presenter {
        void onStart();
        void onShare();
        void onDestroy();
    }
}
