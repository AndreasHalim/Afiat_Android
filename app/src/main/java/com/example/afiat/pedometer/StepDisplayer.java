package com.example.afiat.pedometer;

import java.util.ArrayList;

public class StepDisplayer implements StepListener {
    private int mCount = 0;

    public StepDisplayer() {
        notifyListener();
    }

    public void setSteps(int steps) {
        mCount = steps;
        notifyListener();
    }

    public void onStep() {
        mCount++;
        notifyListener();
    }

    public interface Listener {
        void stepsChanged(int value);
        void passValue();
    }

    private ArrayList<Listener> mListeners = new ArrayList<>();

    public void addListener(Listener l) {
        mListeners.add(l);
    }

    private void notifyListener() {
        for (Listener listener : mListeners) {
            listener.stepsChanged(mCount);
        }
    }
}
