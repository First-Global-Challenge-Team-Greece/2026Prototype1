package org.firstinspires.ftc.teamcode.Subsystems.coralPrototype;

import java.util.function.BooleanSupplier;

public class ContinuousTriggerTask implements Task {

    private final Runnable trigger;
    private final Runnable cleanUp;
    private final BooleanSupplier finishDetector;

    public ContinuousTriggerTask(Runnable trigger, BooleanSupplier finishDetector) {
        this(trigger, finishDetector, null);
    }

    public ContinuousTriggerTask(Runnable trigger, BooleanSupplier finishDetector, Runnable cleanUp) {
        this.trigger = trigger;
        this.finishDetector = finishDetector;
        this.cleanUp = cleanUp;
    }

    @Override
    public void execute() {
        trigger.run();
    }

    @Override
    public boolean isFinished() {
        return finishDetector.getAsBoolean();
    }

    @Override
    public void cleanUp() {
        if (cleanUp == null) return;

        cleanUp.run();
    }
}
