package org.firstinspires.ftc.teamcode.OpMode.Test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.Vision.WildfireCamera;

@TeleOp()
public class WildlifeVisionPortalTest extends OpMode {

    private WildfireCamera wildfireCamera;

    @Override
    public void init() {
        wildfireCamera = new WildfireCamera(hardwareMap, telemetry);
    }

    @Override
    public void loop() {
        telemetry.addData("Left Luma", wildfireCamera.getLumaValues()[0]);
        telemetry.addData("Right Luma", wildfireCamera.getLumaValues()[1]);
    }
}
