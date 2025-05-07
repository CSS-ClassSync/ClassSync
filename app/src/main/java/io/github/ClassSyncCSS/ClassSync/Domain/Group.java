package io.github.ClassSyncCSS.ClassSync.Domain;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private String name;
    private Year year;
    private List<Discipline> disciplines;

    public Group(String name, Year year, List<Discipline> disciplines) {
        this.name = name;
        this.year = year;
        this.disciplines = disciplines;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Year getYear() {
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }

    public List<Discipline> getDisciplines() {
        return disciplines != null ? disciplines : new ArrayList<>();
    }

    public void setDisciplines(List<Discipline> disciplines) {
        this.disciplines = disciplines;
    }

    @Override
    public String toString() {
        return "Group{" +
                "name='" + name + '\'' +
                ", year=" + year.getYear() +
                '}';
    }
}
