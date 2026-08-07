package org.firstinspires.ftc.teamcode.OpMode.Test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Util.ButtonToggle;

@TeleOp()
public class ExtensionTest extends OpMode {

    private DcMotor leftExtension;
    private DcMotor rightExtension;
    private DcMotor intake;

    private ButtonToggle intakeToggle;

    @Override
    public void init() {
        leftExtension = hardwareMap.get(DcMotor.class, "le");
        rightExtension = hardwareMap.get(DcMotor.class, "re");
        intake = hardwareMap.get(DcMotor.class, "im");


    }


    @Override
    public void loop() {
        if (gamepad1.dpad_up) {
            leftExtension.setPower(-1);
            rightExtension.setPower(-1);
        } else if (gamepad1.dpad_down) {
            leftExtension.setPower(1);
            rightExtension.setPower(1);
        } else {
            leftExtension.setPower(0);
            rightExtension.setPower(0);
        }

        if (intakeToggle.update(gamepad1.a)) intake.setPower(1);
        else intake.setPower(0);

    }
}
