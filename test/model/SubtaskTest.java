package model;

import controllers.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    @Test
    public void checkAddSubtaskInvalidEpicIdTest() {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        Epic epic = new Epic("first", "create first test", 50, Status.NEW, 50, LocalDateTime.of(2022, 11, 5, 12, 5));
        Subtask subtask = new Subtask("четвертая", "ывп", 50, Status.NEW, 2, 50, LocalDateTime.of(2021, 10, 5, 12, 5));
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createSubtask(subtask);
        int size = inMemoryTaskManager.getSubtasks().size();
        assertEquals(0, size);
    }

}