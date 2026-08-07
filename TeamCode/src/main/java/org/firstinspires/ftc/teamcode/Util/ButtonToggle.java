package org.firstinspires.ftc.teamcode.Util;

public class ButtonToggle {

    private final long DEBOUNCE_TIME_MS;

    private long lastUpdateMS = 0;
    private boolean state = false;

    public ButtonToggle(long debounceMS) {
        DEBOUNCE_TIME_MS = debounceMS;
    }

    public void forceState(boolean state) {
        this.state = state;
    }

    public boolean update(boolean buttonInput) {

        if ((System.currentTimeMillis() - lastUpdateMS > DEBOUNCE_TIME_MS) && buttonInput) {
            lastUpdateMS = System.currentTimeMillis();
            state = !state;
        }

        return state;
    }
}
