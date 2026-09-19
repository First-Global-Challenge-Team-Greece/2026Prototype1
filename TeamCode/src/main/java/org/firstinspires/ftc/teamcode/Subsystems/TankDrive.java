package org.firstinspires.ftc.teamcode.Subsystems;


import static org.firstinspires.ftc.teamcode.Config.OmniDriveConfig.*;

import com.github.bouyio.cyancore.debugger.Debuggers;
import com.github.bouyio.cyancore.debugger.formating.MessageLevel;
import com.github.bouyio.cyancore.geomery.Point;
import com.github.bouyio.cyancore.geomery.Pose2D;
import com.github.bouyio.cyancore.localization.GyroTankOdometry;
import com.github.bouyio.cyancore.localization.PositionProvider;
import com.github.bouyio.cyancore.pathing.Path;
import com.github.bouyio.cyancore.pathing.PathSequence;
import com.github.bouyio.cyancore.pathing.engine.PathFollower;
import com.github.bouyio.cyancore.pathing.engine.TankDriveVectorInterpreter;
import com.github.bouyio.cyancore.util.*;
import com.github.bouyio.cyanftc.debugger.TelemetryExporter;
import com.qualcomm.hardware.bosch.BHI260IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Config.*;

public class TankDrive {

    private PIDController autotargetPID;
    private PIDCoefficients autoTargetCoefficients;
    private PIDController autoDrivePID;
    private PIDCoefficients autoDriveCoefficients;

    private final TelemetryExporter exporter;

    private GyroTankOdometry odometry = null;
    private PathFollower pathFollower = null;


    public enum DriveMode {
        ROBOT_CENTRIC, FIELD_CENTRIC
    }

    private final DriveMode driveMode;

    private final Telemetry telemetry;


    private final DcMotorEx leftDrive;
    private final DcMotorEx rightDrive;

    private IMU imu;

    private int jiggleSign = -1;

    private long lastJiggleSwitch;

    private int jerkSign = -1;

    private long lastJerkSwitch;

    public TankDrive(HardwareMap hardwareMap, Telemetry telemetry, DriveMode driveMode) {
        leftDrive = hardwareMap.get(DcMotorEx.class, HardwareMapConfig.left_drive_motor_id);
        rightDrive = hardwareMap.get(DcMotorEx.class, HardwareMapConfig.right_drive_motor_id);

        leftDrive.setDirection(TankDriveConfig.LEFT_MOTOR_DIRECTION);
        rightDrive.setDirection(TankDriveConfig.RIGHT_MOTOR_DIRECTION);

        leftDrive.setZeroPowerBehavior(MOTOR_ZERO_POWER_BEHAVIOR);
        rightDrive.setZeroPowerBehavior(MOTOR_ZERO_POWER_BEHAVIOR);

        this.driveMode = driveMode;

        exporter = new TelemetryExporter(telemetry, 50);
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

            GyroTankOdometry.MeasurementProvider measurementProvider = new GyroTankOdometry.MeasurementProvider(
                    leftDrive::getCurrentPosition,
                    rightDrive::getCurrentPosition,
                    () -> imu.getRobotYawPitchRollAngles().getYaw(),
                    TankDriveConfig.TICKS_TO_CM
            );

            odometry = new GyroTankOdometry(Distance.DistanceUnit.CM, measurementProvider);

            pathFollower = new PathFollower(odometry,
                    new TankDriveVectorInterpreter(true, TankDriveVectorInterpreter.TankReverseSideParameters.RIGHT),
                    new PIDController(1 , 0, 0));
            pathFollower.purePursuitSetUp(1, 4);

            pathFollower.attachLogger(Debuggers.getGlobalLogger());
            pathFollower.attachExporters(exporter);

        }
        lastJiggleSwitch = System.currentTimeMillis();
        lastJerkSwitch = System.currentTimeMillis();

        autoTargetCoefficients = new PIDCoefficients(TankDriveConfig.AUTO_TARGET_KP, TankDriveConfig.AUTO_TARGET_KI, TankDriveConfig.AUTO_TARGET_KD);
        autotargetPID = new PIDController(autoTargetCoefficients);

        autoDriveCoefficients = new PIDCoefficients(TankDriveConfig.AUTO_DRIVE_KP, TankDriveConfig.AUTO_DRIVE_KI, TankDriveConfig.AUTO_DRIVE_KD);
        autoDrivePID = new PIDController(autoDriveCoefficients);


        this.telemetry = telemetry;
        telemetry.addData("Drive Train", "INITIALIZED");
    }

    public void driveFieldCentric(double x, double y) {
        double angle = Math.toDegrees(Math.atan2(y, x));
        double robotHeading = imu.getRobotYawPitchRollAngles().getYaw();

        angle = MathUtil.shiftAngle(robotHeading, angle);

        double turn = angle / 180;
        double forward = Math.hypot(x, y);

        driveRobotCentric(forward, turn);

    }

    public void drive(double forward, double turn) {
        double denominator = Math.max(Math.abs(forward) + Math.abs(turn), 1);
        double leftPower = (forward + turn) / denominator;
        double rightPower = (forward - turn) / denominator;

        setPowersWithFeedForward(leftPower, rightPower);
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
        setPowers(TankDriveConfig.KS_LEFT * Math.signum(leftPower) + TankDriveConfig.KV_LEFT * leftPower,
                TankDriveConfig.KS_RIGHT * Math.signum(rightPower) + TankDriveConfig.KV_RIGHT * rightPower);
    }


    public Pose2D getCurrentPosition() {
        odometry.update();
        return odometry.getPose();
    }

    public void followPoint(Point point) {
        odometry.update();

        pathFollower.followPoint(point);

        double[] motorPowers = pathFollower.getCalculatedPowers();
        setPowers(motorPowers[TankDriveVectorInterpreter.LEFT_MOTOR_INDEX_ID], motorPowers[TankDriveVectorInterpreter.RIGHT_MOTOR_INDEX_ID]);
        pathFollower.log();
    }

    public void followPath(Path path) {
        odometry.update();

        pathFollower.followPath(path);

        double[] motorPowers = pathFollower.getCalculatedPowers();
        setPowers(motorPowers[TankDriveVectorInterpreter.LEFT_MOTOR_INDEX_ID], motorPowers[TankDriveVectorInterpreter.RIGHT_MOTOR_INDEX_ID]);
        pathFollower.log();
    }

    public void followPathSequence(PathSequence sequence) {
        odometry.update();

        pathFollower.followPathSequence(sequence);

        double[] motorPowers = pathFollower.getCalculatedPowers();
        setPowers(motorPowers[TankDriveVectorInterpreter.LEFT_MOTOR_INDEX_ID], motorPowers[TankDriveVectorInterpreter.RIGHT_MOTOR_INDEX_ID]);
        pathFollower.log();
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

    public PositionProvider getOdometry() {
        return odometry;
    }

    public void debug() {
        telemetry.addLine("|----- Drivetrain -----|");
        telemetry.addData("Left Drive Current", leftDrive.getCurrent(CurrentUnit.AMPS));
        telemetry.addData("Right Drive Current", rightDrive.getCurrent(CurrentUnit.AMPS));
        telemetry.addData("Forward Velocity", forwardVelocity());
    }

    public void cyanDebug() {
        exporter.export();
    }

    public void jiggle() {
        if (System.currentTimeMillis() - lastJiggleSwitch > 300) {
            lastJiggleSwitch = System.currentTimeMillis();
            jiggleSign *= -1;
        }

        driveRobotCentric(0, jiggleSign * 0.8);
    }

    public void jerk(boolean SHOOTER_INTERFACE) {
        if (!SHOOTER_INTERFACE) return;

        if (System.currentTimeMillis() - lastJerkSwitch > 200) {
            lastJerkSwitch = System.currentTimeMillis();
            jerkSign *= -1;
        }

        driveRobotCentric(jerkSign * 0.8, 0);
    }

    public double forwardVelocity() {
        return (leftDrive.getVelocity() + rightDrive.getVelocity()) / 2;
    }

}
