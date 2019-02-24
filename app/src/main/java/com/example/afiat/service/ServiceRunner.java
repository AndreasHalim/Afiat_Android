package com.example.afiat.service;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public class ServiceRunner {
    private Context context;
    private WorkerService mWorkerService;
    private OnSensorChangeListener callback;
    private ServiceConnection mConnection;

    public ServiceRunner(Context context, OnSensorChangeListener callback) {
        this.context = context;
        this.callback = callback;
    }

    public void bind() {
        if (!isMyServiceRunning(WorkerService.class)) {
            startService(context);
            bindStepService(context, callback);
        } else {
            bindStepService(context, callback);
        }
    }

    public void unbind() {
        context.unbindService(mConnection);
    }

    public WorkerService getService() {
        return this.mWorkerService;
    }

    private void startService(Context context) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(WorkerService.RESTART);
        broadcastIntent.setClass(context, StarterBroadcast.class);
        context.sendBroadcast(broadcastIntent);
    }

    private void bindStepService(Context context, final OnSensorChangeListener callback) {
        mConnection = new ServiceConnection() {
            public void onServiceConnected(ComponentName className, IBinder service) {
                mWorkerService = ((WorkerService.ServiceBinder) service).getService();
                mWorkerService.registerCallback(callback);
            }

            public void onServiceDisconnected(ComponentName className) {
                mWorkerService = null;
            }
        };

        context.bindService(new Intent(context,
                WorkerService.class), mConnection, Context.BIND_AUTO_CREATE);
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
}
