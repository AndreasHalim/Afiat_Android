package com.example.afiat.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

public class ServiceNotificationBuilder {
    private static final String NOTIFICATION_CHANNEL_ID = "com.example.afiat";
    private static final String channelName = "Sensor Service";

    public static Notification Build(NotificationManager manager, Context context) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            createNotificationChannel(manager);
            return createCustomNotification(context);
        } else {
            return new Notification();
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private static void createNotificationChannel(NotificationManager manager) {
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        assert manager != null;
        manager.createNotificationChannel(chan);
    }

    private static Notification createCustomNotification(Context context) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
        return notificationBuilder.setOngoing(true)
                .setContentTitle("Afiat is running.")
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
    }
}
