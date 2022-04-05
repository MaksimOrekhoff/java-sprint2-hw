import java.util.Objects;

public class Subtask extends Task {
    private int connectionWithEpic;

    public Subtask(String name, String description, int identificationNumber, Enum status, int connectionWithEpic) {
        super(name, description, identificationNumber, status);
        this.connectionWithEpic = connectionWithEpic;
    }

    public int getConnectionWithEpic() {
        return connectionWithEpic;
    }

    public void setConnectionWithEpic(int connectionWithEpic) {
        this.connectionWithEpic = connectionWithEpic;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "connectionWithEpic=" + connectionWithEpic +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", identificationNumber=" + identificationNumber +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return connectionWithEpic == subtask.connectionWithEpic;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), connectionWithEpic);
    }
}