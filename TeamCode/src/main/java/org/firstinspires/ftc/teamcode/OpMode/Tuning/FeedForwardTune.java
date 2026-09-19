package org.firstinspires.ftc.teamcode.OpMode.Tuning;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Utility;

import org.firstinspires.ftc.teamcode.Subsystems.TankDrive;

@Utility()
public class FeedForwardTune extends OpMode {

    private TankDrive tankDrive;
    private MultipleTelemetry multipleTelemetry;

    @Override
    public void init() {
        multipleTelemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        tankDrive = new TankDrive(hardwareMap, multipleTelemetry, TankDrive.DriveMode.ROBOT_CENTRIC);
    }

    @Override
    public void loop() {
        tankDrive.tune();
        tankDrive.debug();
        multipleTelemetry.update();
    }
}
