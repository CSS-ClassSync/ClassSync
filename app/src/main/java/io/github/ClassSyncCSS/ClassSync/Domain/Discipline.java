package io.github.ClassSyncCSS.ClassSync.Domain;

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
        return courseProfs;
    }

    public void setCourseProfs(List<Professor> courseProfs) {
        this.courseProfs = courseProfs;
    }

    public List<Professor> getLaboratoryProfs() {
        return laboratoryProfs;
    }

    public void setLaboratoryProfs(List<Professor> laboratoryProfs) {
        this.laboratoryProfs = laboratoryProfs;
    }
}
