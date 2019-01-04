package org.firstinspires.ftc.teamcode;

import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.SwitchableLight;

import org.firstinspires.ftc.teamcode.HardwareComponents.ContinuousServo;
import org.firstinspires.ftc.teamcode.HardwareComponents.Drivetrain;
import org.firstinspires.ftc.teamcode.HardwareComponents.BoundedServo;
import org.firstinspires.ftc.teamcode.HardwareComponents.Motors.BoundedMotor;

public class Robot {
    public final Drivetrain drivetrain;
    public final BoundedMotor screwLift;
    public final BoundedServo latch;
    public final BoundedServo scoop;
    public final int soundId;
    public final ContinuousServo twisty;
    public final BoundedServo markerArm;

    public Robot(HardwareMap hwmap, boolean isDriverControl) {
        drivetrain = new Drivetrain(hwmap.get(DcMotor.class, "left_drive"),
                hwmap.get(DcMotor.class, "right_drive"),

                isDriverControl);

        screwLift = new BoundedMotor(hwmap.get(DcMotor.class, "screw_drive"),
                hwmap.get(DigitalChannel.class, "limit_switch"),
                2400);
        screwLift.setReverse();
        screwLift.maxPowerFactor = 0.5;


        latch = new BoundedServo(hwmap.get(Servo.class, "latch"));
        latch.setReverse();
        latch.openPos = 0.8;
        latch.closePos = 0.1;
        latch.initPos = isDriverControl ? latch.openPos : latch.closePos;

        scoop = new BoundedServo(hwmap.get(Servo.class, "scoop"));
        //scoop.setReverse();
        scoop.openPos = 0.55;
        scoop.closePos = 0;
        scoop.lowerLimit = 0.0;
        scoop.upperLimit = scoop.openPos;
        scoop.initPos = scoop.closePos;

        soundId = hwmap.appContext.getResources().getIdentifier("roll", "raw", hwmap.appContext.getPackageName());
        SoundPlayer.getInstance().preload(hwmap.appContext, soundId);

        twisty = new ContinuousServo(hwmap.get(CRServo.class, "twisty"));

        markerArm = new BoundedServo(hwmap.get(Servo.class, "marker_arm"));
        markerArm.setReverse();
        markerArm.openPos = 0;
        markerArm.closePos = 0.5;
        markerArm.initPos = markerArm.closePos;
        markerArm.upperLimit = markerArm.openPos;
        markerArm.lowerLimit = markerArm.closePos;
    }

    public void init() {
        // Initialize screwLift
        screwLift.init();

        // Initialize latch
        latch.init();

        // Initialize scoop
        scoop.init();

        markerArm.init();
    }

    public void halt() {
        drivetrain.rightDriveMotor.stop();
        drivetrain.leftDriveMotor.stop();
        screwLift.stop();
        twisty.stop();
        markerArm.close();
    }
}
