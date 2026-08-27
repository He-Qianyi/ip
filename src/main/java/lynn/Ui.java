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

    void showWelcome() {
        System.out.println(LINE);
        System.out.println(BANNER);
        System.out.println("Hello! I'm Lynn.");
        System.out.println("How can I help you today?");
        System.out.println(LINE);
    }

    void showLoadingError(String message) {
        showError(message);
    }

    void showError(String message) {
        System.out.println("OOPS! " + message);
        System.out.println(LINE);
    }

    void showGoodbye() {
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }

    void showTaskList(TaskList tasks) {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
        System.out.println(LINE);
    }

    void showTaskAdded(Task task, int taskCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        System.out.println(LINE);
    }

    void showTaskMarked(Task task) {
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + task);
        System.out.println(LINE);
    }

    void showTaskUnmarked(Task task) {
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + task);
        System.out.println(LINE);
    }

    void showTaskDeleted(Task task, int taskCount) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        System.out.println(LINE);
    }
}
