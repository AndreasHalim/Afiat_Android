package com.example.afiat.screen.main;

public class MainBus implements MainEvent.Presenter, MainEvent.Activity {
    private MainEvent.Presenter presenter;
    private MainEvent.Activity activity;

    public MainBus(MainEvent.Activity activity) {
        this.activity = activity;
    }

    public void setPresenter(MainEvent.Presenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void onStart() {
        if (presenter != null) {
            presenter.onStart();
        }
    }

    @Override
    public void onToggleService() {
        if (presenter != null) {
            presenter.onToggleService();
        }
    }

    @Override
    public void onDestroy() {
        if (presenter != null) {
            presenter.onDestroy();
        }
    }

    @Override
    public void refreshPager() {
        if (activity != null) {
            activity.refreshPager();
        }
    }
}
