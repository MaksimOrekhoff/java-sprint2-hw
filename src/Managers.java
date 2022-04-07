public class Managers {
    private final static InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
    private final static InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager(inMemoryHistoryManager);

    public static HistoryManager getDefaultHistory() {
        return inMemoryHistoryManager;
    }

    public static TaskManager getDefault() {
        return inMemoryTaskManager;
    }

}
