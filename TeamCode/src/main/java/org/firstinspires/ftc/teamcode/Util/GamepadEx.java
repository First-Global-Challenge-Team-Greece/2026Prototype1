package org.firstinspires.ftc.teamcode.Util;

import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.BooleanSupplier;

public class GamepadEx {
    private Gamepad controller;

    public final DriverNotifier notifier;

    public enum ButtonName { // Enum for button mapping
        A, B, X, Y, DPAD_UP, DPAD_DOWN, DPAD_LEFT, DPAD_RIGHT,
        LEFT_BUMPER, RIGHT_BUMPER, LEFT_STICK_BUTTON, RIGHT_STICK_BUTTON
    }

    private class Button {
        private final BooleanSupplier rawValue;
        private final ButtonToggle toggleStateManager;
        private final DebouncedButton debounceStateManger;

        public Button(BooleanSupplier inputSupplier, long debounceTimeMS) {
            rawValue = inputSupplier;
            toggleStateManager = new ButtonToggle(debounceTimeMS);
            debounceStateManger = new DebouncedButton(debounceTimeMS);
        }
    }

    private final Map<ButtonName, Button> buttonMap = new EnumMap<>(ButtonName.class);

    private boolean cur_states[] = new boolean[ButtonName.values().length];
    private boolean prev_states[] = new boolean[ButtonName.values().length];

    private static final double GAMEPAD_DEADZONE = 0.05;
    private static final long DEBOUNCE_TIME_MS = 300;

    public GamepadEx(Gamepad gamepad) {
        this.controller = gamepad;
        notifier = new DriverNotifier(gamepad);

        buttonMap.put(ButtonName.A, new Button(() -> controller.a, DEBOUNCE_TIME_MS));
        buttonMap.put(ButtonName.B, new Button(() -> controller.b, DEBOUNCE_TIME_MS));
        buttonMap.put(ButtonName.X, new Button(() -> controller.x, DEBOUNCE_TIME_MS));
        buttonMap.put(ButtonName.Y, new Button(() -> controller.y, DEBOUNCE_TIME_MS));
        buttonMap.put(ButtonName.DPAD_UP, new Button(() -> controller.dpad_up, DEBOUNCE_TIME_MS));
        buttonMap.put(ButtonName.DPAD_DOWN, new Button(() -> controller.dpad_down, DEBOUNCE_TIME_MS));
        buttonMap.put(ButtonName.DPAD_LEFT, new Button(() -> controller.dpad_left, DEBOUNCE_TIME_MS));
        buttonMap.put(ButtonName.DPAD_RIGHT, new Button(() -> controller.dpad_right, DEBOUNCE_TIME_MS));
        buttonMap.put(ButtonName.LEFT_BUMPER, new Button(() -> controller.left_bumper, DEBOUNCE_TIME_MS));
        buttonMap.put(ButtonName.RIGHT_BUMPER, new Button(() -> controller.right_bumper, DEBOUNCE_TIME_MS));
        buttonMap.put(ButtonName.LEFT_STICK_BUTTON, new Button(() -> controller.left_stick_button, DEBOUNCE_TIME_MS));
        buttonMap.put(ButtonName.RIGHT_STICK_BUTTON, new Button(() -> controller.right_stick_button, DEBOUNCE_TIME_MS));

    }

    public void update() {
        prev_states = cur_states.clone();
        for (ButtonName buttonName : ButtonName.values()) {
            cur_states[buttonName.ordinal()] = buttonMap.get(buttonName).rawValue.getAsBoolean();
        }
    }

    // ---------------------------------- Rising Edge Detection --------------------------------- //
    public boolean justPressed(ButtonName buttonName) {
        return cur_states[buttonName.ordinal()] && !prev_states[buttonName.ordinal()];
    }

    // --------------------------------- Falling Edge Detection --------------------------------- //
    public boolean justReleased(ButtonName buttonName) {
        return !cur_states[buttonName.ordinal()] && prev_states[buttonName.ordinal()];
    }

    // ------------------------------------- Button States -------------------------------------- //
    public boolean isDown(ButtonName buttonName) {
        return cur_states[buttonName.ordinal()];
    }

    public boolean isUp(ButtonName buttonName) {
        return !cur_states[buttonName.ordinal()];
    }
    // ------------------------------------- Special States -------------------------------------- //
    public boolean toggle(ButtonName buttonName) {
        return buttonMap.get(buttonName).toggleStateManager.update(cur_states[buttonName.ordinal()]);
    }

    public void forceToggleState(ButtonName buttonName, boolean state) {
        buttonMap.get(buttonName).toggleStateManager.forceState(state);
    }

    public boolean debouncedInput(ButtonName buttonName) {
        return buttonMap.get(buttonName).debounceStateManger.update(cur_states[buttonName.ordinal()]);
    }

    // ----------------------------------------- Linear ----------------------------------------- //
    public double getLeftStickX() {
        return controller.left_stick_x;
    }

    public double getLeftStickY() {
        return -controller.left_stick_y; // Minus to fix the incorrect joystick sign (+ -> up)
    }

    public double getRightStickX() {
        return controller.right_stick_x;
    }

    public double getRightStickY() {
        return -controller.right_stick_y; // Minus to fix the incorrect joystick sign (+ -> up)
    }

    public double getLeftTrigger() {
        return controller.left_trigger;
    }

    public double getRightTrigger() {
        return controller.right_trigger;
    }

    /**
     * Checks if there is any active input on the gamepad.
     * Sticks and triggers are checked against a small deadzone.
     * All buttons in the map are checked for their current state.
     * @return true if any input is detected, false otherwise.
     */
    public boolean hasInput() {
        // Check sticks
        if (Math.abs(controller.left_stick_x) > GAMEPAD_DEADZONE ||
                Math.abs(controller.left_stick_y) > GAMEPAD_DEADZONE ||
                Math.abs(controller.right_stick_x) > GAMEPAD_DEADZONE ||
                Math.abs(controller.right_stick_y) > GAMEPAD_DEADZONE) {
            return true;
        }

        // Check triggers
        if (controller.left_trigger > GAMEPAD_DEADZONE || controller.right_trigger > GAMEPAD_DEADZONE) {
            return true;
        }

        // Check all buttons
        for (boolean curState : cur_states) {
            if (curState) {
                return true;
            }
        }

        return false;
    }
}
