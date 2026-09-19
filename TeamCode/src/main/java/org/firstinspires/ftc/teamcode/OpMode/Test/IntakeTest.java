package org.firstinspires.ftc.teamcode.OpMode.Test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.TankDrive;
@Disabled

@TeleOp()
public class IntakeTest extends OpMode {

    private TankDrive tankDrive;
    private Intake intake;

    @Override
    public void init() {
        tankDrive = new TankDrive(hardwareMap, telemetry, TankDrive.DriveMode.ROBOT_CENTRIC);

        intake = new Intake(hardwareMap, telemetry);
    }

    @Override
    public void loop() {
        tankDrive.driveRobotCentric(-gamepad1.left_stick_y, gamepad1.right_stick_x);

        if (gamepad1.a) intake.collect();
        else if (gamepad1.b) intake.drop();
        else intake.stop();
    }
}
