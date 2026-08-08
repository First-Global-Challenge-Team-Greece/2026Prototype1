package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Config.HardwareMapConfig;
import org.firstinspires.ftc.teamcode.Config.IntakeConfig;

public class Feeder {
    private DcMotorEx feederMotor;
    private boolean isShooterReady = false;
    private Telemetry telemetry;

    public Feeder(HardwareMap hardwareMap, Telemetry telemetry) {
        feederMotor = hardwareMap.get(DcMotorEx.class, HardwareMapConfig.feeder_motor_id);
        this.telemetry = telemetry;
    }

    public void SHOOTER_INTERFACE(boolean isShooterReady) {
        this.isShooterReady = isShooterReady;
    }

    public void feed() {
        if (isShooterReady) {
            feederMotor.setPower(IntakeConfig.MAX_MOTOR_POWER);
        } else stop();
    }

    public void stop() {
        feederMotor.setPower(0);
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
