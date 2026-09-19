package org.firstinspires.ftc.teamcode.OpMode.Test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.Vision.TagCamera;
import org.firstinspires.ftc.teamcode.Subsystems.TankDrive;
import org.firstinspires.ftc.teamcode.Util.DriverNotifier;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.util.Optional;

@Disabled
@TeleOp()
public class AprilTagTest extends OpMode {

    private TagCamera tagCamera;
    private TankDrive tankDrive;
    private DriverNotifier driverNotifier;

    @Override
    public void init() {
        tankDrive = new TankDrive(hardwareMap, telemetry, TankDrive.DriveMode.ROBOT_CENTRIC);

        tagCamera = new TagCamera(hardwareMap, telemetry);

        driverNotifier = new DriverNotifier(gamepad1);
    }

    @Override
    public void loop() {
        Optional<AprilTagDetection> closestTag = tagCamera.getClosestDetection();
        boolean closestTagExists = closestTag.isPresent();
        int id = closestTagExists ? closestTag.get().id : -1;
        double distance = closestTagExists ? closestTag.get().ftcPose.hashCode() : -1;
        double bearing = closestTagExists ? closestTag.get().ftcPose.bearing : 0;

        tankDrive.driveRobotCentric(gamepad1.left_stick_y, -gamepad1.right_stick_x);

        telemetry.addData("Closest Tag", id);
        telemetry.addData("Distance From Tag", distance);
        telemetry.addData("Tag Bearing", bearing);
    }
}
