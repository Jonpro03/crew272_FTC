package org.firstinspires.ftc.teamcode.Movement.Models;

import org.firstinspires.ftc.teamcode.Movement.Models.Route;

public class Rotation extends Route {

    static final double WHEELBASE_IN = 14;
    static final double WHEEL_SLIP_CORRECTION_FACTOR = 1.87;
    static final double RIGHT_TURN_CORRECTION = 0.98 ;

    public Rotation(double angle, double speed, double timeout) {
        super(0, 0, speed, timeout);

        double arc = Math.toRadians(Math.abs(angle)) * (WHEELBASE_IN / 2) * WHEEL_SLIP_CORRECTION_FACTOR;

        if (angle > 0 && angle < 180) {
            arc *= RIGHT_TURN_CORRECTION;
        }

        leftIn = angle > 0 ? arc : -arc;
        rightIn = angle > 0 ? -arc : arc;
    }
}
