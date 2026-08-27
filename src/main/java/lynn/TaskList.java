package lynn;

/** Stores and manages Lynn's fixed-capacity task list. */
class TaskList {
    private static final int MAX_TASKS = 100;
    private final Task[] tasks;
    private int taskCount;

    TaskList() {
        this(new Task[MAX_TASKS]);
    }

    TaskList(Task[] tasks) {
        this.tasks = tasks;
        taskCount = countTasks(tasks);
    }

    void add(Task task) throws LynnException {
        if (taskCount == MAX_TASKS) {
            throw new LynnException("Your task list is full.");
        }
        tasks[taskCount] = task;
        taskCount++;
    }

    Task get(int index) {
        return tasks[index];
    }

    Task delete(int index) {
        Task deletedTask = tasks[index];
        for (int i = index; i < taskCount - 1; i++) {
            tasks[i] = tasks[i + 1];
        }
        taskCount--;
        tasks[taskCount] = null;
        return deletedTask;
    }

    int size() {
        return taskCount;
    }

    private int countTasks(Task[] loadedTasks) {
        int loadedTaskCount = 0;
        while (loadedTaskCount < loadedTasks.length && loadedTasks[loadedTaskCount] != null) {
            loadedTaskCount++;
        }
        return loadedTaskCount;
    }
}
