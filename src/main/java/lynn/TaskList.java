package lynn;

import java.util.Arrays;

/** Stores and manages Lynn's fixed-capacity task list. */
class TaskList {
    private static final int MAX_TASKS = 100;

    private final Task[] tasks;

    private int taskCount;

    /** Creates an empty task list with the fixed project capacity. */
    TaskList() {
        this(new Task[MAX_TASKS]);
    }

    /** Creates a task list from the contiguous tasks loaded from storage. */
    TaskList(Task[] tasks) {
        this.tasks = tasks;
        taskCount = countTasks(tasks);
    }

    /** Adds a task at the end of the list. */
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

    /** Removes the task at an already-validated zero-based index. */
    Task delete(int index) {
        Task deletedTask = tasks[index];
        for (int i = index; i < taskCount - 1; i++) {
            tasks[i] = tasks[i + 1];
        }
        taskCount--;
        tasks[taskCount] = null;
        return deletedTask;
    }

    Task[] find(String keyword) {
        Task[] matchingTasks = new Task[taskCount];
        int matchingTaskCount = 0;
        for (int i = 0; i < taskCount; i++) {
            if (tasks[i].containsKeyword(keyword)) {
                matchingTasks[matchingTaskCount] = tasks[i];
                matchingTaskCount++;
            }
        }
        return Arrays.copyOf(matchingTasks, matchingTaskCount);
    }

    int size() {
        return taskCount;
    }

    /** Counts the contiguous non-null tasks that were loaded from storage. */
    private int countTasks(Task[] loadedTasks) {
        int loadedTaskCount = 0;
        while (loadedTaskCount < loadedTasks.length && loadedTasks[loadedTaskCount] != null) {
            loadedTaskCount++;
        }
        return loadedTaskCount;
    }
}
