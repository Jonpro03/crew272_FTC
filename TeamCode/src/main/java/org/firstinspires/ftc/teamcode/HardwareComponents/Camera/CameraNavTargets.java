package org.firstinspires.ftc.teamcode.HardwareComponents.Camera;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.HardwareComponents.Camera.VuforiaCam;

import java.util.ArrayList;
import java.util.List;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.YZX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.FRONT;

public class CameraNavTargets {
    private static final float mmPerInch        = 25.4f;
    private OpenGLMatrix lastLocation = null;
    public float lastRotation = 0;
    public float lastX = 0;
    public float lastY = 0;

    private boolean targetVisible = false;

    VuforiaCam vuCam;

    public CameraNavTargets(VuforiaCam vuCam) {
        this.vuCam = vuCam;
    }

    public boolean check(Telemetry telem) {
        targetVisible = false;
        for (VuforiaTrackable trackable : vuCam.allTrackables) {
            if (((VuforiaTrackableDefaultListener)trackable.getListener()).isVisible()) {
                telem.addData("Visible Target", trackable.getName());
                targetVisible = true;

                OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener)trackable.getListener()).getUpdatedRobotLocation();
                if (robotLocationTransform != null) {
                    lastLocation = robotLocationTransform;
                }

                VectorF translation = lastLocation.getTranslation();
                lastX = translation.get(0) / mmPerInch;
                lastY = translation.get(1) / mmPerInch;

                Orientation rotation = Orientation.getOrientation(lastLocation, EXTRINSIC, XYZ, DEGREES);
                lastRotation = rotation.thirdAngle;

                telem.addData("Pos (in)", "{X, Y} = %.1f, %.1f", lastX, lastY);
                telem.addData("Rotation (deg)", "{Heading} = %.0f", lastRotation);
                break;
            }
        }

        return targetVisible;
    }

}
