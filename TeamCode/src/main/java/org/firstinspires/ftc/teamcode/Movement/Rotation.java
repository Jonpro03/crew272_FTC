package org.firstinspires.ftc.teamcode.Movement;

public class Rotation extends Route {

    static final double WHEELBASE_IN = 14.5;
    static final double WHEEL_SLIP_CORRECTION_FACTOR = 1.9;

    public Rotation(double angle, double speed, double timeout) {
        super(0, 0, speed, timeout);


        double arc = Math.toRadians(Math.abs(angle)) * (WHEELBASE_IN / 2) * WHEEL_SLIP_CORRECTION_FACTOR;

        leftIn = angle > 0 ? arc : -arc;
        rightIn = angle > 0 ? -arc : arc;
    }
}
