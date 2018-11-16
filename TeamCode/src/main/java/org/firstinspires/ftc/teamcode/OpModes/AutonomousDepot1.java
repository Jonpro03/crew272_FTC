package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.teamcode.Robot;


@Autonomous(name="Autonomous: Depot", group="Autonomous")
public class AutonomousDepot1 extends LinearOpMode {
    private Robot robot;
    private boolean goldFound = false;

    public void initialize() {
        robot = new Robot(hardwareMap, false);
        robot.init();
        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void runOpMode() {

        initialize();
        waitForStart();

        robot.screwLift.extend();
        robot.latch.open();
        sleep(500);
        robot.screwLift.retract(false);
        robot.drivetrain.driveRoute(Routes.DEPARTURE);

        checkGoldOre();
        robot.drivetrain.driveRoute(Routes.MOVE_TO_NEXT_ORE);
        if (!goldFound)
            checkGoldOre();
        robot.drivetrain.driveRoute(Routes.MOVE_TO_NEXT_ORE);
        if (!goldFound)
            checkGoldOre();

        robot.drivetrain.driveRoute(Routes.DEPOT_SIDE_ORE_TO_DEPOT);

        robot.scoop.open();
        sleep(1000);
        robot.scoop.close();

        robot.drivetrain.driveRoute(Routes.REVERSE_INTO_CRATER);

    }

    private void checkGoldOre() {
        NormalizedRGBA colors;
        colors = robot.colorSensor.getNormalizedColors();
        goldFound = colors.blue < colors.red && colors.blue < colors.green;
        if (goldFound)
            robot.drivetrain.driveRoute(Routes.ORE_FOUND);
    }
}
