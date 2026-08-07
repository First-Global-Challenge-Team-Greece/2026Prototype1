package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Config.HardwareMapConfig;

public class Intake {

    public enum ExtensionState {
        EXTENDED, RETRACTED
    }

    private ExtensionState extensionState = ExtensionState.RETRACTED;

    private final double MAX_MOTOR_POWER = 0.8;
    private final boolean USE_SENSORS = false;

    private final Telemetry telemetry;

    private DcMotorEx leftExtension;
    private DcMotorEx rightExtension;

    private DcMotorEx intakeMotor;

    private DigitalChannel intakeExtensionSensor;
    private DigitalChannel intakeRetractionSensor;

    public Intake(HardwareMap hardwareMap, Telemetry telemetry) {
        leftExtension = hardwareMap.get(DcMotorEx.class, HardwareMapConfig.left_intake_extension_motor_id);
        rightExtension = hardwareMap.get(DcMotorEx.class, HardwareMapConfig.right_intake_extension_motor_id);
        leftExtension.setDirection(DcMotorSimple.Direction.REVERSE);

        intakeMotor = hardwareMap.get(DcMotorEx.class, HardwareMapConfig.intake_motor_id);

        if (USE_SENSORS) {
            intakeExtensionSensor = hardwareMap.get(DigitalChannel.class, HardwareMapConfig.intake_extension_magnetic_sensor_id);
            intakeRetractionSensor = hardwareMap.get(DigitalChannel.class, HardwareMapConfig.intake_retraction_magnetic_sensor_id);
            intakeExtensionSensor.setMode(DigitalChannel.Mode.INPUT);
            intakeRetractionSensor.setMode(DigitalChannel.Mode.INPUT);
        }

        this.telemetry = telemetry;
    }

    public void collect() {
        intakeMotor.setPower(MAX_MOTOR_POWER);
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
                    leftExtension.setPower(0);
                    rightExtension.setPower(0);
                    break;
                }

                leftExtension.setPower(MAX_MOTOR_POWER);
                rightExtension.setPower(MAX_MOTOR_POWER);
                break;
            case RETRACTED:
                if (intakeRetractionSensor.getState()) {
                    leftExtension.setPower(0);
                    rightExtension.setPower(0);
                    break;
                }

                leftExtension.setPower(-MAX_MOTOR_POWER);
                rightExtension.setPower(-MAX_MOTOR_POWER);
                break;
        }
    }

    public void MANUAL_EXTENSION_INTERFACE(double power) {
        leftExtension.setPower(power);
        rightExtension.setPower(power);
    }

    public double[] getMotorCurrents() {
        return new double[] {
                leftExtension.getCurrent(CurrentUnit.AMPS),
                rightExtension.getCurrent(CurrentUnit.AMPS),
                intakeMotor.getCurrent(CurrentUnit.AMPS)
        };
    }

    public void debug() {
        telemetry.addLine("|----- Intake -----|");
        telemetry.addData("Intake Current", intakeMotor.getCurrent(CurrentUnit.AMPS));
        telemetry.addData("Left Extension Current", leftExtension.getCurrent(CurrentUnit.AMPS));
        telemetry.addData("Right Extension Current", rightExtension.getCurrent(CurrentUnit.AMPS));
    }
}
