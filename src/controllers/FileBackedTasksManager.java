package controllers;

import model.Epic;
import model.Subtask;
import model.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FileBackedTasksManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTasksManager(HistoryManager historyManager, File file) {
        super(historyManager);
        this.file = file;
    }

    private void save() {
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("id,type,name,status,description,epic" + "\n");

            for (Task task : getTasks().values()) {
                fileWriter.write(toString(task));
            }
            for (Epic epic : getEpics().values()) {
                fileWriter.write(toString(epic));
            }
            for (Subtask subtask : getSubtasks().values()) {
                fileWriter.write(toString(subtask));
            }

            fileWriter.write("\n");

            fileWriter.write(toString(getHistoryManager()));

            fileWriter.close();

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения в файл.");
        }
    }

    @Override
    public void createTask(String name, String description) {
        super.createTask(name, description);
        save();
    }

    @Override
    public void createSubtask(int epicId, String name, String description) {
        super.createSubtask(epicId, name, description);
        save();
    }

    @Override
    public void createEpic(String name, String description) {
        super.createEpic(name, description);
        save();
    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = super.getSubtask(id);
        save();
        return subtask;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void clearTask() {
        super.clearTask();
        save();
    }

    @Override
    public void clearEpic() {
        super.clearEpic();
        save();
    }

    @Override
    public void clearSubtask() {
        super.clearSubtask();
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    public String toString(Task task) {
        if (task.getTypeTask() == TypeTask.SUBTASK) {
            Subtask subtask = (Subtask) task;
            return subtask.getIdentificationNumber() + "," + subtask.getTypeTask() + "," + subtask.getName() + "," + subtask.getStatus() + "," + subtask.getDescription() + "," + subtask.getConnectionWithEpic() + "\n";
        }
        return task.getIdentificationNumber() + "," + task.getTypeTask() + "," + task.getName() + "," + task.getStatus() + "," + task.getDescription() + "\n";
    }


    public static String toString(HistoryManager manager) {
        StringBuilder history = new StringBuilder();
        for (Task task : manager.getHistory()) {
            history.append(task.getIdentificationNumber()).append(", ");
        }
        return history.toString();
    }

    public static Task fromString(String value) {
        String[] line = value.split(",");
        switch (line[1]) {
            case "TASK":
                return new Task(line[2], line[4], Integer.parseInt(line[0]), Status.NEW, TypeTask.TASK);
            case "SUBTASK":
                return new Subtask(line[2], line[4], Integer.parseInt(line[0]), Status.NEW, Integer.parseInt(line[5]));
            case "EPIC":
                return new Epic(line[2], line[4], Integer.parseInt(line[0]), Status.NEW);
            default:
                return null;
        }
    }

    public static List<Integer> historyFromString(String value) {
        List<Integer> idTasks = new ArrayList<>();
        String[] ids = value.split(",");
        for (String id : ids) {

            if (id.isBlank()) {
                continue;
            }
            idTasks.add(Integer.parseInt(id.trim()));
        }
        return idTasks;
    }

    private void restoreHistory(List<Integer> ids, List<Task> tasks) {
        HistoryManager historyManager = getHistoryManager();

        for (int id : ids) {
            for (Task task : tasks) {
                if (id == task.getIdentificationNumber()) {
                    switch (task.getTypeTask()) {
                        case EPIC:
                            Epic epic = (Epic) task;
                            historyManager.add(epic);
                            break;
                        case SUBTASK:
                            Subtask subtask = (Subtask) task;
                            historyManager.add(subtask);
                            break;
                    }
                    historyManager.add(task);
                }
            }
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) {

        FileBackedTasksManager fileBackedTasksManager = Managers.getDefault1(file);

        try {
            List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);

            if (lines.size() == 0) {
                return fileBackedTasksManager;
            }

            List<Task> tasksFromFile = new ArrayList<>();

            String stringTask;

            int max = 0;

            for (int i = 1; i < lines.size(); i++) {

                stringTask = lines.get(i);

                if (stringTask.isBlank()) {
                    break;
                }

                Task task = fromString(stringTask);

                tasksFromFile.add(task);

                switch (task.getTypeTask()) {
                    case TASK:
                        fileBackedTasksManager.getTasks().put(task.getIdentificationNumber(), task);
                        break;
                    case SUBTASK:
                        Subtask subtask = (Subtask) task;
                        fileBackedTasksManager.getSubtasks().put(subtask.getIdentificationNumber(), subtask);
                        fileBackedTasksManager.getEpics().get(subtask.getConnectionWithEpic()).getSubtasksEpic().put(subtask.getIdentificationNumber(), subtask);
                        break;
                    case EPIC:
                        fileBackedTasksManager.getEpics().put(task.getIdentificationNumber(), (Epic) task);
                        break;
                }
                if (max < task.getIdentificationNumber()) {
                    max = task.getIdentificationNumber();
                }
            }

            fileBackedTasksManager.setId(max);

            String history = lines.get(lines.size() - 1);

            if (!history.isBlank()) {
                List<Integer> ids = historyFromString(history);
                fileBackedTasksManager.restoreHistory(ids, tasksFromFile);
            }
            return fileBackedTasksManager;

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла.");
        }

    }

    public static void main(String[] args) {
        File f = new File("/home/maksim/IdeaProjects/Tracker/out/Task.csv");
        if (!f.isFile()) {

            try {
                f.createNewFile();
                FileBackedTasksManager taskManager = Managers.getDefault2(f);
                taskManager.createTask("первая", "ывпа");
                taskManager.createTask("вторая", "выап");
                taskManager.createEpic("третий", "ывапр");
                taskManager.createSubtask(3, "четвертая", "ывп");
                taskManager.createSubtask(3, "пятая", "ывп");
                taskManager.createSubtask(3, "шестая", "пвапр");
                taskManager.createEpic("седьмой", "фвап");

                System.out.println(taskManager.getTask(1));
                System.out.println(taskManager.getTask(2));
                System.out.println(taskManager.getEpic(3));
                System.out.println(taskManager.getSubtask(4));
                System.out.println(taskManager.getSubtask(5));
                System.out.println(taskManager.getSubtask(6));
                System.out.println(taskManager.getEpic(7));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else {
            try {
                FileBackedTasksManager fileBackedTasksManager = Managers.getDefault2(f);
                System.out.println(fileBackedTasksManager.getTask(1));
                System.out.println(fileBackedTasksManager.getTask(2));
                fileBackedTasksManager.deleteTask(1);
                fileBackedTasksManager.createTask("Восьмая", "sdfgh");

                FileBackedTasksManager fileBacked = Managers.getDefault2(f);
                System.out.println(fileBacked.getTask(8));

            } catch (ManagerSaveException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
