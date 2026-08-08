package org.firstinspires.ftc.teamcode.Config;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Config
public class TankDriveConfig {
    public static final boolean TELEMETRY_ENABLED = true;

    // Motor Configuration
    public static final DcMotorEx.Direction LEFT_MOTOR_DIRECTION = DcMotorEx.Direction.FORWARD; // Set to REVERSE if left motor is reversed
    public static final DcMotorEx.Direction RIGHT_MOTOR_DIRECTION = DcMotorEx.Direction.REVERSE; // Set to REVERSE if right motor is reversed
    public static final DcMotorEx.ZeroPowerBehavior MOTOR_ZERO_POWER_BEHAVIOR = DcMotorEx.ZeroPowerBehavior.BRAKE; // Set to FLOAT if you want the robot to coast when no power is applied

    // Feedforward constants
    // KS: is the static gain -> for Static Friction
    // KV: is the velocity gain -> Fixes Motor Inaccuracies Linearly
    public static final double[] LEFT_FEEDFORWARD = {0.03, 1.0}; // KS, KV for left motor
    public static final double[] RIGHT_FEEDFORWARD = {0.03, 1.0}; // KS, KV for right motor
    public static final double KS_THETA = 0.08; // Static gain for turning

    public static double KS_LEFT = 1;
    public static double KS_RIGHT = 1;
    public static double KV_RIGHT = 1;
    public static double KV_LEFT = 1;

    public final static RevHubOrientationOnRobot.LogoFacingDirection CONTROL_HUB_LOGO_DIRECTION =
            RevHubOrientationOnRobot.LogoFacingDirection.DOWN;
    public final static RevHubOrientationOnRobot.UsbFacingDirection CONTROL_HUB_USB_PORT_DIRECTION =
            RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD;

    // Power Mapping Constants
    public static final double DEFAULT_POWER = 0.6; // Default power for driving
    public static final double MAX_POWER = 1.0; // Maximum power for driving

    // Auto drive configuration
    public static double LOWER_LUMA_GAIN = 5;
    public static double UPPER_LUMA_GAIN = 5;

    public static double AUTO_TARGET_KP = 1.5;
    public static double AUTO_TARGET_KI = 0;
    public static double AUTO_TARGET_KD = 0.1;

    public static double AUTO_DRIVE_KP = 3;
    public static double AUTO_DRIVE_KI = 0;
    public static double AUTO_DRIVE_KD = 0.2;

    public static double ENCODER_CPR = 28;
    public static double GEAR_RATIO = 12;

    public static double WHEEL_RADIUS = 4.5;

    public static double TICKS_TO_CM = 2 * Math.PI * WHEEL_RADIUS / (ENCODER_CPR * GEAR_RATIO);
}
