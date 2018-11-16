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
        SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, robot.soundId);
        robot.screwLift.extend(false);
        sleep(2000);
        robot.screwLift.retract(false);
        sleep(2000);
        robot.drivetrain.driveRoute(new Rotation(90, 0.5, 3));
        robot.drivetrain.driveRoute(new StraightRoute(-4, 0.5, 1));
        robot.drivetrain.driveRoute(new Rotation(-3, 0.5, 1));
        robot.drivetrain.driveRoute(new Rotation(6, 0.5, 1));
        robot.drivetrain.driveRoute(new Rotation(-6, 0.5, 1));
        robot.drivetrain.driveRoute(new Rotation(3, 0.5, 1));
        robot.scoop.open();
        robot.scoop.close();
        robot.scoop.open();
        robot.scoop.close();
        robot.drivetrain.driveRoute(new Route(0, 4, 0.5, 2));
        robot.drivetrain.driveRoute(new Route(4, 0, 0.5, 2));


    }
}

