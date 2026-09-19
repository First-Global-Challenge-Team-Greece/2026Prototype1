package org.firstinspires.ftc.teamcode.Util;

import androidx.annotation.NonNull;

/**
 * Utility class for tracking elapsed match time.
 *
 * <p>
 * The timer can be started explicitly or automatically on the first
 * detected input from one or two gamepads.
 * <p>
 *
 * Elapsed time can be retrieved in nanoseconds, milliseconds, or seconds.
 */
public class MatchTimer {
    private long startTime;
    private boolean started;

    private static final long MILLIS_IN_NANOS = 1_000_000;
    private static final long SECONDS_IN_NANOS = 1_000_000_000;

    public MatchTimer() {
        this.started = false;
        this.startTime = 0;
    }

    /**
     * Starts the timer if it hasn't been started yet.
     */
    public void start() {
        if (!started) {
            startTime = System.nanoTime();
            started = true;
        }
    }

    /**
     * Resets the timer
     */
    public void reset() {
        started = false;
        startTime = 0;
    }

    /**
     * Resets and starts the timer.
     */
    public void resetAndStart() {
        startTime = System.nanoTime();
        started = true;
    }

    /**
     * Returns the elapsed time in nanoseconds
     *
     * @return elapsed time in nanoseconds, or 0 if the timer has not started.
     */
    public long getElapsedTimeNanos() {
        if (!started) {
            return 0;
        }
        return (System.nanoTime() - startTime);
    }

    /**
     * Returns the elapsed time in milliseconds.
     *
     * @return elapsed time in milliseconds, or 0 if the timer has not started.
     */
    public long getElapsedTimeMillis() {
        return getElapsedTimeNanos() / MILLIS_IN_NANOS;
    }

    /**
     * Returns the elapsed time in seconds.
     *
     * @return elapsed time in seconds, or 0 if the timer has not started.
     */
    public double getElapsedTimeSeconds() {
        return (double) getElapsedTimeNanos() / SECONDS_IN_NANOS;
    }

    /**
     * Checks if the timer has been started.
     */
    public boolean hasStarted() {
        return started;
    }

    /**
     * Starts the timer if there is any input on the gamepad.
     *
     * <p>
     * {@link GamepadEx#update()} must be called before this method so that
     * {@link GamepadEx#hasInput()} reflects the current controller state.
     * </p>
     *
     * @param controller the gamepad to check for input.
     */
    public void startOnFirstInput(@NonNull GamepadEx controller) {
        if (controller.hasInput()) {
            start();
        }
    }

    /**
     * Starts the timer if there is any input on either gamepad
     *
     * <p>
     * {@link GamepadEx#update()} must be called on both controllers before this method so that
     * {@link GamepadEx#hasInput()} reflects the current controller state.
     * </p>
     *
     * @param controller1 the first gamepad to check for input.
     * @param controller2 the second gamepad to check for input.
     */
    public void startOnFirstInput(@NonNull GamepadEx controller1, @NonNull GamepadEx controller2) {
        if (controller1.hasInput() || controller2.hasInput()) {
            start();
        }
    }
}
