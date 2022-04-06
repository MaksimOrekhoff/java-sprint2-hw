import java.util.HashMap;
import java.util.Objects;

public class Epic extends Task {
    private HashMap<Integer, Subtask> subtasksEpic = new HashMap<>() ;
    private boolean isSubtask;
    public Epic(String name, String description, int identificationNumber, Enum status) {
        super(name, description, identificationNumber, status);
    }

    public HashMap<Integer, Subtask> getSubtasksEpic() {
        return subtasksEpic;
    }

    public void setSubtasksEpic(HashMap<Integer, Subtask> subtasksEpic) {
        this.subtasksEpic = subtasksEpic;
    }

    public boolean isSubtask() {
        return isSubtask;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", identificationNumber=" + getIdentificationNumber() +
                ", status=" + getStatus() +
                ", subtasksEpic=" + subtasksEpic +
                ", isSubtask=" + isSubtask +
                '}';
    }

    public void setSubtask(boolean subtask) {
        isSubtask = subtask;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return isSubtask == epic.isSubtask && Objects.equals(subtasksEpic, epic.subtasksEpic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasksEpic, isSubtask);
    }
}
