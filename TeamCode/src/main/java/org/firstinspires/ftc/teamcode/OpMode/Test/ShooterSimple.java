package org.firstinspires.ftc.teamcode.OpMode.Test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Config.HardwareMapConfig;
import org.firstinspires.ftc.teamcode.Subsystems.Feeder;
@Disabled

@TeleOp()
public class ShooterSimple extends OpMode {

    private DcMotorEx leftShooter;
    private DcMotorEx rightShooter;

    private Feeder feeder;

    @Override
    public void init() {
        leftShooter = hardwareMap.get(DcMotorEx.class, HardwareMapConfig.left_shooter_motor_id);
        rightShooter = hardwareMap.get(DcMotorEx.class, HardwareMapConfig.right_shooter_motor_id);
        leftShooter.setDirection(DcMotorSimple.Direction.REVERSE);

        feeder = new Feeder(hardwareMap, telemetry);
    }

    @Override
    public void loop() {
        leftShooter.setPower(0.8);
        rightShooter.setPower(0.8);

        if (gamepad1.a) feeder.feed();
        else feeder.stop();

        telemetry.addData("Right Current", rightShooter.getCurrent(CurrentUnit.AMPS));
        telemetry.addData("Left Current", leftShooter.getCurrent(CurrentUnit.AMPS));
    }
}
