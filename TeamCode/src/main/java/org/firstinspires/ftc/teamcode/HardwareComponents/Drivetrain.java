package org.firstinspires.ftc.teamcode.HardwareComponents;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Movement.AutonomousRoute;
import org.firstinspires.ftc.teamcode.HardwareComponents.Motors.EncodedMotor;
import org.firstinspires.ftc.teamcode.Movement.Route;

import static android.os.SystemClock.sleep;

public class Drivetrain {
    static final double ENCODER_TICKS = 1456;
    static final double GEAR_REDUCTION = 0.66;
    static final double WHEEL_DIAMETER = 5;
    static final double TICKS_PER_INCH = (ENCODER_TICKS * GEAR_REDUCTION) / (WHEEL_DIAMETER * Math.PI);
    static final int SMOOTHING_INTERVAL = 5;

    public final EncodedMotor leftDriveMotor;
    public final EncodedMotor rightDriveMotor;
    public double currentLeftPower, currentRightPower;

    private ElapsedTime runtime = new ElapsedTime();
    private double intervalCounter = 0;

    /**
     * drivetrain consists of left and right drive motors.
     * For accurate paths, ensure WHEEL_DIAMETER and GEAR_REDUCTION are
     * set correctly.
     * @param leftDrive DcMotor for left drive.
     * @param rightDrive DcMotor for right drive.
     * @param isDriverControl Whether autonomous or driver controlled.
     */
    public Drivetrain(DcMotor leftDrive, DcMotor rightDrive, boolean isDriverControl) {
        leftDriveMotor = new EncodedMotor(leftDrive);
        rightDriveMotor = new EncodedMotor(rightDrive);
        rightDriveMotor.setReverse();

        if (isDriverControl) {
            leftDriveMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rightDriveMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        } else {
            leftDriveMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightDriveMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftDriveMotor.minPower = 0.2;
            rightDriveMotor.minPower = 0.2;
        }
        currentLeftPower = currentRightPower = 0;
    }

    /**
     * drive a route defined by Autonomous Route.
     * pauseMS between each route as defined in AutonomousRoute.
     * Return once the path is complete or timeout has expired.
     * @param ar AutonomousRoute
     */
    public void driveRoute(AutonomousRoute ar) {
        for (Route r : ar.routeItems) {
            driveRoute(r);
            sleep(ar.pauseMS);
        }
    }

    /**
     * drive a route. Return once complete or timeout has expired.
     * @param route Route.
     */
    public void driveRoute(Route route) {
        runtime.reset();
        leftDriveMotor.setTarget((int) (route.leftIn * TICKS_PER_INCH));
        rightDriveMotor.setTarget((int) (route.rightIn * TICKS_PER_INCH));

        boolean leftPathComplete, rightPathComplete;
        leftPathComplete = rightPathComplete = false;

        while (!leftPathComplete && !rightPathComplete && runtime.seconds() < route.timeout)
        {
            leftPathComplete = leftDriveMotor.moveToTarget(route.speed);
            rightPathComplete = rightDriveMotor.moveToTarget(route.speed);
        }

        leftDriveMotor.stop();
        rightDriveMotor.stop();
    }

    /**
     * Accept power inputs directly for the left and right drive wheels.
     * @param left Left side power.
     * @param right Right side power.
     */
    public void drive(double left, double right) {
        // Average current and desired power to get a smoothing average.
        if (intervalCounter % SMOOTHING_INTERVAL == 0) {
            currentLeftPower = (currentLeftPower + currentLeftPower + left) / 3.0;
            currentRightPower = (currentRightPower + currentRightPower + right) / 3.0;

            leftDriveMotor.setPower(currentLeftPower);
            rightDriveMotor.setPower(currentRightPower);
        }
        intervalCounter++;
    }
}
