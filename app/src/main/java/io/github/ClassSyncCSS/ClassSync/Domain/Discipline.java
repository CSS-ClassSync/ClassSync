package io.github.ClassSyncCSS.ClassSync.Domain;

import java.util.ArrayList;
import java.util.List;

public class Discipline {
    private String name;
    private List<Professor> courseProfs;
    private List<Professor> laboratoryProfs;

    // Class Invariant method
    private boolean classInvariant() {
        return name != null && !name.isEmpty()
                && courseProfs != null
                && laboratoryProfs != null;
    }

     public Discipline() {
         this.name = "----";
     }

    public Discipline(String name, List<Professor> courseProfs, List<Professor> laboratoryProfs) {
        assert name != null && !name.isEmpty() : "Precondition failed: name must not be null or empty";
        assert courseProfs != null : "Precondition failed: courseProfs must not be null";
        assert laboratoryProfs != null : "Precondition failed: laboratoryProfs must not be null";

        this.name = name;
        this.courseProfs = courseProfs;
        this.laboratoryProfs = laboratoryProfs;

        assert classInvariant() : "Invariant failed after parameterized constructor";
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

    public List<Professor> getCourseProfs() {
        assert classInvariant() : "Invariant failed before getCourseProfs";
        return courseProfs != null ? courseProfs : new ArrayList<>();
    }

    public void setCourseProfs(List<Professor> courseProfs) {
        assert courseProfs != null : "Precondition failed: courseProfs must not be null";
        this.courseProfs = courseProfs;
        assert classInvariant() : "Invariant failed after setCourseProfs";
    }

    public List<Professor> getLaboratoryProfs() {
        assert classInvariant() : "Invariant failed before getLaboratoryProfs";
        return laboratoryProfs != null ? laboratoryProfs : new ArrayList<>();
    }

    public void setLaboratoryProfs(List<Professor> laboratoryProfs) {
        assert laboratoryProfs != null : "Precondition failed: laboratoryProfs must not be null";
        this.laboratoryProfs = laboratoryProfs;
        assert classInvariant() : "Invariant failed after setLaboratoryProfs";
    }

    @Override
    public String toString() {
        assert classInvariant() : "Invariant failed before toString";
        return "Discipline{" +
                "name='" + name + '\'' +
                // optionally print profs if needed
                '}';
    }
}
