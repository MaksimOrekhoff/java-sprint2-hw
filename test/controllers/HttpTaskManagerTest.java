package controllers;

import controllers.enumeratedtype.Status;
import controllers.enumeratedtype.TypeTask;
import controllers.fromhttp.HttpTaskManager;
import controllers.fromhttp.KVServer;
import controllers.generallogicfortasks.Managers;
import exep.ManagerSaveException;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    KVServer kvServer;

    @BeforeEach
    @Override
    public void init() {
        try {
            kvServer = new KVServer();
            kvServer.start();
            manager = new HttpTaskManager(Managers.getDefaultHistory(), "http://localhost:8078/");
            super.init();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    public void stop() {
        kvServer.stopServer(1);
    }

    @Test
    public void checkLoadTest() {
        try {
            super.init();
            HttpTaskManager httpTaskManager = new HttpTaskManager(Managers.getDefaultHistory(), "http://localhost:8078/");
            assertEquals(manager, httpTaskManager);
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException("Error");
        }
    }

    @Test
    public void checkSaveTest() {
        try {
            new KVServer().start();
            HttpTaskManager httpTaskManager = new HttpTaskManager(Managers.getDefaultHistory(), "http://localhost:8078/");
            task1 = new Task("первая", "ывпа", 20, Status.NEW, TypeTask.TASK, 50, LocalDateTime.of(2022, 6, 1, 1, 5));
            epic1 = new Epic("first", "create first test", 50, Status.NEW, 50, LocalDateTime.of(2022, 6, 1, 3, 5));
            subtask1 = new Subtask("четвертая", "ывп", 50, Status.NEW, 2, 50, LocalDateTime.of(2022, 6, 1, 5, 5));

            assertTrue(httpTaskManager.getTasks().isEmpty());
            assertTrue(httpTaskManager.getEpics().isEmpty());
            assertTrue(httpTaskManager.getSubtasks().isEmpty());

            httpTaskManager.createTask(task1);
            httpTaskManager.createEpic(epic1);
            httpTaskManager.createSubtask(subtask1);

            assertFalse(httpTaskManager.getTasks().isEmpty());
            assertFalse(httpTaskManager.getEpics().isEmpty());
            assertFalse(httpTaskManager.getSubtasks().isEmpty());

        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException("Error");
        }
    }


}