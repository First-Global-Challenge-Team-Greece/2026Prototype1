package org.firstinspires.ftc.teamcode.OpMode.Test;

import com.github.bouyio.cyancore.debugger.Debuggers;
import com.github.bouyio.cyancore.geomery.Point;
import com.github.bouyio.cyancore.pathing.Path;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.TankDrive;
@Disabled

@TeleOp()
public class PathFollowingTest extends OpMode {

    private TankDrive tankDrive;

    private Path thePath = new Path(
            new Point(60, 60),
            new Point(100, 120),
            new Point(140, -20)
    );

    @Override
    public void init() {
        tankDrive = new TankDrive(hardwareMap, telemetry, TankDrive.DriveMode.FIELD_CENTRIC);
        Debuggers.init();
    }

    @Override
    public void loop() {
        telemetry.addData(">", tankDrive.getCurrentPosition().toString());

        if (gamepad1.left_bumper) {
            tankDrive.followPath(thePath);
        } else {
            tankDrive.driveRobotCentric(0, 0);
        }

    }
}
