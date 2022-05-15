package controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class Managers {
    private final static InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
    private final static InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager(inMemoryHistoryManager);

    public static HistoryManager getDefaultHistory() {
        return inMemoryHistoryManager;
    }

    public static TaskManager getDefault() {
        return inMemoryTaskManager;
    }

    public static FileBackedTasksManager getDefault1(File file) {
        return new FileBackedTasksManager(inMemoryHistoryManager, file);
    }

    public static FileBackedTasksManager getDefault2(File file) {
        if (!(file.length() == 0)) {
            return FileBackedTasksManager.loadFromFile(file);
        } else {
            return getDefault1(file);
        }
    }

}
