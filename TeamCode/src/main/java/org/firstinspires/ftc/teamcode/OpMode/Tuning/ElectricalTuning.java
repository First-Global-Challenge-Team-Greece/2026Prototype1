package org.firstinspires.ftc.teamcode.OpMode.Tuning;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.TankDrive;
import org.firstinspires.ftc.teamcode.Util.ButtonToggle;
import org.firstinspires.ftc.teamcode.Util.DebouncedButton;
import org.firstinspires.ftc.teamcode.Util.GlobalDebugVariables;

@TeleOp()
public class ElectricalTuning extends OpMode {

    private DebouncedButton intakeButton;
    private DebouncedButton outtakeButton;

    private TankDrive tankDrive;
    private Intake intake;

    @Override
    public void init() {
        intakeButton = new DebouncedButton(300);
        outtakeButton = new DebouncedButton(300);
        intake = new Intake(hardwareMap, telemetry);
        tankDrive = new TankDrive(hardwareMap, telemetry, TankDrive.DriveMode.ROBOT_CENTRIC);
    }


    @Override
    public void loop() {
        if (gamepad1.dpad_up) {
            intake.MANUAL_EXTENSION_INTERFACE(0.8);
        } else if (gamepad1.dpad_down) {
            intake.MANUAL_EXTENSION_INTERFACE(-0.8);
        } else intake.MANUAL_EXTENSION_INTERFACE(0);

        boolean intakeBinding = intakeButton.update(gamepad1.a);
        boolean outtakeBinding = outtakeButton.update(gamepad1.b);

        if (intakeBinding && intake.getIntakeState() == Intake.IntakeState.COLLECTING)
            intake.setIntakeState(Intake.IntakeState.STOPPED);

        else if (outtakeBinding && intake.getIntakeState() == Intake.IntakeState.DROPPING)
            intake.setIntakeState(Intake.IntakeState.STOPPED);

        else if (intakeBinding)
            intake.setIntakeState(Intake.IntakeState.COLLECTING);

        else if (outtakeBinding)
            intake.setIntakeState(Intake.IntakeState.DROPPING);


        intake.intakeStateManager();

        tankDrive.driveRobotCentric(-gamepad1.left_stick_y, gamepad1.right_stick_x);

        GlobalDebugVariables.dumpSystemCurrents(new double[][]{
                tankDrive.getMotorCurrents(),
                intake.getMotorCurrents()
        });

        GlobalDebugVariables.update();

        tankDrive.debug();
        intake.debug();

        telemetry.addLine("|----- System Info -----|");
        telemetry.addData("Total System Current", GlobalDebugVariables.currentSum);
        telemetry.addData("Peak System Current", GlobalDebugVariables.maxCurrentSum);

        telemetry.addData("State", intake.getIntakeState());
    }
}
