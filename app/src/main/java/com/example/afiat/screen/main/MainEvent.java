package com.example.afiat.screen.main;

public interface MainEvent {
    interface Presenter {
        void onStart();
        void onToggleService();
        void onDestroy();
    }

    public interface Activity {
        void refreshPager();
    }
}
