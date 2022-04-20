package controllers;

import model.Epic;
import model.Subtask;
import model.Task;

public interface TaskManager {

    void printTask();

    void printEpic();

    void printSubtask();

    void printSubtaskEpic(int id);

    void clearTask();

    void clearEpic();
    void clearSubtask();
    void searchForDeletedSubtasksInEpic();

    Task getTask(int id);

    Subtask getSubtask(int id);

    Epic getEpic(int id);

    void createTask(String name, String description);

    void addTaskInList(Task task);

    void createSubtask(int epicId, String name, String description);

    void addSubtaskInList(Subtask subtask);

    void createEpic(String name, String description);

    void addEpicInList(Epic epic);


    void deleteTask(int id);

    void deleteEpic(int id);

    void deleteSubtask(int id);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic);
    void getHistory();
}
