import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private int id;
    private final Scanner scanner = new Scanner(System.in);
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    public void getHistory() {
        System.out.println(historyManager.getHistory());
    }

    public List<Task> getHistoryManager() {
        return historyManager.getHistory();
    }

    public int getId() {
        return id;
    }

    public void setId() {
        id++;
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
            if (epic.isSubtask()) epic.getSubtasksEpic().clear();
        }
    }

    public Task getTask(int id) {
        if (tasks.get(id) != null) {
            historyManager.add(tasks.get(id));
            return tasks.get(id);
        }
        return null;
    }

    public Subtask getSubtask(int id) {
        if (subtasks.get(id) != null) {
            historyManager.add(subtasks.get(id));
            return subtasks.get(id);
        }
        return null;
    }

    public Epic getEpic(int id) {
        if (epics.get(id) != null) {
            historyManager.add(epics.get(id));
            return epics.get(id);
        }
        return null;
    }

    @Override
    public Object getTasks() {
        System.out.println("Какой объект вы хотите получить?" + '\n' +
                "1 - Задача" + '\n' +
                "2 - Эпик" + '\n' +
                "3 - Подзадача ");
        int choice = Integer.parseInt(scanner.nextLine().trim());

        System.out.println("Введите номер задачи, которую хотите получить:");
        int id = Integer.parseInt(scanner.nextLine().trim());

        switch (choice) {
            case 1:
                return getTask(id);
            case 2:
                return getEpic(id);
            case 3:
                return getSubtask(id);
            default:
                return null;
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
                createTask();
                break;
            case "2":
                createEpics();
                break;
            case "3":
                System.out.println("Введите ID эпика для создания подзадачи:");
                int id = Integer.parseInt(scanner.nextLine().trim());
                createSubtask(id);
                break;
        }
    }

    @Override
    public void createTask() {
        System.out.println("Введите имя задачи:");
        String name = scanner.nextLine().trim();
        System.out.println("Введите описание задачи:");
        String description = scanner.nextLine().trim();
        setId();
        tasks.put(getId(), new Task(name, description, getId(), Status.NEW));
    }

    @Override
    public Subtask createSubtask(int connect) {
        if (checkEpic(connect)) {
            System.out.println("Введите имя подзадачи:");
            String name = scanner.nextLine().trim();
            System.out.println("Введите описание подзадачи:");
            String description = scanner.nextLine().trim();
            setId();
            subtasks.put(getId(), new Subtask(name, description, getId(), Status.NEW, connect));
            return subtasks.get(getId());
        } else {
            System.out.println("Такого эпика не существует.");
            return null;
        }

    }

    @Override
    public void createEpics() {
        System.out.println("Желаете рабзить на подзадачи?" + '\n' +
                "1 - Да" + '\n' +
                "2 - Нет");
        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1":
                Epic epic = createEpic();
                System.out.println("Введите число подзадач:");
                String numberSubtask = scanner.nextLine().trim();

                for (int i = 0; i < Integer.parseInt(numberSubtask); i++) {
                    epic.getSubtasksEpic().put(getId() + 1, createSubtask(epic.getIdentificationNumber()));
                    epic.getSubtasksEpic().get(getId()).setConnectionWithEpic(epic.getIdentificationNumber());
                }
                epics.get(getId() - Integer.parseInt(numberSubtask)).setSubtask(true);
                break;

            case "2":
                createEpic();
                break;

            default:
                System.out.println("Такое действие отсутствует.");
        }
    }

    @Override
    public Epic createEpic() {
        System.out.println("Введите имя эпика:");
        String name = scanner.nextLine().trim();
        System.out.println("Введите описание эпика:");
        String description = scanner.nextLine().trim();
        setId();
        epics.put(getId(), new Epic(name, description, getId(), Status.NEW));
        return epics.get(getId());
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
        if (subtasks.get(id) != null) {
            Epic epic = epics.get(subtasks.get(id).getConnectionWithEpic());
            epic.getSubtasksEpic().remove(id);
            updateEpic(epic);
            subtasks.remove(id);
        } else {
            System.out.println("Такой подзадачи нет.");
        }
    }

    @Override
    public void updateTasks(Object obj) {
        System.out.println("Какой объект вы хотите обновить?" + '\n' +
                "1 - Задача" + '\n' +
                "2 - Эпик" + '\n' +
                "3 - Подзадача ");

        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1":
                updateTask((Task) obj);
                break;
            case "2":
                updateSubtask((Subtask) obj);
                break;
            case "3":
                updateEpic((Epic) obj);
                break;
        }

    }

    @Override
    public void mainMenu(Object obj) {
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
                    Object object = getTasks();
                    if (object == null) {
                        System.out.println("Такая задача не найдена");
                    } else {
                        System.out.println(object);
                    }
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
            tasks.put(task.getIdentificationNumber(), task);
            System.out.println(task);
        } else {
            System.out.println("Такой задачи не существует.");
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getIdentificationNumber())) {
            System.out.println("Такая подзадача существует. Обновляем данные");
            tasks.put(subtask.getIdentificationNumber(), subtask);
            System.out.println(subtask);
        }

        for (Epic epic : epics.values()) {
            if (epic.getSubtasksEpic().containsKey(subtask.getIdentificationNumber())) {
                System.out.println("Подзадача найдена. Обновляем объект.");
                epic.getSubtasksEpic().put(subtask.getIdentificationNumber(), subtask);
            }
            updateEpic(epic);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (checkEpic(epic.getIdentificationNumber())) {
            if (epics.containsKey(epic.getIdentificationNumber()) && !epic.isSubtask()) {
                System.out.println("Такой эпик существует. Обновляем данные");
                epics.put(epic.getIdentificationNumber(), epic);
                System.out.println("Эпик без подзадач обновлен.");
            } else if (epic.isSubtask()) {
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
                epics.put(epic.getIdentificationNumber(), epic);
                if (countDone == epic.getSubtasksEpic().size()) {
                    epic.setStatus(Status.DONE);
                } else if (countNew == epic.getSubtasksEpic().size()) {
                    epic.setStatus(Status.NEW);
                } else {
                    epic.setStatus(Status.IN_PROGRESS);
                }
                System.out.println("Эпик с подзадачами обновлен.");
            } else {
                System.out.println("Такой эпик не найден.");
            }
        } else {
            System.out.println("Такой эпик не существует.");
        }

    }

    public boolean checkEpic(int id) {
        boolean isEpic = false;
        for (int idT : epics.keySet()) {
            if (idT == id) {
                isEpic = true;
                break;
            }
        }
        return isEpic;
    }

}



