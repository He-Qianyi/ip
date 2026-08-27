package lynn;

public class Task {
    protected final String description;
    protected boolean isDone;
    private final TaskType taskType;

    public Task(String description, TaskType taskType) {
        this.description = description;
        this.isDone = false;
        this.taskType = taskType;
    }

    protected String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    public void markAsDone() {
        isDone = true;
    }

    public void markAsNotDone() {
        isDone = false;
    }

    TaskType getTaskType() {
        return taskType;
    }

    boolean isDone() {
        return isDone;
    }

    String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "[" + taskType.getIcon() + "][" + getStatusIcon() + "] " + description;
    }
}
