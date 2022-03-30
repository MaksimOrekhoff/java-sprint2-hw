import java.util.HashMap;
import java.util.Scanner;

public class Manager {
    private HashMap<Integer, Task> listTask = new HashMap<>();
    private HashMap<Integer, Subtask> listSubtask = new HashMap<>();
    private HashMap<Integer, Epic> listEpic = new HashMap<>();
    private int id = 0;
    private final  Scanner scanner = new Scanner(System.in);

    public int getId() {
        return id;
    }

    public void setID() {
        id++;
    }

    private void printTask() {
        if (listTask.size() == 0) System.out.println("Список задач пуст.");
        for (Task name : listTask.values()) {
            System.out.println(name.name);
        }
    }

    private void printEpic() {
        if (listEpic.size() == 0) System.out.println("Список эпиков пуст.");
        for (Epic name : listEpic.values()) {
            System.out.println(name.name);
        }
    }

    private void printSubtask() {
        if (listSubtask.size() == 0) System.out.println("Список подзадач пуст.");
        for (Subtask name : listSubtask.values()) {
            System.out.println(name.name);
        }
    }

    private void printSubtaskEpic() {
        System.out.println("Укажите ID эпика.");
        String id = scanner.nextLine();

        if (listEpic.get(Integer.parseInt(id)) == null) {
            System.out.println("Подзадачи отсутствуют.");
        } else {
            System.out.println(listEpic.get(Integer.parseInt(id)).subtasksEpic);
        }
    }

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

    public void clearAll() {
        System.out.println("Какой список задач вы хотите удалить?" + '\n' +
                "1 - Задачи" + '\n' +
                "2 - Эпики" + '\n' +
                "3 - Подзадачи");

        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1":
                listTask.clear();
                System.out.println("Список задач очищен.");
                break;
            case "2":
                listEpic.clear();
                System.out.println("Список эпиков очищен.");
                break;
            case "3":
                listSubtask.clear();
                searchForDeletedSubtasksInEpic();
                System.out.println("Список подзадач очищен.");
        }
    }

    private void searchForDeletedSubtasksInEpic() {
        for (Epic epic : listEpic.values()) {
            if (epic.isSubtask()) listEpic.remove(epic.identificationNumber);
        }
    }

    public Object getTask() {
        System.out.println("Введите номер задачи, которую хотите получить:");
        int id = Integer.parseInt(scanner.nextLine().trim());

        for (int idT : listTask.keySet()) {
            if (idT == id) {
                return listTask.get(idT);
            }
        }

        for (int idT : listEpic.keySet()) {
            if (idT == id) {
                return listEpic.get(idT);
            }
        }

        for (int idT : listSubtask.keySet()) {
            if (idT == id) {
                return listSubtask.get(idT);
            }
        }

        return "Такая задача не найдена";
    }

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
                createSubtask();
                break;
        }
    }

    private void createTask() {
        System.out.println("Введите имя задачи:");
        String name = scanner.nextLine().trim();
        System.out.println("Введите описание задачи:");
        String description = scanner.nextLine().trim();
        setID();
        listTask.put(getId(), new Task(name, description, getId(), "NEW"));
    }


    private Subtask createSubtask() {
        System.out.println("Введите имя подзадачи:");
        String name = scanner.nextLine().trim();
        System.out.println("Введите описание подзадачи:");
        String description = scanner.nextLine().trim();
        setID();
        listSubtask.put(getId(), new Subtask(name, description, getId(), "NEW"));
        return listSubtask.get(getId());
    }

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
                    epic.subtasksEpic.put(getId() + 1, createSubtask());
                    epic.subtasksEpic.get(getId()).setConnectionWithEpic(epic.identificationNumber);
                    listSubtask.get(getId()).setEpic(true);
                }
                listEpic.get(getId() - Integer.parseInt(numberSubtask)).setSubtask(true);
                break;

            case "2":
                createEpic();
                break;

            default:
                System.out.println("Такое действие отсутствует.");
        }
    }

    private Epic createEpic() {
        System.out.println("Введите имя эпика:");
        String name = scanner.nextLine().trim();
        System.out.println("Введите описание эпика:");
        String description = scanner.nextLine().trim();
        setID();
        listEpic.put(getId(), new Epic(name, description, getId(), "NEW"));
        return listEpic.get(getId());
    }

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

    private void deleteTask(int id) {
        if (listTask.get(id) != null) {
            listTask.remove(id);
        } else {
            System.out.println("Такой задачи нет.");
        }

    }

    private void deleteEpic(int id) {
        if (listEpic.get(id) != null) {

            listEpic.remove(id);
        } else {
            System.out.println("Такого эпика нет.");
        }

    }

    private void deleteSubtask(int id) {
        if (listSubtask.get(id) != null) {
            for (Epic epic : listEpic.values()) {
                epic.subtasksEpic.remove(id);
                updateEpic(epic);
            }
            listSubtask.remove(id);
        } else {
            System.out.println("Такой подзадачи нет.");
        }
    }

    private void updateTasks(Object obj) {
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
                    System.out.println(getTask());
                    break;
                case "4":
                    createTasks();
                    break;
                case "5":
                    updateTasks(obj);
                    break;
                case "6":
                    deleteTasks();
                    break;
                case "7":
                    printSubtaskEpic();
                    break;
                default:
                    if (choice.equals("8")) {
                        break;
                    } else {
                        System.out.println("Такой команды нет. Введите заново.");
                    }
            }
        } while (!choice.equals("8"));
    }

    private void printMenu() {
        System.out.println("Что вы хотите сделать?" + '\n' +
                "1 - Получить список всех задач." + '\n' +
                "2 - Удалить все задачи" + '\n' +
                "3 - Получить задачу по идентификатору." + '\n' +
                "4 - Создать новый объект." + '\n' +
                "5 - Обновить данные о задаче." + '\n' +
                "6 - Удалить задачу." + '\n' +
                "7 - Посмотреть список подзадач эпика." + '\n' +
                "8 - Выход.");
    }

    private void updateTask(Task task) {
        if (listTask.containsKey(task.identificationNumber)) {  //получаем новый объект Task и проверяем в списке
            System.out.println("Такая задача существует. Обновляем данные"); //есть совпадение
            listTask.put(task.identificationNumber, task); // обновляем его в списке задач
            System.out.println(task);
        } else {
            System.out.println("Такой задачи не существует.");
        }
    }

    private void updateSubtask(Subtask subtask) {
        if (listSubtask.containsKey(subtask.identificationNumber)) { // получаем новый объект Subtask и проверяем в списке
            System.out.println("Такая подзадача существует. Обновляем данные"); //есть совпадение
            listTask.put(subtask.identificationNumber, subtask);// обновляем его в списке подзадач
            System.out.println(subtask);
        }

        if (subtask.isEpic()) {
            updateEpicWhitSubtask(subtask); //обновляем его состояние в списке Эпика
        }
    }

    private void updateEpicWhitSubtask(Subtask subtask) {
        for (Epic epic : listEpic.values()) { //проходим по списку всех Эпиков
            if (epic.subtasksEpic.containsKey(subtask.identificationNumber)) { //находим нужный
                System.out.println("Подзадача найдена. Обновляем объект.");
                epic.subtasksEpic.put(subtask.identificationNumber, subtask); //и заменяем на новый Subtask
            }
            updateEpic(epic); //проверяем после этого состояние Эпика
        }
    }

    private void updateEpic(Epic epic) {
        if (listEpic.containsKey(epic.identificationNumber) && !epic.isSubtask()) { //обновление эпика не имеющего подзадач
            System.out.println("Такой эпик существует. Обновляем данные");
            listEpic.put(epic.identificationNumber, epic); // обновляем Эпик
            System.out.println("Эпик без подзадач обновлен.");
        } else if (epic.isSubtask()) { //обновление эпика с подзадачами
            int countDone = 0;
            int countNew = 0;

            for (Subtask subtask : epic.subtasksEpic.values()) {
                if (subtask.status.equals("DONE")) {
                    countDone++;
                }
                if (subtask.status.equals("NEW")) {
                    countNew++;
                }
            }
            epic.subtasksEpic = listEpic.get(epic.identificationNumber).subtasksEpic;//передаем подзадачи новому Эпик
            listEpic.put(epic.identificationNumber, epic); // эпик обновлен
            if (countDone == epic.subtasksEpic.size()) {
                epic.status = "DONE";
            } else if (countNew == epic.subtasksEpic.size()) {
                epic.status = "NEW";
            } else {
                epic.status = "IN_PROGRESS";
            }
            System.out.println("Эпик с подзадачами обновлен.");
        } else {
            System.out.println("Такой эпик не найден.");
        }
    }

}



