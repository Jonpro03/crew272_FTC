package org.firstinspires.ftc.teamcode;

import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.SwitchableLight;

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

    public Robot(HardwareMap hwmap, boolean isDriverControl) {
        drivetrain = new Drivetrain(hwmap.get(DcMotor.class, "left_drive"),
                hwmap.get(DcMotor.class, "right_drive"),
                isDriverControl);

        screwLift = new BoundedMotor(hwmap.get(DcMotor.class, "screw_drive"),
                hwmap.get(DigitalChannel.class, "limit_switch"));
        screwLift.setReverse();

        latch = new BoundedServo(hwmap.get(Servo.class, "latch"));
        latch.openPos = 0.5;
        latch.closePos = 1;
        latch.initPos = isDriverControl ? latch.openPos : latch.closePos;

        scoop = new BoundedServo(hwmap.get(Servo.class, "scoop"));
        scoop.setReverse();
        scoop.openPos = 0.7;
        scoop.closePos = 0.95;
        scoop.initPos = isDriverControl ? scoop.openPos : scoop.closePos;

        colorSensor = hwmap.get(NormalizedColorSensor.class, "color_sensor");
        soundId = hwmap.appContext.getResources().getIdentifier("roll", "raw", hwmap.appContext.getPackageName());
        SoundPlayer.getInstance().preload(hwmap.appContext, soundId);
    }

    public void init() {

        // Initialize latch
        latch.init();

        // Initialize scoop
        scoop.init();

        // Initialize screwLift
        screwLift.init();

        // Initialize Color Sensor
        if (colorSensor instanceof SwitchableLight) {
            ((SwitchableLight) colorSensor).enableLight(true);
        }
    }

    public void halt() {
        drivetrain.rightDriveMotor.stop();
        drivetrain.leftDriveMotor.stop();
        screwLift.stop();
    }
}
