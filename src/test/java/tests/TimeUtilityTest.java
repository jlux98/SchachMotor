package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import utility.TimeUtility;

public class TimeUtilityTest {

    /**
     * Appends the unit "s" to the string.
     * Used to make tests easier adaptable in case the unit character
     * is removed or positioned differently.
     * @param seconds string to append "s" to
     * @return seconds + " s"
     */
    private static String addUnit(String seconds) {
        return seconds + " s";
    }

    @Test
    public void timeReturnsLambdaResultTest() {
        TimeUtility<Integer> timer = new TimeUtility<Integer>();
        assertEquals(7, timer.time(() -> 7));
    }

    @Test
    public void convertNanoLessThanOneSecondTest() {
        String secs = TimeUtility.nanoToSeconds(1L);
        assertEquals(addUnit("0.000000001"), secs);
    }

    @Test
    public void convertNanoMoreThanOneSecondTest() {
        String secs = TimeUtility.nanoToSeconds(112233445566778899L);
        assertEquals(addUnit("112233445.566778899"), secs);
    }

    @Test
    public void convertNanoOneDigitMoreThanOneSecondTest() {
        String secs = TimeUtility.nanoToSeconds(1234567891L);
        assertEquals(addUnit("1.234567891"), secs);
    }

    @Test
    public void convertNanoOneDigitLessThanOneSecondTest() {
        String secs = TimeUtility.nanoToSeconds(12345678L);
        assertEquals(addUnit("0.012345678"), secs);
    }

    @Test
    public void convertNanoMultipleOfOneSecondTest() {
        String secs = TimeUtility.nanoToSeconds(4000000000L);
        assertEquals(addUnit("4.000000000"), secs);
    }
}
