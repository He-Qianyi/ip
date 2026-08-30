package lynn;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/** Parses and validates user commands before Lynn executes them. */
class Parser {
    private Parser() {
    }

    /** Extracts the first word of a command after trimming whitespace. */
    static String getCommandWord(String command) {
        String trimmedCommand = command.trim();
        if (trimmedCommand.isEmpty()) {
            return "";
        }
        return trimmedCommand.split("\\s+", 2)[0];
    }

    /** Ensures that a command contains no arguments beyond its keyword. */
    static void requireExactCommand(String command, String keyword) throws LynnException {
        if (!command.trim().equals(keyword)) {
            throw unknownCommandException();
        }
    }

    /** Parses a todo, deadline, or event command into a task object. */
    static Task parseTask(String command) throws LynnException {
        return switch (getCommandWord(command)) {
        case "todo" -> new Todo(parseDescription(command, "todo"));
        case "deadline" -> parseDeadline(command);
        case "event" -> parseEvent(command);
        default -> throw unknownCommandException();
        };
    }

    /** Parses and validates a one-based task number from a user command. */
    static int parseTaskNumber(String command, String keyword, int taskCount) throws LynnException {
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

    /** Extracts a non-empty keyword for a find command. */
    static String parseFindKeyword(String command) throws LynnException {
        String keyword = command.substring("find".length()).trim();
        if (keyword.isEmpty()) {
            throw new LynnException("The keyword to find cannot be empty.");
        }
        return keyword;
    }

    /** Extracts a non-empty description following a task keyword. */
    private static String parseDescription(String command, String keyword) throws LynnException {
        String description = command.substring(keyword.length()).trim();
        if (description.isEmpty()) {
            throw new LynnException("The description of a " + keyword + " cannot be empty.");
        }
        return description;
    }

    /** Parses a deadline command and validates its ISO-8601 date. */
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

    /** Parses an event command and validates its start and end text. */
    private static Event parseEvent(String command) throws LynnException {
        String details = command.substring("event".length()).trim();
        if (details.isEmpty()) {
            throw new LynnException("The description of an event cannot be empty.");
        }

        String[] parts = details.split(" /from | /to ", 3);
        if (parts.length < 3 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()
                || parts[2].trim().isEmpty()) {
            throw new LynnException("Use event <description> /from <start> /to <end>.");
        }
        return new Event(parts[0].trim(), parts[1].trim(), parts[2].trim());
    }

    private static LynnException unknownCommandException() {
        return new LynnException(
                "I don't recognize that command yet. Try todo, deadline, event, list, find, mark, unmark, "
                        + "delete, or bye.");
    }
}
