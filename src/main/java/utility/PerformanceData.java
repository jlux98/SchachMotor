package utility;

/**
 * Class holding some static fields used to store performance related data.
 */
public class PerformanceData {

    /**
     * time used by move-generation while searching for one move
     */
    public static long moveGenerationTime = 0;

    public static void resetMoveGenTime() {
        moveGenerationTime = 0;
    }
}
