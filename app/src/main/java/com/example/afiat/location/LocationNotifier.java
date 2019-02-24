package com.example.afiat.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class LocationNotifier {
    public static final short STATUS_PROHIBITTED = -1;
    public static final short STATUS_OK = 0;
    public static final short STATUS_NO_PROVIDER = 1;

    private static final long UPDATE_DELAY = 700;
    private static final long NOTIFY_DELAY = 1000;
    private static final float MIN_DISTANCE = 0.0001f;

    private Context context;
    private boolean running;
    private OnLocationUpdated callback;
    private Timer timer = new Timer();
    private String locationProvider;
    private Location currentLocation = null;
    private final Object lock = new Object();
    private LocationListener listener;

    public LocationNotifier(Context context) {
        this.context = context;
    }

    public void registerCallback(OnLocationUpdated callback) {
        if (callback != null) {
            this.callback = callback;
            activateLocationListener();
        }
    }

    public void activateLocationListener() {
        if (checkLocationPermission()) {
            if (registerLocationListener()) {
                running = true;
                timer.schedule(locationUpdateTask, NOTIFY_DELAY, NOTIFY_DELAY);
            }
        } else {
            notifyPermissionRequest();
        }
    }

    public void stop() {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (listener != null) {
            manager.removeUpdates(listener);
        }
        running = false;
    }

    private void notifyPermissionRequest() {
        callback.onUpdate(0, 0, STATUS_PROHIBITTED);
    }

    private boolean checkLocationPermission() {
        int locationPermission = ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION);
        return locationPermission == PackageManager.PERMISSION_GRANTED;
    }

    private void updateCurrentLocation(Location location) {
        synchronized (lock) {
            currentLocation = location;
        }
    }

    private Location popCurrentLocation() {
        synchronized (lock) {
            Location temp = currentLocation;
            currentLocation = null;
            return temp;
        }
    }

    private boolean registerLocationListener() {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        Criteria providerCriteria = new Criteria();
        providerCriteria.setAccuracy(Criteria.ACCURACY_FINE);
        providerCriteria.setPowerRequirement(Criteria.POWER_HIGH);

        locationProvider = manager.getBestProvider(providerCriteria, true);

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateCurrentLocation(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle bundle) {
                if (status == LocationProvider.OUT_OF_SERVICE ||
                        status == LocationProvider.TEMPORARILY_UNAVAILABLE) {
                    changeLocationProvider(this);
                }
            }

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {
                changeLocationProvider(this);
            }
        };

        if (locationProvider != null) {
            if (checkLocationPermission()) {
                manager.requestLocationUpdates(locationProvider, UPDATE_DELAY, MIN_DISTANCE, listener);
            } else {
                notifyPermissionRequest();
                return false;
            }
        }

        return true;
    }

    private void changeLocationProvider(LocationListener oldListener) {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        manager.removeUpdates(oldListener);
        registerLocationListener();
    }

    private TimerTask locationUpdateTask = new TimerTask() {
        @Override
        public void run() {
            if (running) {
                if (locationProvider == null) {
                    callback.onUpdate(0, 0, STATUS_NO_PROVIDER);
                    registerLocationListener();
                } else {
                    Location location = popCurrentLocation();
                    if (location != null) {
                        callback.onUpdate(location.getLatitude(), location.getLongitude(), STATUS_OK);
                    }
                }
            } else {
                timer.cancel();
            }
        }
    };
}
