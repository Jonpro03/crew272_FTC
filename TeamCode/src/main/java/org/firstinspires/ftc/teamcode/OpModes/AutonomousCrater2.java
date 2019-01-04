package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.teamcode.Movement.Rotation;
import org.firstinspires.ftc.teamcode.Movement.StraightRoute;
import org.firstinspires.ftc.teamcode.Robot;


@Autonomous(name="Autonomous: Crater2", group="Autonomous")
public class AutonomousCrater2 extends LinearOpMode {
    private Robot robot;
    private boolean goldFound = false;

    public AutonomousCrater2() {
        msStuckDetectInit = 18000;
    }

    public void initialize() {
        robot = new Robot(hardwareMap, false);
        robot.init();
        robot.drivetrain.leftDriveMotor.useSmooth = false;
        robot.drivetrain.rightDriveMotor.useSmooth = false;
    }

    @Override
    public void runOpMode() {
        initialize();
        waitForStart();

        //robot.screwLift.extend();
        // Now we want to go half way down and wait a few seconds for the robot to line itself up
        robot.screwLift.setTarget(1300);
        while (!robot.screwLift.moveToTarget(0.3)) // Move slowly to reduce the shock of the descent
        {
            telemetry.addData("Encoder Pos", robot.screwLift.motor.getCurrentPosition());
        }
        sleep(2000); // wait for the robot to line itself up

        // Finish going down the rest of the way
        robot.screwLift.extend(true);

        // Detach
        robot.latch.open();
        sleep(1000);

        // Tell the screw lift to retract and take off toward the first ore.
        robot.screwLift.retract(false);
        robot.drivetrain.driveRoute(new StraightRoute(42, 0.7, 4));
    }

    private void checkGoldOre() {

    }
}
