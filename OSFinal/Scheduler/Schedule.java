package Scheduler;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Schedule {
    private List<Task> tasks;
    private List<AntiTask> antiTasks;

    public Schedule() {
        this.tasks = new ArrayList<>();
        this.antiTasks = new ArrayList<>();
    }

    public void addTask(Task task) {
        if (isOverlapping(task)) {
            System.out.println("Error: Task overlaps with an existing task.");
            return;
        }
        this.tasks.add(task);
        applyAntiTasks();
    }

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

    public void addAntiTask(AntiTask antiTask) {
        this.antiTasks.add(antiTask);
        applyAntiTasks();
    }

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

    public boolean isOverlapping(Task newTask) {
        for (Task existingTask : tasks) {
            if (newTask.getStartTime().isBefore(existingTask.getEndTime()) &&
                newTask.getEndTime().isAfter(existingTask.getStartTime())) {
                return true;
            }
        }
        return false;
    }

    public List<Task> getTasks() {
        applyAntiTasks(); // Ensure anti-tasks are applied before returning tasks
        return tasks;
    }

    public void displaySchedule() {
        for (Task task : getTasks()) {
            System.out.println(task);
        }
    }

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
