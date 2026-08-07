package org.firstinspires.ftc.teamcode.OpMode.Test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Util.DriverNotifier;

@TeleOp()
public class NotifierTest extends OpMode {

    private DriverNotifier driverNotifier;

    @Override
    public void init() {
        driverNotifier = new DriverNotifier(telemetry, gamepad1);
    }

    @Override
    public void loop() {
        gamepad1.type = Gamepad.Type.XBOX_360;
        if (gamepad1.x) {
            driverNotifier.sendMessage(DriverNotifier.MessageLevel.CRITICAL);
        }

        telemetry.addData("Controller Type", gamepad1.type());
    }
}
