package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Config.HardwareMapConfig;
import org.firstinspires.ftc.teamcode.Config.IntakeConfig;
import org.firstinspires.ftc.teamcode.Util.CurrentTracker;

public class Intake {


    public enum IntakeState {
        COLLECTING, DROPPING, STOPPED
    }

    private IntakeState intakeState = IntakeState.STOPPED;


    private final Telemetry telemetry;

    private final DcMotorEx extensionMotor;

    private final DcMotorEx intakeMotor;

    private DigitalChannel intakeExtensionSensor;

    private boolean isActive = false;
    private boolean hasShutdown = false;

    public Intake(HardwareMap hardwareMap, Telemetry telemetry) {
        extensionMotor = hardwareMap.get(DcMotorEx.class, HardwareMapConfig.intake_extension_motor_id);
        extensionMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        intakeMotor = hardwareMap.get(DcMotorEx.class, HardwareMapConfig.intake_motor_id);
        intakeMotor.setDirection(IntakeConfig.INTAKE_DIRECTION);

        if (IntakeConfig.USE_SENSORS) {
            intakeExtensionSensor = hardwareMap.get(DigitalChannel.class, HardwareMapConfig.intake_extension_magnetic_sensor_id);
            intakeExtensionSensor.setMode(DigitalChannel.Mode.INPUT);
        }

        this.telemetry = telemetry;
    }

    public void collect() {
        intakeMotor.setPower(IntakeConfig.MAX_MOTOR_POWER);
    }

    public void drop() {
        intakeMotor.setPower(-IntakeConfig.MAX_MOTOR_POWER);
    }

    public void stop() {
        intakeMotor.setPower(0);
    }


    public void startMatch() {
        isActive = !hasShutdown;
    }

    public void extensionStateManager() {
        if (!isActive) return;

        telemetry.addLine("State Manager Run");

        if (intakeExtensionSensor.getState()) {
            extend();
            telemetry.addLine("Extended");
        }
        else stopExtension();
    }

    public void intakeStateManager() {
        if (!isActive) intakeState = IntakeState.STOPPED;

        switch (intakeState) {
            case STOPPED:
                stop();
                break;
            case DROPPING:
                drop();
                break;
            case COLLECTING:
                collect();
                break;
        }
    }

    public void setIntakeState(IntakeState intakeState) {
        this.intakeState = intakeState;
    }

    public boolean isIntakeStalled() {
        return intakeMotor.getVelocity() == 0 && intakeState != IntakeState.STOPPED;
    }

    public IntakeState getIntakeState() {
        return intakeState;
    }

    public void extend() {
        extensionMotor.setPower(IntakeConfig.MAX_MOTOR_POWER);
    }

    public void retract() {
        extensionMotor.setPower(-IntakeConfig.MAX_MOTOR_POWER);
    }

    public void stopExtension() {
        extensionMotor.setPower(0);
    }

    public void shutdown() {
        intakeMotor.close();
        extensionMotor.close();

        hasShutdown = true;
    }

    public void MANUAL_EXTENSION_INTERFACE(double power) {
        extensionMotor.setPower(power);
    }

    public double[] getMotorCurrents() {
        return new double[] {
                extensionMotor.getCurrent(CurrentUnit.AMPS),
                intakeMotor.getCurrent(CurrentUnit.AMPS)
        };
    }

    public void debug() {
        telemetry.addLine("|----- Intake -----|");
        telemetry.addData("Intake Current", intakeMotor.getCurrent(CurrentUnit.AMPS));
        telemetry.addData("Extension Current", extensionMotor.getCurrent(CurrentUnit.AMPS));

        telemetry.addData("Sensor status", intakeExtensionSensor.getState());
    }
}
