package io.github.ClassSyncCSS.ClassSync.Domain;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private String name;
    private List<ActivityType> type;

    // Class Invariant method
    private boolean classInvariant() {
        return name != null && !name.isEmpty()
                && type != null;
    }

     public Room() {
         this.name = "----";
     }

    public Room(String name, List<ActivityType> type) {
        assert name != null && !name.isEmpty() : "Precondition failed: name must not be null or empty";
        assert type != null : "Precondition failed: type list must not be null";

        this.name = name;
        this.type = type;

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

    public List<ActivityType> getType() {
        assert classInvariant() : "Invariant failed before getType";
        return type != null ? type : new ArrayList<>();
    }

    public void setType(List<ActivityType> type) {
        assert type != null : "Precondition failed: type list must not be null";
        this.type = type;
        assert classInvariant() : "Invariant failed after setType";
    }

    @Override
    public String toString() {
        assert classInvariant() : "Invariant failed before toString";
        return "Room{" +
                "name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
