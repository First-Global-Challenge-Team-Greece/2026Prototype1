package org.firstinspires.ftc.teamcode.OpMode.Test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.TankDrive;
import org.firstinspires.ftc.teamcode.Subsystems.WildfireCamera;

@TeleOp()
public class AutoTargetTest extends OpMode {

    private WildfireCamera wildfireCamera;
    private TankDrive tankDrive;

    @Override
    public void init() {
        wildfireCamera = new WildfireCamera(hardwareMap, telemetry);
        tankDrive = new TankDrive(hardwareMap, telemetry, TankDrive.DriveMode.ROBOT_CENTRIC);
    }

    @Override
    public void loop() {

        tankDrive.driveToWildfire(wildfireCamera.getLumaValues());

        telemetry.addData("Left Lower Luma", wildfireCamera.getLumaValues()[0]);
        telemetry.addData("Left Upper Luma", wildfireCamera.getLumaValues()[2]);
        telemetry.addData("Right Lower Luma", wildfireCamera.getLumaValues()[1]);
        telemetry.addData("Right Upper Luma", wildfireCamera.getLumaValues()[3]);
    }
}
