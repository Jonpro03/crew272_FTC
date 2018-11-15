package org.firstinspires.ftc.teamcode.HardwareComponents.Motors;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

public class Motor {

    protected final DcMotor motor;
    public double minPower = 0;

    /**
     * Base DcMotor class.
     * @param motor DcMotor
     */
    public Motor(DcMotor motor) {
        this.motor = motor;
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    public void setMode(DcMotor.RunMode mode) {
        motor.setMode(mode);
    }

    /**
     * Set the power level. Inputs exceeding minPower or 1.0 will be trimmed.
     * @param powerTarget
     */
    public void setPower(double powerTarget) {
        double pow = Range.clip(powerTarget, this.minPower,1.0);
        this.motor.setPower(pow/100);
    }

    public double getPower() {
        return motor.getPower();
    }

    /**
     * Set the motor to run in reverse mode.
     */
    public void setReverse(){
        this.motor.setDirection(DcMotorSimple.Direction.REVERSE);
    }
}
