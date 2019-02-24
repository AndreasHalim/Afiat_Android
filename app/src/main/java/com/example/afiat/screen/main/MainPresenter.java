package com.example.afiat.screen.main;

import android.content.Context;
import com.example.afiat.R;
import com.example.afiat.service.OnSensorChangeListener;
import com.example.afiat.service.ServiceRunner;

public class MainPresenter implements MainEvent {
    private MainContract.View view;
    private ServiceRunner runner;
    private Context context;

    public MainPresenter(MainContract.View view, MainBus bus, Context context) {
        this.view = view;
        this.context = context;
        bus.setSubscriber(this);
    }

    @Override
    public void onStart() {
        runner = new ServiceRunner(context, callback);
        if (runner.isWorkerServiceRunning()) {
            view.setToggleName(context.getString(R.string.turn_off));
        } else {
            view.setToggleName(context.getString(R.string.turn_on));
        }
    }

    private OnSensorChangeListener callback = new OnSensorChangeListener() {
        @Override
        public void stepsChanged(int value) {
            view.setSteps(value);
        }

        @Override
        public void distanceChanged(float value) {
            view.setDistance(value);
        }

        @Override
        public void speedChanged(float value) {
            view.setSpeed(value);
        }

        @Override
        public void locationUpdated(double latitude, double longitude, short status) { }

        @Override
        public void onLightChange(float lightLevel) { }

        @Override
        public void onGravityChange(float x, float y, float z) { }
    };

    @Override
    public void onToggleService() {
        if (runner.isWorkerServiceRunning()) {
            runner.stop();
            view.setToggleName(context.getString(R.string.turn_on));
        } else {
            runner.bind();
            view.setToggleName(context.getString(R.string.turn_off));
        }
    }

    @Override
    public void onDestroy() {
        runner.unbind();
    }
}
