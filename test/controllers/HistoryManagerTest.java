package controllers;

import model.Task;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {

    @Test
    public void checkHistoryManagerAnVoidTest() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    void checkHistoryManagerAddExistingTaskTest() throws IllegalAccessException {
        HistoryManager historyManager = Managers.getDefaultHistory();
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager(historyManager);
        inMemoryTaskManager.createTask("первая", "ывпа", 50, LocalDateTime.of(2022, 10, 5, 12, 5));
        Task expected = inMemoryTaskManager.getTask(1);
        Task actual = historyManager.getHistory().get(0);
        int size = historyManager.getHistory().size();

        assertEquals(expected, actual);
        assertEquals(size, historyManager.getHistory().size());

    }

    @Test
    void checkHistoryManagerAddNoExistingTaskTest() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager(historyManager);
        Task expected = inMemoryTaskManager.getTask(1);
        int size = historyManager.getHistory().size();

        assertNull(expected);
        assertEquals(size, historyManager.getHistory().size());

    }

    @Test
    void checkHistoryManagerExistingListTaskGetHistoryTest() throws IllegalAccessException {
        HistoryManager historyManager = Managers.getDefaultHistory();
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager(historyManager);
        inMemoryTaskManager.createTask("первая", "ывпа", 50, LocalDateTime.of(2022, 10, 5, 12, 5));
        inMemoryTaskManager.createTask("вторая", "выап", 50, LocalDateTime.of(2021, 10, 5, 12, 56));

        List<Task> expected = List.of(inMemoryTaskManager.getTask(1), inMemoryTaskManager.getTask(2));
        int size = historyManager.getHistory().size();
        List<Task> actual = historyManager.getHistory();

        assertEquals(expected, actual);
        assertEquals(size, historyManager.getHistory().size());
    }

    @Test
    void checkHistoryManagerNotExistingListTaskGetHistoryTest() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager(historyManager);

        List<Task> expected = new ArrayList<>();
        expected.add(inMemoryTaskManager.getTask(1));
        int size = historyManager.getHistory().size();
        List<Task> actual = historyManager.getHistory();

        assertNotEquals(expected, actual);
        assertEquals(size, historyManager.getHistory().size());
    }

    @Test
    void checkHistoryManagerRemoveNotExistingTaskTest() throws IllegalAccessException {
        HistoryManager historyManager = Managers.getDefaultHistory();
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager(historyManager);
        inMemoryTaskManager.createTask("первая", "ывпа", 50, LocalDateTime.of(2022, 10, 5, 12, 5));

        List<Task> expected = List.of(inMemoryTaskManager.getTask(1));
        int size = historyManager.getHistory().size();
        List<Task> actual = historyManager.getHistory();

        assertEquals(expected, actual);
        assertEquals(size, historyManager.getHistory().size());

        inMemoryTaskManager.deleteTask(2);

        expected = List.of(inMemoryTaskManager.getTask(1));
        size = historyManager.getHistory().size();
        actual = historyManager.getHistory();

        assertEquals(expected, actual);
        assertEquals(size, historyManager.getHistory().size());

    }

    @Test
    void checkHistoryManagerRemoveAverageTaskGetHistoryTest() throws IllegalAccessException {
        HistoryManager historyManager = Managers.getDefaultHistory();
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager(historyManager);
        inMemoryTaskManager.createTask("первая", "ывпа", 50, LocalDateTime.of(2022, 10, 5, 12, 5));
        inMemoryTaskManager.createTask("вторая", "выап", 50, LocalDateTime.of(2021, 10, 5, 12, 56));
        inMemoryTaskManager.createTask("третий", "ывапр", 50, LocalDateTime.of(2020, 10, 5, 12, 5));

        Task task1 = inMemoryTaskManager.getTask(1);
        Task task2 = inMemoryTaskManager.getTask(2);
        Task task3 = inMemoryTaskManager.getTask(3);

        List<Task> expected = List.of(task1, task2, task3);
        List<Task> actual = historyManager.getHistory();
        assertEquals(expected, actual);

        Task removeTask = expected.get(1);
        Task averageTask = historyManager.getHistory().get(1);
        assertEquals(removeTask, averageTask);

        int size = historyManager.getHistory().size();
        assertEquals(size, historyManager.getHistory().size());

        inMemoryTaskManager.deleteTask(2);

        expected = List.of(task1, task3);
        actual = historyManager.getHistory();
        assertEquals(expected, actual);

        size = historyManager.getHistory().size();
        assertEquals(size, historyManager.getHistory().size());

        averageTask = historyManager.getHistory().get(1);
        assertNotEquals(removeTask, averageTask);
    }

    @Test
    void checkHistoryManagerRemoveFirstTaskGetHistoryTest() throws IllegalAccessException {
        HistoryManager historyManager = Managers.getDefaultHistory();
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager(historyManager);
        inMemoryTaskManager.createTask("первая", "ывпа", 50, LocalDateTime.of(2022, 10, 5, 12, 5));
        inMemoryTaskManager.createTask("вторая", "выап", 50, LocalDateTime.of(2021, 10, 5, 12, 56));
        inMemoryTaskManager.createTask("третий", "ывапр", 50, LocalDateTime.of(2020, 10, 5, 12, 5));

        Task task1 = inMemoryTaskManager.getTask(1);
        Task task2 = inMemoryTaskManager.getTask(2);
        Task task3 = inMemoryTaskManager.getTask(3);

        List<Task> expected = List.of(task1, task2, task3);
        List<Task> actual = historyManager.getHistory();
        assertEquals(expected, actual);

        Task removeTask = expected.get(0);
        Task averageTask = historyManager.getHistory().get(0);
        assertEquals(removeTask, averageTask);

        int size = historyManager.getHistory().size();
        assertEquals(size, historyManager.getHistory().size());

        inMemoryTaskManager.deleteTask(1);

        expected = List.of(task2, task3);
        actual = historyManager.getHistory();
        assertEquals(expected, actual);

        size = historyManager.getHistory().size();
        assertEquals(size, historyManager.getHistory().size());

        averageTask = historyManager.getHistory().get(0);
        assertNotEquals(removeTask, averageTask);
    }

    @Test
    void checkHistoryManagerRemoveLastTaskGetHistoryTest() throws IllegalAccessException {
        HistoryManager historyManager = Managers.getDefaultHistory();
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager(historyManager);
        inMemoryTaskManager.createTask("первая", "ывпа", 50, LocalDateTime.of(2022, 10, 5, 12, 5));
        inMemoryTaskManager.createTask("вторая", "выап", 50, LocalDateTime.of(2021, 10, 5, 12, 56));
        inMemoryTaskManager.createTask("третий", "ывапр", 50, LocalDateTime.of(2020, 10, 5, 12, 5));

        Task task1 = inMemoryTaskManager.getTask(1);
        Task task2 = inMemoryTaskManager.getTask(2);
        Task task3 = inMemoryTaskManager.getTask(3);

        List<Task> expected = List.of(task1, task2, task3);
        List<Task> actual = historyManager.getHistory();
        assertEquals(expected, actual);

        Task removeTask = expected.get(2);
        Task averageTask = historyManager.getHistory().get(2);
        assertEquals(removeTask, averageTask);

        int size = historyManager.getHistory().size();
        assertEquals(size, historyManager.getHistory().size());

        inMemoryTaskManager.deleteTask(3);

        expected = List.of(task1, task2);
        actual = historyManager.getHistory();
        assertEquals(expected, actual);

        size = historyManager.getHistory().size();
        assertEquals(size, historyManager.getHistory().size());

        averageTask = historyManager.getHistory().get(historyManager.getHistory().size() - 1);
        assertNotEquals(removeTask, averageTask);
    }

}