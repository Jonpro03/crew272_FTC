package org.firstinspires.ftc.teamcode.HardwareComponents;

import com.qualcomm.robotcore.hardware.CRServo;

public class ContinuousServo {
    protected final CRServo servo;
    public static final double STOP_POW = 0;

    public ContinuousServo(CRServo servo) {
        this.servo = servo;
        this.servo.setPower(STOP_POW);
    }

    public void moveForward(double speed) {
        double correctedSpeed = speed / 2.0;
        servo.setPower(speed);
    }

    public void moveBackwards(double speed) {
        double correctedSpeed = speed / 2.0;
        servo.setPower(-speed);
    }

    public void stop() {
        servo.setPower(STOP_POW);
    }

}
