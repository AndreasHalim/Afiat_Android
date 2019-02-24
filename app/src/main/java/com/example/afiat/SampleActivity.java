package com.example.afiat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.afiat.location.LocationNotifier;
import com.example.afiat.service.OnSensorChangeListener;
import com.example.afiat.service.ServiceRunner;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class SampleActivity extends AppCompatActivity {
    private static final int REQUEST_ACCESS_FINE_LOCATION = 99;
    private static final int MESSAGE_LOCATION_UPDATE = 10;

    private TextView tvStep;
    private TextView tvDistance;
    private TextView tvSpeed;
    private TextView tvLatitude;
    private TextView tvLongitude;
    private ServiceRunner runner;
    private boolean isRequestingPermission = false;
    private ServiceHandler mHandler;

    private class ServiceHandler extends Handler {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if (msg.what == MESSAGE_LOCATION_UPDATE) {
                double latitude = msg.getData().getDouble("latitude");
                double longitude = msg.getData().getDouble("longitude");

                tvLatitude.setText(String.valueOf(latitude));
                tvLongitude.setText(String.valueOf(longitude));

                Toast.makeText(SampleActivity.this, "Location Updated!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank_activty);

        tvStep      = findViewById(R.id.tv_step);
        tvDistance  = findViewById(R.id.tv_distance);
        tvSpeed     = findViewById(R.id.tv_speed);
        tvLatitude  = findViewById(R.id.tv_latitude);
        tvLongitude = findViewById(R.id.tv_longitude);

        tvStep.setText("0");
        tvDistance.setText("0");
        tvSpeed.setText("0");
        tvLatitude.setText("-");
        tvLongitude.setText("-");

        showFCMToken();
    }

    @Override
    public void onRequestPermissionsResult(int rc, @NonNull String permissions[], @NonNull int[] results) {
        switch (rc) {
            case REQUEST_ACCESS_FINE_LOCATION: {
                if (results.length > 0 && results[0] == PackageManager.PERMISSION_GRANTED) {
                    runner.getService().activateLocationListener();
                } else {
                    Toast.makeText(this, "Location service is not activated.", Toast.LENGTH_SHORT).show();
                }
                isRequestingPermission = false;
            }
        }
    }

    private void showFCMToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
            .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                @Override
                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                    if (!task.isSuccessful()) {
                        Log.w("AFIAT", "getInstanceId failed", task.getException());
                        return;
                    }

                    if (task.getResult() != null) {
                        String token = task.getResult().getToken();
                        Log.i("AFIAT", "Token " + token);
                    } else {
                        Log.e("AFIAT", "Got null result....");
                    }
                }
            });
    }

    private void setupSensorService() {
        /*
         * Use code below in any activity which display the data collected from sensor
         * 1. Define the OnSensorChangeListener
         * 2. Instantiate ServiceRunner object
         * 3. Call runner.bind()
         * 4. Don't forget to call runner.unbind() in OnDestroy activity's method
         * 5. Done. It is easy!
         */
        final OnSensorChangeListener mCallback = new OnSensorChangeListener() {
            @Override
            public void stepsChanged(int value) {
                tvStep.setText(String.valueOf(value));
            }

            @Override
            public void distanceChanged(float value) {
                tvDistance.setText(String.valueOf(value));
            }

            @Override
            public void speedChanged(float value) {
                tvSpeed.setText(String.valueOf(value));
            }

            @Override
            public void onLightChange(float lightLevel) {
                Log.i("AFIAT", "current light level is " + String.valueOf(lightLevel));
            }

            @Override
            public void onGravityChange(float x, float y, float z) {
                Log.i("AFIAT", "current gravity is " +
                        String.valueOf(x) + " " +
                        String.valueOf(y) + " " +
                        String.valueOf(z));
            }

            @Override
            public void locationUpdated(double latitude, double longitude, short status) {
                if (status == LocationNotifier.STATUS_PROHIBITTED) {
                    if (!isRequestingPermission) {
                        isRequestingPermission = true;
                        requestLocationPermissison();
                    }
                } else if (status == LocationNotifier.STATUS_OK) {
                    if (mHandler != null) {
                        Bundle bundle = new Bundle();
                        bundle.putDouble("latitude", latitude);
                        bundle.putDouble("longitude", longitude);

                        Message message = new Message();
                        message.what = MESSAGE_LOCATION_UPDATE;
                        message.setData(bundle);

                        mHandler.sendMessage(message);
                    }
                } else {
                    Log.i("AFIAT", "Location: no provider");
                }
            }
        };

        runner = new ServiceRunner(this, mCallback);
        runner.bind();
    }

    private void requestLocationPermissison() {
        ActivityCompat.requestPermissions(
                SampleActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_ACCESS_FINE_LOCATION
        );
    }

    @Override
    protected void onResume() {
        setupSensorService();
        mHandler = new ServiceHandler();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mHandler = null;
        runner.unbind();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
