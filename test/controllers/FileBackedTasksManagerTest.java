package controllers;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import static controllers.FileBackedTasksManager.loadFromFile;

class FileBackedTasksManagerTest extends TaskManagerTest {
    @Test
    public void checkLoadingEmptyTaskListTest() {
        File f = new File("Task.csv");
        if (!f.isFile()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        FileBackedTasksManager fileBackedTaskManager;
        fileBackedTaskManager = loadFromFile(f);

        assertTrue(fileBackedTaskManager.getTasks().isEmpty());
        assertTrue(fileBackedTaskManager.getSubtasks().isEmpty());
        assertTrue(fileBackedTaskManager.getEpics().isEmpty());
    }

    @Test
    public void checkSavingWithoutHistoryTest() throws IllegalAccessException {
        File f = new File("Task.csv");
        if (!f.isFile()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        FileBackedTasksManager fileBackedTaskManager;
        fileBackedTaskManager = loadFromFile(f);
        fileBackedTaskManager.createTask("первая", "ывпа", 50, LocalDateTime.of(2021, 10, 5, 12, 5));
        fileBackedTaskManager.createTask("вторая", "выап", 50, LocalDateTime.of(2021, 10, 5, 12, 56));

        FileBackedTasksManager loadTask;
        loadTask = loadFromFile(f);
        assertEquals(fileBackedTaskManager, loadTask);
        assertTrue(loadTask.getHistoryManagers().isEmpty());
    }

    @Test
    public void checkEpicWithoutSubtaskSavingAndLoadingTest() {
        File f = new File("Task.csv");
        if (!f.isFile()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        FileBackedTasksManager fileBackedTaskManager;
        fileBackedTaskManager = loadFromFile(f);
        fileBackedTaskManager.createEpic("третий", "ывапр", 50, LocalDateTime.of(2020, 10, 5, 12, 5));

        FileBackedTasksManager loadTask;
        loadTask = loadFromFile(f);
        assertEquals(fileBackedTaskManager, loadTask);
        assertTrue(loadTask.getSubtasks().isEmpty());
        assertTrue(loadTask.getEpic(1).getSubtasksEpic().isEmpty());
    }
}