package io.github.ClassSyncCSS.ClassSync.Domain;

import java.util.ArrayList;
import java.util.List;

public class Discipline {
    private String name;
    private List<Professor> courseProfs;
    private List<Professor> laboratoryProfs;

    public Discipline(String name, List<Professor> courseProfs, List<Professor> laboratoryProfs) {
        this.name = name;
        this.courseProfs = courseProfs;
        this.laboratoryProfs = laboratoryProfs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Professor> getCourseProfs() {
        return courseProfs != null ? courseProfs : new ArrayList<>();
    }

    public void setCourseProfs(List<Professor> courseProfs) {
        this.courseProfs = courseProfs;
    }

    public List<Professor> getLaboratoryProfs() {
        return laboratoryProfs != null ? laboratoryProfs : new ArrayList<>();
    }

    public void setLaboratoryProfs(List<Professor> laboratoryProfs) {
        this.laboratoryProfs = laboratoryProfs;
    }

    @Override
    public String toString() {
        return "Discipline{" +
                "name='" + name + '\'' +
//                ", courseProfs=" + courseProfs +
//                ", laboratoryProfs=" + laboratoryProfs +
                '}';
    }
}
