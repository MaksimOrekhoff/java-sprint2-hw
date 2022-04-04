
public class Main {
    public static void main(String[] args) {

        TaskManager taskManager = new Managers().getDefault();


        InMemoryTaskManager manager = (InMemoryTaskManager) taskManager;

        manager.setId();
        Task task = new Task("name", "DFSKF", manager.getId(), Status.DONE);
        manager.setId();
        Task task1 = new Task("nam", "DFSKF", manager.getId(), Status.IN_PROGRESS);
        manager.setId();
        Task task2 = new Task("na", "DFSKF", manager.getId(), Status.NEW);

        manager.listTask.put(task.identificationNumber, task);
        manager.listTask.put(task1.identificationNumber, task1);
        manager.listTask.put(task2.identificationNumber, task2);
        manager.setId();

        Epic epic = new Epic("12", "sfdfs", manager.getId(), Status.NEW);

        manager.listEpic.put(manager.getId(), epic);

        manager.mainMenu(manager);

    }
}
