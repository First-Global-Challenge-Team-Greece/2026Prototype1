package org.firstinspires.ftc.teamcode.Subsystems;


import static org.firstinspires.ftc.teamcode.Config.OmniDriveConfig.MOTOR_ZERO_POWER_BEHAVIOR;
import static org.firstinspires.ftc.teamcode.Config.OmniDriveConfig.ROBOT_IMU_TYPE;

import com.github.bouyio.cyancore.geomery.Pose2D;
import com.github.bouyio.cyancore.pathing.engine.TankDriveVectorInterpreter;
import com.github.bouyio.cyancore.util.MathUtil;
import com.github.bouyio.cyancore.util.PIDCoefficients;
import com.github.bouyio.cyancore.util.PIDController;
import com.qualcomm.hardware.bosch.BHI260IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Config.HardwareMapConfig;
import org.firstinspires.ftc.teamcode.Config.OmniDriveConfig;
import org.firstinspires.ftc.teamcode.Config.TankDriveConfig;

public class TankDrive {

    private PIDController autotargetPID;
    private PIDCoefficients autoTargetCoefficients;
    private PIDController autoDrivePID;
    private PIDCoefficients autoDriveCoefficients;

    private TankDriveVectorInterpreter fieldCentricEngine;

    public enum DriveMode {
        ROBOT_CENTRIC, FIELD_CENTRIC
    }

    private final DriveMode driveMode;

    private final Telemetry telemetry;


    private final DcMotorEx leftDrive;
    private final DcMotorEx rightDrive;

    private IMU imu;

    public TankDrive(HardwareMap hardwareMap, Telemetry telemetry, DriveMode driveMode) {
        leftDrive = hardwareMap.get(DcMotorEx.class, HardwareMapConfig.left_drive_motor_id);
        rightDrive = hardwareMap.get(DcMotorEx.class, HardwareMapConfig.right_drive_motor_id);

        leftDrive.setDirection(TankDriveConfig.LEFT_MOTOR_DIRECTION);
        rightDrive.setDirection(TankDriveConfig.RIGHT_MOTOR_DIRECTION);

        leftDrive.setZeroPowerBehavior(MOTOR_ZERO_POWER_BEHAVIOR);
        rightDrive.setZeroPowerBehavior(MOTOR_ZERO_POWER_BEHAVIOR);

        this.driveMode = driveMode;

        if (driveMode == DriveMode.FIELD_CENTRIC) {
            if (ROBOT_IMU_TYPE == OmniDriveConfig.ImuType.BHI260)
                imu = hardwareMap.get(BHI260IMU.class, HardwareMapConfig.IMU_ID);
            else
                imu = hardwareMap.get(IMU.class, HardwareMapConfig.IMU_ID);

            RevHubOrientationOnRobot orientationOnRobot =
                    new RevHubOrientationOnRobot(
                            TankDriveConfig.CONTROL_HUB_LOGO_DIRECTION,
                            TankDriveConfig.CONTROL_HUB_USB_PORT_DIRECTION);

            imu.initialize(new IMU.Parameters(orientationOnRobot));
            imu.resetYaw();
        }

        autoTargetCoefficients = new PIDCoefficients(TankDriveConfig.AUTO_TARGET_KP, TankDriveConfig.AUTO_TARGET_KI, TankDriveConfig.AUTO_TARGET_KD);
        autotargetPID = new PIDController(autoTargetCoefficients);

        autoDriveCoefficients = new PIDCoefficients(TankDriveConfig.AUTO_DRIVE_KP, TankDriveConfig.AUTO_DRIVE_KI, TankDriveConfig.AUTO_DRIVE_KD);
        autoDrivePID = new PIDController(autoDriveCoefficients);

        fieldCentricEngine = new TankDriveVectorInterpreter(true, TankDriveVectorInterpreter.TankReverseSideParameters.RIGHT);

        this.telemetry = telemetry;
        telemetry.addData("Drive Train", "INITIALIZED");
    }

    public void driveFieldCentric(double x, double y) {
        double angle = Math.toRadians(imu.getRobotYawPitchRollAngles().getYaw()) - Math.atan2(y, x);

        fieldCentricEngine.process(new Pose2D(x, y, angle));
    }

    public void driveRobotCentric(double forward, double turn) {
        double denominator = Math.max(Math.abs(forward) + Math.abs(turn), 1);
        double leftPower = (forward + turn) / denominator;
        double rightPower = (forward - turn) / denominator;

        setPowers(leftPower, rightPower);
    }

    private void setPowers(double leftPower, double rightPower) {
        leftDrive.setPower(leftPower);
        rightDrive.setPower(rightPower);
    }

    public void driveToWildfire(double[] lumaValues) {
        autoTargetCoefficients.kP = TankDriveConfig.AUTO_TARGET_KP;
        autoTargetCoefficients.kI = TankDriveConfig.AUTO_TARGET_KI;
        autoTargetCoefficients.kD = TankDriveConfig.AUTO_TARGET_KD;

        autoDriveCoefficients.kP = TankDriveConfig.AUTO_DRIVE_KP;
        autoDriveCoefficients.kI = TankDriveConfig.AUTO_DRIVE_KI;
        autoDriveCoefficients.kD = TankDriveConfig.AUTO_DRIVE_KD;

        double leftLowerLuma = lumaValues[0] * TankDriveConfig.LOWER_LUMA_GAIN;
        double rightLowerLuma = lumaValues[1] * TankDriveConfig.LOWER_LUMA_GAIN;
        double leftUpperLuma = lumaValues[2] * TankDriveConfig.UPPER_LUMA_GAIN;
        double rightUpperLuma = lumaValues[3] * TankDriveConfig.UPPER_LUMA_GAIN;

        double turnError = (rightUpperLuma + rightLowerLuma) / 2 - (leftLowerLuma + leftUpperLuma) / 2;
        double driveError = (leftLowerLuma + rightLowerLuma) / 2 - (leftUpperLuma + rightUpperLuma)  / 2;

        driveRobotCentric(autoDrivePID.update(driveError), autotargetPID.update(turnError));
    }

    public void tune() {
        telemetry.addData("left velocity", leftDrive.getVelocity());
        telemetry.addData("right velocity", rightDrive.getVelocity());
        setPowersWithFeedForward(1, 1);
    }

    private void setPowersWithFeedForward(double leftPower, double rightPower) {
        setPowers(TankDriveConfig.KS_LEFT * Math.signum(leftPower) + TankDriveConfig.KV_LEFT * leftPower, TankDriveConfig.KS_RIGHT * Math.signum(rightPower) + TankDriveConfig.KV_RIGHT * rightPower);
    }


    public DriveMode getDriveMode() {
        return this.driveMode;
    }

    public double[] getMotorCurrents() {
        return new double[] {
                leftDrive.getCurrent(CurrentUnit.AMPS),
                rightDrive.getCurrent(CurrentUnit.AMPS)
        };
    }

    public void debug() {
        telemetry.addLine("|----- Drivetrain -----|");
        telemetry.addData("Left Drive Current", leftDrive.getCurrent(CurrentUnit.AMPS));
        telemetry.addData("Right Drive Current", rightDrive.getCurrent(CurrentUnit.AMPS));
    }
}
