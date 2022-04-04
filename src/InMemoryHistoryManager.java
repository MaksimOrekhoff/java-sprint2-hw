import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> history = new ArrayList<>();

    public void add(Task task) {
        if (history.size() == 0) {
            history.add(task);
        } else {
            int count = 0;
            for (Task tasks : history) {
                if (task.equals(tasks)) {
                    count++;
                }
            }
            if (count == 0) {
                history.add(task);
            }
        }

    }

    public List<Task> getHistory() {
        return history;
    }

}
