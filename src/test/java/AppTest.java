import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

/**
 * Unit test for simple App.
 */
@Testable
public class AppTest {
    @Test
    public void shouldFail() {
        assertFalse(true);
    }

    @Test
    public void shouldSucceed() {
        assertTrue(true);
    }
}
