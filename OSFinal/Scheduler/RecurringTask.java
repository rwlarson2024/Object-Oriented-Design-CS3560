package Scheduler;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class RecurringTask extends Task {
    private String frequency; // "Daily", "Weekly", "Monthly"
    private int interval; // Number of intervals between each recurrence
    private List<LocalDateTime> exclusions; // List of dates to exclude

    public RecurringTask(String name, LocalDateTime startTime, LocalDateTime endTime, String frequency, int interval) {
        super(name, startTime, endTime);
        this.frequency = frequency;
        this.interval = interval;
        this.exclusions = new ArrayList<>();
    }

    public String getFrequency() {
        return frequency;
    }

    public int getInterval() {
        return interval;
    }

    public void addExclusion(LocalDateTime exclusion) {
        this.exclusions.add(exclusion);
    }

    public List<Task> getOccurrences(LocalDateTime endDate) {
        List<Task> occurrences = new ArrayList<>();
        LocalDateTime nextStart = this.getStartTime();
        LocalDateTime nextEnd = this.getEndTime();

        while (nextStart.isBefore(endDate)) {
            if (!exclusions.contains(nextStart)) {
                occurrences.add(new Task(this.getName(), nextStart, nextEnd));
            }
            switch (frequency) {
                case "Daily":
                    nextStart = nextStart.plus(interval, ChronoUnit.DAYS);
                    nextEnd = nextEnd.plus(interval, ChronoUnit.DAYS);
                    break;
                case "Weekly":
                    nextStart = nextStart.plus(interval, ChronoUnit.WEEKS);
                    nextEnd = nextEnd.plus(interval, ChronoUnit.WEEKS);
                    break;
                case "Monthly":
                    nextStart = nextStart.plus(interval, ChronoUnit.MONTHS);
                    nextEnd = nextEnd.plus(interval, ChronoUnit.MONTHS);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid frequency type: " + frequency);
            }
        }
        return occurrences;
    }
}
