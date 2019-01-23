package org.firstinspires.ftc.teamcode;

import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.teamcode.HardwareComponents.CompassReading;
import org.firstinspires.ftc.teamcode.HardwareComponents.CompassSensor;
import org.firstinspires.ftc.teamcode.HardwareComponents.ContinuousServo;
import org.firstinspires.ftc.teamcode.HardwareComponents.Drivetrain;
import org.firstinspires.ftc.teamcode.HardwareComponents.BoundedServo;
import org.firstinspires.ftc.teamcode.HardwareComponents.Motors.BoundedMotor;
import org.firstinspires.ftc.teamcode.HardwareComponents.Motors.LimitedMotor;

public class Robot {
    public final Drivetrain drivetrain;
    public final BoundedMotor screwLift;
    public final LimitedMotor forklift;
    public final BoundedServo latch;
    public final int soundId;
    public final ContinuousServo twisty;
    public final BoundedServo markerArm;


    public Robot(HardwareMap hwmap, boolean isDriverControl) {
        CompassSensor compassSensor = new CompassSensor(hwmap.appContext);
        CompassReading.getInstance().setCompassSensor(compassSensor);

        drivetrain = new Drivetrain(hwmap.get(DcMotor.class, "left_drive"),
                hwmap.get(DcMotor.class, "right_drive"),
                isDriverControl);

        screwLift = new BoundedMotor(hwmap.get(DcMotor.class, "screw_drive"),
                hwmap.get(DigitalChannel.class, "limit_switch"),
                2400);
        screwLift.setReverse();
        screwLift.maxPowerFactor = 0.5;

        forklift = new LimitedMotor(hwmap.get(DcMotor.class, "forklift_drive"), 9400);
        forklift.setReverse();


        latch = new BoundedServo(hwmap.get(Servo.class, "latch"));
        latch.setReverse();
        latch.openPos = 0.8;
        latch.closePos = 0.1;
        latch.initPos = isDriverControl ? latch.openPos : latch.closePos;

        soundId = hwmap.appContext.getResources().getIdentifier("roll", "raw", hwmap.appContext.getPackageName());
        SoundPlayer.getInstance().preload(hwmap.appContext, soundId);

        twisty = new ContinuousServo(hwmap.get(CRServo.class, "twisty"));

        markerArm = new BoundedServo(hwmap.get(Servo.class, "marker_arm"));
        markerArm.setReverse();
        markerArm.openPos = 0.5;
        markerArm.closePos = 0;
        markerArm.initPos = markerArm.closePos;
        markerArm.upperLimit = markerArm.openPos;
        markerArm.lowerLimit = markerArm.closePos;
    }

    public void init() {
        // Initialize screwLift
        screwLift.init();

        // Initialize latch
        latch.init();

        markerArm.init();

        forklift.init();
    }

    public void halt() {
        drivetrain.rightDriveMotor.stop();
        drivetrain.leftDriveMotor.stop();
        screwLift.stop();
        twisty.stop();
        markerArm.close();
        forklift.stop();
    }
}
