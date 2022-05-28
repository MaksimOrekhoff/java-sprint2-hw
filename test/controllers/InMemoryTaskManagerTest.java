package controllers;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest {
    @Test
    public void checkTasksIntersectionTest()  {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        LocalDateTime start1 = LocalDateTime.of(2022, 10, 5, 12, 0);
        LocalDateTime start2 = LocalDateTime.of(2022, 10, 5, 12, 5);
        LocalDateTime end1 = start1.plusMinutes(5);
        LocalDateTime end2 = start2.plusMinutes(5);
        boolean intersection = inMemoryTaskManager.conditionIntersection(start1, end1, start2, end2);
        assertTrue(intersection); //end1 = start2

        start2 = LocalDateTime.of(2022, 10, 5, 12, 4);
        intersection = inMemoryTaskManager.conditionIntersection(start1, end1, start2, end2);
        assertTrue(intersection); //start1 < start2 < end2 < end1

        start2 = start1;
        assertTrue(inMemoryTaskManager.conditionIntersection(start1, end1, start2, end2));

        end1 = end2;
        assertTrue(inMemoryTaskManager.conditionIntersection(start1, end1, start2, end2));

        end2 = start2;
        assertTrue(inMemoryTaskManager.conditionIntersection(start1, end1, start2, end2));

        start1 = LocalDateTime.of(2022, 10, 5, 12, 2);
        start2 = LocalDateTime.of(2022, 10, 5, 12, 0);
        end2 = start2.plusMinutes(10);
        end1 = start1.plusMinutes(3);
        assertTrue(inMemoryTaskManager.conditionIntersection(start1, end1, start2, end2));

        start2 = LocalDateTime.of(2022, 10, 5, 12, 0).plusMinutes(7);
        end2 = start2.plusMinutes(1);
        assertFalse(inMemoryTaskManager.conditionIntersection(start1, end1, start2, end2));
    }


}