package controllers.fromhttp;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import controllers.fromfile.FileBackedTasksManager;
import controllers.generallogicfortasks.Managers;
import controllers.history.HistoryManager;
import model.Epic;
import model.Subtask;
import model.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpTaskManager extends FileBackedTasksManager {
    private final KVTaskClient kvTaskClient;
    private final Gson gson = Managers.getGson();
    public String[] keys = {"newKeyTask", "newKeySubtask", "newKeyEpic", "keyHistory"};

    public HttpTaskManager(HistoryManager historyManager, String url) throws IOException, InterruptedException {
        super(historyManager);
        this.kvTaskClient = new KVTaskClient(url);
        this.load();
    }

    public KVTaskClient getKvTaskClient() {
        return kvTaskClient;
    }

    public void load() {
        Map<Integer, Task> tasksFromSever = new HashMap<>();
        String text;
        for (String key : keys) {
            text = kvTaskClient.load(key);
            if (key.equals(this.keys[0])) {
                List<Task> tasks1;
                tasks1 = gson.fromJson(text, new TypeToken<ArrayList<Task>>() {
                }.getType());
                if (!(tasks1 == null)) {
                    for (Task task : tasks1) {
                        this.tasks.put(task.getIdentificationNumber(), task);
                    }
                    for (Task task : tasks.values()) {
                        tasksFromSever.put(task.getIdentificationNumber(), task);
                    }
                    continue;
                }
            }
            if (key.equals(this.keys[2])) {
                List<Epic> epics1;
                epics1 = gson.fromJson(text, new TypeToken<ArrayList<Epic>>() {
                }.getType());
                if (!(epics1 == null)) {
                    for (Epic epic : epics1) {
                        this.epics.put(epic.getIdentificationNumber(), epic);
                    }
                    for (Epic epic : epics.values()) {
                        tasksFromSever.put(epic.getIdentificationNumber(), epic);
                    }
                    continue;
                }
            }
            if (key.equals(this.keys[1])) {
                List<Subtask> subtasks1;
                subtasks1 = gson.fromJson(text, new TypeToken<ArrayList<Subtask>>() {
                }.getType());
                if (!(subtasks1 == null)) {
                    for (Subtask subtask : subtasks1) {
                        this.subtasks.put(subtask.getIdentificationNumber(), subtask);
                    }
                    for (Subtask subtask : subtasks.values()) {
                        tasksFromSever.put(subtask.getIdentificationNumber(), subtask);
                    }
                    continue;
                }
            }
            if (key.equals(this.keys[3])) {
                String s = gson.fromJson(text, String.class);
                if (!(s == null)) {
                    List<Integer> id = historyIdFromString(s);
                    if (!(tasksFromSever.isEmpty())) {
                        this.historyManager = restoreHistory(id, tasksFromSever);
                    }
                }
            }
        }
    }

    @Override
    public void save() {
        if (this.getTasks() != null || this.getTasks().size() == 0 ) {
            kvTaskClient.put(keys[0], gson.toJson(this.getTasks()));
        }
        if (this.getEpics() != null ||this.getEpics().size() == 0) {
            kvTaskClient.put(keys[2], gson.toJson(this.getEpics()));
        }
        if (this.getSubtasks() != null || this.getSubtasks().size() == 0) {
            kvTaskClient.put(keys[1], gson.toJson(this.getSubtasks()));
        }
        if (this.historyManager != null || this.historyManager.getHistory().size() == 0) {
            kvTaskClient.put(keys[3], gson.toJson(toStringHistory(this.historyManager)));
        }

    }

    public List<Integer> historyIdFromString(String value) {
        List<Integer> idTasks = new ArrayList<>();
        if (!(value == null)) {
            String[] ids = value.split("");
            for (String id : ids) {
                if (id.isBlank()) {
                    continue;
                }
                idTasks.add(Integer.parseInt(id.trim()));
            }
            return idTasks;
        }
        return null;
    }

    private HistoryManager restoreHistory(List<Integer> ids, Map<Integer, Task> tasks) {
        HistoryManager historyManager = getHistoryManager();
        for (int id : ids) {
            Task task = tasks.get(id);
            switch (task.getTypeTask()) {
                case EPIC:
                    Epic epic = (Epic) task;
                    historyManager.add(epic);
                    break;
                case SUBTASK:
                    Subtask subtask = (Subtask) task;
                    historyManager.add(subtask);
                    break;
                default:
                    historyManager.add(task);
            }
        }
        return historyManager;
    }

    public String toStringHistory(HistoryManager manager) {
        StringBuilder history = new StringBuilder();
        for (Task task : manager.getHistory()) {
            history.append(task.getIdentificationNumber());
        }
        return history.toString();
    }
}
