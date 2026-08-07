package org.firstinspires.ftc.teamcode.OpMode.Test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

@TeleOp(name = "Motor Unit Testing", group = "Unit Testing")
public class MotorUnitTesting extends OpMode {

    DcMotorEx motor;

    @Override
    public void init() {
        motor = hardwareMap.get(DcMotorEx.class, "m");
    }

    @Override
    public void loop() {
        if (gamepad1.a) motor.setPower(-1);
        else motor.setPower(1);

        telemetry.addData("Motor Current Amps", motor.getCurrent(CurrentUnit.AMPS));
        telemetry.addData("Rotational Velocity", motor.getVelocity());
    }
}
