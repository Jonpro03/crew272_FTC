package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.teamcode.Movement.Rotation;
import org.firstinspires.ftc.teamcode.Robot;


@Autonomous(name="Autonomous: Depot", group="Autonomous")
public class AutonomousDepot1 extends LinearOpMode {
    private Robot robot;
    private boolean goldFound = false;

    public AutonomousDepot1() {
        msStuckDetectInit = 15000;
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
        robot.screwLift.setTarget(robot.screwLift.upperBound / 2);
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
        robot.drivetrain.driveRoute(Routes.DEPARTURE);
        checkGoldOre();
        robot.drivetrain.driveRoute(Routes.MOVE_TO_NEXT_ORE);
        if (!goldFound)
            checkGoldOre();
        robot.drivetrain.driveRoute(Routes.MOVE_TO_NEXT_ORE);
        if (!goldFound)
            checkGoldOre();

        // Move from the last ore to the depot.
        robot.drivetrain.driveRoute(Routes.DEPOT_SIDE_ORE_TO_DEPOT);

        // Team marker is on the left side of the robot now.
        // Turn right 90* so that we drop it into the depot,
        // then turn back before going to the crater.
        robot.drivetrain.driveRoute(new Rotation(85, 0.5, 2));
        robot.markerArm.open();
        sleep(1000);
        robot.markerArm.close();
        robot.drivetrain.driveRoute(new Rotation(85, 0.5, 2));

        // We've dropped our team marker, back straight up into the crater.
        robot.drivetrain.driveRoute(Routes.DRIVE_TO_CRATER);
    }

    private void checkGoldOre() {
        robot.colorArm.open();
        sleep(500);
        NormalizedRGBA colors;
        colors = robot.colorSensor.getNormalizedColors();
        goldFound = colors.blue < colors.red && colors.blue < colors.green;
        if (goldFound)
            robot.drivetrain.driveRoute(Routes.ORE_FOUND);
        robot.colorArm.close();
    }
}
