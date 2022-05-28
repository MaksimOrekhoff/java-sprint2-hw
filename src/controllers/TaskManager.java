package controllers;

import model.Epic;
import model.Subtask;
import model.Task;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;

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

    void createTask(String name, String description, long duration, LocalDateTime localDateTime) throws IllegalAccessException;

    void addTaskInList(Task task) throws IllegalAccessException;


    void createSubtask(int epicId, String name, String description, long duration, LocalDateTime localDateTime) throws IllegalAccessException;

    void addSubtaskInList(Subtask subtask) throws IllegalAccessException;

    void createEpic(String name, String description, long duration, LocalDateTime localDateTime);

    void addEpicInList(Epic epic);


    void deleteTask(int id) throws IOException;

    void deleteEpic(int id) throws IOException;

    void deleteSubtask(int id) throws IOException;

    void updateTask(Task task) throws IllegalAccessException;

    void updateSubtask(Subtask subtask) throws IllegalAccessException;

    void updateEpic(Epic epic);
    void getHistory();


    HashMap getEpics();
    boolean checkEpic(int id);

    HashMap getSubtasks();

    HashMap getTasks();
}
