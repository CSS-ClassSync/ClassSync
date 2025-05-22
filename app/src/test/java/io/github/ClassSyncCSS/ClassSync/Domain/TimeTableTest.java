package io.github.ClassSyncCSS.ClassSync.Domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TimeTableTest {

    @TempDir
    Path tempDir;

    Path resourcesDir;
    Path originalResourceDir = Path.of("src/main/resources");
    Path backupDir;

    @BeforeEach
    public void setup() throws IOException {
        // Backup original files
        backupDir = tempDir.resolve("backup");
        Files.createDirectory(backupDir);

        try (Stream<Path> files = Files.list(originalResourceDir)) {
            for (Path file : files.toList()) {
                Files.copy(file, backupDir.resolve(file.getFileName()), StandardCopyOption.REPLACE_EXISTING);
            }
        }

        // Replace with test files
        resourcesDir = originalResourceDir;

        writeCsv("ani.csv", List.of(
                "id,an,spec,type",
                "TESTAN001,1,Test Spec,Licenta",
                "TESTAN002,2,Test Spec,Licenta"
        ));

        writeCsv("grupe.csv", List.of(
                "id,grupa,an",
                "GRP001,0,TESTAN001",
                "GRP002,1,TESTAN002"
        ));

        writeCsv("profesori.csv", List.of(
                "id,name,rol",
                "PROF001,Prof One,Full",
                "PROF002,Prof Two,Assoc"
        ));

        writeCsv("materii.csv", List.of(
                "id,name,tip",
                "MAT001,Algorithms,Curs",
                "MAT002,Data Structures,Laborator",
                "MAT003,Testing,Seminar"
        ));

        writeCsv("sali.csv", List.of(
                "name,tip",
                "Room A,Curs",
                "Room B,Laborator"
        ));

        writeCsv("materii_an.csv", List.of(
                "materie,an",
                "MAT001,TESTAN001",
                "MAT002,TESTAN002",
                "MAT003,TESTAN001"
        ));

        writeCsv("profesori_materii.csv", List.of(
                "profesor,materie",
                "PROF001,MAT001",
                "PROF002,MAT002",
                "PROF001,MAT003"
        ));
    }

    @AfterEach
    public void teardown() throws IOException {
        // Delete test files
        try (Stream<Path> files = Files.list(resourcesDir)) {
            for (Path file : files.toList()) {
                Files.delete(file);
            }
        }
        // Restore original files
        try (Stream<Path> files = Files.list(backupDir)) {
            for (Path file : files.toList()) {
                Files.copy(file, resourcesDir.resolve(file.getFileName()), StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    private void writeCsv(String fileName, List<String> lines) throws IOException {
        Path filePath = resourcesDir.resolve(fileName);
        Files.write(filePath, lines);
    }

    @Test
    void testSetDataInitializesClassMaps() {
        TimeTable timeTable = new TimeTable();
        Discipline discipline = new Discipline("Software Quality", new ArrayList<>(), new ArrayList<>());
        Group group = new Group("MISS1", new Year("1", new ArrayList<>()), List.of(discipline));
        Room room = new Room("C210", null);
        AllData allData = createAllDataWithGroup(group);
        allData.setDisciplines(List.of(discipline));
        allData.setRooms(List.of(room));

        timeTable.setData(allData);

        assertFalse(timeTable.getClassesRemainingByGroup(group).isEmpty());
        assertFalse(timeTable.getClassesRemainingByDiscipline(discipline).isEmpty());
    }

    @Test
    void testAddDisciplineNoConflicts() {
        TimeTable timeTable = new TimeTable();
        Discipline discipline = new Discipline("Software Quality", new ArrayList<>(), new ArrayList<>());
        Group group = new Group("MISS1", new Year("1", new ArrayList<>()), List.of(discipline));
        Room room = new Room("C210", List.of(ActivityType.Course));
        AllData allData = createAllDataWithGroup(group);
        allData.setRooms(List.of(room));
        allData.setDisciplines(List.of(discipline));
        timeTable.setData(allData);
        TimeTableSlot slot = new TimeTableSlot(Weekday.Monday, TimeSlot.EightToTen, null, null,
                allData.getGroups().getFirst(), allData.getDisciplines().getFirst(), ActivityType.Course);

        boolean result = timeTable.addDiscipline(slot);

        assertTrue(result);
    }

    @Test
    void testAddDisciplineWithGroupConflict() {
        AllData allData = AllData.load();

        TimeTable timeTable = new TimeTable();
        Discipline discipline = new Discipline("Software Quality", new ArrayList<>(), new ArrayList<>());
        Group group = new Group("MISS1", new Year("1", new ArrayList<>()), List.of(discipline));
        Room room = new Room("C210", List.of(ActivityType.Course, ActivityType.Lab));
        AllData allData = createAllDataWithGroup(group);
        allData.setDisciplines(List.of(discipline));
        allData.setRooms(List.of(room));
        timeTable.setData(allData);

        Discipline discipline = allData.getDisciplines().getFirst();
        Group group1 = allData.getGroups().getFirst();

        TimeTableSlot slot1 = new TimeTableSlot(Weekday.Monday, TimeSlot.EightToTen, null, null, group1, discipline, ActivityType.Course);
        TimeTableSlot slot2 = new TimeTableSlot(Weekday.Monday, TimeSlot.EightToTen, null, null, group1, discipline, ActivityType.Lab);

        timeTable.addDiscipline(slot1);
        boolean result = timeTable.addDiscipline(slot2);

        assertFalse(result);
    }

    @Test
    void testAddDisciplineWIthProfessorConflict() {
        AllData allData = AllData.load();
        TimeTable timeTable = new TimeTable();
        timeTable.setData(allData);

        Professor professor = allData.getProfessors().getFirst();
        Room room = allData.getRooms().getFirst();
        Discipline discipline = allData.getDisciplines().getFirst();
        Group group = allData.getGroups().getFirst();
        Group group2 = allData.getGroups().get(1);

        TimeTableSlot slot1 = new TimeTableSlot(Weekday.Monday, TimeSlot.EightToTen, professor, room, group, discipline, ActivityType.Course);
        TimeTableSlot slot2 = new TimeTableSlot(Weekday.Monday, TimeSlot.EightToTen, professor, room, group2, discipline, ActivityType.Lab);

        timeTable.addDiscipline(slot1);
        boolean result = timeTable.addDiscipline(slot2);

        assertFalse(result);
    }

    @Test
    void testAddDisciplineWithRoomConflict() {
        AllData allData = AllData.load();
        TimeTable timeTable = new TimeTable();
        timeTable.setData(allData);

        Professor professor = allData.getProfessors().getFirst();
        Professor professor2 = allData.getProfessors().get(1);
        Room room = allData.getRooms().getFirst();
        Discipline discipline = allData.getDisciplines().getFirst();
        Group group = allData.getGroups().getFirst();
        Group group2 = allData.getGroups().get(1);

        TimeTableSlot slot1 = new TimeTableSlot(Weekday.Monday, TimeSlot.EightToTen, professor, room, group, discipline, ActivityType.Course);
        TimeTableSlot slot2 = new TimeTableSlot(Weekday.Monday, TimeSlot.EightToTen, professor2, room, group2, discipline, ActivityType.Lab);

        boolean result1 = timeTable.addDiscipline(slot1);
        assertTrue(result1);

        boolean result2 = timeTable.addDiscipline(slot2);
        assertFalse(result2);

        assertTrue(timeTable.getClassesRemainingByGroup(group).isEmpty());
        assertFalse(timeTable.getClassesRemainingByDiscipline(discipline).isEmpty());
    }

    @Test
    void testRemoveDisciplineExistingSlot() {
        AllData allData = AllData.load();
        TimeTable timeTable = new TimeTable();

        Professor professor = new Professor("ProfA", ProfType.Course, new HashMap<>());
        Discipline discipline = new Discipline("Software Quality", List.of(professor), new ArrayList<>());
        Group group = new Group("MISS1", new Year("1", new ArrayList<>()), List.of(discipline));
        Room room = new Room("C210", null);
        AllData allData = new AllData(List.of(professor), List.of(discipline), new ArrayList<>(), List.of(group.getYear()), List.of(group), new ArrayList<>());
        allData.setDisciplines(List.of(discipline));
        allData.setRooms(List.of(room));

        timeTable.setData(allData);

        Professor professor = allData.getProfessors().getFirst();
        Room room = allData.getRooms().getFirst();
        Discipline discipline = allData.getDisciplines().getFirst();
        Group group = allData.getGroups().getFirst();

        

        TimeTableSlot slot = new TimeTableSlot(
                Weekday.Monday,
                TimeSlot.EightToTen,
                professor,
                room,
                group,
                discipline,
                ActivityType.Course
        );

        boolean add = timeTable.addDiscipline(slot);
        assertTrue(add);

        boolean remove = timeTable.removeDiscipline(slot);
        assertTrue(remove);
        //assertTrue(timeTable.getClassesRemainingByDisipline().isEmpty());
        //assertTrue(timeTable.getClassesRemainingByGroup().isEmpty());
        //assertTrue(timeTable.getClassesRemainingByProfessor().isEmpty());
    }


    @Test
    void testCheckAddNoConflicts() {
        AllData allData = AllData.load();
        TimeTable timeTable = new TimeTable();
        Discipline discipline = new Discipline("Software Quality", new ArrayList<>(), new ArrayList<>());
        Group group = new Group("MISS1", new Year("1", new ArrayList<>()), List.of(discipline));
        Room room = new Room("C210", List.of(ActivityType.Course));
        AllData allData = createAllDataWithGroup(group);
        allData.setDisciplines(List.of(discipline));
        allData.setRooms(List.of(room));
        timeTable.setData(allData);

        Professor professor = allData.getProfessors().getFirst();
        Room room = allData.getRooms().getFirst();
        Discipline discipline = allData.getDisciplines().getFirst();
        Group group = allData.getGroups().getFirst();

        TimeTableSlot slot = new TimeTableSlot(
                Weekday.Monday,
                TimeSlot.EightToTen,
                professor,
                room,
                group,
                discipline,
                ActivityType.Course
        );

        boolean canAdd = timeTable.checkAdd(slot);
        assertTrue(canAdd);
    }

    @Test
    void testGetScheduleByGroup() {
        AllData allData = AllData.load();
        TimeTable timeTable = new TimeTable();
        timeTable.setData(allData);

        Professor professor = allData.getProfessors().getFirst();
        Room room = allData.getRooms().getFirst();
        Discipline discipline = allData.getDisciplines().getFirst();
        Group group = allData.getGroups().getFirst();

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

        Map<Weekday, List<TimeTableSlot>> schedule = timeTable.getScheduleByGroup(group);

        assertNotNull(schedule);
        assertFalse(schedule.isEmpty());
    }

    @Test
    void testGetScheduleByDiscipline() {
        AllData allData = AllData.load();
        TimeTable timeTable = new TimeTable();
        timeTable.setData(allData);

        Professor professor = allData.getProfessors().getFirst();
        Room room = allData.getRooms().getFirst();
        Discipline discipline = allData.getDisciplines().getFirst();
        Group group = allData.getGroups().getFirst();

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

        Map<Weekday, List<TimeTableSlot>> schedule = timeTable.getScheduleByDiscipline(discipline);

        assertNotNull(schedule);
        assertFalse(schedule.isEmpty());
    }

    @Test
    void testGetScheduleByProfessor() {
        AllData allData = AllData.load();
        TimeTable timeTable = new TimeTable();
        timeTable.setData(allData);

        Professor professor = allData.getProfessors().getFirst();
        Room room = allData.getRooms().getFirst();
        Discipline discipline = allData.getDisciplines().getFirst();
        Group group = allData.getGroups().getFirst();

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

        Map<Weekday, List<TimeTableSlot>> schedule = timeTable.getScheduleByProfessor(professor);

        assertNotNull(schedule);
        assertFalse(schedule.isEmpty());
    }

    @Test
    void testGetScheduleByRoom() {
        AllData allData = AllData.load();
        TimeTable timeTable = new TimeTable();
        timeTable.setData(allData);

        Professor professor = allData.getProfessors().getFirst();
        Room room = allData.getRooms().getFirst();
        Discipline discipline = allData.getDisciplines().getFirst();
        Group group = allData.getGroups().getFirst();

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

        Map<Weekday, List<TimeTableSlot>> schedule = timeTable.getScheduleByRoom(room);

        assertNotNull(schedule);
        assertFalse(schedule.isEmpty());
    }

    @Test
    void testGetClassesRemainingByGroup() {
        AllData allData = AllData.load();
        TimeTable timeTable = new TimeTable();
        timeTable.setData(allData);

        Group group = allData.getGroups().getFirst();

        List<TimeTableSlot> classesRemaining = timeTable.getClassesRemainingByGroup(group);

        assertNotNull(classesRemaining);
        assertFalse(classesRemaining.isEmpty());
    }

    @Test
    void testGetClassesRemainingByDiscipline() {
        AllData allData = AllData.load();
        TimeTable timeTable = new TimeTable();
        timeTable.setData(allData);

        Discipline discipline = allData.getDisciplines().getFirst();
        List<TimeTableSlot> classesRemaining = timeTable.getClassesRemainingByDiscipline(discipline);

        assertNotNull(classesRemaining);
        assertFalse(classesRemaining.isEmpty());
    }

    @Test
    void testGetClassesRemainingByProfessor() {
        AllData allData = AllData.load();
        TimeTable timeTable = new TimeTable();
        timeTable.setData(allData);

        Professor professor = allData.getProfessors().getFirst();
        List<TimeTableSlot> classesRemaining = timeTable.getClassesRemainingByProfessor(professor);

        assertNotNull(classesRemaining);
        assertFalse(classesRemaining.isEmpty());
    }

}
