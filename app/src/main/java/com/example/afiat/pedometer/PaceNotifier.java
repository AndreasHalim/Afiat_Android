package com.example.afiat.pedometer;

import java.util.ArrayList;

public class PaceNotifier implements StepListener {
    public interface Listener {
        void paceChanged(int value);
    }

    private ArrayList<PaceNotifier.Listener> mListeners = new ArrayList<>();

    private long mLastStepTime = 0;
    private long[] mLastStepDeltas = {-1, -1, -1, -1};
    private int mLastStepDeltasIndex = 0;
    private long mPace = 0;

    public void setPace(int pace) {
        mPace = pace;
        int avg = (int)(60*1000.0 / mPace);
        for (int i = 0; i < mLastStepDeltas.length; i++) {
            mLastStepDeltas[i] = avg;
        }
        notifyListener();
    }

    public void addListener(PaceNotifier.Listener l) {
        mListeners.add(l);
    }

    public void onStep() {
        long thisStepTime = System.currentTimeMillis();

        // Calculate pace based on last x steps
        if (mLastStepTime > 0) {
            long delta = thisStepTime - mLastStepTime;

            mLastStepDeltas[mLastStepDeltasIndex] = delta;
            mLastStepDeltasIndex = (mLastStepDeltasIndex + 1) % mLastStepDeltas.length;

            long sum = 0;
            boolean isMeaningfull = true;
            for (long mLastStepDelta : mLastStepDeltas) {
                if (mLastStepDelta < 0) {
                    isMeaningfull = false;
                    break;
                }
                sum += mLastStepDelta;
            }
            if (isMeaningfull && sum > 0) {
                long avg = sum / mLastStepDeltas.length;
                mPace = 60*1000 / avg;
            } else {
                mPace = -1;
            }
        }

        mLastStepTime = thisStepTime;
        notifyListener();
    }

    private void notifyListener() {
        for (PaceNotifier.Listener listener : mListeners) {
            listener.paceChanged((int)mPace);
        }
    }
}
