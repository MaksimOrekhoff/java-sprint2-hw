package controllers;

import model.Epic;
import model.Subtask;
import model.Task;

import java.io.File;
import java.io.IOException;

public interface TaskManager {

    void printTask();

    void printEpic();

    void printSubtask();

    void printSubtaskEpic(int id);

    void clearTask();

    void clearEpic();
    void clearSubtask();
    void searchForDeletedSubtasksInEpic();

    Task getTask(int id) throws IOException;

    Subtask getSubtask(int id) throws IOException;

    Epic getEpic(int id) throws IOException;

    void createTask(String name, String description);

    void addTaskInList(Task task);

    void createSubtask(int epicId, String name, String description);

    void addSubtaskInList(Subtask subtask);

    void createEpic(String name, String description);

    void addEpicInList(Epic epic);


    void deleteTask(int id) throws IOException;

    void deleteEpic(int id) throws IOException;

    void deleteSubtask(int id) throws IOException;

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic);
    void getHistory();


}
