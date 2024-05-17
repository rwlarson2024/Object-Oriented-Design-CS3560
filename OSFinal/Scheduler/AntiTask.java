package Scheduler;

import java.time.LocalDateTime;

public class AntiTask extends Task {

    public AntiTask(String name, LocalDateTime startTime, LocalDateTime endTime) {
        super(name, startTime, endTime);
    }
}
