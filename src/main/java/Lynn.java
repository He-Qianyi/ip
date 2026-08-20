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

            if (command.startsWith("mark ")) {
                int taskNumber = Integer.parseInt(command.substring(5)) - 1;
                tasks[taskNumber].markAsDone();
                System.out.println("Nice! I've marked this task as done:");
                System.out.println("  " + tasks[taskNumber]);
                System.out.println(line);
                continue;
            }

            if (command.startsWith("unmark ")) {
                int taskNumber = Integer.parseInt(command.substring(7)) - 1;
                tasks[taskNumber].markAsNotDone();
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println("  " + tasks[taskNumber]);
                System.out.println(line);
                continue;
            }

            if (command.startsWith("todo ")) {
                tasks[taskCount] = new Todo(command.substring(5));
                taskCount++;
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + tasks[taskCount - 1]);
                System.out.println("Now you have " + taskCount + " tasks in the list.");
                System.out.println(line);
                continue;
            }

            if (command.startsWith("deadline ")) {
                String[] parts = command.substring(9).split(" /by ", 2);
                tasks[taskCount] = new Deadline(parts[0], parts[1]);
                taskCount++;
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + tasks[taskCount - 1]);
                System.out.println("Now you have " + taskCount + " tasks in the list.");
                System.out.println(line);
                continue;
            }

            if (command.startsWith("event ")) {
                String[] parts = command.substring(6).split(" /from | /to ", 3);
                tasks[taskCount] = new Event(parts[0], parts[1], parts[2]);
                taskCount++;
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + tasks[taskCount - 1]);
                System.out.println("Now you have " + taskCount + " tasks in the list.");
                System.out.println(line);
                continue;
            }

            tasks[taskCount] = new Todo(command);
            taskCount++;
            System.out.println("Got it. I've added this task:");
            System.out.println("  " + tasks[taskCount - 1]);
            System.out.println("Now you have " + taskCount + " tasks in the list.");
            System.out.println(line);
        }
    }
}
