package io.github.ClassSyncCSS.ClassSync.Domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.util.Map;

public class ScheduleExporter {
    public static void exportScheduleToJson(
            Map<String, Map<Weekday, Map<TimeSlot, TimeTableSlot>>> scheduleByProfessor,
            String outputFilePath
    ) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            mapper.writeValue(new File(outputFilePath), scheduleByProfessor);
            System.out.println("Export complet: " + outputFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
