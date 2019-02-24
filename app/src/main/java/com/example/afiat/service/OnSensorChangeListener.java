package com.example.afiat.service;

import com.example.afiat.othersensor.OtherListener;

public interface OnSensorChangeListener extends OtherListener {
    void stepsChanged(int value);
    void distanceChanged(float value);
    void speedChanged(float value);
    void locationUpdated(double latitude, double longitude, short status);
}
