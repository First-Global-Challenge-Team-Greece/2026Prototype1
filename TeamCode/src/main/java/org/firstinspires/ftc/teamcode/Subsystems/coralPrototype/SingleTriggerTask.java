package org.firstinspires.ftc.teamcode.Subsystems.coralPrototype;


import java.util.function.BooleanSupplier;

public class SingleTriggerTask implements Task {

    private boolean hasBeenTriggered = false;
    private final Runnable trigger;
    private final BooleanSupplier finishDetector;

    public SingleTriggerTask(Runnable trigger, BooleanSupplier finishDetector) {
        this.trigger = trigger;
        this.finishDetector = finishDetector;
    }

    @Override
    public void execute() {
        if (!hasBeenTriggered) {
            trigger.run();
            hasBeenTriggered = true;
        }
    }

    @Override
    public boolean isFinished() {
        return finishDetector.getAsBoolean();
    }

    @Override
    public void cleanUp() {

    }

    public void reset() {
        hasBeenTriggered = false;
    }
}
