package com.example.mcfinal;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SensorManager sM;//sm- Sensor Manager variable
    private Sensor acc; //acc- accelerometer variable
    private TextView rText; //rText- respiratoryRateTextView1 variable
    private Button startButton1;
    private Button stopButton1;
    private long lTS; //lts- last time stamp variable
    private float lA; // lA- last acceleration variable
    private float peakThreshold = 1.5f; // Adjust this threshold based on your measurements
    private DatabaseHelper databaseHelper;
    private int breathCount = 0;
    private long startTime;
    private boolean isMeasuring = false;

    private TextView heartRateTextView;
    private Button startButton;

    private Button button;
    private Camera camera;
    private Camera.Parameters parameters;
    private boolean isMonitoring = false;
    private Handler handler;

    private static final int SIMULATION_DURATION = 8 * 60; // Simulate sleep monitoring for 8 hours
    private static final int GOOD_SLEEP_THRESHOLD = 12; // Adjust based on real-world criteria
    private static final int DISTURBED_SLEEP_THRESHOLD = 18; // Adjust based on real-world criteria

    private boolean isSleepMonitoring = false;

    public void startSleepMonitoring() {
        if (!isSleepMonitoring) {
            isSleepMonitoring = true;

            // Simulate respiratory rate monitoring every minute
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            executor.scheduleAtFixedRate(this::simulateRespiratoryRate, 0, 1, TimeUnit.MINUTES);

            // Simulate sleep monitoring duration
            try {
                Thread.sleep(SIMULATION_DURATION * 60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Stop sleep monitoring after simulation duration
            executor.shutdown();
            isSleepMonitoring = false;
        }
    }

    private void simulateRespiratoryRate() {
        // Simulate changes in respiratory rate over time
        int respiratoryRate = (int) (Math.random() * 10) + 10; // Simulate respiratory rate changes
        System.out.println("Respiratory Rate: " + respiratoryRate + " breaths per minute");

        // Classify sleep based on respiratory rate thresholds
        classifySleep(respiratoryRate);
    }

    private void classifySleep(int respiratoryRate) {
        if (respiratoryRate <= GOOD_SLEEP_THRESHOLD) {
            System.out.println("Good Sleep");
        } else if (respiratoryRate > GOOD_SLEEP_THRESHOLD && respiratoryRate <= DISTURBED_SLEEP_THRESHOLD) {
            System.out.println("Disturbed Sleep");
        } else {
            System.out.println("Very Disturbed Sleep");
        }
        classifySleep(respiratoryRate);

        // Insert sleep cycle into the database
        databaseHelper.insertSleepCycle("Disturbed Sleep");
    }
    protected void onDestroy1() {
        super.onDestroy();
        if (isMonitoring) {
            stopMonitoring();
        }

        // Close the database connection
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }

    private Runnable stopMeasurementRunnable = new Runnable() {
        @Override
        public void run() {
            stopMeasurement();
        }
    };
    private Runnable stopMonitoringRunnable = new Runnable() {
        @Override
        public void run() {
            stopMonitoring();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rText = findViewById(R.id.respiratoryRateTextView1);

        startButton1 = findViewById(R.id.startButton1);
        stopButton1 = findViewById(R.id.stopButton1);
        sM = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        acc = sM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Button openDirectionsButton = findViewById(R.id.openDirectionsButton);
        databaseHelper = new DatabaseHelper(this);
        MainActivity sleepMonitor = null;
        sleepMonitor.startSleepMonitoring();
        openDirectionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch the DirectionsActivity
                Intent intent = new Intent(MainActivity.this, DirectionsActivity.class);
                startActivity(intent);
            }
        });
        startButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMeasurement();
            }
        });
        stopButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopMeasurement();
            }
        });


        button = (Button) findViewById(R.id.button69);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity2();

            }
        });

        heartRateTextView = findViewById(R.id.heartRateTextView);
        startButton = findViewById(R.id.startButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isMonitoring) {
                    startMonitoring();
                    startButton.setText("Measure Heart rate again after 45s");
                } else {
                    stopMonitoring();
                    startButton.setText("Start Monitoring");
                }
            }
        });

        handler = new Handler(Looper.getMainLooper());
    }
    private final SensorEventListener accelerometerListener = new SensorEventListener() {
        @Override

        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                float acceleration = event.values[0] * event.values[0] +
                        event.values[1] * event.values[1] +
                        event.values[2] * event.values[2];

                // Calculate the time difference
                long currentTime = System.currentTimeMillis();
                long timeDifference = currentTime - lTS;

                // Check for a peak in acceleration
                if (acceleration > lA && acceleration > peakThreshold) {
                    // Calculate the time interval between peaks
                    if (timeDifference > 200 && timeDifference < 2000) {
                        breathCount++;
                    }
                }

                // Update the last values
                lTS = currentTime;
                lA = acceleration;

                // Calculate respiratory rate and update the UI
                long elapsedTime = currentTime - startTime;
                double respiratoryRate = (breathCount * 60000.0) / elapsedTime;

                rText.setText("Respiratory Rate: " + respiratoryRate + " breaths per minute");
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // Handle accuracy changes if needed
        }
    };

    private void startMeasurement() {
        // Register the accelerometer listener when measurement starts
        breathCount = 0;
        startTime = System.currentTimeMillis();
        sM.registerListener(accelerometerListener, acc, SensorManager.SENSOR_DELAY_NORMAL);
        isMeasuring = true;
        startButton1.setEnabled(false); // Disable the Start button during measurement
        stopButton1.setEnabled(true);   // Enable the Stop button during measurement

        // Schedule a runnable to automatically stop measurement after a certain duration (e.g., 60 seconds)
        handler.postDelayed(stopMeasurementRunnable, 45000);
    }

    private void stopMeasurement() {
        // Unregister the accelerometer listener when measurement stops
        sM.unregisterListener(accelerometerListener);
        isMeasuring = false;
        startButton1.setEnabled(true);  // Enable the Start button after measurement stops
        stopButton1.setEnabled(false); // Disable the Stop button after measurement stops

        // Remove the scheduled stopMeasurementRunnable
        handler.removeCallbacks(stopMeasurementRunnable);
    }

    public void openActivity2()
    {
        Intent intent= new Intent(this, Activity2.class);
        startActivity(intent);
    }
    private void startMonitoring() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            // in case the device does not have a flash
            return;
        }

        camera = Camera.open();
        parameters = camera.getParameters();

        // Enable flashlight
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(parameters);
        camera.startPreview();

        isMonitoring = true;

        // Simulate heart rate changes
        simulateHeartRate();
        handler.postDelayed(stopMonitoringRunnable, 45000);
    }
    // This function takes charge of starting the process of monitoring heart rate.
    //  Firstly it checks whether the device possesses a functionality. If not it terminates early since the flashlight feature is necessary, for this monitoring procedure.// Next it opens up the camera. Adjusts its settings to activate the flashlight or torch mode.
    //  The camera preview is initiated, resulting in the activation of the flashlight.
    // The flag "isMonitoring" is set to true indicating that the monitoring process is currently active.
    // Afterwards it invokes simulateHeartRate() to mimic changes, in heart rate and schedules the stopMonitoringRunnable to halt monitoring after 45 seconds.
    private void stopMonitoring() {
        if (camera != null) {
            // Turn off flashlight
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(parameters);
            camera.stopPreview();
            camera.release();
            camera = null;
        }

        isMonitoring = false;
    }

    private void simulateHeartRate() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isMonitoring) {
                    List<Long> b = new ArrayList<>();
                    List<Long> a = new ArrayList<>();

                    Bitmap[] frameList = new Bitmap[0];
                    for (Bitmap i : frameList) {
                        int redBucket = 0;
                        for (int y = 550; y < 650; y++) {
                            for (int x = 550; x < 650; x++) {
                                int c = i.getPixel(x, y);
                                int pixelCount=0;
                                pixelCount++;
                                redBucket += Color.red(c) + Color.blue(c) + Color.green(c);
                            }
                        }

                    }
                    for (int i = 0; i < a.size() - 5; i++) {
                        long temp = (a.get(i) + a.get(i + 1) + a.get(i + 2) + a.get(i + 3) + a.get(i + 4)) / 4;
                        b.add(temp);
                    }

                    long x = b.get(0);
                    int count = 0;

                    for (int i = 1; i < b.size() - 1; i++) {
                        long p = b.get(i);
                        if ((p - x) > 3500) {
                            count = count + 1;
                        }
                        x = b.get(i);
                    }

                    for (int i = 1; i < b.size() - 1; i++) {
                        long p = b.get(i);
                        if ((p - x) > 3500) {
                            count = count + 1;
                        }
                        x = b.get(i);
                    }

                    int rate = (int) ((count * 60.0) / 90);
                    updateHeartRateUI(rate);
                    databaseHelper.insertHeartRate(rate);
                    simulateHeartRate(); // proceed with the simulation
                }
            }
        }, 400); // Simulate the heart rate change for every 0.4 seconds
    }

    private void updateHeartRateUI(final int heartRate) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                heartRateTextView.setText("Heart Rate: " + heartRate + " BPM");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isMonitoring) {
            stopMonitoring();
        }
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}