package org.firstinspires.ftc.teamcode.Util;

public class DebouncedButton {

    private final long DEBOUNCE_TIME_MS;

    private long lastUpdateMS = 0;

    public DebouncedButton(long debounceMS) {
        DEBOUNCE_TIME_MS = debounceMS;
    }

    public boolean update(boolean buttonInput) {

        if (((System.currentTimeMillis() - lastUpdateMS) > DEBOUNCE_TIME_MS) && buttonInput) {
            lastUpdateMS = System.currentTimeMillis();
            return true;
        }

        return false;
    }
}
