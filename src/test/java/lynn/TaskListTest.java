package lynn;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class TaskListTest {
    @Test
    void add_twoTasks_keepsTasksInInsertionOrder() throws LynnException {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("read book"));
        tasks.add(new Todo("return book"));

        assertEquals(2, tasks.size());
        assertEquals("[T][ ] read book", tasks.get(0).toString());
        assertEquals("[T][ ] return book", tasks.get(1).toString());
    }

    @Test
    void delete_firstTask_shiftsRemainingTaskForward() throws LynnException {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("read book"));
        tasks.add(new Todo("return book"));

        Task deletedTask = tasks.delete(0);

        assertEquals("[T][ ] read book", deletedTask.toString());
        assertEquals(1, tasks.size());
        assertEquals("[T][ ] return book", tasks.get(0).toString());
    }
}
