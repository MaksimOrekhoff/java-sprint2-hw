package model;

import controllers.TypeTask;

import java.util.Objects;

public class Task {

    private final TypeTask typeTask;
    private final String name;
    private final String description;
    private final int identificationNumber;
    private Enum status;

    public Task(String name, String description, int identificationNumber, Enum status, TypeTask typeTask) {
        this.name = name;
        this.description = description;
        this.identificationNumber = identificationNumber;
        this.status = status;
        this.typeTask = typeTask;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Enum getStatus() {
        return status;
    }

    public TypeTask getTypeTask() {
        return typeTask;
    }

    public void setStatus(Enum status) {
        this.status = status;
    }

    public int getIdentificationNumber() {
        return identificationNumber;
    }

    @Override
    public String toString() {
        return "Task{" +
                "typeTask=" + typeTask +
                ", name='" + name +
                ", description='" + description +
                ", identificationNumber=" + identificationNumber +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return identificationNumber == task.identificationNumber && typeTask == task.typeTask && Objects.equals(name, task.name) && Objects.equals(description, task.description) && Objects.equals(status, task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeTask, name, description, identificationNumber, status);
    }
}



