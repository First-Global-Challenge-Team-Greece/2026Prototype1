package org.firstinspires.ftc.teamcode.OpMode.Test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Subsystems.TankDrive;
@Disabled
@TeleOp()
public class BraceTest extends OpMode {

    private CRServo left;
    private CRServo right;

    private DcMotorEx braceMotor;

    private TankDrive tankDrive;

    @Override
    public void init() {
        left = hardwareMap.get(CRServo.class, "lcr");
        right = hardwareMap.get(CRServo.class, "rcr");
        right.setDirection(DcMotorSimple.Direction.REVERSE);

        braceMotor = hardwareMap.get(DcMotorEx.class, "bm");
        braceMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        tankDrive = new TankDrive(hardwareMap, telemetry, TankDrive.DriveMode.ROBOT_CENTRIC);
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

        if (gamepad1.a) {
            braceMotor.setPower(0.8);
            left.close();
            right.close();
        } else braceMotor.setPower(0);


        tankDrive.driveRobotCentric(-gamepad1.left_stick_y, gamepad1.right_stick_x);
    }
}
