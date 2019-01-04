package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.teamcode.Movement.Rotation;
import org.firstinspires.ftc.teamcode.Movement.Route;
import org.firstinspires.ftc.teamcode.Movement.StraightRoute;
import org.firstinspires.ftc.teamcode.Robot;


@Autonomous(name="Autonomous: EasterEgg", group="Test")
public class Roll extends LinearOpMode {
    private Robot robot;
    private boolean goldFound = false;

    public void initialize() {
        robot = new Robot(hardwareMap, false);
        robot.init();
    }

    @Override
    public void runOpMode() {

        initialize();
        waitForStart();
        sleep(1000);

        robot.drivetrain.driveRoute(new Rotation(-90, 0.3, 3

        ));

        /**
        robot.drivetrain.leftDriveMotor.useSmooth = false;
        robot.drivetrain.rightDriveMotor.useSmooth = false;
        SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, robot.soundId);
        robot.screwLift.extend(false);
        sleep(2500);
        robot.screwLift.retract(false);
        sleep(2500);
        robot.drivetrain.driveRoute(new Rotation(90, 0.2, 3));
        robot.drivetrain.driveRoute(new StraightRoute(-4, 0.1, 1));
        robot.drivetrain.driveRoute(new Rotation(-30, 0.2, 1));
        robot.drivetrain.driveRoute(new Rotation(60, 0.2, 1));
        robot.drivetrain.driveRoute(new Rotation(-60, 0.2, 1));
        robot.drivetrain.driveRoute(new Rotation(30, 0.2, 1));
        robot.latch.open();
        sleep(1000);
        robot.latch.close();
        sleep(1000);
        robot.scoop.open();
        sleep(1000);
        robot.scoop.close();
        sleep(1000);
        robot.drivetrain.driveRoute(new Route(0, 4, 0.3, 2));
        robot.drivetrain.driveRoute(new Route(4, 0, 0.3, 2));
         **/
    }
}

