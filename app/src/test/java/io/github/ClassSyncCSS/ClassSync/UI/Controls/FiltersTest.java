package io.github.ClassSyncCSS.ClassSync.UI.Controls;

import io.github.ClassSyncCSS.ClassSync.Domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FiltersTest {

    private AllData allData;
    private Filters filters;
    private Professor prof1, prof2;
    private Discipline disc1, disc2;
    private Group group1, group2;
    private Room room1, room2;
    private Calendar calendar;
    private SidePane sidePane;

    @BeforeEach
    public void setup() {
        // Mock Professors
        prof1 = mock(Professor.class);
        when(prof1.getName()).thenReturn("Prof One");
        prof2 = mock(Professor.class);
        when(prof2.getName()).thenReturn("Prof Two");

        // Mock Disciplines
        disc1 = mock(Discipline.class);
        when(disc1.getName()).thenReturn("Algorithms");
        when(disc1.getCourseProfs()).thenReturn(List.of(prof1));
        when(disc1.getLaboratoryProfs()).thenReturn(List.of(prof2));

        disc2 = mock(Discipline.class);
        when(disc2.getName()).thenReturn("Data Structures");
        when(disc2.getCourseProfs()).thenReturn(List.of(prof2));
        when(disc2.getLaboratoryProfs()).thenReturn(List.of());

        // Mock Groups
        group1 = mock(Group.class);
        when(group1.getName()).thenReturn("Group A");
        when(group1.getDisciplines()).thenReturn(List.of(disc1));

        group2 = mock(Group.class);
        when(group2.getName()).thenReturn("Group B");
        when(group2.getDisciplines()).thenReturn(List.of(disc2));

        // Mock Rooms
        room1 = mock(Room.class);
        when(room1.getName()).thenReturn("Room 101");
        room2 = mock(Room.class);
        when(room2.getName()).thenReturn("Room 102");

        // Mock AllData
        allData = mock(AllData.class);
        when(allData.getProfessors()).thenReturn(List.of(prof1, prof2));
        when(allData.getDisciplines()).thenReturn(List.of(disc1, disc2));
        when(allData.getGroups()).thenReturn(List.of(group1, group2));
        when(allData.getRooms()).thenReturn(List.of(room1, room2));

        // UI
        calendar = mock(Calendar.class);
        sidePane = mock(SidePane.class);

        filters = new Filters(allData);
        filters.setCalendarRef(calendar);
        filters.setSidePaneRef(sidePane);
    }

    @Test
    public void testInitialFilteredListsContainAllWithPlaceholders() {
        List<Professor> filteredProfs = filters.getFilteredProfessors();
        List<Group> filteredGroups = filters.getFilteredGroups();
        List<Discipline> filteredDisciplines = filters.getFilteredDisciplines();
        List<Room> filteredRooms = filters.getFilteredRooms();

        assertEquals(3, filteredProfs.size()); // 1 placeholder + 2 profs
        assertEquals(3, filteredGroups.size()); // 1 placeholder + 2 groups
        assertEquals(3, filteredDisciplines.size()); // 1 placeholder + 2 disciplines
        assertEquals(3, filteredRooms.size()); // 1 placeholder + 2 rooms

        assertNull(filters.getSelectedProfessor());
        assertNull(filters.getSelectedGroup());
        assertNull(filters.getSelectedDiscipline());
        assertNull(filters.getSelectedRoom());
    }

    @Test
    public void testUpdateFiltersWithDisciplineSelectionFiltersProfessors() {
        // Select disc1 (index 1, since 0 is placeholder)
        filters.getFilteredDisciplines(); // ensure filteredDisciplines is initialized
        filters.selectedDiscipline.set(1);
        filters.getFilteredProfessors(); // ensure filteredProfessors is initialized
        filters.getFilteredGroups(); // ensure filteredGroups is initialized
        filters.getFilteredRooms(); // ensure filteredRooms is initialized

        // Call updateFilters via reflection (since it's private)
        try {
            var m = Filters.class.getDeclaredMethod("updateFilters");
            m.setAccessible(true);
            m.invoke(filters);
        } catch (Exception e) {
            fail(e);
        }

        List<Professor> filteredProfs = filters.getFilteredProfessors();
        // Should contain placeholder + prof1 + prof2 (since both are in course/lab profs for disc1)
        assertTrue(filteredProfs.stream().anyMatch(p -> "Prof One".equals(p.getName())));
        assertTrue(filteredProfs.stream().anyMatch(p -> "Prof Two".equals(p.getName())));
        assertEquals(3, filteredProfs.size());
    }

    @Test
    public void testUpdateFiltersWithProfessorSelectionFiltersDisciplines() {
        // Select prof2 (index 2, since 0 is placeholder, 1 is prof1, 2 is prof2)
        filters.getFilteredProfessors(); // ensure filteredProfessors is initialized
        filters.selectedProfessor.set(2);
        filters.getFilteredDisciplines(); // ensure filteredDisciplines is initialized
        filters.getFilteredGroups(); // ensure filteredGroups is initialized
        filters.getFilteredRooms(); // ensure filteredRooms is initialized

        // Call updateFilters via reflection (since it's private)
        try {
            var m = Filters.class.getDeclaredMethod("updateFilters");
            m.setAccessible(true);
            m.invoke(filters);
        } catch (Exception e) {
            fail(e);
        }

        List<Discipline> filteredDisciplines = filters.getFilteredDisciplines();
        // Should contain placeholder + both disciplines (prof2 is in both as course/lab prof)
        assertTrue(filteredDisciplines.stream().anyMatch(d -> "Algorithms".equals(d.getName())));
        assertTrue(filteredDisciplines.stream().anyMatch(d -> "Data Structures".equals(d.getName())));
        assertEquals(3, filteredDisciplines.size());
    }

    @Test
    public void testUpdateFiltersWithGroupSelectionFiltersDisciplines() {
        // Select group1 (index 1, since 0 is placeholder)
        filters.getFilteredGroups(); // ensure filteredGroups is initialized
        filters.selectedGroup.set(1);
        filters.getFilteredDisciplines(); // ensure filteredDisciplines is initialized
        filters.getFilteredProfessors(); // ensure filteredProfessors is initialized
        filters.getFilteredRooms(); // ensure filteredRooms is initialized

        // Call updateFilters via reflection (since it's private)
        try {
            var m = Filters.class.getDeclaredMethod("updateFilters");
            m.setAccessible(true);
            m.invoke(filters);
        } catch (Exception e) {
            fail(e);
        }

        List<Discipline> filteredDisciplines = filters.getFilteredDisciplines();
        // Should contain placeholder + disc1 (group1 has disc1)
        assertTrue(filteredDisciplines.stream().anyMatch(d -> "Algorithms".equals(d.getName())));
        assertEquals(2, filteredDisciplines.size());
    }

    @Test
    public void testUpdateFiltersWithDisciplineSelectionFiltersGroups() {
        // Select disc2 (index 2, since 0 is placeholder, 1 is disc1, 2 is disc2)
        filters.getFilteredDisciplines(); // ensure filteredDisciplines is initialized
        filters.selectedDiscipline.set(2);
        filters.getFilteredGroups(); // ensure filteredGroups is initialized
        filters.getFilteredProfessors(); // ensure filteredProfessors is initialized
        filters.getFilteredRooms(); // ensure filteredRooms is initialized

        // Call updateFilters via reflection (since it's private)
        try {
            var m = Filters.class.getDeclaredMethod("updateFilters");
            m.setAccessible(true);
            m.invoke(filters);
        } catch (Exception e) {
            fail(e);
        }

        List<Group> filteredGroups = filters.getFilteredGroups();
        // Should contain placeholder + group2 (group2 has disc2)
        assertTrue(filteredGroups.stream().anyMatch(g -> "Group B".equals(g.getName())));
        assertEquals(2, filteredGroups.size());
    }

    @Test
    public void testUpdateFiltersRestoresSelectionIfStillPresent() {
        // Select disc1 and group1
        filters.getFilteredDisciplines();
        filters.selectedDiscipline.set(1);
        filters.getFilteredGroups();
        filters.selectedGroup.set(1);

        // Call updateFilters via reflection (since it's private)
        try {
            var m = Filters.class.getDeclaredMethod("updateFilters");
            m.setAccessible(true);
            m.invoke(filters);
        } catch (Exception e) {
            fail(e);
        }

        // After update, selection should still be present
        assertEquals(1, filters.selectedDiscipline.get());
        assertEquals(1, filters.selectedGroup.get());
    }

    @Test
    public void testUpdateFiltersResetsSelectionIfNotPresent() {
        // Select disc1 and group1, then remove group1 from allData
        filters.getFilteredDisciplines();
        filters.selectedDiscipline.set(1);
        filters.getFilteredGroups();
        filters.selectedGroup.set(1);

        // Remove group1 from allData
        when(allData.getGroups()).thenReturn(List.of(group2));

        // Call updateFilters via reflection (since it's private)
        try {
            var m = Filters.class.getDeclaredMethod("updateFilters");
            m.setAccessible(true);
            m.invoke(filters);
        } catch (Exception e) {
            fail(e);
        }

        // After update, selection should be reset to 0
        assertEquals(0, filters.selectedGroup.get());
    }
}
