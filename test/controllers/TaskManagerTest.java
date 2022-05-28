package controllers;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTest {

    @Test
    public void checkClearTaskTest() throws IllegalAccessException {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        int expected = inMemoryTaskManager.getTasks().size();
        inMemoryTaskManager.createTask("первая", "ывпа", 50, LocalDateTime.of(2022, 10, 5, 12, 5));
        int newSize = inMemoryTaskManager.getTasks().size();
        assertNotEquals(expected, newSize);
        inMemoryTaskManager.clearTask();
        assertEquals(expected, inMemoryTaskManager.getTasks().size());
    }

    @Test
    public void checkClearEpicTest() {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        int size = inMemoryTaskManager.getEpics().size();
        inMemoryTaskManager.createEpic("first", "create first test", 50, LocalDateTime.of(2022, 10, 5, 12, 5));

        assertNotEquals(size, inMemoryTaskManager.getEpics().size());
        inMemoryTaskManager.clearEpic();
        assertTrue(inMemoryTaskManager.getEpics().isEmpty());
    }

    @Test
    public void checkEpicForClearSubtaskTest() throws IOException, IllegalAccessException {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        inMemoryTaskManager.createEpic("first", "create first test", 50, LocalDateTime.of(2020, 10, 5, 12, 5));
        int sizeSubtasks = inMemoryTaskManager.getSubtasks().size();
        inMemoryTaskManager.createSubtask(1, "четвертая", "ывп", 50, LocalDateTime.of(2021, 10, 5, 12, 5));
        inMemoryTaskManager.createSubtask(1, "пятая", "ывп", 50, LocalDateTime.of(2022, 10, 5, 12, 5));

        assertNotEquals(sizeSubtasks, inMemoryTaskManager.getSubtasks().size());
        inMemoryTaskManager.clearSubtask();
        Epic epic = inMemoryTaskManager.getEpic(1);
        assertTrue(epic.getSubtasksEpic().isEmpty());
    }

    @Test
    public void checkClearSubtaskTest() throws IllegalAccessException {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        inMemoryTaskManager.createEpic("first", "create first test", 50, LocalDateTime.of(2020, 10, 5, 12, 5));
        inMemoryTaskManager.createSubtask(1, "четвертая", "ывп", 50, LocalDateTime.of(2021, 10, 5, 12, 5));
        inMemoryTaskManager.createSubtask(1, "пятая", "ывп", 50, LocalDateTime.of(2022, 10, 5, 12, 5));
        assertTrue(inMemoryTaskManager.getSubtasks().size() != 0);
        inMemoryTaskManager.clearSubtask();
        assertTrue(inMemoryTaskManager.getSubtasks().isEmpty());
    }

    @Test
    public void getAnExistingTaskTest() throws IllegalAccessException, IOException {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        inMemoryTaskManager.createTask("первая", "ывпа", 50, LocalDateTime.of(2022, 10, 5, 12, 5));
        Task task = (Task) inMemoryTaskManager.getTasks().get(1);
        assertEquals(task, inMemoryTaskManager.getTask(1));
    }

    @Test
    public void getAnNotExistingTaskTest() {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        assertTrue(inMemoryTaskManager.getTasks().isEmpty());
        Task task = (Task) inMemoryTaskManager.getTasks().get(1);
        assertNull(task);
    }

    @Test
    public void getAnExistingSubtaskTest() throws IllegalAccessException, IOException {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        inMemoryTaskManager.createEpic("first", "create first test", 50, LocalDateTime.of(2021, 10, 5, 12, 5));
        inMemoryTaskManager.createSubtask(1, "четвертая", "ывп", 50, LocalDateTime.of(2022, 10, 5, 12, 5));
        Subtask subtask = (Subtask) inMemoryTaskManager.getSubtasks().get(2);
        assertEquals(subtask, inMemoryTaskManager.getSubtask(2));
    }

    @Test
    public void getAnNotExistingSubtaskTest() {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        assertTrue(inMemoryTaskManager.getSubtasks().isEmpty());
        Subtask subtask = (Subtask) inMemoryTaskManager.getSubtasks().get(1);
        assertNull(subtask);
    }

    @Test
    public void getAnExistingEpicTest() throws IOException {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        inMemoryTaskManager.createEpic("first", "create first test", 50, LocalDateTime.of(2022, 10, 5, 12, 5));
        Epic epic = inMemoryTaskManager.getEpic(1);
        assertEquals(epic, inMemoryTaskManager.getEpics().get(1));
    }

    @Test
    public void getAnNotExistingEpicTest() throws IOException {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        assertTrue(inMemoryTaskManager.getEpics().isEmpty());
        Epic epic = inMemoryTaskManager.getEpic(1);
        assertNull(epic);
    }


    @Test
    public void checkCreationTaskTest() throws IllegalAccessException {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        int size = inMemoryTaskManager.getTasks().size();
        assertTrue(inMemoryTaskManager.getTasks().isEmpty());
        inMemoryTaskManager.createTask("первая", "ывпа", 50, LocalDateTime.of(2022, 10, 5, 12, 5));
        assertNotEquals(size, inMemoryTaskManager.getTasks().size());
        assertNotNull(inMemoryTaskManager.getTasks());
    }


    @Test
    public void checkCreationSubtaskTest() throws IllegalAccessException {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        inMemoryTaskManager.createEpic("first", "create first test", 50, LocalDateTime.of(2022, 10, 5, 12, 5));
        int size = inMemoryTaskManager.getSubtasks().size();
        inMemoryTaskManager.createSubtask(1, "четвертая", "ывп", 50, LocalDateTime.of(2022, 10, 5, 12, 5));
        assertNotEquals(size, inMemoryTaskManager.getSubtasks().size());
    }

    @Test
    public void checkCreationEpicWithoutSubtaskTest() {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        inMemoryTaskManager.createEpic("first", "create first test", 50, LocalDateTime.of(2022, 10, 5, 12, 5));
        Epic epic = (Epic) inMemoryTaskManager.getEpics().get(1);

        assertEquals(1, inMemoryTaskManager.getEpics().size());
        assertTrue(epic.getSubtasksEpic().isEmpty());
    }

    @Test
    public void checkCreationEpicWithoutSubtaskInvalidIdTest() throws IOException {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        inMemoryTaskManager.createEpic("first", "create first test", 50, LocalDateTime.of(2022, 10, 5, 12, 5));
        Epic epic = inMemoryTaskManager.getEpic(3);

        assertEquals(1, inMemoryTaskManager.getEpics().size());
        assertNull(epic);
    }

    @Test
    public void checkCreationEpicWithSubtaskTest() throws IllegalAccessException {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        inMemoryTaskManager.createEpic("first", "create first test", 50, LocalDateTime.of(2020, 10, 5, 12, 5));
        inMemoryTaskManager.createSubtask(1, "четвертая", "ывп", 50, LocalDateTime.of(2021, 10, 5, 12, 5));
        inMemoryTaskManager.createSubtask(1, "пятая", "ывп", 50, LocalDateTime.of(2022, 10, 5, 12, 5));
        Epic epic = (Epic) inMemoryTaskManager.getEpics().get(1);

        assertEquals(1, inMemoryTaskManager.getEpics().size());
        assertFalse(epic.getSubtasksEpic().isEmpty());
    }

    @Test
    public void checkDeleteExistingTaskTest() throws IOException, IllegalAccessException {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        int size = inMemoryTaskManager.getTasks().size();

        inMemoryTaskManager.createTask("первая", "ывпа", 50, LocalDateTime.of(2022, 10, 5, 12, 5));
        assertNotEquals(size, inMemoryTaskManager.getTasks().size());
        inMemoryTaskManager.deleteTask(1);
        assertEquals(size, inMemoryTaskManager.getSubtasks().size());
    }

    @Test
    public void checkDeleteNotExistingTaskTest() throws IOException, IllegalAccessException {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        int size = inMemoryTaskManager.getTasks().size();

        inMemoryTaskManager.createTask("первая", "ывпа", 50, LocalDateTime.of(2022, 10, 5, 12, 5));
        assertNotEquals(size, inMemoryTaskManager.getTasks().size());
        inMemoryTaskManager.deleteTask(2);
        assertNotEquals(size, inMemoryTaskManager.getTasks().size());
    }

    @Test
    public void checkDeleteEpicValidIdTest() throws IOException {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        int index = 1;
        inMemoryTaskManager.createEpic("first", "create first test", 50, LocalDateTime.of(2022, 10, 5, 12, 5));
        int size = inMemoryTaskManager.getEpics().size();
        Epic epic = inMemoryTaskManager.getEpic(index);
        inMemoryTaskManager.deleteEpic(index);

        assertNotEquals(epic, inMemoryTaskManager.getEpic(index));
        assertNotEquals(size, inMemoryTaskManager.getEpics().size());
    }

    @Test
    public void checkDeleteEpicInvalidIdTest() throws IOException {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        int index = 2;
        inMemoryTaskManager.createEpic("first", "create first test", 50, LocalDateTime.of(2022, 10, 5, 12, 5));
        int size = inMemoryTaskManager.getEpics().size();
        Epic epic = inMemoryTaskManager.getEpic(index);
        inMemoryTaskManager.deleteEpic(2);

        assertEquals(epic, inMemoryTaskManager.getEpic(index));
        assertEquals(size, inMemoryTaskManager.getEpics().size());
    }

    @Test
    public void checkDeleteExistingSubtaskTest() throws IOException, IllegalAccessException {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        int size = inMemoryTaskManager.getSubtasks().size();
        inMemoryTaskManager.createEpic("first", "create first test", 50, LocalDateTime.of(2022, 10, 5, 12, 5));
        inMemoryTaskManager.createSubtask(1, "четвертая", "ывп", 50, LocalDateTime.of(2022, 10, 5, 12, 5));
        assertNotEquals(size, inMemoryTaskManager.getSubtasks().size());
        inMemoryTaskManager.deleteSubtask(2);
        assertEquals(size, inMemoryTaskManager.getSubtasks().size());
    }

    @Test
    public void checkDeleteNotExistingSubtaskTest() throws IOException, IllegalAccessException {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        int size = inMemoryTaskManager.getSubtasks().size();
        inMemoryTaskManager.createEpic("first", "create first test", 50, LocalDateTime.of(2022, 10, 5, 12, 5));
        inMemoryTaskManager.createSubtask(1, "четвертая", "ывп", 50, LocalDateTime.of(2022, 10, 5, 12, 5));
        assertNotEquals(size, inMemoryTaskManager.getSubtasks().size());
        inMemoryTaskManager.deleteSubtask(3);
        assertNotEquals(size, inMemoryTaskManager.getSubtasks().size());
    }

    @Test
    public void checkUpdateTaskTest() throws IllegalAccessException, IOException {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        inMemoryTaskManager.createTask("первая", "ывпа", 50, LocalDateTime.of(2022, 10, 5, 12, 5));
        Task task = (Task) inMemoryTaskManager.getTasks().get(1);
        String expected = task.getName();
        task.setName("Update");
        inMemoryTaskManager.updateTask(task);
        assertNotEquals(expected, task.getName());
    }

    @Test
    public void checkUpdateSubtaskTest() throws IOException, IllegalAccessException {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        inMemoryTaskManager.createEpic("first", "create first test", 50, LocalDateTime.of(2021, 10, 5, 12, 5));
        inMemoryTaskManager.createSubtask(1, "четвертая", "ывп", 50, LocalDateTime.of(2023, 10, 5, 12, 5));
        Enum expected = inMemoryTaskManager.getSubtask(2).getStatus();
        Subtask actual = (Subtask) inMemoryTaskManager.getSubtasks().get(2);
        actual.setStatus(Status.DONE);
        inMemoryTaskManager.updateSubtask(actual);
        assertNotEquals(expected, actual.getStatus());
    }

    @Test
    public void checkUpdateEpicInMapTest() throws IOException {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        inMemoryTaskManager.createEpic("first", "create first test", 50, LocalDateTime.of(2022, 10, 5, 12, 5));
        String expected = inMemoryTaskManager.getEpic(1).getName();
        Epic actual = inMemoryTaskManager.getEpic(1);
        actual.setName("update");
        inMemoryTaskManager.updateEpic(actual);
        assertNotEquals(expected, actual.getName());
    }



    @Test
    void checkGetEpicsMapTest() {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        int size = inMemoryTaskManager.getEpics().size();
        inMemoryTaskManager.createEpic("first", "create first test", 50, LocalDateTime.of(2022, 10, 5, 12, 5));
        assertNotEquals(size, inMemoryTaskManager.getEpics().size());
    }

    @Test
    public void checkExistingEpicInMapTest() {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        inMemoryTaskManager.createEpic("first", "create first test", 50, LocalDateTime.of(2022, 10, 5, 12, 5));
        assertTrue(inMemoryTaskManager.checkEpic(1));
    }

    @Test
    public void checkNotExistingEpicInMapTest() {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        inMemoryTaskManager.createEpic("first", "create first test", 50, LocalDateTime.of(2021, 10, 5, 12, 5));
        inMemoryTaskManager.createEpic("седьмой", "фвап", 50, LocalDateTime.of(2022, 5, 27, 12, 5));
        assertFalse(inMemoryTaskManager.checkEpic(4));
    }

    @Test
    void checkGetSubtasksMapTest() throws IllegalAccessException {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        int size = inMemoryTaskManager.getSubtasks().size();
        inMemoryTaskManager.createEpic("first", "create first test", 50, LocalDateTime.of(2021, 10, 5, 12, 5));
        inMemoryTaskManager.createSubtask(1, "четвертая", "ывп", 50, LocalDateTime.of(2022, 10, 5, 12, 5));
        assertNotEquals(size, inMemoryTaskManager.getSubtasks().size());
    }

    @Test
    void checkGetTasksMapTest() throws IllegalAccessException {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        int size = inMemoryTaskManager.getTasks().size();
        inMemoryTaskManager.createTask("первая", "ывпа", 50, LocalDateTime.of(2022, 10, 5, 12, 5));
        assertNotEquals(size, inMemoryTaskManager.getTasks().size());
    }


}