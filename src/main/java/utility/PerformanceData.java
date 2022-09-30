package utility;

/**
 * Class holding static fields used in performance evaluation.
 */
public class PerformanceData {
    public static long moveGenerationTime = 0;
    public static long attackMapGenerationTime = 0;
    public static int getOrComputeStaticValueCalls = 0;
    public static int getOrComputeLeafValueCalls = 0;
    public static int staticValueComputations = 0;
    public static int leafValueComputations = 0;
    public static int ascendingComparisons = 0;
    public static int descendingComparisons = 0;
}
