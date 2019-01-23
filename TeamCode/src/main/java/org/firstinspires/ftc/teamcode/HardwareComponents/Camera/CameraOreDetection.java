package org.firstinspires.ftc.teamcode.HardwareComponents.Camera;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.vuforia.CameraDevice;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.Movement.Positioning;

import java.util.ArrayList;
import java.util.List;

public class CameraOreDetection {

    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";
    private TFObjectDetector tfod;

    public CameraOreDetection(HardwareMap hwmap, VuforiaLocalizer vuLoc) {

        int tfodMonitorViewId = hwmap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hwmap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuLoc);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);

        tfod.activate();
    }

    public boolean seeGold(Telemetry telem) {
        List<Recognition> recognitions = tfod.getRecognitions();
        if (recognitions == null || recognitions.size() == 0) {
            return false;
        }
        for (Recognition r : recognitions) {
            if (r.getLabel().equals(LABEL_GOLD_MINERAL)) {
                telem.addLine("Recognized Gold!");
                return true;
            }
        }
        return false;
    }

    public int goldDirection() {
        List<Recognition> recognitions = tfod.getRecognitions();
        if (recognitions == null || recognitions.size() == 0) {
            return Positioning.STRAIGHT;
        }
        for (Recognition r : recognitions) {
            if (r.getLabel().equals(LABEL_GOLD_MINERAL)) {
                int x = (int) r.getLeft();
                if (x > 320) { return Positioning.RIGHT; }
                if (x < 280) { return Positioning.LEFT; }
                return Positioning.STRAIGHT;
            }
        }
        return Positioning.STRAIGHT;
    }

    public int determineGoldPosition(Telemetry telem) {
        CameraDevice.getInstance().setFlashTorchMode(true);

        int goldx = 0;
        int silver1x = 0;
        int silver2x = 0;

        List<Recognition> recognitions = tfod.getRecognitions();
        if (recognitions == null || recognitions.size() == 0) {
            CameraDevice.getInstance().setFlashTorchMode(false);
            return Positioning.UNKNOWN;
        }

        List<Recognition> scopedList = new ArrayList<Recognition>();
        for (Recognition r : recognitions) {
            telem.addLine(r.getLabel() + ": " + r.getWidth());
            if (r.getWidth() > 34) {
                scopedList.add(r);
            }
        }
        telem.update();

        for (Recognition r : recognitions) {
            if (r.getLabel().equals(LABEL_GOLD_MINERAL)) {
                goldx = (int) r.getLeft();
            } else if (silver1x == 0) {
                silver1x = (int) r.getLeft();
            } else {
                silver2x = (int) r.getLeft();
            }

            if (!(goldx == 0 || silver1x == 0 || silver2x == 0)) {
                if (goldx < silver1x && goldx < silver2x) {
                    CameraDevice.getInstance().setFlashTorchMode(false);
                    return Positioning.LEFT;
                }
                if (silver1x < goldx && goldx < silver2x) {
                    CameraDevice.getInstance().setFlashTorchMode(false);
                    return Positioning.STRAIGHT;
                }
                else {
                    CameraDevice.getInstance().setFlashTorchMode(false);
                    return Positioning.RIGHT;
                }
            }
        }
        CameraDevice.getInstance().setFlashTorchMode(false);
        return Positioning.UNKNOWN;
    }

    public void shutDown() {
        tfod.deactivate();
    }
}
