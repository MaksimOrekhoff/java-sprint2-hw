package controllers;

import model.Epic;
import model.Subtask;
import model.Task;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    private int id;
    private final Scanner scanner = new Scanner(System.in);
    private final HistoryManager historyManager;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    public void getHistory() {
        System.out.println(historyManager.getHistory());
    }

    public List<Task> getHistoryManager() {
        return historyManager.getHistory();
    }

    public int getId() {
        return ++id;
    }

    @Override
    public void printTask() {
        if (tasks.size() == 0) System.out.println("Список задач пуст.");
        for (Task name : tasks.values()) {
            System.out.println(name.getName());
        }
    }

    @Override
    public void printEpic() {
        if (epics.size() == 0) System.out.println("Список эпиков пуст.");
        for (Epic name : epics.values()) {
            System.out.println(name.getName());
        }
    }

    @Override
    public void printSubtask() {
        if (subtasks.size() == 0) System.out.println("Список подзадач пуст.");
        for (Subtask name : subtasks.values()) {
            System.out.println(name.getName());
        }
    }

    @Override
    public void printSubtaskEpic(int id) {
//        System.out.println("Укажите ID эпика.");
//        String id = scanner.nextLine();

        if (epics.get(id) == null) {
            System.out.println("Подзадачи отсутствуют.");
        } else {
            System.out.println(epics.get(id).getSubtasksEpic());
        }
    }

    @Override
    public void clearTask() {
        tasks.clear();
        System.out.println("Список задач очищен.");
    }

    @Override
    public void clearEpic() {
        epics.clear();
        subtasks.clear();
        System.out.println("Список эпиков очищен.");
    }

    @Override
    public void clearSubtask() {
        subtasks.clear();
        searchForDeletedSubtasksInEpic();
        System.out.println("Список подзадач очищен.");
    }

    @Override
    public void searchForDeletedSubtasksInEpic() {
        for (Epic epic : epics.values()) {
            if (epic.getSubtasksEpic().size() != 0) epic.getSubtasksEpic().clear();
        }
    }

    public Task getTask(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(tasks.get(id));
            return tasks.get(id);
        }
        return null;
    }

    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtasks.get(id));
            return subtasks.get(id);
        }
        return null;
    }

    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epics.get(id));
            return epics.get(id);
        }
        return null;
    }


    @Override
    public void createTask(String name, String description) {
        addTaskInList(new Task(name, description, getId(), Status.NEW));
    }

    @Override
    public void addTaskInList(Task task) {
        tasks.put(task.getIdentificationNumber(), task);
    }

    @Override
    public void createSubtask(int epicId, String name, String description) {
        Epic epic = epics.get(epicId);
        if (epic != null) {
            int idSubtask = getId();
            addSubtaskInList(new Subtask(name, description, idSubtask, Status.NEW, epicId));
            Subtask subtask = subtasks.get(idSubtask);
            epic.getSubtasksEpic().put(idSubtask, subtask);
            updateEpic(epics.get(epicId));
        } else {
            System.out.println("Такого эпика не существует.");
        }
    }

    @Override
    public void addSubtaskInList(Subtask subtask) {
        subtasks.put(subtask.getIdentificationNumber(), subtask);
    }


    @Override
    public void createEpic(String name, String description) {
        addEpicInList(new Epic(name, description, getId(), Status.NEW));
    }

    @Override
    public void addEpicInList(Epic epic) {
        epics.put(epic.getIdentificationNumber(), epic);
    }

    @Override
    public void deleteTask(int id) {
        if (tasks.get(id) != null) {
            historyManager.remove(id);
            tasks.remove(id);
        } else {
            System.out.println("Такой задачи нет.");
        }
    }

    @Override
    public void deleteEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            for (Subtask value : epic.getSubtasksEpic().values()) {
                historyManager.remove(value.getIdentificationNumber());
                subtasks.remove(value.getIdentificationNumber());
            }
            historyManager.remove(id);
            epics.remove(id);
        } else {
            System.out.println("Такого эпика нет.");
        }
    }

    @Override
    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getConnectionWithEpic());
            epic.getSubtasksEpic().remove(id);
            updateEpic(epic);
            historyManager.remove(id);
            subtasks.remove(id);
        } else {
            System.out.println("Такой подзадачи нет.");
        }
    }



    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getIdentificationNumber())) {
            System.out.println("Такая задача существует. Обновляем данные");
            addTaskInList(task);
            System.out.println(task);
        } else {
            System.out.println("Такой задачи не существует.");
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getIdentificationNumber())) {
            System.out.println("Такая подзадача существует. Обновляем данные");
            addSubtaskInList(subtask);
            System.out.println(subtask);
            Epic epic = epics.get(subtask.getConnectionWithEpic());
            epic.getSubtasksEpic().put(subtask.getIdentificationNumber(), subtask);
            updateEpic(epic);
        } else {
            System.out.println("Такой подзадачи не существует.");
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (checkEpic(epic.getIdentificationNumber())) {
            if (epic.getSubtasksEpic().size() == 0) {
                epics.put(epic.getIdentificationNumber(), epic);
                System.out.println("Эпик обновлен.");
            } else if (epic.getSubtasksEpic().size() != 0) {
                int countDone = 0;
                int countNew = 0;

                for (Subtask subtask : epic.getSubtasksEpic().values()) {
                    if (subtask.getStatus() == Status.DONE) {
                        countDone++;
                    }
                    if (subtask.getStatus() == Status.NEW) {
                        countNew++;
                    }
                }

                addEpicInList(epic);

                if (countDone == epic.getSubtasksEpic().size()) {
                    epic.setStatus(Status.DONE);
                } else if (countNew == epic.getSubtasksEpic().size()) {
                    epic.setStatus(Status.NEW);
                } else {
                    epic.setStatus(Status.IN_PROGRESS);
                }
                System.out.println("Эпик обновлен.");
            }
        } else {
            System.out.println("Такой эпик не существует.");
        }
    }

    public boolean checkEpic(int id) {
        return epics.get(id) != null;
    }

}



