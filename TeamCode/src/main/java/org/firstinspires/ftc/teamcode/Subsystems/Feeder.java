package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Config.HardwareMapConfig;
import org.firstinspires.ftc.teamcode.Config.IntakeConfig;

public class Feeder {
    private DcMotorEx feederMotor;
    private boolean isShooterReady = false;
    private Telemetry telemetry;

    private CRServo rightSuckerServo;
    private CRServo leftSuckerServo;

    private boolean isActive = true;

    public Feeder(HardwareMap hardwareMap, Telemetry telemetry) {
        feederMotor = hardwareMap.get(DcMotorEx.class, HardwareMapConfig.feeder_motor_id);
        feederMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        rightSuckerServo = hardwareMap.get(CRServo.class, HardwareMapConfig.right_mixer_continuous_servo_id);
        leftSuckerServo = hardwareMap.get(CRServo.class, HardwareMapConfig.left_mixer_continuous_servo_id);
        rightSuckerServo.setDirection(DcMotorSimple.Direction.FORWARD);
        leftSuckerServo.setDirection(DcMotorSimple.Direction.REVERSE);
        this.telemetry = telemetry;
    }

    public void SHOOTER_INTERFACE(boolean isShooterReady) {
        this.isShooterReady = isShooterReady;
    }

    public void feed() {
        if (!isActive) return;
        if (isShooterReady) {
            feederMotor.setPower(IntakeConfig.MAX_MOTOR_POWER);
        } else stop();
    }

    public void mix() {
        if (!isActive) return;
        rightSuckerServo.setPower(1);
        leftSuckerServo.setPower(1);
    }

    public void stop() {
        feederMotor.setPower(0);
    }

    public void shutdown() {
        rightSuckerServo.setPower(0);
        leftSuckerServo.setPower(0);

        feederMotor.close();
        isActive = false;
    }

    public double[] getMotorCurrents() {
        return new double[] {
                feederMotor.getCurrent(CurrentUnit.AMPS)
        };
    }

    public void debug() {
        telemetry.addLine("|----- Feeder -----|");
        telemetry.addData("Feeder Motor Current", feederMotor.getCurrent(CurrentUnit.AMPS));
        telemetry.addData("Shooter Ready", isShooterReady);
    }
}
