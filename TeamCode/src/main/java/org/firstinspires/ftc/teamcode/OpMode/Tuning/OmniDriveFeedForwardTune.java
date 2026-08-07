package org.firstinspires.ftc.teamcode.OpMode.Tuning;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.OmniDrive;

@TeleOp()
public class OmniDriveFeedForwardTune extends OpMode {

    private OmniDrive omniDrive;

    @Override
    public void init() {
        omniDrive = new OmniDrive(
                hardwareMap,
                new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry()),
                OmniDrive.DriveMode.ROBOT_CENTRIC);
    }

    @Override
    public void loop() {
        omniDrive.tune();
        FtcDashboard.getInstance().getTelemetry().update();
    }
}
