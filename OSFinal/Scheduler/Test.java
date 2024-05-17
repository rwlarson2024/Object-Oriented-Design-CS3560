package Scheduler;
import java.time.LocalDateTime;

public class Test {
    public static void main(String[] args) {
        Schedule schedule = new Schedule();

        LocalDateTime start = LocalDateTime.of(2024, 5, 16, 12, 30);
        LocalDateTime end = LocalDateTime.of(2024, 5, 16, 14, 30);
        RecurringTask weeklyMeeting = new RecurringTask("Weekly Meeting", start, end, "Weekly", 1);

        schedule.addRecurringTask(weeklyMeeting, LocalDateTime.of(2024, 6, 30, 0, 0));

        // Adding an anti-task to remove the occurrence on 2024-05-23
        LocalDateTime antiTaskStart = LocalDateTime.of(2024, 5, 23, 12, 30);
        LocalDateTime antiTaskEnd = LocalDateTime.of(2024, 5, 23, 14, 30);
        AntiTask removeWeeklyMeeting = new AntiTask("Weekly Meeting", antiTaskStart, antiTaskEnd);

        schedule.addAntiTask(removeWeeklyMeeting);

        schedule.displaySchedule();
    }
}
