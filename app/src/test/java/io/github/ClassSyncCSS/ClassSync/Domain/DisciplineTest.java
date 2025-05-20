package io.github.ClassSyncCSS.ClassSync.Domain;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DisciplineTest {

    private Professor createDummyProfessor(String name) {
        return new Professor(name, ProfType.Course, new HashMap<>());
    }

    @Test
    void defaultConstructorShouldSetNameToDashes() {
        Discipline discipline = new Discipline();
        assertEquals("----", discipline.getName());
    }

    @Test
    void parameterizedConstructorShouldSetFieldsCorrectly() {
        Professor prof1 = createDummyProfessor("Professor1");
        Professor prof2 = createDummyProfessor("Professor2");

        List<Professor> courseProfs = Arrays.asList(prof1);
        List<Professor> labProfs = Arrays.asList(prof2);

        Discipline discipline = new Discipline("Software Quality", courseProfs, labProfs);

        assertEquals("Software Quality", discipline.getName());
        assertEquals(courseProfs, discipline.getCourseProfs());
        assertEquals(labProfs, discipline.getLaboratoryProfs());
    }

    @Test
    void getCourseProfsShouldReturnEmptyListWhenNull() {
        Discipline discipline = new Discipline();
        discipline.setCourseProfs(null);
        assertNotNull(discipline.getCourseProfs());
        assertTrue(discipline.getCourseProfs().isEmpty());
    }

    @Test
    void getLaboratoryProfsShouldReturnEmptyListWhenNull() {
        Discipline discipline = new Discipline();
        discipline.setLaboratoryProfs(null);
        assertNotNull(discipline.getLaboratoryProfs());
        assertTrue(discipline.getLaboratoryProfs().isEmpty());
    }

    @Test
    void settersAndGettersWorkCorrectly() {
        Discipline discipline = new Discipline();
        discipline.setName("EBS");

        Professor prof = createDummyProfessor("Professor");
        List<Professor> professors = Collections.singletonList(prof);

        discipline.setCourseProfs(professors);
        discipline.setLaboratoryProfs(professors);

        assertEquals("EBS", discipline.getName());
        assertEquals(professors, discipline.getCourseProfs());
        assertEquals(professors, discipline.getLaboratoryProfs());
    }
}
