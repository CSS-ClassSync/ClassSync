package io.github.ClassSyncCSS.ClassSync.Domain;

import java.util.Map;

public class Professor {
    private String name;
    private ProfType type;
    private Map<Discipline, Map<RoomType,Integer>> disciplines;

    public Professor(String name, ProfType type, Map<Discipline, Map<RoomType, Integer>> disciplines) {
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

    public Map<Discipline, Map<RoomType, Integer>> getDisciplines() {
        return disciplines;
    }

    public void setDisciplines(Map<Discipline, Map<RoomType, Integer>> disciplines) {
        this.disciplines = disciplines;
    }

}
