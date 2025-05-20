package io.github.ClassSyncCSS.ClassSync.Domain;

import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {

    @Test
    void defaultConstructorShouldSetName() {
        Room room = new Room();
        assertEquals("----", room.getName());
    }

    @Test
    void parameterizedConstructorShouldSetFields() {
        List<ActivityType> types = List.of(ActivityType.Lab);
        Room room = new Room("C210", types);

        assertEquals("C210", room.getName());
        assertEquals(types, room.getType());
    }

    @Test
    void getTypeShouldReturnEmptyIfNull() {
        Room room = new Room();
        room.setType(null);
        assertNotNull(room.getType());
        assertTrue(room.getType().isEmpty());
    }

    @Test
    void settersShouldWork() {
        Room room = new Room();
        room.setName("C308");
        assertEquals("C308", room.getName());
    }
}
