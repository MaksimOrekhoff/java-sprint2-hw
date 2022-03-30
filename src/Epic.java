import java.util.HashMap;
import java.util.Objects;

public class Epic extends Task {
    HashMap<Integer, Subtask> subtasksEpic = new HashMap<>() ;
    private boolean isSubtask = false;
    public Epic(String name, String description, int unique_ID, String status) {
        super(name, description, unique_ID, status);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "listSubtask=" + subtasksEpic +
                ", isSubtask=" + isSubtask +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", unique_ID=" + identificationNumber +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return isSubtask == epic.isSubtask && Objects.equals(subtasksEpic, epic.subtasksEpic);
    }

    public boolean isSubtask() {
        return isSubtask;
    }

    public void setSubtask(boolean subtask) {
        isSubtask = subtask;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasksEpic, isSubtask);
    }
}