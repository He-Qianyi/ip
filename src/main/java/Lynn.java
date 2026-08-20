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
        String[] tasks = new String[100];
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
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + ". " + tasks[i]);
                }
                System.out.println(line);
                continue;
            }

            tasks[taskCount] = command;
            taskCount++;
            System.out.println("added: " + command);
            System.out.println(line);
        }
    }
}
