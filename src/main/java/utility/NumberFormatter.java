package utility;

import java.text.NumberFormat;

/**
 * Class using NumberFormat to format numbers for output.
 */
public class NumberFormatter {
    private static NumberFormat formatter = NumberFormat.getInstance();

    /**
     * Format the passed number to include dots or commas for better readability.
     * @param number
     * @return the formatted representation as string
     */
    public static String format(long number) {
        return formatter.format(number);
    }
}
