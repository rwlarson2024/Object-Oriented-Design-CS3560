package Scheduler;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    private String name;
    private String type;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    // Default constructor for JSON deserialization
    public Task() {}

    public Task(String name, LocalDateTime startTime, LocalDateTime endTime) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String toJson() {
        return String.format("{\"Name\":\"%s\",\"Type\":\"%s\",\"StartTime\":\"%s\",\"EndTime\":\"%s\"}",
                name, type,
                startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                endTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
    }

    public static Task fromJson(String json) {
        String[] parts = json.replace("{", "").replace("}", "").replace("\"", "").split(",");
        String name = parts[0].split(":")[1];
        String type = parts[1].split(":")[1];
        String startTimeString = parts[2].split(":")[1];
        String endTimeString = parts[3].split(":")[1];

        // Ensure time includes both hours and minutes
        if (startTimeString.length() == 13) {
            startTimeString += ":00";
        }
        if (endTimeString.length() == 13) {
            endTimeString += ":00";
        }

        LocalDateTime startTime = LocalDateTime.parse(startTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        LocalDateTime endTime = LocalDateTime.parse(endTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        Task task = new Task(name, startTime, endTime);
        task.setType(type);
        return task;
    }

    @Override
    public String toString() {
        return String.format("Task name: %s\nType: %s\nStart Time: %s\nEnd Time: %s\n",
                name, type,
                startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                endTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
    }
}
