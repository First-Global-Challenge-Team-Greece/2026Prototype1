package org.firstinspires.ftc.teamcode.Config;

public class HardwareMapConfig {

    // Omni Config
    public final static String left_front_drive_id = "lfm";
    public final static String left_back_drive_id = "lbm";
    public final static String right_front_drive_id = "rfm";
    public final static String right_back_drive_id = "rbm";

    public final static String IMU_ID = "imu";

    // Tank Config
    public final static String left_drive_motor_id = "ldm";
    public final static String right_drive_motor_id = "rdm";

    public final static String webcam_id = "webcam";

    public final static String intake_extension_motor_id = "iem";

    public final static String intake_motor_id = "im";

    public final static String intake_extension_magnetic_sensor_id = "iems";

    public static final String shooter_motor_id = "sm";

    public static final String left_shooter_motor_id = "lsm";
    public static final String right_shooter_motor_id = "rsm";

    public static final String feeder_motor_id = "fm";

    public static final String right_mixer_continuous_servo_id = "rmcr";
    public static final String left_mixer_continuous_servo_id = "lmcr";

    public static final String brace_motor_id = "bm";

    public static final String left_brace_continuous_servo_id = "lbcr";
    public static final String right_brace_continuous_servo_id = "rbcr";

    public static String[] allMotors() {
        return new String[] {
                left_shooter_motor_id,
                right_drive_motor_id,
                left_shooter_motor_id,
                right_shooter_motor_id,
                intake_motor_id,
                intake_extension_motor_id,
                brace_motor_id,
                feeder_motor_id
        };
    }
}
