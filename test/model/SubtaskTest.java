package model;

import controllers.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    @Test
    public void checkAddSubtaskInvalidEpicIdTest() throws IllegalAccessException {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        inMemoryTaskManager.createEpic("first", "create first test", 50, LocalDateTime.of(2021, 10, 5, 12, 5));
        inMemoryTaskManager.createSubtask(2, "четвертая", "ывп", 50, LocalDateTime.of(2022, 10, 5, 12, 5));
        int size = inMemoryTaskManager.getSubtasks().size();
        assertEquals(0, size);
    }

    @Test
    public void checkToStringSubtaskTest() throws IllegalAccessException, IOException {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        inMemoryTaskManager.createEpic("first", "create first test", 50, LocalDateTime.of(2022, 10, 5, 12, 5));
        inMemoryTaskManager.createSubtask(1, "четвертая", "ывп", 50, LocalDateTime.of(2023, 10, 5, 12, 5));
        String expected = "model.Subtask{" +
                "typeTask=" + inMemoryTaskManager.getSubtask(2).getTypeTask() +
                ", name='" + inMemoryTaskManager.getSubtask(2).getName() +
                ", description='" + inMemoryTaskManager.getSubtask(2).getDescription() +
                ", identificationNumber=" + inMemoryTaskManager.getSubtask(2).getIdentificationNumber() +
                ", status=" + inMemoryTaskManager.getSubtask(2).getStatus() +
                ", connectionWithEpic=" + inMemoryTaskManager.getSubtask(2).getConnectionWithEpic() +
                ", duration=" + inMemoryTaskManager.getSubtask(2).getDuration() +
                ", startTime=" + inMemoryTaskManager.getSubtask(2).getStartTime() +
                '}';

        String line = inMemoryTaskManager.getSubtask(2).toString();

        assertEquals(expected, line);
    }

}