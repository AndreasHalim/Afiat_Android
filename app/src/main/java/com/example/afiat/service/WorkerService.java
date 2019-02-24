package com.example.afiat.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.afiat.location.LocationNotifier;
import com.example.afiat.location.OnLocationUpdated;
import com.example.afiat.othersensor.OtherListener;
import com.example.afiat.pedometer.DistanceNotifier;
import com.example.afiat.pedometer.PaceNotifier;
import com.example.afiat.pedometer.SpeedNotifier;
import com.example.afiat.pedometer.StepDetector;
import com.example.afiat.pedometer.StepDisplayer;
import com.example.afiat.othersensor.OtherDetector;

import java.util.Timer;
import java.util.TimerTask;

public class WorkerService extends Service {
    public static final String RESTART = "restart_service";
    public static Boolean running = false;
    private static int mStepCount = 0;
    private static float mSpeed = 0;
    private static float mDistance = 0;
    private static float mLight = 0;
    /*
     * mGravity[] = {x-axis, y-axis, z-axis}
     */
    private static float[] mGravity = new float[3];
    /*
     * position[] = {latitude, longitude}
     */
    private static double[] position = new double[2];
    private static short locationLastStatus = LocationNotifier.STATUS_NO_PROVIDER;
    private static  ToastHandler handler;

    private final IBinder mBinder = new WorkerService.ServiceBinder();

    private OnSensorChangeListener mCallback;
    private LocationNotifier mLocationNotifier;

    public void registerCallback(OnSensorChangeListener cb) {
        mCallback = cb;
        if (mCallback != null) {
            mCallback.stepsChanged(mStepCount);
            mCallback.distanceChanged(mDistance);
            mCallback.speedChanged(mSpeed);
            mCallback.onLightChange(mLight);
            mCallback.onGravityChange(mGravity[0], mGravity[1], mGravity[2]);
            mCallback.locationUpdated(position[0], position[1], locationLastStatus);
        }
    }

    class ServiceBinder extends Binder {
        WorkerService getService() {
            return WorkerService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        startForeground(2, ServiceNotificationBuilder.Build(manager, this));
        running = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        Log.i("AFIAT", "Service is started!");

        mLocationNotifier = setupLocationProvider();
        StepDetector mStepDetector = setupStepDetector();
        StepDisplayer mStepDisplayer = setupStepDisplayer();
        PaceNotifier mPaceNotifier = setupPaceNotifier();
        SpeedNotifier mSpeedNotifier = setupSpeedNotifier();
        DistanceNotifier mDistanceNotifier = setupDistanceNotifier();

        mStepDetector.addStepListener(mStepDisplayer);
        mStepDetector.addStepListener(mPaceNotifier);
        mPaceNotifier.addListener(mSpeedNotifier);
        mStepDetector.addStepListener(mDistanceNotifier);

        OtherDetector mOtherDetector = setupOtherDetector();
        mOtherDetector.addListener(new OtherListener() {
            @Override
            public void onLightChange(float lightLevel) {
                mLight = lightLevel;
                if (mCallback != null) {
                    mCallback.onLightChange(lightLevel);
                }
            }

            @Override
            public void onGravityChange(float x, float y, float z) {
                mGravity[0] = x;
                mGravity[1] = y;
                mGravity[2] = z;
                if (mCallback != null) {
                    mCallback.onGravityChange(x, y, z);
                }
            }
        });

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (running) {
            Log.i("AFIAT", "Service is restarted");
            start(this);
        } else {
            Log.i("AFIAT", "Service is destroyed!");
        }
    }

    public void activateLocationListener() {
        mLocationNotifier.activateLocationListener();
    }

    private static void start(Context context) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(WorkerService.RESTART);
        broadcastIntent.setClass(context, StarterBroadcast.class);
        context.sendBroadcast(broadcastIntent);
    }

    private class ToastHandler extends Handler {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            showToast(msg.getData().getString("message"));
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private LocationNotifier setupLocationProvider() {
        LocationNotifier locationNotifier = new LocationNotifier(getApplicationContext());
        locationNotifier.registerCallback(new OnLocationUpdated() {
            @Override
            public void onUpdate(double longitude, double latitude, short status) {
                position[0] = longitude;
                position[1] = latitude;
                locationLastStatus = status;
                if (mCallback != null) {
                    mCallback.locationUpdated(position[0], position[1], locationLastStatus);
                }
            }
        });
        return locationNotifier;
    }

    private OtherDetector setupOtherDetector() {
        OtherDetector otherDetector = new OtherDetector();
        otherDetector.addListener(new OtherListener() {
            @Override
            public void onLightChange(float lightLevel) {
                mLight = lightLevel;
                if (mCallback != null) {
                    mCallback.onLightChange(mLight);
                }
            }

            @Override
            public void onGravityChange(float x, float y, float z) {
                mGravity[0] = x;
                mGravity[1] = y;
                mGravity[2] = z;
                if (mCallback != null) {
                    mCallback.onGravityChange(mGravity[0], mGravity[1], mGravity[2]);
                }
            }
        });

        SensorManager mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (handler == null) {
            handler = new ToastHandler();
        }

        Sensor gravity = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        if (gravity == null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("message", "Gravity sensor is not exists!");
            msg.setData(bundle);
            handler.sendMessage(msg);
        } else {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("message", "Gravity sensor is exists!");
            msg.setData(bundle);
            handler.sendMessage(msg);

            mSensorManager.registerListener(otherDetector,
                gravity,
                SensorManager.SENSOR_DELAY_NORMAL);
        }

        Sensor light = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        if (light == null) {
            (new Timer()).schedule(new TimerTask() {
                @Override
                public void run() {
                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("message", "Light sensor is not exists!");
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
            }, 1000);
        } else {
            (new Timer()).schedule(new TimerTask() {
                @Override
                public void run() {
                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("message", "Light sensor is exists!");
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
            }, 3000);

            mSensorManager.registerListener(otherDetector,
                light,
                SensorManager.SENSOR_DELAY_NORMAL);
        }

        return otherDetector;
    }

    private StepDetector setupStepDetector() {
        StepDetector mStepDetector = new StepDetector();
        mStepDetector.setSensitivity(10);
        SensorManager mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor mSensor = mSensorManager.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(mStepDetector,
                mSensor,
                SensorManager.SENSOR_DELAY_FASTEST);
        return mStepDetector;
    }

    private StepDisplayer setupStepDisplayer() {
        StepDisplayer.Listener mStepListener = new StepDisplayer.Listener() {
            public void stepsChanged(int value) {
                mStepCount = value;
                passValue();
            }
            public void passValue() {
                if (mCallback != null) {
                    mCallback.stepsChanged(mStepCount);
                }
            }
        };

        StepDisplayer mStepDisplayer = new StepDisplayer();
        mStepDisplayer.setSteps(0);
        mStepDisplayer.addListener(mStepListener);
        return mStepDisplayer;
    }

    private PaceNotifier setupPaceNotifier() {
        PaceNotifier mPaceNotifier = new PaceNotifier();
        mPaceNotifier.setPace(0);
        return mPaceNotifier;
    }

    private SpeedNotifier setupSpeedNotifier() {
        SpeedNotifier.Listener mSpeedListener = new SpeedNotifier.Listener() {
            public void valueChanged(float value) {
                mSpeed = value;
                passValue();
            }
            public void passValue() {
                if (mCallback != null) {
                    mCallback.speedChanged(mSpeed);
                }
            }
        };
        SpeedNotifier mSpeedNotifier = new SpeedNotifier(mSpeedListener);
        mSpeedNotifier.setSpeed(0);
        return mSpeedNotifier;
    }

    private DistanceNotifier setupDistanceNotifier() {
        DistanceNotifier.Listener mDistanceListener = new DistanceNotifier.Listener() {
            public void valueChanged(float value) {
                mDistance = value;
                passValue();
            }
            public void passValue() {
                if (mCallback != null) {
                    mCallback.distanceChanged(mDistance);
                }
            }
        };
        DistanceNotifier mDistanceNotifier = new DistanceNotifier(mDistanceListener);
        mDistanceNotifier.setDistance(0);
        return mDistanceNotifier;
    }
}