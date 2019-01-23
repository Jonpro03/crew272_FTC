package org.firstinspires.ftc.teamcode.HardwareComponents;

public class CompassReading implements CompassSensor.CompassListener {
    private static final CompassReading instance = new CompassReading();

    public static CompassReading getInstance() {
        return instance;
    }

    private CompassReading() { }

    private CompassSensor sensor = null;
    private Float compassDegrees = null;

    public void setCompassSensor(CompassSensor sensor) {
        if (this.sensor == null) {
            this.sensor = sensor;
            sensor.setListener(this);
        }
    }

    protected Float getReading() {
        if (this.sensor == null) {
            return null;
        }
        return compassDegrees;
    }

    @Override
    public void onNewAzimuth(float azimuth) {
        this.compassDegrees = azimuth;
    }
}
