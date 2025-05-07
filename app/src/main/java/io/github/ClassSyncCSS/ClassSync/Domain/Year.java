package io.github.ClassSyncCSS.ClassSync.Domain;

import java.util.ArrayList;
import java.util.List;

public class Year {
    private String year;
    private List<Specialization> specializations;

    public Year(String year, List<Specialization> specializations) {
        this.year = year;
        this.specializations = specializations;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<Specialization> getSpecializations() {
        return specializations != null ? specializations : new ArrayList<Specialization>();
    }

    public void setSpecializations(List<Specialization> specializations) {
        this.specializations = specializations;
    }

    @Override
    public String toString() {
        return "Year{" +
                "year='" + year + '\'' +
                ", specializations=" + specializations +
                '}';
    }
}
