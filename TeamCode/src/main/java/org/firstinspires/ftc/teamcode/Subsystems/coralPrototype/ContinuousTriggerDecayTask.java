package org.firstinspires.ftc.teamcode.Subsystems.coralPrototype;


public class ContinuousTriggerDecayTask implements Task {

    private boolean hasBeenTriggered = false;
    private final Runnable trigger;
    private final Runnable cleanUp;

    private final int decay;

    private long startingTimeMS;

    public ContinuousTriggerDecayTask(Runnable trigger, int decayMS) {
        this(trigger, null, decayMS);
    }
    public ContinuousTriggerDecayTask(Runnable trigger, Runnable cleanUp, int decayMS) {
        this.trigger = trigger;
        this.cleanUp = cleanUp;
        decay = decayMS;
    }

    @Override
    public void execute() {
        if (!hasBeenTriggered) {
            hasBeenTriggered = true;
            startingTimeMS = System.currentTimeMillis();
        }
        trigger.run();
    }

    @Override
    public boolean isFinished() {
        return (System.currentTimeMillis() - startingTimeMS) >= decay;
    }

    public void reset() {
        hasBeenTriggered = false;
    }

    @Override
    public void cleanUp() {
        if (cleanUp != null) {
            cleanUp.run();
        }
    }
}
