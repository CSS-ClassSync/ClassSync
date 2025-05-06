package io.github.ClassSyncCSS.ClassSync.Domain;

import java.util.Dictionary;
import java.util.List;

public class AllData {
    private final List<Professor> professors;
    private final List<Discipline> disciplines;
    private final List<Specialization> specializations;
    private final List<Group> groups;
    private final List<Room> rooms;

    public AllData(List<Professor> professors, List<Discipline> disciplines, List<Specialization> specializations, List<Group> groups, List<Room> rooms) {
        this.professors = professors;
        this.disciplines = disciplines;
        this.specializations = specializations;
        this.groups = groups;
        this.rooms = rooms;
    }

    public List<Professor> getProfessors() {
        return professors;
    }

    public List<Discipline> getDisciplines() {
        return disciplines;
    }

    public List<Specialization> getSpecializations() {
        return specializations;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public List<Room> getRooms() {
        return rooms;
    }
}
