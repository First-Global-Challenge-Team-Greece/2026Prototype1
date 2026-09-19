package org.firstinspires.ftc.teamcode.Subsystems;

public class ShooterBallCounter {
    private final double VELOCITY_DROP_THRESHOLD;
    private double previousVelocity = 0;

    private int ballCount = 0;

    public ShooterBallCounter(double velocityDropThreshold) {
        VELOCITY_DROP_THRESHOLD = velocityDropThreshold;
    }

    public void updateCount(double currentVelocity) {
        if (previousVelocity - currentVelocity > VELOCITY_DROP_THRESHOLD) {
            ballCount++;
        }

        previousVelocity = currentVelocity;
    }

    public int getBallCount() {
        return ballCount;
    }

    public void resetBallCount() {
        ballCount = 0;
    }
}
