package model;

import controllers.InMemoryTaskManager;
import controllers.Managers;
import controllers.TaskManager;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    @Test
    public void checkToStringTaskTest() throws IllegalAccessException, IOException {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        inMemoryTaskManager.createTask("первая", "ывпа", 50, LocalDateTime.of(2022, 10, 5, 12, 5));

        String expected = "Task{" +
                "typeTask=" + inMemoryTaskManager.getTask(1).getTypeTask() +
                ", name='" + inMemoryTaskManager.getTask(1).getName() +
                ", description='" + inMemoryTaskManager.getTask(1).getDescription() +
                ", identificationNumber=" + inMemoryTaskManager.getTask(1).getIdentificationNumber() +
                ", status=" + inMemoryTaskManager.getTask(1).getStatus() +
                ", duration=" + inMemoryTaskManager.getTask(1).getDuration() +
                ", startTime=" + inMemoryTaskManager.getTask(1).getStartTime() +
                '}';
        String line = inMemoryTaskManager.getTask(1).toString();
        assertEquals(expected, line);
    }

    @Test
    public void checkEndTimeTaskTest() throws IllegalAccessException, IOException {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        inMemoryTaskManager.createTask("первая", "ывпа", 50, LocalDateTime.of(2022, 10, 5, 12, 5));
        LocalDateTime localEndTime = inMemoryTaskManager.getTask(1).getEndTime();
        LocalDateTime endTime = inMemoryTaskManager.getTask(1).getStartTime().plusMinutes(inMemoryTaskManager.getTask(1).getDuration());
        assertEquals(endTime, localEndTime);
    }

}