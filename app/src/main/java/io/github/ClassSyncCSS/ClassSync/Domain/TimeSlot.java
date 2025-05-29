package io.github.ClassSyncCSS.ClassSync.Domain;

import java.util.Objects;

public class TimeSlot {
    private final int start;
    private final int end;

    // Class invariant
    private boolean classInvariant() {
        return start >= 8 && end > start && end <= 20;
    }

    public TimeSlot(int start, int end) {
        // Preconditions
        assert start >= 8 : "Precondition failed: start time must be >= 8";
        assert end > start : "Precondition failed: end time must be greater than start time";
        assert end <= 20 : "Precondition failed: end time must be <= 20";

        this.start = start;
        this.end = end;

        // Postconditions (represented by invariant for immutable object)
        assert classInvariant() : "Invariant failed after TimeSlot construction";
    }

    public int getStart() {
        assert classInvariant() : "Invariant failed before getStart";
        return start;
    }

    public int getEnd() {
        assert classInvariant() : "Invariant failed before getEnd";
        return end;
    }

    // Predefined static instances
    public static final TimeSlot EightToTen = new TimeSlot(8, 10);
    public static final TimeSlot TenToTwelve = new TimeSlot(10, 12);
    public static final TimeSlot TwelveToFourteen = new TimeSlot(12, 14);
    public static final TimeSlot FourteenToSixteen = new TimeSlot(14, 16);
    public static final TimeSlot SixteenToEighteen = new TimeSlot(16, 18);
    public static final TimeSlot EighteenToTwenty = new TimeSlot(18, 20);

    @Override
    public boolean equals(Object o) {
        assert classInvariant() : "Invariant failed before equals";
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeSlot timeSlot = (TimeSlot) o;
        return start == timeSlot.start && end == timeSlot.end;
    }

    @Override
    public int hashCode() {
        assert classInvariant() : "Invariant failed before hashCode";
        return Objects.hash(start, end);
    }
}