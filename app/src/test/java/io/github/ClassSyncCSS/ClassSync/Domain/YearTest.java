package io.github.ClassSyncCSS.ClassSync.Domain;

import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class YearTest {

    @Test
    void parameterizedConstructorShouldSetValues() {
        List<Specialization> specs = new ArrayList<>();
        Year year = new Year("1", specs);

        assertEquals("1", year.getYear());
        assertEquals(specs, year.getSpecializations());
    }

    @Test
    void getSpecializationsShouldReturnEmptyIfNull() {
        Year year = new Year("1", null);
        assertNotNull(year.getSpecializations());
        assertTrue(year.getSpecializations().isEmpty());
    }

    @Test
    void settersShouldWork() {
        Year year = new Year("1", null);
        year.setYear("2");
        assertEquals("2", year.getYear());
    }
}
