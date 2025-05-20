package io.github.ClassSyncCSS.ClassSync.Domain;

import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GroupTest {

    @Test
    void defaultConstructorShouldWork() {
        Group instance = new Group();
        assertNotNull(instance);
        assertEquals("----", instance.getName());
    }

    @Test
    void gettersShouldReturnDefaultsIfNull() {
        Group instance = new Group();
        instance.setDisciplines(null);
        assertNotNull(instance.getDisciplines());
        assertTrue(instance.getDisciplines().isEmpty());
    }

    @Test
    void parameterizedConstructorShouldSetValues() {
        Year year = new Year("1", new ArrayList<>());
        Discipline discipline = new Discipline("Software Quality", null, null);
        List<Discipline> disciplines = List.of(discipline);
        Group group = new Group("MISS1", year, disciplines);

        assertEquals("MISS1", group.getName());
        assertEquals(year, group.getYear());
        assertEquals(disciplines, group.getDisciplines());
    }

    @Test
    void settersShouldStoreValuesCorrectly() {
        Group group = new Group();
        group.setName("MISS2");
        assertEquals("MISS2", group.getName());
    }
}
