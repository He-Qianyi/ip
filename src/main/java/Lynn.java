import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Lynn {
    public static void main(String[] args) {
        String banner = " _                          \n"
                + "| |    _   _ _ __  _ __    \n"
                + "| |   | | | | '_ \\| '_ \\   \n"
                + "| |___| |_| | | | | | | |  \n"
                + "|_____|\\__, |_| |_|_| |_|  \n"
                + "       |___/               \n";
        String line = "____________________________________________________________";
        Task[] tasks = new Task[100];
        int taskCount = 0;

        try {
            tasks = Storage.load();
            taskCount = countTasks(tasks);
        } catch (LynnException e) {
            System.out.println("OOPS! " + e.getMessage());
            System.out.println(line);
        }

        System.out.println(line);
        System.out.println(banner);
        System.out.println("Hello! I'm Lynn.");
        System.out.println("How can I help you today?");
        System.out.println(line);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            try {
                if ("bye".equals(command)) {
                    System.out.println("Bye. Hope to see you again soon!");
                    System.out.println(line);
                    break;
                }

                if ("list".equals(command)) {
                    System.out.println("Here are the tasks in your list:");
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println((i + 1) + "." + tasks[i]);
                    }
                    System.out.println(line);
                    continue;
                }

                if (command.startsWith("mark")) {
                    int taskNumber = parseTaskNumber(command, "mark", taskCount);
                    tasks[taskNumber].markAsDone();
                    Storage.save(tasks, taskCount);
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println("  " + tasks[taskNumber]);
                    System.out.println(line);
                    continue;
                }

                if (command.startsWith("unmark")) {
                    int taskNumber = parseTaskNumber(command, "unmark", taskCount);
                    tasks[taskNumber].markAsNotDone();
                    Storage.save(tasks, taskCount);
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println("  " + tasks[taskNumber]);
                    System.out.println(line);
                    continue;
                }

                if (command.startsWith("delete")) {
                    int taskNumber = parseTaskNumber(command, "delete", taskCount);
                    Task deletedTask = tasks[taskNumber];
                    taskCount = deleteTask(tasks, taskCount, taskNumber);
                    Storage.save(tasks, taskCount);
                    System.out.println("Noted. I've removed this task:");
                    System.out.println("  " + deletedTask);
                    System.out.println("Now you have " + taskCount + " tasks in the list.");
                    System.out.println(line);
                    continue;
                }

                if (command.startsWith("todo")) {
                    String description = parseDescription(command, "todo");
                    ensureTaskCapacity(taskCount);
                    tasks[taskCount] = new Todo(description);
                    taskCount++;
                    Storage.save(tasks, taskCount);
                    printTaskAdded(tasks[taskCount - 1], taskCount, line);
                    continue;
                }

                if (command.startsWith("deadline")) {
                    Deadline deadline = parseDeadline(command);
                    ensureTaskCapacity(taskCount);
                    tasks[taskCount] = deadline;
                    taskCount++;
                    Storage.save(tasks, taskCount);
                    printTaskAdded(tasks[taskCount - 1], taskCount, line);
                    continue;
                }

                if (command.startsWith("event")) {
                    String[] parts = parseEvent(command);
                    ensureTaskCapacity(taskCount);
                    tasks[taskCount] = new Event(parts[0], parts[1], parts[2]);
                    taskCount++;
                    Storage.save(tasks, taskCount);
                    printTaskAdded(tasks[taskCount - 1], taskCount, line);
                    continue;
                }

                throw new LynnException("I don't recognize that command yet. Try todo, deadline, event, list, mark, unmark, delete, or bye.");
            } catch (LynnException e) {
                System.out.println("OOPS! " + e.getMessage());
                System.out.println(line);
            }
        }
    }

    private static void printTaskAdded(Task task, int taskCount, String line) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        System.out.println(line);
    }

    private static int countTasks(Task[] tasks) {
        int taskCount = 0;
        while (taskCount < tasks.length && tasks[taskCount] != null) {
            taskCount++;
        }
        return taskCount;
    }

    private static void ensureTaskCapacity(int taskCount) throws LynnException {
        if (taskCount == 100) {
            throw new LynnException("Your task list is full.");
        }
    }

    private static int deleteTask(Task[] tasks, int taskCount, int taskNumber) {
        for (int i = taskNumber; i < taskCount - 1; i++) {
            tasks[i] = tasks[i + 1];
        }
        tasks[taskCount - 1] = null;
        return taskCount - 1;
    }

    private static String parseDescription(String command, String keyword) throws LynnException {
        String description = command.substring(keyword.length()).trim();
        if (description.isEmpty()) {
            throw new LynnException("The description of a " + keyword + " cannot be empty.");
        }
        return description;
    }

    private static Deadline parseDeadline(String command) throws LynnException {
        String details = command.substring("deadline".length()).trim();
        if (details.isEmpty()) {
            throw new LynnException("The description of a deadline cannot be empty.");
        }

        String[] parts = details.split(" /by ", 2);
        if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            throw new LynnException("Use deadline <description> /by <yyyy-MM-dd>.");
        }

        try {
            return new Deadline(parts[0].trim(), LocalDate.parse(parts[1].trim()));
        } catch (DateTimeParseException e) {
            throw new LynnException("Use yyyy-MM-dd for deadline dates, for example 2026-09-01.");
        }
    }

    private static String[] parseEvent(String command) throws LynnException {
        String details = command.substring("event".length()).trim();
        if (details.isEmpty()) {
            throw new LynnException("The description of an event cannot be empty.");
        }

        String[] parts = details.split(" /from | /to ", 3);
        if (parts.length < 3 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty() || parts[2].trim().isEmpty()) {
            throw new LynnException("Use event <description> /from <start> /to <end>.");
        }
        return new String[]{parts[0].trim(), parts[1].trim(), parts[2].trim()};
    }

    private static int parseTaskNumber(String command, String keyword, int taskCount) throws LynnException {
        String numberText = command.substring(keyword.length()).trim();
        if (numberText.isEmpty()) {
            throw new LynnException("Tell me which task number to " + keyword + ".");
        }

        try {
            int taskNumber = Integer.parseInt(numberText) - 1;
            if (taskNumber < 0 || taskNumber >= taskCount) {
                throw new LynnException("That task number is not in your list.");
            }
            return taskNumber;
        } catch (NumberFormatException e) {
            throw new LynnException("Task numbers should be whole numbers.");
        }
    }
}
