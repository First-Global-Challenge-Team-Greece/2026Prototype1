package org.firstinspires.ftc.teamcode.Util;

public class GlobalDebugVariables {
    public static double currentSum = 0;
    public static double maxCurrentSum = 0;

    public static void update() {
        if (currentSum > maxCurrentSum) maxCurrentSum = currentSum;
    }

    public static void dumpSystemCurrents(double[][] systemCurrents) {
        currentSum = 0;

        for (double[] system : systemCurrents) {
            for (double current : system) {
                currentSum += current;
            }
        }
    }
}
