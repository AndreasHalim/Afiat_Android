package com.example.afiat.screen.main;

public interface MainContract {
    interface View {
        void setSteps(int steps);
        void setSpeed(float speed);
        void setDistance(float distance);
        void setToggleName(String message);
    }
}
