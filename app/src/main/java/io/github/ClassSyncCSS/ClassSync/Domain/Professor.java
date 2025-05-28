package io.github.ClassSyncCSS.ClassSync.Domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Professor {
    private String name;
    private ProfType type;
    private Map<Discipline, List<ActivityType>> disciplines;

    // Class invariant method
    private boolean classInvariant() {
        return name != null && !name.isEmpty()
                && type != null
                && disciplines != null;
    }

     public Professor() {
         this.name = "----";
     }

    public Professor(String name, ProfType type, Map<Discipline, List<ActivityType>> disciplines) {
        assert name != null && !name.isEmpty() : "Precondition failed: name must not be null or empty";
        assert type != null : "Precondition failed: type must not be null";
        assert disciplines != null : "Precondition failed: disciplines must not be null";

        this.name = name;
        this.type = type;
        this.disciplines = disciplines;

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

    public ProfType getType() {
        assert classInvariant() : "Invariant failed before getType";
        return type;
    }

    public void setType(ProfType type) {
        assert type != null : "Precondition failed: type must not be null";
        this.type = type;
        assert classInvariant() : "Invariant failed after setType";
    }

    public Map<Discipline, List<ActivityType>> getDisciplines() {
        assert classInvariant() : "Invariant failed before getDisciplines";
        return disciplines != null ? disciplines : new HashMap<>();
    }

    public void setDisciplines(Map<Discipline, List<ActivityType>> disciplines) {
        assert disciplines != null : "Precondition failed: disciplines must not be null";
        this.disciplines = disciplines;
        assert classInvariant() : "Invariant failed after setDisciplines";
    }

    @Override
    public boolean equals(Object o) {
        assert classInvariant() : "Invariant failed before equals";
        if (o == null || getClass() != o.getClass()) return false;
        Professor professor = (Professor) o;
        return Objects.equals(name, professor.name);
    }

    @Override
    public int hashCode() {
        assert classInvariant() : "Invariant failed before hashCode";
        return Objects.hashCode(name);
    }
}