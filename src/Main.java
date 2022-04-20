import controllers.Managers;
import controllers.TaskManager;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        taskManager.createTask("первая", "ывпа");
        taskManager.createTask("вторая", "выап");
        taskManager.createEpic("третий", "ывапр");
        taskManager.createSubtask(3, "четвертая", "ывп");
        taskManager.createSubtask(3,"пятая", "ывп");
        taskManager.createSubtask(3, "шестая", "пвапр");
        taskManager.createEpic("седьмой", "фвап");

        System.out.println(taskManager.getTask(1));

        System.out.println(taskManager.getTask(2));

        System.out.println(taskManager.getEpic(3));

        System.out.println(taskManager.getSubtask(4));

        System.out.println(taskManager.getSubtask(5));

        System.out.println(taskManager.getSubtask(6));

        System.out.println(taskManager.getEpic(7));

        taskManager.getHistory();

        System.out.println(taskManager.getTask(1));

        System.out.println(taskManager.getEpic(3));

        System.out.println(taskManager.getSubtask(5));

        taskManager.getHistory();

        taskManager.deleteSubtask(4);

        taskManager.getHistory();

        taskManager.deleteEpic(3);

        taskManager.getHistory();

        taskManager.deleteTask(2);

        taskManager.getHistory();
    }
}
