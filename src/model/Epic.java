package model;

import controllers.TypeTask;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.LongStream;

public class Epic extends Task {
    private HashMap<Integer, Subtask> subtasksEpic = new HashMap<>();
    private LocalDateTime endTime = null;

    public Epic(String name, String description, int identificationNumber, Enum status, long duration, LocalDateTime startTime) {
        super(name, description, identificationNumber, status, TypeTask.EPIC, duration, startTime);

    }

    public HashMap<Integer, Subtask> getSubtasksEpic() {
        return subtasksEpic;
    }

    public void setSubtasksEpic(HashMap<Integer, Subtask> subtasksEpic) {
        this.subtasksEpic = subtasksEpic;
    }

    @Override
    public LocalDateTime getEndTime() {
        if (subtasksEpic.isEmpty()) {
            endTime = getLocalDateTime().plusMinutes(getDuration());
        } else {
            List<Subtask> sub = new ArrayList<>(subtasksEpic.values());
            sub.sort(Comparator.comparing(Subtask::getLocalDateTime));
            endTime = sub.get(sub.size() - 1).getLocalDateTime().plusMinutes(sub.get(sub.size() - 1).getDuration());
        }
        return endTime;
    }

    @Override
    public LocalDateTime getStartTime() {
        if (subtasksEpic.isEmpty()) {
            setStartTime(getLocalDateTime());
            return getLocalDateTime();
        } else {
            List<Subtask> sub = new ArrayList<>(subtasksEpic.values());
            sub.sort(Comparator.comparing(Subtask::getLocalDateTime));
            setStartTime(sub.get(0).getLocalDateTime());
            return sub.get(0).getLocalDateTime();
        }
    }

    @Override
    public long getDuration() {
        if (subtasksEpic.isEmpty()) {
            return super.getDuration();
        } else {
            List<Subtask> sub = new ArrayList<>(subtasksEpic.values());
            return sub.stream()
                    .mapToLong(subtask -> LongStream.of(subtask.getDuration()).sum())
                    .sum();
        }
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
                ", duration=" + getDuration() +
                ", startTime=" + getStartTime() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtasksEpic, epic.subtasksEpic) && Objects.equals(endTime, epic.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasksEpic, endTime);
    }
}
