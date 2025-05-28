package io.github.ClassSyncCSS.ClassSync.Domain;

import java.util.ArrayList;
import java.util.List;

public class Specialization {
    private String name;
    private List<Group> groups;
    private List<Discipline> disciplines;

    // Class invariant
    private boolean classInvariant() {
        return name != null && !name.isEmpty()
                && groups != null
                && disciplines != null;
    }

    public Specialization(String name, List<Group> groups, List<Discipline> disciplines) {
        assert name != null && !name.isEmpty() : "Precondition failed: name must not be null or empty";
        assert groups != null : "Precondition failed: groups list must not be null";
        assert disciplines != null : "Precondition failed: disciplines list must not be null";

        this.name = name;
        this.groups = groups;
        this.disciplines = disciplines;

        assert classInvariant() : "Invariant failed after constructor";
    }

    public String getName() {
        assert classInvariant() : "Invariant failed before getName";
        return name;
    }

    public void setName(String name) {
        assert name != null && !name.isEmpty() : "Precondition failed: name must not be null or empty";
        this.name = name;
        assert classInvariant() : "Invariant failed after setName";
    }

    public List<Group> getGroups() {
        assert classInvariant() : "Invariant failed before getGroups";
        return groups != null ? groups : new ArrayList<>();
    }

    public void setGroups(List<Group> groups) {
        assert groups != null : "Precondition failed: groups list must not be null";
        this.groups = groups;
        assert classInvariant() : "Invariant failed after setGroups";
    }

    public List<Discipline> getDisciplines() {
        assert classInvariant() : "Invariant failed before getDisciplines";
        return disciplines != null ? disciplines : new ArrayList<>();
    }

    public void setDisciplines(List<Discipline> disciplines) {
        assert disciplines != null : "Precondition failed: disciplines list must not be null";
        this.disciplines = disciplines;
        assert classInvariant() : "Invariant failed after setDisciplines";
    }

    @Override
    public String toString() {
        assert classInvariant() : "Invariant failed before toString";
        return "Specialization{" +
                "name='" + name + '\'' +
                ", groups=" + groups +
                ", disciplines=" + disciplines +
                '}';
    }
}
