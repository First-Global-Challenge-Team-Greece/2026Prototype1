package org.firstinspires.ftc.teamcode.OpMode.Test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.TankDrive;
import org.firstinspires.ftc.teamcode.Util.ButtonToggle;

@TeleOp()
public class CompleteTest extends OpMode {

    private ButtonToggle intakeToggle;

    private TankDrive tankDrive;
    private Intake intake;

    @Override
    public void init() {
        intakeToggle = new ButtonToggle(300);
        intake = new Intake(hardwareMap, telemetry);
        tankDrive = new TankDrive(hardwareMap, telemetry, TankDrive.DriveMode.ROBOT_CENTRIC);
    }


    @Override
    public void loop() {
        if (gamepad1.dpad_up) {
            intake.extend();
        } else if (gamepad1.dpad_down) {
            intake.retract();
        }

        if (intakeToggle.update(gamepad1.a)) intake.collect();
        else intake.stop();

        tankDrive.driveRobotCentric(-gamepad1.left_stick_y, gamepad1.right_stick_x);
        intake.extensionStateManager();
    }
}
