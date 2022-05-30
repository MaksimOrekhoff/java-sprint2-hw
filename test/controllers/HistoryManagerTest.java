package controllers;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest<T extends TaskManager> {

    HistoryManager historyManager;
    TaskManager taskManager;

    Task task;
    Epic epic;
    Subtask subtask;

    @BeforeEach
    void init() {
        historyManager = Managers.getDefaultHistory();
        task = new Task("первая", "ывпа", 20, Status.NEW, TypeTask.TASK, 50, LocalDateTime.of(2022, 10, 5, 12, 5));
        epic = new Epic("first", "create first test", 50, Status.NEW, 50, LocalDateTime.of(2022, 11, 5, 12, 5));
        subtask = new Subtask("четвертая", "ывп", 50, Status.NEW, 2, 50, LocalDateTime.of(2021, 10, 5, 12, 5));
        taskManager = new InMemoryTaskManager(historyManager);
    }

    @Test
    public void checkHistoryManagerAnVoidTest() {
        assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    void checkHistoryManagerAddExistingTaskTest() {
        taskManager.createTask(task);
        Task expected = taskManager.getTask(1);
        Task actual = historyManager.getHistory().get(0);
        int size = historyManager.getHistory().size();

        assertEquals(expected, actual);
        assertEquals(size, historyManager.getHistory().size());

    }

    @Test
    void checkHistoryManagerAddNoExistingTaskTest() {
        Task expected = taskManager.getTask(2);
        int size = historyManager.getHistory().size();

        assertNull(expected);
        assertEquals(size, historyManager.getHistory().size());
    }

    @Test
    void checkHistoryManagerExistingListTaskGetHistoryTest() {
        taskManager.createTask(task);
        taskManager.createEpic(epic);

        List<Task> expected = List.of(taskManager.getTask(1), taskManager.getEpic(2));
        int size = historyManager.getHistory().size();
        List<Task> actual = historyManager.getHistory();

        assertEquals(expected, actual);
        assertEquals(size, historyManager.getHistory().size());
    }

    @Test
    void checkHistoryManagerNotExistingListTaskGetHistoryTest() {
        List<Task> expected = new ArrayList<>();
        expected.add(taskManager.getTask(1));
        int size = historyManager.getHistory().size();
        List<Task> actual = historyManager.getHistory();

        assertNotEquals(expected, actual);
        assertEquals(size, historyManager.getHistory().size());
    }

    @Test
    void checkHistoryManagerRemoveNotExistingTaskTest() {
        taskManager.createTask(task);
        List<Task> expected = List.of(taskManager.getTask(1));
        int size = historyManager.getHistory().size();
        List<Task> actual = historyManager.getHistory();

        assertEquals(expected, actual);
        assertEquals(size, historyManager.getHistory().size());

        taskManager.deleteTask(2);

        expected = List.of(taskManager.getTask(1));
        size = historyManager.getHistory().size();
        actual = historyManager.getHistory();

        assertEquals(expected, actual);
        assertEquals(size, historyManager.getHistory().size());
    }

    @Test
    void checkHistoryManagerRemoveAverageTaskGetHistoryTest() {
        taskManager.createTask(task);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);

        Epic task1 = taskManager.getEpic(2);
        Task task2 = taskManager.getTask(1);
        Subtask task3 = taskManager.getSubtask(3);

        List<Task> expected = List.of(task1, task2, task3);
        List<Task> actual = historyManager.getHistory();
        assertEquals(expected, actual);

        Task removeTask = expected.get(1);
        Task averageTask = historyManager.getHistory().get(1);
        assertEquals(removeTask, averageTask);

        int size = historyManager.getHistory().size();
        assertEquals(size, historyManager.getHistory().size());

        taskManager.deleteTask(1);

        expected = List.of(task1, task3);
        actual = historyManager.getHistory();
        assertEquals(expected, actual);

        size = historyManager.getHistory().size();
        assertEquals(size, historyManager.getHistory().size());

        averageTask = historyManager.getHistory().get(1);
        assertNotEquals(removeTask, averageTask);
    }

    @Test
    void checkHistoryManagerRemoveFirstTaskGetHistoryTest() {
        taskManager.createTask(task);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);

        Task task1 = taskManager.getTask(1);
        Epic task2 = taskManager.getEpic(2);
        Subtask task3 = taskManager.getSubtask(3);

        List<Task> expected = List.of(task1, task2, task3);
        List<Task> actual = historyManager.getHistory();
        assertEquals(expected, actual);

        Task removeTask = expected.get(0);
        Task averageTask = historyManager.getHistory().get(0);
        assertEquals(removeTask, averageTask);

        int size = historyManager.getHistory().size();
        assertEquals(size, historyManager.getHistory().size());

        taskManager.deleteTask(1);

        expected = List.of(task2, task3);
        actual = historyManager.getHistory();
        assertEquals(expected, actual);

        size = historyManager.getHistory().size();
        assertEquals(size, historyManager.getHistory().size());

        averageTask = historyManager.getHistory().get(0);
        assertNotEquals(removeTask, averageTask);
    }

    @Test
    void checkHistoryManagerRemoveLastTaskGetHistoryTest(){
        taskManager.createTask(task);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);

        Task task1 = taskManager.getTask(1);
        Epic task2 = taskManager.getEpic(2);
        Subtask task3 = taskManager.getSubtask(3);

        List<Task> expected = List.of(task1, task2, task3);
        List<Task> actual = historyManager.getHistory();
        assertEquals(expected, actual);

        Task removeTask = expected.get(2);
        Task averageTask = historyManager.getHistory().get(2);
        assertEquals(removeTask, averageTask);

        int size = historyManager.getHistory().size();
        assertEquals(size, historyManager.getHistory().size());

        taskManager.deleteSubtask(3);

        expected = List.of(task1, task2);
        actual = historyManager.getHistory();
        assertEquals(expected, actual);

        size = historyManager.getHistory().size();
        assertEquals(size, historyManager.getHistory().size());

        averageTask = historyManager.getHistory().get(historyManager.getHistory().size() - 1);
        assertNotEquals(removeTask, averageTask);
    }

}