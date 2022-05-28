package model;

import controllers.TypeTask;

import java.time.LocalDateTime;
import java.util.Objects;

public class Task {

    private final TypeTask typeTask;
    private String name;
    private final String description;
    private final int identificationNumber;
    private Enum status;
    private long duration;
    private LocalDateTime startTime;

    public Task(String name, String description, int identificationNumber, Enum status, TypeTask typeTask, long duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.identificationNumber = identificationNumber;
        this.status = status;
        this.typeTask = typeTask;
        this.duration = duration;
        this.startTime = startTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Enum getStatus() {
        return status;
    }

    public LocalDateTime getLocalDateTime() {
        return startTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.startTime = localDateTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(duration);
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
                ", duration=" + duration +
                ", startTime=" + startTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return identificationNumber == task.identificationNumber && duration == task.duration && typeTask == task.typeTask && Objects.equals(name, task.name) && Objects.equals(description, task.description) && Objects.equals(status, task.status) && Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeTask, name, description, identificationNumber, status, duration, startTime);
    }
}



