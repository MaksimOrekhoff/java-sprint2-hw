public class Managers {

    private final InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
    private final InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();

    public  TaskManager getDefault() {
        return inMemoryTaskManager;
    }

    public  HistoryManager getDefaultHistory() {
        return inMemoryHistoryManager;
    }
}
