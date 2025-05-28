package io.github.ClassSyncCSS.ClassSync.Domain;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TimeTableSlotTest {

    private Professor createDummyProfessor() {
        return new Professor("ProfA", ProfType.Course, new HashMap<>());
    }

    private Room createDummyRoom() {
        return new Room("C309", List.of(ActivityType.Course));
    }

    private Group createDummyGroup() {
        Discipline d = new Discipline("Software Quality", null, null);
        Year y = new Year("1", new ArrayList<>());
        return new Group("MISS1", y, List.of(d));
    }

    private Discipline createDummyDiscipline() {
        return new Discipline("EBS", null, null);
    }

    @Test
    void constructorShouldSetAllFieldsCorrectly() {
        TimeTableSlot slot = new TimeTableSlot(
                Weekday.Monday,
                TimeSlot.EightToTen,
                createDummyProfessor(),
                createDummyRoom(),
                createDummyGroup(),
                createDummyDiscipline(),
                ActivityType.Lab
        );

        assertEquals(Weekday.Monday, slot.getWeekday());
        assertEquals(TimeSlot.EightToTen, slot.getSlot());
        assertEquals("ProfA", slot.getProfessor().getName());
        assertEquals("C309", slot.getRoom().getName());
        assertEquals("MISS1", slot.getGroup().getName());
        assertEquals("EBS", slot.getDiscipline().getName());
        assertEquals(ActivityType.Lab, slot.getActivityType());
    }

    @Test
    void settersShouldUpdateFields() {
        TimeTableSlot slot = new TimeTableSlot(
                Weekday.Monday,
                TimeSlot.EightToTen,
                createDummyProfessor(),
                createDummyRoom(),
                createDummyGroup(),
                createDummyDiscipline(),
                ActivityType.Lab
        );

        slot.setActivityType(ActivityType.Course);
        slot.setWeekday(Weekday.Friday);

        assertEquals(ActivityType.Course, slot.getActivityType());
        assertEquals(Weekday.Friday, slot.getWeekday());
    }

    @Test
    void testEqualsAndHashCode() {
        Professor p = createDummyProfessor();
        Room r = createDummyRoom();
        Group g = createDummyGroup();
        Discipline d = createDummyDiscipline();
        TimeSlot t = TimeSlot.EightToTen;

        TimeTableSlot slot1 = new TimeTableSlot(Weekday.Monday, t, p, r, g, d, ActivityType.Lab);
        TimeTableSlot slot2 = new TimeTableSlot(Weekday.Monday, t, p, r, g, d, ActivityType.Course); // same everything except activity type

        assertEquals(slot1, slot2);
        assertEquals(slot1.hashCode(), slot2.hashCode());
    }

    @Test
    void unequalSlotsShouldNotBeEqual() {
        TimeTableSlot slot1 = new TimeTableSlot(Weekday.Monday, TimeSlot.EightToTen, createDummyProfessor(), createDummyRoom(), createDummyGroup(), createDummyDiscipline(), ActivityType.Lab);
        TimeTableSlot slot2 = new TimeTableSlot(Weekday.Tuesday, TimeSlot.TenToTwelve, createDummyProfessor(), createDummyRoom(), createDummyGroup(), createDummyDiscipline(), ActivityType.Lab);

        assertNotEquals(slot1, slot2);
    }

    @Test
    void testToString() {
        TimeTableSlot slot = new TimeTableSlot(
                Weekday.Monday,
                TimeSlot.EightToTen,
                createDummyProfessor(),
                createDummyRoom(),
                createDummyGroup(),
                createDummyDiscipline(),
                ActivityType.Lab
        );

        String expected = "TimeTableSlot{" +
                "weekday=" + slot.getWeekday() +
                ", slot=" + slot.getSlot() +
                ", professor=" + slot.getProfessor() +
                ", room=" + slot.getRoom() +
                ", group=" + slot.getGroup() +
                ", discipline=" + slot.getDiscipline() +
                ", activityType=" + slot.getActivityType() +
                '}';

        assertEquals(expected, slot.toString());
    }

    @Test
    void testSetters() {
        TimeTableSlot slot = new TimeTableSlot(
                Weekday.Monday,
                TimeSlot.EightToTen,
                createDummyProfessor(),
                createDummyRoom(),
                createDummyGroup(),
                createDummyDiscipline(),
                ActivityType.Lab
        );

        slot.setWeekday(Weekday.Wednesday);
        slot.setSlot(TimeSlot.TenToTwelve);
        slot.setActivityType(ActivityType.Course);
        slot.setProfessor(new Professor("ProfB", ProfType.Course, new HashMap<>()));
        slot.setRoom(new Room("C310", List.of(ActivityType.Course)));
        slot.setGroup(new Group("MISS2", new Year("2", new ArrayList<>()), List.of(new Discipline("OOP", null, null))));
        slot.setDiscipline(new Discipline("Data Structures", null, null));

        assertEquals(Weekday.Wednesday, slot.getWeekday());
        assertEquals(TimeSlot.TenToTwelve, slot.getSlot());
        assertEquals(ActivityType.Course, slot.getActivityType());
        assertEquals("ProfB", slot.getProfessor().getName());
        assertEquals("C310", slot.getRoom().getName());
        assertEquals("MISS2", slot.getGroup().getName());
        assertEquals("Data Structures", slot.getDiscipline().getName());
    }
}