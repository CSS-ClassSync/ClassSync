package io.github.ClassSyncCSS.ClassSync.Domain;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private String name;
    private Year year;
    private List<Discipline> disciplines;

    // Class Invariant method
    private boolean classInvariant() {
        return name != null && !name.isEmpty()
                && year != null
                && disciplines != null;
    }

     public Group() {
         this.name = "----";
     }

    public Group(String name, Year year, List<Discipline> disciplines) {
        assert name != null && !name.isEmpty() : "Precondition failed: name must not be null or empty";
        assert year != null : "Precondition failed: year must not be null";
        assert disciplines != null : "Precondition failed: disciplines must not be null";

        this.name = name;
        this.year = year;
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

    public Year getYear() {
        assert classInvariant() : "Invariant failed before getYear";
        return year;
    }

    public void setYear(Year year) {
        assert year != null : "Precondition failed: year must not be null";
        this.year = year;
        assert classInvariant() : "Invariant failed after setYear";
    }

    public List<Discipline> getDisciplines() {
        assert classInvariant() : "Invariant failed before getDisciplines";
        return disciplines != null ? disciplines : new ArrayList<>();
    }

    public void setDisciplines(List<Discipline> disciplines) {
        assert disciplines != null : "Precondition failed: disciplines must not be null";
        this.disciplines = disciplines;
        assert classInvariant() : "Invariant failed after setDisciplines";
    }

    @Override
    public String toString() {
        assert classInvariant() : "Invariant failed before toString";
        return "Group{" +
                "name='" + name + '\'' +
                ", year=" + year.getYear() +
                '}';
    }
}