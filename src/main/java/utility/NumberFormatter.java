package utility;

import java.text.NumberFormat;

public class NumberFormatter {
    private static NumberFormat formatter = NumberFormat.getInstance();

    public static String format(long l) {
        return formatter.format(l);
    }
}
