package io.github.ClassSyncCSS.ClassSync.Domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeTable {
    Map<String, Map<Weekday, Map<TimeSlot, TimeTableSlot>>> scheduleByGroup = new HashMap<>();
    Map<String, Map<Weekday, Map<TimeSlot, TimeTableSlot>>> scheduleByProfessor = new HashMap<>();
    Map<String, Map<Weekday, Map<TimeSlot, TimeTableSlot>>> scheduleByRoom = new HashMap<>();
    Map<String, Map<Weekday, Map<TimeSlot, TimeTableSlot>>> scheduleByDiscipline = new HashMap<>();

    Map<Professor, List<TimeTableSlot>> classesRemainingByProfessor;
    Map<Group, List<TimeTableSlot>> classesRemainingByGroup;
    Map<Discipline, List<TimeTableSlot>> classesRemainingByDisipline;



    public void setData(AllData allData) {
        classesRemainingByProfessor = new HashMap<>();
        classesRemainingByGroup = new HashMap<>();
        classesRemainingByDisipline = new HashMap<>();

        List<Group> groups = allData.getGroups();

        for(Group group : groups) {
            for(Discipline discipline : group.getDisciplines()) {
                TimeTableSlot course = new TimeTableSlot(
                        null,
                        null,
                        null,
                        null,
                        group,
                        discipline,
                        ActivityType.Course
                );
                TimeTableSlot lab = new TimeTableSlot(
                        null,
                        null,
                        null,
                        null,
                        group,
                        discipline,
                        ActivityType.Lab
                );

                // Add to discipline map
                if (!classesRemainingByDisipline.containsKey(discipline)) {
                    classesRemainingByDisipline.put(discipline, new ArrayList<>());
                }
                classesRemainingByDisipline.get(discipline).add(course);
                classesRemainingByDisipline.get(discipline).add(lab);

                // Add to group map
                if (!classesRemainingByGroup.containsKey(group)) {
                    classesRemainingByGroup.put(group, new ArrayList<>());
                }
                classesRemainingByGroup.get(group).add(course);
                classesRemainingByGroup.get(group).add(lab);

                // Add to professors map
                List<Professor> courseProfs = discipline.getCourseProfs();
                for(Professor professor : courseProfs) {
                    if (!classesRemainingByProfessor.containsKey(professor)) {
                        classesRemainingByProfessor.put(professor, new ArrayList<>());
                    }
                    classesRemainingByProfessor.get(professor).add(course);
                }
                
                List<Professor> labProfs = discipline.getLaboratoryProfs();
                for(Professor professor : labProfs) {
                    if (!classesRemainingByProfessor.containsKey(professor)) {
                        classesRemainingByProfessor.put(professor, new ArrayList<>());
                    }
                    classesRemainingByProfessor.get(professor).add(lab);
                }
            }
        }
    }

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

        classesRemainingByGroup.remove(slot.getGroup());
        classesRemainingByDisipline.remove(slot.getDiscipline());
        classesRemainingByProfessor.remove(slot.getProfessor());

        return true;
    }

    public boolean checkAdd(TimeTableSlot slot) {
        if (slot == null || slot.getSlot() == null || slot.getWeekday() == null || 
            slot.getGroup() == null || slot.getProfessor() == null || slot.getRoom() == null) {
            return false;
        }
        
        TimeSlot hours = slot.getSlot();
        Weekday day = slot.getWeekday();
        String group = slot.getGroup().getYear() + slot.getGroup().getName();
    
        if (hasConflict(scheduleByGroup, group, day, hours)) return false;
        if (hasConflict(scheduleByProfessor, slot.getProfessor().getName(), day, hours)) return false;
        if (hasConflict(scheduleByRoom, slot.getRoom().getName(), day, hours)) return false;
    
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

        if (removed) {
            // Restore the slot to the classesRemaining maps
            TimeTableSlot unscheduledSlot = new TimeTableSlot(
                null, null, slot.getProfessor(), null, slot.getGroup(), 
                slot.getDiscipline(), slot.getActivityType()
            );
            
            // Add to group map
            if (!classesRemainingByGroup.containsKey(slot.getGroup())) {
                classesRemainingByGroup.put(slot.getGroup(), new ArrayList<>());
            }
            classesRemainingByGroup.get(slot.getGroup()).add(unscheduledSlot);
            
            // Add to discipline map
            if (!classesRemainingByDisipline.containsKey(slot.getDiscipline())) {
                classesRemainingByDisipline.put(slot.getDiscipline(), new ArrayList<>());
            }
            classesRemainingByDisipline.get(slot.getDiscipline()).add(unscheduledSlot);
            
            // Add to professor map
            if (!classesRemainingByProfessor.containsKey(slot.getProfessor())) {
                classesRemainingByProfessor.put(slot.getProfessor(), new ArrayList<>());
            }
            classesRemainingByProfessor.get(slot.getProfessor()).add(unscheduledSlot);
        }

        return removed;
    }

    public Map<Professor, List<TimeTableSlot>> getClassesRemainingByProfessor() {
        return classesRemainingByProfessor;
    }

    public Map<Group, List<TimeTableSlot>> getClassesRemainingByGroup() {
        return classesRemainingByGroup;
    }

    public Map<Discipline, List<TimeTableSlot>> getClassesRemainingByDisipline() {
        return classesRemainingByDisipline;
    }
}

