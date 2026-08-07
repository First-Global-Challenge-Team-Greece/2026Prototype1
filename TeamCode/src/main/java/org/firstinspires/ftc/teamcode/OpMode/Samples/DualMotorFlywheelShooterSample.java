package org.firstinspires.ftc.teamcode.OpMode.Samples;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.DualMotorFlywheelShooter;

@Disabled
@TeleOp()
public class DualMotorFlywheelShooterSample extends OpMode {

    private DualMotorFlywheelShooter shooter;

    @Override
    public void init() {
        shooter = new DualMotorFlywheelShooter(hardwareMap);
    }

    @Override
    public void loop() {
        if (gamepad1.aWasPressed()) {
            shooter.shoot();
        }

        if (gamepad1.bWasPressed()) {
            shooter.shoot();
        }

        if (gamepad1.xWasPressed()) {
            shooter.idle();
        }

        shooter.shooterStateMachine();
    }
}
