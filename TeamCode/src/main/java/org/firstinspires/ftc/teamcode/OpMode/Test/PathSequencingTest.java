package org.firstinspires.ftc.teamcode.OpMode.Test;

import com.github.bouyio.cyancore.debugger.Debuggers;
import com.github.bouyio.cyancore.geomery.Point;
import com.github.bouyio.cyancore.pathing.Path;
import com.github.bouyio.cyancore.pathing.PathSequence;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.TankDrive;
@Disabled

@TeleOp()
public class PathSequencingTest extends OpMode {

    private TankDrive tankDrive;

    private final Path p1 = new Path(
            new Point(0, 0),
            new Point(60, 120)
    );

    private final Path p2 = new Path(
            new Point(60, 120),
            new Point(120, 60),
            new Point(60, 0)
    );

    private final Path p3 = new Path(
            new Point(30, 0)
    );

    private PathSequence seq;



    @Override
    public void init() {
        tankDrive = new TankDrive(hardwareMap, telemetry, TankDrive.DriveMode.FIELD_CENTRIC);
        Debuggers.init();

        seq = new PathSequence(
                tankDrive.getOdometry(),
                4,
                p1,
                p2);
    }

    @Override
    public void loop() {
        telemetry.addData(">", tankDrive.getCurrentPosition().toString());

        if (gamepad1.left_bumper) {
            tankDrive.followPathSequence(seq);
        } else {
            tankDrive.driveRobotCentric(0, 0);
        }

        tankDrive.cyanDebug();

    }
}
