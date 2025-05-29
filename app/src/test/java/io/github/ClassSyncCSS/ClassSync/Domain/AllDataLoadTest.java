package io.github.ClassSyncCSS.ClassSync.Domain;

import io.github.ClassSyncCSS.ClassSync.Domain.AllData;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class AllDataLoadTest {

    @TempDir
    Path tempDir;

    Path resourcesDir;
    Path originalResourceDir = Path.of("src/main/resources");
    Path backupDir;

    @BeforeEach
    public void setup() throws IOException {
        // Backup original files
        backupDir = tempDir.resolve("backup");
        Files.createDirectory(backupDir);

        try (Stream<Path> files = Files.list(originalResourceDir)) {
            for (Path file : files.collect(Collectors.toList())) {
                Files.copy(file, backupDir.resolve(file.getFileName()), StandardCopyOption.REPLACE_EXISTING);
            }
        }

        // Replace with test files
        resourcesDir = originalResourceDir;

        writeCsv("ani.csv", List.of(
                "id,an,spec,type",
                "TESTAN001,1,Test Spec,Licenta",
                "TESTAN002,2,Test Spec,Licenta"
        ));

        writeCsv("grupe.csv", List.of(
                "id,grupa,an",
                "GRP001,0,TESTAN001",
                "GRP002,1,TESTAN002"
        ));

        writeCsv("profesori.csv", List.of(
                "id,name,rol",
                "PROF001,Prof One,Full",
                "PROF002,Prof Two,Assoc"
        ));

        writeCsv("materii.csv", List.of(
                "id,name,tip",
                "MAT001,Algorithms,Curs",
                "MAT002,Data Structures,Laborator",
                "MAT003,Testing,Seminar"
        ));

        writeCsv("sali.csv", List.of(
                "name,tip",
                "Room A,Curs",
                "Room B,Laborator",
                "Room B,Seminar"
        ));

        writeCsv("materii_an.csv", List.of(
                "materie,an",
                "MAT001,TESTAN001",
                "MAT002,TESTAN002",
                "MAT003,TESTAN001"
        ));

        writeCsv("profesori_materii.csv", List.of(
                "profesor,materie",
                "PROF001,MAT001",
                "PROF002,MAT002",
                "PROF001,MAT003"
        ));
    }

    @AfterEach
    public void teardown() throws IOException {
        // Delete test files
        try (Stream<Path> files = Files.list(resourcesDir)) {
            for (Path file : files.collect(Collectors.toList())) {
                Files.delete(file);
            }
        }
        // Restore original files
        try (Stream<Path> files = Files.list(backupDir)) {
            for (Path file : files.collect(Collectors.toList())) {
                Files.copy(file, resourcesDir.resolve(file.getFileName()), StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    private void writeCsv(String fileName, List<String> lines) throws IOException {
        Path filePath = resourcesDir.resolve(fileName);
        Files.write(filePath, lines);
    }

    @Test
    public void testLoadAllDataFromValidCSVs() {
        AllData data = AllData.load();

        assertEquals(2, data.getYears().size());
        assertEquals(2, data.getGroups().size());
        assertEquals(2, data.getProfessors().size());
        assertEquals(3, data.getDisciplines().size());
        assertEquals(2, data.getRooms().size());
    }

    @Test
    public void testLoadFailsWithMissingFile() throws IOException {
        Files.delete(resourcesDir.resolve("grupe.csv"));
        Exception exception = assertThrows(RuntimeException.class, AllData::load);
        assertTrue(exception.getMessage().contains("grupe.csv"));
    }

    @Test
    public void testLoadFailsWithWrongDelimiter() throws IOException {
        writeCsv("materii.csv", List.of(
                "id;name;tip",
                "MAT001;Algorithms;Curs"
        ));

        Exception exception = assertThrows(RuntimeException.class, AllData::load);
        assertNotNull(exception);
    }

    @Test
    public void testLoadHandlesEmptyFiles() throws IOException {
        writeCsv("sali.csv", List.of("name,tip"));

        AllData data = AllData.load();
        assertEquals(0, data.getRooms().size());
    }

    @Test
    public void testLoadHandlesIncompleteRows() throws IOException {
        writeCsv("materii.csv", List.of(
                "id,name,tip",
                "MAT001,Algorithms",
                "MAT002,Data Structures,Laborator"
        ));

        Exception exception = assertThrows(IndexOutOfBoundsException.class, AllData::load);
        assertNotNull(exception);
    }

    @Test
    public void testLoadFailsWithDuplicateIDs() throws IOException {
        writeCsv("grupe.csv", List.of(
                "id,grupa,an",
                "GRP001,0,TESTAN001",
                "GRP001,1,TESTAN002"
        ));

        Exception exception = assertThrows(RuntimeException.class, AllData::load);
        assertTrue(exception.getMessage().toLowerCase().contains("duplicate"));
    }

   @Test
   public void testLoadFailsWithOrphanReferences() throws IOException {
       writeCsv("materii_an.csv", List.of(
               "materie,an",
               "NON_EXISTENT_ID,TESTAN001"
       ));

       Exception exception = assertThrows(RuntimeException.class, AllData::load);
       assertTrue(exception.getMessage().toLowerCase().contains("not found"));
   }

    @Test
    public void testLoadWithManyValidRows() throws IOException {
        List<String> lines = Stream.concat(
                Stream.of("id,name,tip"),
                IntStream.range(0, 1000).mapToObj(i -> "MAT" + i + ",Subject" + i + ",Curs")
        ).collect(Collectors.toList());

        writeCsv("materii.csv", lines);
        AllData data = AllData.load();
        assertEquals(1000, data.getDisciplines().size());
    }

   @Test
   public void testLoadFailsWithIncorrectHeader() throws IOException {
       writeCsv("materii.csv", List.of(
               "identifier,name,tip",
               "MAT001,Algorithms,Curs"
       ));

       Exception exception = assertThrows(RuntimeException.class, AllData::load);
       assertTrue(exception.getMessage().toLowerCase().contains("header"));
   }

    @Test
    public void testGetSpecializations() {
        AllData data = AllData.load();
        List<String> expectedSpecializations = List.of("Test Spec");
        List<String> actualSpecializations = data.getSpecializations().stream().map(Specialization::getName).toList();

        assertEquals(expectedSpecializations.size(), actualSpecializations.size());
        assertTrue(actualSpecializations.containsAll(expectedSpecializations));
    }

    @Test
    public void testToString() {
        AllData data = AllData.load();
        String expectedString = "AllData{" +
                "professors=" + data.getProfessors() +
                ", disciplines=" + data.getDisciplines() +
                ", specializations=" + data.getSpecializations() +
                ", years=" + data.getYears() +
                ", groups=" + data.getGroups() +
                ", rooms=" + data.getRooms() +
                '}';
        assertEquals(expectedString, data.toString());
    }
}
