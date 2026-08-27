package lynn;

/** Coordinates the chatbot's user interface, task list, parser, and storage. */
public class Lynn {
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    public Lynn() {
        storage = new Storage();
        ui = new Ui();
        TaskList loadedTasks;
        try {
            loadedTasks = storage.load();
        } catch (LynnException e) {
            ui.showLoadingError(e.getMessage());
            loadedTasks = new TaskList();
        }
        tasks = loadedTasks;
    }

    /** Runs Lynn until the user enters the bye command. */
    public void run() {
        ui.showWelcome();
        while (ui.hasNextCommand()) {
            String command = ui.readCommand();
            try {
                if (handleCommand(command)) {
                    return;
                }
            } catch (LynnException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    private boolean handleCommand(String command) throws LynnException {
        String commandWord = Parser.getCommandWord(command);
        switch (commandWord) {
        case "bye":
            Parser.requireExactCommand(command, "bye");
            ui.showGoodbye();
            return true;
        case "list":
            Parser.requireExactCommand(command, "list");
            ui.showTaskList(tasks);
            return false;
        case "find":
            findTasks(command);
            return false;
        case "mark":
            markTask(command, true);
            return false;
        case "unmark":
            markTask(command, false);
            return false;
        case "delete":
            deleteTask(command);
            return false;
        default:
            addTask(command);
            return false;
        }
    }

    private void findTasks(String command) throws LynnException {
        String keyword = Parser.parseFindKeyword(command);
        Task[] matchingTasks = tasks.find(keyword);
        ui.showMatchingTasks(matchingTasks);
    }

    private void addTask(String command) throws LynnException {
        Task task = Parser.parseTask(command);
        tasks.add(task);
        storage.save(tasks);
        ui.showTaskAdded(task, tasks.size());
    }

    private void markTask(String command, boolean isDone) throws LynnException {
        String commandWord = Parser.getCommandWord(command);
        int taskNumber = Parser.parseTaskNumber(command, commandWord, tasks.size());
        Task task = tasks.get(taskNumber);
        if (isDone) {
            task.markAsDone();
            storage.save(tasks);
            ui.showTaskMarked(task);
        } else {
            task.markAsNotDone();
            storage.save(tasks);
            ui.showTaskUnmarked(task);
        }
    }

    private void deleteTask(String command) throws LynnException {
        int taskNumber = Parser.parseTaskNumber(command, "delete", tasks.size());
        Task deletedTask = tasks.delete(taskNumber);
        storage.save(tasks);
        ui.showTaskDeleted(deletedTask, tasks.size());
    }

    public static void main(String[] args) {
        new Lynn().run();
    }
}
