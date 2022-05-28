package model;

import controllers.InMemoryTaskManager;
import controllers.Managers;
import controllers.Status;
import controllers.TaskManager;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    @Test
    public void checkStatusEpicWithoutSubtaskTest() {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        inMemoryTaskManager.createEpic("first", "create first test", 50, LocalDateTime.of(2020, 10, 5, 12, 5));
        Epic epic = (Epic) inMemoryTaskManager.getEpics().get(1);
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void checkStatusEpicTestWithSubtaskNewValidIdTest() throws IllegalAccessException {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        inMemoryTaskManager.createEpic("first", "create first test", 50, LocalDateTime.of(2020, 10, 5, 12, 5));
        inMemoryTaskManager.createSubtask(1, "четвертая", "ывп", 50, LocalDateTime.of(2021, 10, 5, 12, 5));
        inMemoryTaskManager.createSubtask(1, "пятая", "ывп", 50, LocalDateTime.of(2022, 10, 5, 12, 5));
        Epic epic = (Epic) inMemoryTaskManager.getEpics().get(1);
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void checkStatusEpicTestWithSubtaskInProgressValidIdTest() throws IOException, IllegalAccessException {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        inMemoryTaskManager.createEpic("first", "create first test", 50, LocalDateTime.of(2020, 10, 5, 12, 5));
        inMemoryTaskManager.createSubtask(1, "четвертая", "ывп", 50, LocalDateTime.of(2021, 10, 5, 12, 5));
        inMemoryTaskManager.createSubtask(1, "пятая", "ывп", 50, LocalDateTime.of(2022, 10, 5, 12, 5));
        inMemoryTaskManager.getSubtask(2).setStatus(Status.IN_PROGRESS);
        inMemoryTaskManager.getSubtask(3).setStatus(Status.IN_PROGRESS);
        Subtask subtask = inMemoryTaskManager.getSubtask(2);
        inMemoryTaskManager.updateSubtask(subtask);
        subtask = inMemoryTaskManager.getSubtask(3);
        inMemoryTaskManager.updateSubtask(subtask);
        Epic epic = (Epic) inMemoryTaskManager.getEpics().get(1);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void checkStatusEpicTestWithSubtaskNewAndDoneValidIdTest() throws IOException, IllegalAccessException {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        inMemoryTaskManager.createEpic("first", "create first test", 50, LocalDateTime.of(2020, 10, 5, 12, 5));
        inMemoryTaskManager.createSubtask(1, "четвертая", "ывп", 50, LocalDateTime.of(2021, 10, 5, 12, 5));
        inMemoryTaskManager.createSubtask(1, "пятая", "ывп", 50, LocalDateTime.of(2022, 10, 5, 12, 5));
        inMemoryTaskManager.getSubtask(2).setStatus(Status.DONE);
        Subtask subtask = inMemoryTaskManager.getSubtask(2);
        inMemoryTaskManager.updateSubtask(subtask);
        Epic epic = (Epic) inMemoryTaskManager.getEpics().get(1);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void checkStatusEpicWithSubtaskDoneValidIdTest() throws IOException, IllegalAccessException {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        inMemoryTaskManager.createEpic("first", "create first test", 50, LocalDateTime.of(2020, 10, 5, 12, 5));
        inMemoryTaskManager.createSubtask(1, "четвертая", "ывп", 50, LocalDateTime.of(2021, 10, 5, 12, 5));
        inMemoryTaskManager.createSubtask(1, "пятая", "ывп", 50, LocalDateTime.of(2022, 10, 5, 12, 5));
        inMemoryTaskManager.getSubtask(2).setStatus(Status.DONE);
        inMemoryTaskManager.getSubtask(3).setStatus(Status.DONE);
        Subtask subtask = inMemoryTaskManager.getSubtask(2);
        inMemoryTaskManager.updateSubtask(subtask);
        subtask = inMemoryTaskManager.getSubtask(3);
        inMemoryTaskManager.updateSubtask(subtask);
        Epic epic = (Epic) inMemoryTaskManager.getEpics().get(1);
        assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    public void checkToStringEpicTestWithSubtask() throws IllegalAccessException, IOException {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        inMemoryTaskManager.createEpic("first", "create first test", 50, LocalDateTime.of(2022, 10, 5, 12, 5));
        inMemoryTaskManager.createSubtask(1, "четвертая", "ывп", 50, LocalDateTime.of(2023, 10, 5, 12, 5));
        inMemoryTaskManager.createSubtask(1, "пятая", "ывп", 50, LocalDateTime.of(2024, 10, 5, 12, 5));
        String expected = "model.Epic{" +
                "typeTask=" + inMemoryTaskManager.getEpic(1).getTypeTask() +
                ", name='" + inMemoryTaskManager.getEpic(1).getName() +
                ", description='" + inMemoryTaskManager.getEpic(1).getDescription() +
                ", identificationNumber=" + inMemoryTaskManager.getEpic(1).getIdentificationNumber() +
                ", status=" + inMemoryTaskManager.getEpic(1).getStatus() +
                ", subtasksEpic=" + inMemoryTaskManager.getEpic(1).getSubtasksEpic() +
                ", duration=" + inMemoryTaskManager.getEpic(1).getDuration() +
                ", startTime=" + inMemoryTaskManager.getEpic(1).getStartTime() +
                '}';

        String line = inMemoryTaskManager.getEpic(1).toString();

        assertEquals(expected, line);
    }

    @Test
    public void checkToStringEpicTestNoSubtask() throws IOException {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        inMemoryTaskManager.createEpic("first", "create first test", 50, LocalDateTime.of(2022, 10, 5, 12, 5));
        String expected = "model.Epic{" +
                "typeTask=" + inMemoryTaskManager.getEpic(1).getTypeTask() +
                ", name='" + inMemoryTaskManager.getEpic(1).getName() +
                ", description='" + inMemoryTaskManager.getEpic(1).getDescription() +
                ", identificationNumber=" + inMemoryTaskManager.getEpic(1).getIdentificationNumber() +
                ", status=" + inMemoryTaskManager.getEpic(1).getStatus() +
                ", subtasksEpic=" + inMemoryTaskManager.getEpic(1).getSubtasksEpic() +
                ", duration=" + inMemoryTaskManager.getEpic(1).getDuration() +
                ", startTime=" + inMemoryTaskManager.getEpic(1).getStartTime() +
                '}';

        String line = inMemoryTaskManager.getEpic(1).toString();

        assertEquals(expected, line);
    }

    @Test
    public void checkDurationEpicWithoutSubtaskTest() throws IOException {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        long expectedDuration = 50;
        inMemoryTaskManager.createEpic("first", "create first test", expectedDuration, LocalDateTime.of(2022, 10, 5, 12, 5));
        long duration = inMemoryTaskManager.getEpic(1).getDuration();

        assertEquals(expectedDuration, duration);
    }

    @Test
    public void checkDurationEpicWithSubtaskTest() throws IOException, IllegalAccessException {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        inMemoryTaskManager.createEpic("first", "create first test", 10, LocalDateTime.of(2021, 10, 5, 12, 5));
        inMemoryTaskManager.createSubtask(1, "четвертая", "ывп", 15, LocalDateTime.of(2022, 10, 5, 12, 5));
        inMemoryTaskManager.createSubtask(1, "пятая", "ывп", 15, LocalDateTime.of(2022, 10, 5, 12, 21));
        long expectedDuration = inMemoryTaskManager.getSubtask(2).getDuration() + inMemoryTaskManager.getSubtask(3).getDuration();

        long duration = inMemoryTaskManager.getEpic(1).getDuration();

        assertEquals(expectedDuration, duration);
    }

    @Test
    public void checkStartTimeEpicWithSubtaskTest() throws IOException, IllegalAccessException {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        LocalDateTime expectedStartTime = LocalDateTime.of(2022, 10, 5, 12, 5);
        inMemoryTaskManager.createEpic("first", "create first test", 10, LocalDateTime.of(2021, 10, 5, 12, 5));
        inMemoryTaskManager.createSubtask(1, "четвертая", "ывп", 15, expectedStartTime);
        inMemoryTaskManager.createSubtask(1, "пятая", "ывп", 15, LocalDateTime.of(2022, 10, 5, 12, 21));

        LocalDateTime startTime = inMemoryTaskManager.getEpic(1).getStartTime();

        assertEquals(expectedStartTime, startTime);
    }

    @Test
    public void checkStartTimeEpicWithoutSubtaskTest() throws IOException {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        LocalDateTime expectedStartTime = LocalDateTime.of(2021, 10, 5, 12, 5);
        inMemoryTaskManager.createEpic("first", "create first test", 10, expectedStartTime);

        LocalDateTime startTime = inMemoryTaskManager.getEpic(1).getStartTime();

        assertEquals(expectedStartTime, startTime);
    }

    @Test
    public void checkEndTimeEpicWithoutSubtaskTest() throws IOException {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        long duration = 50;
        LocalDateTime startTime = LocalDateTime.of(2021, 10, 5, 12, 5);
        LocalDateTime expectedEndTime = startTime.plusMinutes(duration);
        inMemoryTaskManager.createEpic("first", "create first test", duration, startTime);

        LocalDateTime endTime = inMemoryTaskManager.getEpic(1).getEndTime();

        assertEquals(expectedEndTime, endTime);
    }

    @Test
    public void checkEndTimeEpicWithSubtaskTest() throws IOException, IllegalAccessException {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        long duration = 50;
        LocalDateTime startTime = LocalDateTime.of(2022, 10, 5, 12, 5);
        LocalDateTime expectedEndTime = LocalDateTime.of(2022, 10, 5, 12, 21).plusMinutes(duration);
        inMemoryTaskManager.createEpic("first", "create first test", 10, LocalDateTime.of(2021, 10, 5, 12, 5));
        inMemoryTaskManager.createSubtask(1, "четвертая", "ывп", 15, startTime);
        inMemoryTaskManager.createSubtask(1, "пятая", "ывп", duration, LocalDateTime.of(2022, 10, 5, 12, 21));
        LocalDateTime endTime = inMemoryTaskManager.getEpic(1).getEndTime();

        assertEquals(expectedEndTime, endTime);
    }

}