package model;

import controllers.*;
import controllers.enumeratedtype.Status;
import controllers.generallogicfortasks.Managers;
import controllers.generallogicfortasks.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    TaskManager taskManager;
    Epic epic;
    Subtask subtask1;
    Subtask subtask2;

    @BeforeEach
    void init() {
        epic = new Epic("first", "create first test", 50, Status.NEW, 50, LocalDateTime.of(2022, 11, 5, 12, 5));
        subtask1 = new Subtask("четвертая", "ывп", 50, Status.NEW, 1, 50, LocalDateTime.of(2021, 10, 5, 12, 5));
        subtask2 = new Subtask("пятая", "ывп", 50, Status.NEW, 1, 50, LocalDateTime.of(2022, 10, 5, 12, 56));
        taskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
    }

    @Test
    public void checkStatusEpicWithoutSubtaskTest() {
        taskManager.createEpic(epic);
        Epic epic = taskManager.getEpics().get(0);

        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void checkStatusEpicTestWithSubtaskNewValidIdTest() {
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        Epic epic = taskManager.getEpics().get(0);

        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void checkStatusEpicTestWithSubtaskInProgressValidIdTest() {
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.getSubtask(2).setStatus(Status.IN_PROGRESS);
        taskManager.getSubtask(3).setStatus(Status.IN_PROGRESS);
        Subtask subtask = taskManager.getSubtask(2);
        taskManager.updateSubtask(subtask);
        subtask = taskManager.getSubtask(3);
        taskManager.updateSubtask(subtask);
        Epic epic = taskManager.getEpics().get(0);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void checkStatusEpicTestWithSubtaskNewAndDoneValidIdTest() {
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.getSubtask(2).setStatus(Status.DONE);
        Subtask subtask = taskManager.getSubtask(2);
        taskManager.updateSubtask(subtask);
        Epic epic = taskManager.getEpics().get(0);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void checkStatusEpicWithSubtaskDoneValidIdTest() {
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.getSubtask(2).setStatus(Status.DONE);
        taskManager.getSubtask(3).setStatus(Status.DONE);
        Subtask subtask = taskManager.getSubtask(2);
        taskManager.updateSubtask(subtask);
        subtask = taskManager.getSubtask(3);
        taskManager.updateSubtask(subtask);
        Epic epic = taskManager.getEpics().get(0);
        assertEquals(Status.DONE, epic.getStatus());
    }


    @Test
    public void checkDurationEpicWithoutSubtaskTest() {
        long expectedDuration = 50;
        Epic epic1 = new Epic("firs", "create first test", 50, Status.NEW, expectedDuration, LocalDateTime.of(2022, 11, 5, 12, 5));
        taskManager.createEpic(epic1);
        long duration = taskManager.getEpic(1).getDuration();

        assertEquals(expectedDuration, duration);
    }

    @Test
    public void checkDurationEpicWithSubtaskTest() {
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        long expectedDuration = taskManager.getSubtask(2).getDuration() + taskManager.getSubtask(3).getDuration();

        long duration = taskManager.getEpic(1).getDuration();

        assertEquals(expectedDuration, duration);
    }

    @Test
    public void checkStartTimeEpicWithSubtaskTest() {
        LocalDateTime expectedStartTime = LocalDateTime.of(2022, 10, 5, 12, 5);
        Subtask subtask = new Subtask("пятая", "ывп", 50, Status.NEW, 1, 50, expectedStartTime);

        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);
        taskManager.createSubtask(subtask2);
        LocalDateTime startTime = taskManager.getEpic(1).getStartTime();

        assertEquals(expectedStartTime, startTime);
    }

    @Test
    public void checkStartTimeEpicWithoutSubtaskTest() {
        LocalDateTime expectedStartTime = LocalDateTime.of(2021, 10, 5, 12, 5);
        Epic epic1 = new Epic("firs", "create first test", 50, Status.NEW, 50, expectedStartTime);
        taskManager.createEpic(epic1);
        LocalDateTime startTime = taskManager.getEpic(1).getStartTime();

        assertEquals(expectedStartTime, startTime);
    }

    @Test
    public void checkEndTimeEpicWithoutSubtaskTest() {
        long duration = 50;
        LocalDateTime startTime = LocalDateTime.of(2021, 10, 5, 12, 5);
        LocalDateTime expectedEndTime = startTime.plusMinutes(duration);
        Epic epic1 = new Epic("firs", "create first test", 22, Status.NEW, duration, startTime);
        taskManager.createEpic(epic1);

        LocalDateTime endTime = taskManager.getEpic(1).getEndTime();

        assertEquals(expectedEndTime, endTime);
    }

    @Test
    public void checkEndTimeEpicWithSubtaskTest() {
        long duration = 10;
        LocalDateTime startTime = LocalDateTime.of(2022, 10, 5, 12, 5);
        LocalDateTime expectedEndTime = LocalDateTime.of(2022, 10, 5, 12, 21).plusMinutes(duration);
        subtask1 = new Subtask("четвертая", "ывп", 5, Status.NEW, 1, 5, startTime);
        subtask2 = new Subtask("пятая", "ывп", 50, Status.NEW, 1, duration, LocalDateTime.of(2022, 10, 5, 12, 21));
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        LocalDateTime endTime = taskManager.getEpic(1).getEndTime();

        assertEquals(expectedEndTime, endTime);
    }

}