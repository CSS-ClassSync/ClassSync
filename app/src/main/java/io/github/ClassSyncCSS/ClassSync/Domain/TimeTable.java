package io.github.ClassSyncCSS.ClassSync.Domain;

import java.util.HashMap;
import java.util.Map;

public class TimeTable {
    Map<String, Map<Weekday, Map<TimeSlot, TimeTableSlot>>> scheduleByGroup = new HashMap<>();
    Map<String, Map<Weekday, Map<TimeSlot, TimeTableSlot>>> scheduleByProfessor = new HashMap<>();
    Map<String, Map<Weekday, Map<TimeSlot, TimeTableSlot>>> scheduleByRoom = new HashMap<>();
    Map<String, Map<Weekday, Map<TimeSlot, TimeTableSlot>>> scheduleByDiscipline = new HashMap<>();

    private boolean hasConflict(Map<String, Map<Weekday, Map<TimeSlot, TimeTableSlot>>> schedule,
                                String key, Weekday day, TimeSlot hours) {
        return schedule.containsKey(key)
                && schedule.get(key).containsKey(day)
                && schedule.get(key).get(day).containsKey(hours);
    }

    private void insertSlot(Map<String, Map<Weekday, Map<TimeSlot, TimeTableSlot>>> schedule,
                            String key, Weekday day, TimeSlot hour, TimeTableSlot slot) {
        schedule
                .computeIfAbsent(key, k -> new HashMap<>())
                .computeIfAbsent(day, d -> new HashMap<>())
                .put(hour, slot);
    }

    public boolean addDiscipline(TimeTableSlot slot) {
        TimeSlot hours = slot.getSlot();
        Weekday day = slot.getWeekday();
        String group = slot.getGroup().getYear() + slot.getGroup().getName();

        if (hasConflict(scheduleByGroup, group, day, hours)) return false;

        if (hasConflict(scheduleByProfessor, slot.getProfessor().getName(), day, hours)) return false;

        if (hasConflict(scheduleByRoom, slot.getRoom().getName(), day, hours)) return false;

        insertSlot(scheduleByGroup, group, day, hours, slot);
        insertSlot(scheduleByProfessor, slot.getProfessor().getName(), day, hours, slot);
        insertSlot(scheduleByRoom, slot.getRoom().getName(), day, hours, slot);
        insertSlot(scheduleByDiscipline, slot.getDiscipline().getName(), day, hours, slot);

        return true;
    }

    private boolean removeSlot(Map<String, Map<Weekday, Map<TimeSlot, TimeTableSlot>>> schedule,
                               String key, Weekday day, TimeSlot hour, TimeTableSlot expectedSlot) {
        if (!schedule.containsKey(key)) return false;
        Map<Weekday, Map<TimeSlot, TimeTableSlot>> byDay = schedule.get(key);
        if (!byDay.containsKey(day)) return false;
        Map<TimeSlot, TimeTableSlot> byHour = byDay.get(day);
        if (!byHour.containsKey(hour)) return false;

        TimeTableSlot existing = byHour.get(hour);
        if (!existing.equals(expectedSlot)) return false;

        byHour.remove(hour);
        if (byHour.isEmpty()) byDay.remove(day);
        if (byDay.isEmpty()) schedule.remove(key);

        return true;
    }


    public boolean removeDiscipline(TimeTableSlot slot) {
        TimeSlot hours = slot.getSlot();
        Weekday day = slot.getWeekday();
        String group = slot.getGroup().getYear() + slot.getGroup().getName();

        boolean removed = false;

        removed |= removeSlot(scheduleByGroup, group, day, hours, slot);
        removed |= removeSlot(scheduleByProfessor, slot.getProfessor().getName(), day, hours, slot);
        removed |= removeSlot(scheduleByRoom, slot.getRoom().getName(), day, hours, slot);
        removed |= removeSlot(scheduleByDiscipline, slot.getDiscipline().getName(), day, hours, slot);

        return removed;
    }


}

