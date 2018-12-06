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
    public final NormalizedColorSensor colorSensor;
    public final int soundId;
    public final ContinuousServo twisty;
    public final BoundedServo markerArm;
    public final BoundedServo colorArm;

    public Robot(HardwareMap hwmap, boolean isDriverControl) {
        drivetrain = new Drivetrain(hwmap.get(DcMotor.class, "left_drive"),
                hwmap.get(DcMotor.class, "right_drive"),
                isDriverControl);

        screwLift = new BoundedMotor(hwmap.get(DcMotor.class, "screw_drive"),
                hwmap.get(DigitalChannel.class, "limit_switch"),
                2580);
        screwLift.setReverse();
        screwLift.maxPowerFactor = 0.5;


        latch = new BoundedServo(hwmap.get(Servo.class, "latch"));
        latch.setReverse();
        latch.openPos = 0.8;
        latch.closePos = 0.1;
        latch.initPos = isDriverControl ? latch.openPos : latch.closePos;

        scoop = new BoundedServo(hwmap.get(Servo.class, "scoop"));
        scoop.setReverse();
        scoop.openPos = 0.5;
        scoop.closePos = 0.8;
        scoop.lowerLimit = 0.2;
        scoop.upperLimit = scoop.closePos;
        scoop.initPos = isDriverControl ? scoop.openPos : scoop.closePos;

        colorSensor = hwmap.get(NormalizedColorSensor.class, "color_sensor");
        soundId = hwmap.appContext.getResources().getIdentifier("roll", "raw", hwmap.appContext.getPackageName());
        SoundPlayer.getInstance().preload(hwmap.appContext, soundId);

        twisty = new ContinuousServo(hwmap.get(CRServo.class, "twisty"));

        markerArm = new BoundedServo(hwmap.get(Servo.class, "marker_arm"));
        markerArm.setReverse();
        markerArm.openPos = 0.75;
        markerArm.closePos = 0;
        markerArm.initPos = markerArm.closePos;
        markerArm.upperLimit = markerArm.openPos;
        markerArm.lowerLimit = markerArm.closePos;

        colorArm = new BoundedServo(hwmap.get(Servo.class, "color_arm"));
        colorArm.setReverse();
        colorArm.openPos = 0.94;
        colorArm.closePos = 0.32;
        colorArm.initPos = colorArm.closePos;
        colorArm.upperLimit = colorArm.openPos;
        colorArm.lowerLimit = colorArm.closePos;

    }

    public void init() {

        // Initialize latch
        latch.init();

        // Initialize scoop
        scoop.init();

        // Initialize screwLift
        screwLift.init();

        // Initialize Color Sensor\
        if (colorSensor instanceof SwitchableLight) {
            ((SwitchableLight) colorSensor).enableLight(false);
        }

        markerArm.init();
        colorArm.init();
    }

    public void halt() {
        drivetrain.rightDriveMotor.stop();
        drivetrain.leftDriveMotor.stop();
        screwLift.stop();
        twisty.stop();
        markerArm.close();
    }
}
