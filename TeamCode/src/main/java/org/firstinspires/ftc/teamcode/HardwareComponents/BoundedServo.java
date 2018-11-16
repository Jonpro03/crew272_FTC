package org.firstinspires.ftc.teamcode.HardwareComponents;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class BoundedServo {

    public double upperLimit = 0.9;
    public double lowerLimit = 0.1;
    public double initPos = 0.5;

    public double closePos = 0.2;
    public double openPos = 0.7;

    protected final Servo gripperServo;


    /**
     * Server that has upper and lower limits.
     * @param servo Servo
     */
    public BoundedServo(Servo servo) {

        gripperServo = servo;
    }

    /**
     * Initialize the servo and set it's position to initPos.
     */
    public void init() {
        gripperServo.setPosition(initPos);
    }

    /**
     * Move the servo to the close position.
     */
    public void close() {
        gripperServo.setPosition(closePos);
    }

    /**
     * Move the servo to the open position.
     */
    public void open() {
        gripperServo.setPosition(openPos);
    }

    /**
     * Move the servo to the defined position.
     * @param pos Position between 0-1.
     */
    public void setPosition(double pos) {
        gripperServo.setPosition(Range.clip(pos, lowerLimit, upperLimit));
    }

    /**
     * Return the current position of the servo.
     * @return Servo position.
     */
    public double getPosition() {
        return gripperServo.getPosition();
    }

    /**
     * Set the servo to run in reverse.
     */
    public void setReverse() {
        gripperServo.setDirection(Servo.Direction.REVERSE);
    }
}
