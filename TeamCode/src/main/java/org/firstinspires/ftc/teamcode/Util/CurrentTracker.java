package org.firstinspires.ftc.teamcode.Util;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Utility class for monitoring motor current consumption.
 *
 * <p>
 * Tracks:
 * <ul>
 * <li>Instantaneous current per motor</li>
 * <li>Maximum current per motor</li>
 * <li>Total instantaneous current</li>
 * <li>Maximum total current</li>
 * </ul>
 */
public class CurrentTracker {

    private final Map<String, DcMotorEx> motors;
    private final Map<String, Double> currentByMotor;
    private final Map<String, Double> maxCurrentByMotor;

    private double totalCurrent;
    private double maxTotalCurrent;

    /**
     * Creates a new CurrentTracker.
     *
     * @param motorNames  list of motor names to track
     * @param hardwareMap hardware map used to access motors
     */
    public CurrentTracker(
            List<String> motorNames,
            HardwareMap hardwareMap) {
        Objects.requireNonNull(
                hardwareMap,
                "hardwareMap cannot be null");
        Objects.requireNonNull(
                motorNames,
                "motorNames cannot be null");

        this.currentByMotor = new LinkedHashMap<>();
        this.maxCurrentByMotor = new LinkedHashMap<>();
        this.motors = new LinkedHashMap<>();

        for (String motorName : motorNames) {
            motors.put(motorName, hardwareMap.get(DcMotorEx.class, motorName));
        }

        for (String motorName : this.motors.keySet()) {
            currentByMotor.put(motorName, 0.0);
            maxCurrentByMotor.put(motorName, 0.0);
        }
    }

    /**
     * Reads the current consumption of all tracked motors and
     * updates maximum values
     *
     * <p>
     * Should be called once during each OpMode loop iteration.
     * </p>
     */
    public void update() {
        totalCurrent = 0.0;

        for (Map.Entry<String, DcMotorEx> entry : motors.entrySet()) {
            String motorName = entry.getKey();
            DcMotorEx motor = entry.getValue();

            double current = motor.getCurrent(CurrentUnit.AMPS);

            currentByMotor.put(motorName, current);
            totalCurrent += current;

            double previousMax = maxCurrentByMotor.get(motorName);

            if (current > previousMax) {
                maxCurrentByMotor.put(motorName, current);
            }
        }

        if (totalCurrent > maxTotalCurrent) {
            maxTotalCurrent = totalCurrent;
        }
    }

    /**
     * Adds the current measurements to telemetry.
     */
    public void addTelemetry(Telemetry telemetry) {
        telemetry.addLine("--- Motor Current Tracker ---");

        for (String motorName : motors.keySet()) {
            telemetry.addData(
                    motorName + " Current (A)",
                    currentByMotor.get(motorName));

            telemetry.addData(
                    motorName + " Max Current (A)",
                    maxCurrentByMotor.get(motorName));
            telemetry.addLine();
        }

        telemetry.addLine("-----------------------------");
        telemetry.addData("Total Current (A)", totalCurrent);
        telemetry.addData("Max Total Current (A)", maxTotalCurrent);
    }

    /**
     * Returns the latest current reading for a motor.
     *
     * @param motorName name of the motor
     * @return current in amps, or 0 if the motor is not tracked
     */
    public double getCurrent(String motorName) {
        return currentByMotor.getOrDefault(motorName, 0.0);
    }

    /**
     * Returns the maximum recorded current for a motor.
     *
     * @param motorName name of the motor
     * @return maximum current in A, or 0 if the motor is not tracked
     */
    public double getMaxCurrent(String motorName) {
        return maxCurrentByMotor.getOrDefault(motorName, 0.0);
    }

    /**
     * Returns the latest total current of all tracked motors.
     *
     * @return total current in A
     */
    public double getTotalCurrent() {
        return totalCurrent;
    }

    /**
     * Returns the maximum recorded total current.
     *
     * @return maximum total current in A
     */
    public double getMaxTotalCurrent() {
        return maxTotalCurrent;
    }

    /**
     * Resets all current measurements.
     */
    public void reset() {
        for (String motorName : motors.keySet()) {
            currentByMotor.put(motorName, 0.0);
            maxCurrentByMotor.put(motorName, 0.0);
        }

        totalCurrent = 0.0;
        maxTotalCurrent = 0.0;
    }
}
