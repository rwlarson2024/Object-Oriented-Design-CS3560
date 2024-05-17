package Scheduler;

import java.time.LocalDateTime;

/**
 * This class represents an AntiTask, which is a type of Task that cancels out
 * or nullifies another Task with the same name, start time, and end time.
 * It extends the Task class.
 */
public class AntiTask extends Task {

    /**
     * Constructs a new AntiTask with the specified name, start time, and end time.
     *
     * @param name      The name of the anti-task.
     * @param startTime The start time of the anti-task.
     * @param endTime   The end time of the anti-task.
     */
    public AntiTask(String name, LocalDateTime startTime, LocalDateTime endTime) {
        super(name, startTime, endTime);
    }
}
