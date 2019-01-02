package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.teamcode.HardwareComponents.Camera.CameraNavTargets;
import org.firstinspires.ftc.teamcode.HardwareComponents.Camera.CameraOreDetection;
import org.firstinspires.ftc.teamcode.HardwareComponents.Camera.VuforiaCam;
import org.firstinspires.ftc.teamcode.Movement.Models.Point2D;
import org.firstinspires.ftc.teamcode.Movement.Rotation;
import org.firstinspires.ftc.teamcode.Movement.StraightRoute;
import org.firstinspires.ftc.teamcode.Movement.Utility;
import org.firstinspires.ftc.teamcode.Movement.Models.PolarCoordinate;
import org.firstinspires.ftc.teamcode.Robot;


@Autonomous(name="Autonomous: Crater Vision", group="Autonomous")
public class AutonomousCraterVision extends LinearOpMode {
    private Robot robot;
    private boolean goldFound = false;


    public AutonomousCraterVision() {
        msStuckDetectInit = 18000;
    }
    VuforiaCam vuCam;

    public void initialize() {
        robot = new Robot(hardwareMap, false);
        robot.init();
        robot.drivetrain.leftDriveMotor.useSmooth = false;
        robot.drivetrain.rightDriveMotor.useSmooth = false;

        vuCam = new VuforiaCam(hardwareMap);
    }

    @Override
    public void runOpMode() {
        initialize();
        waitForStart();
        CameraOreDetection oreDetect = new CameraOreDetection(hardwareMap, vuCam.vuforia);

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

        robot.drivetrain.driveRoute(new StraightRoute(8,8, 2));
        robot.drivetrain.driveRoute(new Rotation(30, 0.5, 1));

        // Rotate Left and check the camera to see if a gold ore appears.
        // Todo: Add fault timer.
        while (!oreDetect.seeGold(telemetry)) {
            robot.drivetrain.drive(-0.3, 0.3);
        }
        robot.drivetrain.drive(0, 0);

        // We see an ore, so now we just need to line up with it.
        int correctionDirection = Utility.LEFT;
        // Todo: add fault timer.
        while (correctionDirection != Utility.STRAIGHT) {
            correctionDirection = oreDetect.goldDirection();
            robot.drivetrain.drive(0.3 * correctionDirection, -0.3 * correctionDirection);
        }

        // Drive forward to hit the ore.
        robot.drivetrain.driveRoute(new StraightRoute(12, 0.6, 2));
        robot.drivetrain.driveRoute(new StraightRoute(-12, 0.6, 2));

        robot.drivetrain.driveRoute(new Rotation(-45, 0.5, 2));

        // red is -45
        // blue 12 135

        // red target location = 0, -48
        double targetX = 0;
        double targetY = -48;

        Point2D target = new Point2D(0, -48);

        // use the camera to straighten out.
        CameraNavTargets navTargets = new CameraNavTargets(vuCam);
        // Try up to 3 times to find the vumark target.
        boolean targetFound = false;
        for (int i=0; i<3; i++) {
            targetFound = navTargets.check(telemetry);
            if (targetFound) {
                break;
            } else {
                sleep(500);
            }
        }

        if (!targetFound) {
            // We don't know where we are, so just stop.
            telemetry.addLine("Unable to find a target");
            telemetry.update();
            return;
        }

        PolarCoordinate destination = Utility.calcPolarCoordinate(navTargets.lastKnownLocation, target);

        // Rotate to point at destination
        robot.drivetrain.driveRoute(new Rotation(destination.angle, 0.4, 2));

        // Drive the length of the hypotenuse
        robot.drivetrain.driveRoute(new StraightRoute(destination.length, 0.8, 4));

        // Turn and look at the vumark. See how close we were
        robot.drivetrain.driveRoute(new Rotation(45, 0.6, 2));

        // Team marker is on the left side of the robot now.
        // Turn right 90* so that we drop it into the depot,
        // then turn back before going to the crater.
        robot.drivetrain.driveRoute(new Rotation(82.5, 0.3, 3));
        robot.markerArm.open();
        sleep(1000);
        robot.markerArm.close();
        robot.drivetrain.driveRoute(new Rotation(82.5, 0.3, 3));

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
