import java.util.Objects;

public class Subtask extends Task {
    private boolean isEpic = false;
    private int connectionWithEpic; // связь с эпиком не у каждого объекта, потому не в конструкторе

    public Subtask(String name, String description, int identificationNumber, String status) {
        super(name, description, identificationNumber, status);
    }

    public int getConnectionWithEpic() {
        return connectionWithEpic;
    }

    public void setConnectionWithEpic(int connectionWithEpic) {
        this.connectionWithEpic = connectionWithEpic;
    }

    public boolean isEpic() {
        return isEpic;
    }

    public void setEpic(boolean epic) {
        isEpic = epic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return isEpic == subtask.isEpic;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), isEpic);
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "isEpic=" + isEpic +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", unique_ID=" + identificationNumber +
                ", status='" + status + '\'' +
                '}';
    }
}