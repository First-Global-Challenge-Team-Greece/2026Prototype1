package org.firstinspires.ftc.teamcode.OpMode.Utilities;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Utility;

import org.firstinspires.ftc.teamcode.Subsystems.Brace;

@Utility()
public class BraceExtensionResetUtility extends OpMode {

    private Brace brace;

    @Override
    public void init() {
        brace = new Brace(hardwareMap);
    }

    @Override
    public void loop() {
        if (gamepad1.a) brace.retract();
        else brace.stop();
    }
}
