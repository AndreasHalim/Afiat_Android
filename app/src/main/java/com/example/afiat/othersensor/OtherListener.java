package com.example.afiat.othersensor;

public interface OtherListener {
    void onLightChange(float lightLevel);
    void onGravityChange(float x, float y, float z);
}
