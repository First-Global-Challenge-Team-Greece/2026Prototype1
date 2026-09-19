package org.firstinspires.ftc.teamcode.OpMode.Utilities;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Utility;

import org.firstinspires.ftc.teamcode.Subsystems.Intake;

@Utility()
public class IntakeExtensionResetUtility extends OpMode {

    private Intake intake;

    @Override
    public void init() {
        intake = new Intake(hardwareMap, telemetry);
    }

    @Override
    public void loop() {
        if (gamepad1.a) intake.MANUAL_EXTENSION_INTERFACE(-0.8);
        else intake.MANUAL_EXTENSION_INTERFACE(0);
    }
}
