package controllers;

import java.io.IOException;

public class Managers {

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefault() {
        return new FileBackedTasksManager(getDefaultHistory());
    }

    public static TaskManager getDefaults(HistoryManager historyManager, String url) throws IOException, InterruptedException {
        return new HttpTaskManager(historyManager, url);
    }

}
