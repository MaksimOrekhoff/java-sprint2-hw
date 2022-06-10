package model;

import controllers.enumeratedtype.Status;
import controllers.enumeratedtype.TypeTask;

import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {
    private final int connectionWithEpic;

    public Subtask(String name, String description, int identificationNumber, Status status, int connectionWithEpic, long duration, LocalDateTime startTime) {
        super(name, description, identificationNumber, status, TypeTask.SUBTASK, duration, startTime);
        this.connectionWithEpic = connectionWithEpic;
    }

    public int getConnectionWithEpic() {
        return connectionWithEpic;
    }

    @Override
    public String toString() {
        return "model.Subtask{" +
                "typeTask=" + getTypeTask() +
                ", name='" + getName() +
                ", description='" + getDescription() +
                ", identificationNumber=" + getIdentificationNumber() +
                ", status=" + getStatus() +
                ", connectionWithEpic=" + connectionWithEpic +
                ", duration=" + getDuration() +
                ", startTime=" + getStartTime() +
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