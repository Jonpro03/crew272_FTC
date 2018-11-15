package org.firstinspires.ftc.teamcode.HardwareComponents.Motors;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Movement.SmoothMovement;

public class BoundedMotor extends EncodedMotor {

    static final double LOWER_BOUND = 0;
    static final double UPPER_BOUND = 14400;

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
        this.limitSwitch.setMode(DigitalChannel.Mode.INPUT);
        //this.useSmooth = false; // Uncomment if smooth movement is not desired.
        this.motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    /**
     * Initialize the Bounded motor by retracting until the lower limit is found.
     * Will return once the motor has reached the lower limit.
     */
    public void init() {
        // Clockwise is extend. Counter-clockwise is retract.
        resetEncoder();
        boolean switchPressed = getSwitchState();

        if (switchPressed) { return; }

        // Lower the motor and check for the switch press.
        this.setTarget(-UPPER_BOUND);
        while (!switchPressed) {
            this.setPower(-1);
            switchPressed = getSwitchState();
        }
        resetEncoder();
    }

    @Override
    public void setTarget(double target) {
        this.tarPos = Range.clip(target, LOWER_BOUND, UPPER_BOUND);
        this.motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.smoothMove = new SmoothMovement(Math.abs(target));
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
        this.setTarget(UPPER_BOUND);

        if (!wait) {
            useSmooth = false;
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
        this.setTarget(LOWER_BOUND);

        if (!wait) {
            useSmooth = false;
            moveToTarget(1);
            return;
        }

        boolean pathComplete = false;
        while (!pathComplete) {
            pathComplete = moveToTarget(1);
        }
    }

    public boolean getSwitchState() {
        return limitSwitch.getState();
    }
}
