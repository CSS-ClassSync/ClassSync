package io.github.ClassSyncCSS.ClassSync.UI.Controls;

import io.github.ClassSyncCSS.ClassSync.Domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CalendarTest {

    private Calendar calendar;
    private Filters filters;
    private SidePane sidePane;
    private TimeTable timeTable;
    private Group group;
    private Professor professor;
    private Discipline discipline;
    private Room room;
    private TimeTableSlot slot;
    private AllData allData;

    @BeforeEach
    public void setup() {
        // Mocks
        filters = mock(Filters.class);
        sidePane = new SidePane();
        timeTable = mock(TimeTable.class);
        group = mock(Group.class);
        professor = mock(Professor.class);
        discipline = mock(Discipline.class);
        room = mock(Room.class);
        slot = mock(TimeTableSlot.class);
        when(slot.getWeekday()).thenReturn(Weekday.Monday);
        when(slot.getSlot()).thenReturn(TimeSlot.EightToTen);
        when(slot.getGroup()).thenReturn(group);
        when(slot.getProfessor()).thenReturn(professor);
        when(slot.getDiscipline()).thenReturn(discipline);
        when(slot.getRoom()).thenReturn(room);
        when(slot.getActivityType()).thenReturn(ActivityType.Course);

        allData = mock(AllData.class);

        // Mock all data
        when(group.getName()).thenReturn("Group A");
        when(professor.getName()).thenReturn("Prof A");
        when(discipline.getName()).thenReturn("Discipline A");
        when(room.getName()).thenReturn("Room A");


        // Calendar with injected mocks
        calendar = new Calendar();
        calendar.setSidePaneRef(sidePane);
        calendar.setFiltersRef(filters);

        // Inject mock timetable
        calendar.fullTimeTable = timeTable;
        calendar.setData(allData);
    }

    @Test
    public void testUpdateFiltersWithGroup() {
        when(filters.getSelectedGroup()).thenReturn(group);
        when(filters.getSelectedProfessor()).thenReturn(null);
        when(filters.getSelectedDiscipline()).thenReturn(null);
        when(filters.getSelectedRoom()).thenReturn(null);

        List<TimeTableSlot> groupSlots = List.of(slot);
        Map<Weekday, List<TimeTableSlot>> schedule = new EnumMap<>(Weekday.class);
        schedule.put(Weekday.Monday, groupSlots);

        when(timeTable.getClassesRemainingByGroup(group)).thenReturn(groupSlots);
        when(timeTable.getScheduleByGroup(group)).thenReturn(schedule);

        calendar.updateFilters();

        // SidePane should be updated
        assertEquals(groupSlots, sidePane.slots);

        // ScheduleData should have the slot in the correct place
        boolean found = false;
        for (TimeTableSlot[] row : calendar.scheduleData) {
            for (TimeTableSlot s : row) {
                if (s == slot) found = true;
            }
        }
        assertTrue(found, "Slot should be present in scheduleData");
    }

    @Test
    public void testUpdateFiltersWithNoFiltersResetsScheduleData() {
        when(filters.getSelectedGroup()).thenReturn(null);
        when(filters.getSelectedProfessor()).thenReturn(null);
        when(filters.getSelectedDiscipline()).thenReturn(null);
        when(filters.getSelectedRoom()).thenReturn(null);

        // Fill scheduleData with dummy slot
        for (int i = 0; i < calendar.scheduleData.length; i++) {
            for (int j = 0; j < calendar.scheduleData[i].length; j++) {
                calendar.scheduleData[i][j] = slot;
            }
        }

        calendar.updateFilters();

        // All scheduleData cells should be null
        for (TimeTableSlot[] row : calendar.scheduleData) {
            for (TimeTableSlot s : row) {
                assertNull(s, "scheduleData should be reset to null");
            }
        }
    }

    @Test
    public void testUpdateFiltersWithProfessor() {
        when(filters.getSelectedGroup()).thenReturn(null);
        when(filters.getSelectedProfessor()).thenReturn(professor);
        when(filters.getSelectedDiscipline()).thenReturn(null);
        when(filters.getSelectedRoom()).thenReturn(null);

        List<TimeTableSlot> profSlots = List.of(slot);
        Map<Weekday, List<TimeTableSlot>> schedule = new EnumMap<>(Weekday.class);
        schedule.put(Weekday.Monday, profSlots);

        when(timeTable.getClassesRemainingByProfessor(professor)).thenReturn(profSlots);
        when(timeTable.getScheduleByProfessor(professor)).thenReturn(schedule);

        calendar.updateFilters();

        // SidePane should be updated
        assertEquals(profSlots, sidePane.slots);

        // ScheduleData should have the slot in the correct place
        boolean found = false;
        for (TimeTableSlot[] row : calendar.scheduleData) {
            for (TimeTableSlot s : row) {
                if (s == slot) found = true;
            }
        }
        assertTrue(found, "Slot should be present in scheduleData");
    }

    @Test
    public void testUpdateFiltersWithDiscipline() {
        when(filters.getSelectedGroup()).thenReturn(null);
        when(filters.getSelectedProfessor()).thenReturn(null);
        when(filters.getSelectedDiscipline()).thenReturn(discipline);
        when(filters.getSelectedRoom()).thenReturn(null);

        List<TimeTableSlot> discSlots = List.of(slot);
        Map<Weekday, List<TimeTableSlot>> schedule = new EnumMap<>(Weekday.class);
        schedule.put(Weekday.Monday, discSlots);

        when(timeTable.getClassesRemainingByDiscipline(discipline)).thenReturn(discSlots);
        when(timeTable.getScheduleByDiscipline(discipline)).thenReturn(schedule);

        calendar.updateFilters();

        // SidePane should be updated
        assertEquals(discSlots, sidePane.slots);

        // ScheduleData should have the slot in the correct place
        boolean found = false;
        for (TimeTableSlot[] row : calendar.scheduleData) {
            for (TimeTableSlot s : row) {
                if (s == slot) found = true;
            }
        }
        assertTrue(found, "Slot should be present in scheduleData");
    }

    @Test
    public void testUpdateFiltersWithRoom() {
        when(filters.getSelectedGroup()).thenReturn(null);
        when(filters.getSelectedProfessor()).thenReturn(null);
        when(filters.getSelectedDiscipline()).thenReturn(null);
        when(filters.getSelectedRoom()).thenReturn(room);

        List<TimeTableSlot> roomSlots = List.of(slot);
        Map<Weekday, List<TimeTableSlot>> schedule = new EnumMap<>(Weekday.class);
        schedule.put(Weekday.Monday, roomSlots);

        // when(timeTable.get(room)).thenReturn(roomSlots);
        when(timeTable.getScheduleByRoom(room)).thenReturn(schedule);

        calendar.updateFilters();

        // SidePane should be updated
        // assertEquals(roomSlots, sidePane.slots);

        // ScheduleData should have the slot in the correct place
        boolean found = false;
        for (TimeTableSlot[] row : calendar.scheduleData) {
            for (TimeTableSlot s : row) {
                if (s == slot) found = true;
            }
        }
        assertTrue(found, "Slot should be present in scheduleData");
    }
}
