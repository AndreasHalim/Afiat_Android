package com.example.afiat.screen.main;

public class MainBus implements MainEvent {
    private MainEvent subscriber;

    public void setSubscriber(MainEvent subscriber) {
        this.subscriber = subscriber;
    }

    @Override
    public void onStart() {
        if (subscriber != null) {
            subscriber.onStart();
        }
    }

    @Override
    public void onToggleService() {
        if (subscriber != null) {
            subscriber.onToggleService();
        }
    }

    @Override
    public void onDestroy() {
        if (subscriber != null) {
            subscriber.onDestroy();
        }
    }
}
