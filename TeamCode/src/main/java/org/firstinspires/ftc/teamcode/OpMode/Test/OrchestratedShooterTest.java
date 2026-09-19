package org.firstinspires.ftc.teamcode.OpMode.Test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Subsystems.DualMotorFlywheelShooter;
import org.firstinspires.ftc.teamcode.Subsystems.Feeder;
import org.firstinspires.ftc.teamcode.Subsystems.FlywheelShooter;
import org.firstinspires.ftc.teamcode.Util.ButtonToggle;
@Disabled

@TeleOp()
public class OrchestratedShooterTest extends OpMode {

    private DualMotorFlywheelShooter shooter;
    private Feeder feeder;

    private ButtonToggle feederToggle;

    @Override
    public void init() {
        shooter = new DualMotorFlywheelShooter(hardwareMap, telemetry);
        feeder = new Feeder(hardwareMap, telemetry);
        feederToggle = new ButtonToggle(200);
    }

    @Override
    public void loop() {
        shooter.shoot();


        if (feederToggle.update(gamepad1.a)) feeder.feed();
        else feeder.stop();


        shooter.shooterStateMachine();
        boolean isShooterReady = shooter.isReady();
        feeder.SHOOTER_INTERFACE(isShooterReady);
        shooter.debug();
        feeder.debug();
        telemetry.addData("Is Shooter Actually Ready", isShooterReady);
    }
}
