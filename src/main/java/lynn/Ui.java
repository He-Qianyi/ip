package lynn;

import java.util.Scanner;

/** Handles all command-line input and output for Lynn. */
class Ui {
    private static final String LINE = "____________________________________________________________";
    private static final String BANNER = " _                          \n"
            + "| |    _   _ _ __  _ __    \n"
            + "| |   | | | | '_ \\| '_ \\   \n"
            + "| |___| |_| | | | | | | |  \n"
            + "|_____|\\__, |_| |_|_| |_|  \n"
            + "       |___/               \n";
    private final Scanner scanner;

    Ui() {
        scanner = new Scanner(System.in);
    }

    boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    String readCommand() {
        return scanner.nextLine();
    }

    /** Prints any number of lines in the order they are supplied. */
    private void showLines(String... lines) {
        for (String line : lines) {
            System.out.println(line);
        }
    }

    void showWelcome() {
        showLines(LINE, BANNER, "Hello! I'm Lynn.", "How can I help you today?", LINE);
    }

    void showLoadingError(String message) {
        showError(message);
    }

    void showError(String message) {
        showLines("OOPS! " + message, LINE);
    }

    void showGoodbye() {
        showLines("Bye. Hope to see you again soon!", LINE);
    }

    void showTaskList(TaskList tasks) {
        showLines("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
        showLines(LINE);
    }

    void showMatchingTasks(Task[] matchingTasks) {
        showLines("Here are the matching tasks in your list:");
        int displayedTaskCount = 1;
        for (Task matchingTask : matchingTasks) {
            if (matchingTask != null) {
                System.out.println(displayedTaskCount + "." + matchingTask);
                displayedTaskCount++;
            }
        }
        showLines(LINE);
    }

    void showTaskAdded(Task task, int taskCount) {
        showLines("Got it. I've added this task:", "  " + task,
                "Now you have " + taskCount + " tasks in the list.", LINE);
    }

    void showTaskMarked(Task task) {
        showLines("Nice! I've marked this task as done:", "  " + task, LINE);
    }

    void showTaskUnmarked(Task task) {
        showLines("OK, I've marked this task as not done yet:", "  " + task, LINE);
    }

    void showTaskDeleted(Task task, int taskCount) {
        showLines("Noted. I've removed this task:", "  " + task,
                "Now you have " + taskCount + " tasks in the list.", LINE);
    }
}
