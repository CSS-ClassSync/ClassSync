package io.github.ClassSyncCSS.ClassSync.Domain;

import java.util.Objects;

public class TimeSlot {
    private int start;
    private int end;

    private TimeSlot(int start, int end) {
        this.start = start;
        this.end = end;
    }

    static public TimeSlot EightToTen = new TimeSlot(8,10);
    static public TimeSlot TenToTwelve = new TimeSlot(10,12);
    static public TimeSlot TwelveToFourteen = new TimeSlot(12,14);
    static public TimeSlot FourteenToSixteen = new TimeSlot(14,16);
    static public TimeSlot SixteenToEighteen = new TimeSlot(16,18);
    static public TimeSlot EighteenToTwenty = new TimeSlot(18,20);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeSlot timeSlot = (TimeSlot) o;
        return start == timeSlot.start && end == timeSlot.end;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
}


