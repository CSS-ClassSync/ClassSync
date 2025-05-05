package io.github.ClassSyncCSS.ClassSync.Domain;

import java.util.List;

public class Specialization {
    private String name;
    private List<Group> groups;
    private List<Discipline> disciplines;

    public Specialization(String name, List<Group> groups, List<Discipline> disciplines) {
        this.name = name;
        this.groups = groups;
        this.disciplines = disciplines;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Discipline> getDisciplines() {
        return disciplines;
    }

    public void setDisciplines(List<Discipline> disciplines) {
        this.disciplines = disciplines;
    }
}
