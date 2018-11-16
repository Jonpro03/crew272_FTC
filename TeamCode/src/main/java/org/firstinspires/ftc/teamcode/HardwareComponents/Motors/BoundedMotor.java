package org.firstinspires.ftc.teamcode.HardwareComponents.Motors;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.util.Range;

public class BoundedMotor extends EncodedMotor {

    static final int LOWER_BOUND = 0;
    static final int UPPER_BOUND = 32768;

    private final DigitalChannel limitSwitch;

    /**
     * Encoded motor that has a lower bound set by a limit switch, and
     * an upper bound defined as a number of encoder ticks past the lower.
     * @param tetrixMotor DcMotor.
     * @param limitSwitch DigitialChannel for the limit switch.
     */
    public BoundedMotor(DcMotor tetrixMotor, DigitalChannel limitSwitch) {
        super(tetrixMotor);
        this.limitSwitch = limitSwitch;
        limitSwitch.setMode(DigitalChannel.Mode.INPUT);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    /**
     * Initialize the Bounded motor by retracting until the lower limit is found.
     * Will return once the motor has reached the lower limit.
     */
    public void init() {
        resetEncoder();
        useSmooth = false;
        boolean switchPressed = getSwitchPressed();

        if (switchPressed) { return; }

        // Lower the motor and check for the switch press.
        motor.setTargetPosition(-UPPER_BOUND);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        moveToTarget(0.8);
        while (!switchPressed) {
            switchPressed = getSwitchPressed();
        }
        stop();
        resetEncoder();
        useSmooth = true;
    }

    @Override
    public void setTarget(int target) {
        int targetOffset = Range.clip(target, LOWER_BOUND, UPPER_BOUND);
        // Sanity check that the target needs to be set.
        if (motor.getTargetPosition() == targetOffset) { return; }
        motor.setTargetPosition(targetOffset);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
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
        setTarget(UPPER_BOUND);

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

    public boolean getSwitchPressed() {
        return !limitSwitch.getState();
    }
}
