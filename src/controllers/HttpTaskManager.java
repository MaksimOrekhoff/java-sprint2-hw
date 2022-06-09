package controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import model.Epic;
import model.Subtask;
import model.Task;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class HttpTaskManager extends FileBackedTasksManager {


    private final KVTaskClient kvTaskClient;
    private static final GsonBuilder gsonBuilder = new GsonBuilder();
    Map<Integer, Task> tasksFromSever = new HashMap<>();
    private static final Gson gson = gsonBuilder
            .registerTypeAdapter(LocalDateTime.class, new HttpTaskManager.LocalDateAdapter())
            .create();
    public String[] key = {"newKeyTask", "newKeySubtask", "newKeyEpic", "keyHistory"};

    public HttpTaskManager(HistoryManager historyManager, String url) throws IOException, InterruptedException {
        super(historyManager);
        this.kvTaskClient = new KVTaskClient(url);
    }
    public KVTaskClient getKvTaskClient() {
        return kvTaskClient;
    }

    public void load(String key) throws IOException, InterruptedException {
        String text = kvTaskClient.load(key);
        if (key.equals(this.key[0])) {
            this.tasks = gson.fromJson(text, new TypeToken<HashMap<Integer, Task>>() {
            }.getType());
            if (!(tasks == null)) {
                for (Task task : tasks.values()) {
                    tasksFromSever.put(task.getIdentificationNumber(), task);
                }
                return;
            }
        }
        if (key.equals(this.key[2])) {
            this.epics = gson.fromJson(text, new TypeToken<HashMap<Integer, Epic>>() {
            }.getType());
            if (!(epics == null)) {
                for (Epic epic : epics.values()) {
                    tasksFromSever.put(epic.getIdentificationNumber(), epic);
                }
                return;
            }
        }
        if (key.equals(this.key[1])) {
            this.subtasks = gson.fromJson(text, new TypeToken<HashMap<Integer, Subtask>>() {
            }.getType());
            if (!(subtasks == null)) {
                for (Subtask subtask : subtasks.values()) {
                    tasksFromSever.put(subtask.getIdentificationNumber(), subtask);
                }
                return;
            }
        }

        if (key.equals(this.key[3])) {
            String s = gson.fromJson(text, String.class);
            if (!(s == null)) {
                List<Integer> id = historyFromString(s);
                if (!(tasksFromSever.isEmpty())) {
                    this.historyManager = restoreHistory(id, tasksFromSever);
                }
            }


        }

    }

    public void save(String key, String json) throws IOException, InterruptedException {
        kvTaskClient.put(key, json);
    }

    static class LocalDateAdapter extends TypeAdapter<LocalDateTime> {

        private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;


        @Override
        public void write(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {
            if (localDateTime == null) {
                jsonWriter.value("null");
                return;
            }
            jsonWriter.value(localDateTime.format(formatter));
        }

        @Override
        public LocalDateTime read(final JsonReader jsonReader) throws IOException {
            final String text = jsonReader.nextString();
            if (text.equals("null")) {
                return null;
            }
            return LocalDateTime.parse(text, formatter);
        }
    }

    public static List<Integer> historyFromString(String value) {
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

    @Override
    public String toString() {
        return "HttpTaskManager{" +
                "kvTaskClient=" + kvTaskClient +
                ", tasksFromSever=" + tasksFromSever +
                ", key=" + Arrays.toString(key) +
                ", tasks=" + tasks +
                ", subtasks=" + subtasks +
                ", epics=" + epics +
                ", historyManager=" + historyManager +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        HttpTaskManager that = (HttpTaskManager) o;
        return Objects.equals(kvTaskClient, that.kvTaskClient) && Objects.equals(tasksFromSever, that.tasksFromSever) && Arrays.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(super.hashCode(), kvTaskClient, tasksFromSever);
        result = 31 * result + Arrays.hashCode(key);
        return result;
    }
}
