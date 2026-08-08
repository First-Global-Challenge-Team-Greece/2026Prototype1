package org.firstinspires.ftc.teamcode.OpMode.Test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp()
public class CrServoTest extends OpMode {

    private CRServo left;
    private CRServo right;

    @Override
    public void init() {
        left = hardwareMap.get(CRServo.class, "lcr");
        right = hardwareMap.get(CRServo.class, "rcr");
        right.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void loop() {
        if (gamepad1.left_bumper) {
            left.setPower(1);
            right.setPower(1);
        } else if (gamepad1.right_bumper) {
            left.setPower(-1);
            right.setPower(-1);
        } else {
            left.setPower(0);
            right.setPower(0);
        }
    }
}
