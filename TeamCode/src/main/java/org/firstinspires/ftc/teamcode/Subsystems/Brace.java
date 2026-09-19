package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Config.HardwareMapConfig;

public class Brace {
    private final DcMotorEx braceMotor;
    private final CRServo leftBraceExtensionServo;
    private final CRServo rightBraceExtensionServo;

    public Brace(HardwareMap hardwareMap) {
        braceMotor = hardwareMap.get(DcMotorEx.class, HardwareMapConfig.brace_motor_id);

        leftBraceExtensionServo = hardwareMap.get(CRServo.class, HardwareMapConfig.left_brace_continuous_servo_id);
        rightBraceExtensionServo = hardwareMap.get(CRServo.class, HardwareMapConfig.right_brace_continuous_servo_id);
        leftBraceExtensionServo.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void extend() {
        leftBraceExtensionServo.setPower(1);
        rightBraceExtensionServo.setPower(1);
    }
    public void retract() {
        leftBraceExtensionServo.setPower(-1);
        rightBraceExtensionServo.setPower(-1);
    }
    public void stop() {
        leftBraceExtensionServo.setPower(0);
        rightBraceExtensionServo.setPower(0);
    }

    public void climb() {
        braceMotor.setPower(0.8);
    }

    public void stall() {
        braceMotor.setPower(0);
    }
}
