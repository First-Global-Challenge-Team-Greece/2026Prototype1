package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Config.HardwareMapConfig;
import org.firstinspires.ftc.teamcode.Config.IntakeConfig;

public class Intake {

    public enum ExtensionState {
        EXTENDED, RETRACTED
    }

    public enum IntakeState {
        COLLECTING, DROPPING, STOPPED
    }

    private ExtensionState extensionState = ExtensionState.RETRACTED;
    private IntakeState intakeState = IntakeState.STOPPED;


    private final Telemetry telemetry;

    private final DcMotorEx extensionMotor;

    private final DcMotorEx intakeMotor;

    private DigitalChannel intakeExtensionSensor;
    private DigitalChannel intakeRetractionSensor;

    public Intake(HardwareMap hardwareMap, Telemetry telemetry) {
        extensionMotor = hardwareMap.get(DcMotorEx.class, HardwareMapConfig.intake_extension_motor_id);
        extensionMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        intakeMotor = hardwareMap.get(DcMotorEx.class, HardwareMapConfig.intake_motor_id);

        if (IntakeConfig.USE_SENSORS) {
            intakeExtensionSensor = hardwareMap.get(DigitalChannel.class, HardwareMapConfig.intake_extension_magnetic_sensor_id);
            intakeRetractionSensor = hardwareMap.get(DigitalChannel.class, HardwareMapConfig.intake_retraction_magnetic_sensor_id);
            intakeExtensionSensor.setMode(DigitalChannel.Mode.INPUT);
            intakeRetractionSensor.setMode(DigitalChannel.Mode.INPUT);
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

    public void extend() {
        extensionState = ExtensionState.EXTENDED;
    }

    public void retract() {
        extensionState = ExtensionState.RETRACTED;
    }

    public void extensionStateManager() {
        switch (extensionState) {
            case EXTENDED:
                if (intakeExtensionSensor.getState()) {
                    extensionMotor.setPower(0);
                    break;
                }

                extensionMotor.setPower(IntakeConfig.MAX_MOTOR_POWER);
                break;
            case RETRACTED:
                if (intakeRetractionSensor.getState()) {
                    extensionMotor.setPower(0);
                    break;
                }

                extensionMotor.setPower(-IntakeConfig.MAX_MOTOR_POWER);
                break;
        }
    }

    public void intakeStateManager() {
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

    public IntakeState getIntakeState() {
        return intakeState;
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
    }
}
