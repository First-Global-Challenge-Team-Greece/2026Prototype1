package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Config.FlywheelShooterConfig;
import org.firstinspires.ftc.teamcode.Config.HardwareMapConfig;

public class DualMotorFlywheelShooter extends FlywheelShooter {

    private final DcMotorEx leftShooterMotor;
    private final DcMotorEx rightShooterMotor;

    private final Telemetry telemetry;

    public DualMotorFlywheelShooter(HardwareMap hardwareMap, Telemetry telemetry) {
        leftShooterMotor = hardwareMap.get(DcMotorEx.class, HardwareMapConfig.left_shooter_motor_id);
        rightShooterMotor = hardwareMap.get(DcMotorEx.class, HardwareMapConfig.right_shooter_motor_id);
        leftShooterMotor.setDirection(FlywheelShooterConfig.LEFT_FLYWHEEL_MOTOR_DIRECTION);
        rightShooterMotor.setDirection(FlywheelShooterConfig.RIGHT_FLYWHEEL_MOTOR_DIRECTION);

        this.telemetry = telemetry;
    }

    @Override
    void applyPower(double power) {
        leftShooterMotor.setPower(power);
        rightShooterMotor.setPower(power);
    }

    @Override
    double getVelocity() {
        return ((leftShooterMotor.getVelocity() * FlywheelShooterConfig.SECOND_TO_MINUTE_COEFFICIENT / FlywheelShooterConfig.ENCODER_CPR) +
                (rightShooterMotor.getVelocity() * FlywheelShooterConfig.SECOND_TO_MINUTE_COEFFICIENT / FlywheelShooterConfig.ENCODER_CPR)) / 2;
    }

    public double[] getMotorCurrents() {
        return new double[] {
                leftShooterMotor.getCurrent(CurrentUnit.AMPS),
                rightShooterMotor.getCurrent(CurrentUnit.AMPS)
        };
    }

    public void debug() {
        telemetry.addLine("|----- Shooter -----|");
        super.debug(telemetry);
        telemetry.addLine();
        telemetry.addData("Left Shooter Current", leftShooterMotor.getCurrent(CurrentUnit.AMPS));
        telemetry.addData("Right Shooter Current", rightShooterMotor.getCurrent(CurrentUnit.AMPS));
    }
}
