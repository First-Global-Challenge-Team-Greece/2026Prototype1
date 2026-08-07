package org.firstinspires.ftc.teamcode.Util;
// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).

import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion;
import org.firstinspires.ftc.robotcore.external.ExportClassToBlocks;
import org.firstinspires.ftc.robotcore.external.ExportToBlocks;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.vision.apriltag.AprilTagLibrary;

@ExportClassToBlocks
public class IgnitingInnovationGameDatabase extends BlocksOpModeCompanion {
    public IgnitingInnovationGameDatabase() {
    }

    @ExportToBlocks(
            comment = "Get the Igniting Innovation AprilTag library",
            heading = "getIgnitingInnovationTagLibrary"
    )
    public static AprilTagLibrary getIgnitingInnovationTagLibrary() {
        return (new AprilTagLibrary.Builder()).addTag(100, "Red Side - Facing Drivers", (double)160.0F, DistanceUnit.MM).addTag(101, "Red Side - Facing Audience", (double)160.0F, DistanceUnit.MM).addTag(102, "Center - Facing Audience", (double)160.0F, DistanceUnit.MM).addTag(103, "Blue Side - Facing Audience", (double)160.0F, DistanceUnit.MM).addTag(104, "Blue Side - Facing Drivers", (double)160.0F, DistanceUnit.MM).build();
    }
}
