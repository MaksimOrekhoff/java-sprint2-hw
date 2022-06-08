package controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpExchange;
import model.Epic;
import model.Subtask;
import model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RequestHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final GsonBuilder gsonBuilder = new GsonBuilder();
    private static final Gson gson = gsonBuilder
            .registerTypeAdapter(LocalDateTime.class, new RequestHandler.LocalDateAdapter())
            .create();



//    public void createTask() {
//        Task task1 = new Task("первая", "ывпа", 100, Status.DONE,
//                TypeTask.TASK, 50, null);
//        fileBackedTasksManager.createTask(task1);
//
//        Epic epic1 = new Epic("третий", "ывапр", 100, Status.NEW, 50, LocalDateTime.of(2023, 10, 5, 12, 5, 0, 0));
//        fileBackedTasksManager.createEpic(epic1);
//
//        Subtask subtask1 = new Subtask("четвертая", "ывп", 50, Status.NEW, 2, 50, LocalDateTime.of(2022, 9, 5, 12, 5, 0, 0));
//
//        fileBackedTasksManager.createSubtask(subtask1);
//    }


    public Response response(HttpExchange httpExchange, HttpTaskManager httpTaskManager) throws IOException, InterruptedException {

        String response = "";
        int code = 200;

        InputStream inputStream = httpExchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
        System.out.println("Тело запроса:\n" + body);

        String method = httpExchange.getRequestMethod();
        System.out.println("Началась обработка " + method + " /tasks запроса от клиента.");
        URI uri = httpExchange.getRequestURI();
        String[] arrayPath = uri.toString().split("/");
        String typeTask = arrayPath[arrayPath.length - 1];

        switch (method) {
            case "GET":
                if (typeTask.equals("tasks")) {
                    response = gson.toJson(httpTaskManager.getPrioritizedTasks());
                } else {
                    switch (typeTask) {
                        case "task":
                            response = gson.toJson(httpTaskManager.getTasks());
                            break;
                        case "subtask":
                            response = gson.toJson(httpTaskManager.getSubtasks());
                            break;
                        case "epic":
                            response = gson.toJson(httpTaskManager.getEpics());
                            break;
                        case "history":
                            response = gson.toJson(httpTaskManager.getHistoryManagers());
                            break;
                        default:
                            int idGetTask = Integer.parseInt(typeTask.substring(typeTask.length() - 1));
                            switch (arrayPath[arrayPath.length - 2]) {
                                case "task":
                                    response = gson.toJson(httpTaskManager.getTask(idGetTask));
                                    httpTaskManager.save(httpTaskManager.key[3], gson.toJson(toStringHistory(httpTaskManager.historyManager)));
                                    break;
                                case "subtask":
                                    response = gson.toJson(httpTaskManager.getSubtask(idGetTask));
                                    httpTaskManager.save(httpTaskManager.key[3], gson.toJson(toStringHistory(httpTaskManager.historyManager)));

                                    break;
                                case "epic":
                                    String subtask = arrayPath[arrayPath.length - 3];
                                    if (subtask.equals("subtask")) {
                                        response = gson.toJson(httpTaskManager.getEpic(idGetTask).getSubtasksEpic());
                                        break;
                                    }
                                    response = gson.toJson(httpTaskManager.getEpic(idGetTask));
                                    httpTaskManager.save(httpTaskManager.key[3], gson.toJson(toStringHistory(httpTaskManager.historyManager)));
                                    break;
                            }
                            break;
                    }
                }
                break;
            case "POST":
                switch (typeTask) {
                    case "task":
                        Task newTask = gson.fromJson(body, Task.class);
                        if (httpTaskManager.getTask(newTask.getIdentificationNumber()) != null) {
                            httpTaskManager.updateTask(newTask);
                            httpTaskManager.save(httpTaskManager.key[0], gson.toJson(httpTaskManager.tasks));
                            response = "Задача обновлена.";
                        } else {
                            httpTaskManager.createTask(newTask);
                            httpTaskManager.save(httpTaskManager.key[0], gson.toJson(httpTaskManager.tasks));
                            response = "Задача создана.";
                        }

                        break;
                    case "subtask":
                        Subtask newSubtask = gson.fromJson(body, Subtask.class);
                        if (httpTaskManager.getSubtask(newSubtask.getIdentificationNumber()) != null) {
                            httpTaskManager.updateTask(newSubtask);
                            httpTaskManager.save(httpTaskManager.key[1], gson.toJson(httpTaskManager.subtasks));
                            response = "Подадача обновлена.";
                        } else {
                            httpTaskManager.createSubtask(newSubtask);
                            httpTaskManager.save(httpTaskManager.key[1], gson.toJson(httpTaskManager.subtasks));
                            response = "Подадача создана.";
                        }
                        break;
                    case "epic":
                        Epic newEpic = gson.fromJson(body, Epic.class);
                        if (httpTaskManager.getEpic(newEpic.getIdentificationNumber()) != null) {
                            httpTaskManager.updateTask(newEpic);
                            httpTaskManager.save(httpTaskManager.key[2], gson.toJson(httpTaskManager.epics));
                            response = "Эпик обновлен.";
                        } else {
                            httpTaskManager.createEpic(newEpic);
                            httpTaskManager.save(httpTaskManager.key[2], gson.toJson(httpTaskManager.epics));
                            response = "Эпик создан.";
                        }
                        break;
                    default:
                        response = "Не верный тип задачи.";
                        code = 404;
                }
                break;
            case "DELETE":
                switch (typeTask) {
                    case "task":
                        httpTaskManager.clearTask();
                        httpTaskManager.save(httpTaskManager.key[0], gson.toJson(httpTaskManager.tasks));
                        response = "Список задач очищен.";
                        break;
                    case "subtask":
                        httpTaskManager.clearSubtask();
                        httpTaskManager.save(httpTaskManager.key[1], gson.toJson(httpTaskManager.subtasks));
                        response = "Список подзадач очищен.";
                        break;
                    case "epic":
                        httpTaskManager.clearEpic();
                        httpTaskManager.save(httpTaskManager.key[1], gson.toJson(httpTaskManager.subtasks));
                        httpTaskManager.save(httpTaskManager.key[2], gson.toJson(httpTaskManager.epics));
                        response = "Список эпиков очищен.";
                        break;
                    default:
                        int idDeleteTask = Integer.parseInt(typeTask.substring(typeTask.length() - 1));
                        switch (arrayPath[arrayPath.length - 2]) {
                            case "task":
                                httpTaskManager.deleteTask(idDeleteTask);
                                httpTaskManager.save(httpTaskManager.key[0], gson.toJson(httpTaskManager.tasks));
                                response = "Задача удалена.";
                                break;
                            case "subtask":
                                httpTaskManager.deleteSubtask(idDeleteTask);
                                httpTaskManager.save(httpTaskManager.key[1], gson.toJson(httpTaskManager.subtasks));
                                response = "Подзадача удалена.";
                                break;
                            case "epic":
                                httpTaskManager.deleteEpic(idDeleteTask);
                                httpTaskManager.save(httpTaskManager.key[1], gson.toJson(httpTaskManager.subtasks));
                                httpTaskManager.save(httpTaskManager.key[2], gson.toJson(httpTaskManager.epics));
                                response = "Эпик удален.";
                                break;
                            default:
                                response = "Неверный запрос.";
                                code = 404;
                        }
                }
                break;
            default:
                code = 404;
                System.out.println("Неверный запрос.");
        }
        return new Response(response, code);
    }

    public static String toStringHistory(HistoryManager manager) {
        StringBuilder history = new StringBuilder();
        for (Task task : manager.getHistory()) {
            history.append(task.getIdentificationNumber());
        }
        return history.toString();
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
