package io.github.ClassSyncCSS.ClassSync.Domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleExporterTest {

    private final String testFilePath = "test_schedule.json";

    @AfterEach
    void cleanup() {
        File file = new File(testFilePath);
        if (file.exists()) file.delete();
    }

    @Test
    void testExportScheduleToJsonCreatesFile() {

        Map<String, Map<Weekday, Map<TimeSlot, TimeTableSlot>>> schedule = new HashMap<>();

        Professor prof = new Professor("ProfA", ProfType.Course, new HashMap<>());
        Group group = new Group("MISS1", new Year("1", new ArrayList<>()), new ArrayList<>());
        Discipline discipline = new Discipline("Software Quality", List.of(prof), new ArrayList<>());

        TimeTableSlot slot = new TimeTableSlot(
                Weekday.Monday,
                TimeSlot.EightToTen,
                prof,
                new Room("C210", List.of(ActivityType.Course)),
                group,
                discipline,
                ActivityType.Course
        );

        Map<TimeSlot, TimeTableSlot> timeSlotMap = new HashMap<>();
        timeSlotMap.put(TimeSlot.EightToTen, slot);

        Map<Weekday, Map<TimeSlot, TimeTableSlot>> weekdayMap = new HashMap<>();
        weekdayMap.put(Weekday.Monday, timeSlotMap);

        schedule.put(prof.getName(), weekdayMap);

        ScheduleExporter.exportScheduleToJson(schedule, testFilePath);

        File outputFile = new File(testFilePath);
        assertTrue(outputFile.exists());
        assertTrue(outputFile.length() > 0);
    }

    @Test
    void testExportScheduleHandlesNullInput() {
        // This test ensures no crash occurs and the method handles null
        assertDoesNotThrow(() -> ScheduleExporter.exportScheduleToJson(null, testFilePath));
    }

    @Test
    void testExportScheduleHandlesInvalidPath() {
        // Try exporting to a invalid path
        assertDoesNotThrow(() -> ScheduleExporter.exportScheduleToJson(new HashMap<>(), "///invalid_path.json"));
    }
}
