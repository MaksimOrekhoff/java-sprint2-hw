import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected int identificationNumber;
    protected Enum status;

    public Task(String name, String description, int identificationNumber, Enum status) {
        this.name = name;
        this.description = description;
        this.identificationNumber = identificationNumber;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", identificationNumber=" + identificationNumber +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return identificationNumber == task.identificationNumber && Objects.equals(name, task.name) && Objects.equals(description, task.description) && Objects.equals(status, task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, identificationNumber, status);
    }
}
