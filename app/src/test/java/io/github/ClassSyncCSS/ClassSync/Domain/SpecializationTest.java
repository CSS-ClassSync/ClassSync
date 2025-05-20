package io.github.ClassSyncCSS.ClassSync.Domain;

import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SpecializationTest {

    @Test
    void parameterizedConstructorShouldSetFields() {
        List<Group> groups = new ArrayList<>();
        List<Discipline> disciplines = new ArrayList<>();
        Specialization spec = new Specialization("MISS", groups, disciplines);

        assertEquals("MISS", spec.getName());
        assertEquals(groups, spec.getGroups());
        assertEquals(disciplines, spec.getDisciplines());
    }

    @Test
    void getGroupsShouldReturnEmptyIfNull() {
        Specialization spec = new Specialization("MISS", null, null);
        assertNotNull(spec.getGroups());
        assertTrue(spec.getGroups().isEmpty());
    }

    @Test
    void getDisciplinesShouldReturnEmptyIfNull() {
        Specialization spec = new Specialization("MISS", null, null);
        assertNotNull(spec.getDisciplines());
        assertTrue(spec.getDisciplines().isEmpty());
    }

    @Test
    void settersShouldWork() {
        Specialization spec = new Specialization("MISS", null, null);
        spec.setName("MSD");
        assertEquals("MSD", spec.getName());
    }
}

