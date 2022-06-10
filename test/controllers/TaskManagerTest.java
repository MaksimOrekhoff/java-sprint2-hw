package controllers;

import controllers.enumeratedtype.Status;
import controllers.enumeratedtype.TypeTask;
import controllers.generallogicfortasks.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    T manager;
    Task task1;
    Task task2;
    Epic epic1;
    Epic epic2;
    Subtask subtask1;
    Subtask subtask2;


    protected void init() {
        task1 = new Task("первая", "ывпа", 20, Status.NEW, TypeTask.TASK, 50, LocalDateTime.of(2022, 6, 1, 1, 5));
        task2 = new Task("вторая", "выап", 50, Status.NEW, TypeTask.TASK, 50, LocalDateTime.of(2022, 6, 1, 2, 5));
        epic1 = new Epic("first", "create first test", 50, Status.NEW, 50, LocalDateTime.of(2022, 6, 1, 3, 5));
        epic2 = new Epic("седьмой", "фвап", 50, Status.NEW, 50, LocalDateTime.of(2022, 6, 1, 4, 5));
        subtask1 = new Subtask("четвертая", "ывп", 50, Status.NEW, 3, 50, LocalDateTime.of(2022, 6, 1, 5, 5));
        subtask2 = new Subtask("пятая", "ывп", 50, Status.NEW, 3, 50, LocalDateTime.of(2022, 6, 1, 6, 5));
        manager.createTask(task1);
        manager.createTask(task2);
        manager.createEpic(epic1);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        manager.createEpic(epic2);
    }

    @Test
    public void checkGetHistoryAtGetTaskTest() {
        Task task = manager.getTask(1);
        Epic epic = manager.getEpic(3);
        Subtask subtask = manager.getSubtask(5);
        List<Task> expected = List.of(task, epic, subtask);
        List<Task> actual = manager.getHistoryManagers();
        assertEquals(expected, actual, "Список задач совпадает");
        assertEquals(expected.size(), actual.size());
    }

    @Test
    public void checkGetHistoryAtDeleteTaskTest() {
        Task task = manager.getTask(1);
        Epic epic = manager.getEpic(3);
        Subtask subtask = manager.getSubtask(5);
        List<Task> expected = List.of(task, epic, subtask);
        int size = manager.getHistoryManagers().size();
        List<Task> actual = manager.getHistoryManagers();

        assertEquals(expected, actual, "Список задач совпадает");
        assertEquals(expected.size(), actual.size(), "размер совпадает");

        manager.deleteTask(1);
        actual = manager.getHistoryManagers();
        assertNotEquals(expected, actual, "список не совпадает");
        assertNotEquals(size, manager.getHistoryManagers().size());
    }

    @Test
    public void checkGetPrioritizedTasksAtAddTaskTest() {
        List<Task> expected = List.of(manager.getTask(1), manager.getTask(2), manager.getSubtask(4), manager.getSubtask(5));
        List<Task> actual = new ArrayList<>(manager.getPrioritizedTasks());

        assertEquals(expected, actual, "Список задач совпадает");
        assertEquals(expected.size(), actual.size(), "размер совпадает");
    }

    @Test
    public void checkGetPrioritizedTasksAtDeleteTaskTest() {
        manager.deleteTask(1);
        List<Task> expected = List.of(manager.getTask(2), manager.getSubtask(4), manager.getSubtask(5));
        List<Task> actual = new ArrayList<>(manager.getPrioritizedTasks());

        assertEquals(expected, actual, "Список задач совпадает");
        assertEquals(expected.size(), actual.size(), "размер совпадает");
    }

    @Test
    public void checkGetPrioritizedTasksAtUpdateStartTimeTaskTest() {
        Task updateTask = new Task("первая", "ывпа", 1, Status.NEW, TypeTask.TASK, 50, LocalDateTime.of(2022, 6, 1, 7, 5));
        manager.updateTask(updateTask);

        List<Task> expected = List.of(manager.getTask(2), manager.getSubtask(4), manager.getSubtask(5), manager.getTask(1));
        List<Task> actual = new ArrayList<>(manager.getPrioritizedTasks());

        assertEquals(expected, actual, "Список задач совпадает");
        assertEquals(expected.size(), actual.size(), "размер совпадает");
    }

    @Test
    public void checkClearTaskTest() {
        manager.clearTask();
        assertEquals(0, manager.getTasks().size(), "Список задач пуст");
    }

    @Test
    public void checkClearEpicTest() {
        manager.clearEpic();
        assertEquals(0, manager.getEpics().size(), "Список эпиков пуст");
    }

    @Test
    public void checkEpicForClearSubtaskTest() {
        assertNotEquals(0, manager.getSubtasks().size());
        manager.clearSubtask();
        Epic epic = manager.getEpic(3);
        assertEquals(0, manager.getSubtasks().size());
        assertTrue(epic.getSubtasksEpic().isEmpty());
    }

    @Test
    public void checkClearSubtaskTest() {
        assertTrue(manager.getSubtasks().size() != 0);
        manager.clearSubtask();
        assertTrue(manager.getSubtasks().isEmpty());
    }

    @Test
    public void getAnExistingTaskTest() {
        Task task = manager.getTasks().get(0);
        assertEquals(task, manager.getTask(1));
    }

    @Test
    public void getAnNotExistingTaskTest() {
        manager.clearTask();
        Task task = manager.getTask(4);
        assertNull(task);
    }

    @Test
    public void getAnExistingSubtaskTest() {

        Subtask subtask = manager.getSubtask(2);
        assertEquals(epic1.getSubtasksEpic().get(0), subtask);
    }

    @Test
    public void getAnNotExistingSubtaskTest() {
        assertFalse(manager.getSubtasks().isEmpty());
        Subtask subtask = manager.getSubtask(6);
        assertNull(subtask);
    }

    @Test
    public void getAnExistingEpicTest() {
        Epic epic = manager.getEpic(3);
        assertEquals(epic, manager.getEpics().get(0));
    }

    @Test
    public void getAnNotExistingEpicTest() {
        assertFalse(manager.getEpics().isEmpty());
        Epic epic = manager.getEpic(1);
        assertNull(epic);
    }

    @Test
    public void checkCreationTaskTest() {
        int size = manager.getTasks().size();
        Task task3 = new Task("7", "createTask", 54, Status.NEW, TypeTask.TASK, 50, LocalDateTime.now());
        manager.createTask(task3);
        assertNotEquals(size, manager.getTasks().size());
    }


    @Test
    public void checkCreationSubtaskTest() {
        int size = manager.getSubtasks().size();
        Subtask subtask3 = new Subtask("7", "createTask", 54, Status.NEW, 3, 50, LocalDateTime.now());
        manager.createSubtask(subtask3);
        assertNotEquals(size, manager.getSubtasks().size());
    }

    @Test
    public void checkCreationEpicWithoutSubtaskTest() {
        int size = manager.getEpics().size();
        Epic epic3 = new Epic("седьмой", "фвап", 50, Status.NEW, 50, LocalDateTime.now());
        manager.createEpic(epic3);
        assertNotEquals(size, manager.getEpics().size());
        assertTrue(epic3.getSubtasksEpic().isEmpty());
    }


    @Test
    public void checkCreationEpicWithSubtaskTest() {
        int size = manager.getEpics().size();
        Epic epic3 = new Epic("седьмой", "фвап", 50, Status.NEW, 50, LocalDateTime.now());
        manager.createEpic(epic3);
        Subtask subtask3 = new Subtask("7", "createTask", 54, Status.NEW, 3, 50, LocalDateTime.now());
        manager.createSubtask(subtask3);

        assertNotEquals(size, manager.getEpics().size());
        assertTrue(manager.getEpics().get(2).getSubtasksEpic().isEmpty());
    }

    @Test
    public void checkDeleteExistingTaskTest() {
        int size = manager.getTasks().size();
        Task expected = manager.getTask(1);
        manager.deleteTask(1);
        Task actual = manager.getTask(1);
        assertNotEquals(expected, actual);
        assertNotEquals(size, manager.getTasks().size());
    }

    @Test
    public void checkDeleteNotExistingTaskTest() {
        int size = manager.getTasks().size();

        manager.deleteTask(4);
        assertEquals(size, manager.getTasks().size());
    }

    @Test
    public void checkDeleteEpicValidIdTest() {
        int index = manager.getEpics().get(0).getIdentificationNumber();
        int size = manager.getEpics().size();
        Epic epic = manager.getEpic(index);
        manager.deleteEpic(index);

        assertNotEquals(epic, manager.getEpic(index));
        assertNotEquals(size, manager.getEpics().size());
    }

    @Test
    public void checkDeleteEpicInvalidIdTest() {
        int index = manager.getEpics().get(1).getIdentificationNumber();
        int size = manager.getEpics().size();
        Epic epic = manager.getEpic(index);
        manager.deleteEpic(2);

        assertEquals(epic, manager.getEpic(index));
        assertEquals(size, manager.getEpics().size());
    }

    @Test
    public void checkDeleteExistingSubtaskTest() {
        int index = manager.getSubtasks().get(1).getIdentificationNumber();
        int size = manager.getSubtasks().size();
        Subtask subtask = manager.getSubtask(index);
        manager.deleteSubtask(5);

        assertNotEquals(subtask, manager.getSubtask(index));
        assertNotEquals(size, manager.getSubtasks().size());
    }

    @Test
    public void checkDeleteNotExistingSubtaskTest() {
        int index = manager.getSubtasks().get(1).getIdentificationNumber();
        int size = manager.getSubtasks().size();
        Subtask subtask = manager.getSubtask(index);
        manager.deleteSubtask(1);

        assertEquals(subtask, manager.getSubtask(index));
        assertEquals(size, manager.getSubtasks().size());
    }

    @Test
    public void checkUpdateTaskTest() {
        Task task = manager.getTasks().get(1);
        String expected = task.getName();
        task.setName("Update");
        manager.updateTask(task);
        assertNotEquals(expected, task.getName());
        assertEquals(task, manager.getTask(2));
    }

    @Test
    public void checkUpdateSubtaskTest() {
        Enum expected = manager.getSubtask(5).getStatus();
        Subtask actual = manager.getSubtasks().get(0);
        actual.setStatus(Status.DONE);
        manager.updateSubtask(actual);
        assertNotEquals(expected, actual.getStatus());
    }

    @Test
    public void checkUpdateEpicInMapTest() {
        String expected = manager.getEpic(3).getName();
        Epic actual = manager.getEpic(3);
        actual.setName("update");
        manager.updateEpic(actual);
        assertNotEquals(expected, actual.getName());
    }


    @Test
    void checkGetEpicsMapTest() {
        Epic expected = manager.getEpics().get(0);
        Epic actual = manager.getEpic(3);

        assertEquals(expected, actual);
    }

    @Test
    public void checkExistingEpicInMapTest() {

        assertTrue(manager.checkEpic(3));
    }

    @Test
    public void checkNotExistingEpicInMapTest() {
        assertFalse(manager.checkEpic(4));
    }

    @Test
    void checkGetSubtasksMapTest() {
        Subtask expected = manager.getSubtask(5);
        Subtask actual = manager.getSubtasks().get(1);
        assertEquals(expected, actual);
    }

    @Test
    void checkGetTasksMapTest() {
        Task expected = manager.getTask(1);
        Task actual = manager.getTasks().get(0);
        assertEquals(expected, actual);
    }

    @Test
    public void checkTasksIntersectionTest() {
        LocalDateTime start1 = LocalDateTime.of(2022, 10, 5, 12, 0);
        LocalDateTime start2 = LocalDateTime.of(2022, 10, 5, 12, 5);
        LocalDateTime end1 = start1.plusMinutes(5);
        LocalDateTime end2 = start2.plusMinutes(5);
        boolean intersection = manager.conditionIntersection(start1, end1, start2, end2);
        assertTrue(intersection); //end1 = start2

        start2 = LocalDateTime.of(2022, 10, 5, 12, 4);
        intersection = manager.conditionIntersection(start1, end1, start2, end2);
        assertTrue(intersection); //start1 < start2 < end2 < end1

        start2 = start1;
        assertTrue(manager.conditionIntersection(start1, end1, start2, end2));

        end1 = end2;
        assertTrue(manager.conditionIntersection(start1, end1, start2, end2));

        end2 = start2;
        assertTrue(manager.conditionIntersection(start1, end1, start2, end2));

        start1 = LocalDateTime.of(2022, 10, 5, 12, 2);
        start2 = LocalDateTime.of(2022, 10, 5, 12, 0);
        end2 = start2.plusMinutes(10);
        end1 = start1.plusMinutes(3);
        assertTrue(manager.conditionIntersection(start1, end1, start2, end2));

        start2 = LocalDateTime.of(2022, 10, 5, 12, 0).plusMinutes(7);
        end2 = start2.plusMinutes(1);
        assertFalse(manager.conditionIntersection(start1, end1, start2, end2));
    }


}