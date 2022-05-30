package model;

import controllers.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    @Test
    public void checkEndTimeTaskTest() {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        Task task = new Task("первая", "ывпа", 20, Status.NEW, TypeTask.TASK, 50, LocalDateTime.of(2022, 10, 5, 12, 5));
        inMemoryTaskManager.createTask(task);
        LocalDateTime localEndTime = inMemoryTaskManager.getTask(1).getEndTime();
        LocalDateTime endTime = inMemoryTaskManager.getTask(1).getStartTime().plusMinutes(inMemoryTaskManager.getTask(1).getDuration());
        assertEquals(endTime, localEndTime);
    }

}