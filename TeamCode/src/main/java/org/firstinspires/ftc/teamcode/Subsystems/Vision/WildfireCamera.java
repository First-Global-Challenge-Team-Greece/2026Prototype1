package org.firstinspires.ftc.teamcode.Subsystems.Vision;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.Config.HardwareMapConfig;
import org.firstinspires.ftc.vision.VisionPortal;

public class WildfireCamera {
    private WebcamName camera;
    private HardwareMap hardwareMap;
    private Telemetry telemetry;
    private VisionPortal visionPortal;
    private WildfireVisualProcessorBlueShift visualProcessor;

    public WildfireCamera(HardwareMap hardwareMap, Telemetry telemetry) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;

        camera = hardwareMap.get(WebcamName.class, HardwareMapConfig.webcam_id);

        visualProcessor = new WildfireVisualProcessorBlueShift(telemetry);

        visionPortal = new VisionPortal.Builder()
                .setCamera(camera)
                .addProcessor(visualProcessor)
                .enableLiveView(true)
                .build();

    }


    public double[] getLumaValues() {
        return new double[] {
                visualProcessor.getLeftLowerLuma(),
                visualProcessor.getRightLowerLuma(),
                visualProcessor.getLeftUpperLuma(),
                visualProcessor.getRightUpperLuma()
        };
    }
}
