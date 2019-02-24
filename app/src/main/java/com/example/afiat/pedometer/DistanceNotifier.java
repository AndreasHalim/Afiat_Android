package com.example.afiat.pedometer;

public class DistanceNotifier implements StepListener {
    private static final float STEP_LENGTH = 20;

    public interface Listener {
        void valueChanged(float value);
        void passValue();
    }

    private Listener mListener;
    private float mDistance = 0;

    public DistanceNotifier(Listener listener) {
        mListener = listener;
    }

    public void setDistance(float distance) {
        mDistance = distance;
        notifyListener();
    }

    public void onStep() {
        mDistance += (float)(   // kilometers
                STEP_LENGTH     // centimeters
                / 100000.0);    // centimeters/kilometer
        notifyListener();
    }

    private void notifyListener() {
        mListener.valueChanged(mDistance);
    }
}
