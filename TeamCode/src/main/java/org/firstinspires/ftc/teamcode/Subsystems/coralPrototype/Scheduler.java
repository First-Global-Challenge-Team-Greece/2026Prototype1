package org.firstinspires.ftc.teamcode.Subsystems.coralPrototype;


import java.util.LinkedList;
import java.util.List;

public class Scheduler {
    private List<Task> taskQueue = new LinkedList<>();

    private int index = 0;

    public void addTask(Task t) {
        taskQueue.add(t);
    }

    public void next() {
        // Legend xwris to 'd'
        if (index >= taskQueue.size())
            return;

        if (taskQueue.get(index) == null)
            return;

        taskQueue.get(index).execute();

        if (taskQueue.get(index).isFinished()) {
            taskQueue.get(index).cleanUp();
            index++;
        }
    }

    public void reset() {
        index = 0;
    }

    public void clear() {
        index = 0;
        taskQueue = new LinkedList<>();
    }

    public void addAll(List<Task> allTasks) {
        taskQueue = allTasks;
    }

    public int getIndex() {
        return index;
    }
}
