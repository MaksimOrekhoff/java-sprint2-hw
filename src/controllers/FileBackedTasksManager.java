package controllers;

import model.Epic;
import model.Subtask;
import model.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;


public class FileBackedTasksManager extends InMemoryTaskManager {
    private File file;

    public FileBackedTasksManager(HistoryManager historyManager) {
        super(historyManager);
    }

    public void setFile(File file) {
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

    private void restoreHistory(List<Integer> ids, Map<Integer, Task> tasks) {
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
    }


    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = (FileBackedTasksManager) Managers.getDefault();
        fileBackedTasksManager.setFile(file);
        try {
            List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
            if (lines.size() == 0) {
                return fileBackedTasksManager;
            }
            Map<Integer, Task> tasksFromFile = new HashMap<>();
            String stringTask;
            int max = 0;
            for (int i = 1; i < lines.size(); i++) {
                stringTask = lines.get(i);
                if (stringTask.isBlank()) {
                    break;
                }
                Task task = fromString(stringTask);
                tasksFromFile.put(task.getIdentificationNumber(), task);
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
        File f = new File("Task.csv");
        if (!f.isFile()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        FileBackedTasksManager fileBackedTaskManager = (FileBackedTasksManager) Managers.getDefault();
        fileBackedTaskManager = fileBackedTaskManager.loadFromFile(f);
        fileBackedTaskManager.createTask("первая", "ывпа");
        fileBackedTaskManager.createTask("вторая", "выап");
        fileBackedTaskManager.createEpic("третий", "ывапр");
        fileBackedTaskManager.createSubtask(3, "четвертая", "ывп");
        fileBackedTaskManager.createSubtask(3, "пятая", "ывп");
        fileBackedTaskManager.createSubtask(3, "шестая", "пвапр");
        fileBackedTaskManager.createEpic("седьмой", "фвап");

        System.out.println(fileBackedTaskManager.getTask(1));
        System.out.println(fileBackedTaskManager.getTask(2));
        System.out.println(fileBackedTaskManager.getEpic(3));
        System.out.println(fileBackedTaskManager.getSubtask(4));
        System.out.println(fileBackedTaskManager.getSubtask(5));
        System.out.println(fileBackedTaskManager.getSubtask(6));
        System.out.println(fileBackedTaskManager.getEpic(7));

        FileBackedTasksManager fileBackedTask = (FileBackedTasksManager) Managers.getDefault();
        fileBackedTask = fileBackedTask.loadFromFile(f);
        if (fileBackedTask.equals(fileBackedTaskManager)) {
            System.out.println("Congratulation");
        } else {
            System.out.println("Not equals");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileBackedTasksManager that = (FileBackedTasksManager) o;
        return Objects.equals(file, that.file);
    }

    @Override
    public int hashCode() {
        return Objects.hash(file);
    }
}
