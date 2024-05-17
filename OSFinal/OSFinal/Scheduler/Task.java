package Scheduler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class represents a Task with a name, type, start time, and end time.
 * It provides methods for serialization and deserialization to and from JSON.
 *
 */
public class Task {
    private String name;
    private String type;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    /**
     * Default constructor for JSON deserialization.
     */
    public Task() {}

    /**
     * Constructs a new Task with the specified name, start time, and end time.
     *
     * @param name      The name of the task.
     * @param startTime The start time of the task.
     * @param endTime   The end time of the task.
     */
    public Task(String name, LocalDateTime startTime, LocalDateTime endTime) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Gets the name of the task.
     *
     * @return The name of the task.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the task.
     *
     * @param name The name of the task.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the type of the task.
     *
     * @return The type of the task.
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of the task.
     *
     * @param type The type of the task.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the start time of the task.
     *
     * @return The start time of the task.
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Sets the start time of the task.
     *
     * @param startTime The start time of the task.
     */
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets the end time of the task.
     *
     * @return The end time of the task.
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * Sets the end time of the task.
     *
     * @param endTime The end time of the task.
     */
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    /**
     * Converts the Task object to a JSON string representation.
     *
     * @return The JSON string representation of the task.
     */
    public String toJson() {
        return String.format("{\"Name\":\"%s\",\"Type\":\"%s\",\"StartTime\":\"%s\",\"EndTime\":\"%s\"}",
                name, type,
                startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                endTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
    }

    /**
     * Creates a Task object from a JSON string representation.
     *
     * @param json The JSON string representation of the task.
     * @return The Task object created from the JSON string.
     */
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

    /**
     * Returns a string representation of the Task object.
     *
     * @return The string representation of the Task object.
     */
    @Override
    public String toString() {
        return String.format("Task name: %s\nType: %s\nStart Time: %s\nEnd Time: %s\n",
                name, type,
                startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                endTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
    }
}
