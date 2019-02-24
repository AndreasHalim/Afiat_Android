package com.example.afiat.pedometer;

public class SpeedNotifier implements PaceNotifier.Listener {
    private static final float STEP_LENGTH = 20;
    private float mSpeed = 0;

    public interface Listener {
        void valueChanged(float value);
        void passValue();
    }

    private Listener mListener;

    public SpeedNotifier(Listener listener) {
        mListener = listener;
    }

    public void setSpeed(float speed) {
        mSpeed = speed;
        notifyListener();
    }

    private void notifyListener() {
        mListener.valueChanged(mSpeed);
    }

    public void paceChanged(int value) {
        mSpeed =                // kilometers / hour
            value * STEP_LENGTH // centimeters / minute
            / 100000f * 60f;    // centimeters/kilometer
        notifyListener();
    }
}
