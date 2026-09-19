package org.firstinspires.ftc.teamcode.OpMode.Test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.Feeder;

@Disabled
@TeleOp()
public class FeederTest extends OpMode {

    private Feeder feeder;

    @Override
    public void init() {
        feeder = new Feeder(hardwareMap, telemetry);
    }

    @Override
    public void loop() {
        feeder.SHOOTER_INTERFACE(true);
        feeder.feed();
    }
}
