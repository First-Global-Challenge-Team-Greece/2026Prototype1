package org.firstinspires.ftc.teamcode.Config;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Config
public class OmniDriveConfig {
    public enum ImuType {
        BHI260, BNO055
    }

    // Non-Tunable constants
    public final static ImuType ROBOT_IMU_TYPE = ImuType.BNO055;

    public final static DcMotorSimple.Direction LEFT_FRONT_DRIVE_DIRECTION = DcMotorSimple.Direction.FORWARD;
    public final static DcMotorSimple.Direction LEFT_BACK_DRIVE_DIRECTION = DcMotorSimple.Direction.FORWARD;
    public final static DcMotorSimple.Direction RIGHT_FRONT_DRIVE_DIRECTION = DcMotorSimple.Direction.REVERSE;
    public final static DcMotorSimple.Direction RIGHT_BACK_DRIVE_DIRECTION = DcMotorSimple.Direction.REVERSE;

    public final static DcMotor.ZeroPowerBehavior MOTOR_ZERO_POWER_BEHAVIOR = DcMotor.ZeroPowerBehavior.BRAKE;

    public final static RevHubOrientationOnRobot.LogoFacingDirection CONTROL_HUB_LOGO_DIRECTION =
            RevHubOrientationOnRobot.LogoFacingDirection.UP;
    public final static RevHubOrientationOnRobot.UsbFacingDirection CONTROL_HUB_USB_PORT_DIRECTION =
            RevHubOrientationOnRobot.UsbFacingDirection.RIGHT;


    // Tunable at runtime constants
    public static double MAX_SPEED = 0.8;

    public static double[] KV = {1, 1, 1, 1};
    public static double[] KS = {0.181, 0.128, 0.11, 0.1};
}
