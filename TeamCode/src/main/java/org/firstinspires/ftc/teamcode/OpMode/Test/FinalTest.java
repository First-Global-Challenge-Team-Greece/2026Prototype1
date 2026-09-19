package org.firstinspires.ftc.teamcode.OpMode.Test;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.robotcore.external.navigation.VoltageUnit;
import org.firstinspires.ftc.teamcode.Subsystems.Brace;
import org.firstinspires.ftc.teamcode.Subsystems.DualMotorFlywheelShooter;
import org.firstinspires.ftc.teamcode.Subsystems.Feeder;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.TankDrive;
import org.firstinspires.ftc.teamcode.Subsystems.coralPrototype.Scheduler;
import org.firstinspires.ftc.teamcode.Subsystems.coralPrototype.SingleTriggerDecayTask;
import org.firstinspires.ftc.teamcode.Util.ButtonToggle;
import org.firstinspires.ftc.teamcode.Util.DebouncedButton;
import org.firstinspires.ftc.teamcode.Util.DriverNotifier;
import org.firstinspires.ftc.teamcode.Util.GamepadEx;
import org.firstinspires.ftc.teamcode.Util.GlobalDebugVariables;
import org.firstinspires.ftc.teamcode.Util.MatchTimer;

import java.util.ArrayList;
import java.util.List;

@Disabled
@TeleOp()
public class FinalTest extends OpMode {

    private TankDrive tankDrive;
    private Intake intake;
    private DualMotorFlywheelShooter shooter;
    private Feeder feeder;
    private Brace brace;

    private DebouncedButton intakeToggle;
    private DebouncedButton outtakeToggle;
    private ButtonToggle feederToggle;
    private ButtonToggle shooterToggle;

    private DriverNotifier notifier;

    private MatchTimer timer;

    private Scheduler scheduler;

    private List<LynxModule> hubs = new ArrayList<>();

    private boolean systemActive = true;

    @Override
    public void init() {
        tankDrive = new TankDrive(hardwareMap, telemetry, TankDrive.DriveMode.ROBOT_CENTRIC);
        intake = new Intake(hardwareMap, telemetry);
        shooter = new DualMotorFlywheelShooter(hardwareMap, telemetry);
        feeder = new Feeder(hardwareMap, telemetry);
        brace = new Brace(hardwareMap);

        intakeToggle = new DebouncedButton(300);
        outtakeToggle = new DebouncedButton(300);
        feederToggle = new ButtonToggle(300);
        shooterToggle = new ButtonToggle(300);

        notifier = new DriverNotifier(gamepad2);
        timer = new MatchTimer();

        scheduler = new Scheduler();

        hubs = hardwareMap.getAll(LynxModule.class);
    }

    @Override
    public void loop() {

        timer.startOnFirstInput(new GamepadEx(gamepad1));

        tankDrive.driveRobotCentric(-gamepad1.left_stick_y, gamepad1.right_stick_x);

        boolean intakeBind = intakeToggle.update(gamepad2.a);
        boolean outtakeBind = outtakeToggle.update(gamepad2.b);

        if (intakeBind && intake.getIntakeState() == Intake.IntakeState.COLLECTING) intake.setIntakeState(Intake.IntakeState.STOPPED);
        else if (outtakeBind && intake.getIntakeState() == Intake.IntakeState.DROPPING) intake.setIntakeState(Intake.IntakeState.STOPPED);
        else if (intakeBind) intake.setIntakeState(Intake.IntakeState.COLLECTING);
        else if (outtakeBind) intake.setIntakeState(Intake.IntakeState.DROPPING);

        intake.intakeStateManager();

        if (gamepad2.dpad_up) intake.MANUAL_EXTENSION_INTERFACE(0.8);
        else if (gamepad2.dpad_down) intake.MANUAL_EXTENSION_INTERFACE(-0.8);
        else intake.MANUAL_EXTENSION_INTERFACE(0);

        if (shooterToggle.update(gamepad2.x)) shooter.shoot();
        else shooter.idle();

        if (gamepad2.xWasPressed()) scheduler.addTask(new SingleTriggerDecayTask(
                () -> {
                    intake.MANUAL_EXTENSION_INTERFACE(-0.8);
                    telemetry.addLine("Extend!");
                },
                () -> intake.MANUAL_EXTENSION_INTERFACE(0),
                1000
        ));
        scheduler.next();

        shooter.shooterStateMachine();

        feeder.SHOOTER_INTERFACE(shooter.isReady());
        feeder.feed();
        feeder.mix();


        if (gamepad2.left_trigger_pressed) brace.retract();
        else if (gamepad2.right_trigger_pressed) brace.extend();
        else brace.stop();

        if (gamepad1.right_trigger_pressed) {
            brace.climb();
            if (systemActive) {
                systemActive = false;

                feeder.shutdown();
                intake.shutdown();
                shooter.shutdown();
            }
        } else brace.stall();

        shooter.updateBallCounts(timer.getElapsedTimeSeconds());

        tankDrive.debug();
        intake.debug();
        shooter.debug();
        feeder.debug();


        if (intake.isIntakeStalled())
            notifier.sendMessage(DriverNotifier.MessageLevel.WARNING);

        GlobalDebugVariables.dumpSystemCurrents(
                new double[][] {
                        tankDrive.getMotorCurrents(),
                        intake.getMotorCurrents(),
                        shooter.getMotorCurrents(),
                        feeder.getMotorCurrents()
                }
        );

        GlobalDebugVariables.update();

        telemetry.addLine("|----- System Info -----|");
        telemetry.addData("Total System Current", GlobalDebugVariables.currentSum);
        telemetry.addData("Peak System Current", GlobalDebugVariables.maxCurrentSum);
        telemetry.addData("Elapsed Time", timer.getElapsedTimeSeconds());
        telemetry.addData("System Status", systemActive ? "ACTIVE" : "DISABLED");

        for (LynxModule hub :
             hubs) {
            telemetry.addLine();
            telemetry.addLine("|----- " + hub.getDeviceName() + " -----|");
            telemetry.addData("Current", hub.getCurrent(CurrentUnit.AMPS));
            telemetry.addData("Input Voltage", hub.getInputVoltage(VoltageUnit.VOLTS));
        }
    }
}
