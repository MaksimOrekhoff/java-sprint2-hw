package controllers;

import model.Epic;
import model.Subtask;
import model.Task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HistoryManager historyManager;
    private int id;
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private final Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getLocalDateTime));

    public Set<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void getHistory() {
        System.out.println(historyManager.getHistory());
    }

    public List<Task> getHistoryManagers() {
        return historyManager.getHistory();
    }

    public int getId() {
        return ++id;
    }

    private void addTaskInPrioritizedTasks(Task task) throws IllegalAccessException {
        if (checkTasksIntersection(task)) {

            throw new IllegalAccessException("Пересечение задач во времени.");
        }
        if (task.getStartTime() == null && prioritizedTasks.size() == 0) {
            task.setStartTime(LocalDateTime.of(2023, 5, 28, 23, 59));
            prioritizedTasks.add(task);
        } else if (task.getStartTime() == null) {
            List<Task> tasks = new ArrayList<>(prioritizedTasks);
            task.setStartTime(tasks.get(tasks.size() - 1).getEndTime().plusHours(12));
            prioritizedTasks.add(task);
        }
        prioritizedTasks.add(task);
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
        if (epics.get(id) == null) {
            System.out.println("Подзадачи отсутствуют.");
        } else {
            System.out.println(epics.get(id).getSubtasksEpic());
        }
    }

    @Override
    public void clearTask() {
        tasks.clear();
        prioritizedTasks.removeIf(task -> task.getTypeTask().equals(TypeTask.TASK));
        System.out.println("Список задач очищен.");
    }

    @Override
    public void clearEpic() {
        subtasks.clear();
        prioritizedTasks.removeIf(subtask -> subtask.getTypeTask().equals(TypeTask.SUBTASK));
        epics.clear();
        System.out.println("Список эпиков очищен.");
    }

    @Override
    public void clearSubtask() {
        subtasks.clear();
        searchForDeletedSubtasksInEpic();
        prioritizedTasks.removeIf(subtask -> subtask.getTypeTask().equals(TypeTask.SUBTASK));
        System.out.println("Список подзадач очищен.");
    }

    @Override
    public void searchForDeletedSubtasksInEpic() {
        for (Epic epic : epics.values()) {
            if (epic.getSubtasksEpic().size() != 0) {

                for (Subtask sub : epic.getSubtasksEpic().values()) {
                    prioritizedTasks.remove(sub);
                }

                epic.getSubtasksEpic().clear();
            }

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
    public void createTask(String name, String description, long duration, LocalDateTime localDateTime) throws IllegalAccessException {
        addTaskInList(new Task(name, description, getId(), Status.NEW, TypeTask.TASK, duration, localDateTime));
    }

    @Override
    public void addTaskInList(Task task) throws IllegalAccessException {
        tasks.put(task.getIdentificationNumber(), task);
        addTaskInPrioritizedTasks(task);
    }

    @Override
    public void createSubtask(int epicId, String name, String description, long duration, LocalDateTime localDateTime) throws IllegalAccessException {
        Epic epic = epics.get(epicId);
        if (epic != null) {
            int idSubtask = getId();
            addSubtaskInList(new Subtask(name, description, idSubtask, Status.NEW, epicId, duration, localDateTime));
            Subtask subtask = subtasks.get(idSubtask);
            epic.getSubtasksEpic().put(idSubtask, subtask);
            updateEpic(epics.get(epicId));
        } else {
            System.out.println("Такого эпика не существует.");
        }
    }

    @Override
    public void addSubtaskInList(Subtask subtask) throws IllegalAccessException {
        subtasks.put(subtask.getIdentificationNumber(), subtask);
        addTaskInPrioritizedTasks(subtask);
    }


    @Override
    public void createEpic(String name, String description, long duration, LocalDateTime localDateTime) {
        addEpicInList(new Epic(name, description, getId(), Status.NEW, duration, localDateTime));
    }

    @Override
    public void addEpicInList(Epic epic) {
        epics.put(epic.getIdentificationNumber(), epic);
    }

    @Override
    public void deleteTask(int id) {
        if (tasks.get(id) != null) {
            historyManager.remove(id);
            prioritizedTasks.remove(tasks.get(id));
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
                prioritizedTasks.remove(value);
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
            prioritizedTasks.remove(subtask);
            historyManager.remove(id);
            subtasks.remove(id);
        } else {
            System.out.println("Такой подзадачи нет.");
        }
    }

    @Override
    public void updateTask(Task task) throws IllegalAccessException {
        if (tasks.containsKey(task.getIdentificationNumber())) {
            System.out.println("Такая задача существует. Обновляем данные");
            addTaskInList(task);

            addTaskInPrioritizedTasks(task);

            System.out.println(task);
        } else {
            System.out.println("Такой задачи не существует.");
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) throws IllegalAccessException {
        if (subtasks.containsKey(subtask.getIdentificationNumber())) {
            System.out.println("Такая подзадача существует. Обновляем данные");
            addSubtaskInList(subtask);
            System.out.println(subtask);

            addTaskInPrioritizedTasks(subtask);

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

    public String getLineData(LocalDateTime localDateTime) {
        return localDateTime.format(formatter);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InMemoryTaskManager that = (InMemoryTaskManager) o;
        return id == that.id && Objects.equals(tasks, that.tasks) && Objects.equals(subtasks, that.subtasks) && Objects.equals(epics, that.epics) && Objects.equals(historyManager, that.historyManager);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tasks, subtasks, epics, historyManager, id);
    }

    private boolean checkTasksIntersection(Task newTask) {

        LocalDateTime startTime = newTask.getStartTime();
        LocalDateTime endTime = newTask.getEndTime();
        for (Task task : prioritizedTasks) {
            if (newTask.getIdentificationNumber() == task.getIdentificationNumber()) {
                prioritizedTasks.remove(task);
                return false;
            }
            return conditionIntersection(startTime, endTime, task.getStartTime(), task.getEndTime());

        }
        return false;
    }

    public boolean conditionIntersection(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
        return start1.isBefore(end2) && start1.isAfter(start2)
                || end1.isBefore(end2) && end1.isAfter(start2)
                || start1.equals(start2) || start1.equals(end2)
                || end1.equals(start2) || end1.equals(end2)
                || start1.isBefore(start2) && end1.isAfter(start2);
    }
}



