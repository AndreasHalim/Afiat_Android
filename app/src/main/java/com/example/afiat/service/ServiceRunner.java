package com.example.afiat.service;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

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
        if (!isWorkerServiceRunning()) {
            startService(context);
            bindStepService(context, callback);
        } else {
            bindStepService(context, callback);
        }
    }

    public void unbind() {
        if (mConnection != null) {
            context.unbindService(mConnection);
            mConnection = null;
        }
    }

    public void stop() {
        if (mWorkerService != null) {
            unbind();
            mWorkerService.stop();
            mConnection = null;
            mWorkerService = null;
        }
    }

    public WorkerService getService() {
        return this.mWorkerService;
    }

    public boolean isWorkerServiceRunning() {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (WorkerService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
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
}
