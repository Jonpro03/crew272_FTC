package org.firstinspires.ftc.teamcode.HardwareComponents.Camera;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.Movement.Utility;

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
            return Utility.STRAIGHT;
        }
        for (Recognition r : recognitions) {
            if (r.getLabel().equals(LABEL_GOLD_MINERAL)) {
                int x = (int) r.getLeft();
                if (x > 320) { return Utility.RIGHT; }
                if (x < 280) { return Utility.LEFT; }
                return Utility.STRAIGHT;
            }
        }
        return Utility.STRAIGHT;
    }

    public void shutDown() {
        tfod.deactivate();
    }
}
