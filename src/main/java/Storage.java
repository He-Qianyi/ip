import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/** Handles persistent storage of Lynn's task list. */
final class Storage {
    private static final Path STORAGE_PATH = Path.of("data", "lynn.txt");

    private Storage() {
    }

    /** Loads saved tasks, creating the storage directory and file if needed. */
    static Task[] load() throws LynnException {
        try {
            createStorageIfMissing();
            List<String> taskLines = Files.readAllLines(STORAGE_PATH, StandardCharsets.UTF_8);
            Task[] tasks = new Task[100];
            int taskCount = 0;

            for (String taskLine : taskLines) {
                if (taskLine.isBlank()) {
                    continue;
                }
                if (taskCount == tasks.length) {
                    throw new LynnException("Your saved task list is too large to load.");
                }
                tasks[taskCount] = parseTask(taskLine);
                taskCount++;
            }
            return tasks;
        } catch (IOException e) {
            throw new LynnException("I couldn't load your saved tasks.");
        } catch (IllegalArgumentException e) {
            throw new LynnException("Your saved task data is invalid.");
        }
    }

    /** Saves all current tasks to a relative, OS-independent file path. */
    static void save(Task[] tasks, int taskCount) throws LynnException {
        try {
            createStorageIfMissing();
            List<String> taskLines = new ArrayList<>();
            for (int i = 0; i < taskCount; i++) {
                taskLines.add(formatTask(tasks[i]));
            }
            Files.write(STORAGE_PATH, taskLines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new LynnException("I couldn't save your tasks.");
        }
    }

    private static void createStorageIfMissing() throws IOException {
        Files.createDirectories(STORAGE_PATH.getParent());
        if (Files.notExists(STORAGE_PATH)) {
            Files.createFile(STORAGE_PATH);
        }
    }

    private static String formatTask(Task task) {
        String status = task.isDone() ? "1" : "0";
        String description = encode(task.getDescription());
        return switch (task.getTaskType()) {
        case TODO -> "T|" + status + "|" + description;
        case DEADLINE -> "D|" + status + "|" + description + "|"
                + encode(((Deadline) task).getBy());
        case EVENT -> "E|" + status + "|" + description + "|"
                + encode(((Event) task).getFrom()) + "|" + encode(((Event) task).getTo());
        };
    }

    private static Task parseTask(String taskLine) {
        String[] fields = taskLine.split("\\|", -1);
        Task task = switch (fields[0]) {
        case "T" -> createTodo(fields);
        case "D" -> createDeadline(fields);
        case "E" -> createEvent(fields);
        default -> throw new IllegalArgumentException("Unknown task type");
        };
        if ("1".equals(fields[1])) {
            task.markAsDone();
        } else if (!"0".equals(fields[1])) {
            throw new IllegalArgumentException("Unknown task status");
        }
        return task;
    }

    private static Todo createTodo(String[] fields) {
        requireFieldCount(fields, 3);
        return new Todo(decode(fields[2]));
    }

    private static Deadline createDeadline(String[] fields) {
        requireFieldCount(fields, 4);
        return new Deadline(decode(fields[2]), decode(fields[3]));
    }

    private static Event createEvent(String[] fields) {
        requireFieldCount(fields, 5);
        return new Event(decode(fields[2]), decode(fields[3]), decode(fields[4]));
    }

    private static void requireFieldCount(String[] fields, int expectedCount) {
        if (fields.length != expectedCount) {
            throw new IllegalArgumentException("Incorrect number of fields");
        }
    }

    private static String encode(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private static String decode(String value) {
        return new String(Base64.getDecoder().decode(value), StandardCharsets.UTF_8);
    }
}
