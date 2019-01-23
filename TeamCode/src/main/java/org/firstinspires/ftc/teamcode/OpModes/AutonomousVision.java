package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.HardwareComponents.Camera.CameraNavTargets;
import org.firstinspires.ftc.teamcode.HardwareComponents.Camera.CameraOreDetection;
import org.firstinspires.ftc.teamcode.HardwareComponents.Camera.VuforiaCam;
import org.firstinspires.ftc.teamcode.Movement.Models.Point2D;
import org.firstinspires.ftc.teamcode.Movement.Models.Rotation;
import org.firstinspires.ftc.teamcode.Movement.Models.StraightRoute;
import org.firstinspires.ftc.teamcode.Movement.Positioning;
import org.firstinspires.ftc.teamcode.Movement.Models.PolarCoordinate;
import org.firstinspires.ftc.teamcode.Robot;


@Autonomous(name="Autonomous: Vision", group="Autonomous")
public class AutonomousVision extends LinearOpMode {
    private Robot robot;
    private Point2D alignmentTargetCoords;
    private double alignmentTargetHeading;
    private boolean isCraterSide = false;

    public AutonomousVision() {
        msStuckDetectInit = 18000;
    }
    VuforiaCam vuCam;

    static final int ARENA_RADIUS = 72;
    static final int WALL_DISTANCE = 18;
    static final double TURN_SPEED = 0.2;
    static final double HALF_SPEED = 0.5;

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

        // Descend
        robot.screwLift.extend(true);
        sleep(100);

        // Detach
        robot.latch.open();
        sleep(1000);

        // Retract screw lift
        robot.screwLift.retract(false);

        int goldPos = Positioning.UNKNOWN;

        // Try up to 3 times to sample the ores
        for (int i=0; i<3; i++) {
            sleep(500);
            goldPos = oreDetect.determineGoldPosition(telemetry);
            if (goldPos != Positioning.UNKNOWN) { break; }
        }

        if (goldPos != Positioning.UNKNOWN) {
            SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, robot.soundId);
        }

        // Pull away from the lander
        robot.drivetrain.driveRoute(new StraightRoute(2, HALF_SPEED, 2));

        // Approach the gold ore
        /** Geometry
         * The distance (b) from the robot to the center ore is 34".
         * The distance (a) between ores is 14.5".
         * The distance (c) to an ore on the side is c^2 = a^2+b^2, c = 36.9".
         * The angle to drive to an ore (left/right) is angle A. sin(A)=a/c, A = 23.1 degrees.
        **/
        double a = 14.5;
        double b = 34;
        double c = Math.sqrt((a*a) + (b*b));
        double A = Math.toDegrees(Math.atan2(a, b));
        switch(goldPos) {
            case Positioning.LEFT: {
                robot.drivetrain.rotate(new Rotation(-A, TURN_SPEED, 1));
                robot.drivetrain.driveRoute(new StraightRoute(c, HALF_SPEED, 3));
                break;
            }
            case Positioning.STRAIGHT:
            case Positioning.UNKNOWN: {
                robot.drivetrain.driveRoute(new StraightRoute(b, HALF_SPEED, 3));
                break;
            }
            case Positioning.RIGHT: {
                robot.drivetrain.rotate(new Rotation(A, TURN_SPEED, 1));
                robot.drivetrain.driveRoute(new StraightRoute(c, HALF_SPEED, 3));
                break;
            }
        }

        // Pick up the ore
        robot.twisty.moveForward(1);
        //robot.scoop.open();
        sleep(700);
        robot.twisty.stop();
        //robot.scoop.close();

        // Retreat to previous position
        switch(goldPos) {
            case Positioning.LEFT: {
                robot.drivetrain.driveRoute(new StraightRoute(-c, HALF_SPEED, 3));
                robot.drivetrain.rotate(new Rotation(A, TURN_SPEED, 1));
                break;
            }
            case Positioning.STRAIGHT:
            case Positioning.UNKNOWN:{
                robot.drivetrain.driveRoute(new StraightRoute(-b, HALF_SPEED, 3));
                break;
            }
            case Positioning.RIGHT: {
                robot.drivetrain.driveRoute(new StraightRoute(-c, HALF_SPEED, 3));
                robot.drivetrain.rotate(new Rotation(-A, TURN_SPEED, 1));
                break;
            }
        }

        // Navigate to the VuMark
        robot.drivetrain.driveRoute(new StraightRoute(20, HALF_SPEED, 4));

        robot.drivetrain.rotate(new Rotation(-90, 0.3, 4));

        robot.drivetrain.driveRoute(new StraightRoute(24, 1, 4));

        robot.drivetrain.rotate(new Rotation(10, TURN_SPEED, 4));

        // Try up to 3 times to find the VuMark
        CameraNavTargets navTargets = new CameraNavTargets(vuCam);
        boolean targetFound = false;

        for (int i=0; i<3; i++) {
            targetFound = navTargets.check(telemetry);
            if (targetFound) {
                break;
            } else {
                sleep(500);
            }
        }

        // If we couldn't find the vumark, stop here so we don't end up flipping the robot.
        if (!targetFound) {
            telemetry.addLine("Unable to find VuMark. Parking here.");
            telemetry.update();
            sleep(3000); // Let the message show for a bit.
            return;
        }

        switch (navTargets.lastVuMarkName) {
            case VuforiaCam.BACK_WALL_VUMARK_NAME:
            {
                isCraterSide = false;
                alignmentTargetCoords = new Point2D(ARENA_RADIUS-WALL_DISTANCE, 0);
                alignmentTargetHeading = 90;
                break;
            }
            case VuforiaCam.BLUE_WALL_VUMARK_NAME:
            {
                isCraterSide = true;
                alignmentTargetCoords = new Point2D(0, ARENA_RADIUS-WALL_DISTANCE);
                alignmentTargetHeading = 0;
                return;
            }
            case VuforiaCam.FRONT_WALL_VUMARK_NAME:
            {
                isCraterSide = false;
                alignmentTargetCoords = new Point2D(-ARENA_RADIUS+WALL_DISTANCE, 0);
                alignmentTargetHeading = 90;
                return;
            }
            case VuforiaCam.RED_WALL_VUMARK_NAME:
            {
                isCraterSide = true;
                alignmentTargetCoords = new Point2D(0, -ARENA_RADIUS+WALL_DISTANCE);
                alignmentTargetHeading = 180;
                break;
            }
            default:
            {
                telemetry.addLine("Unable to find VuMark. Parking here.");
                telemetry.update();
                sleep(3000); // Let the message show for a bit.
                return;
            }

        }

        PolarCoordinate destination = Positioning.calcPolarCoordinate(navTargets.lastKnownLocation, alignmentTargetCoords);
        double newHeading = navTargets.lastKnownLocation.heading + destination.angle ;

        // Print out debug stuff to screen.
        telemetry.addLine("x: " + navTargets.lastKnownLocation.position.x);
        telemetry.addLine("y: " + navTargets.lastKnownLocation.position.y);
        telemetry.addLine("r: " + navTargets.lastKnownLocation.heading);
        telemetry.addLine("l: " + destination.length);
        telemetry.addLine("a: " + destination.angle);
        telemetry.addLine("h: " + newHeading);
        telemetry.addLine(isCraterSide ? "Crater Side" : "Depot Side");
        telemetry.update();

        // Move to destination.
        robot.drivetrain.rotate(new Rotation(destination.angle, TURN_SPEED, 2));
        robot.drivetrain.driveRoute(new StraightRoute(destination.length, HALF_SPEED, 3));

        // Turn to face the vumark.
        double rot = alignmentTargetHeading - newHeading - 10;
        robot.drivetrain.rotate(new Rotation(rot, 0.1, 2));

        double adjustment = 0;

        /** Uncomment if we need to line up again. For the remainder of the journey, there are no vumarks in sight.
        // Try up to 3 times to find the VuMark
        targetFound = false;

        for (int i=0; i<3; i++) {
            targetFound = navTargets.check(telemetry);
            if (targetFound) {
                break;
            } else {
                sleep(500);
            }
        }

        // If we couldn't find the vumark, stop here so we don't end up flipping the robot.
        if (!targetFound) {
            telemetry.addLine("Unable to find VuMark. Parking here.");
            telemetry.update();
            sleep(3000); // Let the message show for a bit.
            return;
        }

        // See how close we are to our target heading and adjust the next turn.
         adjustment = alignmentTargetHeading - navTargets.lastKnownLocation.heading;
         **/

        // Turn to drive to depot.
        if (isCraterSide) {
            robot.drivetrain.rotate(new Rotation(-90 + adjustment, 0.1, 4));
        } else {
            robot.drivetrain.rotate(new Rotation(90 + adjustment, 0.1, 4));
        }

        // Drive to depot.
        robot.drivetrain.driveRoute(new StraightRoute(ARENA_RADIUS - 24, 1, 5)); // Drive up to 12" away from the wall.

        if (isCraterSide){
            robot.drivetrain.rotate(new Rotation(-45, 0.1, 3));
            robot.drivetrain.driveRoute(new StraightRoute(12, 0.8, 2));
            robot.drivetrain.rotate(new Rotation(160, 0.5, 8));

        } else {
            robot.drivetrain.rotate(new Rotation(30, TURN_SPEED, 2));
        }
        // Rotate to drop the marker.


        // Drop the mineral we're (hopefully) holding.
        robot.twisty.moveBackwards(1);

        // Drop it
        robot.markerArm.open();
        sleep(1000);
        robot.markerArm.close();

        // Straighten out
        robot.drivetrain.rotate(new Rotation(-30, TURN_SPEED, 2));
        robot.twisty.stop();

        // Extra push to make sure things are in the depot.
        robot.drivetrain.driveRoute(new StraightRoute(-8, 0.8, 2));
        robot.drivetrain.driveRoute(new StraightRoute(8, HALF_SPEED, 2));

        // Drive into crater
        //robot.drivetrain.driveRoute(new StraightRoute(-128, 0.8, 8));

    }
}

