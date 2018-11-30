package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Robot;

@TeleOp(name="Driver Control", group="Iterative OpMode")
public class DriverControl extends OpMode {

    protected Robot robot;
    private ElapsedTime runtime = new ElapsedTime();

    static final float SCOOP_SENSITIVITY_FACTOR = 0.03f;

    public DriverControl() {
        msStuckDetectInit = 15000;
    }

    @Override
    public void init() {
        robot = new Robot(hardwareMap, true);
        robot.init();
        telemetry.addData("Status", "Initialized");
        robot.screwLift.useSmooth = false;
        robot.drivetrain.leftDriveMotor.useSmooth = false;
        robot.drivetrain.rightDriveMotor.useSmooth = false;
    }

    @Override
    public void start() {
        runtime.reset();
    }

    @Override
    public void loop() {

        /** Controls
         *  Gamepad 1:
         *  Forward - Right Trigger
         *  Reverse - Left Trigger
         *  Turn - Left Stick X Axis
         *  ColorArm Extend - X
         *  ColorArm Retract - Y
         *  MarkerArm Extend - A
         *  MarkerArm Retract - B
         *
         *
         *  Gamepad 2:
         *  scoop open - Right Trigger or Right Bumper
         *  scoop close = Left Trigger or Left Bumper
         *  screwLift retract - DPad Down
         *  screwLift extend - DPad Up
         *  latch open - Y
         *  latch close - B
         *  twisty in - X
         *  twisty out - A
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

        robot.drivetrain.drive(leftPower, rightPower);

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
        if (gamepad2.dpad_down) {
            robot.screwLift.setTarget(robot.screwLift.upperBound / 2);
            robot.screwLift.moveToTarget(1);
            //robot.screwLift.retract(false);
        }
        if (gamepad2.dpad_up) {
            robot.screwLift.extend(false);
        }

        if (gamepad2.y) {
            robot.latch.open();
        }
        if (gamepad2.b) {
            robot.latch.close();
        }

        // Accept inputs for twisty motor
        if (gamepad2.x) {
            robot.twisty.moveForward(0.5);
        } else if (gamepad2.a) {
            robot.twisty.moveBackwards(0.5);
        } else {
            robot.twisty.stop();
        }

        // Accept inputs for testing the autonomous mode servos
        if (gamepad1.x) {
            robot.colorArm.open();
        }
        if (gamepad1.y) {
            robot.colorArm.close();
        }
        if (gamepad1.a) {
            robot.markerArm.open();
        }
        if (gamepad1.b) {
            robot.markerArm.close();
        }

        NormalizedRGBA colors = robot.colorSensor.getNormalizedColors();
        telemetry.addLine()
                .addData("a", "%.3f", colors.alpha)
                .addData("r", "%.3f", colors.red)
                .addData("g", "%.3f", colors.green)
                .addData("b", "%.3f", colors.blue);

        // Show the elapsed game time and wheel power.
        telemetry.addData("Status", "Run Time: " +
                "" + runtime.toString());
        telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
        telemetry.addData("Encoder Pos", robot.screwLift.motor.getCurrentPosition());
        telemetry.addData("Actual Power", "left (%.2f), right (%.2f)", robot.drivetrain.leftDriveMotor.getPower(), robot.drivetrain.rightDriveMotor.getPower());
        telemetry.addData("Limit Switch", robot.screwLift.getSwitchPressed() ? "pressed" : "unpressed");
    }

    @Override
    public void stop() {
        robot.halt();
    }

}
