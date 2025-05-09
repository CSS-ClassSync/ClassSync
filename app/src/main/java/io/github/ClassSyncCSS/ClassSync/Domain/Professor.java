package io.github.ClassSyncCSS.ClassSync.Domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Professor {
    private String name;
    private ProfType type;
    private Map<Discipline, List<ActivityType>> disciplines;

    public Professor() {
        this.name = "----";
    }

    public Professor(String name, ProfType type, Map<Discipline, List<ActivityType>> disciplines) {
        this.name = name;
        this.type = type;
        this.disciplines = disciplines;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProfType getType() {
        return type;
    }

    public void setType(ProfType type) {
        this.type = type;
    }

    public Map<Discipline, List<ActivityType>> getDisciplines() {
        return disciplines != null ? disciplines : new HashMap<>();
    }

    public void setDisciplines(Map<Discipline, List<ActivityType>> disciplines) {
        this.disciplines = disciplines;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Professor professor = (Professor) o;
        return Objects.equals(name, professor.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
