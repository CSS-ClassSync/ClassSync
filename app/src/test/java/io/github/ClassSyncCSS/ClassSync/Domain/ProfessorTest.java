package io.github.ClassSyncCSS.ClassSync.Domain;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ProfessorTest {

    @Test
    void defaultConstructorShouldSetNameToDashes() {
        Professor prof = new Professor();
        assertEquals("----", prof.getName());
    }

    @Test
    void parameterizedConstructorShouldSetFields() {
        Discipline d1 = new Discipline("Software Quality", null, null);
        List<ActivityType> activities = Arrays.asList(ActivityType.Course);

        Map<Discipline, List<ActivityType>> map = new HashMap<>();
        map.put(d1, activities);

        Professor prof = new Professor("Professor1", ProfType.Course, map);

        assertEquals("Professor1", prof.getName());
        assertEquals(ProfType.Course, prof.getType());
        assertEquals(map, prof.getDisciplines());
    }

    @Test
    void getDisciplinesShouldReturnEmptyMapIfNull() {
        Professor prof = new Professor();
        prof.setDisciplines(null);
        assertNotNull(prof.getDisciplines());
        assertTrue(prof.getDisciplines().isEmpty());
    }

    @Test
    void testEqualsAndHashCode() {
        Professor p1 = new Professor();
        p1.setName("Professor1");

        Professor p2 = new Professor();
        p2.setName("Professor1");

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void settersAndGettersShouldWork() {
        Professor prof = new Professor();
        prof.setName("Professor2");
        prof.setType(ProfType.Laboratory);

        Map<Discipline, List<ActivityType>> map = new HashMap<>();
        Discipline d = new Discipline("EBS", null, null);
        map.put(d, List.of(ActivityType.Lab));
        prof.setDisciplines(map);

        assertEquals("Professor2", prof.getName());
        assertEquals(ProfType.Laboratory, prof.getType());
        assertEquals(map, prof.getDisciplines());
    }
}
