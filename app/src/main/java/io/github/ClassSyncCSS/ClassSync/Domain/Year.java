package io.github.ClassSyncCSS.ClassSync.Domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class Year {
    private String year;
    
    @JsonIgnore
    private List<Specialization> specializations;

    // Class invariant method
    private boolean classInvariant() {
        return year != null && !year.isEmpty()
                && year.matches("[1-3]")  
                && specializations != null;
    }

    public Year(String year, List<Specialization> specializations) {
        assert year != null && !year.isEmpty() : "Precondition failed: year must not be null or empty";
        assert year.matches("[1-3]") : "Precondition failed: year must be '1', '2', or '3'";
        assert specializations != null : "Precondition failed: specializations list must not be null";

        this.year = year;
        this.specializations = specializations;

        assert classInvariant() : "Invariant failed after constructor";
    }

    public String getYear() {
        assert classInvariant() : "Invariant failed before getYear";
        return year;
    }

    public void setYear(String year) {
        assert year != null && !year.isEmpty() : "Precondition failed: year must not be null or empty";
        this.year = year;
        assert classInvariant() : "Invariant failed after setYear";
    }

    public List<Specialization> getSpecializations() {
        assert classInvariant() : "Invariant failed before getSpecializations";
        return specializations != null ? specializations : new ArrayList<>();
    }

    public void setSpecializations(List<Specialization> specializations) {
        assert specializations != null : "Precondition failed: specializations list must not be null";
        this.specializations = specializations;
        assert classInvariant() : "Invariant failed after setSpecializations";
    }

    @Override
    public String toString() {
        assert classInvariant() : "Invariant failed before toString";
        return "Year{" +
                "year='" + year + '\'' +
                ", specializations=" + specializations +
                '}';
    }
}