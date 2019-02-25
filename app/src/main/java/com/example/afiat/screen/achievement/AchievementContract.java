package com.example.afiat.screen.achievement;

public interface AchievementContract {
    interface View {
        void setMaxSteps(int steps);
        void setHighestSpeed(float speed);
        void setLongestDistance(float distance);
        void sendTwitterPost(String twit);
    }
}
