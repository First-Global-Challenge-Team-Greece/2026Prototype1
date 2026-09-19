package org.firstinspires.ftc.teamcode.OpMode.Samples;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.OmniDrive;

/**
 * <p>
 *     Ένα παράδειχμα χρήσης του κώδικα για omni βάσεις (π.χ. xDrive, mecanum).
 * </p>
 * */
@Disabled
@TeleOp()
public class Sample_OmniDrive extends OpMode {

    private OmniDrive omniDrive;

    @Override
    public void init() {
        // Εδώ γίνεται η επιλογή της λειτουργίας οδήγησης της βάσης,
        // δηλαδή, εάν θα μεταφράζει την κατεύθυνση του αναλογικού stick του χειρηστιρίου
        // σε σχέση με το ρομπότ ή σε σχέση με την πίστα.
        omniDrive = new OmniDrive(hardwareMap, telemetry, OmniDrive.DriveMode.FIELD_CENTRIC);
    }

    @Override
    public void loop() {

        if (omniDrive.getDriveMode() == OmniDrive.DriveMode.ROBOT_CENTRIC)
            omniDrive.driveRobotCentric(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

        else if (omniDrive.getDriveMode() == OmniDrive.DriveMode.FIELD_CENTRIC)
            omniDrive.driveFieldCentric(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
    }
}
