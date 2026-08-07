package org.firstinspires.ftc.teamcode.Subsystems;


import static org.firstinspires.ftc.teamcode.Config.OmniDriveConfig.MAX_SPEED;
import static org.firstinspires.ftc.teamcode.Config.OmniDriveConfig.MOTOR_ZERO_POWER_BEHAVIOR;
import static org.firstinspires.ftc.teamcode.Config.OmniDriveConfig.ROBOT_IMU_TYPE;

import com.qualcomm.hardware.bosch.BHI260IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Config.HardwareMapConfig;
import org.firstinspires.ftc.teamcode.Config.OmniDriveConfig;

public class OmniDrive {
    public enum DriveMode {
        ROBOT_CENTRIC, FIELD_CENTRIC
    }

    private final DriveMode driveMode;

    private final Telemetry telemetry;


    private final DcMotorEx leftFrontDrive;
    private final DcMotorEx leftBackDrive;
    private final DcMotorEx rightFrontDrive;
    private final DcMotorEx rightBackDrive;

    private IMU imu;

    public OmniDrive(HardwareMap hardwareMap, Telemetry telemetry, DriveMode driveMode) {
        leftFrontDrive = hardwareMap.get(DcMotorEx.class, HardwareMapConfig.left_front_drive_id);
        leftBackDrive = hardwareMap.get(DcMotorEx.class, HardwareMapConfig.left_back_drive_id);
        rightFrontDrive = hardwareMap.get(DcMotorEx.class, HardwareMapConfig.right_front_drive_id);
        rightBackDrive = hardwareMap.get(DcMotorEx.class, HardwareMapConfig.right_back_drive_id);

        leftFrontDrive.setDirection(OmniDriveConfig.LEFT_FRONT_DRIVE_DIRECTION);
        leftBackDrive.setDirection(OmniDriveConfig.LEFT_BACK_DRIVE_DIRECTION);
        rightFrontDrive.setDirection(OmniDriveConfig.RIGHT_FRONT_DRIVE_DIRECTION);
        rightBackDrive.setDirection(OmniDriveConfig.RIGHT_BACK_DRIVE_DIRECTION);

        leftFrontDrive.setZeroPowerBehavior(MOTOR_ZERO_POWER_BEHAVIOR);
        leftBackDrive.setZeroPowerBehavior(MOTOR_ZERO_POWER_BEHAVIOR);
        rightFrontDrive.setZeroPowerBehavior(MOTOR_ZERO_POWER_BEHAVIOR);
        rightBackDrive.setZeroPowerBehavior(MOTOR_ZERO_POWER_BEHAVIOR);

        this.driveMode = driveMode;

        if (driveMode == DriveMode.FIELD_CENTRIC) {
            if (ROBOT_IMU_TYPE == OmniDriveConfig.ImuType.BHI260)
                imu = hardwareMap.get(BHI260IMU.class, HardwareMapConfig.IMU_ID);
            else
                imu = hardwareMap.get(IMU.class, HardwareMapConfig.IMU_ID);


            RevHubOrientationOnRobot orientationOnRobot =
                    new RevHubOrientationOnRobot(
                            OmniDriveConfig.CONTROL_HUB_LOGO_DIRECTION,
                            OmniDriveConfig.CONTROL_HUB_USB_PORT_DIRECTION);

            imu.initialize(new IMU.Parameters(orientationOnRobot));
            imu.resetYaw();
        }


        this.telemetry = telemetry;
        telemetry.addData("Drive Train", "INITIALIZED");
    }

    public void driveFieldCentric(double forward, double strafe, double rotate) {
        double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
        double rotX = strafe * Math.cos(-botHeading) - forward * Math.sin(-botHeading);
        double rotY = strafe * Math.sin(-botHeading) + forward * Math.cos(-botHeading);

        // Normalizing the motor powers
        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rotate), 1);
        double frontLeftPower = (rotY + rotX + rotate) * MAX_SPEED / denominator;
        double backLeftPower = (rotY - rotX + rotate) * MAX_SPEED / denominator;
        double frontRightPower = (rotY - rotX - rotate) * MAX_SPEED / denominator;
        double backRightPower = (rotY + rotX - rotate) * MAX_SPEED / denominator;

        setPowersWithFeedForward(frontLeftPower, backLeftPower, frontRightPower, backRightPower);
    }

    public void driveRobotCentric(double forward, double strafe, double rotate) {
        double denominator = Math.max(Math.abs(forward) + Math.abs(strafe) + Math.abs(rotate), 1);
        double frontLeftPower  = (forward + strafe + rotate) * MAX_SPEED / denominator;
        double backLeftPower   = (forward - strafe + rotate) * MAX_SPEED / denominator;
        double frontRightPower = (forward - strafe - rotate) * MAX_SPEED / denominator;
        double backRightPower  = (forward + strafe - rotate) * MAX_SPEED / denominator;

        setPowersWithFeedForward(frontLeftPower, backLeftPower, frontRightPower, backRightPower);
    }

    private void setPowers(double leftFrontPower, double leftBackPower, double rightFrontPower, double rightBackPower) {
        leftFrontDrive.setPower(leftFrontPower);
        leftBackDrive.setPower(leftBackPower);
        rightFrontDrive.setPower(rightFrontPower);
        rightBackDrive.setPower(rightBackPower);
    }

    private void setPowersWithFeedForward(double leftFrontPower, double leftBackPower, double rightFrontPower, double rightBackPower) {
        setPowers(
                OmniDriveConfig.KS[0] * Math.signum(leftFrontPower) + OmniDriveConfig.KV[0] * leftFrontPower,
                OmniDriveConfig.KS[1] * Math.signum(leftBackPower) + OmniDriveConfig.KV[1] * leftBackPower,
                OmniDriveConfig.KS[2] * Math.signum(rightFrontPower) + OmniDriveConfig.KV[2] * rightFrontPower,
                OmniDriveConfig.KS[3] * Math.signum(rightBackPower) + OmniDriveConfig.KV[3] * rightBackPower
        );
    }

    public void tune() {
        telemetry.addData("left front velocity", leftFrontDrive.getVelocity());
        telemetry.addData("left back velocity", leftBackDrive.getVelocity());
        telemetry.addData("right front velocity", rightFrontDrive.getVelocity());
        telemetry.addData("right back velocity", rightBackDrive.getVelocity());
        setPowersWithFeedForward(1, 1, 1, 1);
    }


    public DriveMode getDriveMode() {
        return this.driveMode;
    }
}
