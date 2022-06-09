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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest {
    HttpTaskManager manager;
    HttpTaskServer httpTaskServer;
    KVServer kvServer;
    Task task1;
    String nameTask = "task";
    String taskJson;
    String bodyTask1;
    Epic epic1;
    String nameEpic = "epic";
    String bodyEpic1;
    Subtask subtask1;
    String nameSubtask = "subtask";
    String bodySubtask1;
    HttpTaskQuery httpTaskQuery;
    String url = "http://localhost:8078/";
    HttpClient client = HttpClient.newHttpClient();
    private static final GsonBuilder gsonBuilder = new GsonBuilder();
    private static final Gson gson = gsonBuilder
            .registerTypeAdapter(LocalDateTime.class, new HttpTaskManager.LocalDateAdapter())
            .create();

    @BeforeEach
    void init() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        httpTaskServer = new HttpTaskServer();
        httpTaskServer.mai();
        httpTaskQuery = new HttpTaskQuery();
        manager = httpTaskServer.getHttpTaskManager();
        bodyTask1 = "\t{\n" +
                "\t\t\"typeTask\": \"TASK\",\n" +
                "\t\t\"name\": \"первая\",\n" +
                "\t\t\"description\": \"ывпа\",\n" +
                "\t\t\"identificationNumber\": 1,\n" +
                "\t\t\"status\": \"DONE\",\n" +
                "\t\t\"duration\": 50,\n" +
                "\t\t\"startTime\": \"2021-09-05T12:05:00\"\n" +
                "\t}";
        bodySubtask1 = "{\n" +
                "\t\t\"connectionWithEpic\": 2,\n" +
                "\t\t\"typeTask\": \"SUBTASK\",\n" +
                "\t\t\"name\": \"четвертая\",\n" +
                "\t\t\"description\": \"ывп\",\n" +
                "\t\t\"identificationNumber\": 3,\n" +
                "\t\t\"status\": \"NEW\",\n" +
                "\t\t\"duration\": 50,\n" +
                "\t\t\"startTime\": \"2022-09-05T12:05:00\"\n" +
                "\t}";
        bodyEpic1 = "\n" +
                "\t{\n" +
                "\t\t\"subtasksEpic\": {\n" +
                "\t\t\t\"3\": {\n" +
                "\t\t\t\t\"connectionWithEpic\": 2,\n" +
                "\t\t\t\t\"typeTask\": \"SUBTASK\",\n" +
                "\t\t\t\t\"name\": \"четвертая\",\n" +
                "\t\t\t\t\"description\": \"ывп\",\n" +
                "\t\t\t\t\"identificationNumber\": 3,\n" +
                "\t\t\t\t\"status\": \"NEW\",\n" +
                "\t\t\t\t\"duration\": 50,\n" +
                "\t\t\t\t\"startTime\": \"2022-09-05T12:05:00\"\n" +
                "\t\t\t}\n" +
                "\t\t},\n" +
                "\t\t\"endTime\": \"null\",\n" +
                "\t\t\"typeTask\": \"EPIC\",\n" +
                "\t\t\"name\": \"третий\",\n" +
                "\t\t\"description\": \"ывапр\",\n" +
                "\t\t\"identificationNumber\": 2,\n" +
                "\t\t\"status\": \"NEW\",\n" +
                "\t\t\"duration\": 50,\n" +
                "\t\t\"startTime\": \"2022-09-05T12:05:00\"\n" +
                "\t}";

        task1 = new Task("первая", "ывпа", 1, Status.DONE, TypeTask.TASK, 50, LocalDateTime.of(2021, 9, 5, 12, 5));
        epic1 = new Epic("третий", "ывапр", 2, Status.NEW, 50, LocalDateTime.of(2023, 10, 5, 12, 5, 0, 0));//        subtask1 = new Subtask("четвертая", "ывп", 50, Status.NEW, 3, 50, LocalDateTime.of(2022, 6, 1, 5, 5));
        subtask1 = new Subtask("четвертая", "ывп", 3, Status.NEW, 2, 50, LocalDateTime.of(2022, 9, 5, 12, 5, 0, 0));
//        manager.createTask(task1);
//        manager.createEpic(epic1);
//        manager.createSubtask(subtask1);
    }

    @Test
    public void checkCreateTaskAndMethodPostKVServer() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task/");
        String json = gson.toJson(task1);
        int sizeTask = manager.tasks.size();
        int sizeKVServ = kvServer.data.size();
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertNotEquals(sizeTask, manager.tasks.size());
        assertNotEquals(sizeKVServ, kvServer.data.size());
        assertEquals(task1, manager.getTask(1));
    }

    @Test
    public void checkCreateEpicAndMethodPostKVServer() throws IOException, InterruptedException {
        int sizeTask = manager.epics.size();
        int sizeKVServ = kvServer.data.size();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        String json = gson.toJson(epic1);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertNotEquals(sizeTask, manager.epics.size());
        assertNotEquals(sizeKVServ, kvServer.data.size());
        assertEquals(manager.epics.get(1), manager.getEpic(1));
    }

    @Test
    public void checkCreateSubtaskAndMethodPostKVServer() throws IOException, InterruptedException {
        int sizeTask = manager.epics.size();
        int sizeKVServ = kvServer.data.size();
        httpTaskQuery.postTask(nameEpic, bodyEpic1);
        assertNotEquals(sizeTask, manager.epics.size());
        assertNotEquals(sizeKVServ, kvServer.data.size());
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        String json = gson.toJson(subtask1);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(2, kvServer.data.size());
        assertEquals(manager.subtasks.get(2), manager.getSubtask(2));
    }

    @Test
    public void checkGetAllTaskTest() throws IOException, InterruptedException {
        httpTaskQuery.postTask(nameTask, bodyTask1);
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ArrayList<Task> task = gson.fromJson(response.body(), new TypeToken<ArrayList<Task>>() {
        }.getType());
        assertEquals(200, response.statusCode());
        assertEquals(task, manager.getTasks());
    }

    @Test
    public void checkGetAllEpicTest() throws IOException, InterruptedException {
        httpTaskQuery.postTask(nameEpic, bodyEpic1);
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ArrayList<Epic> epic = gson.fromJson(response.body(), new TypeToken<ArrayList<Epic>>() {
        }.getType());
        assertEquals(200, response.statusCode());
        assertEquals(epic, manager.getEpics());
    }

    @Test
    public void checkGetAllSubtaskTest() throws IOException, InterruptedException {
        httpTaskQuery.postTask(nameEpic, bodyEpic1);
        httpTaskQuery.postTask(nameSubtask, bodySubtask1);
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ArrayList<Subtask> subtasks = gson.fromJson(response.body(), new TypeToken<ArrayList<Subtask>>() {
        }.getType());
        assertEquals(200, response.statusCode());
        assertEquals(subtasks, manager.getSubtasks());
    }

    @Test
    public void checkGetTaskOnIdTest() throws IOException, InterruptedException {
        httpTaskQuery.postTask(nameTask, bodyTask1);
        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Код ответа: " + response.statusCode());
        System.out.println("Тело ответа: " + response.body());
        Task task = gson.fromJson(response.body(), Task.class);
        assertEquals(200, response.statusCode());
        assertEquals(task, manager.getTask(1));
    }

    @Test
    public void checkGetEpicOnIdTest() throws IOException, InterruptedException {
        httpTaskQuery.postTask(nameEpic, bodyEpic1);
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Epic epic = gson.fromJson(response.body(), Epic.class);
        assertEquals(200, response.statusCode());
        assertEquals(epic, manager.getEpic(1));
    }

    @Test
    public void checkGetSubtaskOnIdTest() throws IOException, InterruptedException {
        httpTaskQuery.postTask(nameEpic, bodyEpic1);
        httpTaskQuery.postTask(nameSubtask, bodySubtask1);
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Subtask subtask = gson.fromJson(response.body(), Subtask.class);
        assertEquals(200, response.statusCode());
        assertEquals(subtask, manager.getSubtask(2));
    }

    @Test
    public void checkDeleteTaskOnIdTest() throws IOException, InterruptedException {
        httpTaskQuery.postTask(nameTask, bodyTask1);
        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Task task = gson.fromJson(response.body(), Task.class);
        assertEquals(200, response.statusCode());
        assertEquals(task, manager.getTask(1));

        url = URI.create("http://localhost:8080/tasks/task/?id=1");
        request = HttpRequest.newBuilder().uri(url).DELETE().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertTrue(manager.tasks.isEmpty());
    }

    @Test
    public void checkDeleteEpicOnIdTest() throws IOException, InterruptedException {
        httpTaskQuery.postTask(nameEpic, bodyEpic1);
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Epic epic = gson.fromJson(response.body(), Epic.class);
        assertEquals(200, response.statusCode());
        assertEquals(epic, manager.getEpic(1));
        url = URI.create("http://localhost:8080/tasks/epic/?id=1");
        request = HttpRequest.newBuilder().uri(url).DELETE().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertTrue(manager.epics.isEmpty());
    }

    @Test
    public void checkDeleteSubtaskOnIdTest() throws IOException, InterruptedException {
        httpTaskQuery.postTask(nameEpic, bodyEpic1);
        httpTaskQuery.postTask(nameSubtask, bodySubtask1);
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Subtask subtask = gson.fromJson(response.body(), Subtask.class);
        assertEquals(200, response.statusCode());
        assertEquals(subtask, manager.getSubtask(2));
        url = URI.create("http://localhost:8080/tasks/subtask/?id=2");
        request = HttpRequest.newBuilder().uri(url).DELETE().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertTrue(manager.subtasks.isEmpty());
    }

    @Test
    public void checkLoadHttpTaskManagerTest() throws IOException, InterruptedException {
        int size = 1;
        httpTaskQuery.postTask(nameTask, bodyTask1);
        httpTaskQuery.postTask(nameEpic, bodyEpic1);
        httpTaskQuery.postTask(nameSubtask, bodySubtask1);
        assertEquals(size, manager.getEpics().size());
        assertEquals(size, manager.getTasks().size());
        assertEquals(size, manager.getSubtasks().size());

        HttpTaskManager httpTaskManager = new HttpTaskManager(Managers.getDefaultHistory(), url);
        assertTrue(httpTaskManager.tasks.isEmpty());
        assertTrue(httpTaskManager.subtasks.isEmpty());
        assertTrue(httpTaskManager.epics.isEmpty());
        httpTaskManager.getKvTaskClient().setToken(manager.getKvTaskClient().getToken());
        for (int i = 0; i < httpTaskManager.key.length; i++) {
            httpTaskManager.load(httpTaskManager.key[i]);
        }
        System.out.println(manager);
        System.out.println(httpTaskManager);
    }

    @Test
    public void checkLoadHttpTaskManagerHistoryTest() throws IOException, InterruptedException {
        httpTaskQuery.postTask(nameTask, bodyTask1);
        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Task task = gson.fromJson(response.body(), Task.class);
        assertEquals(200, response.statusCode());
        assertEquals(task, manager.getTask(1));
        List<Task> historyOld = manager.getHistoryManagers();
        HttpTaskManager httpTaskManager = new HttpTaskManager(Managers.getDefaultHistory(), this.url);
        assertTrue(httpTaskManager.tasks.isEmpty());
        assertTrue(httpTaskManager.subtasks.isEmpty());
        assertTrue(httpTaskManager.epics.isEmpty());
        httpTaskManager.getKvTaskClient().setToken(manager.getKvTaskClient().getToken());
        for (int i = 0; i < httpTaskManager.key.length; i++) {
            httpTaskManager.load(httpTaskManager.key[i]);
        }
        List<Task> newHistory = httpTaskManager.getHistoryManagers();
        assertEquals(historyOld, newHistory);
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

}