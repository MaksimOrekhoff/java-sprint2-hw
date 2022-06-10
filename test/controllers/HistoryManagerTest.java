package controllers;

import controllers.enumeratedtype.Status;
import controllers.enumeratedtype.TypeTask;
import controllers.history.HistoryManager;
import controllers.history.InMemoryHistoryManager;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {

    HistoryManager historyManager;
    Task task;
    Epic epic;
    Subtask subtask;

    @BeforeEach
    void init() {
        historyManager = new InMemoryHistoryManager();
        task = new Task("первая", "ывпа", 1, Status.NEW, TypeTask.TASK, 50, LocalDateTime.of(2022, 10, 5, 12, 5));
        epic = new Epic("first", "create first test", 2, Status.NEW, 50, LocalDateTime.of(2022, 11, 5, 12, 5));
        subtask = new Subtask("четвертая", "ывп", 3, Status.NEW, 2, 50, LocalDateTime.of(2021, 10, 5, 12, 5));
    }

    @Test
    public void checkHistoryManagerAnVoidTest() {
        assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    void checkHistoryManagerAddTaskTest() {
        int size = 1;

        historyManager.add(task);
        Task actual = historyManager.getHistory().get(0);

        assertEquals(task, actual);
        assertEquals(size, historyManager.getHistory().size());

    }

    @Test
    void checkHistoryManagerDoubleAddTaskTest() {
        int size = 2;
        historyManager.add(task);
        historyManager.add(task);

        assertFalse(historyManager.getHistory().isEmpty(), "История не пустая");
        assertNotEquals(size, historyManager.getHistory().size(), "В истории одна задача");
    }

    @Test
    void checkHistoryManagerSubsequenceListTaskGetHistoryTest() {
        historyManager.add(task);
        historyManager.add(epic);

        List<Task> expected = List.of(task, epic);
        int size = historyManager.getHistory().size();
        List<Task> actual = historyManager.getHistory();

        assertEquals(expected, actual);
        assertEquals(size, historyManager.getHistory().size());
    }

//    @Test
//    void checkHistoryManagerNotExistingListTaskGetHistoryTest() { deleteTaskFromTasks
//        List<Task> expected = new ArrayList<>();
//        expected.add(taskManager.getTask(1));
//        int size = historyManager.getHistory().size();
//        List<Task> actual = historyManager.getHistory();
//
//        assertNotEquals(expected, actual);
//        assertEquals(size, historyManager.getHistory().size());
//    }

    @Test
    void checkHistoryManagerRemoveNotExistingTaskTest() {
        int size = 1;
        historyManager.add(task);

        assertEquals(size, historyManager.getHistory().size());

        historyManager.remove(2);

        assertEquals(size, historyManager.getHistory().size());
        assertEquals(task, historyManager.getHistory().get(0));
    }

    @Test
    void checkHistoryManagerRemoveExistingTaskTest() {
        int size = historyManager.getHistory().size();
        historyManager.add(task);

        assertNotEquals(size, historyManager.getHistory().size());

        historyManager.remove(1);

        assertEquals(size, historyManager.getHistory().size());
    }

    @Test
    void checkHistoryManagerRemoveAverageTaskGetHistoryTest() {
        historyManager.add(epic);
        historyManager.add(subtask);
        historyManager.add(task);
        List<Task> expected = List.of(epic, subtask, task);
        List<Task> actual = historyManager.getHistory();
        int size = historyManager.getHistory().size();
        Task taskExpected = historyManager.getHistory().get(1);

        assertEquals(subtask, taskExpected, "это онда и та же задача");
        assertEquals(expected, actual);

        historyManager.remove(3);
        expected = List.of(epic, task);
        actual = historyManager.getHistory();

        assertNotEquals(taskExpected, historyManager.getHistory().get(1), "Задачи не совпадают");
        assertEquals(expected, actual);
        assertNotEquals(size, historyManager.getHistory().size());
    }

    @Test
    void checkHistoryManagerRemoveFirstTaskGetHistoryTest() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask);
        List<Task> expected = List.of(task, epic, subtask);
        List<Task> actual = historyManager.getHistory();
        assertEquals(expected, actual);
        int size = historyManager.getHistory().size();
        Task taskExpected = historyManager.getHistory().get(0);

        assertEquals(task, taskExpected, "это онда и та же задача");
        assertEquals(expected, actual);

        historyManager.remove(1);
        expected = List.of(epic, subtask);
        actual = historyManager.getHistory();

        assertNotEquals(taskExpected, historyManager.getHistory().get(0), "Задачи не совпадают");
        assertEquals(expected, actual);
        assertNotEquals(size, historyManager.getHistory().size());
    }

    @Test
    void checkHistoryManagerRemoveLastTaskGetHistoryTest() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask);
        List<Task> expected = List.of(task, epic, subtask);
        List<Task> actual = historyManager.getHistory();
        assertEquals(expected, actual);
        int size = historyManager.getHistory().size();
        Task taskExpected = historyManager.getHistory().get(2);

        assertEquals(subtask, taskExpected, "это онда и та же задача");
        assertEquals(expected, actual);

        historyManager.remove(3);
        expected = List.of(task, epic);
        actual = historyManager.getHistory();

        assertEquals(expected, actual);
        assertNotEquals(size, historyManager.getHistory().size(), "удален последний элемент. Размеры не равны");
    }

}