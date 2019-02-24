package com.example.afiat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.example.afiat.service.OnSensorChangeListener;
import com.example.afiat.service.ServiceRunner;

public class SampleActivity extends AppCompatActivity {
    private TextView tvStep;
    private TextView tvDistance;
    private TextView tvSpeed;
    private ServiceRunner runner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank_activty);

        tvStep      = findViewById(R.id.tv_step);
        tvDistance  = findViewById(R.id.tv_distance);
        tvSpeed     = findViewById(R.id.tv_speed);

        tvStep.setText("0");
        tvDistance.setText("0");
        tvSpeed.setText("0");

        /*
         * Use code below in any activity which display the data collected from sensor
         * 1. Define the OnSensorChangeListener
         * 2. Instantiate ServiceRunner object
         * 3. Call runner.bind()
         * 4. Don't forget to call runner.unbind() in OnDestroy activity's method
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
        };

        runner = new ServiceRunner(this, mCallback);
        runner.bind();
    }

    @Override
    protected void onDestroy() {
        runner.unbind();
        super.onDestroy();
    }
}
