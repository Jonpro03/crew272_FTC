package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.SwitchableLight;

import org.firstinspires.ftc.teamcode.HardwareComponents.Drivetrain;
import org.firstinspires.ftc.teamcode.HardwareComponents.BoundedServo;
import org.firstinspires.ftc.teamcode.HardwareComponents.Motors.BoundedMotor;

public class Robot {
    public final Drivetrain Drivetrain;
    public final BoundedMotor screwLift;
    public final BoundedServo latch;
    public final BoundedServo scoop;
    public final NormalizedColorSensor ColorSensor;

    public Robot(HardwareMap hwmap, boolean isDriverControl) {
        Drivetrain = new Drivetrain(hwmap.get(DcMotor.class, "left_drive"),
                hwmap.get(DcMotor.class, "right_drive"),
                isDriverControl);

        screwLift = new BoundedMotor(hwmap.get(DcMotor.class, "screw_drive"),
                hwmap.get(DigitalChannel.class, "limit_switch"));

        latch = new BoundedServo(hwmap.get(Servo.class, "latch"));
        latch.openPos = 0.5;
        latch.closePos = 1;
        latch.initPos = isDriverControl ? latch.openPos : latch.closePos;

        scoop = new BoundedServo(hwmap.get(Servo.class, "scoop"));
        scoop.setReverse();
        scoop.openPos = 0.52;
        scoop.closePos = 0.85;
        scoop.initPos = isDriverControl ? scoop.openPos : scoop.closePos;

        ColorSensor = hwmap.get(NormalizedColorSensor.class, "color_sensor");
    }

    public void Init() {

        // Initialize latch
        latch.init();

        // Initialize scoop
        scoop.init();

        // Initialize screwLift
        screwLift.init();

        // Initialize Color Sensor
        if (ColorSensor instanceof SwitchableLight) {
            ((SwitchableLight) ColorSensor).enableLight(true);
        }
    }

    public void Halt() {
        Drivetrain.rightDriveMotor.stop();
        Drivetrain.leftDriveMotor.stop();
        screwLift.stop();
    }
}
