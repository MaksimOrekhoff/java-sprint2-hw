public interface TaskManager {

    void mainMenu();

    void printTask();

    void printEpic();

    void printSubtask();

    void printSubtaskEpic();

    void printList();

    void clearAll();

    void searchForDeletedSubtasksInEpic();

    void getTasks();

    void createTasks();

    void createTask(int id);

    void addTaskInList(Task task);

    void createSubtask(int id);

    void addSubtaskInList(Subtask subtask);

    void createEpics(int id);

    void createEpic(int id);

    void addEpicInList(Epic epic);

    void deleteTasks();

    void deleteTask(int id);

    void deleteEpic(int id);

    void deleteSubtask(int id);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic);
}
