package org.firstinspires.ftc.teamcode.HardwareComponents;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class CompassSensor implements SensorEventListener {

    public interface CompassListener {
        void onNewAzimuth(float azimuth);
    }

    private CompassListener listener;

    private SensorManager sensorManager;
    private Sensor gravitySensor;
    private Sensor magneticSensor;

    private float[] gravityReadings = new float[3];
    private float[] magneticReadings = new float[3];
    private float[] Rotation = new float[9];
    private float[] Incline = new float[9];

    private float azimuth;

    public CompassSensor(Context context) {
        sensorManager = (SensorManager) context
                .getSystemService(Context.SENSOR_SERVICE);
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    public void start() {
        sensorManager.registerListener(this, gravitySensor,
                SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, magneticSensor,
                SensorManager.SENSOR_DELAY_GAME);
    }

    public void stop() {
        sensorManager.unregisterListener(this);
    }


    public void setListener(CompassListener l) {
        listener = l;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        final float alpha = 0.97f;

        synchronized (this) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

                gravityReadings[0] = alpha * gravityReadings[0] + (1 - alpha)
                        * event.values[0];
                gravityReadings[1] = alpha * gravityReadings[1] + (1 - alpha)
                        * event.values[1];
                gravityReadings[2] = alpha * gravityReadings[2] + (1 - alpha)
                        * event.values[2];
            }

            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {

                magneticReadings[0] = alpha * magneticReadings[0] + (1 - alpha)
                        * event.values[0];
                magneticReadings[1] = alpha * magneticReadings[1] + (1 - alpha)
                        * event.values[1];
                magneticReadings[2] = alpha * magneticReadings[2] + (1 - alpha)
                        * event.values[2];

            }

            boolean success = SensorManager.getRotationMatrix(Rotation, Incline, gravityReadings,
                    magneticReadings);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(Rotation, orientation);
                azimuth = (float) Math.toDegrees(orientation[0]); // orientation

                if (listener != null) {
                    listener.onNewAzimuth(azimuth);
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // We don't really care
    }
}