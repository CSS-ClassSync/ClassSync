package io.github.ClassSyncCSS.ClassSync.Domain;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class TimeTableTest {

    private AllData createAllDataWithGroup(Group group) {
        return new AllData(
                new ArrayList<>(),                   // professors
                new ArrayList<>(),                   // disciplines
                new ArrayList<>(),                   // specializations
                List.of(group.getYear()),            // years
                List.of(group),                      // groups
                new ArrayList<>()                    // rooms
        );
    }

//    @Test
//    void testSetDataInitializesClassMaps() {
//        TimeTable timeTable = new TimeTable();
//        Discipline discipline = new Discipline("Software Quality", new ArrayList<>(), new ArrayList<>());
//        Group group = new Group("MISS1", new Year("1", new ArrayList<>()), List.of(discipline));
//        AllData allData = createAllDataWithGroup(group);
//
//        timeTable.setData(allData);
//
//        assertFalse(timeTable.getClassesRemainingByGroup().isEmpty());
//        assertFalse(timeTable.getClassesRemainingByDisipline().isEmpty());
//    }

    @Test
    void testAddDisciplineNoConflicts() {
        TimeTable timeTable = new TimeTable();
        Discipline discipline = new Discipline("Software Quality", new ArrayList<>(), new ArrayList<>());
        Group group = new Group("MISS1", new Year("1", new ArrayList<>()), List.of(discipline));
        AllData allData = createAllDataWithGroup(group);
        timeTable.setData(allData);
        TimeTableSlot slot = new TimeTableSlot(Weekday.Monday, TimeSlot.EightToTen, null, null, group, discipline, ActivityType.Course);

        boolean result = timeTable.addDiscipline(slot);

        assertTrue(result);
    }

    @Test
    void testAddDisciplineWithConflict() {
        TimeTable timeTable = new TimeTable();
        Discipline discipline = new Discipline("Software Quality", new ArrayList<>(), new ArrayList<>());
        Group group = new Group("MISS1", new Year("1", new ArrayList<>()), List.of(discipline));
        AllData allData = createAllDataWithGroup(group);
        timeTable.setData(allData);
        TimeTableSlot slot1 = new TimeTableSlot(Weekday.Monday, TimeSlot.EightToTen, null, null, group, discipline, ActivityType.Course);
        TimeTableSlot slot2 = new TimeTableSlot(Weekday.Monday, TimeSlot.EightToTen, null, null, group, discipline, ActivityType.Lab);

        timeTable.addDiscipline(slot1);
        boolean result = timeTable.addDiscipline(slot2);

        assertFalse(result);
    }

    @Test
    void testRemoveDisciplineExistingSlot() {
        TimeTable timeTable = new TimeTable();

        Professor professor = new Professor("ProfA", ProfType.Course, new HashMap<>());
        Discipline discipline = new Discipline("Software Quality", List.of(professor), new ArrayList<>());
        Group group = new Group("MISS1", new Year("1", new ArrayList<>()), List.of(discipline));
        Room room = new Room("C210", null);
        AllData allData = new AllData(List.of(professor), List.of(discipline), new ArrayList<>(), List.of(group.getYear()), List.of(group), new ArrayList<>());

        timeTable.setData(allData);

        TimeTableSlot slot = new TimeTableSlot(
                Weekday.Monday,
                TimeSlot.EightToTen,
                professor,
                room,
                group,
                discipline,
                ActivityType.Course
        );

        timeTable.addDiscipline(slot);

        boolean removed = timeTable.removeDiscipline(slot);
        assertTrue(removed);
    }


    @Test
    void testCheckAddNoConflicts() {
        TimeTable timeTable = new TimeTable();
        Discipline discipline = new Discipline("Software Quality", new ArrayList<>(), new ArrayList<>());
        Group group = new Group("MISS1", new Year("1", new ArrayList<>()), List.of(discipline));
        AllData allData = createAllDataWithGroup(group);
        timeTable.setData(allData);
        TimeTableSlot slot = new TimeTableSlot(Weekday.Monday, TimeSlot.EightToTen, null, null, group, discipline, ActivityType.Course);

        boolean canAdd = timeTable.checkAdd(slot);
        assertTrue(canAdd);
    }
}
