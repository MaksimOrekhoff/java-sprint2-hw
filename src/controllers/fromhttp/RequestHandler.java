package controllers.fromhttp;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import controllers.generallogicfortasks.Managers;
import controllers.history.HistoryManager;
import model.Epic;
import model.Subtask;
import model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class RequestHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final Gson gson = Managers.getGson();

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
                            response = gson.toJson(httpTaskManager.getSubtasks());//получение Subtasks по id реализовано ниже
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
                                    httpTaskManager.getKvTaskClient().put(httpTaskManager.keys[3], gson.toJson(toStringHistory(httpTaskManager.getHistoryManager())));
                                    httpTaskManager.save();
                                    break;
                                case "subtask":
                                    response = gson.toJson(httpTaskManager.getSubtask(idGetTask));
                                    httpTaskManager.getKvTaskClient().put(httpTaskManager.keys[3], gson.toJson(toStringHistory(httpTaskManager.historyManager)));
                                    httpTaskManager.save();
                                    break;
                                case "epic":
                                    String subtask = arrayPath[arrayPath.length - 3];
                                    if (subtask.equals("subtask")) {
                                        response = gson.toJson(httpTaskManager.getEpic(idGetTask).getSubtasksEpic());
                                        break;
                                    }
                                    response = gson.toJson(httpTaskManager.getEpic(idGetTask));
                                    httpTaskManager.getKvTaskClient().put(httpTaskManager.keys[3], gson.toJson(toStringHistory(httpTaskManager.historyManager)));
                                    httpTaskManager.save();
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
                            httpTaskManager.getKvTaskClient().put(httpTaskManager.keys[0], gson.toJson(httpTaskManager.getTasks()));
                            httpTaskManager.save();
                            response = "Задача обновлена.";
                        } else {
                            httpTaskManager.createTask(newTask);
                            httpTaskManager.getKvTaskClient().put(httpTaskManager.keys[0], gson.toJson(httpTaskManager.getTasks()));
                            httpTaskManager.save();
                            response = "Задача создана.";
                        }
                        break;
                    case "subtask":
                        Subtask newSubtask = gson.fromJson(body, Subtask.class);
                        if (httpTaskManager.getSubtask(newSubtask.getIdentificationNumber()) != null) {
                            httpTaskManager.updateTask(newSubtask);
                            httpTaskManager.getKvTaskClient().put(httpTaskManager.keys[1], gson.toJson(httpTaskManager.getSubtasks()));
                            httpTaskManager.getKvTaskClient().put(httpTaskManager.keys[2], gson.toJson(httpTaskManager.getEpics()));
                            httpTaskManager.save();
                            response = "Подадача обновлена.";
                        } else {
                            httpTaskManager.createSubtask(newSubtask);
                            httpTaskManager.getKvTaskClient().put(httpTaskManager.keys[1], gson.toJson(httpTaskManager.getSubtasks()));
                            httpTaskManager.save();
                            response = "Подадача создана.";
                        }
                        break;
                    case "epic":
                        Epic newEpic = gson.fromJson(body, Epic.class);
                        if (httpTaskManager.getEpic(newEpic.getIdentificationNumber()) != null) {
                            httpTaskManager.updateTask(newEpic);
                            httpTaskManager.getKvTaskClient().put(httpTaskManager.keys[2], gson.toJson(httpTaskManager.getEpics()));
                            httpTaskManager.save();
                            response = "Эпик обновлен.";
                        } else {
                            httpTaskManager.createEpic(newEpic);
                            httpTaskManager.getKvTaskClient().put(httpTaskManager.keys[2], gson.toJson(httpTaskManager.getEpics()));
                            httpTaskManager.save();
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
                        httpTaskManager.getKvTaskClient().put(httpTaskManager.keys[0], gson.toJson(httpTaskManager.getTasks()));
                        httpTaskManager.save();
                        response = "Список задач очищен.";
                        break;
                    case "subtask":
                        httpTaskManager.clearSubtask();
                        httpTaskManager.getKvTaskClient().put(httpTaskManager.keys[1], gson.toJson(httpTaskManager.getSubtasks()));
                        httpTaskManager.save();
                        response = "Список подзадач очищен.";
                        break;
                    case "epic":
                        httpTaskManager.clearEpic();
                        httpTaskManager.getKvTaskClient().put(httpTaskManager.keys[1], gson.toJson(httpTaskManager.getSubtasks()));
                        httpTaskManager.getKvTaskClient().put(httpTaskManager.keys[2], gson.toJson(httpTaskManager.getEpics()));
                        httpTaskManager.save();
                        response = "Список эпиков очищен.";
                        break;
                    default:
                        int idDeleteTask = Integer.parseInt(typeTask.substring(typeTask.length() - 1));
                        switch (arrayPath[arrayPath.length - 2]) {
                            case "task":
                                httpTaskManager.deleteTask(idDeleteTask);
                                httpTaskManager.getKvTaskClient().put(httpTaskManager.keys[0], gson.toJson(httpTaskManager.getTasks()));
                                httpTaskManager.save();
                                response = "Задача удалена.";
                                break;
                            case "subtask":
                                httpTaskManager.deleteSubtask(idDeleteTask);
                                httpTaskManager.getKvTaskClient().put(httpTaskManager.keys[1], gson.toJson(httpTaskManager.getSubtasks()));
                                httpTaskManager.save();
                                response = "Подзадача удалена.";
                                break;
                            case "epic":
                                httpTaskManager.deleteEpic(idDeleteTask);
                                httpTaskManager.getKvTaskClient().put(httpTaskManager.keys[1], gson.toJson(httpTaskManager.getSubtasks()));
                                httpTaskManager.getKvTaskClient().put(httpTaskManager.keys[2], gson.toJson(httpTaskManager.getEpics()));
                                httpTaskManager.save();
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
}
