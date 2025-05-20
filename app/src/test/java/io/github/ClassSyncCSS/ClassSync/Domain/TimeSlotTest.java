package io.github.ClassSyncCSS.ClassSync.Domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TimeSlotTest {

    @Test
    void predefinedTimeSlotsShouldHaveCorrectValues() {
        assertEquals(8, TimeSlot.EightToTen.getStart());
        assertEquals(10, TimeSlot.EightToTen.getEnd());

        assertEquals(10, TimeSlot.TenToTwelve.getStart());
        assertEquals(12, TimeSlot.TenToTwelve.getEnd());

        assertEquals(12, TimeSlot.TwelveToFourteen.getStart());
        assertEquals(14, TimeSlot.TwelveToFourteen.getEnd());

        assertEquals(14, TimeSlot.FourteenToSixteen.getStart());
        assertEquals(16, TimeSlot.FourteenToSixteen.getEnd());

        assertEquals(16, TimeSlot.SixteenToEighteen.getStart());
        assertEquals(18, TimeSlot.SixteenToEighteen.getEnd());

        assertEquals(18, TimeSlot.EighteenToTwenty.getStart());
        assertEquals(20, TimeSlot.EighteenToTwenty.getEnd());
    }

    @Test
    void timeSlotsShouldBeEqualIfStartAndEndMatch() {
        TimeSlot slot1 = TimeSlot.EightToTen;
        TimeSlot slot2 = new TimeSlot(8, 10);

        assertEquals(slot1, slot2);
        assertEquals(slot1.hashCode(), slot2.hashCode());
    }

    @Test
    void timeSlotsShouldNotBeEqualIfStartOrEndDiffer() {
        TimeSlot slot1 = TimeSlot.EightToTen;
        TimeSlot slot2 = new TimeSlot(9, 11);

        assertNotEquals(slot1, slot2);
    }
}
