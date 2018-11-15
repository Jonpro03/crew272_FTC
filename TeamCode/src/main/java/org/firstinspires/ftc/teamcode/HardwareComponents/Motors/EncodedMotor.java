package org.firstinspires.ftc.teamcode.HardwareComponents.Motors;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Movement.SmoothMovement;

public class EncodedMotor extends Motor {

    protected double tarPos = 0;
    protected SmoothMovement smoothMove;

    /**
     * Whether or not to use accel/decel curves.
     */
    public boolean useSmooth = true;

    /**
     * DcMotor with an Encoder
     * @param tetrixMotor DcMotor
     */
    public EncodedMotor(DcMotor tetrixMotor) {
        super(tetrixMotor);
        this.resetEncoder();
    }

    /**
     * Reset the encoders to 0 at the current position.
     */
    public void resetEncoder() {
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    /**
     * Set the target destination for the next movement.
     * @param target Number of encoder ticks to the next target.
     */
    public void setTarget(double target) {
        resetEncoder();
        tarPos = target + motor.getCurrentPosition();
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        smoothMove = new SmoothMovement(Math.abs(target));
    }

    /**
     * Power the motors to move to the target. Loop over this
     * method until the return value is true or a timeout has been
     * reached.
     * @param powerDecimal Amount of power between 0-1.
     * @return Whether target has been reached.
     */
    public boolean moveToTarget(double powerDecimal) {
        double power = powerDecimal *
                this.smoothMove.SmoothMoveFactor(this.motor.getCurrentPosition());
        this.setPower(useSmooth ? power : powerDecimal);
        return !this.motor.isBusy();
    }

    /**
     * Stop all movement and reset the encoders.
     */
    public void stop() {
        this.motor.setPower(0);
        resetEncoder();
    }
}