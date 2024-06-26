package Scheduler;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.YearMonth;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * This is the main class of the PSS. The User Class runs a UI that hosts the information that
 * is passed by the user and calls upon the Schedule class to do the creation of all types of Tasks.
 *
 * @author  Ryan Larson, Russel, Quaid Everett, Shannon
 */
public class User {
    private Schedule schedule;
    private Scanner scanner;

    /**
     * Constructs a new User with a schedule and scanner.
     */
    public User() {
        this.schedule = new Schedule();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Starts the User interface, allowing the user to interact with the scheduling system.
     */
    public void start() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n1. Create Task\n2. Create Recurring Task\n3. Create Anti-Task\n4. Edit Task\n5. Print Daily Schedule\n6. Print Weekly Schedule\n7. Print Monthly Schedule\n8. Write Daily Schedule to File\n9. Write Weekly Schedule to File\n10. Write Monthly Schedule to File\n11. Read Schedule from File\n12. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    createTask();
                    break;
                case 2:
                    createRecurringTask();
                    break;
                case 3:
                    createAntiTask();
                    break;
                case 4:
                    editTask();
                    break;
                case 5:
                    printDailySchedule();
                    break;
                case 6:
                    printWeeklySchedule();
                    break;
                case 7:
                    printMonthlySchedule();
                    break;
                case 8:
                    writeDailyScheduleToFile();
                    break;
                case 9:
                    writeWeeklyScheduleToFile();
                    break;
                case 10:
                    writeMonthlyScheduleToFile();
                    break;
                case 11:
                    readScheduleFromFile();
                    break;
                case 12:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    /**
     * Prompts the user for task details and creates a new task.
     */
    private void createTask() {
        System.out.print("Enter task name: ");
        String name = scanner.nextLine();
        System.out.print("Enter task type: ");
        String type = scanner.nextLine();
        LocalDateTime startTime = roundToNearest15Minutes(getValidDateTime("Enter start time (yyyy-MM-dd HH:mm): "));
        LocalDateTime endTime = roundToNearest15Minutes(getValidDateTime("Enter end time (yyyy-MM-dd HH:mm): "));

        Task task = new Task(name, startTime, endTime);
        task.setType(type);
        schedule.addTask(task);
        System.out.println("Task added successfully.");
    }

    /**
     * Prompts the user for recurring task details and creates a new recurring task.
     */
    private void createRecurringTask() {
        System.out.print("Enter task name: ");
        String name = scanner.nextLine();
        LocalDateTime startTime = roundToNearest15Minutes(getValidDateTime("Enter start time (yyyy-MM-dd HH:mm): "));
        LocalDateTime endTime = roundToNearest15Minutes(getValidDateTime("Enter end time (yyyy-MM-dd HH:mm): "));
        System.out.print("Enter frequency (Daily/Weekly/Monthly): ");
        String frequency = scanner.nextLine();
        System.out.print("Enter interval: ");
        int interval = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        RecurringTask recurringTask = new RecurringTask(name, startTime, endTime, frequency, interval);
        LocalDate endDate = getValidDate("Enter end date for recurrence (yyyy-MM-dd): ");

        schedule.addRecurringTask(recurringTask, endDate.atStartOfDay());
        System.out.println("Recurring task added successfully.");
    }

    /**
     * Prompts the user for anti-task details and creates a new anti-task.
     */
    private void createAntiTask() {
        System.out.print("Enter task name: ");
        String name = scanner.nextLine();
        LocalDateTime startTime = roundToNearest15Minutes(getValidDateTime("Enter start time (yyyy-MM-dd HH:mm): "));
        LocalDateTime endTime = roundToNearest15Minutes(getValidDateTime("Enter end time (yyyy-MM-dd HH:mm): "));

        AntiTask antiTask = new AntiTask(name, startTime, endTime);
        schedule.addAntiTask(antiTask);
        System.out.println("Anti-task added successfully.");
    }

    /**
     * Prompts the user for task details and allows editing of an existing task.
     */
    private void editTask() {
        System.out.print("Enter the name of the task to edit: ");
        String name = scanner.nextLine();
        LocalDateTime startTime = getValidDateTime("Enter the start time of the task to edit (yyyy-MM-dd HH:mm): ");
        Task taskToEdit = null;
        for (Task task : schedule.getTasks()) {
            if (task.getName().equals(name) && task.getStartTime().equals(startTime)) {
                taskToEdit = task;
                break;
            }
        }

        if (taskToEdit == null) {
            System.out.println("Task not found.");
            return;
        }

        System.out.println("Editing task: " + taskToEdit);
        System.out.print("Enter new name (leave blank to keep current name): ");
        String newName = scanner.nextLine();
        if (!newName.isEmpty()) {
            taskToEdit.setName(newName);
        }

        LocalDateTime newStartTime = roundToNearest15Minutes(getValidDateTime("Enter new start time (yyyy-MM-dd HH:mm) (leave blank to keep current time): ", taskToEdit.getStartTime()));
        LocalDateTime newEndTime = roundToNearest15Minutes(getValidDateTime("Enter new end time (yyyy-MM-dd HH:mm) (leave blank to keep current time): ", taskToEdit.getEndTime()));

        Task newTask = new Task(taskToEdit.getName(), newStartTime, newEndTime);
        if (schedule.isOverlapping(newTask)) {
            System.out.println("Error: Edited task overlaps with an existing task. Changes not saved.");
        } else {
            taskToEdit.setStartTime(newStartTime);
            taskToEdit.setEndTime(newEndTime);
            System.out.println("Task edited successfully.");
        }
    }

    /**
     * Prints the daily schedule for a specified date.
     */
    private void printDailySchedule() {
        LocalDate date = getValidDate("Enter date (yyyy-MM-dd): ");
        System.out.println("Daily Schedule for " + date);
        for (Task task : schedule.getTasks()) {
            if (task.getStartTime().toLocalDate().equals(date)) {
                System.out.println(task);
            }
        }
    }

    /**
     * Prints the weekly schedule for a specified start date.
     */
    private void printWeeklySchedule() {
        LocalDate date = getValidDate("Enter start date of the week (yyyy-MM-dd): ");
        LocalDate endDate = date.plusDays(6);
        System.out.println("Weekly Schedule from " + date + " to " + endDate);
        for (Task task : schedule.getTasks()) {
            if (!task.getStartTime().toLocalDate().isBefore(date) && !task.getStartTime().toLocalDate().isAfter(endDate)) {
                System.out.println(task);
            }
        }
    }

    /**
     * Prints the monthly schedule for a specified month and year.
     */
    private void printMonthlySchedule() {
        LocalDate date = getValidMonthAndYear("Enter month and year (yyyy-MM): ");
        LocalDate endDate = date.withDayOfMonth(date.lengthOfMonth());
        System.out.println("Monthly Schedule for " + date.getMonth() + " " + date.getYear());
        for (Task task : schedule.getTasks()) {
            if (!task.getStartTime().toLocalDate().isBefore(date) && !task.getStartTime().toLocalDate().isAfter(endDate)) {
                System.out.println(task);
            }
        }
    }

    /**
     * Writes the daily schedule for a specified date to a file.
     */
    private void writeDailyScheduleToFile() {
        LocalDate date = getValidDate("Enter date (yyyy-MM-dd): ");
        System.out.print("Enter file name to write the daily schedule to: ");
        String fileName = scanner.nextLine();
        List<Task> dailyTasks = schedule.getTasks().stream()
                .filter(task -> task.getStartTime().toLocalDate().equals(date))
                .collect(Collectors.toList());
        try {
            schedule.writeTasksToFile(dailyTasks, fileName);
            System.out.println("Daily schedule written to " + fileName);
        } catch (IOException e) {
            System.out.println("Error writing daily schedule to file: " + e.getMessage());
        }
    }

    /**
     * Writes the weekly schedule for a specified start date to a file.
     */
    private void writeWeeklyScheduleToFile() {
        LocalDate startDate = getValidDate("Enter start date of the week (yyyy-MM-dd): ");
        LocalDate endDate = startDate.plusDays(6);
        System.out.print("Enter file name to write the weekly schedule to: ");
        String fileName = scanner.nextLine();
        List<Task> weeklyTasks = schedule.getTasks().stream()
                .filter(task -> !task.getStartTime().toLocalDate().isBefore(startDate) && !task.getStartTime().toLocalDate().isAfter(endDate))
                .collect(Collectors.toList());
        try {
            schedule.writeTasksToFile(weeklyTasks, fileName);
            System.out.println("Weekly schedule written to " + fileName);
        } catch (IOException e) {
            System.out.println("Error writing weekly schedule to file: " + e.getMessage());
        }
    }

    /**
     * Writes the monthly schedule for a specified month and year to a file.
     */
    private void writeMonthlyScheduleToFile() {
        LocalDate date = getValidMonthAndYear("Enter month and year (yyyy-MM): ");
        LocalDate endDate = date.withDayOfMonth(date.lengthOfMonth());
        System.out.print("Enter file name to write the monthly schedule to: ");
        String fileName = scanner.nextLine();
        List<Task> monthlyTasks = schedule.getTasks().stream()
                .filter(task -> !task.getStartTime().toLocalDate().isBefore(date) && !task.getStartTime().toLocalDate().isAfter(endDate))
                .collect(Collectors.toList());
        try {
            schedule.writeTasksToFile(monthlyTasks, fileName);
            System.out.println("Monthly schedule written to " + fileName);
        } catch (IOException e) {
            System.out.println("Error writing monthly schedule to file: " + e.getMessage());
        }
    }

    /**
     * Reads the schedule from a specified file.
     */
    private void readScheduleFromFile() {
        System.out.print("Enter file name to read the schedule from: ");
        String fileName = scanner.nextLine();
        try {
            schedule.readScheduleFromFile(fileName);
            System.out.println("Schedule read from " + fileName);
        } catch (IOException e) {
            System.out.println("Error reading schedule from file: " + e.getMessage());
        }
    }

    /**
     * Prompts the user for a valid date and returns it.
     *
     * @param prompt The prompt to display to the user.
     * @return The valid date entered by the user.
     */
    private LocalDate getValidDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return LocalDate.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
            }
        }
    }

    /**
     * Prompts the user for a valid month and year and returns it.
     *
     * @param prompt The prompt to display to the user.
     * @return The valid month and year entered by the user.
     */
    private LocalDate getValidMonthAndYear(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                YearMonth yearMonth = YearMonth.parse(input, DateTimeFormatter.ofPattern("yyyy-MM"));
                return yearMonth.atDay(1);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter the date in yyyy-MM format.");
            }
        }
    }

    /**
     * Prompts the user for a valid date and time and returns it.
     *
     * @param prompt The prompt to display to the user.
     * @return The valid date and time entered by the user.
     */
    private LocalDateTime getValidDateTime(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return LocalDateTime.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date and time format. Please enter the date and time in yyyy-MM-dd HH:mm format.");
            }
        }
    }

    /**
     * Prompts the user for a valid date and time and returns it, or returns the default value if the input is empty.
     *
     * @param prompt The prompt to display to the user.
     * @param defaultValue The default value to return if the input is empty.
     * @return The valid date and time entered by the user or the default value.
     */
    private LocalDateTime getValidDateTime(String prompt, LocalDateTime defaultValue) {
        System.out.print(prompt);
        String input = scanner.nextLine();
        if (input.isEmpty()) {
            return defaultValue;
        }
        try {
            return LocalDateTime.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date and time format. Keeping the current value.");
            return defaultValue;
        }
    }

    /**
     * Rounds a given LocalDateTime to the nearest 15 minutes.
     *
     * @param dateTime The LocalDateTime to round.
     * @return The rounded LocalDateTime.
     */
    private LocalDateTime roundToNearest15Minutes(LocalDateTime dateTime) {
        int minute = dateTime.getMinute();
        int newMinute;

        if (minute % 15 < 7.5) {
            newMinute = minute - (minute % 15);
        } else {
            newMinute = minute + (15 - minute % 15);
        }

        if (newMinute == 60) {
            dateTime = dateTime.plusHours(1);
            newMinute = 0;
        }

        return dateTime.withMinute(newMinute).withSecond(0).withNano(0);
    }

    /**
     * The main method to start the application.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        User user = new User();
        user.start();
    }
}
