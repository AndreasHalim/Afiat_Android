package com.example.afiat.service;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public class ServiceRunner {
    private Context context;
    private SensorService mSensorService;
    private OnSensorChangeListener callback;
    private ServiceConnection mConnection;

    public ServiceRunner(Context context, OnSensorChangeListener callback) {
        this.context = context;
        this.callback = callback;
    }

    public void bind() {
        if (!isMyServiceRunning(SensorService.class)) {
            startService(context);
            bindStepService(context, callback);
        } else {
            bindStepService(context, callback);
        }
    }

    private void startService(Context context) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(SensorService.RESTART);
        broadcastIntent.setClass(context, StarterBroadcast.class);
        context.sendBroadcast(broadcastIntent);
    }

    private void bindStepService(Context context, final OnSensorChangeListener callback) {
        mConnection = new ServiceConnection() {
            public void onServiceConnected(ComponentName className, IBinder service) {
                mSensorService = ((SensorService.ServiceBinder) service).getService();
                mSensorService.registerCallback(callback);
            }

            public void onServiceDisconnected(ComponentName className) {
                mSensorService = null;
            }
        };

        context.bindService(new Intent(context,
                SensorService.class), mConnection, Context.BIND_AUTO_CREATE);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void unbind() {
        context.unbindService(mConnection);
    }
}
