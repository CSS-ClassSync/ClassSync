package io.github.ClassSyncCSS.ClassSync.Domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeTable {
    Map<String, List<TimeTableSlot>> classesRemainingByProfessor = new HashMap<>();
    Map<String, List<TimeTableSlot>> classesRemainingByGroup = new HashMap<>();
    Map<String, List<TimeTableSlot>> classesRemainingByDisipline = new HashMap<>();

    Map<String, Map<Weekday, Map<TimeSlot, TimeTableSlot>>> scheduleByGroup = new HashMap<>();
    Map<String, Map<Weekday, Map<TimeSlot, TimeTableSlot>>> scheduleByProfessor = new HashMap<>();
    Map<String, Map<Weekday, Map<TimeSlot, TimeTableSlot>>> scheduleByRoom = new HashMap<>();
    Map<String, Map<Weekday, Map<TimeSlot, TimeTableSlot>>> scheduleByDiscipline = new HashMap<>();

    private boolean classInvariant() {
        return classesRemainingByProfessor != null &&
               classesRemainingByGroup != null &&
               classesRemainingByDisipline != null &&
               scheduleByGroup != null &&
               scheduleByProfessor != null &&
               scheduleByRoom != null &&
               scheduleByDiscipline != null;
    }

    public void setData(AllData allData) {
        assert allData != null : "allData must not be null";
        assert allData.getGroups() != null : "Groups list must not be null";
        assert allData.getRooms() != null && !allData.getRooms().isEmpty() : "Rooms list must not be null or empty";

        classesRemainingByProfessor = new HashMap<>();
        classesRemainingByGroup = new HashMap<>();
        classesRemainingByDisipline = new HashMap<>();

        List<Group> groups = allData.getGroups();

        for (Group group : groups) {
            assert group != null : "Group must not be null";
            for (Discipline discipline : group.getDisciplines()) {
                assert discipline != null : "Discipline must not be null";

                TimeTableSlot course = new TimeTableSlot(
                        null,
                        null,
                        !discipline.getCourseProfs().isEmpty() ? discipline.getCourseProfs().get(0) : null,
                        allData.getRooms().get(0),
                        group,
                        discipline,
                        ActivityType.Course
                );
                TimeTableSlot lab = new TimeTableSlot(
                        null,
                        null,
                        !discipline.getLaboratoryProfs().isEmpty() ? discipline.getLaboratoryProfs().getFirst() : null,
                        allData.getRooms().get(0),
                        group,
                        discipline,
                        ActivityType.Lab
                );

                String disciplineKey = discipline.getName();
                classesRemainingByDisipline.computeIfAbsent(disciplineKey, k -> new ArrayList<>()).add(course);
                classesRemainingByDisipline.get(disciplineKey).add(lab);

                String groupKey = group.getName();
                classesRemainingByGroup.computeIfAbsent(groupKey, k -> new ArrayList<>()).add(course);
                classesRemainingByGroup.get(groupKey).add(lab);

                for (Professor professor : discipline.getCourseProfs()) {
                    String professorKey = professor.getName();
                    classesRemainingByProfessor.computeIfAbsent(professorKey, k -> new ArrayList<>()).add(course);
                }

                for (Professor professor : discipline.getLaboratoryProfs()) {
                    String professorKey = professor.getName();
                    classesRemainingByProfessor.computeIfAbsent(professorKey, k -> new ArrayList<>()).add(lab);
                }
            }
        }

        assert classInvariant() : "Invariant failed after setData";
    }

    private boolean hasConflict(Map<String, Map<Weekday, Map<TimeSlot, TimeTableSlot>>> schedule,
                                String key, Weekday day, TimeSlot hours) {
        assert key != null && day != null && hours != null : "hasConflict arguments must not be null";
        return schedule.containsKey(key)
                && schedule.get(key).containsKey(day)
                && schedule.get(key).get(day).containsKey(hours);
    }

    private void insertSlot(Map<String, Map<Weekday, Map<TimeSlot, TimeTableSlot>>> schedule,
                            String key, Weekday day, TimeSlot hour, TimeTableSlot slot) {
        assert key != null && day != null && hour != null && slot != null : "insertSlot arguments must not be null";
        schedule.computeIfAbsent(key, k -> new HashMap<>())
                .computeIfAbsent(day, d -> new HashMap<>())
                .put(hour, slot);
    }

    public boolean addDiscipline(TimeTableSlot slot) {
        assert slot != null : "slot must not be null";
        assert slot.getSlot() != null && slot.getWeekday() != null && slot.getGroup() != null && slot.getDiscipline() != null : "slot fields must not be null";

        TimeSlot hours = slot.getSlot();
        Weekday day = slot.getWeekday();
        String group = slot.getGroup().getName();
        Professor professor = slot.getProfessor();
        Room room = slot.getRoom();

        if (hasConflict(scheduleByGroup, group, day, hours)) return false;
        if (professor != null && hasConflict(scheduleByProfessor, professor.getName(), day, hours)) return false;
        if (room != null && hasConflict(scheduleByRoom, room.getName(), day, hours)) return false;

        insertSlot(scheduleByGroup, group, day, hours, slot);
        if (professor != null) insertSlot(scheduleByProfessor, professor.getName(), day, hours, slot);
        if (room != null) insertSlot(scheduleByRoom, room.getName(), day, hours, slot);
        insertSlot(scheduleByDiscipline, slot.getDiscipline().getName(), day, hours, slot);

        String groupKey = slot.getGroup().getName();
        List<TimeTableSlot> groupSlots = classesRemainingByGroup.get(groupKey);
        if (groupSlots != null) {
            groupSlots.removeIf(s -> s.getProfessor() == null ||
                    (s.getProfessor().equals(slot.getProfessor()) &&
                    s.getDiscipline().equals(slot.getDiscipline()) &&
                    s.getActivityType() == slot.getActivityType()));
        }

        String disciplineKey = slot.getDiscipline().getName();
        List<TimeTableSlot> disciplineSlots = classesRemainingByDisipline.get(disciplineKey);
        if (disciplineSlots != null) {
            disciplineSlots.removeIf(s -> s.getGroup().equals(slot.getGroup()) &&
                    s.getActivityType() == slot.getActivityType());
        }

        if (professor != null) {
            String professorKey = professor.getName();
            List<TimeTableSlot> professorSlots = classesRemainingByProfessor.get(professorKey);
            if (professorSlots != null) {
                professorSlots.removeIf(s -> s.getDiscipline().equals(slot.getDiscipline()) &&
                        s.getGroup().equals(slot.getGroup()) &&
                        s.getActivityType() == slot.getActivityType());
            }
        }

        assert classInvariant() : "Invariant failed after addDiscipline";
        return true;
    }

    public boolean checkAdd(TimeTableSlot slot) {
        assert slot != null && slot.getSlot() != null && slot.getWeekday() != null && slot.getGroup() != null : "Precondition failed: slot or fields null";

        TimeSlot hours = slot.getSlot();
        Weekday day = slot.getWeekday();
        String group = slot.getGroup().getName();
        Professor professor = slot.getProfessor();
        Room room = slot.getRoom();

        return !hasConflict(scheduleByGroup, group, day, hours) &&
               (professor == null || !hasConflict(scheduleByProfessor, professor.getName(), day, hours)) &&
               (room == null || !hasConflict(scheduleByRoom, room.getName(), day, hours));
    }

    private boolean removeSlot(Map<String, Map<Weekday, Map<TimeSlot, TimeTableSlot>>> schedule,
                               String key, Weekday day, TimeSlot hour, TimeTableSlot expectedSlot) {
        assert key != null && day != null && hour != null && expectedSlot != null : "removeSlot arguments must not be null";

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
        assert slot != null && slot.getSlot() != null && slot.getWeekday() != null && slot.getGroup() != null : "removeDiscipline slot or fields null";

        TimeSlot hours = slot.getSlot();
        Weekday day = slot.getWeekday();
        String group = slot.getGroup().getName();

        boolean removed = false;

        removed |= removeSlot(scheduleByGroup, group, day, hours, slot);
        removed |= removeSlot(scheduleByProfessor, slot.getProfessor().getName(), day, hours, slot);
        removed |= removeSlot(scheduleByRoom, slot.getRoom().getName(), day, hours, slot);
        removed |= removeSlot(scheduleByDiscipline, slot.getDiscipline().getName(), day, hours, slot);

        if (removed) {
            TimeTableSlot unscheduledSlot = new TimeTableSlot(
                null, null, slot.getProfessor(), null, slot.getGroup(), 
                slot.getDiscipline(), slot.getActivityType()
            );

            classesRemainingByGroup.computeIfAbsent(group, k -> new ArrayList<>()).add(unscheduledSlot);
            classesRemainingByDisipline.computeIfAbsent(slot.getDiscipline().getName(), k -> new ArrayList<>()).add(unscheduledSlot);

            if (slot.getProfessor() != null) {
                classesRemainingByProfessor.computeIfAbsent(slot.getProfessor().getName(), k -> new ArrayList<>()).add(unscheduledSlot);
            }
        }

        assert classInvariant() : "Invariant failed after removeDiscipline";
        return removed;
    }
    
    // Update getters to use string keys
    public List<TimeTableSlot> getClassesRemainingByGroup(Group group) {
        assert group != null : "group must not be null";
        return classesRemainingByGroup.getOrDefault(group.getName(), new ArrayList<>());
    }
    
    public List<TimeTableSlot> getClassesRemainingByProfessor(Professor professor) {
        assert professor != null : "professor must not be null";
        return classesRemainingByProfessor.getOrDefault(professor.getName(), new ArrayList<>());
    }
    
    public List<TimeTableSlot> getClassesRemainingByDiscipline(Discipline discipline) {
        assert discipline != null : "discipline must not be null";
        return classesRemainingByDisipline.getOrDefault(discipline.getName(), new ArrayList<>());
    }

    /**
     * Get the schedule for a specific group
     * @param group The group to get the schedule for
     * @return A map of weekday to list of timeslots for this group
     */
    public Map<Weekday, List<TimeTableSlot>> getScheduleByGroup(Group group) {
        assert group != null : "group must not be null";
        String key = group.getName();
        return convertScheduleFormat(scheduleByGroup.get(key));
    }

    /**
     * Get the schedule for a specific professor
     * @param professor The professor to get the schedule for
     * @return A map of weekday to list of timeslots for this professor
     */
    public Map<Weekday, List<TimeTableSlot>> getScheduleByProfessor(Professor professor) {
        assert professor != null : "professor must not be null";
        String key = professor.getName();
        return convertScheduleFormat(scheduleByProfessor.get(key));
    }

    /**
     * Get the schedule for a specific room
     * @param room The room to get the schedule for
     * @return A map of weekday to list of timeslots for this room
     */
    public Map<Weekday, List<TimeTableSlot>> getScheduleByRoom(Room room) {
        assert room != null : "room must not be null";
        String key = room.getName();
        return convertScheduleFormat(scheduleByRoom.get(key));
    }

    /**
     * Get the schedule for a specific discipline
     * @param discipline The discipline to get the schedule for
     * @return A map of weekday to list of timeslots for this discipline
     */
    public Map<Weekday, List<TimeTableSlot>> getScheduleByDiscipline(Discipline discipline) {
        assert discipline != null : "discipline must not be null";
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

    public Map<String, Map<Weekday, Map<TimeSlot, TimeTableSlot>>> getScheduleByGroupRaw() {
        return scheduleByGroup;
    }

}

