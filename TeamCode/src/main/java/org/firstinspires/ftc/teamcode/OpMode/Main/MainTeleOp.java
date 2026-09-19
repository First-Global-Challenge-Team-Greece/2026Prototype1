package org.firstinspires.ftc.teamcode.OpMode.Main;


import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.robotcore.external.navigation.VoltageUnit;
import org.firstinspires.ftc.teamcode.Config.HardwareMapConfig;
import org.firstinspires.ftc.teamcode.Subsystems.Brace;
import org.firstinspires.ftc.teamcode.Subsystems.DualMotorFlywheelShooter;
import org.firstinspires.ftc.teamcode.Subsystems.Feeder;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.TankDrive;
import org.firstinspires.ftc.teamcode.Subsystems.coralPrototype.ContinuousTriggerDecayTask;
import org.firstinspires.ftc.teamcode.Subsystems.coralPrototype.Scheduler;
import org.firstinspires.ftc.teamcode.Util.CurrentTracker;
import org.firstinspires.ftc.teamcode.Util.DriverNotifier;
import org.firstinspires.ftc.teamcode.Util.GamepadEx;
import org.firstinspires.ftc.teamcode.Util.MatchTimer;
import org.firstinspires.ftc.teamcode.Util.TelemetryDivider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@TeleOp()
public class MainTeleOp extends OpMode {

    private TankDrive drivetrain;

    private Intake intake;

    private Feeder feeder;
    private DualMotorFlywheelShooter shooter;

    private Brace brace;

    private GamepadEx primaryGamepad;
    private GamepadEx secondaryGamepad;
    private Scheduler scheduler;

    private CurrentTracker currentTracker;
    private int multiFunctionDisplayMode = 0;
    private MatchTimer timer;

    private boolean systemActive = true;
    private boolean hasBraceExtended = false;
    private boolean hasMatchStarted = false;

    private final List<Runnable> mfdDisplays = new ArrayList<>();

    @Override
    public void init() {
        drivetrain = new TankDrive(hardwareMap, telemetry, TankDrive.DriveMode.ROBOT_CENTRIC);
        intake = new Intake(hardwareMap, telemetry);

        shooter = new DualMotorFlywheelShooter(hardwareMap, telemetry);
        shooter.stop();
        feeder = new Feeder(hardwareMap, telemetry);

        brace = new Brace(hardwareMap);

        primaryGamepad = new GamepadEx(gamepad1);
        secondaryGamepad = new GamepadEx(gamepad2);

        scheduler = new Scheduler();

        List<String> motorNames = Arrays.asList(HardwareMapConfig.allMotors());

        currentTracker = new CurrentTracker(
                motorNames,
                hardwareMap
        );

        timer = new MatchTimer();

        mfdDisplays.add(() -> {
            telemetry.addLine(TelemetryDivider.generate("Match View", 10));
            telemetry.addLine();

            telemetry.addData("Elapsed Time", timer.getElapsedTimeSeconds());
            telemetry.addData("System Status", systemActive ? "ACTIVE" : "DISABLED");
            telemetry.addLine();

            telemetry.addData("Brace Extension Status", hasBraceExtended ? "FREE" : "RETRACTED");
            telemetry.addData("Match Status", hasMatchStarted ? "RUNNING" : "WAITING");
            telemetry.addLine();

            currentTracker.update();
            telemetry.addData("System Current", currentTracker.getTotalCurrent());
            telemetry.addData("Peak System Current", currentTracker.getMaxTotalCurrent());
        });

    }

    @Override
    public void loop() {
        timer.startOnFirstInput(primaryGamepad, secondaryGamepad);
        primaryGamepad.update(); secondaryGamepad.update();

        scheduler.next();

        if (primaryGamepad.hasInput() || secondaryGamepad.hasInput()) {
            intake.startMatch();
            feeder.mix();
            hasMatchStarted = true;
        }

        telemetry.addLine("Page " + (int)(multiFunctionDisplayMode + 1) + " out of " + mfdDisplays.size());
        mfdDisplays.get(multiFunctionDisplayMode).run();

        if (!hasMatchStarted) return;

        drivetrain.drive(-gamepad1.left_stick_y, gamepad1.right_stick_x);

        boolean intakeBind = secondaryGamepad.debouncedInput(GamepadEx.ButtonName.A);
        boolean outtakeBind = secondaryGamepad.debouncedInput(GamepadEx.ButtonName.B);


        if (intakeBind && intake.getIntakeState() == Intake.IntakeState.COLLECTING) intake.setIntakeState(Intake.IntakeState.STOPPED);
        else if (outtakeBind && intake.getIntakeState() == Intake.IntakeState.DROPPING) intake.setIntakeState(Intake.IntakeState.STOPPED);
        else if (intakeBind) intake.setIntakeState(Intake.IntakeState.COLLECTING);
        else if (outtakeBind) intake.setIntakeState(Intake.IntakeState.DROPPING);
        feeder.SHOOTER_INTERFACE(shooter.isReady());

        if (intake.isIntakeStalled()) secondaryGamepad.notifier.sendMessage(DriverNotifier.MessageLevel.ERROR);

        if (secondaryGamepad.toggle(GamepadEx.ButtonName.X)) {
            shooter.shoot();
            feeder.feed();
        } else {
            shooter.idle();
            feeder.stop();
        }

        braceExtensionHandler();

        if (primaryGamepad.getRightTrigger() > 0.5) {
            brace.climb();
            if (systemActive) {
                systemActive = false;

                feeder.shutdown();
                intake.shutdown();
                shooter.shutdown();
            }
        } else brace.stall();


        intake.intakeStateManager();
        intake.extensionStateManager();
        shooter.shooterStateMachine();

        if (secondaryGamepad.debouncedInput(GamepadEx.ButtonName.RIGHT_STICK_BUTTON)) {
            multiFunctionDisplayMode++;
            if (multiFunctionDisplayMode > mfdDisplays.size() -1) multiFunctionDisplayMode = 0;
        } else if (secondaryGamepad.debouncedInput(GamepadEx.ButtonName.LEFT_STICK_BUTTON)) {
            multiFunctionDisplayMode--;
            if (multiFunctionDisplayMode <0) multiFunctionDisplayMode = mfdDisplays.size() - 1;
        }

    }

    private void braceExtensionHandler() {
        if (secondaryGamepad.debouncedInput(GamepadEx.ButtonName.DPAD_UP) && !hasBraceExtended) {
            scheduler.addTask(new ContinuousTriggerDecayTask(
                    brace::extend,
                    () -> {
                        brace.stop();
                        hasBraceExtended = true;
                    },
                    3000
            ));
        }

        if (!hasBraceExtended) return;

        if (secondaryGamepad.isDown(GamepadEx.ButtonName.DPAD_UP)) brace.extend();
        else if (secondaryGamepad.isDown(GamepadEx.ButtonName.DPAD_DOWN)) brace.retract();
        else brace.stop();
    }
}
