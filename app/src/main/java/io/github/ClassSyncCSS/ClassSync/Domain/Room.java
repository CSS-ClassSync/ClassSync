package io.github.ClassSyncCSS.ClassSync.Domain;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private String name;
    private List<ActivityType> type;

    public Room() {
        this.name = "----";
    }

    public Room(String name, List<ActivityType> type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ActivityType> getType() {
        return type != null ? type : new ArrayList<>();
    }

    public void setType(List<ActivityType> type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Room{" +
                "name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
