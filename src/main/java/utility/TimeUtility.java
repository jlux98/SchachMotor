package utility;

import java.util.function.Supplier;

/**
 * Class used to time the execution of code.
 * Use {@link #time(Supplier)} to time the execution of code that returns a result.
 */
public class TimeUtility<T> {

    private long elapsedTime = -1;
    /**
     * conversion multiplier: seconds * SECOND_TO_NANO = nanoseconds
     */
    public static final  long SECOND_TO_NANO = 1_000_000_000;

    /**
     * Converts nanoseconds stored as long to seconds represented as String.
     * Adds a decimal point and leading 0's (as required).
     * @param nanoSeconds the duration in nano seconds that should be converted to seconds
     * @return the String representation of the time in seconds
     */
    public static String nanoToSeconds(long nanoSeconds) {
        String time = Long.toString(nanoSeconds);
        StringBuilder builder = new StringBuilder(time);
        if (time.length() <= 9) {
            //leading 0 and point

            for (int i = 0; i < 9 - time.length(); i++) {
                builder.insert(0, "0"); //prepend 0's
            }
            builder.insert(0, "0."); //prepend one 0 and a point

        } else {
            //reading right to left, insert point after 9 digits
            builder.insert(time.length() - 9, ".");
        }
        builder.append(" s"); //append unit "s" to the result
        return builder.toString();
    }

    /**
     * Executes the provided code (Supplier) and returns its result.
     * Measures the time the supplier takes to execute.
     * Time can be retrieved by {@link #getElapsedTime()}.
     * Calling this method will overwrite any previous measurements.
     * @param supplier code that should be timed
     * @return the passed code's result
     */
    public T time(Supplier<T> supplier) {
        resetElapsedTime();

        long startingTime = System.nanoTime();
        T result = supplier.get(); //execute passed code
        long endingTime = System.nanoTime();

        //save rough time spent calculating
        elapsedTime = Math.abs(endingTime - startingTime);
        return result;
        //works for negative and positive numbers
        //(System.nanoTime can be negative = relative to a future time)
        //start = 2, end = 5:    5 - 2 = 3
        //start = -5, end = -2: -2 --5 = 3
        //start = -3, end = 2: -3 -2 = -5 -> abs()
    }

    /**
     * @return the time measured by the last invocation of {@link #time(Supplier)}
     * in nanoseconds.
     */
    public long getElapsedTime() {
        return elapsedTime;
    }

    /**
     * Converts the result of the last invocation of {@link #time(Supplier)}
     * to seconds.
     * @return a string representing the elapsed time in seconds
     */
    public String getElapsedSecondsAsString() {
        return nanoToSeconds(getElapsedTime());
    }

    /**
     * Resets the time stored from last execution of
     * {@link #time(Supplier)}.
     */
    public void resetElapsedTime() {
        elapsedTime = -1;
    }

}
