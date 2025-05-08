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

    public TimeTableSlot(Weekday weekday, TimeSlot slot, Professor professor, Room room, Group group, Discipline discipline, ActivityType activityType) {
        this.weekday = weekday;
        this.slot = slot;
        this.professor = professor;
        this.room = room;
        this.group = group;
        this.discipline = discipline;
        this.activityType = activityType;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public Weekday getWeekday() {
        return weekday;
    }

    public void setWeekday(Weekday weekday) {
        this.weekday = weekday;
    }

    public TimeSlot getSlot() {
        return slot;
    }

    public void setSlot(TimeSlot slot) {
        this.slot = slot;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeTableSlot that = (TimeTableSlot) o;
        return weekday == that.weekday && Objects.equals(slot, that.slot) && Objects.equals(professor, that.professor) && Objects.equals(room, that.room) && Objects.equals(group, that.group) && Objects.equals(discipline, that.discipline);
    }

    @Override
    public int hashCode() {
        return Objects.hash(weekday, slot, professor, room, group, discipline);
    }
}
