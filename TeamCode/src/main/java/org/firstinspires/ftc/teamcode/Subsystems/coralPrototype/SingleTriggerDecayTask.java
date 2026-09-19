package org.firstinspires.ftc.teamcode.Subsystems.coralPrototype;


public class SingleTriggerDecayTask implements Task {

    private boolean hasBeenTriggered = false;
    private final Runnable trigger;
    private final Runnable cleanUp;

    private final int decay;

    private long startingTimeMS;

    public SingleTriggerDecayTask(Runnable trigger, int decayMS) {
        this(trigger, null, decayMS);
    }
    public SingleTriggerDecayTask(Runnable trigger, Runnable cleanUp, int decayMS) {
        this.trigger = trigger;
        this.cleanUp = cleanUp;
        decay = decayMS;
    }

    @Override
    public void execute() {
        if (!hasBeenTriggered) {
            trigger.run();
            hasBeenTriggered = true;
            startingTimeMS = System.currentTimeMillis();
        }
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
