package lynn;

import java.io.PrintStream;
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
    private final PrintStream output;

    Ui() {
        this(System.out);
    }

    /** Creates a user interface that writes messages to the supplied stream. */
    Ui(PrintStream output) {
        scanner = new Scanner(System.in);
        this.output = output;
    }

    boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    String readCommand() {
        return scanner.nextLine();
    }

    void showWelcome() {
        output.println(LINE);
        output.println(BANNER);
        output.println("Hello! I'm Lynn.");
        output.println("How can I help you today?");
        output.println(LINE);
    }

    void showLoadingError(String message) {
        showError(message);
    }

    void showError(String message) {
        output.println("OOPS! " + message);
        output.println(LINE);
    }

    void showGoodbye() {
        output.println("Bye. Hope to see you again soon!");
        output.println(LINE);
    }

    void showTaskList(TaskList tasks) {
        output.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            output.println((i + 1) + "." + tasks.get(i));
        }
        output.println(LINE);
    }

    void showMatchingTasks(Task[] matchingTasks) {
        output.println("Here are the matching tasks in your list:");
        int displayedTaskCount = 1;
        for (Task matchingTask : matchingTasks) {
            if (matchingTask != null) {
                output.println(displayedTaskCount + "." + matchingTask);
                displayedTaskCount++;
            }
        }
        output.println(LINE);
    }

    void showTaskAdded(Task task, int taskCount) {
        output.println("Got it. I've added this task:");
        output.println("  " + task);
        output.println("Now you have " + taskCount + " tasks in the list.");
        output.println(LINE);
    }

    void showTaskMarked(Task task) {
        output.println("Nice! I've marked this task as done:");
        output.println("  " + task);
        output.println(LINE);
    }

    void showTaskUnmarked(Task task) {
        output.println("OK, I've marked this task as not done yet:");
        output.println("  " + task);
        output.println(LINE);
    }

    void showTaskDeleted(Task task, int taskCount) {
        output.println("Noted. I've removed this task:");
        output.println("  " + task);
        output.println("Now you have " + taskCount + " tasks in the list.");
        output.println(LINE);
    }
}
