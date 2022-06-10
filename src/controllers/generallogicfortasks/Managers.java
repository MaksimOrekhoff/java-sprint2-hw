package controllers.generallogicfortasks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controllers.fromfile.FileBackedTasksManager;
import controllers.fromhttp.HttpTaskManager;
import controllers.history.HistoryManager;
import controllers.history.InMemoryHistoryManager;
import controllers.utility.LocalDateAdapter;

import java.io.IOException;
import java.time.LocalDateTime;

public class Managers {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
            .create();

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefault() {
        return new FileBackedTasksManager(getDefaultHistory());
    }

    public static TaskManager getDefaults(HistoryManager historyManager, String url) throws IOException, InterruptedException {
        return new HttpTaskManager(historyManager, url);
    }

    public static Gson getGson() {
        return gson;
    }

}
