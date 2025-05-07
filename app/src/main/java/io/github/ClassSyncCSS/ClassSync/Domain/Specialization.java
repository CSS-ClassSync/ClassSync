package io.github.ClassSyncCSS.ClassSync.Domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        return groups != null ? groups : new

    ArrayList<Group>();
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Discipline> getDisciplines() {
        return disciplines != null ? disciplines : new ArrayList<Discipline>();
    }

    public void setDisciplines(List<Discipline> disciplines) {
        this.disciplines = disciplines;
    }

    @Override
    public String toString() {
        return "Specialization{" +
                "name='" + name + '\'' +
                ", groups=" + groups +
                ", disciplines=" + disciplines +
                '}';
    }
}
