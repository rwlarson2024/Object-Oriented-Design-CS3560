package Scheduler;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a RecurringTask, which is a type of Task that repeats
 * at a specified frequency and interval. It extends the Task class and includes
 * methods to manage recurrences and exclusions.
 */
public class RecurringTask extends Task {
    private String frequency; // "Daily", "Weekly", "Monthly"
    private int interval; // Number of intervals between each recurrence
    private List<LocalDateTime> exclusions; // List of dates to exclude

    /**
     * Constructs a new RecurringTask with the specified name, start time, end time,
     * frequency, and interval.
     *
     * @param name      The name of the recurring task.
     * @param startTime The start time of the recurring task.
     * @param endTime   The end time of the recurring task.
     * @param frequency The frequency of recurrence ("Daily", "Weekly", "Monthly").
     * @param interval  The number of intervals between each recurrence.
     */
    public RecurringTask(String name, LocalDateTime startTime, LocalDateTime endTime, String frequency, int interval) {
        super(name, startTime, endTime);
        this.frequency = frequency;
        this.interval = interval;
        this.exclusions = new ArrayList<>();
    }

    /**
     * Gets the frequency of the recurring task.
     *
     * @return The frequency of the recurring task.
     */
    public String getFrequency() {
        return frequency;
    }

    /**
     * Gets the interval between recurrences of the recurring task.
     *
     * @return The interval between recurrences.
     */
    public int getInterval() {
        return interval;
    }

    /**
     * Adds an exclusion date to the recurring task.
     *
     * @param exclusion The date to exclude.
     */
    public void addExclusion(LocalDateTime exclusion) {
        this.exclusions.add(exclusion);
    }

    /**
     * Gets a list of Task occurrences for the recurring task up to the specified end date.
     *
     * @param endDate The end date for generating occurrences.
     * @return A list of Task occurrences.
     */
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
