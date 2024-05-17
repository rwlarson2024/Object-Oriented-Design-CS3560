package Scheduler;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The Schedule class manages a list of tasks and anti-tasks.
 * It provides methods to add tasks, recurring tasks, and anti-tasks,
 * check for overlapping tasks, and apply anti-tasks.
 * It also allows writing tasks to a file and reading tasks from a file.
 *
 */
public class Schedule {
    private List<Task> tasks;
    private List<AntiTask> antiTasks;

    /**
     * Constructs a new Schedule with empty task and anti-task lists.
     */
    public Schedule() {
        this.tasks = new ArrayList<>();
        this.antiTasks = new ArrayList<>();
    }

    /**
     * Adds a task to the schedule if it does not overlap with existing tasks.
     *
     * @param task The task to be added.
     */
    public void addTask(Task task) {
        if (isOverlapping(task)) {
            System.out.println("Error: Task overlaps with an existing task.");
            return;
        }
        this.tasks.add(task);
        applyAntiTasks();
    }

    /**
     * Adds a recurring task to the schedule if it does not overlap with existing tasks.
     *
     * @param recurringTask The recurring task to be added.
     * @param endDate       The end date for the recurrence.
     */
    public void addRecurringTask(RecurringTask recurringTask, LocalDateTime endDate) {
        List<Task> occurrences = recurringTask.getOccurrences(endDate);
        for (Task occurrence : occurrences) {
            if (isOverlapping(occurrence)) {
                System.out.println("Error: Recurring task overlaps with an existing task.");
                return;
            }
        }
        this.tasks.addAll(occurrences);
        applyAntiTasks();
    }

    /**
     * Adds an anti-task to the schedule and applies it to existing tasks.
     *
     * @param antiTask The anti-task to be added.
     */
    public void addAntiTask(AntiTask antiTask) {
        this.antiTasks.add(antiTask);
        applyAntiTasks();
    }

    /**
     * Applies anti-tasks to the current list of tasks, removing tasks that match the anti-tasks.
     */
    private void applyAntiTasks() {
        for (AntiTask antiTask : antiTasks) {
            Iterator<Task> iterator = tasks.iterator();
            while (iterator.hasNext()) {
                Task task = iterator.next();
                if (task.getName().equals(antiTask.getName()) &&
                    task.getStartTime().equals(antiTask.getStartTime()) &&
                    task.getEndTime().equals(antiTask.getEndTime())) {
                    iterator.remove();
                }
            }
        }
    }

    /**
     * Checks if a new task overlaps with any existing tasks.
     *
     * @param newTask The new task to check.
     * @return true if the new task overlaps with an existing task, false otherwise.
     */
    public boolean isOverlapping(Task newTask) {
        for (Task existingTask : tasks) {
            if (newTask.getStartTime().isBefore(existingTask.getEndTime()) &&
                newTask.getEndTime().isAfter(existingTask.getStartTime())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the current list of tasks, applying anti-tasks before returning.
     *
     * @return The current list of tasks.
     */
    public List<Task> getTasks() {
        applyAntiTasks(); // Ensure anti-tasks are applied before returning tasks
        return tasks;
    }

    /**
     * Displays the current schedule by printing the tasks.
     */
    public void displaySchedule() {
        for (Task task : getTasks()) {
            System.out.println(task);
        }
    }

    /**
     * Writes the specified list of tasks to a file in JSON format.
     *
     * @param tasks    The list of tasks to write to the file.
     * @param fileName The name of the file to write the tasks to.
     * @throws IOException If an I/O error occurs.
     */
    public void writeTasksToFile(List<Task> tasks, String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("[");
            for (int i = 0; i < tasks.size(); i++) {
                writer.write(tasks.get(i).toJson());
                if (i < tasks.size() - 1) {
                    writer.write(",");
                }
            }
            writer.write("]");
        }
    }

    /**
     * Reads the schedule from a file in JSON format.
     *
     * @param fileName The name of the file to read the schedule from.
     * @throws IOException If an I/O error occurs.
     */
    public void readScheduleFromFile(String fileName) throws IOException {
        StringBuilder jsonContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }
        }
        String content = jsonContent.toString().replace("[", "").replace("]", "");
        String[] taskJsonArray = content.split("},\\{");
        tasks = new ArrayList<>();
        for (String taskJson : taskJsonArray) {
            if (!taskJson.startsWith("{")) {
                taskJson = "{" + taskJson;
            }
            if (!taskJson.endsWith("}")) {
                taskJson = taskJson + "}";
            }
            Task task = Task.fromJson(taskJson);
            task.setStartTime(TimeUtils.roundToNearest15Minutes(task.getStartTime()));
            task.setEndTime(TimeUtils.roundToNearest15Minutes(task.getEndTime()));
            tasks.add(task);
        }
        applyAntiTasks();
    }
}
