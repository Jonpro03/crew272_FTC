package org.firstinspires.ftc.teamcode.HardwareComponents.Motors;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.util.Range;

public class LimitedMotor extends EncodedMotor {

    static final int LOWER_BOUND = 0;
    public int upperBound;


    /**
     * Encoded motor that has a lower bound set by a limit switch, and
     * an upper bound defined as a number of encoder ticks past the lower.
     * @param tetrixMotor DcMotor.
     */
    public LimitedMotor(DcMotor tetrixMotor, int upperBound) {
        super(tetrixMotor);
        this.upperBound = upperBound;
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    /**
     * Initialize the Bounded motor by retracting until the lower limit is found.
     *
     * Will return once the motor has reached the lower limit.
     */
    public void init() {
        //resetEncoder();
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        useSmooth = false;

    }

    public void move(double power) {
        if (power > 0) {
            if (motor.getCurrentPosition() < upperBound) {
                motor.setPower(power);
            }
        } else if (power < 0) {
            if (motor.getCurrentPosition() >= LOWER_BOUND) {
                motor.setPower(power);
            }
        }
        else {
            motor.setPower(0);
        }
    }

    @Override
    public void setTarget(int target) {
        int targetOffset = Range.clip(target, LOWER_BOUND, upperBound);
        // Sanity check that the target needs to be set.
        if (motor.getTargetPosition() == targetOffset) { return; }
        motor.setTargetPosition(targetOffset);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    /**
     * Run the motor to its upper bound. Return when complete.
     */
    public void extend() {
        extend(true);
    }

    /**
     * Run the motor to its upper bound.
     * @param wait Whether to return when complete or immediately.
     */
    public void extend(boolean wait) {
        setTarget(upperBound);

        if (!wait) {
            moveToTarget(1);
            return;
        }

        boolean pathComplete = false;
        while (!pathComplete) {
            pathComplete = moveToTarget(1);
        }
    }

    /**
     * Run the motor to its lower bound. Return when complete.
     */
    public void retract() {
        extend(true);
    }

    /**
     * Run the motor to its lower bound.
     * @param wait Whether to return when complete or immediately.
     */
    public void retract(boolean wait) {
        setTarget(LOWER_BOUND);

        if (!wait) {
            moveToTarget(1);
            return;
        }

        boolean pathComplete = false;
        while (!pathComplete) {
            pathComplete = moveToTarget(1);
        }
    }
}
