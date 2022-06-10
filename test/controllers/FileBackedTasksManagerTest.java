package controllers;


import controllers.enumeratedtype.Status;
import controllers.fromfile.FileBackedTasksManager;
import controllers.generallogicfortasks.Managers;
import model.Epic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import static controllers.fromfile.FileBackedTasksManager.loadFromFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    @BeforeEach
    @Override
    public void init() {
        File f = new File("Task.csv");
        if (!f.isFile()) {
            try {
                 f.createNewFile();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        manager = loadFromFile(f);
        super.init();
    }


    @Test
    public void checkLoadingEmptyTaskListTest() {
        File f = new File("Task.csv");

            try {
                f.createNewFile();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

        FileBackedTasksManager fileBackedTaskManager = new FileBackedTasksManager(Managers.getDefaultHistory());
        assertTrue(fileBackedTaskManager.getTasks().isEmpty());
        assertTrue(fileBackedTaskManager.getSubtasks().isEmpty());
        assertTrue(fileBackedTaskManager.getEpics().isEmpty());
    }

    @Test
    public void checkSavingWithoutHistoryTest() {
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
        fileBackedTaskManager.createTask(task1);
        fileBackedTaskManager.createTask(task2);

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
        FileBackedTasksManager fileBackedTaskManager = loadFromFile(f);
        Epic epic4 = new Epic("седьмой", "фвап", 50, Status.NEW, 50, LocalDateTime.now());

        fileBackedTaskManager.createEpic(epic4);

        FileBackedTasksManager loadTask  = loadFromFile(f);
        assertEquals(fileBackedTaskManager, loadTask);
        assertTrue(loadTask.getEpics().get(2).getSubtasksEpic().isEmpty());
    }
}