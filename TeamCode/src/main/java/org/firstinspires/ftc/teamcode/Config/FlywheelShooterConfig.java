package org.firstinspires.ftc.teamcode.Config;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Config
public class FlywheelShooterConfig {
    public static double ENCODER_CPR = 28;
    public static double SECOND_TO_MINUTE_COEFFICIENT = 60;

    public static double IDLE_VELOCITY = 3000;
    public static double SHOOTING_VELOCITY = 4000;
    public static double STALLED_VELOCITY = 0;
    public static double RPM_THRESHOLD = 3000;

    public static double TUNING_VELOCITY = 2000;

    public static double kP = 0.007;
    public static double kV = 0.0001795;
    public static double kS = 0.1;

    // ----SINGLE MOTOR FLYWHEEL CONFIG----
    public static DcMotorSimple.Direction FLYWHEEL_MOTOR_DIRECTION = DcMotorSimple.Direction.FORWARD;

    // ----DUAL MOTOR FLYWHEEL CONFIG----
    public static DcMotorSimple.Direction LEFT_FLYWHEEL_MOTOR_DIRECTION = DcMotorSimple.Direction.REVERSE;
    public static DcMotorSimple.Direction RIGHT_FLYWHEEL_MOTOR_DIRECTION = DcMotorSimple.Direction.FORWARD;

}
