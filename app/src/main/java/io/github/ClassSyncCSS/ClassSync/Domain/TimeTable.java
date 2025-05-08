package io.github.ClassSyncCSS.ClassSync.Domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeTable {
    // Change the key types in the maps from objects to strings
    Map<String, List<TimeTableSlot>> classesRemainingByProfessor = new HashMap<>();
    Map<String, List<TimeTableSlot>> classesRemainingByGroup = new HashMap<>();
    Map<String, List<TimeTableSlot>> classesRemainingByDisipline = new HashMap<>();
    
    Map<String, Map<Weekday, Map<TimeSlot, TimeTableSlot>>> scheduleByGroup = new HashMap<>();
    Map<String, Map<Weekday, Map<TimeSlot, TimeTableSlot>>> scheduleByProfessor = new HashMap<>();
    Map<String, Map<Weekday, Map<TimeSlot, TimeTableSlot>>> scheduleByRoom = new HashMap<>();
    Map<String, Map<Weekday, Map<TimeSlot, TimeTableSlot>>> scheduleByDiscipline = new HashMap<>();

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

                // Add to discipline map - use name as key
                String disciplineKey = discipline.getName();
                if (!classesRemainingByDisipline.containsKey(disciplineKey)) {
                    classesRemainingByDisipline.put(disciplineKey, new ArrayList<>());
                }
                classesRemainingByDisipline.get(disciplineKey).add(course);
                classesRemainingByDisipline.get(disciplineKey).add(lab);

                // Add to group map - use name as key
                String groupKey = group.getName();
                if (!classesRemainingByGroup.containsKey(groupKey)) {
                    classesRemainingByGroup.put(groupKey, new ArrayList<>());
                }
                classesRemainingByGroup.get(groupKey).add(course);
                classesRemainingByGroup.get(groupKey).add(lab);

                // Add to professors map - use name as key
                List<Professor> courseProfs = discipline.getCourseProfs();
                for(Professor professor : courseProfs) {
                    String professorKey = professor.getName();
                    if (!classesRemainingByProfessor.containsKey(professorKey)) {
                        classesRemainingByProfessor.put(professorKey, new ArrayList<>());
                    }
                    classesRemainingByProfessor.get(professorKey).add(course);
                }
                
                List<Professor> labProfs = discipline.getLaboratoryProfs();
                for(Professor professor : labProfs) {
                    String professorKey = professor.getName();
                    if (!classesRemainingByProfessor.containsKey(professorKey)) {
                        classesRemainingByProfessor.put(professorKey, new ArrayList<>());
                    }
                    classesRemainingByProfessor.get(professorKey).add(lab);
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
        String group = slot.getGroup().getName();
        Professor professor = slot.getProfessor();
        Room room = slot.getRoom();


        if (hasConflict(scheduleByGroup, group, day, hours)) return false;

        if (professor != null && hasConflict(scheduleByProfessor, professor.getName(), day, hours)) return false;

        if (room != null && hasConflict(scheduleByRoom, room.getName(), day, hours)) return false;

        insertSlot(scheduleByGroup, group, day, hours, slot);
        if(professor != null) insertSlot(scheduleByProfessor, professor.getName(), day, hours, slot);
        if(room != null) insertSlot(scheduleByRoom, room.getName(), day, hours, slot);
        insertSlot(scheduleByDiscipline, slot.getDiscipline().getName(), day, hours, slot);

        // Update classesRemaining maps using string keys
        String groupKey = slot.getGroup().getName();
        List<TimeTableSlot> groupSlots = classesRemainingByGroup.get(groupKey);
        if(groupSlots != null) {
            groupSlots.removeIf(s -> 
                s.getProfessor() == null || s.getProfessor().equals(slot.getProfessor()) &&
                s.getDiscipline().equals(slot.getDiscipline()) && 
                s.getActivityType() == slot.getActivityType());
        }
        
        String disciplineKey = slot.getDiscipline().getName();
        List<TimeTableSlot> disciplineSlots = classesRemainingByDisipline.get(disciplineKey);
        if(disciplineSlots != null) {
            disciplineSlots.removeIf(s -> 
                s.getGroup().equals(slot.getGroup()) && 
                s.getActivityType() == slot.getActivityType());
        }

        if (slot.getProfessor() != null) {
            String professorKey = slot.getProfessor().getName();
            List<TimeTableSlot> professorSlots = classesRemainingByProfessor.get(professorKey);
            if(professorSlots != null) {
                professorSlots.removeIf(s -> 
                s.getDiscipline().equals(slot.getDiscipline()) && 
                s.getGroup().equals(slot.getGroup()) && 
                s.getActivityType() == slot.getActivityType());
            }
        }
        
        return true;
    }

    public boolean checkAdd(TimeTableSlot slot) {
//        if (slot == null || slot.getSlot() == null || slot.getWeekday() == null ||
//            slot.getGroup() == null || slot.getProfessor() == null || slot.getRoom() == null) {
//            return false;
//        }
        
        TimeSlot hours = slot.getSlot();
        Weekday day = slot.getWeekday();
        String group = slot.getGroup().getName();

        Professor professor = slot.getProfessor();
        Room room = slot.getRoom();

        if (hasConflict(scheduleByGroup, group, day, hours)) return false;
        if (professor != null && hasConflict(scheduleByProfessor, professor.getName(), day, hours)) return false;
        if (room != null && hasConflict(scheduleByRoom, room.getName(), day, hours)) return false;
    
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
        String group = slot.getGroup().getName();

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
            
            // Add to group map using string key
            String groupKey = slot.getGroup().getName();
            if (!classesRemainingByGroup.containsKey(groupKey)) {
                classesRemainingByGroup.put(groupKey, new ArrayList<>());
            }
            classesRemainingByGroup.get(groupKey).add(unscheduledSlot);
            
            // Add to discipline map using string key
            String disciplineKey = slot.getDiscipline().getName();
            if (!classesRemainingByDisipline.containsKey(disciplineKey)) {
                classesRemainingByDisipline.put(disciplineKey, new ArrayList<>());
            }
            classesRemainingByDisipline.get(disciplineKey).add(unscheduledSlot);
            
            // Add to professor map using string key
            if (slot.getProfessor() != null) {
                String professorKey = slot.getProfessor().getName();
                if (!classesRemainingByProfessor.containsKey(professorKey)) {
                    classesRemainingByProfessor.put(professorKey, new ArrayList<>());
                }
                classesRemainingByProfessor.get(professorKey).add(unscheduledSlot);
            }
        }
        
        return removed;
    }
    
    // Update getters to use string keys
    public List<TimeTableSlot> getClassesRemainingByGroup(Group group) {
        if (group == null) return new ArrayList<>();
        return classesRemainingByGroup.getOrDefault(group.getName(), new ArrayList<>());
    }
    
    public List<TimeTableSlot> getClassesRemainingByProfessor(Professor professor) {
        if (professor == null) return new ArrayList<>();
        return classesRemainingByProfessor.getOrDefault(professor.getName(), new ArrayList<>());
    }
    
    public List<TimeTableSlot> getClassesRemainingByDiscipline(Discipline discipline) {
        if (discipline == null) return new ArrayList<>();
        return classesRemainingByDisipline.getOrDefault(discipline.getName(), new ArrayList<>());
    }
    
    // For backward compatibility - these should be updated wherever they're used
    public Map<String, List<TimeTableSlot>> getAllClassesRemainingByProfessor() {
        return classesRemainingByProfessor;
    }
    
    public Map<String, List<TimeTableSlot>> getAllClassesRemainingByGroup() {
        return classesRemainingByGroup;
    }
    
    public Map<String, List<TimeTableSlot>> getAllClassesRemainingByDiscipline() {
        return classesRemainingByDisipline;
    }

    /**
     * Get the schedule for a specific group
     * @param group The group to get the schedule for
     * @return A map of weekday to list of timeslots for this group
     */
    public Map<Weekday, List<TimeTableSlot>> getScheduleByGroup(Group group) {
        if (group == null) return new HashMap<>();
        String key = group.getName();
        return convertScheduleFormat(scheduleByGroup.get(key));
    }

    /**
     * Get the schedule for a specific professor
     * @param professor The professor to get the schedule for
     * @return A map of weekday to list of timeslots for this professor
     */
    public Map<Weekday, List<TimeTableSlot>> getScheduleByProfessor(Professor professor) {
        if (professor == null) return new HashMap<>();
        String key = professor.getName();
        return convertScheduleFormat(scheduleByProfessor.get(key));
    }

    /**
     * Get the schedule for a specific room
     * @param room The room to get the schedule for
     * @return A map of weekday to list of timeslots for this room
     */
    public Map<Weekday, List<TimeTableSlot>> getScheduleByRoom(Room room) {
        if (room == null) return new HashMap<>();
        String key = room.getName();
        return convertScheduleFormat(scheduleByRoom.get(key));
    }

    /**
     * Get the schedule for a specific discipline
     * @param discipline The discipline to get the schedule for
     * @return A map of weekday to list of timeslots for this discipline
     */
    public Map<Weekday, List<TimeTableSlot>> getScheduleByDiscipline(Discipline discipline) {
        if (discipline == null) return new HashMap<>();
        String key = discipline.getName();
        return convertScheduleFormat(scheduleByDiscipline.get(key));
    }

    /**
     * Helper method to convert the nested map structure to a simpler format
     * @param scheduleMap The original nested map from the scheduleBy* fields
     * @return A map of Weekday to List of TimeTableSlots
     */
    private Map<Weekday, List<TimeTableSlot>> convertScheduleFormat(Map<Weekday, Map<TimeSlot, TimeTableSlot>> scheduleMap) {
        Map<Weekday, List<TimeTableSlot>> result = new HashMap<>();
        
        if (scheduleMap == null) return result;
        
        for (Map.Entry<Weekday, Map<TimeSlot, TimeTableSlot>> entry : scheduleMap.entrySet()) {
            Weekday weekday = entry.getKey();
            Map<TimeSlot, TimeTableSlot> timeSlotMap = entry.getValue();
            
            List<TimeTableSlot> slots = new ArrayList<>(timeSlotMap.values());
            result.put(weekday, slots);
        }
        
        return result;
    }


    public String getGroupsJsonString() {
        String s = "{}";

        return s;
    }

    public Map<String, Map<Weekday, Map<TimeSlot, TimeTableSlot>>> getScheduleByGroupRaw() {
        return scheduleByGroup;
    }

}

