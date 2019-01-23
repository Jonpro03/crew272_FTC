package org.firstinspires.ftc.teamcode.HardwareComponents.Camera;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.ArrayList;
import java.util.List;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.YZX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.FRONT;

public class VuforiaCam {
    private static final String VUFORIA_KEY = "AQJiMSv/////AAABmfT1cLzA5EhbkKRAfJuuv9OA04G4bNNEu2cZh9R1VH3gpPeK/wzVvGUO+cvbP2qOqrm03Hco5H5kYa+OmPFAkvIvtGEFJ9hZO24ocVIXilRSogW2Xy2eXji1yw9OJabWcGvJEbfkSVwKvcQYPh8cCEj+WNApkFVLrjBd+K7cA8fYzwoQAwDneWUtzrForXag5eevs3+3MfMz2C9Bgg17sKIBHyNiQoFZDY/QJWVxvTbqbMgvxrdUTPFA+SP+8cwVaK1gUit7slfuFWOc6gHRFlJhopdeVeUPMeDXPTvpCc/o0PZhC8aNEA0wdhqGPtpW96vCdBAdQFINxuUA23xav8PUINlWR9PAL5tZ2GTBLhxq";
    private static final float mmPerInch        = 25.4f;
    private static final float mmFTCFieldWidth  = (12*6) * mmPerInch;       // the width of the FTC field (from the center point to the outer panels)
    private static final float mmTargetHeight   = (6) * mmPerInch;

    final int CAMERA_FORWARD_DISPLACEMENT  = 190;   // eg: Camera is x mm in front of robot center
    final int CAMERA_VERTICAL_DISPLACEMENT = 190;   // eg: Camera is x mm above ground
    final int CAMERA_LEFT_DISPLACEMENT     = -50;     // eg: Camera is x mm from the robot's center line
    private static final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = BACK;

    public List<VuforiaTrackable> allTrackables;

    public VuforiaLocalizer vuforia;

    public static final String BACK_WALL_VUMARK_NAME = "Back-Space";
    public static final String BLUE_WALL_VUMARK_NAME = "Blue-Rover";
    public static final String RED_WALL_VUMARK_NAME = "Red-Footprint";
    public static final String FRONT_WALL_VUMARK_NAME = "Front-Craters";


    public VuforiaCam(HardwareMap hwMap) {
        int cameraMonitorViewId = hwMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hwMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = CAMERA_CHOICE;

        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        VuforiaTrackables targetsRoverRuckus = this.vuforia.loadTrackablesFromAsset("RoverRuckus");
        VuforiaTrackable blueRover = targetsRoverRuckus.get(0);
        blueRover.setName(BLUE_WALL_VUMARK_NAME);
        VuforiaTrackable redFootprint = targetsRoverRuckus.get(1);
        redFootprint.setName(RED_WALL_VUMARK_NAME);
        VuforiaTrackable frontCraters = targetsRoverRuckus.get(2);
        frontCraters.setName(FRONT_WALL_VUMARK_NAME);
        VuforiaTrackable backSpace = targetsRoverRuckus.get(3);
        backSpace.setName(BACK_WALL_VUMARK_NAME);

        // For convenience, gather together all the trackable objects in one easily-iterable collection */
        allTrackables = new ArrayList<VuforiaTrackable>();
        allTrackables.addAll(targetsRoverRuckus);

        OpenGLMatrix blueRoverLocationOnField = OpenGLMatrix
                .translation(0, mmFTCFieldWidth, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 0));
        blueRover.setLocation(blueRoverLocationOnField);

        OpenGLMatrix redFootprintLocationOnField = OpenGLMatrix
                .translation(0, -mmFTCFieldWidth, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 180));
        redFootprint.setLocation(redFootprintLocationOnField);

        OpenGLMatrix frontCratersLocationOnField = OpenGLMatrix
                .translation(-mmFTCFieldWidth, 0, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0 , 90));
        frontCraters.setLocation(frontCratersLocationOnField);

        OpenGLMatrix backSpaceLocationOnField = OpenGLMatrix
                .translation(mmFTCFieldWidth, 0, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, -90));
        backSpace.setLocation(backSpaceLocationOnField);

        OpenGLMatrix phoneLocationOnRobot = OpenGLMatrix
                .translation(CAMERA_FORWARD_DISPLACEMENT, CAMERA_LEFT_DISPLACEMENT, CAMERA_VERTICAL_DISPLACEMENT)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, YZX, DEGREES,
                        CAMERA_CHOICE == FRONT ? 90 : -90, -15, 0)); // Second angle = 180 for upside-down phone.

        for (VuforiaTrackable trackable : allTrackables) {
            ((VuforiaTrackableDefaultListener) trackable.getListener()).setPhoneInformation(phoneLocationOnRobot, parameters.cameraDirection);
        }

        targetsRoverRuckus.activate();
    }
}

