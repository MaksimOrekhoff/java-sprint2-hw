import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
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
    public void printSubtaskEpic() {
        System.out.println("Укажите ID эпика.");
        String id = scanner.nextLine();

        if (epics.get(Integer.parseInt(id)) == null) {
            System.out.println("Подзадачи отсутствуют.");
        } else {
            System.out.println(epics.get(Integer.parseInt(id)).getSubtasksEpic());
        }
    }

    @Override
    public void printList() {
        System.out.println("Какой список хотите просмотреть?" + '\n' +
                "1 - Задачи" + '\n' +
                "2 - Эпики" + '\n' +
                "3 - Подзадачи" + '\n' +
                "4 - Подзадачи эпика");
        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1":
                printTask();
                break;
            case "2":
                printEpic();
                break;
            case "3":
                printSubtask();
                break;
            case "4":
                printSubtaskEpic();
                break;
            default:
                System.out.println("Не верный ввод.");
        }
    }

    @Override
    public void clearAll() {
        System.out.println("Какой список задач вы хотите удалить?" + '\n' +
                "1 - Задачи" + '\n' +
                "2 - Эпики" + '\n' +
                "3 - Подзадачи");

        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1":
                tasks.clear();
                System.out.println("Список задач очищен.");
                break;
            case "2":
                epics.clear();
                subtasks.clear();
                System.out.println("Список эпиков очищен.");
                break;
            case "3":
                subtasks.clear();
                searchForDeletedSubtasksInEpic();
                System.out.println("Список подзадач очищен.");
        }
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
    public void getTasks() {
        System.out.println("Какой объект вы хотите получить?" + '\n' +
                "1 - Задача" + '\n' +
                "2 - Эпик" + '\n' +
                "3 - Подзадача ");
        int choice = Integer.parseInt(scanner.nextLine().trim());

        System.out.println("Введите номер задачи, которую хотите получить:");
        int id = Integer.parseInt(scanner.nextLine().trim());

        switch (choice) {
            case 1:
                Task task = getTask(id);
                if (task == null) {
                    System.out.println("Такая задача не найдена");
                } else {
                    System.out.println(task);
                }
                break;
            case 2:
                Epic epic = getEpic(id);
                if (epic == null) {
                    System.out.println("Такая задача не найдена");
                } else {
                    System.out.println(epic);
                }
                break;
            case 3:
                Subtask subtask = getSubtask(id);
                if (subtask == null) {
                    System.out.println("Такая задача не найдена");
                } else {
                    System.out.println(subtask);
                }
                break;
            default:
                System.out.println("Такой команды нет.");
        }
    }

    @Override
    public void createTasks() {
        System.out.println("Какой объект вы хотите создать?" + '\n' +
                "1 - Задача" + '\n' +
                "2 - Эпик" + '\n' +
                "3 - Подзадача ");
        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1":
                createTask(getId());
                break;
            case "2":
                createEpics(getId());
                break;
            case "3":
                System.out.println("Введите ID эпика для создания подзадачи:");
                int id = Integer.parseInt(scanner.nextLine().trim());
                createSubtask(id);
                break;
        }
    }

    @Override
    public void createTask(int id) {
        System.out.println("Введите имя задачи:");
        String name = scanner.nextLine().trim();
        System.out.println("Введите описание задачи:");
        String description = scanner.nextLine().trim();
        addTaskInList(new Task(name, description, id, Status.NEW));
    }

    @Override
    public void addTaskInList(Task task) {
        tasks.put(task.getIdentificationNumber(), task);
    }

    @Override
    public void createSubtask(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic != null) {
            System.out.println("Введите имя подзадачи:");
            String name = scanner.nextLine().trim();
            System.out.println("Введите описание подзадачи:");
            String description = scanner.nextLine().trim();
            int idSubtask = getId();
            addSubtaskInList(new Subtask(name, description, idSubtask, Status.NEW, epicId));
            Subtask subtask = subtasks.get(idSubtask);
            epic.getSubtasksEpic().put(idSubtask, subtask);
        } else {
            System.out.println("Такого эпика не существует.");
        }
    }

    @Override
    public void addSubtaskInList(Subtask subtask) {
        subtasks.put(subtask.getIdentificationNumber(), subtask);
    }

    @Override
    public void createEpics(int id) {
        System.out.println("Желаете рабзить на подзадачи?" + '\n' +
                "1 - Да" + '\n' +
                "2 - Нет");
        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1":
                createEpic(id);
                System.out.println("Введите число подзадач:");
                String numberSubtask = scanner.nextLine().trim();

                for (int i = 0; i < Integer.parseInt(numberSubtask); i++) {
                    createSubtask(id);
                }
                updateEpic(epics.get(id));
                break;
            case "2":
                createEpic(id);
                break;
            default:
                System.out.println("Такое действие отсутствует.");
        }
    }

    @Override
    public void createEpic(int id) {
        System.out.println("Введите имя эпика:");
        String name = scanner.nextLine().trim();
        System.out.println("Введите описание эпика:");
        String description = scanner.nextLine().trim();
        addEpicInList(new Epic(name, description, id, Status.NEW));
    }

    @Override
    public void addEpicInList(Epic epic) {
        epics.put(epic.getIdentificationNumber(), epic);
    }

    @Override
    public void deleteTasks() {
        System.out.println("Что вы хотите удалить?" + '\n' +
                "1 - Задача" + '\n' +
                "2 - Эпик" + '\n' +
                "3 - Подзадача");
        String choice = scanner.nextLine().trim();

        System.out.println("Введите ID задачи.");
        int id = Integer.parseInt(scanner.nextLine().trim());

        switch (choice) {
            case "1":
                deleteTask(id);
                break;
            case "2":
                deleteEpic(id);
                break;
            case "3":
                deleteSubtask(id);
                break;
            default:
                System.out.println("Не верный ввод.");
        }
    }

    @Override
    public void deleteTask(int id) {
        if (tasks.get(id) != null) {
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
                subtasks.remove(value.getIdentificationNumber());
            }
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
            subtasks.remove(id);
        } else {
            System.out.println("Такой подзадачи нет.");
        }
    }

    @Override
    public void mainMenu() {
        String choice;
        do {
            printMenu();
            choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    printList();
                    break;
                case "2":
                    clearAll();
                    break;
                case "3":
                    getTasks();
                    break;
                case "4":
                    createTasks();
                    break;
                case "5":
                    deleteTasks();
                    break;
                case "6":
                    printSubtaskEpic();
                    break;
                case "7":
                    getHistory();
                    break;
                case "8":
                    scanner.close();
                    return;
                default:
                    System.out.println("Такой команды нет. Введите заново.");
            }
        } while (true);
    }

    private void printMenu() {
        System.out.println("Что вы хотите сделать?" + '\n' +
                "1 - Получить список всех задач." + '\n' +
                "2 - Удалить все задачи" + '\n' +
                "3 - Получить задачу по идентификатору." + '\n' +
                "4 - Создать новый объект." + '\n' +
                "5 - Удалить задачу." + '\n' +
                "6 - Посмотреть список подзадач эпика." + '\n' +
                "7 - Список последних просмотренных задач." + '\n' +
                "8 - Выход.");
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



