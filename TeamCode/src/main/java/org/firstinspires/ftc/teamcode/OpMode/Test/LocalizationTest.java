package org.firstinspires.ftc.teamcode.OpMode.Test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.TankDrive;

@TeleOp()
public class LocalizationTest extends OpMode {

    TankDrive tankDrive;

    @Override
    public void init() {
        tankDrive = new TankDrive(hardwareMap, telemetry, TankDrive.DriveMode.FIELD_CENTRIC);

    }

    @Override
    public void loop() {
        tankDrive.driveRobotCentric(gamepad1.left_stick_y, gamepad1.right_stick_x);
        telemetry.addData(">", tankDrive.getCurrentPosition().toString());
    }
}
