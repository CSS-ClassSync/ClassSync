package io.github.ClassSyncCSS.ClassSync.Domain;

import java.util.List;

public class Year {
    private int year;
    private List<Specialization> specializations;

    public Year(int year, List<Specialization> specializations) {
        this.year = year;
        this.specializations = specializations;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<Specialization> getSpecializations() {
        return specializations;
    }

    public void setSpecializations(List<Specialization> specializations) {
        this.specializations = specializations;
    }
}
