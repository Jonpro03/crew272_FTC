package org.firstinspires.ftc.teamcode.Movement;

public class SmoothMovement {
    static final int RAMP_UP_DURATION = 1440;
    static final int RAMP_DOWN_DURATION = 1440;

    private final double totalDuration;

    public SmoothMovement(double duration) {
        totalDuration = duration;
    }

    /**
     * Calculate and return a smoothing factor for accel/decel.
     *
     * @param curPos
     * @return
     */
    public double SmoothMoveFactor(double curPos) {
        double factor = 1;
        double position = Math.abs(curPos);

        if(position <= RAMP_UP_DURATION) {
            factor = position / RAMP_UP_DURATION;
        }

        double rampDownPoint = totalDuration - RAMP_DOWN_DURATION;
        if (position > rampDownPoint) {
            double curPosInRampDown = position - rampDownPoint;
            factor = RAMP_DOWN_DURATION / curPosInRampDown;
        }

        return factor;
    }
}
