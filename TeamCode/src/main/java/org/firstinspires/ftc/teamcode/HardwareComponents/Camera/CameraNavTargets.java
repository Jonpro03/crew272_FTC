package org.firstinspires.ftc.teamcode.HardwareComponents.Camera;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.teamcode.Movement.Models.Location;
import org.firstinspires.ftc.teamcode.Movement.Models.Point2D;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;

public class CameraNavTargets {
    private static final float mmPerInch        = 25.4f;
    private OpenGLMatrix lastLocation = null;
    public Location lastKnownLocation = new Location(new Point2D(0, 0), 0);

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
                lastKnownLocation.position.x = translation.get(0) / mmPerInch;
                lastKnownLocation.position.y = translation.get(1) / mmPerInch;

                Orientation rotation = Orientation.getOrientation(lastLocation, EXTRINSIC, XYZ, DEGREES);
                lastKnownLocation.rotation = rotation.thirdAngle;

                telem.addData("Pos (in)", "{X, Y} = %.1f, %.1f", lastKnownLocation.position.x, lastKnownLocation.position.y);
                telem.addData("Rotation (deg)", "{Heading} = %.0f", lastKnownLocation.rotation);
                break;
            }
        }

        return targetVisible;
    }

}
