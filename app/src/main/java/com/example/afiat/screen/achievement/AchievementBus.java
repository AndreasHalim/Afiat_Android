package com.example.afiat.screen.achievement;

public class AchievementBus implements AchievementEvent.Presenter {
    private AchievementEvent.Presenter presenter;

    public void setPresenter(AchievementEvent.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onStart() {
        if (presenter != null) {
            presenter.onStart();
        }
    }

    @Override
    public void onShare() {
        if (presenter != null) {
            presenter.onShare();
        }
    }

    @Override
    public void onDestroy() {
        if (presenter != null) {
            presenter.onDestroy();
        }
    }
}
