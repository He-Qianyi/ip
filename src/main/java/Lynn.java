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
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println("  " + tasks[taskNumber]);
                    System.out.println(line);
                    continue;
                }

                if (command.startsWith("unmark")) {
                    int taskNumber = parseTaskNumber(command, "unmark", taskCount);
                    tasks[taskNumber].markAsNotDone();
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println("  " + tasks[taskNumber]);
                    System.out.println(line);
                    continue;
                }

                if (command.startsWith("todo")) {
                    String description = parseDescription(command, "todo");
                    tasks[taskCount] = new Todo(description);
                    taskCount = printTaskAdded(tasks, taskCount, line);
                    continue;
                }

                if (command.startsWith("deadline")) {
                    String[] parts = parseDeadline(command);
                    tasks[taskCount] = new Deadline(parts[0], parts[1]);
                    taskCount = printTaskAdded(tasks, taskCount, line);
                    continue;
                }

                if (command.startsWith("event")) {
                    String[] parts = parseEvent(command);
                    tasks[taskCount] = new Event(parts[0], parts[1], parts[2]);
                    taskCount = printTaskAdded(tasks, taskCount, line);
                    continue;
                }

                throw new LynnException("I don't recognize that command yet. Try todo, deadline, event, list, mark, unmark, or bye.");
            } catch (LynnException e) {
                System.out.println("OOPS! " + e.getMessage());
                System.out.println(line);
            }
        }
    }

    private static int printTaskAdded(Task[] tasks, int taskCount, String line) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + tasks[taskCount]);
        taskCount++;
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        System.out.println(line);
        return taskCount;
    }

    private static String parseDescription(String command, String keyword) throws LynnException {
        String description = command.substring(keyword.length()).trim();
        if (description.isEmpty()) {
            throw new LynnException("The description of a " + keyword + " cannot be empty.");
        }
        return description;
    }

    private static String[] parseDeadline(String command) throws LynnException {
        String details = command.substring("deadline".length()).trim();
        if (details.isEmpty()) {
            throw new LynnException("The description of a deadline cannot be empty.");
        }

        String[] parts = details.split(" /by ", 2);
        if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            throw new LynnException("Use deadline <description> /by <time>.");
        }
        return new String[]{parts[0].trim(), parts[1].trim()};
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
