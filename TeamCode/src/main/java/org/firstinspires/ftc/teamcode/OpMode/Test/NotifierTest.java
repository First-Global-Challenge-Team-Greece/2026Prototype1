package org.firstinspires.ftc.teamcode.OpMode.Test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Util.DriverNotifier;
@Disabled

@TeleOp()
public class NotifierTest extends OpMode {

    private DriverNotifier driverNotifier;

    @Override
    public void init() {
        driverNotifier = new DriverNotifier(gamepad1);
    }

    @Override
    public void loop() {
        telemetry.addLine();
        if (gamepad1.x) {
            driverNotifier.sendMessage(DriverNotifier.MessageLevel.ERROR);
            telemetry.addLine("Vibrating");
            telemetry.speak("Doorknob");
            gamepad1.setLedColor(0, 255, 255, 2000);
        }

        telemetry.addData("Controller Type", gamepad1.type());
    }
}
