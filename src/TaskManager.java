public interface TaskManager {

    void printTask();

    void printEpic();

    void printSubtask();

    void printSubtaskEpic();

    void printList();

    void clearAll();

    void searchForDeletedSubtasksInEpic();

    Object getTasks();

    void createTasks();

    void createTask();

    Subtask createSubtask(int connect);

    void createEpics();

    Epic createEpic();

    void deleteTasks();

    void deleteTask(int id);

    void deleteEpic(int id);

    void deleteSubtask(int id);

    void updateTasks(Object obj);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpicWhitSubtask(Subtask subtask);

    void updateEpic(Epic epic);
}
