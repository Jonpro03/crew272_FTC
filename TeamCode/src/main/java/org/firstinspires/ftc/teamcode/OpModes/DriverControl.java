package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Robot;

@TeleOp(name="Driver Control", group="Iterative OpMode")
public class DriverControl extends OpMode {

    protected Robot robot;
    private ElapsedTime runtime = new ElapsedTime();

    static final float SCOOP_SENSITIVITY_FACTOR = 0.1f;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, true);
        robot.Init();
        telemetry.addData("Status", "Initialized");
        robot.screwLift.useSmooth = false;
        robot.Drivetrain.leftDriveMotor.useSmooth = false;
        robot.Drivetrain.rightDriveMotor.useSmooth = false;
    }

    @Override
    public void start() { runtime.reset(); }

    @Override
    public void loop() {

        /** Controls
         *  Gamepad 1:
         *  Forward - Right Trigger
         *  Reverse - Left Trigger
         *  Turn - Left Stick X Axis
         *
         *  Gamepad 2:
         *  scoop open - Right Trigger or Right Bumper
         *  scoop close = Left Trigger or Left Bumper
         *  screwLift retract - DPad Up
         *  screwLift extend - DPad Down
         *  latch open - A
         *  latch close - B
         */

        // Accept inputs for driving
        double throttleInput = 0;
        throttleInput += gamepad1.right_trigger;
        throttleInput -= gamepad1.left_trigger;

        // Check for left or right inputs
        double leftInput = gamepad1.left_stick_x;
        double rightInput = -gamepad1.left_stick_x;

        double leftPower = Range.clip((throttleInput + leftInput), -1, 1);
        double rightPower = Range.clip((throttleInput + rightInput), -1, 1);

        robot.Drivetrain.drive(leftPower, rightPower);

        // Accept inputs for the scoop
        robot.scoop.setPosition(robot.scoop.getPosition() +
                (gamepad2.right_trigger * SCOOP_SENSITIVITY_FACTOR) -
                (gamepad2.left_trigger * SCOOP_SENSITIVITY_FACTOR));
        if(gamepad2.right_bumper) {
            robot.scoop.close();
        }
        if(gamepad2.left_bumper) {
            robot.scoop.open();
        }

        // Accept inputs for screw drive and latch
        if (gamepad2.dpad_up) {
            robot.screwLift.retract();
        }
        if (gamepad2.dpad_down) {
            robot.screwLift.extend();
        }

        if (gamepad2.a) {
            robot.latch.open();
        }
        if (gamepad2.b) {
            robot.latch.close();
        }


        // Show the elapsed game time and wheel power.
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
        telemetry.addData("Adjusted Power", "left (%.2f), right (%.2f)", robot.Drivetrain.currentLeftPower, robot.Drivetrain.currentRightPower);
        telemetry.addData("Actual Power", "left (%.2f), right (%.2f)", robot.Drivetrain.leftDriveMotor.getPower(), robot.Drivetrain.rightDriveMotor.getPower());
        telemetry.addData("Limit Switch", robot.screwLift.getSwitchState() ? "pressed" : "unpressed");
    }

    @Override
    public void stop() {
        robot.Halt();
    }

}
