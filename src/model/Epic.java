package model;

import controllers.TypeTask;

import java.util.HashMap;
import java.util.Objects;

public class Epic extends Task {
    private HashMap<Integer, Subtask> subtasksEpic = new HashMap<>() ;

    public Epic(String name, String description, int identificationNumber, Enum status) {
        super(name, description, identificationNumber, status, TypeTask.EPIC);

    }

    public HashMap<Integer, Subtask> getSubtasksEpic() {
        return subtasksEpic;
    }

    public void setSubtasksEpic(HashMap<Integer, Subtask> subtasksEpic) {
        this.subtasksEpic = subtasksEpic;
    }


    @Override
    public String toString() {
        return "model.Epic{" +
                "typeTask=" + getTypeTask() +
                ", name='" + getName() +
                ", description='" + getDescription() +
                ", identificationNumber=" + getIdentificationNumber() +
                ", status=" + getStatus() +
                ", subtasksEpic=" + subtasksEpic +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtasksEpic, epic.subtasksEpic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasksEpic);
    }
}
