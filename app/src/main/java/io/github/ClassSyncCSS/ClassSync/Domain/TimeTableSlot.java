package io.github.ClassSyncCSS.ClassSync.Domain;

import java.util.Objects;

public class TimeTableSlot {
    private Weekday weekday;
    private TimeSlot slot;
    private Professor professor;
    private Room room;
    private Group group;
    private Discipline discipline;
    private ActivityType activityType;

    // Class invariant
    private boolean classInvariant() {
        return weekday != null &&
               slot != null &&
               professor != null &&
               room != null &&
               group != null &&
               discipline != null &&
               activityType != null;
    }

    public TimeTableSlot(Weekday weekday, TimeSlot slot, Professor professor, Room room, Group group, Discipline discipline, ActivityType activityType) {
        // Preconditions
        assert weekday != null : "Precondition failed: weekday must not be null";
        assert slot != null : "Precondition failed: time slot must not be null";
        assert professor != null : "Precondition failed: professor must not be null";
        assert room != null : "Precondition failed: room must not be null";
        assert group != null : "Precondition failed: group must not be null";
        assert discipline != null : "Precondition failed: discipline must not be null";
        assert activityType != null : "Precondition failed: activityType must not be null";

        this.weekday = weekday;
        this.slot = slot;
        this.professor = professor;
        this.room = room;
        this.group = group;
        this.discipline = discipline;
        this.activityType = activityType;

        // Postcondition
        assert classInvariant() : "Invariant failed after constructor";
    }

    public Weekday getWeekday() {
        assert classInvariant() : "Invariant failed before getWeekday";
        return weekday;
    }

    public void setWeekday(Weekday weekday) {
        assert weekday != null : "Precondition failed: weekday must not be null";
        this.weekday = weekday;
        assert classInvariant() : "Invariant failed after setWeekday";
    }

    public TimeSlot getSlot() {
        assert classInvariant() : "Invariant failed before getSlot";
        return slot;
    }

    public void setSlot(TimeSlot slot) {
        assert slot != null : "Precondition failed: slot must not be null";
        this.slot = slot;
        assert classInvariant() : "Invariant failed after setSlot";
    }

    public Professor getProfessor() {
        assert classInvariant() : "Invariant failed before getProfessor";
        return professor;
    }

    public void setProfessor(Professor professor) {
        assert professor != null : "Precondition failed: professor must not be null";
        this.professor = professor;
        assert classInvariant() : "Invariant failed after setProfessor";
    }

    public Room getRoom() {
        assert classInvariant() : "Invariant failed before getRoom";
        return room;
    }

    public void setRoom(Room room) {
        assert room != null : "Precondition failed: room must not be null";
        this.room = room;
        assert classInvariant() : "Invariant failed after setRoom";
    }

    public Group getGroup() {
        assert classInvariant() : "Invariant failed before getGroup";
        return group;
    }

    public void setGroup(Group group) {
        assert group != null : "Precondition failed: group must not be null";
        this.group = group;
        assert classInvariant() : "Invariant failed after setGroup";
    }

    public Discipline getDiscipline() {
        assert classInvariant() : "Invariant failed before getDiscipline";
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        assert discipline != null : "Precondition failed: discipline must not be null";
        this.discipline = discipline;
        assert classInvariant() : "Invariant failed after setDiscipline";
    }

    public ActivityType getActivityType() {
        assert classInvariant() : "Invariant failed before getActivityType";
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        assert activityType != null : "Precondition failed: activityType must not be null";
        this.activityType = activityType;
        assert classInvariant() : "Invariant failed after setActivityType";
    }

    @Override
    public String toString() {
        assert classInvariant() : "Invariant failed before toString";
        return "TimeTableSlot{" +
                "weekday=" + weekday +
                ", slot=" + slot +
                ", professor=" + professor +
                ", room=" + room +
                ", group=" + group +
                ", discipline=" + discipline +
                ", activityType=" + activityType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        assert classInvariant() : "Invariant failed before equals";
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeTableSlot that = (TimeTableSlot) o;
        return weekday == that.weekday &&
               Objects.equals(slot, that.slot) &&
               Objects.equals(professor, that.professor) &&
               Objects.equals(room, that.room) &&
               Objects.equals(group, that.group) &&
               Objects.equals(discipline, that.discipline);
    }

    @Override
    public int hashCode() {
        assert classInvariant() : "Invariant failed before hashCode";
        return Objects.hash(weekday, slot, professor, room, group, discipline);
    }
}
